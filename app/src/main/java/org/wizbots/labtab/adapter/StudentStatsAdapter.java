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
import org.wizbots.labtab.model.student.StudentStats;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class StudentStatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LabTabConstants {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<StudentStats> objectArrayList;
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
            StudentStats studentStats = (StudentStats) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.student_stats_root_layout:
                    studentStatsAdapterClickListener.onActionViewClick(studentStats.getLevel());
                    break;
            }
        }
    }

    public StudentStatsAdapter(ArrayList<StudentStats> objectArrayList, Context context, StudentStatsAdapterClickListener studentStatsAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.studentStatsAdapterClickListener = studentStatsAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_DATA:
                View dataView = inflater.inflate(R.layout.item_student_stats, viewGroup, false);
                viewHolder = new StudentStatsViewHolder(dataView, studentStatsAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_student_stats, viewGroup, false);
                viewHolder = new StudentStatsViewHolder(defaultView, studentStatsAdapterClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof StudentStats) {
            return VIEW_ITEM_DATA;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_DATA:
                StudentStatsViewHolder studentStatsViewHolder = (StudentStatsViewHolder) viewHolder;
                configureLabListViewHolder(studentStatsViewHolder, position);
                break;
            default:
                StudentStatsViewHolder defaultViewHolder = (StudentStatsViewHolder) viewHolder;
                configureLabListViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureLabListViewHolder(StudentStatsViewHolder studentStatsViewHolder, int position) {
        StudentStats studentStats = (StudentStats) objectArrayList.get(position);
        int studentsStatsListLinearLayoutColor;
        if (position % 2 == 0) {
            studentsStatsListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            studentsStatsListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }
        studentStatsViewHolder.studentStatsLinearLayout.setBackgroundColor(studentsStatsListLinearLayoutColor);
        LabTabUtil.setLabLevelImageResource(studentStats.getLevel(), studentStatsViewHolder.labLevelImageView);
        studentStatsViewHolder.noOfProjectsTextViewCustom.setText(String.valueOf(studentStats.getProject_count()));
        studentStatsViewHolder.noOfLabTimeTextViewCustom.setText(studentStats.getLab_time_count().replaceAll("\"", ""));
        studentStatsViewHolder.noOfDoneTextViewCustom.setText(String.valueOf(studentStats.getDone_count()));
        studentStatsViewHolder.noOfSkippedTextViewCustom.setText(String.valueOf(studentStats.getSkipped_count()));
        studentStatsViewHolder.noOfPendingTextViewCustom.setText(String.valueOf(studentStats.getPending_count()));
        studentStatsViewHolder.noOfImagineeringTextViewCustom.setText(String.valueOf(studentStats.getImagineering_count()));
        studentStatsViewHolder.noOfProgrammingTextViewCustom.setText(String.valueOf(studentStats.getProgramming_count()));
        studentStatsViewHolder.noOfMechanismsTextViewCustom.setText(String.valueOf(studentStats.getMechanisms_count()));
        studentStatsViewHolder.noOfStructuresTextViewCustom.setText(String.valueOf(studentStats.getStructures_count()));
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }
}