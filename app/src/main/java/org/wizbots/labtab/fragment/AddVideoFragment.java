package org.wizbots.labtab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.HorizontalProjectCreatorAdapter;
import org.wizbots.labtab.adapter.ProjectCreatorAdapter;
import org.wizbots.labtab.customview.EditTextCustom;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.interfaces.HorizontalProjectCreatorAdapterClickListener;
import org.wizbots.labtab.interfaces.ProjectCreatorAdapterClickListener;
import org.wizbots.labtab.interfaces.StudentStatsAdapterClickListener;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class AddVideoFragment extends ParentFragment implements View.OnClickListener, StudentStatsAdapterClickListener, LabTabConstants, ProjectCreatorAdapterClickListener, HorizontalProjectCreatorAdapterClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;

    private ProjectCreatorAdapter projectCreatorAdapter;
    private HorizontalProjectCreatorAdapter horizontalProjectCreatorAdapter;

    private RecyclerView recyclerViewProjectCreator;
    private RecyclerView horizontalRecyclerViewProjectCreator;

    private ArrayList<Object> objectArrayListCreatorsAvailable = new ArrayList<>();
    private ArrayList<Object> objectArrayListCreatorsSelected = new ArrayList<>();

    private HomeActivity homeActivityContext;
    private EditTextCustom projectCreatorEditTextCustom;
    private LinearLayout recyclerViewContainer;
    private NestedScrollView nestedScrollView;
    private ArrayList<String> stringArrayList;
    private Spinner categorySpinner;

    public AddVideoFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_video, container, false);
        homeActivityContext = (HomeActivity) context;
        initView();
        prepareDummyList();
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(homeActivityContext, android.R.layout.simple_spinner_dropdown_item, stringArrayList);
        categorySpinner.setAdapter(spinnerArrayAdapter);
        addProjectCreatorEditTextListeners();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return LabDetailsFragment.class.getSimpleName();
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        projectCreatorEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_project_creators);
        recyclerViewContainer = (LinearLayout) rootView.findViewById(R.id.recycler_view_container);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.scroll_view_edit_video);
        categorySpinner = (Spinner) rootView.findViewById(R.id.spinner_category);
        stringArrayList = new ArrayList<>();

        labTabHeaderLayout.getDynamicTextViewCustom().setText("Add Video");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.menu);

        recyclerViewProjectCreator = (RecyclerView) rootView.findViewById(R.id.recycler_view_project_creators);
        horizontalRecyclerViewProjectCreator = (RecyclerView) rootView.findViewById(R.id.recycler_view_horizontal_project_creators);

        objectArrayListCreatorsAvailable = new ArrayList<>();
        objectArrayListCreatorsSelected = new ArrayList<>();

        projectCreatorAdapter = new ProjectCreatorAdapter(objectArrayListCreatorsAvailable, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewProjectCreator.setLayoutManager(mLayoutManager);
        recyclerViewProjectCreator.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProjectCreator.setAdapter(projectCreatorAdapter);

        horizontalProjectCreatorAdapter = new HorizontalProjectCreatorAdapter(objectArrayListCreatorsSelected, homeActivityContext, this);
        RecyclerView.LayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerViewProjectCreator.setLayoutManager(horizontalLayoutManager);
        horizontalRecyclerViewProjectCreator.setItemAnimator(new DefaultItemAnimator());
        horizontalRecyclerViewProjectCreator.setAdapter(horizontalProjectCreatorAdapter);

        recyclerViewProjectCreator.setVisibility(View.GONE);
        recyclerViewContainer.setVisibility(View.GONE);
    }

    public void prepareDummyList() {
        objectArrayListCreatorsAvailable.add("Harsh Sharma");
        objectArrayListCreatorsAvailable.add("Ankush Rana");
        objectArrayListCreatorsAvailable.add("Vineet Handa");
        objectArrayListCreatorsAvailable.add("Gaurav Bhardwaj");
        objectArrayListCreatorsAvailable.add("Niraj Raskoti");
        objectArrayListCreatorsAvailable.add("Shweta Jha");
        objectArrayListCreatorsAvailable.add("Nishant Arora");
        objectArrayListCreatorsAvailable.add("Arushi Sharma");
        objectArrayListCreatorsAvailable.add("Manav budhia");
        objectArrayListCreatorsAvailable.add("Harry Potter");
        objectArrayListCreatorsAvailable.add("John");
        objectArrayListCreatorsAvailable.add("Afghanistan");
        objectArrayListCreatorsAvailable.add("Albania");
        objectArrayListCreatorsAvailable.add("Algeria");
        objectArrayListCreatorsAvailable.add("Bangladesh");
        objectArrayListCreatorsAvailable.add("Belarus");
        objectArrayListCreatorsAvailable.add("Canada");
        objectArrayListCreatorsAvailable.add("Cape Verde");
        objectArrayListCreatorsAvailable.add("Central African Republic");
        objectArrayListCreatorsAvailable.add("Denmark");
        objectArrayListCreatorsAvailable.add("Dominican Republic");
        objectArrayListCreatorsAvailable.add("Egypt");
        objectArrayListCreatorsAvailable.add("France");
        objectArrayListCreatorsAvailable.add("Germany");
        objectArrayListCreatorsAvailable.add("Hong Kong");
        objectArrayListCreatorsAvailable.add("India");
        objectArrayListCreatorsAvailable.add("Iceland");


        stringArrayList.add("Category 1");
        stringArrayList.add("Category 2");
        stringArrayList.add("Category 3");
        stringArrayList.add("Category 4");
        stringArrayList.add("Category 5");
        stringArrayList.add("Category 6");

        projectCreatorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onActionViewClick() {
        homeActivityContext.replaceFragment(LabTabConstants.FRAGMENT_STUDENT_STATS_DETAILS);
    }

    @Override
    public void onProjectCreatorClick(final String string) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                objectArrayListCreatorsSelected.add(string);
                horizontalProjectCreatorAdapter.notifyDataSetChanged();
                recyclerViewContainer.setVisibility(View.GONE);
                recyclerViewProjectCreator.setVisibility(View.GONE);
                projectCreatorEditTextCustom.clearFocus();
                LabTabUtil.hideSoftKeyboard(homeActivityContext);
            }
        });
    }

    public void addProjectCreatorEditTextListeners() {

        projectCreatorEditTextCustom.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final ArrayList<Object> filteredList = new ArrayList<>();

                for (int i = 0; i < objectArrayListCreatorsAvailable.size(); i++) {

                    final String text = ((String) objectArrayListCreatorsAvailable.get(i)).toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(objectArrayListCreatorsAvailable.get(i));
                    }
                }

                projectCreatorAdapter = new ProjectCreatorAdapter(filteredList, homeActivityContext, AddVideoFragment.this);
                recyclerViewProjectCreator.setAdapter(projectCreatorAdapter);
                projectCreatorAdapter.notifyDataSetChanged();
            }
        });

        projectCreatorEditTextCustom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    recyclerViewContainer.setVisibility(View.GONE);
                    recyclerViewProjectCreator.setVisibility(View.GONE);
                } else {
                    projectCreatorEditTextCustom.requestFocus();
                    recyclerViewContainer.setVisibility(View.VISIBLE);
                    recyclerViewProjectCreator.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onProjectCreatorDeleteClick(final String string) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                objectArrayListCreatorsSelected.remove(string);
                horizontalProjectCreatorAdapter.notifyDataSetChanged();
            }
        });
    }

}
