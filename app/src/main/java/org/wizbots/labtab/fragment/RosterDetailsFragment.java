package org.wizbots.labtab.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.RosterDetailsAdapter;
import org.wizbots.labtab.adapter.RosterFirstListAdapter;
import org.wizbots.labtab.adapter.RosterThirdListAdapter;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.interfaces.OnSyncDoneListener;
import org.wizbots.labtab.model.roster.RosterFirstListItem;
import org.wizbots.labtab.model.roster.RosterThirdListItem;
import org.wizbots.labtab.model.roster.StudentListItem;
import org.wizbots.labtab.service.SyncManager;

import java.util.ArrayList;

public class RosterDetailsFragment extends ParentFragment implements OnSyncDoneListener {

    private View rootView;
    private Toolbar toolbar;
    private HomeActivity homeActivityContext;
    private LabTabHeaderLayout labTabHeaderLayout;
    private TextViewCustom labSKUTextViewCustom, availabilityTextViewCustom,
            nameTextViewCustom, locationTextViewCustom, categoryTextViewCustom,
            roomTextViewCustom, gradesTextViewCustom, priceTextViewCustom;
    private RecyclerView recyclerViewRosterDetails, recyclerViewThirdList, recyclerViewFirstList;
    private ArrayList<StudentListItem> studentArrayList;
    private ArrayList<RosterThirdListItem> rosterThirdListItemArrayList;
    private ArrayList<RosterFirstListItem> rosterFirstListItemArrayList;
    private RosterDetailsAdapter rosterDetailsAdapter;
    private RosterThirdListAdapter rosterThirdListAdapter;
    private RosterFirstListAdapter rosterFirstListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LabTabApplication.getInstance().addUIListener(OnSyncDoneListener.class, this);

        homeActivityContext = (HomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_roster_details, container, false);


        initViews();
        return rootView;
    }

    private void initViews() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.ROSTER);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);

        recyclerViewRosterDetails = (RecyclerView) rootView.findViewById(R.id.recycler_view_roster_details_shifts);
        recyclerViewThirdList = (RecyclerView) rootView.findViewById(R.id.recycler_view_roster_details_third);
        recyclerViewFirstList = (RecyclerView) rootView.findViewById(R.id.recycler_view_roster_details_first);
        initHeaderView();

        initRecyclerViews();

    }

    private void initRecyclerViews() {
        initFirstRecyclerView();
        initSecondRecyclerView();
        initThirdRecyclerView();
    }

    private void initThirdRecyclerView() {
        rosterThirdListItemArrayList = new ArrayList<>();
        rosterThirdListItemArrayList.add(new RosterThirdListItem("David Yurovitsky", "Yes", "No", "Escondido Kids Club", "650-855-9828"));
        rosterThirdListItemArrayList.add(new RosterThirdListItem("Ezra Milbourne", "No", "No", "None", "None"));
        rosterThirdListItemArrayList.add(new RosterThirdListItem("David Yurovitsky", "No", "No", "None", "None"));
        rosterThirdListItemArrayList.add(new RosterThirdListItem("David Yurovitsky", "No", "No", "None", "None"));
        rosterThirdListItemArrayList.add(new RosterThirdListItem("David Yurovitsky", "No", "No", "None", "None"));
        rosterThirdListItemArrayList.add(new RosterThirdListItem("David Yurovitsky", "No", "No", "None", "None"));
        rosterThirdListItemArrayList.add(new RosterThirdListItem("David Yurovitsky", "No", "No", "None", "None"));

        rosterThirdListAdapter = new RosterThirdListAdapter(rosterThirdListItemArrayList, homeActivityContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewThirdList.setLayoutManager(mLayoutManager);
        recyclerViewThirdList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewThirdList.setAdapter(rosterThirdListAdapter);
    }

    private void initFirstRecyclerView() {
        rosterFirstListItemArrayList = new ArrayList<>();
        rosterFirstListItemArrayList.add(new RosterFirstListItem("David Yoroviskiv", "M", "9", "3", "Vandana Ayaa", "4255582567", "vandana@gmail.com", "879 Marshall Dr, Suite Palo Alto, CA, 98765", "Dust, Cat and Dog fur", false));
        rosterFirstListItemArrayList.add(new RosterFirstListItem("David Yoroviskiv", "M", "9", "3", "Vandana Ayaa", "4255582567", "vandana@gmail.com", "879 Marshall Dr, Suite Palo Alto, CA, 98765", "Dust, Cat and Dog fur", false));
        rosterFirstListItemArrayList.add(new RosterFirstListItem("David Yoroviskiv", "M", "9", "3", "Vandana Ayaa", "4255582567", "vandana@gmail.com", "879 Marshall Dr, Suite Palo Alto, CA, 98765", "Dust, Cat and Dog fur", false));
        rosterFirstListItemArrayList.add(new RosterFirstListItem("David Yoroviskiv", "M", "9", "3", "Vandana Ayaa", "4255582567", "vandana@gmail.com", "879 Marshall Dr, Suite Palo Alto, CA, 98765", "Dust, Cat and Dog fur", false));
        rosterFirstListItemArrayList.add(new RosterFirstListItem("David Yoroviskiv", "M", "9", "3", "Vandana Ayaa", "4255582567", "vandana@gmail.com", "879 Marshall Dr, Suite Palo Alto, CA, 98765", "Dust, Cat and Dog fur", false));
        rosterFirstListItemArrayList.add(new RosterFirstListItem("David Yoroviskiv", "M", "9", "3", "Vandana Ayaa", "4255582567", "vandana@gmail.com", "879 Marshall Dr, Suite Palo Alto, CA, 98765", "Dust, Cat and Dog fur", false));
        rosterFirstListItemArrayList.add(new RosterFirstListItem("David Yoroviskiv", "M", "9", "3", "Vandana Ayaa", "4255582567", "vandana@gmail.com", "879 Marshall Dr, Suite Palo Alto, CA, 98765", "Dust, Cat and Dog fur", false));
        rosterFirstListAdapter = new RosterFirstListAdapter(rosterFirstListItemArrayList, homeActivityContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewFirstList.setLayoutManager(mLayoutManager);
        recyclerViewFirstList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFirstList.setAdapter(rosterFirstListAdapter);
    }

    private void initSecondRecyclerView() {
        studentArrayList = new ArrayList<>();
        studentArrayList.add(new StudentListItem("David Yurovitski", "David Yurovitski", "David Yurovitski", ""));
        studentArrayList.add(new StudentListItem("David Yurovitski", "David Yurovitski", "David Yurovitski", ""));
        studentArrayList.add(new StudentListItem("David Yurovitski", "David Yurovitski", "David Yurovitski", ""));
        studentArrayList.add(new StudentListItem("David Yurovitski", "David Yurovitski", "David Yurovitski", ""));
        studentArrayList.add(new StudentListItem("David Yurovitski", "David Yurovitski", "David Yurovitski", ""));
        studentArrayList.add(new StudentListItem("David Yurovitski", "David Yurovitski", "David Yurovitski", ""));
        studentArrayList.add(new StudentListItem("David Yurovitski", "David Yurovitski", "David Yurovitski", ""));
        studentArrayList.add(new StudentListItem("David Yurovitski", "David Yurovitski", "David Yurovitski", ""));

        rosterDetailsAdapter = new RosterDetailsAdapter(studentArrayList, homeActivityContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewRosterDetails.setLayoutManager(mLayoutManager);
        recyclerViewRosterDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRosterDetails.setAdapter(rosterDetailsAdapter);
    }

    private void initHeaderView() {
        labSKUTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_lab_sku);
        availabilityTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_availability);
        nameTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_name);
        locationTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_location);
        categoryTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_category);
        roomTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_room);
        gradesTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_grades);
        priceTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_price);

        labSKUTextViewCustom.setText("----");
        availabilityTextViewCustom.setText("----");
        nameTextViewCustom.setText("----");
        locationTextViewCustom.setText("----");
        categoryTextViewCustom.setText("----");
        roomTextViewCustom.setText("----");
        gradesTextViewCustom.setText("----");
        priceTextViewCustom.setText("----");
    }

    @Override
    public String getFragmentName() {
        return RosterDetailsFragment.class.getSimpleName();
    }

    @Override
    public void onSyncDone() {
        LabTabApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean syncStatus = SyncManager.getInstance().isLabDetailSynced();
                Log.d("RosterDetails", "onSyncDone " + syncStatus);
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

    @Override
    public void noInternetConnection() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LabTabApplication.getInstance().removeUIListener(OnSyncDoneListener.class, this);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
