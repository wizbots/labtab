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
import org.wizbots.labtab.adapter.ListOfSkipsAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.database.ProgramAbsencesTable;
import org.wizbots.labtab.interfaces.ListOfSkipsAdapterClickListener;
import org.wizbots.labtab.interfaces.OnSyncDoneListener;
import org.wizbots.labtab.interfaces.requesters.GetStudentsListener;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.requesters.GetStudentsRequester;
import org.wizbots.labtab.service.SyncManager;
import org.wizbots.labtab.util.BackgroundExecutor;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;
import java.util.Date;

public class ListOfSkipsFragment extends ParentFragment implements ListOfSkipsAdapterClickListener,
        GetStudentsListener, OnSyncDoneListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private ListOfSkipsAdapter listOfSkipsAdapter;
    private RecyclerView recyclerViewListOfSkips;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;
    private Program program;
    private ProgressDialog progressDialog;
    private TextViewCustom labSKUTextViewCustom, availabilityTextViewCustom,
            nameTextViewCustom, locationTextViewCustom, categoryTextViewCustom,
            roomTextViewCustom, gradesTextViewCustom, priceTextViewCustom,
            fromTextViewCustom, toTextViewCustom, timeSlotTextViewCustom, dayTextViewCustom;
    private String labLevel = "";

    public ListOfSkipsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LabTabApplication.getInstance().addUIListener(OnSyncDoneListener.class, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_of_skips, container, false);
        homeActivityContext = (HomeActivity) context;
        program = getArguments().getParcelable(LabDetailsFragment.PROGRAM);
        labLevel = getArguments().getString(LabDetailsFragment.LAB_LEVEL);
        initView();
        initListeners();
        return rootView;
    }

    public void initView() {
        progressDialog = new ProgressDialog(homeActivityContext);
        progressDialog.setMessage("processing");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.LIST_OF_SKIPS);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
        initHeaderView();

        recyclerViewListOfSkips = (RecyclerView) rootView.findViewById(R.id.recycler_view_list_of_skips);
//        recyclerViewListOfSkips.setFocusable(false);
        objectArrayList = new ArrayList<>();

        listOfSkipsAdapter = new ListOfSkipsAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListOfSkips.setLayoutManager(mLayoutManager);
        recyclerViewListOfSkips.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListOfSkips.setAdapter(listOfSkipsAdapter);
        rootView.findViewById(R.id.btn_additional).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
                bundle.putString(LabDetailsFragment.LAB_LEVEL, labLevel);
                homeActivityContext.replaceFragment(Fragments.ADDITIONAL_INFORMATION, bundle);
            }
        });
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());

        if (program != null) {
            ArrayList<Absence> absenceArrayList = ProgramAbsencesTable.getInstance().getAbsencesListByProgramId(program.getId());
            if (!absenceArrayList.isEmpty()) {
                objectArrayList.addAll(absenceArrayList);
                listOfSkipsAdapter.notifyDataSetChanged();
            }else if(!LabTabApplication.getInstance().isNetworkAvailable() && absenceArrayList.isEmpty()){
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_DATA_NO_CONNECTION);
            } else {
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_LIST_OF_SKIPS_FOUND_FOR_THIS_LAB);
            }
            setHeaderView(program);
            progressDialog.dismiss();
        } else {
            progressDialog.dismiss();
            if(!LabTabApplication.getInstance().isNetworkAvailable() && program == null){
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_DATA_NO_CONNECTION);
            }
        }
        rootView.findViewById(R.id.ll_add_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!objectArrayList.isEmpty()) {
                    progressDialog.show();
                    BackgroundExecutor.getInstance().execute(new GetStudentsRequester(ListOfSkipsFragment.this, getSelectStudentsId(), program));
                } else {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.AT_LEAST_ONE_STUDENT_IS_NEEDED_TO_ADD_VIDEO_FOR_THIS_LAB);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        SyncManager.getInstance().onRefreshData(1);
        boolean isSync = SyncManager.getInstance().isMarkAbsentSync();
        if(isSync){
            updateSyncStatus(true);
        }else {
            updateSyncStatus(false);
        }
    }

    @Override
    public String getFragmentName() {
        return ListOfSkipsFragment.class.getSimpleName();
    }


    @Override
    public void onActionViewClick() {

    }

    @Override
    public void onCheckChanged(int position, boolean value) {
        ((Absence) objectArrayList.get(position)).setCheck(value);
    }

    public void initHeaderView() {
        labSKUTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_lab_sku);
        availabilityTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_availability);
        nameTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_name);
        locationTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_location);
        categoryTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_category);
        roomTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_room);
        gradesTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_grades);
        priceTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_price);
        fromTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_from);
        toTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_to);
        timeSlotTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_time_slot);
        dayTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_day);

        labSKUTextViewCustom.setText("----");
        availabilityTextViewCustom.setText("----");
        nameTextViewCustom.setText("----");
        locationTextViewCustom.setText("----");
        categoryTextViewCustom.setText("----");
        roomTextViewCustom.setText("----");
        gradesTextViewCustom.setText("----");
        priceTextViewCustom.setText("----");
        fromTextViewCustom.setText("----");
        toTextViewCustom.setText("----");
        timeSlotTextViewCustom.setText("----");
        dayTextViewCustom.setText("----");
    }

    public void setHeaderView(Program program) {
        labSKUTextViewCustom.setText(String.valueOf(program.getSku()));
        availabilityTextViewCustom.setText(program.getAvailability());
        nameTextViewCustom.setText(program.getName());
        locationTextViewCustom.setText(program.getLocation());
        categoryTextViewCustom.setText(program.getCategory());
        roomTextViewCustom.setText(program.getRoom());
        gradesTextViewCustom.setText(program.getGrades());
        priceTextViewCustom.setText(program.getPrice());
        fromTextViewCustom.setText(program.getStarts());
        toTextViewCustom.setText(program.getEnds());
        timeSlotTextViewCustom.setText(program.getTime_slot());
        dayTextViewCustom.setText(LabTabUtil.getFormattedDate(DateFormat.DEFAULT, new Date()));
    }

    @Override
    public void onDestroy() {
        LabTabApplication.getInstance().removeUIListener(GetStudentsListener.class, this);
        LabTabApplication.getInstance().removeUIListener(OnSyncDoneListener.class, this);
        progressDialog.dismiss();
        super.onDestroy();
    }

    private ArrayList<String> getSelectStudentsId() {
        ArrayList<String> selectedStudentsIds = new ArrayList<>();
        for (Object object : objectArrayList) {
            if (((Absence) object).isCheck()) {
                selectedStudentsIds.add(((Absence) object).getStudent_id());
            }
        }
        return selectedStudentsIds;
    }


    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(GetStudentsListener.class, this);
    }

    @Override
    public void studentsFetched(final ArrayList<Student> studentArrayList) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
                bundle.putString(LabDetailsFragment.LAB_LEVEL, labLevel);
                bundle.putSerializable(LabDetailsFragment.SELECTED_STUDENTS, studentArrayList);
                homeActivityContext.replaceFragment(Fragments.ADD_VIDEO, bundle);
            }
        });
    }

    @Override
    public void onSyncDone() {
        LabTabApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean syncStatus = SyncManager.getInstance().isMarkAbsentSync();
                updateSyncStatus(syncStatus);
            }
        });
    }

    private void updateSyncStatus(boolean isSync){
        if (isSync) {
            labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
        } else {
            labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_notsynced);
        }
    }
}
