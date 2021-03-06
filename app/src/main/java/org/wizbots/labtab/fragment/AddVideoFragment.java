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
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
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
import org.wizbots.labtab.database.ProgramTable;
import org.wizbots.labtab.database.ProgramsOrLabsTable;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.dialog.SelectCreatorDialog;
import org.wizbots.labtab.interfaces.HorizontalProjectCreatorAdapterClickListener;
import org.wizbots.labtab.interfaces.ProjectCreatorAdapterClickListener;
import org.wizbots.labtab.interfaces.VideoOptionSelector;
import org.wizbots.labtab.interfaces.requesters.GetProgramOrLabListener;
import org.wizbots.labtab.interfaces.requesters.GetProgramStudentsListener;
import org.wizbots.labtab.interfaces.requesters.ShouldDialogueShow;
import org.wizbots.labtab.model.Nuggests;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.program.response.ProgramResponse;
import org.wizbots.labtab.model.video.CreateProjectRequest;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.requesters.ProgramOrLabRequester;
import org.wizbots.labtab.requesters.ProgramStudentsRequester;
import org.wizbots.labtab.service.LabTabSyncService;
import org.wizbots.labtab.util.BackgroundExecutor;
import org.wizbots.labtab.util.DialogueUtil;
import org.wizbots.labtab.util.LabTabUtil;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import life.knowledge4.videotrimmer.utils.FileUtils;

import static android.R.attr.borderlessButtonStyle;
import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.orientation;
import static android.content.Context.WINDOW_SERVICE;

public class AddVideoFragment extends ParentFragment implements View.OnClickListener,
        ProjectCreatorAdapterClickListener, HorizontalProjectCreatorAdapterClickListener,
        LabListDialogFragment.LabListClickListener, GetProgramOrLabListener, GetProgramStudentsListener, SelectCreatorDialog.SelectedCreatorDialogListener, ShouldDialogueShow {

    public static final String TAG = AddVideoFragment.class.getSimpleName();

    public static final int REQUEST_CODE_TRIM_VIDEO = 300;
    private static final int RESULT_LOAD_VIDEO = 201;
    KnowledgeNuggetExpand knowledgeNuggetExpand;
    public static final String URI = "URI";
    public static final String PROJECT_CREATORS = "PROJECT_CREATORS";
    public static final String KNOWLEDGE_NUGGETS = "KNOWLEDGE_NUGGETS";
    public static final String LEVEL = "lab_level";
    public static final String FILE_URI = "file-uri";
    public static final String NUGGETS = "NUGGETS";
    public static final String PROGRAM = "PROGRAM";
    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private int optionType;
    private final int CAMERA = 1;
    private final int GALLERY = 2;
    private ExpandableListView expandableListView;

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
    private ButtonCustom createButtonCustom, cancelButtonCustom;
    private TextViewCustom mentorNameTextViewCustom, labSKUTextViewCustom, componentTextViewCustom, knowledgeNuggetsEditTextCustom;
    private LinearLayout closeLinearLayout;

    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;
    private Uri savedVideoUri;
    private Program program;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private ArrayList<String> knowledgeNuggets = new ArrayList<>();
    private String level, knowledgeNuggetsSelected = "";
    // variable to track event time
    private long mLastClickTime = 0;
    private int check = 0;
//    private ArrayList<String> selectedNuggest = new ArrayList<>();

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
        level = getArguments().getString(LabDetailsFragment.LAB_LEVEL);
        initView(savedInstanceState);
        initKnowledgeNuggets(savedInstanceState);
        prepareStudentCategoryList();
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(homeActivityContext, android.R.layout.simple_spinner_dropdown_item, categoryArrayList);
        categorySpinner.setAdapter(spinnerArrayAdapter);
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
        addProjectCreatorEditTextListeners();
        ArrayList<Student> objects = (ArrayList<Student>) getArguments().getSerializable(LabDetailsFragment.SELECTED_STUDENTS);
        if (savedInstanceState == null && objects != null && !objects.isEmpty()) {
            creatorsSelected.clear();
            creatorsSelected.addAll(objects);
            initKnowledgeNuggets(null);
            horizontalProjectCreatorAdapter.notifyDataSetChanged();
        }
        initListeners();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return AddVideoFragment.class.getSimpleName();
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
        categoryArrayList = new ArrayList<>();

        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.ADD_VIDEO);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

//        recyclerViewProjectCreator = (RecyclerView) rootView.findViewById(R.id.recycler_view_project_creators);
        horizontalRecyclerViewProjectCreator = (RecyclerView) rootView.findViewById(R.id.recycler_view_horizontal_project_creators);


//        projectCreatorAdapter = new ProjectCreatorAdapter(creatorsAvailable, creatorsSelected);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerViewProjectCreator.setLayoutManager(mLayoutManager);
//        recyclerViewProjectCreator.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewProjectCreator.setAdapter(projectCreatorAdapter);

        horizontalProjectCreatorAdapter = new HorizontalProjectCreatorAdapter(creatorsSelected, homeActivityContext, this);
        Display display = ((WindowManager) getActivity().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        RecyclerView.LayoutManager horizontalLayoutManager = new GridLayoutManager(getActivity(), 2);
        if (getRotation(getActivity()).equalsIgnoreCase("landscape") || getRotation(getActivity()).equalsIgnoreCase("reverse landscape")) {
            horizontalLayoutManager = new GridLayoutManager(getActivity(), 3);

        }
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
                    rootView.findViewById(R.id.btn_create).callOnClick();
                    handled = true;
                }
                return handled;
            }
        });
        createButtonCustom = (ButtonCustom) rootView.findViewById(R.id.btn_create);
        cancelButtonCustom = (ButtonCustom) rootView.findViewById(R.id.btn_cancel);

        mentorNameTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_mentor_name);
        labSKUTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.tv_lab_sku);
        componentTextViewCustom = (TextViewCustom) rootView.findViewById(R.id.component);

        mentorNameTextViewCustom.setText(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());

        videoThumbnailImageView.setOnClickListener(this);
        createButtonCustom.setOnClickListener(this);
        cancelButtonCustom.setOnClickListener(this);
        closeImageView.setOnClickListener(this);
        closeLinearLayout.setOnClickListener(this);
        componentTextViewCustom.setOnClickListener(this);
        labSKUTextViewCustom.setOnClickListener(this);
        knowledgeNuggetsEditTextCustom.setOnClickListener(this);

        if (bundle != null) {
            program = bundle.getParcelable(PROGRAM);
        }
        if (program == null) {
            labSKUTextViewCustom.setText("");
        } else {
            labSKUTextViewCustom.setText(String.valueOf(program.getSku()));
            labSKUTextViewCustom.setClickable(false);
        }

        if (bundle != null) {
            knowledgeNuggetsEditTextCustom.setText(bundle.getString(NUGGETS));
        }

        if (bundle != null) {
            fileUri=bundle.getParcelable(FILE_URI);
            savedVideoUri = bundle.getParcelable(URI);
            if (savedVideoUri != null) {
                Glide
                        .with(homeActivityContext)
                        .load(Uri.fromFile(new File(savedVideoUri.getPath())))
                        .into(videoThumbnailImageView);
            }
            level = bundle.getString(LEVEL);
            ArrayList<Student> objects = (ArrayList<Student>) bundle.getSerializable(PROJECT_CREATORS);
            if (objects != null && !objects.isEmpty()) {
                creatorsSelected.clear();
                creatorsSelected.addAll(objects);
                horizontalProjectCreatorAdapter.notifyDataSetChanged();
            }
            ArrayList<String> kN = (ArrayList<String>) bundle.getSerializable(KNOWLEDGE_NUGGETS);
            if (kN != null && !kN.isEmpty()) {
                knowledgeNuggets.clear();
                knowledgeNuggets.addAll(kN);
            }
            knowledgeNuggetsSelected = bundle.getString(AddVideoFragment.NUGGETS, "");
        }

        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
        //rootView.findViewById(R.id.ll_lab_sku).setOnClickListener(this);
        registerNextEvent();
    }

    public void prepareStudentCategoryList() {
        //String[] categories = homeActivityContext.getResources().getStringArray(R.array.array_category);
        String[] categories = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCategory();
        categoryArrayList.addAll(Arrays.asList(categories));
        categoryArrayList.add(0, getString(R.string.select_category));
        if (program != null) {
            creatorsAvailable.addAll(ProgramStudentsTable.getInstance().getStudentsListByProgramId(program.getId()));
//            projectCreatorAdapter.notifyDataSetChanged();
        }
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

    /**
     * this methode show option record or gallery option dialogue
     */

    private void showOptionDialogue() {

        DialogueUtil.showVideoOptionDialogue(getActivity(), new VideoOptionSelector() {
            @Override
            public void onCameraClick() {
                optionType = CAMERA;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkAndRequestPermissions()) {
                    openCamera();
                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    openCamera();
                }

            }

            @Override
            public void onGalleryClick() {
                optionType = GALLERY;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkAndRequestPermissions()) {
                    openGalleryVideo();
                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    openGalleryVideo();
                }

            }

            @Override
            public void onCancelled() {
                Toast.makeText(homeActivityContext, "cancelled", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * this method for opening gallery
     */
    private void openGalleryVideo() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        startActivityForResult(i, RESULT_LOAD_VIDEO);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video_thumbnail:
                /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkAndRequestPermissions()) {
                    openCamera();
                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    openCamera();
                }*/
                LabTabUtil.hideSoftKeyboard(homeActivityContext);
                showOptionDialogue();
                break;
            case R.id.btn_create:

                Log.v(LabTabConstants.VIDEO_LOGS_TAG, "on create button clicked. " + "Device time - " + java.text.DateFormat.getDateTimeInstance().format(new Date()));
                if (titleEditTextCustom.getText().toString().length() == 0) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Give A Title To Video");
                    break;
                }

                if (titleEditTextCustom.getText().toString().length() < 5) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Title must consist at least 5 characters");
                    break;
                }

                if (categorySpinner.getSelectedItemPosition() == 0) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Select Category");
                    break;
                }

                if (labSKUTextViewCustom.getText().toString().equals("")) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please select a lab sku first by tapping Lab SKU");
                    break;
                }

                if (savedVideoUri == null) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Capture A Video First");
                    break;
                }

                if (creatorsSelected.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Select at least one creator");
                    break;
                }

                if (knowledgeNuggets.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Please Select At Least One Knowledge Nugget");
                    break;
                }



               /* if (descriptionEditTextCustom.getText().toString().length() < 5) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Description must consist 5 characters");
                    break;
                }*/



/*                if (notesToTheFamilyEditTextCustom.getText().toString().length() < 5) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "Notes must consist 5 words");
                    break;
                }*/
                //Project_Name.SKU.NamesOfKidsInCamelCaseEach


                CreateProjectRequest createProjectRequest = new CreateProjectRequest();
                createProjectRequest.setId(Calendar.getInstance().getTimeInMillis() + "");
                createProjectRequest.setMentor_id(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
                createProjectRequest.setStatus(0);
                String newFileName = getNewFileName(titleEditTextCustom.getText().toString(), (labSKUTextViewCustom.getText().toString()).toUpperCase(), creatorsSelected);
                savedVideoUri = renameFile(savedVideoUri, newFileName);
                createProjectRequest.setPath(savedVideoUri.getPath());
                createProjectRequest.setTitle(titleEditTextCustom.getText().toString());
                createProjectRequest.setCategory((String) (categorySpinner.getSelectedItem()));
                createProjectRequest.setMentor_name(mentorNameTextViewCustom.getText().toString());
                createProjectRequest.setLab_sku(labSKUTextViewCustom.getText().toString());
                Log.d("AddVideoFragment", level);
                createProjectRequest.setLab_level(level);
                createProjectRequest.setKnowledge_nuggets(knowledgeNuggets);
                createProjectRequest.setDescription(descriptionEditTextCustom.getText().toString());
                createProjectRequest.setProject_creators(creatorsSelected);
                createProjectRequest.setNotes_to_the_family(notesToTheFamilyEditTextCustom.getText().toString());
                createProjectRequest.setProgram_id(program.getId());

                Video video = new Video();
                video.setId(createProjectRequest.getId());
                video.setMentor_id(createProjectRequest.getMentor_id());
                video.setStatus(0);
                video.setPath(createProjectRequest.getPath());
                video.setTitle(createProjectRequest.getTitle());
                video.setCategory(createProjectRequest.getCategory());
                video.setMentor_name(createProjectRequest.getMentor_name());
                video.setLab_sku(createProjectRequest.getLab_sku());
                Log.d("AddVideoFragment", createProjectRequest.getLab_level());
                video.setLab_level(createProjectRequest.getLab_level());
                video.setKnowledge_nuggets(LabTabUtil.toJson(getKnowledgeNuggets(createProjectRequest.getKnowledge_nuggets())));
                video.setDescription(createProjectRequest.getDescription());
                video.setProject_creators(LabTabUtil.toJson(createProjectRequest.getProject_creators()));
                video.setNotes_to_the_family(createProjectRequest.getNotes_to_the_family());
                video.setEdit_sync_status(SyncStatus.SYNCED);
                video.setIs_transCoding(String.valueOf(false));
                video.setVideo("");
                video.setVideoId("");
                video.setProgramId(createProjectRequest.getProgram_id());

                VideoTable.getInstance().insert(video);
                LabTabUtil.hideSoftKeyboard(homeActivityContext);
                Intent uploadService = new Intent(homeActivityContext, LabTabSyncService.class);
                uploadService.putExtra(LabTabSyncService.EVENT, Events.ADD_VIDEO);
                homeActivityContext.startService(uploadService);
                homeActivityContext.clearAllTheFragmentFromStack(true);
/*                homeActivityContext.replaceFragment(Fragments.HOME, new Bundle());
                homeActivityContext.replaceFragment(Fragments.VIDEO_LIST, new Bundle());*/
                homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.PROJECT_CREATED_SUCCESSFULLY);


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
                final AlertDialog dialog = builder.create();
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

                break;
            case R.id.tv_lab_sku:
                //Double Click Fix
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (ProgramsOrLabsTable.getInstance().
                        getProgramsByMemberId(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id()).isEmpty()) {
                    progressDialog.show();
                    BackgroundExecutor.getInstance().execute(new ProgramOrLabRequester());
                } else {
                    showLabListDialog();
                }
                break;

        }
    }

    private boolean validateData() {
        if (!TextUtils.isEmpty(titleEditTextCustom.getText().toString())
                || categorySpinner.getSelectedItemPosition() > 0
                || !TextUtils.isEmpty(labSKUTextViewCustom.getText().toString())
                || savedVideoUri != null
                || !creatorsSelected.isEmpty()
                || !knowledgeNuggets.isEmpty()) {
            return true;

        }
        return false;
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


    private String getNewFileName(String s, String s1, ArrayList<Student> creatorsSelected) {
        StringBuilder fileName = new StringBuilder();
        fileName.append(s).append(".").append(s1).append(".");
        for (Student student : creatorsSelected) {
            fileName.append(getCustomName(student.getName()).trim()).append(".");
        }
        fileName.deleteCharAt(fileName.length() - 1);
        //fileName.append(".").append(Calendar.getInstance().getTimeInMillis());
        return fileName.toString().replaceAll("\\s+", "");
    }

    private String getCustomName(String name) {
        String[] fullname = name.split(" ");
        if (fullname.length > 1) {
            if (fullname[1].length() > 5) {
                return fullname[0] + fullname[1].substring(0, 5);
            }
            return fullname[0] + fullname[1];
        }
        return fullname[0];

    }

    private Uri renameFile(Uri uri, String newFileName) {
        File oldfile = new File(uri.getPath());
        File newfile = new File(oldfile.getParent() + "/" + newFileName + ".mp4");

        if (oldfile.renameTo(newfile)) {
        } else {
        }
        return Uri.fromFile(newfile);
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
                        //next(3);
                    }
                }
                initKnowledgeNuggets(null);
                projectCreatorEditTextCustom.setText("");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        LabTabApplication.getInstance().removeUIListener(GetProgramOrLabListener.class, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        LabTabApplication.getInstance().addUIListener(GetProgramOrLabListener.class, this);
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

                    final String text = creatorsAvailable.get(i).getName().toLowerCase();
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
                    if (!creatorsAvailable.isEmpty()) {
                        projectCreatorEditTextCustom.requestFocus();
                        recyclerViewContainer.setVisibility(View.VISIBLE);
                        recyclerViewProjectCreator.setVisibility(View.VISIBLE);
                    } else {
                        projectCreatorEditTextCustom.clearFocus();
                        LabTabUtil.hideSoftKeyboard(homeActivityContext);
                        Toast.makeText(homeActivityContext, "Select a lab first by tapping Lab SKU", Toast.LENGTH_SHORT).show();
                    }
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
                SelectCreatorDialog dialog = new SelectCreatorDialog(homeActivityContext, creatorsAvailable, creatorsSelected, AddVideoFragment.this);
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
        } else if (requestCode == RESULT_LOAD_VIDEO && data != null) {

            Uri videoURI = data.getData();
            if (videoURI != null) {
                startTrimActivity(videoURI);
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
        outState.putSerializable(KNOWLEDGE_NUGGETS, knowledgeNuggets);
        outState.putString(NUGGETS, knowledgeNuggetsSelected);
        outState.putString(LEVEL, level);
        outState.putParcelable(FILE_URI, fileUri);
        outState.putParcelable(PROGRAM, program);
        super.onSaveInstanceState(outState);
    }

    public void initKnowledgeNuggets(Bundle bundle) {
        if (creatorsSelected == null || creatorsSelected.size() == 0)
            return;
        builder = new AlertDialog.Builder(homeActivityContext);
        final String[] components;
        components = LabTabApplication.getInstance().getKnowledgeNuggetsByStudent(creatorsSelected);
        builder.setCancelable(false);
        AlertDialog a = builder.create();
      /*  a.setContentView(R.layout.knowledgenuggets_expand_layout);
        ExpandableListView expandableListView=a.findViewById(R.i)*/


        final boolean[] componentSelection;
        if (components != null) {
            componentSelection = new boolean[components.length];
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
                    knowledgeNuggetsSelected = LabTabUtil.toJson(getKnowledgeNuggets(knowledgeNuggets));
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
        } else {
//            initKnowledgeNuggets(null);
        }
    }

    private String[] getKnowledgeNuggets(ArrayList<String> knowledgeNuggets) {
        String[] nuggets = new String[knowledgeNuggets.size()];
        for (int i = 0; i < knowledgeNuggets.size(); i++) {
            nuggets[i] = knowledgeNuggets.get(i);
        }
        return nuggets;
    }


    private void initListeners() {
        LabTabApplication.getInstance().addUIListener(GetProgramStudentsListener.class, this);
    }

    @Override
    public void onDestroy() {
        LabTabApplication.getInstance().removeUIListener(GetProgramStudentsListener.class, this);
        progressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void actionViewClick(ProgramOrLab programOrLab) {
        labSKUTextViewCustom.setText(String.valueOf(programOrLab.getSku()));
        level = programOrLab.getLevel().toUpperCase();
        if (program != null) {
            if (program.getSku() != programOrLab.getSku()) {
                creatorsSelected.clear();
                horizontalProjectCreatorAdapter.notifyDataSetChanged();
                knowledgeNuggets.clear();
                knowledgeNuggetsSelected = "";
                if (knowledgeNuggetsEditTextCustom != null)
                    knowledgeNuggetsEditTextCustom.setText("");
            }
        }
        progressDialog.show();
        if (ProgramStudentsTable.getInstance().getStudentsListByProgramId(programOrLab.getId()).isEmpty()) {
            BackgroundExecutor.getInstance().execute(new ProgramStudentsRequester(programOrLab.getId()));
        } else {
            progressDialog.dismiss();
            program = ProgramTable.getInstance().getProgramByProgramId(programOrLab.getId());
            creatorsAvailable.clear();
            creatorsAvailable.addAll(ProgramStudentsTable.getInstance().getStudentsListByProgramId(programOrLab.getId()));
//            projectCreatorAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void programOrLabFetchedSuccessfully(final ArrayList<ProgramOrLab> programOrLabs) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if (programOrLabs.isEmpty()) {
                    Toast.makeText(homeActivityContext, R.string.no_labs_found, Toast.LENGTH_LONG).show();
                } else {
                    showLabListDialog();
                }
            }
        });
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
            labSKUTextViewCustom.setText("");
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_INTERNET_CONNECTION);
        }
    }

    private void showLabListDialog() {
        FragmentManager fragmentManager = homeActivityContext.getSupportFragmentManager();
        LabListDialogFragment labListDialogFragment = LabListDialogFragment.newInstanceLabListDialogFragment();
        labListDialogFragment.setTargetFragment(AddVideoFragment.this, 300);
        labListDialogFragment.show(fragmentManager, "Lab_List_Dialog_Fragment");
    }

    @Override
    public void programStudentsFetchedSuccessfully(ProgramResponse programResponse, final Program programFetched, final ArrayList<Student> studentArrayList, ArrayList<Absence> absenceArrayList) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                program = programFetched;
                if (studentArrayList.isEmpty()) {
                    homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_STUDENT_FOUND_FOR_THIS_LAB);
                } else {
                    creatorsAvailable.clear();
                    creatorsAvailable.addAll(ProgramStudentsTable.getInstance().getStudentsListByProgramId(program.getId()));
//                    projectCreatorAdapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void unableToFetchProgramStudents(int responseCode) {
        homeActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                creatorsAvailable.clear();
//                projectCreatorAdapter.notifyDataSetChanged();
//                recyclerViewContainer.setVisibility(View.GONE);
//                recyclerViewProjectCreator.setVisibility(View.GONE);
                projectCreatorEditTextCustom.clearFocus();
                LabTabUtil.hideSoftKeyboard(homeActivityContext);
            }
        });
        if (responseCode == StatusCode.FORBIDDEN) {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_LAB_DETAIL_FOR_THIS_LAB);
        } else {
            labSKUTextViewCustom.setText("");
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, ToastTexts.NO_INTERNET_CONNECTION);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // create a file to save the video
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        // set the image file name
        if (fileUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        }
        // set the video image quality to high
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        //intent.putExtra("android.intent.extra.durationLimit", 300);
        // start the Video Capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }

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
                        if (optionType == GALLERY) {
                            openGalleryVideo();
                        } else {
                            openCamera();
                        }
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
//                        saveVideoData();
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
        dialog1.setTitle("Select Knowledge Nuggets");
        LinkedHashMap<String, ArrayList<Nuggests>> list;
        String[] a = convertTextToModel();

        list = LabTabApplication.getInstance().getKnowledgeNuggetHashsByStudent(creatorsSelected, a);
        sortHashMapValueList(list);  // Syadav

        ArrayList<String> keys = new ArrayList(list.keySet());
        ExpandableListView expandableListView = (ExpandableListView) dialog1.findViewById(R.id.lvExp);
        knowledgeNuggetExpand = new KnowledgeNuggetExpand(getActivity(), keys, list);
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

    private void disableKeyboard(final EditTextCustom e) {
        e.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = e.getInputType(); // backup the input type
                e.setInputType(InputType.TYPE_NULL); // disable soft input
                e.onTouchEvent(event); // call native handler
                e.setInputType(inType); // restore input type
                return true; // consume touch even
            }
        });
    }


    public void myOnKeyDown(int keycode) {
        if (keycode == 261) {

        }

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
        return validateData();
    }
}