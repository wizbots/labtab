package org.wizbots.labtab.fragment;


import android.app.DatePickerDialog;
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
import org.wizbots.labtab.enums.LabLevel;
import org.wizbots.labtab.interfaces.LabDetailsAdapterClickListener;
import org.wizbots.labtab.model.LabDetails;

import java.util.ArrayList;
import java.util.Calendar;

public class LabDetailsFragment extends ParentFragment implements LabDetailsAdapterClickListener {
    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private LabDetailsAdapter labDetailsAdapter;
    private RecyclerView recyclerViewLabDetails;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;
    private ImageView calendarImageView;
    private DatePickerDialog datePickerDialog;

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
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

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
                homeActivityContext.replaceFragment(FRAGMENT_LIST_OF_SKIPS, new Bundle());
            }
        });
        rootView.findViewById(R.id.btn_additional).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivityContext.replaceFragment(FRAGMENT_ADDITIONAL_INFORMATION, new Bundle());
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
    }

    public void prepareDummyList() {
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.APPRENTICE.getLabLevel(), "100", "100", "100", "100", "100", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.EXPLORER.getLabLevel(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(false, "Student Name", LabLevel.MASTER.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.WIZARD.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.LAB_CERTIFIED.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(false, "Student Name", LabLevel.IMAGINEER.getLabLevel(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.APPRENTICE.getLabLevel(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.EXPLORER.getLabLevel(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(false, "Student Name", LabLevel.MASTER.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.WIZARD.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.LAB_CERTIFIED.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(false, "Student Name", LabLevel.IMAGINEER.getLabLevel(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.APPRENTICE.getLabLevel(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.EXPLORER.getLabLevel(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(false, "Student Name", LabLevel.MASTER.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.WIZARD.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.LAB_CERTIFIED.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(false, "Student Name", LabLevel.IMAGINEER.getLabLevel(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.APPRENTICE.getLabLevel(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.EXPLORER.getLabLevel(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(false, "Student Name", LabLevel.MASTER.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.WIZARD.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.LAB_CERTIFIED.getLabLevel(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.IMAGINEER.getLabLevel(), "8", "3", "2", "0", "1", true));
        labDetailsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActionViewClick(LabDetails labList) {
        homeActivityContext.replaceFragment(FRAGMENT_STUDENT_LAB_DETAILS, new Bundle());
    }

    @Override
    public void onActionEditClick(LabDetails labList) {
        homeActivityContext.replaceFragment(FRAGMENT_STUDENT_PROFILE, new Bundle());
    }

    @Override
    public void onActionCloseToNextLevelClick(LabDetails labList) {
        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "On Action Close To Next Level");
    }

    @Override
    public void onCheckChanged(int position, boolean value) {
        ((LabDetails) objectArrayList.get(position)).setCheck(value);
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
}
