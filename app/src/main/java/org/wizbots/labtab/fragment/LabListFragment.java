package org.wizbots.labtab.fragment;

import android.app.ProgressDialog;
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
import org.wizbots.labtab.adapter.LabListAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.database.ProgramsOrLabsTable;
import org.wizbots.labtab.interfaces.GetProgramOrLabListener;
import org.wizbots.labtab.interfaces.LabListAdapterClickListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.requesters.ProgramOrLabRequester;
import org.wizbots.labtab.util.BackgroundExecutor;

import java.util.ArrayList;

public class LabListFragment extends ParentFragment implements LabListAdapterClickListener, GetProgramOrLabListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private LabListAdapter labListAdapter;
    private RecyclerView recyclerViewLabList;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;
    private ProgressDialog progressDialog;
    private ProgramOrLabRequester programOrLabRequester;

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
        homeActivityContext = (HomeActivity) context;
        initListeners();
        initView();
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
                homeActivityContext.onBackPressed();
            }
        });

        recyclerViewLabList = (RecyclerView) rootView.findViewById(R.id.recycler_view_lab_list);
        objectArrayList = new ArrayList<>();

        labListAdapter = new LabListAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLabList.setLayoutManager(mLayoutManager);
        recyclerViewLabList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLabList.setAdapter(labListAdapter);
        progressDialog = new ProgressDialog(homeActivityContext);
        ArrayList<ProgramOrLab> programOrLabArrayList = ProgramsOrLabsTable.getInstance().getProgramsList();
        progressDialog.setMessage("processing");
        progressDialog.setCanceledOnTouchOutside(false);
        if (!programOrLabArrayList.isEmpty()) {
            progressDialog.show();
            programOrLabFetchedSuccessfully(programOrLabArrayList);
        } else {
            if (programOrLabRequester == null) {
                progressDialog.show();
                programOrLabRequester = new ProgramOrLabRequester();
                BackgroundExecutor.getInstance().execute(programOrLabRequester);
            }
        }
    }


    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(GetProgramOrLabListener.class, this);
    }

    @Override
    public void onActionViewClick(ProgramOrLab labList) {
        homeActivityContext.replaceFragment(FRAGMENT_LAB_DETAILS_LIST);
    }

    @Override
    public void onDestroy() {
        LabTabApplication.getInstance().removeUIListener(GetProgramOrLabListener.class, this);
        super.onDestroy();
    }

    @Override
    public String getFragmentName() {
        return LabListFragment.class.getSimpleName();
    }

    @Override
    public void programOrLabFetchedSuccessfully(final ArrayList<ProgramOrLab> programOrLabs) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgramsOrLabsTable.getInstance().insert(programOrLabs);
                objectArrayList.addAll(programOrLabs);
                labListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void unableToFetchPrograms(int responseCode) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
        LabTabPreferences.getInstance(LabTabApplication.getInstance()).setUserLoggedIn(false);
        if (responseCode == SC_FORBIDDEN) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, NO_LAB_FOUND);
        } else {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, NO_INTERNET_CONNECTION);
        }
    }
}
