package org.wizbots.labtab.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wizbots.labtab.R;
import org.wizbots.labtab.adapter.LabListAdapter;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.model.LabLevel;
import org.wizbots.labtab.model.LabList;

import java.util.ArrayList;

public class LabListFragment extends Fragment {

    LabTabHeaderLayout labTabHeaderLayout;
    LabListAdapter labListAdapter;
    RecyclerView recyclerViewLabList;
    ArrayList<Object> objectArrayList = new ArrayList<>();

    public LabListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lab_list, container, false);
        labTabHeaderLayout = (LabTabHeaderLayout) getActivity().findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText("Lab List");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.menu);
        labTabHeaderLayout.getMenuImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        });

        recyclerViewLabList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        objectArrayList = new ArrayList<>();

        labListAdapter = new LabListAdapter(objectArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLabList.setLayoutManager(mLayoutManager);
        recyclerViewLabList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLabList.setAdapter(labListAdapter);
        prepareDummyList();

        return rootView;
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
