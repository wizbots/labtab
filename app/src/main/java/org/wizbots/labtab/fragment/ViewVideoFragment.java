package org.wizbots.labtab.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private EditTextCustom titleEditTextCustom, projectCreatorEditTextCustom, descriptionEditTextCustom, notesToTheFamilyEditTextCustom;
    private TextViewCustom mentorNameTextViewCustom, labSKUTextViewCustom, knowledgeNuggetsEditTextCustom;
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
        knowledgeNuggetsEditTextCustom = (TextViewCustom) rootView.findViewById(R.id.edt_knowledge_nuggets);
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

        RecyclerView.LayoutManager horizontalLayoutManager = new GridLayoutManager(getActivity(),2);
        if (getRotation(getActivity()).equalsIgnoreCase("landscape") || getRotation(getActivity()).equalsIgnoreCase("reverse landscape")) {
            horizontalLayoutManager = new GridLayoutManager(getActivity(), 3);

        }
       // RecyclerView.LayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
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


    public void prepareCategoryList() {
      //  String[] categories = homeActivityContext.getResources().getStringArray(R.array.array_category);
        String[] categories = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCategory();

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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkAndRequestPermissionsSingle()){
            Glide.with(context)
                    .load(Uri.fromFile(new File(video.getPath())))
                    .into(videoThumbnailImageView);
        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Glide.with(context)
                    .load(Uri.fromFile(new File(video.getPath())))
                    .into(videoThumbnailImageView);
        }
    }

        /*
    * Permission Code Start Here
    * * */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constants.PERMISSION_REQUEST_CODE:
                final Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                if(grantResults.length > 0){
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Glide.with(context)
                                .load(Uri.fromFile(new File(video.getPath())))
                                .into(videoThumbnailImageView);
                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    || shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    switch (which){
                                                        case DialogInterface.BUTTON_POSITIVE:
                                                            Set<String> pendingPermission = new HashSet();
                                                            for (String key : perms.keySet()) {
                                                                if(perms.get(key) != PackageManager.PERMISSION_GRANTED){
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
                            }else {
                                Toast.makeText(homeActivityContext, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                break;
            case Constants.PERMISSION_REQUEST_CODE_STORAGE:
                final Map<String, Integer> perms1 = new HashMap<String, Integer>();
                perms1.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if(grantResults.length > 0){
                    for (int i = 0; i < permissions.length; i++)
                        perms1.put(permissions[i], grantResults[i]);
                    if (perms1.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  PackageManager.PERMISSION_GRANTED) {
                        Glide.with(context)
                                .load(Uri.fromFile(new File(video.getPath())))
                                .into(videoThumbnailImageView);
                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    switch (which){
                                                        case DialogInterface.BUTTON_POSITIVE:
                                                            Set<String> pendingPermission = new HashSet();
                                                            for (String key : perms1.keySet()) {
                                                                if(perms1.get(key) != PackageManager.PERMISSION_GRANTED){
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
                            }else {
                                Toast.makeText(homeActivityContext, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener listener) {
        new androidx.appcompat.app.AlertDialog.Builder(homeActivityContext)
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
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),Constants.PERMISSION_REQUEST_CODE);
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
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),Constants.PERMISSION_REQUEST_CODE_STORAGE);
            return false;
        }
        return true;
    }
}
