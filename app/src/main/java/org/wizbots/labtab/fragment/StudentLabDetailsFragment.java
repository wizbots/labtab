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

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.StudentLabDetailsAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.database.StudentStatsTable;
import org.wizbots.labtab.database.StudentsProfileTable;
import org.wizbots.labtab.interfaces.StudentLabDetailsAdapterClickListener;
import org.wizbots.labtab.interfaces.StudentTypeInterface;
import org.wizbots.labtab.interfaces.requesters.GetStudentProfileAndStatsListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.student.StudentLabDetailsType1;
import org.wizbots.labtab.model.student.StudentLabDetailsType2;
import org.wizbots.labtab.model.student.StudentProfile;
import org.wizbots.labtab.model.student.StudentStats;
import org.wizbots.labtab.model.student.response.StudentResponse;
import org.wizbots.labtab.requesters.StudentProfileAndStatsRequester;
import org.wizbots.labtab.util.BackgroundExecutor;
import org.wizbots.labtab.util.LabTabUtil;
import org.wizbots.labtab.util.LevelComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class StudentLabDetailsFragment extends ParentFragment implements View.OnClickListener, StudentLabDetailsAdapterClickListener, GetStudentProfileAndStatsListener {

    public static final String LEVEL = "LEVEL";
    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private StudentLabDetailsAdapter studentLabDetailsAdapter;
    private RecyclerView recyclerViewStudentLabDetails;
    private ArrayList<StudentTypeInterface> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;
    private Program program;
    private Student student;
    private ProgramOrLab programOrLab;
    private ProgressDialog progressDialog;
    private TextViewCustom labSKUTextViewCustom, availabilityTextViewCustom,
            nameTextViewCustom, locationTextViewCustom, categoryTextViewCustom,
            roomTextViewCustom, gradesTextViewCustom, priceTextViewCustom,
            fromTextViewCustom, toTextViewCustom, timeSlotTextViewCustom, dayTextViewCustom;
    private String labLevel = "";

    public StudentLabDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_student_lab_details, container, false);
        homeActivityContext = (HomeActivity) context;
        programOrLab = getArguments().getParcelable(LabListFragment.LAB);
        program = getArguments().getParcelable(LabDetailsFragment.PROGRAM);
        student = getArguments().getParcelable(LabDetailsFragment.STUDENT);
        labLevel = getArguments().getString(LabDetailsFragment.LAB_LEVEL);
        initView();
        initListeners();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return LabDetailsFragment.class.getSimpleName();
    }

    public void initView() {
        progressDialog = new ProgressDialog(homeActivityContext);
        progressDialog.setMessage("processing");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.STUDENT_LAB_DETAILS);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
        initHeaderView();

        recyclerViewStudentLabDetails = (RecyclerView) rootView.findViewById(R.id.recycler_view_student_lab_details);
//        recyclerViewStudentLabDetails.setFocusable(false);
        objectArrayList = new ArrayList<>();

        studentLabDetailsAdapter = new StudentLabDetailsAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewStudentLabDetails.setLayoutManager(mLayoutManager);
        recyclerViewStudentLabDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewStudentLabDetails.setAdapter(studentLabDetailsAdapter);
        rootView.findViewById(R.id.btn_absences).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
                bundle.putString(LabDetailsFragment.LAB_LEVEL, labLevel);
                homeActivityContext.replaceFragment(Fragments.LIST_OF_SKIPS, bundle);
            }
        });
        rootView.findViewById(R.id.btn_additional).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
                bundle.putString(LabDetailsFragment.LAB_LEVEL, labLevel);
                homeActivityContext.replaceFragment(Fragments.ADDITIONAL_INFORMATION, bundle);
            }
        });
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());

        if (program != null) {
            setHeaderView(program);
        }

        ArrayList<StudentStats> statsArrayList = StudentStatsTable.getInstance().getStudentStatsById(student.getStudent_id());

        if (statsArrayList.isEmpty()) {
            BackgroundExecutor.getInstance().execute(new StudentProfileAndStatsRequester(student.getStudent_id()));
        } else {
            prepareStudentStats(statsArrayList, StudentsProfileTable.getInstance().getStudentProfileById(student.getStudent_id()));
            progressDialog.dismiss();
        }

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onViewTypeClick1() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(LabListFragment.LAB, programOrLab);
        bundle.putString(LabDetailsFragment.LAB_LEVEL, labLevel);
        homeActivityContext.replaceFragment(Fragments.LAB_DETAILS_LIST, bundle);
    }

    @Override
    public void onViewTypeClick2(String level) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
        bundle.putParcelable(LabListFragment.LAB, programOrLab);
        bundle.putParcelable(LabDetailsFragment.STUDENT, student);
        bundle.putString(LEVEL, level);
        bundle.putString(LabDetailsFragment.LAB_LEVEL, labLevel);
        homeActivityContext.replaceFragment(Fragments.STUDENT_STATS_DETAILS, bundle);
    }

    public void initHeaderView() {
        labSKUTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_lab_sku);
        availabilityTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_availability);
        nameTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_name);
        locationTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_location);
        categoryTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_category);
        roomTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_room);
        gradesTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_grades);
        priceTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_price);
        fromTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_from);
        toTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_to);
        timeSlotTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_time_slot);
        dayTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_day);

        labSKUTextViewCustom.setText("----");
        availabilityTextViewCustom.setText("----");
        nameTextViewCustom.setText("----");
        locationTextViewCustom.setText("----");
        categoryTextViewCustom.setText("----");
        roomTextViewCustom.setText("----");
        gradesTextViewCustom.setText("----");
        priceTextViewCustom.setText("----");
        fromTextViewCustom.setText("----");
        toTextViewCustom.setText("----");
        timeSlotTextViewCustom.setText("----");
        dayTextViewCustom.setText("----");
    }

    public void setHeaderView(Program program) {
        labSKUTextViewCustom.setText(String.valueOf(program.getSku()));
        availabilityTextViewCustom.setText(program.getAvailability());
        nameTextViewCustom.setText(program.getName());
        locationTextViewCustom.setText(program.getLocation());
        categoryTextViewCustom.setText(program.getCategory());
        roomTextViewCustom.setText(program.getRoom());
        gradesTextViewCustom.setText(program.getGrades());
        priceTextViewCustom.setText(program.getPrice());
        fromTextViewCustom.setText(program.getStarts());
        toTextViewCustom.setText(program.getEnds());
        timeSlotTextViewCustom.setText(program.getTime_slot());
        dayTextViewCustom.setText(LabTabUtil.getFormattedDate(DateFormat.DEFAULT, new Date()));
    }


    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(GetStudentProfileAndStatsListener.class, this);
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
                prepareStudentStats(statsArrayList, studentProfile);
                progressDialog.dismiss();
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

    private void prepareStudentStats(ArrayList<StudentStats> studentStatsArrayList, StudentProfile studentProfile) {
        LabTabApplication.getInstance().setCountToZero();
        StudentStats studentStats = null;
        for (StudentStats studentStat : studentStatsArrayList) {
            if (studentProfile.getLevel().equalsIgnoreCase("NOVICE")) {
                studentStats = studentStat;
                break;
            }
            if (studentProfile.getLevel().equalsIgnoreCase(studentStat.getLevel())) {
                studentStats = studentStat;
                break;
            }
        }


        for (StudentStats studentStat : studentStatsArrayList) {
            StudentLabDetailsType2 studentLabDetailsType2 = new StudentLabDetailsType2("",
                    studentStat.getLevel(),
                    String.valueOf(studentStat.getProject_count()),
                    studentStat.getLab_time_count().replaceAll("\"", ""),
                    String.valueOf(studentStat.getDone_count()),
                    String.valueOf(studentStat.getSkipped_count()),
                    String.valueOf(studentStat.getPending_count()));
            objectArrayList.add(studentLabDetailsType2);
        }
        Collections.sort(objectArrayList, LevelComparator.getCompByName());
        StudentLabDetailsType1 studentLabDetailsType1 = new StudentLabDetailsType1(studentProfile.getFullName(),
                studentProfile.getLevel(),
                String.valueOf(LabTabApplication.getInstance().getTotalProjects()),
                LabTabApplication.getInstance().getLabTime() + "".replaceAll("\"", ""),
                String.valueOf(LabTabApplication.getInstance().getCompletedProjects()),
                String.valueOf(LabTabApplication.getInstance().getSkippedProjects()),
                String.valueOf(LabTabApplication.getInstance().getPendingProjects()));
        objectArrayList.add(0, studentLabDetailsType1);
        studentLabDetailsAdapter.notifyDataSetChanged();
    }
}
