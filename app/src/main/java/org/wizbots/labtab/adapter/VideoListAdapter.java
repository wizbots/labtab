package org.wizbots.labtab.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.interfaces.VideoListAdapterClickListener;
import org.wizbots.labtab.model.VideoList;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LabTabConstants {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<Object> objectArrayList;
    private Context context;
    private VideoListAdapterClickListener videoListAdapterClickListener;

    private class VideoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout videoListLinearLayout;
        ImageView videoThumbnailImageView;
        TextViewCustom thumbnailTextViewCustom;
        ImageView labLevelImageView;
        TextViewCustom videoNameTextViewCustom;
        TextViewCustom videoStatusTextViewCustom;
        ImageView actionViewImageView;
        ImageView actionEditImageView;
        VideoListAdapterClickListener videoListAdapterClickListener;

        VideoListViewHolder(View view, VideoListAdapterClickListener videoListAdapterClickListener) {
            super(view);
            this.videoListAdapterClickListener = videoListAdapterClickListener;
            videoListLinearLayout = (LinearLayout) view.findViewById(R.id.video_list_root_layout);
            videoThumbnailImageView = (ImageView) view.findViewById(R.id.iv_thumbnail);
            thumbnailTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_thumbnail);
            labLevelImageView = (ImageView) view.findViewById(R.id.iv_lab_level);
            videoNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_video_name);
            videoStatusTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_video_status);
            actionViewImageView = (ImageView) view.findViewById(R.id.iv_action_view);
            actionEditImageView = (ImageView) view.findViewById(R.id.iv_action_edit);
            actionEditImageView.setOnClickListener(this);
            actionViewImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            VideoList videoList = (VideoList) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.iv_action_view:
                    videoListAdapterClickListener.onVideoListItemActionView(videoList);
                    break;
                case R.id.iv_action_edit:
                    videoListAdapterClickListener.onVideoListItemActionEdit(videoList);
                    break;
            }
        }
    }

    public VideoListAdapter(ArrayList<Object> objectArrayList, Context context, VideoListAdapterClickListener videoListAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.videoListAdapterClickListener = videoListAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_DATA:
                View dataView = inflater.inflate(R.layout.item_video_list, viewGroup, false);
                viewHolder = new VideoListViewHolder(dataView, videoListAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_video_list, viewGroup, false);
                viewHolder = new VideoListViewHolder(defaultView, videoListAdapterClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof VideoList) {
            return VIEW_ITEM_DATA;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_DATA:
                VideoListViewHolder videoListViewHolder = (VideoListViewHolder) viewHolder;
                configureLabListViewHolder(videoListViewHolder, position);
                break;
            default:
                VideoListViewHolder defaultViewHolder = (VideoListViewHolder) viewHolder;
                configureLabListViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureLabListViewHolder(final VideoListViewHolder videoListViewHolder, int position) {
        VideoList videoList = (VideoList) objectArrayList.get(position);
        int videoListLinearLayoutColor;
        if (position % 2 == 0) {
            videoListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            videoListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }
        videoListViewHolder.videoListLinearLayout.setBackgroundColor(videoListLinearLayoutColor);
        LabTabUtil.setLabLevelImageResource(videoList.getLabLevel(), videoListViewHolder.labLevelImageView);
        videoListViewHolder.thumbnailTextViewCustom.setVisibility(View.VISIBLE);
        videoListViewHolder.videoThumbnailImageView.setVisibility(View.GONE);

        Picasso.with(context)
                .load(videoList.getThumbnailLinkOrPath())
                .into(videoListViewHolder.videoThumbnailImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        videoListViewHolder.thumbnailTextViewCustom.setVisibility(View.GONE);
                        videoListViewHolder.videoThumbnailImageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        int statusTextColor;
        if (videoList.getVideoStatus() == 100) {
            statusTextColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.green);
        } else if (videoList.getVideoStatus() == 0) {
            statusTextColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.red);
        } else {
            statusTextColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.orange);
        }

        videoListViewHolder.videoStatusTextViewCustom.setText(videoList.getVideoStatus() + "%");
        videoListViewHolder.videoStatusTextViewCustom.setTextColor(statusTextColor);
        videoListViewHolder.videoNameTextViewCustom.setText(videoList.getVideoName());

    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

}