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
import org.wizbots.labtab.adapter.StudentStatsAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.interfaces.StudentStatsAdapterClickListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.student.StudentStatistics;

import java.util.ArrayList;

public class StudentProfileFragment extends ParentFragment implements View.OnClickListener, StudentStatsAdapterClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private StudentStatsAdapter studentStatsAdapter;
    private RecyclerView recyclerViewStudentStats;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;
    private Program program;
    private Student student;
    private ProgramOrLab programOrLab;

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
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_notsynced);

        recyclerViewStudentStats = (RecyclerView) rootView.findViewById(R.id.recycler_view_student_stats_list);
        objectArrayList = new ArrayList<>();

        studentStatsAdapter = new StudentStatsAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewStudentStats.setLayoutManager(mLayoutManager);
        recyclerViewStudentStats.setItemAnimator(new DefaultItemAnimator());
        recyclerViewStudentStats.setAdapter(studentStatsAdapter);
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
    }

    public void prepareDummyList() {
        objectArrayList.add(new StudentStatistics(LabLevels.APPRENTICE, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LabLevels.EXPLORER, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LabLevels.IMAGINEER, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LabLevels.LAB_CERTIFIED, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LabLevels.MAKER, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LabLevels.MASTER, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        objectArrayList.add(new StudentStatistics(LabLevels.WIZARD, "50", "45", "40", "35", "30", "25", "20", "15", "10"));
        studentStatsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onActionViewClick() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
        bundle.putParcelable(LabListFragment.LAB, programOrLab);
        bundle.putParcelable(LabDetailsFragment.STUDENT, student);
        homeActivityContext.replaceFragment(Fragments.STUDENT_STATS_DETAILS, bundle);
    }
}
