package org.wizbots.labtab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.adapter.VideoListAdapter;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.interfaces.VideoListAdapterClickListener;
import org.wizbots.labtab.model.VideoList;

import java.util.ArrayList;

public class VideoListFragment extends ParentFragment implements VideoListAdapterClickListener {

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
        prepareDummyList();
        return rootView;
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText("Video List");
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.menu);

        recyclerViewVideoList = (RecyclerView) rootView.findViewById(R.id.recycler_view_video_list);
        objectArrayList = new ArrayList<>();

        videoListAdapter = new VideoListAdapter(objectArrayList, homeActivityContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewVideoList.setLayoutManager(mLayoutManager);
        recyclerViewVideoList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideoList.setAdapter(videoListAdapter);
    }

    @Override
    public String getFragmentName() {
        return VideoListFragment.class.getSimpleName();
    }

    @Override
    public void onVideoListItemActionView(VideoList videoList) {
        homeActivityContext.replaceFragment(FRAGMENT_ADD_VIDEO);
    }

    @Override
    public void onVideoListItemActionEdit(VideoList videoList) {
        homeActivityContext.replaceFragment(FRAGMENT_EDIT_VIDEO);
    }

    public void prepareDummyList() {
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_APPRENTICE, "Video Name", 100));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/", LAB_LEVEL_EXPLORER, "Video Name", 70));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_IMAGINEER, "Video Name", 0));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/", LAB_LEVEL_LAB_CERTIFIED, "Video Name", 30));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_MASTER, "Video Name", 100));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/", LAB_LEVEL_MAKER, "Video Name", 0));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_WIZARD, "Video Name", 70));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_APPRENTICE, "Video Name", 100));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/", LAB_LEVEL_EXPLORER, "Video Name", 70));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_IMAGINEER, "Video Name", 0));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/", LAB_LEVEL_LAB_CERTIFIED, "Video Name", 30));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_MASTER, "Video Name", 100));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/", LAB_LEVEL_MAKER, "Video Name", 0));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_WIZARD, "Video Name", 70));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_APPRENTICE, "Video Name", 100));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/", LAB_LEVEL_EXPLORER, "Video Name", 70));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_IMAGINEER, "Video Name", 0));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/", LAB_LEVEL_LAB_CERTIFIED, "Video Name", 30));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_MASTER, "Video Name", 100));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/", LAB_LEVEL_MAKER, "Video Name", 0));
        objectArrayList.add(new VideoList("http://cdn.shopify.com/s/files/1/0151/7261/t/7/assets/page_home_img.png?8350576394726272064", LAB_LEVEL_WIZARD, "Video Name", 70));
        videoListAdapter.notifyDataSetChanged();
    }
}
