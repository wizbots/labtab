package org.wizbots.labtab.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.HorizontalProjectCreatorAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.ButtonCustom;
import org.wizbots.labtab.customview.EditTextCustom;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.interfaces.HorizontalProjectCreatorAdapterClickListener;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.video.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ViewVideoFragment extends ParentFragment implements View.OnClickListener, HorizontalProjectCreatorAdapterClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;

    private HorizontalProjectCreatorAdapter horizontalProjectCreatorAdapter;
    private RecyclerView horizontalRecyclerViewProjectCreator;
    private ArrayList<Student> creatorsSelected = new ArrayList<>();

    private HomeActivity homeActivityContext;

    private ArrayList<String> categoryArrayList;
    private Spinner categorySpinner;

    private ImageView videoThumbnailImageView, closeImageView;
    private ButtonCustom createButtonCustom, cancelButtonCustom;
    private EditTextCustom titleEditTextCustom, projectCreatorEditTextCustom, knowledgeNuggetsEditTextCustom, descriptionEditTextCustom, notesToTheFamilyEditTextCustom;
    private TextViewCustom mentorNameTextViewCustom, labSKUTextViewCustom;
    private LinearLayout closeLinearLayout;

    private Uri savedVideoUri;
    private Video video;

    public ViewVideoFragment() {

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
        video = getArguments().getParcelable(VideoListFragment.VIDEO);
        initView();
        fetchDataFromBundle();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return LabDetailsFragment.class.getSimpleName();
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);

        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        categorySpinner = (Spinner) rootView.findViewById(R.id.spinner_category);
        categoryArrayList = new ArrayList<>();

        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.VIEW_VIDEO);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);


        projectCreatorEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_project_creators);
        titleEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_title);
        knowledgeNuggetsEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_knowledge_nuggets);
        descriptionEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_description);
        notesToTheFamilyEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_notes_to_the_family);
        projectCreatorEditTextCustom.setFocusable(false);
        titleEditTextCustom.setFocusable(false);
        knowledgeNuggetsEditTextCustom.setFocusable(false);
        descriptionEditTextCustom.setFocusable(false);
        notesToTheFamilyEditTextCustom.setFocusable(false);

        mentorNameTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_mentor_name);
        labSKUTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_lab_sku);

        horizontalRecyclerViewProjectCreator = (RecyclerView) rootView.findViewById(R.id.recycler_view_horizontal_project_creators);
        creatorsSelected = new ArrayList<>();
        horizontalProjectCreatorAdapter = new HorizontalProjectCreatorAdapter(creatorsSelected, homeActivityContext, this);

        RecyclerView.LayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerViewProjectCreator.setLayoutManager(horizontalLayoutManager);
        horizontalRecyclerViewProjectCreator.setItemAnimator(new DefaultItemAnimator());
        horizontalRecyclerViewProjectCreator.setAdapter(horizontalProjectCreatorAdapter);


        videoThumbnailImageView = (ImageView) rootView.findViewById(R.id.iv_video_thumbnail);
        closeImageView = (ImageView) rootView.findViewById(R.id.iv_close);
        closeLinearLayout = (LinearLayout) rootView.findViewById(R.id.ll_close);
        createButtonCustom = (ButtonCustom) rootView.findViewById(R.id.btn_create);
        cancelButtonCustom = (ButtonCustom) rootView.findViewById(R.id.btn_cancel);

        videoThumbnailImageView.setOnClickListener(this);
        createButtonCustom.setVisibility(View.GONE);
        cancelButtonCustom.setVisibility(View.GONE);
        closeImageView.setOnClickListener(this);
        closeLinearLayout.setOnClickListener(this);
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
    }

    public void prepareCategoryList() {
        String[] categories = homeActivityContext.getResources().getStringArray(R.array.array_category);
        categoryArrayList.addAll(Arrays.asList(categories));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video_thumbnail:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getPath()));
                intent.setDataAndType(Uri.parse(video.getPath()), "video/*");
                startActivity(intent);
                break;
            case R.id.iv_close:
                homeActivityContext.onBackPressed();
                break;
            case R.id.ll_close:
                homeActivityContext.onBackPressed();
                break;

        }
    }

    @Override
    public void onProjectCreatorDeleteClick(Student student) {

    }

    public void fetchDataFromBundle() {
        prepareCategoryList();
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(homeActivityContext, android.R.layout.simple_spinner_dropdown_item, categoryArrayList);
        categorySpinner.setAdapter(spinnerArrayAdapter);

        for (String s : categoryArrayList) {
            if (s.equals(video.getCategory())) {
                categorySpinner.setSelection(categoryArrayList.indexOf(s));
                break;
            }
        }

        titleEditTextCustom.setText(video.getTitle());
        mentorNameTextViewCustom.setText(video.getMentor_name());
        labSKUTextViewCustom.setText(video.getLab_sku());
        knowledgeNuggetsEditTextCustom.setText(video.getKnowledge_nuggets());
        descriptionEditTextCustom.setText(video.getDescription());
        notesToTheFamilyEditTextCustom.setText(video.getNotes_to_the_family());

        ArrayList<Student> objectArrayList = new Gson().fromJson(video.getProject_creators(), new TypeToken<ArrayList<Student>>() {
        }.getType());
        creatorsSelected.addAll(objectArrayList);
        horizontalProjectCreatorAdapter.notifyDataSetChanged();
        Glide.with(context)
                .load(Uri.fromFile(new File(video.getPath())))
                .into(videoThumbnailImageView);
    }
}
