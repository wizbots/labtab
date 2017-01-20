package org.wizbots.labtab.fragment;


import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.LabDetailsAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.database.ProgramTable;
import org.wizbots.labtab.interfaces.LabDetailsAdapterClickListener;
import org.wizbots.labtab.interfaces.requesters.GetProgramStudentsListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.program.response.ProgramResponse;
import org.wizbots.labtab.requesters.ProgramStudentsRequester;
import org.wizbots.labtab.util.BackgroundExecutor;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class LabDetailsFragment extends ParentFragment implements LabDetailsAdapterClickListener, GetProgramStudentsListener {
    public static final String PROGRAM = "PROGRAM";
    public static final String STUDENT = "STUDENT";
    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private LabDetailsAdapter labDetailsAdapter;
    private RecyclerView recyclerViewLabDetails;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;
    private ImageView calendarImageView;
    private DatePickerDialog datePickerDialog;
    private ProgramOrLab programOrLab;
    private Program program;
    private ProgressDialog progressDialog;
    private TextViewCustom labSKUTextViewCustom, availabilityTextViewCustom,
            nameTextViewCustom, locationTextViewCustom, categoryTextViewCustom,
            roomTextViewCustom, gradesTextViewCustom, priceTextViewCustom,
            fromTextViewCustom, toTextViewCustom, timeSlotTextViewCustom, dayTextViewCustom;

    public LabDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lab_details, container, false);
        homeActivityContext = (HomeActivity) context;
        programOrLab = getArguments().getParcelable(LabListFragment.LAB);
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
        labTabHeaderLayout.getDynamicTextViewCustom().setText("Lab Details");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
        initHeaderView();

        recyclerViewLabDetails = (RecyclerView) rootView.findViewById(R.id.recycler_view_lab_details);
        objectArrayList = new ArrayList<>();

        labDetailsAdapter = new LabDetailsAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLabDetails.setLayoutManager(mLayoutManager);
        recyclerViewLabDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLabDetails.setAdapter(labDetailsAdapter);
        rootView.findViewById(R.id.btn_absences).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(PROGRAM, program);
                homeActivityContext.replaceFragment(Fragments.LIST_OF_SKIPS, bundle);
            }
        });
        rootView.findViewById(R.id.btn_additional).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(PROGRAM, program);
                homeActivityContext.replaceFragment(Fragments.ADDITIONAL_INFORMATION, bundle);
            }
        });
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());

        calendarImageView = (ImageView) rootView.findViewById(R.id.iv_calendar);

        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
        program = ProgramTable.getInstance().getProgramByProgramId(programOrLab.getId());
        if (program != null) {
            setHeaderView(program);
            ArrayList<Student> studentArrayList = ProgramStudentsTable.getInstance().getStudentsListByProgramId(programOrLab.getId());
            if (!studentArrayList.isEmpty()) {
                objectArrayList.addAll(studentArrayList);
                labDetailsAdapter.notifyDataSetChanged();
            } else {
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_STUDENT_FOUND_FOR_THIS_LAB);
            }
            progressDialog.dismiss();
        } else {
            BackgroundExecutor.getInstance().execute(new ProgramStudentsRequester(programOrLab.getId()));
        }

        rootView.findViewById(R.id.iv_add_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ProgramStudentsTable.getInstance().getStudentsListByProgramId(program.getId()).isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
                    homeActivityContext.replaceFragment(Fragments.ADD_VIDEO, bundle);
                } else {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.AT_LEAST_ONE_STUDENT_IS_NEEDED_TO_ADD_VIDEO);
                }
            }
        });
    }

    @Override
    public void onActionViewClick(Student student) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROGRAM, program);
        bundle.putParcelable(LabListFragment.LAB, programOrLab);
        bundle.putParcelable(STUDENT, student);
        homeActivityContext.replaceFragment(Fragments.STUDENT_LAB_DETAILS, bundle);
    }

    @Override
    public void onActionEditClick(Student student) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROGRAM, program);
        bundle.putParcelable(LabListFragment.LAB, programOrLab);
        bundle.putParcelable(STUDENT, student);
        homeActivityContext.replaceFragment(Fragments.STUDENT_PROFILE, bundle);
    }

    @Override
    public void onActionCloseToNextLevelClick(Student student) {
        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "On Action Close To Next Level");
    }

    @Override
    public void onCheckChanged(int position, boolean value) {
        ((Student) objectArrayList.get(position)).setCheck(value);
    }

    private void showCalendar() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            }

        };
        new DatePickerDialog(homeActivityContext,
                date, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(GetProgramStudentsListener.class, this);
    }

    @Override
    public void programStudentsFetchedSuccessfully(ProgramResponse programResponse, final Program program, final ArrayList<Student> studentArrayList, ArrayList<Absence> absenceArrayList) {
        this.program = program;
        if (studentArrayList.isEmpty()) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_STUDENT_FOUND_FOR_THIS_LAB);
        }
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setHeaderView(program);
                objectArrayList.addAll(studentArrayList);
                labDetailsAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void unableToFetchProgramStudents(int responseCode) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
        LabTabPreferences.getInstance(LabTabApplication.getInstance()).setUserLoggedIn(false);
        if (responseCode == StatusCode.FORBIDDEN) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_LAB_DETAIL_FOR_THIS_LAB);
        } else {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_INTERNET_CONNECTION);
        }
    }

    @Override
    public void onDestroy() {
        LabTabApplication.getInstance().removeUIListener(GetProgramStudentsListener.class, this);
        progressDialog.dismiss();
        super.onDestroy();
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
        dayTextViewCustom.setText(LabTabUtil.getFormattedDate());
    }
}
