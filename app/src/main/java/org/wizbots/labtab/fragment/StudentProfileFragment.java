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
import org.wizbots.labtab.adapter.StudentStatsAdapter;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.interfaces.StudentStatsAdapterClickListener;
import org.wizbots.labtab.model.StudentStatistics;

import java.util.ArrayList;

public class StudentProfileFragment extends ParentFragment implements View.OnClickListener, StudentStatsAdapterClickListener, LabTabConstants {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private StudentStatsAdapter studentStatsAdapter;
    private RecyclerView recyclerViewStudentStats;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;

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
        labTabHeaderLayout.getDynamicTextViewCustom().setText("Kid Name Account");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.menu);

        recyclerViewStudentStats = (RecyclerView) rootView.findViewById(R.id.recycler_view_student_stats_list);
        objectArrayList = new ArrayList<>();

        studentStatsAdapter = new StudentStatsAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewStudentStats.setLayoutManager(mLayoutManager);
        recyclerViewStudentStats.setItemAnimator(new DefaultItemAnimator());
        recyclerViewStudentStats.setAdapter(studentStatsAdapter);
    }

    public void prepareDummyList() {
        objectArrayList.add(new StudentStatistics(LAB_LEVEL_APPRENTICE, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LAB_LEVEL_EXPLORER, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LAB_LEVEL_IMAGINEER, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LAB_LEVEL_LAB_CERTIFIED, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LAB_LEVEL_MAKER, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LAB_LEVEL_MASTER, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LAB_LEVEL_WIZARD, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        studentStatsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onActionViewClick() {
        homeActivityContext.replaceFragment(LabTabConstants.FRAGMENT_STUDENT_STATS_DETAILS);
    }
}
