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
import org.wizbots.labtab.adapter.ListOfSkipsAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.interfaces.ListOfSkipsAdapterClickListener;
import org.wizbots.labtab.model.ListOfSkips;

import java.util.ArrayList;

public class ListOfSkipsFragment extends ParentFragment implements ListOfSkipsAdapterClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private ListOfSkipsAdapter listOfSkipsAdapter;
    private RecyclerView recyclerViewListOfSkips;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;

    public ListOfSkipsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_of_skips, container, false);
        homeActivityContext = (HomeActivity) context;
        initView();
        prepareDummyList();
        return rootView;
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText("List Of Skips");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_notsynced);

        recyclerViewListOfSkips = (RecyclerView) rootView.findViewById(R.id.recycler_view_list_of_skips);
        objectArrayList = new ArrayList<>();

        listOfSkipsAdapter = new ListOfSkipsAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListOfSkips.setLayoutManager(mLayoutManager);
        recyclerViewListOfSkips.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListOfSkips.setAdapter(listOfSkipsAdapter);
        rootView.findViewById(R.id.btn_additional).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivityContext.replaceFragment(Fragments.ADDITIONAL_INFORMATION, new Bundle());
            }
        });
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
    }

    @Override
    public String getFragmentName() {
        return ListOfSkipsFragment.class.getSimpleName();
    }


    @Override
    public void onActionViewClick() {

    }

    public void prepareDummyList() {
        objectArrayList.add(new ListOfSkips(true, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(false, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(true, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(false, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(true, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(false, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(true, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(false, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(true, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(false, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(true, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(false, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(true, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(false, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(true, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(false, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(true, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(false, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(true, "Student Name", "Mentor Name", "12/12/2016", ""));
        objectArrayList.add(new ListOfSkips(false, "Student Name", "Mentor Name", "12/12/2016", ""));
        listOfSkipsAdapter.notifyDataSetChanged();
    }

}
