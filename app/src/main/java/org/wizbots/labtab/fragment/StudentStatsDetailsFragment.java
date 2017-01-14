package org.wizbots.labtab.fragment;

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
import org.wizbots.labtab.interfaces.StudentStatsDetailsAdapterClickListener;
import org.wizbots.labtab.model.StudentStatisticsDetail;
import org.wizbots.labtab.model.StudentStatisticsDetailProjects;

import java.util.ArrayList;

public class StudentStatsDetailsFragment extends ParentFragment implements View.OnClickListener, StudentStatsDetailsAdapterClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private StudentStatsDetailsAdapter studentStatsDetailsAdapter;
    private RecyclerView recyclerViewStudentStatsDetails;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;

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
        initView();
        prepareDummyList();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return LabDetailsFragment.class.getSimpleName();
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText("Lab Details");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_notsynced);

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
                homeActivityContext.replaceFragment(Fragments.LIST_OF_SKIPS, new Bundle());
            }
        });
        rootView.findViewById(R.id.btn_additional).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivityContext.replaceFragment(Fragments.ADDITIONAL_INFORMATION, new Bundle());
            }
        });
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
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
        homeActivityContext.replaceFragment(Fragments.LAB_DETAILS_LIST, new Bundle());
    }
}
