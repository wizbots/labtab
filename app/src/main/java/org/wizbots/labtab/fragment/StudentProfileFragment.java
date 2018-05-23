package org.wizbots.labtab.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.StudentStatsAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.database.StudentStatsTable;
import org.wizbots.labtab.database.StudentsProfileTable;
import org.wizbots.labtab.interfaces.StudentStatsAdapterClickListener;
import org.wizbots.labtab.interfaces.requesters.AddWizchipsListener;
import org.wizbots.labtab.interfaces.requesters.GetStudentProfileAndStatsListener;
import org.wizbots.labtab.interfaces.requesters.WithdrawWizchipsListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.student.StudentProfile;
import org.wizbots.labtab.model.student.StudentStats;
import org.wizbots.labtab.model.student.response.CreatorResponse;
import org.wizbots.labtab.model.student.response.StudentResponse;
import org.wizbots.labtab.requesters.AddWizchipsRequester;
import org.wizbots.labtab.requesters.StudentProfileAndStatsRequester;
import org.wizbots.labtab.requesters.WithdrawWizchipsRequester;
import org.wizbots.labtab.util.BackgroundExecutor;
import org.wizbots.labtab.util.LabTabUtil;
import org.wizbots.labtab.util.StudentStatsComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentProfileFragment extends ParentFragment implements View.OnClickListener, StudentStatsAdapterClickListener, GetStudentProfileAndStatsListener, WithdrawWizchipsListener, AddWizchipsListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private StudentStatsAdapter studentStatsAdapter;
    private RecyclerView recyclerViewStudentStats;
    private ArrayList<StudentStats> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;
    private Program program;
    private Student student;
    private ProgramOrLab programOrLab;
    private ProgressDialog progressDialog;
    private ImageView labLevelImageView;
    private TextViewCustom firstNameTextViewCustom, lastNameTextViewCustom,
            emailTextViewCustom, gradeTextViewCustom, dateOfBirthTextViewCustom, parentsPhoneTextViewCustom,
            allergiesSpecialNeedsTextViewCustom, afterCarePhoneNameTextViewCustom, enrollmentsCountTextViewCustom,
            absencesTextViewCustom, wizchipsTextViewCustom;
    private TextView addWizchips, withdrawWizchips;
    private String labLevel = "";

    public StudentProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_student_profile, container, false);
        homeActivityContext = (HomeActivity) context;
        programOrLab = getArguments().getParcelable(LabListFragment.LAB);
        program = getArguments().getParcelable(LabDetailsFragment.PROGRAM);
        student = getArguments().getParcelable(LabDetailsFragment.STUDENT);
        labLevel = getArguments().getString(LabDetailsFragment.LAB_LEVEL);
        initView();
        initListeners();
//        prepareDummyList();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return StudentProfileFragment.class.getSimpleName();
    }

    public void initView() {
        progressDialog = new ProgressDialog(homeActivityContext);
        progressDialog.setMessage("processing");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.STUDENT_PROFILE);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

        recyclerViewStudentStats = (RecyclerView) rootView.findViewById(R.id.recycler_view_student_stats_list);
        objectArrayList = new ArrayList<>();

        studentStatsAdapter = new StudentStatsAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewStudentStats.setLayoutManager(mLayoutManager);
        recyclerViewStudentStats.setItemAnimator(new DefaultItemAnimator());
        recyclerViewStudentStats.setAdapter(studentStatsAdapter);
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());


        labLevelImageView = (ImageView) rootView.findViewById(R.id.iv_student_lab_level);

        firstNameTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_first_name);
        lastNameTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_last_name);
        emailTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_email);
        gradeTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_grade);
        dateOfBirthTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_dob_age);
        parentsPhoneTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_parents_phone);
        allergiesSpecialNeedsTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_allergies_special_needs);
        afterCarePhoneNameTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_after_care_phone_name);
        enrollmentsCountTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_enrollments_count);
        absencesTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_abscences_count);
        wizchipsTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_wizchips);
        addWizchips = (TextView) rootView.findViewById(R.id.tv_add_wizchip);
        withdrawWizchips = (TextView) rootView.findViewById(R.id.tv_remove_wizchip);
        withdrawWizchips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Student> studentList = new ArrayList<>();
                studentList.add(student);
                List<String> selectted = new ArrayList<>();

                if (studentList.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.SELECT_STUDENT_FIRST_DECREMENT);
                } /*else if (studentList.size() > 1) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.SELECT_STUDENT_FIRST_DECREMENT);
                }*/ else {
                    Student student = studentList.get(0);
                    if (getChips(student.getWizchips(), student.getOfflinewizchips()) <= 0) {
                        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.ALREADY_MINIMUM_WIZCHIPS);
                    } else {
                        progressDialog.show();
                    }
                    for (Student student1 : studentList) {
                        selectted.add(student1.getStudent_id());
                    }
                    progressDialog.show();
                    //   BackgroundExecutor.getInstance().execute(new AddWizchipsRequester(programOrLab.getId(), selectted, 1));
                    BackgroundExecutor.getInstance().execute(new WithdrawWizchipsRequester(programOrLab.getId(), selectted, 1));
                }

            }
        });
        addWizchips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Student> studentList = new ArrayList<>();
                studentList.add(student);
                if (studentList.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.SELECT_STUDENT_FIRST_INCREMENT);
                } /*else if (studentList.size() > 1) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.SELECT_STUDENT_FIRST_INCREMENT);
                }*/ else {
                    Student student = studentList.get(0);
                    List<String> selectted = new ArrayList<>();
                    for (Student student1 : studentList) {
                        selectted.add(student1.getStudent_id());
                    }
                    progressDialog.show();
                    BackgroundExecutor.getInstance().execute(new AddWizchipsRequester(programOrLab.getId(), selectted, 1));
                }
            }
        });

        StudentProfile studentProfile = StudentsProfileTable.getInstance().getStudentProfileById(student.getStudent_id());
        if (studentProfile != null) {
            progressDialog.dismiss();
            setProfileDetails(studentProfile);
            prepareStudentStats(StudentStatsTable.getInstance().getStudentStatsById(student.getStudent_id()));
        } else {
            BackgroundExecutor.getInstance().execute(new StudentProfileAndStatsRequester(student.getStudent_id()));
        }
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onActionViewClick(String level) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
        bundle.putParcelable(LabListFragment.LAB, programOrLab);
        bundle.putParcelable(LabDetailsFragment.STUDENT, student);
        bundle.putString(StudentLabDetailsFragment.LEVEL, level);
        bundle.putString(LabDetailsFragment.LAB_LEVEL, labLevel);
        homeActivityContext.replaceFragment(Fragments.STUDENT_STATS_DETAILS, bundle);
    }

    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(GetStudentProfileAndStatsListener.class, this);
        LabTabApplication.getInstance().addUIListener(WithdrawWizchipsListener.class, this);
        LabTabApplication.getInstance().addUIListener(AddWizchipsListener.class, this);
    }

    @Override
    public void onDestroy() {
        LabTabApplication.getInstance().addUIListener(GetStudentProfileAndStatsListener.class, this);
        super.onDestroy();
    }

    @Override
    public void studentProfileFetchedSuccessfully(StudentResponse studentResponse, final StudentProfile studentProfile, final ArrayList<StudentStats> statsArrayList) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                setProfileDetails(studentProfile);
                prepareStudentStats(statsArrayList);
            }
        });
    }

    @Override
    public void unableToFetchStudent(int responseCode) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
        if (responseCode == StatusCode.NOT_FOUND) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_LAB_FOUND);
        } else {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_INTERNET_CONNECTION);
        }
    }

    @Override
    public void offlineNoData() {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_DATA_NO_CONNECTION);
            }
        });
    }

    private void setProfileDetails(StudentProfile studentProfile) {
        LabTabUtil.setBackGroundImageResource(student.getLevel(), labLevelImageView);
        CreatorResponse creatorResponse = (CreatorResponse) LabTabUtil.fromJson(studentProfile.getCreator(), CreatorResponse.class);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(studentProfile.getFirst_name() + " " + studentProfile.getLast_name());

        firstNameTextViewCustom.setText(studentProfile.getFirst_name());
        lastNameTextViewCustom.setText(studentProfile.getLast_name());
        emailTextViewCustom.setText(creatorResponse.getEmail());
        gradeTextViewCustom.setText(studentProfile.getGrade());
        String dob[] = studentProfile.getDate_of_birth().split(" ");
        dateOfBirthTextViewCustom.setText(dob[0]);
//        parentsPhoneTextViewCustom.setText(creatorResponse.getPhone_1() != null ? creatorResponse.getPhone_1() : "");
//        allergiesSpecialNeedsTextViewCustom.setText(studentProfile.getAllergies() != null ? studentProfile.getAllergies() : "");
//        afterCarePhoneNameTextViewCustom.setText(studentProfile.getAfter_care_phone() != null ? studentProfile.getAfter_care_phone() : "");
        enrollmentsCountTextViewCustom.setText(studentProfile.getEnrollments_count());
        absencesTextViewCustom.setText(studentProfile.getAbsence_count());
        wizchipsTextViewCustom.setText(String.valueOf(student.getWizchips() + student.getOfflinewizchips()));


        parentsPhoneTextViewCustom.setText(creatorResponse.parentsPhone());

        allergiesSpecialNeedsTextViewCustom.setText(studentProfile.getAllergies());

        afterCarePhoneNameTextViewCustom.setText(studentProfile.getAfter_care_after() != null ? studentProfile.getAfter_care_after() : "");

    }

    private void prepareStudentStats(ArrayList<StudentStats> studentStatsArrayList) {

        objectArrayList.addAll(studentStatsArrayList);
        Collections.sort(objectArrayList, StudentStatsComparator.getCompByName());
        studentStatsAdapter.notifyDataSetChanged();
    }

    private int getChips(int onlineChips, int offlineChips) {
        return (onlineChips + offlineChips) > 0 ? (onlineChips + offlineChips) : 0;
    }

    @Override
    public void onWithdrawWizchipsSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                wizchipsTextViewCustom.setText(String.valueOf(Integer.parseInt(wizchipsTextViewCustom.getText().toString()) - 1));
            }
        });
    }

    @Override
    public void onWithdrawWizchipsError() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                wizchipsTextViewCustom.setText(String.valueOf(Integer.parseInt(wizchipsTextViewCustom.getText().toString()) - 1));
            }
        });
    }

    @Override
    public void onAddWizchipsSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                wizchipsTextViewCustom.setText(String.valueOf(Integer.parseInt(wizchipsTextViewCustom.getText().toString()) + 1));
            }
        });
    }

    @Override
    public void onAddWizchipsError() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                wizchipsTextViewCustom.setText(String.valueOf(Integer.parseInt(wizchipsTextViewCustom.getText().toString()) + 1));
            }
        });
    }
}
