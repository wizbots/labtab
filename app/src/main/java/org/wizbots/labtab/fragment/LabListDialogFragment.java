package org.wizbots.labtab.fragment;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.adapter.LabListDialogAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.database.ProgramsOrLabsTable;
import org.wizbots.labtab.interfaces.LabListAdapterClickListener;
import org.wizbots.labtab.model.ProgramOrLab;

import java.util.ArrayList;

public class LabListDialogFragment extends DialogFragment implements LabListAdapterClickListener {
    public static final String LAB_LIST = "LAB_LIST";
    private LabListDialogAdapter labListDialogAdapter;
    private RecyclerView recyclerViewLabList;
    private ArrayList<Object> objectArrayList = new ArrayList<>();

    public interface LabListClickListener {
        void actionViewClick(ProgramOrLab programOrLab);
    }


    public static LabListDialogFragment newInstanceLabListDialogFragment() {
        LabListDialogFragment labListDialogFragment = new LabListDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LAB_LIST, ProgramsOrLabsTable
                .getInstance()
                .getProgramsByMemberId(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id()));
        labListDialogFragment.setArguments(bundle);
        return labListDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_frament_lab_list, container, false);
        objectArrayList.addAll((ArrayList<ProgramOrLab>) getArguments().getSerializable(LAB_LIST));
        recyclerViewLabList = (RecyclerView) view.findViewById(R.id.recycler_view_lab_list);
        labListDialogAdapter = new LabListDialogAdapter(objectArrayList, getActivity(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLabList.setLayoutManager(mLayoutManager);
        recyclerViewLabList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLabList.setAdapter(labListDialogAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActionViewClick(final ProgramOrLab programOrLab) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LabListClickListener labListClickListener = (LabListClickListener) getTargetFragment();
                labListClickListener.actionViewClick(programOrLab);
                dismiss();
            }
        });
    }

    @Override
    public void onRosterDetailsClick(ProgramOrLab labList) {

    }
}