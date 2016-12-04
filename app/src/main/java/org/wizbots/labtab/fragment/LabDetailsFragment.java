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

import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.BaseActivity;
import org.wizbots.labtab.adapter.LabDetailsAdapter;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.interfaces.LabDetailsAdapterClickListener;
import org.wizbots.labtab.model.LabDetails;
import org.wizbots.labtab.model.LabLevel;

import java.util.ArrayList;

public class LabDetailsFragment extends ParentFragment implements LabDetailsAdapterClickListener {
    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private LabDetailsAdapter labDetailsAdapter;
    private RecyclerView recyclerViewLabDetails;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private BaseActivity baseActivityContext;

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
        baseActivityContext = (BaseActivity) context;
        initView();
        prepareDummyList();
        return rootView;
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText("Lab Details");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.menu);
        labTabHeaderLayout.getMenuImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseActivityContext.replaceFragment(BaseActivity.FRAGMENT_LAB_LIST);
            }
        });

        recyclerViewLabDetails = (RecyclerView) rootView.findViewById(R.id.recycler_view_lab_details);
        objectArrayList = new ArrayList<>();

        labDetailsAdapter = new LabDetailsAdapter(objectArrayList, baseActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLabDetails.setLayoutManager(mLayoutManager);
        recyclerViewLabDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLabDetails.setAdapter(labDetailsAdapter);
    }

    public void prepareDummyList() {
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.APPRENTICE.getValue(), "100", "100", "100", "100", "100", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.EXPLORER.getValue(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.MASTER.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.WIZARD.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.LAB_CERTIFIED.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.IMAGINEER.getValue(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.APPRENTICE.getValue(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.EXPLORER.getValue(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.MASTER.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.WIZARD.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.LAB_CERTIFIED.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.IMAGINEER.getValue(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.APPRENTICE.getValue(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.EXPLORER.getValue(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.MASTER.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.WIZARD.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.LAB_CERTIFIED.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.IMAGINEER.getValue(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.APPRENTICE.getValue(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.EXPLORER.getValue(), "8", "3", "2", "0", "1", true));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.MASTER.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.WIZARD.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.LAB_CERTIFIED.getValue(), "8", "3", "2", "0", "1", false));
        objectArrayList.add(new LabDetails(true, "Student Name", LabLevel.IMAGINEER.getValue(), "8", "3", "2", "0", "1", true));
        labDetailsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActionViewClick(LabDetails labList) {
        baseActivityContext.sendMessageToHandler(baseActivityContext.SHOW_TOAST, -1, -1, "On Action View");
    }

    @Override
    public void onActionEditClick(LabDetails labList) {
        baseActivityContext.sendMessageToHandler(baseActivityContext.SHOW_TOAST, -1, -1, "On Action Edit");
    }

    @Override
    public void onActionCloseToNextLevelClick(LabDetails labList) {
        baseActivityContext.sendMessageToHandler(baseActivityContext.SHOW_TOAST, -1, -1, "On Action Close To Next Level");
    }

    @Override
    public void onCheckChanged(int position, boolean value) {
        ((LabDetails) objectArrayList.get(position)).setCheck(value);
    }
}
