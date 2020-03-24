package org.wizbots.labtab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.LabListAdapter;
import org.wizbots.labtab.adapter.LocationAdapter;
import org.wizbots.labtab.adapter.RosterListAdapter;
import org.wizbots.labtab.adapter.SpinnerAdapter;
import org.wizbots.labtab.customview.EditTextCustom;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.database.LocationTable;
import org.wizbots.labtab.interfaces.OnSyncDoneListener;
import org.wizbots.labtab.interfaces.RosterListAdapterClickListener;
import org.wizbots.labtab.model.LocationResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RosterFragment extends ParentFragment implements OnSyncDoneListener, View.OnClickListener, RosterListAdapterClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private HomeActivity homeActivityContext;
    private Map<String, String> filterMap;
    private View rootView;

    private Toolbar toolbar;
    private Spinner spinnerLocation, spinnerYear, spinnerSeason;
    private EditTextCustom editTextSku;

    private String yearSearch, seasonSearch;
    private int yearPos, seasonPos;
    private RecyclerView recyclerViewRosterList;
    private ArrayList<Object> objectArrayList;
    private RosterListAdapter rosterListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LabTabApplication.getInstance().addUIListener(OnSyncDoneListener.class, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_roster, container, false);
        filterMap = new LinkedHashMap<String, String>();
        homeActivityContext = (HomeActivity) context;

        initViews();
        return rootView;
    }

    private void initViews() {
        //isFilter = true;
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        spinnerLocation = (Spinner) rootView.findViewById(R.id.spinner_location);
        spinnerYear = (Spinner) rootView.findViewById(R.id.spinner_year);
        spinnerSeason = (Spinner) rootView.findViewById(R.id.spinner_season);
        editTextSku = (EditTextCustom) rootView.findViewById(R.id.edit_text_sku);
        rootView.findViewById(R.id.spinner_location_frame).setOnClickListener(this);
        rootView.findViewById(R.id.spinner_year_frame).setOnClickListener(this);
        rootView.findViewById(R.id.spinner_season_frame).setOnClickListener(this);
        rootView.findViewById(R.id.edit_text_sku_frame).setOnClickListener(this);

        spinnerSeason.setAdapter(new SpinnerAdapter(homeActivityContext,
                Arrays.asList(homeActivityContext.getResources().getStringArray(R.array.array_season))));
        spinnerYear.setAdapter(new SpinnerAdapter(homeActivityContext,
                getYearsList()));

        // Default Selection
        spinnerYear.setSelection(5);
        yearSearch = (String) spinnerYear.getSelectedItem();
        yearPos = spinnerYear.getSelectedItemPosition();
        spinnerSeason.setSelection(4);
        String season = (String) spinnerSeason.getSelectedItem();
        seasonSearch = season != null ? season.toLowerCase() : "";
        seasonPos = spinnerSeason.getSelectedItemPosition();
        spinnerLocation.setAdapter(new LocationAdapter(homeActivityContext, getLocation(LocationTable.getInstance().getLocationList())));

        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.ROSTER);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

        recyclerViewRosterList = (RecyclerView) rootView.findViewById(R.id.recycler_view_roster_list);
        objectArrayList = new ArrayList<>();

        objectArrayList.add("Robotics Lab at Charter Learning Center(34705)");
        objectArrayList.add("Robotics Lab at Charter Learning Center(34706)");
        objectArrayList.add("Robotics Lab at Encinal Elementary(34471)");
        objectArrayList.add("Robotics Lab at Encinal Elementary(34472)");
        objectArrayList.add("Robotics Lab at North Star Academy(34218)");
        objectArrayList.add("Robotics Lab at North Star Academy(34219)");
        objectArrayList.add("Robotics Lab at North Star Academy(34986)");

        rosterListAdapter = new RosterListAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewRosterList.setLayoutManager(mLayoutManager);
        recyclerViewRosterList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRosterList.setAdapter(rosterListAdapter);
    }

    @Override
    public String getFragmentName() {
        return RosterFragment.class.getSimpleName();
    }

    @Override
    public void onSyncDone() {

    }

    @Override
    public void noInternetConnection() {

    }

    @Override
    public void onClick(View v) {

    }

    private List<String> getYearsList() {
        List<String> years = new ArrayList<>();
        years.add(getString(R.string.all_years));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int index = 4; index >= 0; index--) {
            years.add(String.valueOf(currentYear - index));
        }
        return years;
    }

    private ArrayList<LocationResponse> getLocation(ArrayList<LocationResponse> list) {
        ArrayList<LocationResponse> locationList = new ArrayList<>();
        locationList.addAll(list);

        Collections.sort(locationList, LocationResponse.locationResponseComparator);
        locationList.add(0, new LocationResponse("", "All Locations"));

        return locationList;
    }

    @Override
    public void onActionViewClick() {
        Bundle bundle = new Bundle();
        homeActivityContext.replaceFragment(Fragments.ROSTER_DETAILS_FRAGMENT, bundle);
    }
}
