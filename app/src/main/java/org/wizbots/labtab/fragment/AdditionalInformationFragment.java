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
import org.wizbots.labtab.adapter.AdditionalInformationAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.interfaces.AdditionalInformationAdapterClickListener;
import org.wizbots.labtab.model.AdditionalInformation;

import java.util.ArrayList;

public class AdditionalInformationFragment extends ParentFragment implements AdditionalInformationAdapterClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private AdditionalInformationAdapter additionalInformationAdapter;
    private RecyclerView recyclerViewAdditionalInformation;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;

    public AdditionalInformationFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_additional_information, container, false);
        homeActivityContext = (HomeActivity) context;
        initView();
        prepareDummyList();
        return rootView;
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText("Additional Information");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

        recyclerViewAdditionalInformation = (RecyclerView) rootView.findViewById(R.id.recycler_view_additional_information);
        objectArrayList = new ArrayList<>();

        additionalInformationAdapter = new AdditionalInformationAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAdditionalInformation.setLayoutManager(mLayoutManager);
        recyclerViewAdditionalInformation.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAdditionalInformation.setAdapter(additionalInformationAdapter);
        rootView.findViewById(R.id.btn_absences).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivityContext.replaceFragment(Fragments.LIST_OF_SKIPS, new Bundle());
            }
        });
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
    }

    @Override
    public String getFragmentName() {
        return AdditionalInformationFragment.class.getSimpleName();
    }


    @Override
    public void onActionViewClick() {

    }

    public void prepareDummyList() {
        objectArrayList.add(new AdditionalInformation(true, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(false, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(true, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(false, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(true, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(false, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(true, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(false, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(true, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(false, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(true, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(false, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(true, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(false, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        objectArrayList.add(new AdditionalInformation(true, "Student Name", "Yes", "Walks home, me", "02-30 PM\n04-30 PM", ""));
        additionalInformationAdapter.notifyDataSetChanged();
    }

}
