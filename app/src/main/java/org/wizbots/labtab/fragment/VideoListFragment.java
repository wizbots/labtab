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

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.VideoListAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.interfaces.VideoListAdapterClickListener;
import org.wizbots.labtab.model.Video;

import java.util.ArrayList;

public class VideoListFragment extends ParentFragment implements VideoListAdapterClickListener {

    public static final String VIDEO = "VIDEO";
    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private VideoListAdapter videoListAdapter;
    private RecyclerView recyclerViewVideoList;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private HomeActivity homeActivityContext;

    public VideoListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video_list, container, false);
        homeActivityContext = (HomeActivity) context;
        initView();
        prepareVideoList();
        return rootView;
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.VIDEO_LIST);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_notsynced);

        recyclerViewVideoList = (RecyclerView) rootView.findViewById(R.id.recycler_view_video_list);
//        recyclerViewVideoList.setFocusable(false);
        objectArrayList = new ArrayList<>();

        videoListAdapter = new VideoListAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewVideoList.setLayoutManager(mLayoutManager);
        recyclerViewVideoList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideoList.setAdapter(videoListAdapter);
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
    }

    @Override
    public String getFragmentName() {
        return VideoListFragment.class.getSimpleName();
    }

    @Override
    public void onVideoListItemActionView(Video video) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(VIDEO, video);
        homeActivityContext.replaceFragment(Fragments.VIEW_VIDEO, bundle);
    }

    @Override
    public void onVideoListItemActionEdit(Video video) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(VIDEO, video);
        homeActivityContext.replaceFragment(Fragments.EDIT_VIDEO, bundle);
    }

    @Override
    public void playVideo(Video video) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getPath()));
        intent.setDataAndType(Uri.parse(video.getPath()), "video/*");
        startActivity(intent);
    }

    public void prepareVideoList() {
        ArrayList<Video> videoArrayList = VideoTable.getInstance().getVideoList();
        if (!videoArrayList.isEmpty()) {
            objectArrayList.addAll(videoArrayList);
            videoListAdapter.notifyDataSetChanged();

            boolean videoUploadTaskCompleted = true;
            for (Video video : videoArrayList) {
                videoUploadTaskCompleted &= (video.getStatus() == 100);
            }

            if (videoUploadTaskCompleted) {
                labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
            } else {
                labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_notsynced);
            }
        } else {
            homeActivityContext.sendMessageToHandler(homeActivityContext.SHOW_TOAST, -1, -1, "No Video Available");
            labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

        }
    }
}
