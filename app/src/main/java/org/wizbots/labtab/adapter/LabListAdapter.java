package org.wizbots.labtab.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.interfaces.LabListAdapterClickListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class LabListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LabTabConstants {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<Object> objectArrayList;
    private Context context;
    private LabListAdapterClickListener labListAdapterClickListener;

    private class LabListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout labListLinearLayout;
        ImageView labLevelImageView;
        TextViewCustom labNameTextViewCustom;
        ImageView actionImageView,rosterDetailsImageView;
        LabListAdapterClickListener labListAdapterClickListener;

        LabListViewHolder(View view, LabListAdapterClickListener labListAdapterClickListener) {
            super(view);
            this.labListAdapterClickListener = labListAdapterClickListener;
            labListLinearLayout = (LinearLayout) view.findViewById(R.id.lab_list_root_layout);
            labLevelImageView = (ImageView) view.findViewById(R.id.iv_lab_level);
            labNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_lab_name);
            actionImageView = (ImageView) view.findViewById(R.id.iv_action);
            actionImageView.setOnClickListener(this);
            rosterDetailsImageView = (ImageView) view.findViewById(R.id.roster_details);
            rosterDetailsImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ProgramOrLab labList = (ProgramOrLab) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.iv_action:
                    labListAdapterClickListener.onActionViewClick(labList);
                    break;
                case R.id.roster_details:
                    labListAdapterClickListener.onRosterDetailsClick(labList);
                    break;
            }
        }
    }

    public LabListAdapter(ArrayList<Object> objectArrayList, Context context, LabListAdapterClickListener labListAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.labListAdapterClickListener = labListAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_DATA:
                View dataView = inflater.inflate(R.layout.item_lab_list, viewGroup, false);
                viewHolder = new LabListViewHolder(dataView, labListAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_lab_list, viewGroup, false);
                viewHolder = new LabListViewHolder(defaultView, labListAdapterClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof ProgramOrLab) {
            return VIEW_ITEM_DATA;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_DATA:
                LabListViewHolder labListViewHolder = (LabListViewHolder) viewHolder;
                configureLabListViewHolder(labListViewHolder, position);
                break;
            default:
                LabListViewHolder defaultViewHolder = (LabListViewHolder) viewHolder;
                configureLabListViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureLabListViewHolder(LabListViewHolder labListViewHolder, int position) {
        ProgramOrLab labList = (ProgramOrLab) objectArrayList.get(position);
        int labListLinearLayoutColor;
        if (position % 2 == 0) {
            labListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            labListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }
        labListViewHolder.labListLinearLayout.setBackgroundColor(labListLinearLayoutColor);
        LabTabUtil.setLabLevelImageResource(labList.getLevel(), labListViewHolder.labLevelImageView);
        labListViewHolder.labNameTextViewCustom.setText(labList.getTitle());
        labListViewHolder.actionImageView.setImageResource(R.drawable.ic_action_view);
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

}