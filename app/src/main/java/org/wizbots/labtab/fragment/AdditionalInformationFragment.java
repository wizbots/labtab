package org.wizbots.labtab.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.AdditionalInformationAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.interfaces.AdditionalInformationAdapterClickListener;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;
import java.util.Date;

public class AdditionalInformationFragment extends ParentFragment implements AdditionalInformationAdapterClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private AdditionalInformationAdapter additionalInformationAdapter;
    private RecyclerView recyclerViewAdditionalInformation;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;
    private Program program;
    private ProgressDialog progressDialog;
    private TextViewCustom labSKUTextViewCustom, availabilityTextViewCustom,
            nameTextViewCustom, locationTextViewCustom, categoryTextViewCustom,
            roomTextViewCustom, gradesTextViewCustom, priceTextViewCustom,
            fromTextViewCustom, toTextViewCustom, timeSlotTextViewCustom, dayTextViewCustom;
    private String labLevel = "";

    public AdditionalInformationFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_additional_information, container, false);
        homeActivityContext = (HomeActivity) context;
        program = getArguments().getParcelable(LabDetailsFragment.PROGRAM);
        labLevel = getArguments().getString(LabDetailsFragment.LAB_LEVEL);
        initView();
        return rootView;
    }

    public void initView() {
        progressDialog = new ProgressDialog(homeActivityContext);
        progressDialog.setMessage("processing");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.ADDITIONAL_INFORMATION);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
        initHeaderView();

        recyclerViewAdditionalInformation = (RecyclerView) rootView.findViewById(R.id.recycler_view_additional_information);
//        recyclerViewAdditionalInformation.setFocusable(false);
        objectArrayList = new ArrayList<>();

        additionalInformationAdapter = new AdditionalInformationAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAdditionalInformation.setLayoutManager(mLayoutManager);
        recyclerViewAdditionalInformation.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAdditionalInformation.setAdapter(additionalInformationAdapter);
        rootView.findViewById(R.id.btn_absences).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
                bundle.putString(LabDetailsFragment.LAB_LEVEL, labLevel);
                homeActivityContext.replaceFragment(Fragments.LIST_OF_SKIPS, bundle);
            }
        });
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());

        if (program != null) {
            ArrayList<Student> studentArrayList = ProgramStudentsTable.getInstance().getStudentsListByProgramId(program.getId());
            if (!studentArrayList.isEmpty()) {
                objectArrayList.addAll(studentArrayList);
                additionalInformationAdapter.notifyDataSetChanged();
            }else if(LabTabApplication.getInstance().isNetworkAvailable() && studentArrayList.isEmpty()){
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_DATA_NO_CONNECTION);
            } else {
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_ADDITIONAL_INFO_FOUND_FOR_THIS_LAB);
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
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(LabDetailsFragment.PROGRAM, program);
                    bundle.putSerializable(LabDetailsFragment.SELECTED_STUDENTS, getSelectedStudents());
                    bundle.putString(LabDetailsFragment.LAB_LEVEL, labLevel);
                    homeActivityContext.replaceFragment(Fragments.ADD_VIDEO, bundle);
                } else {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.AT_LEAST_ONE_STUDENT_IS_NEEDED_TO_ADD_VIDEO_FOR_THIS_LAB);
                }
            }
        });

    }

    @Override
    public String getFragmentName() {
        return AdditionalInformationFragment.class.getSimpleName();
    }


    @Override
    public void onActionViewClick() {

    }

    @Override
    public void onCheckChanged(int position, boolean value) {
        ((Student) objectArrayList.get(position)).setCheck(value);
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
        progressDialog.dismiss();
        super.onDestroy();
    }

    private ArrayList<Student> getSelectedStudents() {
        ArrayList<Student> studentArrayList = new ArrayList<>();
        for (Object object : objectArrayList) {
            if (((Student) object).isCheck()) {
                studentArrayList.add((Student) object);
            }
        }
        return studentArrayList;
    }

}
