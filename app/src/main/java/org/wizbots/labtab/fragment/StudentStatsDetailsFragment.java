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

import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.StudentStatsDetailsAdapter;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.interfaces.StudentStatsDetailsAdapterClickListener;
import org.wizbots.labtab.model.StudentStatisticsDetail;
import org.wizbots.labtab.model.StudentStatisticsDetailProjects;

import java.util.ArrayList;

public class StudentStatsDetailsFragment extends ParentFragment implements View.OnClickListener, LabTabConstants, StudentStatsDetailsAdapterClickListener {

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
        labTabHeaderLayout.getDynamicTextViewCustom().setText("Student Stats Details");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.menu);
        labTabHeaderLayout.getMenuImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivityContext.onBackPressed();
            }
        });

        recyclerViewStudentStatsDetails = (RecyclerView) rootView.findViewById(R.id.recycler_view_student_stats_details);
        objectArrayList = new ArrayList<>();

        studentStatsDetailsAdapter = new StudentStatsDetailsAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewStudentStatsDetails.setLayoutManager(mLayoutManager);
        recyclerViewStudentStatsDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewStudentStatsDetails.setAdapter(studentStatsDetailsAdapter);
    }

    public void prepareDummyList() {
        objectArrayList.add(new StudentStatisticsDetail("Judy", LAB_LEVEL_APPRENTICE, "50", "45", "40", "35", "30"));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_1, "Algorithms", MARKS_DONE));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_2, "Gyroscope", MARKS_PENDING));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_3, "Data Structures", MARKS_SKIPPED));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_4, "Accelerometer", MARKS_CLOSE_TO_NEXT_LEVEl));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_1, "Algorithms", MARKS_MECHANISMS));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_2, "Gyroscope", MARKS_PROGRAMMING));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_3, "Data Structures", MARKS_STRUCTURES));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_4, "Accelerometer", MARKS_NONE));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_1, "Algorithms", MARKS_DONE));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_2, "Gyroscope", MARKS_PENDING));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_3, "Data Structures", MARKS_SKIPPED));
        objectArrayList.add(new StudentStatisticsDetailProjects(LAB_STEP_4, "Accelerometer", MARKS_CLOSE_TO_NEXT_LEVEl));
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
        homeActivityContext.replaceFragment(LabTabConstants.FRAGMENT_LAB_DETAILS_LIST);
    }
}
