package org.wizbots.labtab.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.activity.TrimmerActivity;
import org.wizbots.labtab.adapter.HorizontalProjectCreatorAdapter;
import org.wizbots.labtab.adapter.ProjectCreatorAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.ButtonCustom;
import org.wizbots.labtab.customview.EditTextCustom;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.interfaces.HorizontalProjectCreatorAdapterClickListener;
import org.wizbots.labtab.interfaces.ProjectCreatorAdapterClickListener;
import org.wizbots.labtab.model.Video;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.service.LabTabUploadService;
import org.wizbots.labtab.util.LabTabUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import life.knowledge4.videotrimmer.utils.FileUtils;

public class AddVideoFragment extends ParentFragment implements View.OnClickListener, ProjectCreatorAdapterClickListener, HorizontalProjectCreatorAdapterClickListener {

    public static final int REQUEST_CODE_TRIM_VIDEO = 300;
    public static final String URI = "URI";
    public static final String PROJECT_CREATORS = "PROJECT_CREATORS";
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
    private LinearLayout recyclerViewContainer;
    private NestedScrollView nestedScrollView;
    private ArrayList<String> stringArrayList;
    private Spinner categorySpinner;
    private ImageView videoThumbnailImageView, closeImageView;
    private EditTextCustom titleEditTextCustom, projectCreatorEditTextCustom, knowledgeNuggetsEditTextCustom, descriptionEditTextCustom, notesToTheFamilyEditTextCustom;
    private ButtonCustom createButtonCustom, cancelButtonCustom;
    private TextViewCustom mentorNameTextViewCustom, labSKUTextViewCustom;
    private LinearLayout closeLinearLayout;

    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;
    private Uri savedVideoUri;
    private Program program;

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
        program = getArguments().getParcelable(LabDetailsFragment.PROGRAM);
        initView(savedInstanceState);
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

    public void initView(Bundle bundle) {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        projectCreatorEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_project_creators);
        recyclerViewContainer = (LinearLayout) rootView.findViewById(R.id.recycler_view_container);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.scroll_view_edit_video);
        categorySpinner = (Spinner) rootView.findViewById(R.id.spinner_category);
        stringArrayList = new ArrayList<>();

        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.ADD_VIDEO);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

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

        videoThumbnailImageView = (ImageView) rootView.findViewById(R.id.iv_video_thumbnail);
        closeImageView = (ImageView) rootView.findViewById(R.id.iv_close);
        closeLinearLayout = (LinearLayout) rootView.findViewById(R.id.ll_close);
        titleEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_title);
        knowledgeNuggetsEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_knowledge_nuggets);
        descriptionEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_description);
        notesToTheFamilyEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_notes_to_the_family);
        createButtonCustom = (ButtonCustom) rootView.findViewById(R.id.btn_create);
        cancelButtonCustom = (ButtonCustom) rootView.findViewById(R.id.btn_cancel);

        mentorNameTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_mentor_name);
        labSKUTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_lab_sku);

        mentorNameTextViewCustom.setText(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
        labSKUTextViewCustom.setText(String.valueOf(program.getSku()));

        videoThumbnailImageView.setOnClickListener(this);
        createButtonCustom.setOnClickListener(this);
        cancelButtonCustom.setOnClickListener(this);
        closeImageView.setOnClickListener(this);
        closeLinearLayout.setOnClickListener(this);

        if (bundle != null) {
            savedVideoUri = bundle.getParcelable(URI);
            if (savedVideoUri != null) {
                Glide
                        .with(homeActivityContext)
                        .load(Uri.fromFile(new File(savedVideoUri.getPath())))
                        .into(videoThumbnailImageView);
            }
            ArrayList<Object> objects = (ArrayList<Object>) bundle.getSerializable(PROJECT_CREATORS);
            if (!objects.isEmpty()) {
                objectArrayListCreatorsSelected.addAll(objects);
                horizontalProjectCreatorAdapter.notifyDataSetChanged();
            }
        }
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
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


        stringArrayList.add("Select Category");
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
        switch (view.getId()) {
            case R.id.iv_video_thumbnail:
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                // create a file to save the video
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                // set the image file name
                if (fileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                }
                // set the video image quality to high
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra("android.intent.extra.durationLimit", 300);
                // start the Video Capture Intent
                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.btn_create:
                if (savedVideoUri == null) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Capture A Video First");
                    break;
                }


                if (titleEditTextCustom.getText().toString().length() < 5) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Title must consist at least 5 words");
                    break;
                }

                if (categorySpinner.getSelectedItemPosition() == 0) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Select Category");
                    break;
                }

                if (titleEditTextCustom.getText().toString().length() == 0) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Give A Title To Video");
                    break;
                }

                if (knowledgeNuggetsEditTextCustom.getText().toString().length() < 5) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Knowledge Nuggets must consist 5 words");
                    break;
                }

                if (descriptionEditTextCustom.getText().toString().length() < 5) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Description must consist 5 words");
                    break;
                }

                if (objectArrayListCreatorsSelected.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Select at least one creator");
                    break;
                }

                if (notesToTheFamilyEditTextCustom.getText().toString().length() < 5) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Notes must consist 5 words");
                    break;
                }

                Video video = new Video();
                video.setId(Calendar.getInstance().getTimeInMillis() + "");
                video.setMentor_id(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
                video.setStatus(0);
                video.setTitle(titleEditTextCustom.getText().toString());
                video.setPath(savedVideoUri.getPath());
                video.setCategory((String) (categorySpinner.getSelectedItem()));
                video.setMentor_name(mentorNameTextViewCustom.getText().toString());
                video.setLab_sku(labSKUTextViewCustom.getText().toString());
                video.setLab_level(LabLevels.APPRENTICE);
                video.setKnowledge_nuggets(knowledgeNuggetsEditTextCustom.getText().toString());
                video.setDescription(descriptionEditTextCustom.getText().toString());
                video.setProject_creators(LabTabUtil.toJson(objectArrayListCreatorsSelected));
                video.setNotes_to_the_family(notesToTheFamilyEditTextCustom.getText().toString());
                VideoTable.getInstance().insert(video);
                Intent uploadService = new Intent(homeActivityContext, LabTabUploadService.class);
                uploadService.putExtra(LabTabUploadService.EVENT, Events.ADD_VIDEO);
                homeActivityContext.startService(uploadService);
                homeActivityContext.clearAllTheFragmentFromStack();
                homeActivityContext.replaceFragment(Fragments.HOME, new Bundle());
                break;
            case R.id.btn_cancel:
                homeActivityContext.onBackPressed();
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

    /**
     * Create a file Uri for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(int type) {

        if (Environment.getExternalStorageState() == null) {
            return null;
        }
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "MyCameraVideo");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

                //output.setText("Failed to create directory MyCameraVideo.");

                Toast.makeText(homeActivityContext, "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();

                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }


        // Create a media file name

        // For unique file name appending current timeStamp with file name
    /*    java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());*/

        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + System.currentTimeMillis() + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (fileUri != null) {
                    final Uri selectedUri = fileUri;
                    if (selectedUri != null) {
                        startTrimActivity(selectedUri);
                    } else {
                        Toast.makeText(homeActivityContext, "Video saved to: " + data.getData(), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(homeActivityContext, "User cancelled the video capture.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(homeActivityContext, "Video capture failed.",
                        Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_TRIM_VIDEO) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("URI");
                savedVideoUri = uri;
                Glide
                        .with(homeActivityContext)
                        .load(Uri.fromFile(new File(savedVideoUri.getPath())))
                        .into(videoThumbnailImageView);
                Log.d("Trimming Activity", uri.getPath());
            } else {
                Log.d("Trimming Activity", "Result for trimming video canceled");
            }
        }
    }

    private void startTrimActivity(@NonNull Uri uri) {
        Intent intent = new Intent(homeActivityContext, TrimmerActivity.class);
        intent.putExtra(EXTRA_VIDEO_PATH, FileUtils.getPath(homeActivityContext, uri));
        startActivityForResult(intent, REQUEST_CODE_TRIM_VIDEO);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(URI, savedVideoUri);
        outState.putSerializable(PROJECT_CREATORS, objectArrayListCreatorsSelected);
        super.onSaveInstanceState(outState);
    }
}
