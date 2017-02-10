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
import android.widget.ImageView;
import android.widget.Spinner;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.LabListAdapter;
import org.wizbots.labtab.adapter.SpinnerAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.database.ProgramsOrLabsTable;
import org.wizbots.labtab.interfaces.LabListAdapterClickListener;
import org.wizbots.labtab.interfaces.requesters.GetProgramOrLabListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.requesters.ProgramOrLabRequester;
import org.wizbots.labtab.requesters.ProjectsMetaDataRequester;
import org.wizbots.labtab.util.BackgroundExecutor;

import java.util.ArrayList;
import java.util.Arrays;

public class LabListFragment extends ParentFragment implements LabListAdapterClickListener, GetProgramOrLabListener, View.OnClickListener {

    public static final String LAB = "LAB";
    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private LabListAdapter labListAdapter;
    private RecyclerView recyclerViewLabList;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;
    private ProgressDialog progressDialog;
    private ProgramOrLabRequester programOrLabRequester;
    private Spinner spinnerLocation, spinnerYear, spinnerSeason;
    private ImageView imageViewSearch, imageViewCancel;

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
        if (LabTabApplication.getInstance().getMetaDatas() == null) {
            BackgroundExecutor.getInstance().execute(new ProjectsMetaDataRequester());
        }
        initListeners();
        initView();
        return rootView;
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        spinnerLocation = (Spinner) rootView.findViewById(R.id.spinner_location);
        spinnerYear = (Spinner) rootView.findViewById(R.id.spinner_year);
        spinnerSeason = (Spinner) rootView.findViewById(R.id.spinner_season);
        rootView.findViewById(R.id.spinner_location_frame).setOnClickListener(this);
        rootView.findViewById(R.id.spinner_year_frame).setOnClickListener(this);
        rootView.findViewById(R.id.spinner_season_frame).setOnClickListener(this);
        spinnerSeason.setAdapter(new SpinnerAdapter(homeActivityContext,
                Arrays.asList(homeActivityContext.getResources().getStringArray(R.array.array_season))));
        spinnerYear.setAdapter(new SpinnerAdapter(homeActivityContext,
                Arrays.asList(homeActivityContext.getResources().getStringArray(R.array.array_year))));
        spinnerLocation.setAdapter(new SpinnerAdapter(homeActivityContext,
                Arrays.asList(homeActivityContext.getResources().getStringArray(R.array.array_location))));
        imageViewSearch = (ImageView) rootView.findViewById(R.id.iv_search);
        imageViewCancel = (ImageView) rootView.findViewById(R.id.iv_cancel);
        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerLocation.setSelection(0);
                spinnerYear.setSelection(0);
                spinnerSeason.setSelection(0);
            }
        });
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.LAB_LIST);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

        recyclerViewLabList = (RecyclerView) rootView.findViewById(R.id.recycler_view_lab_list);
//        recyclerViewLabList.setFocusable(false);
        objectArrayList = new ArrayList<>();

        labListAdapter = new LabListAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLabList.setLayoutManager(mLayoutManager);
        recyclerViewLabList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLabList.setAdapter(labListAdapter);
        progressDialog = new ProgressDialog(homeActivityContext);
        ArrayList<ProgramOrLab> programOrLabArrayList
                = ProgramsOrLabsTable
                .getInstance()
                .getProgramsByMemberId(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
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
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
    }


    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(GetProgramOrLabListener.class, this);
    }

    @Override
    public void onActionViewClick(ProgramOrLab programOrLab) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(LAB, programOrLab);
        homeActivityContext.replaceFragment(Fragments.LAB_DETAILS_LIST, bundle);
    }

    @Override
    public void onDestroy() {
        progressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LabTabApplication.getInstance().removeUIListener(GetProgramOrLabListener.class, this);
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
                objectArrayList.clear();
                objectArrayList.addAll(ProgramsOrLabsTable
                        .getInstance()
                        .getProgramsByMemberId(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id()));
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
        if (responseCode == StatusCode.FORBIDDEN) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_LAB_FOUND);
        } else {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_INTERNET_CONNECTION);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.spinner_year_frame:
                spinnerYear.performClick();
                break;
            case R.id.spinner_location_frame:
                spinnerLocation.performClick();
                break;
            case R.id.spinner_season_frame:
                spinnerSeason.performClick();
                break;
        }
    }
}
