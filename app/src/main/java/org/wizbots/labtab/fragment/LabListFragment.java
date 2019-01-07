package org.wizbots.labtab.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.DatePicker;
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
import org.wizbots.labtab.interfaces.OnSyncDoneListener;
import org.wizbots.labtab.interfaces.requesters.GetProgramOrLabListener;
import org.wizbots.labtab.interfaces.requesters.OnFilterListener;
import org.wizbots.labtab.model.LocationResponse;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.requesters.FilterRequester;
import org.wizbots.labtab.requesters.ProgramOrLabRequester;
import org.wizbots.labtab.requesters.ProjectsMetaDataRequester;
import org.wizbots.labtab.service.SyncManager;
import org.wizbots.labtab.util.BackgroundExecutor;
import org.wizbots.labtab.util.LabTabUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LabListFragment extends ParentFragment implements LabListAdapterClickListener,
        LabTabConstants, OnFilterListener, GetProgramOrLabListener, View.OnClickListener, OnSyncDoneListener {

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
    private Date dateSelected;
    private boolean isFilter;
    private String locationSearch;
    private int locationPos;
    private String yearSearch;
    private int yearPos;
    private String seasonSearch;
    private int seasonPos;


    public LabListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LabTabApplication.getInstance().addUIListener(OnSyncDoneListener.class, this);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filterMap.clear();
        boolean isAPICalleligible = false;
        if (LabTabApplication.getInstance().getYear() != null && !LabTabApplication.getInstance().getYear().equalsIgnoreCase("") && !LabTabApplication.getInstance().getYear().equalsIgnoreCase("All Years")) {
            isAPICalleligible = true;
            filterMap.put(FilterRequestParameter.SEASON_YEAR, LabTabApplication.getInstance().getYear());


        }
        if (LabTabApplication.getInstance().getLocation() != null && !LabTabApplication.getInstance().getLocation().equalsIgnoreCase("") && !LabTabApplication.getInstance().getLocation().equalsIgnoreCase("All Locations")) {
            isAPICalleligible = true;
            filterMap.put(FilterRequestParameter.LOCATION_ID, LabTabApplication.getInstance().getLocation());

        }
        if (LabTabApplication.getInstance().getSeason() != null && !LabTabApplication.getInstance().getSeason().equalsIgnoreCase("") && !LabTabApplication.getInstance().getSeason().equalsIgnoreCase("All Seasons")) {
            isAPICalleligible = true;
            filterMap.put(FilterRequestParameter.SEASON, LabTabApplication.getInstance().getSeason());


        }
        if (isAPICalleligible) {
            callFilterApi();
        } else {
            ArrayList<ProgramOrLab> programOrLabArrayList
                    = ProgramsOrLabsTable
                    .getInstance()
                    .getProgramsByMemberId(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
            if (!programOrLabArrayList.isEmpty()) {
                progressDialog.show();
                programOrLabFetchedSuccessfully(programOrLabArrayList);
            }
        }
        setLocYrSeasonView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //  outState.putInt("curChoice", mCurCheckPosition);
    }

    public void initView() {
        //isFilter = true;
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

        // Default Selection
        spinnerYear.setSelection(9);
        yearSearch = (String) spinnerYear.getSelectedItem();
        yearPos = spinnerYear.getSelectedItemPosition();
        spinnerSeason.setSelection(4);
        String season = (String) spinnerSeason.getSelectedItem();
        seasonSearch = season != null ? season.toLowerCase() : "";
        seasonPos = spinnerSeason.getSelectedItemPosition();

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
          /*  progressDialog.show();
            programOrLabFetchedSuccessfully(programOrLabArrayList);*/
        } else {
            if (programOrLabRequester == null) {
                progressDialog.show();
                programOrLabRequester = new ProgramOrLabRequester();
                BackgroundExecutor.getInstance().execute(programOrLabRequester);
            }
        }

        callCachedFilerAPI();
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
    }

    private ArrayList<LocationResponse> getLocation(ArrayList<LocationResponse> list) {
        ArrayList<LocationResponse> locationList = new ArrayList<LocationResponse>();
        locationList.addAll(list);

        Collections.sort(locationList, LocationResponse.locationResponseComparator);
        locationList.add(0, new LocationResponse("", "All Locations"));

        return locationList;
    }

    @Override
    public void onResume() {
        super.onResume();
        SyncManager.getInstance().onRefreshData(1);
        boolean isSync = SyncManager.getInstance().isLabDetailSynced();
        if (isSync) {
            updateSyncStatus(true);
        } else {
            updateSyncStatus(false);
        }
        rootView.findViewById(R.id.iv_search).setOnClickListener(this);
        rootView.findViewById(R.id.iv_cancel).setOnClickListener(this);
        rootView.findViewById(R.id.btn_today).setOnClickListener(this);
        rootView.findViewById(R.id.btn_tomorrow).setOnClickListener(this);
        rootView.findViewById(R.id.calendar).setOnClickListener(this);
        initAdapterListener();
        //resetFilter();
    }

    private void showCalendar() {
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateSelected = calendar.getTime();
                if (dateSelected != null) {
                    filterMap.clear();
                    filterMap.putAll(getDateByCalendar(dateSelected));
                    callFilterApi();
                }
/*                if (LabTabUtil.isValidDateSelection(calendar.getTime())) {
                    dateSelected = calendar.getTime();

                } else {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.YOU_CAN_NOT_SELECT_DATE_MORE_THAN_TODAY);
                }*/
            }

        };
        new DatePickerDialog(homeActivityContext,
                date, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void initAdapterListener() {
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                LocationResponse location = (LocationResponse) adapterView.getItemAtPosition(position);
                removeTodayTomorrowFilterKey();
                isFilter = false;

//                disableLocationYearsSeason();
                if (position == 0) {
                    filterMap.remove(FilterRequestParameter.LOCATION_ID);

                } else {
                    filterMap.put(FilterRequestParameter.LOCATION_ID, location.getId());

                }
                locationSearch = location.getId();
                locationPos = position;
                //LabTabApplication.getInstance().setLocation(location.getId(), position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                disableLocationYearsSeason();
                String season = (String) adapterView.getItemAtPosition(position);
                isFilter = false;

                removeTodayTomorrowFilterKey();
                if (position == 0) {
                    filterMap.remove(FilterRequestParameter.SEASON);

                } else {
                    filterMap.put(FilterRequestParameter.SEASON, season != null ? season.toLowerCase() : "");

                }
                seasonSearch = season != null ? season.toLowerCase() : "";
                seasonPos = position;
                //LabTabApplication.getInstance().setSeason(season.toLowerCase(), position);

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
                isFilter = false;
                //disableLocationYearsSeason();
                if (position == 0) {
                    filterMap.remove(FilterRequestParameter.SEASON_YEAR);

                } else {
                    filterMap.put(FilterRequestParameter.SEASON_YEAR, year);
                }
                yearSearch = year;
                yearPos = position;
                //LabTabApplication.getInstance().setYear(year, position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void removeTodayTomorrowFilterKey() {
        filterMap.remove(FilterRequestParameter.TO);
        filterMap.remove(FilterRequestParameter.FROM);
    }

    private void callFilterApi() {
        progressDialog.show();
        filterMap.put(FilterRequestParameter.MENTOR_ID, LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
        Map<String, String> filterMap1 = new LinkedHashMap<>();
        filterMap1.putAll(filterMap);
        BackgroundExecutor.getInstance().execute(new FilterRequester(filterMap1));
    }

    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(GetProgramOrLabListener.class, this);
        LabTabApplication.getInstance().addUIListener(OnFilterListener.class, this);
    }

    private void callCachedFilerAPI() {
        boolean isAPICalleligible = false;
        if (isFilter) {
            filterMap.clear();
            if (LabTabApplication.getInstance().getYear() != null && !LabTabApplication.getInstance().getYear().equalsIgnoreCase("") && !LabTabApplication.getInstance().getYear().equalsIgnoreCase("All Years")) {
                isAPICalleligible = true;
                filterMap.put(FilterRequestParameter.SEASON_YEAR, LabTabApplication.getInstance().getYear());


            }
            if (LabTabApplication.getInstance().getLocation() != null && !LabTabApplication.getInstance().getLocation().equalsIgnoreCase("") && !LabTabApplication.getInstance().getLocation().equalsIgnoreCase("All Locations")) {
                isAPICalleligible = true;
                filterMap.put(FilterRequestParameter.LOCATION_ID, LabTabApplication.getInstance().getLocation());

            }
            if (LabTabApplication.getInstance().getSeason() != null && !LabTabApplication.getInstance().getSeason().equalsIgnoreCase("") && !LabTabApplication.getInstance().getSeason().equalsIgnoreCase("All Seasons")) {
                isAPICalleligible = true;
                filterMap.put(FilterRequestParameter.SEASON, LabTabApplication.getInstance().getSeason());


            }
            if (isAPICalleligible) {
                callFilterApi();
            }
        } else {

        }
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
        LabTabApplication.getInstance().removeUIListener(GetProgramOrLabListener.class, this);
        LabTabApplication.getInstance().removeUIListener(OnSyncDoneListener.class, this);
        progressDialog.dismiss();
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
                if (!LabTabApplication.getInstance().isNetworkAvailable() && (programOrLabs == null || programOrLabs.isEmpty())) {
                    progressDialog.dismiss();
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "No data available, please try again later with Internet connectivity");
                } else {
                    objectArrayList.clear();
                    objectArrayList.addAll(programOrLabs);
                    recyclerViewLabList.setAdapter(labListAdapter);

                    labListAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void resetOriginalData() {
        objectArrayList.clear();
        objectArrayList.addAll(ProgramsOrLabsTable
                .getInstance()
                .getProgramsByMemberId(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id()));
        recyclerViewLabList.setAdapter(labListAdapter);

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

    private void setValues() {
        LabTabApplication.getInstance().setSeason(seasonSearch, seasonPos);
        LabTabApplication.getInstance().setYear(yearSearch, yearPos);
        LabTabApplication.getInstance().setLocation(locationSearch, locationPos);

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
                isFilter = true;
                setValues();
                callFilterApi();
                break;
            case R.id.calendar:
                showCalendar();
                break;
            case R.id.btn_today:
                Log.d(TAG, "TODAY BUTTON CLICKED");
                filterMap.clear();
                filterMap.putAll(getTodaysDate());
                callFilterApi();
                break;
            case R.id.btn_tomorrow:
                Log.d(TAG, "TOMORROW BUTTON CLICKED");
                progressDialog.show();
                spinnerLocation.setSelection(0);
                spinnerYear.setSelection(9);
                spinnerSeason.setSelection(0);
                programOrLabRequester = new ProgramOrLabRequester();
                BackgroundExecutor.getInstance().execute(programOrLabRequester);


                break;
            case R.id.iv_cancel:
                progressDialog.show();
                spinnerLocation.setSelection(0);
                spinnerYear.setSelection(9);
                spinnerSeason.setSelection(0);
                LabTabApplication.getInstance().setSeason(null, 0);
                LabTabApplication.getInstance().setYear(null, 0);
                LabTabApplication.getInstance().setLocation(null, 0);

               /* filterMap.clear();
                filterMap.put(FilterRequestParameter.SEASON_YEAR, String.valueOf(spinnerYear.getAdapter().getItem(0)));
                filterMap.put(FilterRequestParameter.SEASON, String.valueOf(spinnerSeason.getAdapter().getItem(0)));
                callFilterApi();*/
                if (programOrLabRequester == null) {
                    programOrLabRequester = new ProgramOrLabRequester();
                }
                BackgroundExecutor.getInstance().execute(programOrLabRequester);
                break;
        }
    }

    private void resetFilter() {
        filterMap.clear();
        spinnerLocation.setSelection(0);
        spinnerYear.setSelection(0);
        spinnerSeason.setSelection(0);
    }

   /* private void enableLocationYearsSeason() {
        LabTabApplication.getInstance().setLocationEnable(true);
        LabTabApplication.getInstance().setYearEnable(true);
        LabTabApplication.getInstance().setSeasonEnable(true);

    }

    private void disableLocationYearsSeason() {
        LabTabApplication.getInstance().setLocationEnable(false);
        LabTabApplication.getInstance().setYearEnable(false);
        LabTabApplication.getInstance().setSeasonEnable(false);

    }*/

    @Override
    public void onFilterSuccess(final ArrayList<ProgramOrLab> programOrLabs) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //  enableLocationYearsSeason();
                if (!LabTabApplication.getInstance().isNetworkAvailable() && (programOrLabs == null || programOrLabs.isEmpty())) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_DATA_NO_CONNECTION);
                } else if (programOrLabs != null && programOrLabs.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_DATA_FOUND);
                }
                removeTodayTomorrowFilterKey();
                objectArrayList.clear();
                objectArrayList.addAll(programOrLabs);
                recyclerViewLabList.setAdapter(labListAdapter);

                labListAdapter.notifyDataSetChanged();
                setLocYrSeasonView();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onFilterError(int responseCode) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // disableLocationYearsSeason();
                isFilter = false;
                removeTodayTomorrowFilterKey();
                progressDialog.dismiss();
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.FAILED_TO_FETCH_FILTER);
            }
        });
    }

    private void setLocYrSeasonView() {
        if (LabTabApplication.getInstance().getYear() != null) {
            spinnerYear.setSelection(LabTabApplication.getInstance().getYearPos());


        }
        if (LabTabApplication.getInstance().getLocation() != null) {
            spinnerLocation.setSelection(LabTabApplication.getInstance().getLocationPos());


        }
        if (LabTabApplication.getInstance().getSeason() != null) {
            spinnerSeason.setSelection(LabTabApplication.getInstance().getSeasonPos());


        }

    }

    private Map<String, String> getDateByCalendar(Date date) {
        Map<String, String> todayDate = new LinkedHashMap<>();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(date);
        todayDate.put(FilterRequestParameter.FROM, formatted);
        todayDate.put(FilterRequestParameter.TO, formatted);
        return todayDate;
    }

    private Map<String, String> getTodaysDate() {
        Map<String, String> todayDate = new LinkedHashMap<>();
        todayDate.put(FilterRequestParameter.FROM, LabTabUtil.getTodayDate(false));
        todayDate.put(FilterRequestParameter.TO, LabTabUtil.getTodayDate(false));
        return todayDate;
    }

    private Map<String, String> getTomorrowDate() {
        Map<String, String> todayDate = new LinkedHashMap<>();
        todayDate.put(FilterRequestParameter.FROM, LabTabUtil.getTodayDate(true));
        todayDate.put(FilterRequestParameter.TO, LabTabUtil.getTodayDate(true));
        return todayDate;
    }

    @Override
    public void onSyncDone() {
        LabTabApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean syncStatus = SyncManager.getInstance().isLabDetailSynced();
                updateSyncStatus(syncStatus);
            }
        });
    }

    private void updateSyncStatus(boolean isSync) {
        if (isSync) {
            labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
        } else {
            labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_notsynced);
        }
    }

    private int getSeasonPosition(String season) {
        List list = Arrays.asList(homeActivityContext.getResources().getStringArray(R.array.array_season));
        return list.indexOf(season);
    }

    private int getYearPosition(int year) {
        List list = Arrays.asList(homeActivityContext.getResources().getStringArray(R.array.array_year));
        return list.indexOf(String.valueOf(year));
    }






}
