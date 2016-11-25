package org.wizbots.labtab.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.model.LabList;
import org.wizbots.labtab.model.LabListHeader;
import org.wizbots.labtab.util.TextViewCustom;

import java.util.ArrayList;

public class LabListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_HEADER = 0;
    private final int VIEW_ITEM_DATA = 1;
    private final int VIEW_ITEM_FOOTER = 2;
    private ArrayList<Object> objectArrayList;

    class LabListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout labListLinearLayout;
        ImageView labLevelImageView;
        TextViewCustom labNameTextViewCustom;
        ImageView actionImageView;

        LabListViewHolder(View view) {
            super(view);
            labListLinearLayout = (LinearLayout) view.findViewById(R.id.lab_list_root_layout);
            labLevelImageView = (ImageView) view.findViewById(R.id.iv_lab_level);
            labNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_lab_name);
            actionImageView = (ImageView) view.findViewById(R.id.iv_action);
        }
    }

    class LabListHeaderHolder extends RecyclerView.ViewHolder {
        LinearLayout labListLinearLayout;
        TextViewCustom levelTextViewCustom;
        TextViewCustom nameTextViewCustom;
        TextViewCustom actionTextViewCustom;

        LabListHeaderHolder(View view) {
            super(view);
            labListLinearLayout = (LinearLayout) view.findViewById(R.id.lab_list_root_layout);
            levelTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_level);
            nameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_name);
            actionTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_action);
        }
    }

    public LabListAdapter(ArrayList<Object> objectArrayList) {
        this.objectArrayList = objectArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_HEADER:
                View headerView = inflater.inflate(R.layout.item_lab_list, viewGroup, false);
                viewHolder = new LabListHeaderHolder(headerView);
                break;
            case VIEW_ITEM_DATA:
                View dataView = inflater.inflate(R.layout.item_lab_list, viewGroup, false);
                viewHolder = new LabListViewHolder(dataView);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_lab_list, viewGroup, false);
                viewHolder = new LabListHeaderHolder(defaultView);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof LabList) {
            return VIEW_ITEM_DATA;
        } else if (objectArrayList.get(position) instanceof LabListHeader) {
            return VIEW_ITEM_HEADER;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_HEADER:
                LabListHeaderHolder labListHeaderHolder = (LabListHeaderHolder) viewHolder;
                configureLabListHeaderHolder(labListHeaderHolder, position);
                break;
            case VIEW_ITEM_DATA:
                LabListViewHolder labListViewHolder = (LabListViewHolder) viewHolder;
                configureLabListViewHolder(labListViewHolder, position);
                break;
            default:
                LabListHeaderHolder defaultHeaderHolder = (LabListHeaderHolder) viewHolder;
                configureLabListHeaderHolder(defaultHeaderHolder, position);
                break;
        }
    }

    private void configureLabListViewHolder(LabListViewHolder labListViewHolder, int position) {
        LabList labList = (LabList) objectArrayList.get(position);
        int labListLinearLayoutColor;
        if (position % 2 == 0) {
            labListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            labListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }
        labListViewHolder.labListLinearLayout.setBackgroundColor(labListLinearLayoutColor);
        labListViewHolder.labLevelImageView.setVisibility(View.VISIBLE);
        labListViewHolder.labNameTextViewCustom.setVisibility(View.VISIBLE);
        labListViewHolder.actionImageView.setVisibility(View.VISIBLE);
        labListViewHolder.labLevelImageView.setImageResource(labList.getLevel());
        labListViewHolder.labNameTextViewCustom.setText(labList.getLabName());
        labListViewHolder.actionImageView.setImageResource(labList.getAction());
    }

    private void configureLabListHeaderHolder(LabListHeaderHolder labListHeaderHolder, int position) {
        LabListHeader labListHeader = (LabListHeader) objectArrayList.get(position);
        int labListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        labListHeaderHolder.labListLinearLayout.setBackgroundColor(labListLinearLayoutColor);
        labListHeaderHolder.levelTextViewCustom.setVisibility(View.VISIBLE);
        labListHeaderHolder.nameTextViewCustom.setVisibility(View.VISIBLE);
        labListHeaderHolder.actionTextViewCustom.setVisibility(View.VISIBLE);
        labListHeaderHolder.levelTextViewCustom.setText(labListHeader.getLevel());
        labListHeaderHolder.nameTextViewCustom.setText(labListHeader.getName());
        labListHeaderHolder.actionTextViewCustom.setText(labListHeader.getAction());
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

}