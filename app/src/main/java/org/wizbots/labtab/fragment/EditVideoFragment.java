package org.wizbots.labtab.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.activity.TrimmerActivity;
import org.wizbots.labtab.adapter.HorizontalProjectCreatorAdapter;
import org.wizbots.labtab.adapter.KnowledgeNuggetExpand;
import org.wizbots.labtab.adapter.ProjectCreatorAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.ButtonCustom;
import org.wizbots.labtab.customview.EditTextCustom;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.dialog.SelectCreatorDialog;
import org.wizbots.labtab.interfaces.HorizontalProjectCreatorAdapterClickListener;
import org.wizbots.labtab.interfaces.ProjectCreatorAdapterClickListener;
import org.wizbots.labtab.interfaces.requesters.EditProjectListener;
import org.wizbots.labtab.interfaces.requesters.OnDeleteVideoListener;
import org.wizbots.labtab.interfaces.requesters.ShouldDialogueShow;
import org.wizbots.labtab.model.Nuggests;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.model.video.response.EditProjectResponse;
import org.wizbots.labtab.requesters.DeleteVideoRequester;
import org.wizbots.labtab.requesters.EditProjectRequester;
import org.wizbots.labtab.util.BackgroundExecutor;
import org.wizbots.labtab.util.LabTabUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import life.knowledge4.videotrimmer.utils.FileUtils;

public class EditVideoFragment extends ParentFragment implements View.OnClickListener,
        ProjectCreatorAdapterClickListener, HorizontalProjectCreatorAdapterClickListener,
        EditProjectListener, OnDeleteVideoListener, SelectCreatorDialog.SelectedCreatorDialogListener, ShouldDialogueShow {

    public static final int REQUEST_CODE_TRIM_VIDEO = 300;
    public static final String URI = "URI";
    public static final String FILE_URI = "file-uri";
    public static final String PROJECT_CREATORS = "PROJECT_CREATORS";
    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;

//    private ProjectCreatorAdapter projectCreatorAdapter;
    private HorizontalProjectCreatorAdapter horizontalProjectCreatorAdapter;

//    private RecyclerView recyclerViewProjectCreator;
    private RecyclerView horizontalRecyclerViewProjectCreator;

    private ArrayList<Student> creatorsAvailable = new ArrayList<>();
    private ArrayList<Student> creatorsSelected = new ArrayList<>();

    private HomeActivity homeActivityContext;
//    private LinearLayout recyclerViewContainer;
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
    // variable to track event time
    private long mLastClickTime = 0;
    private int check = 0;

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
        if (savedInstanceState == null) {
            video = getArguments().getParcelable(VideoListFragment.VIDEO);
            savedVideoUri = Uri.parse(video.getPath());
//            knowledgeNuggetsSelected = LabTabUtil.toJson(getKnowledgeNuggets(knowledgeNuggets));
            editVideoCase = getArguments().getString(VideoListFragment.VIDEO_EDIT_CASE, VideoEditCase.INTERNET_ON);
            initListeners();
            initView(savedInstanceState);
            fetchDataFromBundle();

        } else {
            editVideoCase = savedInstanceState.getString(VideoListFragment.VIDEO_EDIT_CASE, VideoEditCase.INTERNET_ON);
            video = savedInstanceState.getParcelable(VideoListFragment.VIDEO);
            savedVideoUri = Uri.parse(video.getPath());
            fileUri=savedInstanceState.getParcelable(FILE_URI);
            initListeners();
            initView(savedInstanceState);
        }

        prepareStudentsCategoryList();
        initCategory();
        addProjectCreatorEditTextListeners();
        check=0;
        LabTabUtil.videoRequestOnOpeningEditScreen = compareChangeWithThis();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        rootView.findViewById(R.id.btn_delete).setOnClickListener(this);
    }

    @Override
    public String getFragmentName() {
        return EditVideoFragment.class.getSimpleName();
    }

    public void initView(Bundle bundle) {

        progressDialog = new ProgressDialog(homeActivityContext);
        progressDialog.setMessage("processing");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        projectCreatorEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_project_creators);
        disableKeyboard();
//        recyclerViewContainer = (LinearLayout) rootView.findViewById(R.id.recycler_view_container);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.scroll_view_edit_video);
        categorySpinner = (Spinner) rootView.findViewById(R.id.spinner_category);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    next(2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categoryArrayList = new ArrayList<>();

        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.EDIT_VIDEO);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

//        recyclerViewProjectCreator = (RecyclerView) rootView.findViewById(R.id.recycler_view_project_creators);
        horizontalRecyclerViewProjectCreator = (RecyclerView) rootView.findViewById(R.id.recycler_view_horizontal_project_creators);

        creatorsAvailable = new ArrayList<>();
        creatorsSelected = new ArrayList<>();

//        projectCreatorAdapter = new ProjectCreatorAdapter(creatorsAvailable, creatorsSelected);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerViewProjectCreator.setLayoutManager(mLayoutManager);
//        recyclerViewProjectCreator.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewProjectCreator.setAdapter(projectCreatorAdapter);

        horizontalProjectCreatorAdapter = new HorizontalProjectCreatorAdapter(creatorsSelected, homeActivityContext, this);
        RecyclerView.LayoutManager horizontalLayoutManager = new GridLayoutManager(getActivity(), 2);
        if (getRotation(getActivity()).equalsIgnoreCase("landscape") || getRotation(getActivity()).equalsIgnoreCase("reverse landscape")) {
            horizontalLayoutManager = new GridLayoutManager(getActivity(), 3);

        }
        // RecyclerView.LayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerViewProjectCreator.setLayoutManager(horizontalLayoutManager);
        horizontalRecyclerViewProjectCreator.setItemAnimator(new DefaultItemAnimator());
        horizontalRecyclerViewProjectCreator.setAdapter(horizontalProjectCreatorAdapter);

//        recyclerViewProjectCreator.setVisibility(View.GONE);
//        recyclerViewContainer.setVisibility(View.GONE);

        videoThumbnailImageView = (ImageView) rootView.findViewById(R.id.iv_video_thumbnail);
        closeImageView = (ImageView) rootView.findViewById(R.id.iv_close);
        closeLinearLayout = (LinearLayout) rootView.findViewById(R.id.ll_close);
        titleEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_title);
        knowledgeNuggetsEditTextCustom = (TextViewCustom) rootView.findViewById(R.id.edt_knowledge_nuggets);
        descriptionEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_description);
        notesToTheFamilyEditTextCustom = (EditTextCustom) rootView.findViewById(R.id.edt_notes_to_the_family);

        notesToTheFamilyEditTextCustom.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    rootView.findViewById(R.id.btn_save).callOnClick();
                    handled = true;
                }
                return handled;
            }
        });
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkAndRequestPermissionsSingle()) {
                    Glide
                            .with(homeActivityContext)
                            .load(Uri.fromFile(new File(savedVideoUri.getPath())))
                            .into(videoThumbnailImageView);
                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Glide.with(context)
                            .load(Uri.fromFile(new File(video.getPath())))
                            .into(videoThumbnailImageView);
                }
            }
            ArrayList<Student> objects = (ArrayList<Student>) bundle.getSerializable(PROJECT_CREATORS);
            if (objects != null && !objects.isEmpty()) {
                creatorsSelected.clear();
                creatorsSelected.addAll(objects);
                horizontalProjectCreatorAdapter.notifyDataSetChanged();
            }
            initKnowledgeNuggets(bundle);
/*            ArrayList<String> kN = (ArrayList<String>) bundle.getSerializable(AddVideoFragment.KNOWLEDGE_NUGGETS);
            if (kN != null && !kN.isEmpty()) {
                knowledgeNuggets.clear();
                knowledgeNuggets.addAll(kN);
            }
            knowledgeNuggetsSelected = bundle.getString(AddVideoFragment.NUGGETS, "");
            knowledgeNuggetsEditTextCustom.setText(knowledgeNuggetsSelected);*/
        }
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
        registerNextEvent();
    }

    public void prepareStudentsCategoryList() {
        creatorsAvailable.addAll(ProgramStudentsTable.getInstance().getStudentsListByProgramId(video.getProgramId()));
//        projectCreatorAdapter.notifyDataSetChanged();
    }

    public String getRotation(Context context) {
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "portrait";
            case Surface.ROTATION_90:
                return "landscape";
            case Surface.ROTATION_180:
                return "reverse portrait";
            default:
                return "reverse landscape";
        }
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
                saveVideo();
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
                if (creatorsSelected == null || creatorsSelected.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please select a creator first");
                    return;
                }
                //Double Click Fix
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.edt_knowledge_nuggets:
                if (creatorsSelected == null || creatorsSelected.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please select a creator first");
                    return;
                }
                //Double Click Fix
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showDialogForKnowledgeNuggets();

               /* AlertDialog dialog1 = builder.create();
                dialog1.show();*/
                break;
            case R.id.btn_delete:
                showConfirmDialog("Are you sure you want to delete this project",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        progressDialog.show();
                                        BackgroundExecutor.getInstance().execute(new DeleteVideoRequester(video));
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        });
                break;

        }
    }

    private void saveVideo() {
        if (savedVideoUri == null) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Capture A Video First");
            return;
        }

        if (titleEditTextCustom.getText().toString().length() < 5) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Title must consist at least 5 words");
            return;
        }

        if (categorySpinner.getSelectedItemPosition() == 0) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Select Category");
            return;
        }

        if (titleEditTextCustom.getText().toString().length() == 0) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Give A Title To Video");
            return;
        }

        if (creatorsSelected.isEmpty()) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Select at least one creator");
        }

        if (knowledgeNuggets.isEmpty()) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Select At Least One Knowledge Nugget");
            return;
        }
/*
        if (descriptionEditTextCustom.getText().toString().length() < 5) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Description must consist 5 words");
            return;
        }*/

        if (creatorsSelected.isEmpty()) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Select at least one creator");
            return;
        }

      /*  if (notesToTheFamilyEditTextCustom.getText().toString().length() < 5) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Notes must consist 5 words");
            return;
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkAndRequestPermissionsSingle()) {
            return;
        }
       /* Video videoRequest = new Video();
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
        videoRequest.setProgramId(video.getProgramId());*/


        if (isDataChange()) {
            progressDialog.show();
            BackgroundExecutor.getInstance().execute(new EditProjectRequester(getVideoRequest(), editVideoCase));
        } else {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_CHANGES_ARE_MADE);
        }
    }



    private Video getVideoRequest(){
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
        return videoRequest;
    }
    @Override
    public void onProjectCreatorClick(final Student student) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (creatorsSelected.isEmpty()) {
                    creatorsSelected.add(student);
                    horizontalProjectCreatorAdapter.notifyDataSetChanged();
//                    recyclerViewContainer.setVisibility(View.GONE);
//                    recyclerViewProjectCreator.setVisibility(View.GONE);
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
//                        recyclerViewContainer.setVisibility(View.GONE);
//                        recyclerViewProjectCreator.setVisibility(View.GONE);
                        projectCreatorEditTextCustom.clearFocus();
                        LabTabUtil.hideSoftKeyboard(homeActivityContext);
                    } else {
                        homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "This student is already in the list");
                    }
                }
                initKnowledgeNuggets(null);
                projectCreatorEditTextCustom.setText("");
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

//                projectCreatorAdapter = new ProjectCreatorAdapter(filteredList,creatorsSelected);
//                recyclerViewProjectCreator.setAdapter(projectCreatorAdapter);
//                projectCreatorAdapter.notifyDataSetChanged();
            }
        });

        /*projectCreatorEditTextCustom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        });*/
        projectCreatorEditTextCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (creatorsAvailable.isEmpty()) {
                    Toast.makeText(homeActivityContext, "Select a lab first by tapping Lab SKU", Toast.LENGTH_SHORT).show();
                    return;
                }
                projectCreatorEditTextCustom.clearFocus();
                LabTabUtil.hideSoftKeyboard(homeActivityContext);
                SelectCreatorDialog dialog = new SelectCreatorDialog(homeActivityContext,creatorsAvailable,creatorsSelected,EditVideoFragment.this);
                dialog.show();
            }
        });
    }

    @Override
    public void onProjectCreatorDeleteClick(final Student student) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                creatorsSelected.remove(student);
                if (creatorsSelected != null && creatorsSelected.isEmpty()) {
                    knowledgeNuggets.clear();
                    video.setKnowledge_nuggets("");
                    knowledgeNuggetsSelected = "";
                    if (knowledgeNuggetsEditTextCustom != null)
                        knowledgeNuggetsEditTextCustom.setText("");
                }
                initKnowledgeNuggets(null);
                horizontalProjectCreatorAdapter.notifyDataSetChanged();
                projectCreatorEditTextCustom.setText("");
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkAndRequestPermissionsSingle()) {
                    Glide
                            .with(homeActivityContext)
                            .load(Uri.fromFile(new File(savedVideoUri.getPath())))
                            .into(videoThumbnailImageView);
                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Glide.with(context)
                            .load(Uri.fromFile(new File(video.getPath())))
                            .into(videoThumbnailImageView);
                }
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
        outState.putParcelable(FILE_URI, fileUri);
        outState.putSerializable(AddVideoFragment.KNOWLEDGE_NUGGETS, knowledgeNuggets);
        outState.putString(AddVideoFragment.NUGGETS, knowledgeNuggetsSelected);
        outState.putString(VideoListFragment.VIDEO_EDIT_CASE, editVideoCase);
        outState.putInt("check", check);
        super.onSaveInstanceState(outState);
    }

    public void fetchDataFromBundle() {
        titleEditTextCustom.setText(video.getTitle());
//        knowledgeNuggetsEditTextCustom.setText(video.getKnowledge_nuggets().replaceAll("\"", ""));
        descriptionEditTextCustom.setText(video.getDescription());
        notesToTheFamilyEditTextCustom.setText(video.getNotes_to_the_family());

        mentorNameTextViewCustom.setText(video.getMentor_name());
        labSKUTextViewCustom.setText(video.getLab_sku());

        ArrayList<Student> objectArrayList = new Gson().fromJson(video.getProject_creators(), new TypeToken<ArrayList<Student>>() {
        }.getType());
        creatorsSelected.addAll(objectArrayList);
        initKnowledgeNuggets(null);
        horizontalProjectCreatorAdapter.notifyDataSetChanged();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkAndRequestPermissionsSingle()) {
            Glide.with(context)
                    .load(Uri.fromFile(new File(video.getPath())))
                    .into(videoThumbnailImageView);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Glide.with(context)
                    .load(Uri.fromFile(new File(video.getPath())))
                    .into(videoThumbnailImageView);
        }
    }

    private void initCategory() {
//        String[] categories = homeActivityContext.getResources().getStringArray(R.array.array_category);
        String[] categories = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCategory();
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
        if (creatorsSelected == null || creatorsSelected.size() == 0)
            return;
        builder = new AlertDialog.Builder(homeActivityContext);
        Set<String> noug = new HashSet<>();
        String[] temp = LabTabApplication.getInstance().getKnowledgeNuggetsByStudent(creatorsSelected);
        if (temp != null && temp.length > 0)
            noug.addAll(Arrays.asList(temp));

/*        if (bundle != null) {
            HashSet<String> kN = (HashSet<String>) bundle.getSerializable(AddVideoFragment.KNOWLEDGE_NUGGETS);
            if (kN != null && !kN.isEmpty()) {
                noug.addAll(kN);
            }
        }else if(video != null){
            String[] knwlgngts = (String[]) LabTabUtil.fromJson(video.getKnowledge_nuggets(), String[].class);
            if(knwlgngts != null && knwlgngts.length > 0){
                noug.addAll(Arrays.asList(knwlgngts));
            }
        }*/

        final String[] components = noug.toArray(new String[noug.size()]);

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
        } else if (video != null) {
            knowledgeNuggets.clear();
            String[] knwlgngts = (String[]) LabTabUtil.fromJson(video.getKnowledge_nuggets(), String[].class);
            if (knwlgngts != null && knwlgngts.length > 0) {
                for (String kn : knwlgngts) {
                    for (int i = 0; i < components.length; i++) {
                        if (components[i].equals(kn)) {
                            componentSelection[i] = true;
                            knowledgeNuggets.add(kn);
                            break;
                        }
                    }
                }
            }
        }

        knowledgeNuggetsSelected = LabTabUtil.toJson(getKnowledgeNuggets(knowledgeNuggets));


        String[] tmp = getKnowledgeNuggets(knowledgeNuggets);
        if (tmp != null && tmp.length > 0) {
            String tempStr = LabTabUtil.toJson(tmp).replaceAll("\"", "");
            video.setKnowledge_nuggets(knowledgeNuggetsSelected);
            knowledgeNuggetsEditTextCustom.setText((tempStr != null && !tempStr.isEmpty()) ? tempStr : "");
        } else {
            video.setKnowledge_nuggets(knowledgeNuggetsSelected);
            knowledgeNuggetsEditTextCustom.setText("");
        }


        if (components != null) {
            String[] knwlgngts = (String[]) LabTabUtil.fromJson(knowledgeNuggetsSelected, String[].class);
            if (knwlgngts != null && knwlgngts.length > 0) {
                List<String> result = new LinkedList();
                for (int i = 0; i < components.length; i++) {
                    for (int j = 0; j < knwlgngts.length; j++) {
                        if (components[i].equals(knwlgngts[j])) {
                            result.add(knwlgngts[j]);
                        }
                    }
                }
                if (result.size() > 0) {
                    knwlgngts = result.toArray(new String[result.size()]);
                }
                knowledgeNuggets.clear();
                knowledgeNuggets.addAll(Arrays.asList(knwlgngts));
                knowledgeNuggetsSelected = LabTabUtil.toJson(getKnowledgeNuggets(knowledgeNuggets));
                knowledgeNuggetsEditTextCustom.setText(LabTabUtil.toJson(getKnowledgeNuggets(knowledgeNuggets)).replaceAll("\"", ""));
                for (int i = 0; i < components.length; i++) {
                    for (int j = 0; j < knwlgngts.length; j++) {
                        if (components[i].equals(knwlgngts[j])) {
                            componentSelection[i] = true;
                        }
                    }
                }
            }
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
                video.setKnowledge_nuggets(knowledgeNuggetsSelected);
                String[] tmp = getKnowledgeNuggets(knowledgeNuggets);
                if (tmp != null && tmp.length > 0) {
                    knowledgeNuggetsEditTextCustom.setText(LabTabUtil.toJson(tmp).replaceAll("\"", ""));
                } else {
                    knowledgeNuggetsEditTextCustom.setText("");
                }
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

        if (components != null && components.length <= 0)
            builder.setMessage("No knowledge nuggets for selected student");
    }

    private String[] getKnowledgeNuggets(ArrayList<String> knowledgeNuggets) {
        String[] nuggets;
        nuggets = knowledgeNuggets.toArray(new String[knowledgeNuggets.size()]);
        return nuggets;
    }

    public void initListeners() {
        LabTabApplication.getInstance().addUIListener(EditProjectListener.class, this);
        LabTabApplication.getInstance().addUIListener(OnDeleteVideoListener.class, this);
    }

    @Override
    public void projectUpdatedSuccessfully(EditProjectResponse editProjectResponse, Video video) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LabTabUtil.hideSoftKeyboard(homeActivityContext);
                progressDialog.dismiss();
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.PROJECT_EDITED_SUCCESSFULLY);
                try {
//                    homeActivityContext.clearAllTheFragmentFromStack();
                    homeActivityContext.getSupportFragmentManager().popBackStackImmediate();
                } catch (Exception e) {
                    progressDialog.dismiss();
                } finally {
                    progressDialog.dismiss();
                }
                homeActivityContext.replaceFragment(Fragments.HOME, new Bundle());
                homeActivityContext.replaceFragment(Fragments.VIDEO_LIST, new Bundle());
            }
        });
    }

    @Override
    public void unableToUpdateProject(final int responseCode) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LabTabUtil.hideSoftKeyboard(homeActivityContext);
                progressDialog.dismiss();
                if (responseCode == 1000) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.PROJECT_EDITED_SUCCESSFULLY);
                    homeActivityContext.clearAllTheFragmentFromStack();
                    homeActivityContext.replaceFragment(Fragments.HOME, new Bundle());
                    homeActivityContext.replaceFragment(Fragments.VIDEO_LIST, new Bundle());
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
        LabTabApplication.getInstance().removeUIListener(EditProjectListener.class, this);
        LabTabApplication.getInstance().removeUIListener(OnDeleteVideoListener.class, this);
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

    @Override
    public void onDeleteVideoSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.PROJECT_DELETED_SUCCESS);
                homeActivityContext.getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }

    @Override
    public void onDeleteVideoError() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.PROJECT_DELETED_FAILED);
                homeActivityContext.getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }

    /*
    * Permission Code Start Here
    * * */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_REQUEST_CODE:
                final Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        openCamera();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    || shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    switch (which) {
                                                        case DialogInterface.BUTTON_POSITIVE:
                                                            Set<String> pendingPermission = new HashSet();
                                                            for (String key : perms.keySet()) {
                                                                if (perms.get(key) != PackageManager.PERMISSION_GRANTED) {
                                                                    pendingPermission.add(key);
                                                                }
                                                            }
                                                            requestPermissions(pendingPermission.toArray(new String[pendingPermission.size()]), Constants.PERMISSION_REQUEST_CODE);
                                                            break;
                                                        case DialogInterface.BUTTON_NEGATIVE:
                                                            Toast.makeText(homeActivityContext, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                                                            break;
                                                    }
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(homeActivityContext, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                break;
            case Constants.PERMISSION_REQUEST_CODE_STORAGE:
                final Map<String, Integer> perms1 = new HashMap<String, Integer>();
                perms1.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms1.put(permissions[i], grantResults[i]);
                    if (perms1.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Glide.with(context)
                                .load(Uri.fromFile(new File(video.getPath())))
                                .into(videoThumbnailImageView);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    switch (which) {
                                                        case DialogInterface.BUTTON_POSITIVE:
                                                            Set<String> pendingPermission = new HashSet();
                                                            for (String key : perms1.keySet()) {
                                                                if (perms1.get(key) != PackageManager.PERMISSION_GRANTED) {
                                                                    pendingPermission.add(key);
                                                                }
                                                            }
                                                            requestPermissions(pendingPermission.toArray(new String[pendingPermission.size()]), Constants.PERMISSION_REQUEST_CODE);
                                                            break;
                                                        case DialogInterface.BUTTON_NEGATIVE:
                                                            Toast.makeText(homeActivityContext, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                                                            break;
                                                    }
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(homeActivityContext, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showConfirmDialog(String message, DialogInterface.OnClickListener listener) {
        new android.support.v7.app.AlertDialog.Builder(homeActivityContext)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", listener)
                .create()
                .show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener listener) {
        new android.support.v7.app.AlertDialog.Builder(homeActivityContext)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", listener)
                .create()
                .show();
    }

    @SuppressLint("NewApi")
    public boolean checkAndRequestPermissions() {
        int storage = homeActivityContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = homeActivityContext.checkSelfPermission(Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<String>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), Constants.PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @SuppressLint("NewApi")
    public boolean checkAndRequestPermissionsSingle() {
        int storage = homeActivityContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<String>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), Constants.PERMISSION_REQUEST_CODE_STORAGE);
            return false;
        }
        return true;
    }

    public void showDialogForKnowledgeNuggets() {
        final Dialog dialog1 = new Dialog(context);
        dialog1.setContentView(R.layout.knowledgenuggets_expand_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog1.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = getResources().getDimensionPixelOffset(R.dimen.login_container_width);
        lp.gravity = Gravity.CENTER;

        dialog1.getWindow().setAttributes(lp);
        dialog1.setContentView(R.layout.knowledgenuggets_expand_layout);
        dialog1.setTitle("Select Knowledge Nuggets");
        HashMap<String, ArrayList<Nuggests>> list;
        String[] a = convertTextToModel();

        list = LabTabApplication.getInstance().getKnowledgeNuggetHashsByStudent(creatorsSelected, a);
        sortHashMapValueList(list);  // Syadav
        ArrayList<String> keys = new ArrayList(list.keySet());
        ExpandableListView expandableListView = (ExpandableListView) dialog1.findViewById(R.id.lvExp);
        final KnowledgeNuggetExpand knowledgeNuggetExpand = new KnowledgeNuggetExpand(getActivity(), keys, list);
        expandableListView.setAdapter(knowledgeNuggetExpand);
        dialog1.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knowledgeNuggets = knowledgeNuggetExpand.getSelectedNuggest();
                knowledgeNuggetsSelected = knowledgeNuggets.toString();
                if (knowledgeNuggetsSelected.length() > 2) {
                    next(5);
                    knowledgeNuggetsEditTextCustom.setText(knowledgeNuggets.toString());
                } else {
                    knowledgeNuggetsEditTextCustom.setText("");
                }
                dialog1.dismiss();

            }
        });
        dialog1.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    // ========================================SYADAV=======================================================
    private void sortHashMapValueList(HashMap<String, ArrayList<Nuggests>> list) {
        for (Map.Entry<String, ArrayList<Nuggests>> entry : list.entrySet()) {
            entry.getValue();
            Collections.sort(entry.getValue(), comparator);
        }

    }

    // placed inline for the demonstration, but doesn't have to be an anonymous class
    Comparator<Nuggests> comparator = new Comparator<Nuggests>() {
        public int compare(Nuggests o1, Nuggests o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
    // ===============================================================================================

    private String[] convertTextToModel() {
        String selectedNuggets = knowledgeNuggetsEditTextCustom.getText().toString();
        if (selectedNuggets != null && selectedNuggets.length() > 2) {
            String originalNuggets = selectedNuggets.substring(1, selectedNuggets.length() - 1);
            String[] nuggets = originalNuggets.split(",");
            return nuggets;
        }
        return null;
    }

    private void disableKeyboard() {
        projectCreatorEditTextCustom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = projectCreatorEditTextCustom.getInputType(); // backup the input type
                projectCreatorEditTextCustom.setInputType(InputType.TYPE_NULL); // disable soft input
                projectCreatorEditTextCustom.onTouchEvent(event); // call native handler
                projectCreatorEditTextCustom.setInputType(inType); // restore input type
                return true; // consume touch even
            }
        });
    }


    public void next(int pos) {
        switch (pos) {
            case 1:
                showKeyboard();

                categorySpinner.performClick();


                break;
            case 2:
                // projectCreatorEditTextCustom.performClick();
//                showKeyboard();
                projectCreatorEditTextCustom.requestFocus();
                break;
            case 3:
                descriptionEditTextCustom.requestFocus();
                break;

            case 4:
                showKeyboard();
                knowledgeNuggetsEditTextCustom.performClick();
                break;
            case 5:
                showKeyboard();
                notesToTheFamilyEditTextCustom.requestFocus();
                break;


        }
    }


    public void registerNextEvent() {
        titleEditTextCustom.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard();
                    next(1);
                    return true;
                }
                return false;
            }
        });

        projectCreatorEditTextCustom.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard();
                    next(3);
                    return true;
                }
                return false;
            }
        });

        descriptionEditTextCustom.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard();
                    next(4);
                    return true;
                }
                return false;
            }
        });


    }


    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }

    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    @Override
    public void onSaveClick() {
        Collections.sort(creatorsSelected, new Comparator<Student>() {
            @Override
            public int compare(Student student, Student t1) {
                return student.getName().compareTo(t1.getName());
            }
        });
        horizontalProjectCreatorAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean isDataChange() {
        return LabTabUtil.compareEditedVideo(getVideoRequest());
    }
}
