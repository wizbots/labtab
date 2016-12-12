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
import org.wizbots.labtab.interfaces.StudentStatsAdapterClickListener;
import org.wizbots.labtab.model.LabListHeader;
import org.wizbots.labtab.model.StudentStatistics;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class StudentStatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LabTabConstants {

    private final int VIEW_ITEM_HEADER = 0;
    private final int VIEW_ITEM_DATA = 1;
    private final int VIEW_ITEM_FOOTER = 2;
    private ArrayList<Object> objectArrayList;
    private Context context;
    private StudentStatsAdapterClickListener studentStatsAdapterClickListener;

    private class StudentStatsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout studentStatsLinearLayout;
        ImageView labLevelImageView;
        TextViewCustom noOfProjectsTextViewCustom;
        TextViewCustom noOfLabTimeTextViewCustom;
        TextViewCustom noOfDoneTextViewCustom;
        TextViewCustom noOfSkippedTextViewCustom;
        TextViewCustom noOfPendingTextViewCustom;
        TextViewCustom noOfImagineeringTextViewCustom;
        TextViewCustom noOfProgrammingTextViewCustom;
        TextViewCustom noOfMechanismsTextViewCustom;
        TextViewCustom noOfStructuresTextViewCustom;

        StudentStatsAdapterClickListener studentStatsAdapterClickListener;

        StudentStatsViewHolder(View view, StudentStatsAdapterClickListener studentStatsAdapterClickListener) {
            super(view);
            this.studentStatsAdapterClickListener = studentStatsAdapterClickListener;
            studentStatsLinearLayout = (LinearLayout) view.findViewById(R.id.student_stats_root_layout);
            labLevelImageView = (ImageView) view.findViewById(R.id.iv_lab_level);
            noOfProjectsTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_projects);
            noOfLabTimeTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_lab_time);
            noOfDoneTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_done);
            noOfSkippedTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_skipped);
            noOfPendingTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_pending);
            noOfImagineeringTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_imagineering);
            noOfProgrammingTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_programming);
            noOfMechanismsTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_mechanisms);
            noOfStructuresTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_structures);
            studentStatsLinearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            StudentStatistics studentStatistics = (StudentStatistics) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.student_stats_root_layout:
                    studentStatsAdapterClickListener.onActionViewClick();
                    break;
            }
        }
    }

    private class StudentStatsHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout studentStatsLinearLayout;
        ImageView labLevelImageView;
        TextViewCustom projectsTextViewCustom;
        TextViewCustom labTimeTextViewCustom;
        TextViewCustom doneTextViewCustom;
        TextViewCustom skippedTextViewCustom;
        TextViewCustom pendingTextViewCustom;
        ImageView imagineeringImageView;
        ImageView programmingImageView;
        ImageView mechanismsImageView;
        ImageView structuresImageView;

        StudentStatsAdapterClickListener studentStatsAdapterClickListener;

        StudentStatsHeaderHolder(View view, StudentStatsAdapterClickListener studentStatsAdapterClickListener) {
            super(view);
            this.studentStatsAdapterClickListener = studentStatsAdapterClickListener;
            studentStatsLinearLayout = (LinearLayout) view.findViewById(R.id.student_stats_root_layout);
            labLevelImageView = (ImageView) view.findViewById(R.id.iv_lab_level);
            projectsTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_projects);
            labTimeTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_lab_time);
            doneTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_done);
            skippedTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_skipped);
            pendingTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_pending);
            imagineeringImageView = (ImageView) view.findViewById(R.id.iv_marks_imagineering);
            programmingImageView = (ImageView) view.findViewById(R.id.iv_marks_programming);
            mechanismsImageView = (ImageView) view.findViewById(R.id.iv_marks_mechanisms);
            structuresImageView = (ImageView) view.findViewById(R.id.iv_marks_structures);
            studentStatsLinearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            StudentStatistics studentStatistics = (StudentStatistics) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.student_stats_root_layout:
                    studentStatsAdapterClickListener.onActionViewClick();
                    break;
            }
        }
    }

    public StudentStatsAdapter(ArrayList<Object> objectArrayList, Context context, StudentStatsAdapterClickListener studentStatsAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.studentStatsAdapterClickListener = studentStatsAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_HEADER:
                View headerView = inflater.inflate(R.layout.item_student_stats, viewGroup, false);
                viewHolder = new StudentStatsHeaderHolder(headerView, studentStatsAdapterClickListener);
                break;
            case VIEW_ITEM_DATA:
                View dataView = inflater.inflate(R.layout.item_student_stats, viewGroup, false);
                viewHolder = new StudentStatsViewHolder(dataView, studentStatsAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_student_stats, viewGroup, false);
                viewHolder = new StudentStatsHeaderHolder(defaultView, studentStatsAdapterClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof StudentStatistics) {
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
                StudentStatsHeaderHolder studentStatsHeaderHolder = (StudentStatsHeaderHolder) viewHolder;
                configureLabListHeaderHolder(studentStatsHeaderHolder, position);
                break;
            case VIEW_ITEM_DATA:
                StudentStatsViewHolder studentStatsViewHolder = (StudentStatsViewHolder) viewHolder;
                configureLabListViewHolder(studentStatsViewHolder, position);
                break;
            default:
                StudentStatsHeaderHolder defaultHeaderHolder = (StudentStatsHeaderHolder) viewHolder;
                configureLabListHeaderHolder(defaultHeaderHolder, position);
                break;
        }
    }

    private void configureLabListViewHolder(StudentStatsViewHolder studentStatsViewHolder, int position) {
        StudentStatistics studentStatistics = (StudentStatistics) objectArrayList.get(position);
        int studentsStatsListLinearLayoutColor;
        if (position % 2 == 0) {
            studentsStatsListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            studentsStatsListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }
        studentStatsViewHolder.studentStatsLinearLayout.setBackgroundColor(studentsStatsListLinearLayoutColor);
        LabTabUtil.setLabLevelImageResource(studentStatistics.getLabLevel(), studentStatsViewHolder.labLevelImageView);
        studentStatsViewHolder.noOfProjectsTextViewCustom.setText(studentStatistics.getNoOfProjects());
        studentStatsViewHolder.noOfLabTimeTextViewCustom.setText(studentStatistics.getNoOfLabTime());
        studentStatsViewHolder.noOfDoneTextViewCustom.setText(studentStatistics.getNoOfDone());
        studentStatsViewHolder.noOfSkippedTextViewCustom.setText(studentStatistics.getNoOfSkipped());
        studentStatsViewHolder.noOfPendingTextViewCustom.setText(studentStatistics.getNoOfPending());
        studentStatsViewHolder.noOfImagineeringTextViewCustom.setText(studentStatistics.getNoOfImagineering());
        studentStatsViewHolder.noOfProgrammingTextViewCustom.setText(studentStatistics.getNoOfProgramming());
        studentStatsViewHolder.noOfMechanismsTextViewCustom.setText(studentStatistics.getNoOfMechanisms());
        studentStatsViewHolder.noOfStructuresTextViewCustom.setText(studentStatistics.getNoOfStructures());
    }

    private void configureLabListHeaderHolder(StudentStatsHeaderHolder studentStatsHeaderHolder, int position) {
        LabListHeader labListHeader = (LabListHeader) objectArrayList.get(position);
        int labListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }
}