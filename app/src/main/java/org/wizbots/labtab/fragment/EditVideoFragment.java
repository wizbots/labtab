package org.wizbots.labtab.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.interfaces.HorizontalProjectCreatorAdapterClickListener;
import org.wizbots.labtab.interfaces.ProjectCreatorAdapterClickListener;
import org.wizbots.labtab.interfaces.requesters.EditProjectListener;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.model.video.response.EditProjectResponse;
import org.wizbots.labtab.requesters.EditProjectRequester;
import org.wizbots.labtab.util.BackgroundExecutor;
import org.wizbots.labtab.util.LabTabUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import life.knowledge4.videotrimmer.utils.FileUtils;

public class EditVideoFragment extends ParentFragment implements View.OnClickListener, ProjectCreatorAdapterClickListener, HorizontalProjectCreatorAdapterClickListener, EditProjectListener {

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

    private ArrayList<Student> creatorsAvailable = new ArrayList<>();
    private ArrayList<Student> creatorsSelected = new ArrayList<>();

    private HomeActivity homeActivityContext;
    private LinearLayout recyclerViewContainer;
    private NestedScrollView nestedScrollView;
    private ArrayList<String> categoryArrayList;
    private Spinner categorySpinner;
    private ImageView videoThumbnailImageView, closeImageView;
    private EditTextCustom titleEditTextCustom, projectCreatorEditTextCustom, descriptionEditTextCustom, notesToTheFamilyEditTextCustom;
    private TextViewCustom mentorNameTextViewCustom, labSKUTextViewCustom, componentTextViewCustom, knowledgeNuggetsEditTextCustom;
    private ButtonCustom saveButtonCustom, cancelButtonCustom;
    private LinearLayout closeLinearLayout;

    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;
    private Uri savedVideoUri;
    private Video video;
    private AlertDialog.Builder builder;
    private ArrayList<String> knowledgeNuggets = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String knowledgeNuggetsSelected = "";
    private String editVideoCase = "";

    public EditVideoFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_edit_video, container, false);
        homeActivityContext = (HomeActivity) context;
        initListeners();
        initView(savedInstanceState);
        if (savedInstanceState == null) {
            video = getArguments().getParcelable(VideoListFragment.VIDEO);
            savedVideoUri = Uri.parse(video.getPath());
//            knowledgeNuggetsSelected = LabTabUtil.toJson(getKnowledgeNuggets(knowledgeNuggets));
            editVideoCase = getArguments().getString(VideoListFragment.VIDEO_EDIT_CASE, VideoEditCase.INTERNET_ON);
            fetchDataFromBundle();
        } else {
            editVideoCase = savedInstanceState.getString(VideoListFragment.VIDEO_EDIT_CASE, VideoEditCase.INTERNET_ON);
            video = savedInstanceState.getParcelable(VideoListFragment.VIDEO);
            savedVideoUri = Uri.parse(video.getPath());
        }
        initKnowledgeNuggets(savedInstanceState);
        prepareStudentsCategoryList();
        initCategory();
        addProjectCreatorEditTextListeners();
        LabTabUtil.videoRequestOnOpeningEditScreen = compareChangeWithThis();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return LabDetailsFragment.class.getSimpleName();
    }

    public void initView(Bundle bundle) {
        progressDialog = new ProgressDialog(homeActivityContext);
        progressDialog.setMessage("processing");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        projectCreatorEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_project_creators);
        recyclerViewContainer = (LinearLayout) rootView.findViewById(R.id.recycler_view_container);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.scroll_view_edit_video);
        categorySpinner = (Spinner) rootView.findViewById(R.id.spinner_category);
        categoryArrayList = new ArrayList<>();

        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.EDIT_VIDEO);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

        recyclerViewProjectCreator = (RecyclerView) rootView.findViewById(R.id.recycler_view_project_creators);
        horizontalRecyclerViewProjectCreator = (RecyclerView) rootView.findViewById(R.id.recycler_view_horizontal_project_creators);

        creatorsAvailable = new ArrayList<>();
        creatorsSelected = new ArrayList<>();

        projectCreatorAdapter = new ProjectCreatorAdapter(creatorsAvailable, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewProjectCreator.setLayoutManager(mLayoutManager);
        recyclerViewProjectCreator.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProjectCreator.setAdapter(projectCreatorAdapter);

        horizontalProjectCreatorAdapter = new HorizontalProjectCreatorAdapter(creatorsSelected, homeActivityContext, this);
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
        knowledgeNuggetsEditTextCustom = (TextViewCustom) rootView.findViewById(R.id.edt_knowledge_nuggets);
        descriptionEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_description);
        notesToTheFamilyEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_notes_to_the_family);
        saveButtonCustom = (ButtonCustom) rootView.findViewById(R.id.btn_save);
        cancelButtonCustom = (ButtonCustom) rootView.findViewById(R.id.btn_cancel);

        mentorNameTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_mentor_name);
        labSKUTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_lab_sku);
        componentTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.component);

        videoThumbnailImageView.setOnClickListener(this);
        saveButtonCustom.setOnClickListener(this);
        cancelButtonCustom.setOnClickListener(this);
        closeImageView.setOnClickListener(this);
        closeLinearLayout.setOnClickListener(this);
        componentTextViewCustom.setOnClickListener(this);
        knowledgeNuggetsEditTextCustom.setOnClickListener(this);

        if (bundle != null) {
            savedVideoUri = bundle.getParcelable(URI);
            if (savedVideoUri != null) {
                Glide
                        .with(homeActivityContext)
                        .load(Uri.fromFile(new File(savedVideoUri.getPath())))
                        .into(videoThumbnailImageView);
            }
            ArrayList<Student> objects = (ArrayList<Student>) bundle.getSerializable(PROJECT_CREATORS);
            if (objects != null && !objects.isEmpty()) {
                creatorsSelected.clear();
                creatorsSelected.addAll(objects);
                horizontalProjectCreatorAdapter.notifyDataSetChanged();
            }
            ArrayList<String> kN = (ArrayList<String>) bundle.getSerializable(AddVideoFragment.KNOWLEDGE_NUGGETS);
            if (kN != null && !kN.isEmpty()) {
                knowledgeNuggets.clear();
                knowledgeNuggets.addAll(kN);
            }
            knowledgeNuggetsSelected = bundle.getString(AddVideoFragment.NUGGETS, "");
        }
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
    }

    public void prepareStudentsCategoryList() {
        creatorsAvailable.addAll(ProgramStudentsTable.getInstance().getStudentsListByProgramId(video.getProgramId()));
        projectCreatorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video_thumbnail:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getPath()));
                intent.setDataAndType(Uri.parse(video.getPath()), "video/*");
                startActivity(intent);
                break;
            case R.id.btn_save:
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

                if (knowledgeNuggets.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Select At Least One Knowledge Nugget");
                    break;
                }

                if (descriptionEditTextCustom.getText().toString().length() < 5) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Description must consist 5 words");
                    break;
                }

                if (creatorsSelected.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Select at least one creator");
                    break;
                }

                if (notesToTheFamilyEditTextCustom.getText().toString().length() < 5) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Notes must consist 5 words");
                    break;
                }

                Video videoRequest = new Video();
                videoRequest.setId(video.getId());
                videoRequest.setMentor_id(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
                videoRequest.setStatus(100);
                videoRequest.setPath(video.getPath());
                videoRequest.setTitle(titleEditTextCustom.getText().toString());
                videoRequest.setCategory((String) (categorySpinner.getSelectedItem()));
                videoRequest.setMentor_name(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
                videoRequest.setLab_sku(video.getLab_sku());
                videoRequest.setLab_level(video.getLab_level());
                videoRequest.setKnowledge_nuggets(LabTabUtil.toJson(getKnowledgeNuggets(knowledgeNuggets)));
                videoRequest.setDescription(descriptionEditTextCustom.getText().toString());
                videoRequest.setProject_creators(LabTabUtil.toJson(creatorsSelected));
                videoRequest.setNotes_to_the_family(notesToTheFamilyEditTextCustom.getText().toString());
                videoRequest.setIs_transCoding(String.valueOf(false));
                videoRequest.setVideo(video.getVideo());
                videoRequest.setVideoId(video.getVideoId());
                videoRequest.setProgramId(video.getProgramId());

                if (LabTabUtil.compareEditedVideo(videoRequest)) {
                    progressDialog.show();
                    BackgroundExecutor.getInstance().execute(new EditProjectRequester(videoRequest, editVideoCase));
                } else {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_CHANGES_ARE_MADE);
                }
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
            case R.id.component:
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.edt_knowledge_nuggets:
                AlertDialog dialog1 = builder.create();
                dialog1.show();
                break;

        }
    }

    @Override
    public void onProjectCreatorClick(final Student student) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (creatorsSelected.isEmpty()) {
                    creatorsSelected.add(student);
                    horizontalProjectCreatorAdapter.notifyDataSetChanged();
                    recyclerViewContainer.setVisibility(View.GONE);
                    recyclerViewProjectCreator.setVisibility(View.GONE);
                    projectCreatorEditTextCustom.clearFocus();
                    LabTabUtil.hideSoftKeyboard(homeActivityContext);
                } else {
                    boolean found = false;

                    for (Student studentFound : creatorsSelected) {
                        if (studentFound.getStudent_id().equals(student.getStudent_id())) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        creatorsSelected.add(student);
                        horizontalProjectCreatorAdapter.notifyDataSetChanged();
                        recyclerViewContainer.setVisibility(View.GONE);
                        recyclerViewProjectCreator.setVisibility(View.GONE);
                        projectCreatorEditTextCustom.clearFocus();
                        LabTabUtil.hideSoftKeyboard(homeActivityContext);
                    } else {
                        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "This student is already in the list");
                    }
                }
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

                final ArrayList<Student> filteredList = new ArrayList<>();

                for (int i = 0; i < creatorsAvailable.size(); i++) {

                    final String text = ((creatorsAvailable.get(i)).getName().toLowerCase());
                    if (text.contains(query)) {
                        filteredList.add(creatorsAvailable.get(i));
                    }
                }

                projectCreatorAdapter = new ProjectCreatorAdapter(filteredList, homeActivityContext, EditVideoFragment.this);
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
    public void onProjectCreatorDeleteClick(final Student student) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                creatorsSelected.remove(student);
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
        outState.putSerializable(PROJECT_CREATORS, creatorsSelected);
        outState.putParcelable(VideoListFragment.VIDEO, video);
        outState.putSerializable(AddVideoFragment.KNOWLEDGE_NUGGETS, knowledgeNuggets);
        outState.putString(AddVideoFragment.NUGGETS, knowledgeNuggetsSelected);
        outState.putString(VideoListFragment.VIDEO_EDIT_CASE, editVideoCase);
        super.onSaveInstanceState(outState);
    }

    public void fetchDataFromBundle() {
        titleEditTextCustom.setText(video.getTitle());
        knowledgeNuggetsEditTextCustom.setText(video.getKnowledge_nuggets().replaceAll("\"", ""));
        descriptionEditTextCustom.setText(video.getDescription());
        notesToTheFamilyEditTextCustom.setText(video.getNotes_to_the_family());

        mentorNameTextViewCustom.setText(video.getMentor_name());
        labSKUTextViewCustom.setText(video.getLab_sku());

        ArrayList<Student> objectArrayList = new Gson().fromJson(video.getProject_creators(), new TypeToken<ArrayList<Student>>() {
        }.getType());
        creatorsSelected.addAll(objectArrayList);
        horizontalProjectCreatorAdapter.notifyDataSetChanged();
        Glide.with(context)
                .load(Uri.fromFile(new File(video.getPath())))
                .into(videoThumbnailImageView);
    }

    private void initCategory() {
        String[] categories = homeActivityContext.getResources().getStringArray(R.array.array_category);
        categoryArrayList.addAll(Arrays.asList(categories));
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(homeActivityContext, android.R.layout.simple_spinner_dropdown_item, categoryArrayList);
        categorySpinner.setAdapter(spinnerArrayAdapter);

        for (String s : categoryArrayList) {
            if (s.equals(video.getCategory())) {
                categorySpinner.setSelection(categoryArrayList.indexOf(s));
                break;
            }
        }
    }

    public void initKnowledgeNuggets(Bundle bundle) {
        builder = new AlertDialog.Builder(homeActivityContext);
        final String[] components;
        if (video.getLab_level() != null) {
            components = LabTabApplication.getInstance().getKnowledgeNuggets(video.getLab_level());
        } else {
            components = homeActivityContext.getResources().getStringArray(R.array.components);
        }
        final boolean[] componentSelection = new boolean[components.length];

        if (bundle != null) {
            ArrayList<String> kN = (ArrayList<String>) bundle.getSerializable(AddVideoFragment.KNOWLEDGE_NUGGETS);
            if (kN != null && !kN.isEmpty()) {
                knowledgeNuggets.clear();
                knowledgeNuggets.addAll(kN);
                for (String kn : kN) {
                    for (int i = 0; i < components.length; i++) {
                        if (components[i].equals(kn)) {
                            componentSelection[i] = true;
                            break;
                        }
                    }
                }
            }
            String[] knwlgngts = (String[]) LabTabUtil.fromJson(video.getKnowledge_nuggets(), String[].class);
            for (String kn : knwlgngts) {
                for (int i = 0; i < components.length; i++) {
                    if (components[i].equals(kn)) {
                        componentSelection[i] = true;
                        break;
                    }
                }
                knowledgeNuggets.add(kn);
            }

        } else {
            String[] knwlgngts = (String[]) LabTabUtil.fromJson(video.getKnowledge_nuggets(), String[].class);
            for (String kn : knwlgngts) {
                for (int i = 0; i < components.length; i++) {
                    if (components[i].equals(kn)) {
                        componentSelection[i] = true;
                        break;
                    }
                }
                knowledgeNuggets.add(kn);
            }
            knowledgeNuggetsSelected = LabTabUtil.toJson(getKnowledgeNuggets(knowledgeNuggets));
        }

        builder.setMultiChoiceItems(components, componentSelection, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                componentSelection[which] = isChecked;
            }
        });
        builder.setCancelable(false);
        builder.setTitle("Select Knowledge Nuggets");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                knowledgeNuggets.clear();
                for (int i = 0; i < componentSelection.length; i++) {
                    boolean checked = componentSelection[i];
                    if (checked) {
                        knowledgeNuggets.add(components[i]);
                    }
                }
                knowledgeNuggetsSelected = (LabTabUtil.toJson(knowledgeNuggets));
                knowledgeNuggetsEditTextCustom.setText(LabTabUtil.toJson(getKnowledgeNuggets(knowledgeNuggets)).replaceAll("\"", ""));
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] knwlgngts = (String[]) LabTabUtil.fromJson(knowledgeNuggetsSelected, String[].class);
                if (knwlgngts != null) {
                    ArrayList<Integer> integers = new ArrayList<>();
                    for (String kn : knwlgngts) {
                        for (int i = 0; i < components.length; i++) {
                            if (components[i].equals(kn)) {
                                integers.add(i);
                                break;
                            }
                        }
                        for (int i = 0; i < components.length; i++) {
                            componentSelection[i] = false;
                        }

                        for (int a : integers) {
                            componentSelection[a] = true;
                        }
                    }
                } else {
                    for (int i = 0; i < components.length; i++) {
                        componentSelection[i] = false;
                    }
                }
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] knwlgngts = (String[]) LabTabUtil.fromJson(knowledgeNuggetsSelected, String[].class);
                if (knwlgngts != null) {
                    ArrayList<Integer> integers = new ArrayList<>();
                    for (String kn : knwlgngts) {
                        for (int i = 0; i < components.length; i++) {
                            if (components[i].equals(kn)) {
                                integers.add(i);
                                break;
                            }
                        }
                        for (int i = 0; i < components.length; i++) {
                            componentSelection[i] = false;
                        }

                        for (int a : integers) {
                            componentSelection[a] = true;
                        }
                    }
                } else {
                    for (int i = 0; i < components.length; i++) {
                        componentSelection[i] = false;
                    }
                }
            }
        });

    }

    private String[] getKnowledgeNuggets(ArrayList<String> knowledgeNuggets) {
        String[] nuggets = new String[knowledgeNuggets.size()];
        for (int i = 0; i < knowledgeNuggets.size(); i++) {
            nuggets[i] = knowledgeNuggets.get(i);
        }
        return nuggets;
    }

    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(EditProjectListener.class, this);
    }

    @Override
    public void projectUpdatedSuccessfully(EditProjectResponse editProjectResponse, Video video) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.PROJECT_EDITED_SUCCESSFULLY);
                homeActivityContext.clearAllTheFragmentFromStack();
                homeActivityContext.replaceFragment(Fragments.HOME, new Bundle());
            }
        });
    }

    @Override
    public void unableToUpdateProject(final int responseCode) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if (responseCode == 1000) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.PROJECT_EDITED_SUCCESSFULLY);
                    homeActivityContext.clearAllTheFragmentFromStack();
                    homeActivityContext.replaceFragment(Fragments.HOME, new Bundle());
                } else if (responseCode != 0) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.OOPS_SOMETHING_WENT_WRONG);
                } else {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_INTERNET_CONNECTION);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        progressDialog.dismiss();
        super.onDestroy();
    }

    private Video compareChangeWithThis() {
        Video videoRequestOnOpeningEditScreen = new Video();
        videoRequestOnOpeningEditScreen.setId(video.getId());
        videoRequestOnOpeningEditScreen.setMentor_id(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
        videoRequestOnOpeningEditScreen.setStatus(100);
        videoRequestOnOpeningEditScreen.setPath(video.getPath());
        videoRequestOnOpeningEditScreen.setTitle(titleEditTextCustom.getText().toString());
        videoRequestOnOpeningEditScreen.setCategory((String) (categorySpinner.getSelectedItem()));
        videoRequestOnOpeningEditScreen.setMentor_name(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
        videoRequestOnOpeningEditScreen.setLab_sku(video.getLab_sku());
        videoRequestOnOpeningEditScreen.setLab_level(video.getLab_level());
        videoRequestOnOpeningEditScreen.setKnowledge_nuggets(LabTabUtil.toJson(getKnowledgeNuggets(knowledgeNuggets)));
        videoRequestOnOpeningEditScreen.setDescription(descriptionEditTextCustom.getText().toString());
        videoRequestOnOpeningEditScreen.setProject_creators(LabTabUtil.toJson(creatorsSelected));
        videoRequestOnOpeningEditScreen.setNotes_to_the_family(notesToTheFamilyEditTextCustom.getText().toString());
        videoRequestOnOpeningEditScreen.setIs_transCoding(String.valueOf(false));
        videoRequestOnOpeningEditScreen.setVideo(video.getVideo());
        videoRequestOnOpeningEditScreen.setVideoId(video.getVideoId());
        videoRequestOnOpeningEditScreen.setProgramId(video.getProgramId());
        return videoRequestOnOpeningEditScreen;
    }
}
