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
import org.wizbots.labtab.adapter.StudentStatsDetailsAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.interfaces.StudentStatsDetailsAdapterClickListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.student.StudentStatisticsDetail;
import org.wizbots.labtab.model.student.StudentStatisticsDetailProjects;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class StudentStatsDetailsFragment extends ParentFragment implements View.OnClickListener, StudentStatsDetailsAdapterClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private StudentStatsDetailsAdapter studentStatsDetailsAdapter;
    private RecyclerView recyclerViewStudentStatsDetails;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;
    private Program program;
    private Student student;
    private ProgramOrLab programOrLab;
    private ProgressDialog progressDialog;
    private TextViewCustom labSKUTextViewCustom, availabilityTextViewCustom,
            nameTextViewCustom, locationTextViewCustom, categoryTextViewCustom,
            roomTextViewCustom, gradesTextViewCustom, priceTextViewCustom,
            fromTextViewCustom, toTextViewCustom, timeSlotTextViewCustom, dayTextViewCustom;

    public StudentStatsDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_student_stats_details, container, false);
        homeActivityContext = (HomeActivity) context;
        programOrLab = getArguments().getParcelable(LabListFragment.LAB);
        program = getArguments().getParcelable(LabDetailsFragment.PROGRAM);
        student = getArguments().getParcelable(LabDetailsFragment.STUDENT);
        initView();
        prepareDummyList();
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
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_notsynced);
        initHeaderView();

        recyclerViewStudentStatsDetails = (RecyclerView) rootView.findViewById(R.id.recycler_view_student_stats_details);
        objectArrayList = new ArrayList<>();

        studentStatsDetailsAdapter = new StudentStatsDetailsAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewStudentStatsDetails.setLayoutManager(mLayoutManager);
        recyclerViewStudentStatsDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewStudentStatsDetails.setAdapter(studentStatsDetailsAdapter);
        rootView.findViewById(R.id.btn_absences).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
                homeActivityContext.replaceFragment(Fragments.LIST_OF_SKIPS, bundle);
            }
        });
        rootView.findViewById(R.id.btn_additional).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
                homeActivityContext.replaceFragment(Fragments.ADDITIONAL_INFORMATION, bundle);
            }
        });
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());

        if (program != null) {
            setHeaderView(program);
            progressDialog.dismiss();
        } else {
            progressDialog.dismiss();
        }
    }

    public void prepareDummyList() {
        objectArrayList.add(new StudentStatisticsDetail("Judy", LabLevels.APPRENTICE, "50", "45", "40", "35", "30"));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_1, "Algorithms", Marks.DONE));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_2, "Gyroscope", Marks.PENDING));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_3, "Data Structures", Marks.SKIPPED));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_4, "Accelerometer", Marks.CLOSE_TO_NEXT_LEVEl));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_1, "Algorithms", Marks.MECHANISMS));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_2, "Gyroscope", Marks.PROGRAMMING));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_3, "Data Structures", Marks.STRUCTURES));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_4, "Accelerometer", Marks.NONE));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_1, "Algorithms", Marks.DONE));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_2, "Gyroscope", Marks.PENDING));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_3, "Data Structures", Marks.SKIPPED));
        objectArrayList.add(new StudentStatisticsDetailProjects(Steps.LAB_STEP_4, "Accelerometer", Marks.CLOSE_TO_NEXT_LEVEl));
        studentStatsDetailsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onViewTypeClick1() {

    }

    @Override
    public void onViewTypeClick2() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(LabListFragment.LAB, programOrLab);
        homeActivityContext.replaceFragment(Fragments.LAB_DETAILS_LIST, bundle);
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
