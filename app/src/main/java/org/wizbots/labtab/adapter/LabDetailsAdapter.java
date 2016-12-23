package org.wizbots.labtab.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.interfaces.LabDetailsAdapterClickListener;
import org.wizbots.labtab.model.LabDetails;

import java.util.ArrayList;

public class LabDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<Object> objectArrayList;
    private Context context;
    private LabDetailsAdapterClickListener labDetailsAdapterClickListener;

    private class LabDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout labDetailsLinearLayout;
        CheckBox checkBoxLabDetail;
        TextViewCustom studentNameTextViewCustom;
        ImageView labLevelImageView;
        TextViewCustom noOfProjectsTextViewCustom;
        TextViewCustom noOfLabTimeTextViewCustom;
        TextViewCustom noOfDoneTextViewCustom;
        TextViewCustom noOfSkippedTextViewCustom;
        TextViewCustom noOfPendingTextViewCustom;
        ImageView actionViewImageView;
        ImageView actionEditLabLevelImageView;
        ImageView actionCloseToNextLevelLabLevelImageView;
        LabDetailsAdapterClickListener labDetailsAdapterClickListener;

        LabDetailsViewHolder(View view, final LabDetailsAdapterClickListener labDetailsAdapterClickListener) {
            super(view);
            this.labDetailsAdapterClickListener = labDetailsAdapterClickListener;
            labDetailsLinearLayout = (LinearLayout) view.findViewById(R.id.lab_details_root_layout);
            checkBoxLabDetail = (CheckBox) view.findViewById(R.id.cb_lab_detail);
            studentNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_student_name);
            labLevelImageView = (ImageView) view.findViewById(R.id.iv_lab_level);
            noOfProjectsTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_projects);
            noOfLabTimeTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_lab_time);
            noOfDoneTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_done);
            noOfSkippedTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_skipped);
            noOfPendingTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_pending);
            actionViewImageView = (ImageView) view.findViewById(R.id.iv_action_view);
            actionEditLabLevelImageView = (ImageView) view.findViewById(R.id.iv_action_edit);
            actionCloseToNextLevelLabLevelImageView = (ImageView) view.findViewById(R.id.iv_action_close_to_next_level);
            actionViewImageView.setOnClickListener(this);
            actionEditLabLevelImageView.setOnClickListener(this);
            actionCloseToNextLevelLabLevelImageView.setOnClickListener(this);
            checkBoxLabDetail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checkState) {
                    labDetailsAdapterClickListener.onCheckChanged(getAdapterPosition(), checkState);
                }
            });
        }

        @Override
        public void onClick(View view) {
            LabDetails labDetails = (LabDetails) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.iv_action_view:
                    labDetailsAdapterClickListener.onActionViewClick(labDetails);
                    break;
                case R.id.iv_action_edit:
                    labDetailsAdapterClickListener.onActionEditClick(labDetails);
                    break;
                case R.id.iv_action_close_to_next_level:
                    labDetailsAdapterClickListener.onActionCloseToNextLevelClick(labDetails);
                    break;
            }
        }
    }

    public LabDetailsAdapter(ArrayList<Object> objectArrayList, Context context, LabDetailsAdapterClickListener labDetailsAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.labDetailsAdapterClickListener = labDetailsAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_DATA:
                View dataView = inflater.inflate(R.layout.item_lab_details_list, viewGroup, false);
                viewHolder = new LabDetailsViewHolder(dataView, labDetailsAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_lab_details_list, viewGroup, false);
                viewHolder = new LabDetailsViewHolder(defaultView, labDetailsAdapterClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof LabDetails) {
            return VIEW_ITEM_DATA;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_DATA:
                LabDetailsViewHolder labDetailsViewHolder = (LabDetailsViewHolder) viewHolder;
                configureLabListViewHolder(labDetailsViewHolder, position);
                break;
            default:
                LabDetailsViewHolder defaultViewHolder = (LabDetailsViewHolder) viewHolder;
                configureLabListViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureLabListViewHolder(LabDetailsViewHolder labDetailsViewHolder, int position) {
        LabDetails labDetails = (LabDetails) objectArrayList.get(position);
        int labDetailsLinearLayoutColor;
        if (position % 2 == 0) {
            labDetailsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            labDetailsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }
        labDetailsViewHolder.labDetailsLinearLayout.setBackgroundColor(labDetailsLinearLayoutColor);
        labDetailsViewHolder.checkBoxLabDetail.setChecked(labDetails.isCheck());
        labDetailsViewHolder.studentNameTextViewCustom.setText(labDetails.getStudentName());
        labDetailsViewHolder.labLevelImageView.setImageResource(labDetails.getLevel());
        labDetailsViewHolder.noOfProjectsTextViewCustom.setText(labDetails.getNo_of_projects());
        labDetailsViewHolder.noOfLabTimeTextViewCustom.setText(labDetails.getNo_of_lab_time());
        labDetailsViewHolder.noOfDoneTextViewCustom.setText(labDetails.getNo_of_done());
        labDetailsViewHolder.noOfSkippedTextViewCustom.setText(labDetails.getNo_of_skipped());
        labDetailsViewHolder.noOfPendingTextViewCustom.setText(labDetails.getNo_of_pending());
        if (labDetails.isCloseToNextLevel()) {
            labDetailsViewHolder.actionCloseToNextLevelLabLevelImageView.setVisibility(View.VISIBLE);
        } else {
            labDetailsViewHolder.actionCloseToNextLevelLabLevelImageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

}