package org.wizbots.labtab.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.LabListAdapter;
import org.wizbots.labtab.adapter.LocationAdapter;
import org.wizbots.labtab.adapter.SpinnerAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.database.LocationTable;
import org.wizbots.labtab.database.ProgramsOrLabsTable;
import org.wizbots.labtab.interfaces.LabListAdapterClickListener;
import org.wizbots.labtab.interfaces.requesters.GetProgramOrLabListener;
import org.wizbots.labtab.interfaces.requesters.OnFilterListener;
import org.wizbots.labtab.model.LocationResponse;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.requesters.FilterRequester;
import org.wizbots.labtab.requesters.ProgramOrLabRequester;
import org.wizbots.labtab.requesters.ProjectsMetaDataRequester;
import org.wizbots.labtab.util.BackgroundExecutor;
import org.wizbots.labtab.util.LabTabUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class LabListFragment extends ParentFragment implements LabListAdapterClickListener, LabTabConstants, OnFilterListener, GetProgramOrLabListener, View.OnClickListener {

    private static final String TAG = LabListFragment.class.getSimpleName();
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
    private Map<String, String> filterMap;

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
        filterMap = new LinkedHashMap<String, String>();
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
        spinnerLocation.setAdapter(new LocationAdapter(homeActivityContext, getLocation(LocationTable.getInstance().getLocationList())));
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.LAB_LIST);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

        recyclerViewLabList = (RecyclerView) rootView.findViewById(R.id.recycler_view_lab_list);
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

    private ArrayList<LocationResponse> getLocation(ArrayList<LocationResponse> list){
        ArrayList<LocationResponse> locationList = new ArrayList<LocationResponse>();
        locationList.addAll(list);
        locationList.add(0,new LocationResponse("", "All Location"));
        return locationList;
    }

    @Override
    public void onResume() {
        super.onResume();
        rootView.findViewById(R.id.iv_search).setOnClickListener(this);
        rootView.findViewById(R.id.iv_cancel).setOnClickListener(this);
        rootView.findViewById(R.id.btn_today).setOnClickListener(this);
        rootView.findViewById(R.id.btn_tomorrow).setOnClickListener(this);
        initAdapterListener();
    }

    private void initAdapterListener(){
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                LocationResponse location = (LocationResponse) adapterView.getItemAtPosition(position);
                removeTodayTomorrowFilterKey();
                if (position == 0){
                    filterMap.remove(FilterRequestParameter.LOCATION_ID);
                }else {
                    filterMap.put(FilterRequestParameter.LOCATION_ID, location.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String season = (String) adapterView.getItemAtPosition(position);
                removeTodayTomorrowFilterKey();
                if (position == 0){
                    filterMap.remove(FilterRequestParameter.SEASON);
                }else {
                    filterMap.put(FilterRequestParameter.SEASON, season);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String year = (String) adapterView.getItemAtPosition(position);
                removeTodayTomorrowFilterKey();
                if (position == 0){
                    filterMap.remove(FilterRequestParameter.SEASON_YEAR);
                }else {
                    filterMap.put(FilterRequestParameter.SEASON_YEAR, year);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void removeTodayTomorrowFilterKey(){
        filterMap.remove(FilterRequestParameter.TO);
        filterMap.remove(FilterRequestParameter.FROM);
    }

    private void callFilterApi(){
        progressDialog.show();
        Map<String, String> filterMap1 = new LinkedHashMap<>();
        filterMap1.putAll(filterMap);
        BackgroundExecutor.getInstance().execute(new FilterRequester(filterMap1));
    }

    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(GetProgramOrLabListener.class, this);
        LabTabApplication.getInstance().addUIListener(OnFilterListener.class, this);
    }

    @Override
    public void onActionViewClick(ProgramOrLab programOrLab) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(LAB, programOrLab);
        homeActivityContext.replaceFragment(Fragments.LAB_DETAILS_LIST, bundle);
    }

    @Override
    public void onDestroy() {
        LabTabApplication.getInstance().removeUIListener(OnFilterListener.class, this);
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
                resetOriginalData();
            }
        });
    }

    private void resetOriginalData(){
        objectArrayList.clear();
        objectArrayList.addAll(ProgramsOrLabsTable
                .getInstance()
                .getProgramsByMemberId(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id()));
        labListAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
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
            case R.id.iv_search:
                callFilterApi();
                break;
            case R.id.btn_today:
                Log.d(TAG, "TODAY BUTTON CLICKED");
                filterMap.clear();
                filterMap.putAll(getTodaysDate());
                callFilterApi();
                break;
            case R.id.btn_tomorrow:
                Log.d(TAG, "TOMORROW BUTTON CLICKED");
                filterMap.clear();
                filterMap.putAll(getTomorrowDate());
                callFilterApi();
                break;
            case R.id.iv_cancel:
                filterMap.clear();
                spinnerLocation.setSelection(0);
                spinnerYear.setSelection(0);
                spinnerSeason.setSelection(0);
                resetOriginalData();
                break;
        }
    }

    @Override
    public void onFilterSuccess(final ArrayList<ProgramOrLab> programOrLabs) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(programOrLabs != null && programOrLabs.isEmpty()){
                    Toast.makeText(homeActivityContext, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                objectArrayList.clear();
                objectArrayList.addAll(programOrLabs);
                labListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onFilterError(int responseCode) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Toast.makeText(homeActivityContext, "Failed to fetch filter", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Map<String, String> getTodaysDate(){
        Map<String, String> todayDate = new LinkedHashMap<>();
        todayDate.put(FilterRequestParameter.FROM, LabTabUtil.getTodayDate(false));
        todayDate.put(FilterRequestParameter.TO, LabTabUtil.getTodayDate(false));
        return todayDate;
    }

    private Map<String, String> getTomorrowDate(){
        Map<String, String> todayDate = new LinkedHashMap<>();
        todayDate.put(FilterRequestParameter.FROM, LabTabUtil.getTodayDate(true));
        todayDate.put(FilterRequestParameter.TO, LabTabUtil.getTodayDate(true));
        return todayDate;
    }
}
