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
import org.wizbots.labtab.adapter.StudentLabDetailsAdapter;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.interfaces.StudentLabDetailsAdapterClickListener;
import org.wizbots.labtab.model.StudentLabDetailsType1;
import org.wizbots.labtab.model.StudentLabDetailsType2;

import java.util.ArrayList;

public class StudentLabDetailsFragment extends ParentFragment implements View.OnClickListener, LabTabConstants, StudentLabDetailsAdapterClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private StudentLabDetailsAdapter studentLabDetailsAdapter;
    private RecyclerView recyclerViewStudentLabDetails;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;

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
        labTabHeaderLayout.getDynamicTextViewCustom().setText("Student Labs Details");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.menu);
        labTabHeaderLayout.getMenuImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivityContext.onBackPressed();
            }
        });

        recyclerViewStudentLabDetails = (RecyclerView) rootView.findViewById(R.id.recycler_view_student_lab_details);
        objectArrayList = new ArrayList<>();

        studentLabDetailsAdapter = new StudentLabDetailsAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewStudentLabDetails.setLayoutManager(mLayoutManager);
        recyclerViewStudentLabDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewStudentLabDetails.setAdapter(studentLabDetailsAdapter);
    }

    public void prepareDummyList() {
        objectArrayList.add(new StudentLabDetailsType1("Judy", LAB_LEVEL_APPRENTICE, "50", "45", "40", "35", "30"));
        objectArrayList.add(new StudentLabDetailsType2("", LAB_LEVEL_APPRENTICE, "50", "45", "40", "35", "30"));
        objectArrayList.add(new StudentLabDetailsType2("", LAB_LEVEL_EXPLORER, "50", "45", "40", "35", "30"));
        objectArrayList.add(new StudentLabDetailsType2("", LAB_LEVEL_IMAGINEER, "50", "45", "40", "35", "30"));
        objectArrayList.add(new StudentLabDetailsType2("", LAB_LEVEL_LAB_CERTIFIED, "50", "45", "40", "35", "30"));
        objectArrayList.add(new StudentLabDetailsType2("", LAB_LEVEL_MAKER, "50", "45", "40", "35", "30"));
        objectArrayList.add(new StudentLabDetailsType2("", LAB_LEVEL_MASTER, "50", "45", "40", "35", "30"));
        objectArrayList.add(new StudentLabDetailsType2("", LAB_LEVEL_WIZARD, "50", "45", "40", "35", "30"));
        studentLabDetailsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onViewTypeClick1() {

    }

    @Override
    public void onViewTypeClick2() {
        homeActivityContext.replaceFragment(FRAGMENT_STUDENT_STATS_DETAILS);
    }
}
