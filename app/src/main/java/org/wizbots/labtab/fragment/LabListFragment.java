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
import org.wizbots.labtab.adapter.LabListAdapter;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.model.LabLevel;
import org.wizbots.labtab.model.LabList;

import java.util.ArrayList;

public class LabListFragment extends ParentFragment {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private LabListAdapter labListAdapter;
    private RecyclerView recyclerViewLabList;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private BaseActivity baseActivityContext;

    public LabListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lab_list, container, false);
        baseActivityContext = (BaseActivity)context;
        initView();
        prepareDummyList();
        return rootView;
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText("Lab List");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.menu);
        labTabHeaderLayout.getMenuImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseActivityContext.replaceFragment(BaseActivity.FRAGMENT_HOME);
            }
        });

        recyclerViewLabList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        objectArrayList = new ArrayList<>();

        labListAdapter = new LabListAdapter(objectArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLabList.setLayoutManager(mLayoutManager);
        recyclerViewLabList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLabList.setAdapter(labListAdapter);
    }

    public void prepareDummyList() {
//        objectArrayList.add(new LabListHeader("Level", "Name", "Actions"));
        objectArrayList.add(new LabList(LabLevel.APPRENTICE.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.EXPLORER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.IMAGINEER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.LAB_CERTIFIED.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MAKER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MASTER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.WIZARD.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.APPRENTICE.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.EXPLORER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.IMAGINEER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.LAB_CERTIFIED.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MAKER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MASTER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.WIZARD.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.APPRENTICE.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.EXPLORER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.IMAGINEER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.LAB_CERTIFIED.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MAKER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MASTER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.WIZARD.getValue(), "Lab Name", R.drawable.action_view));
        labListAdapter.notifyDataSetChanged();
    }

}
