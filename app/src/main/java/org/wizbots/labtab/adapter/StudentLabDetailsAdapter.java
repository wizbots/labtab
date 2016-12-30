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
import org.wizbots.labtab.interfaces.StudentLabDetailsAdapterClickListener;
import org.wizbots.labtab.model.StudentLabDetailsType1;
import org.wizbots.labtab.model.StudentLabDetailsType2;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class StudentLabDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LabTabConstants {

    private final int VIEW_ITEM_DATA_TYPE_1 = 1;
    private final int VIEW_ITEM_DATA_TYPE_2 = 2;
    private ArrayList<Object> objectArrayList;
    private Context context;
    private StudentLabDetailsAdapterClickListener studentLabDetailsAdapterClickListener;

    private class StudentLabDetailsType1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout studentLabDetailsType1LinearLayout;
        ImageView labLevelImageView;
        TextViewCustom studentNameTextViewCustom;
        TextViewCustom noOfProjectsTextViewCustom;
        TextViewCustom noOfLabTimeTextViewCustom;
        TextViewCustom noOfDoneTextViewCustom;
        TextViewCustom noOfSkippedTextViewCustom;
        TextViewCustom noOfPendingTextViewCustom;
        ImageView actionImageView;

        StudentLabDetailsAdapterClickListener studentLabDetailsAdapterClickListener;

        StudentLabDetailsType1ViewHolder(View view, StudentLabDetailsAdapterClickListener studentLabDetailsAdapterClickListener) {
            super(view);
            this.studentLabDetailsAdapterClickListener = studentLabDetailsAdapterClickListener;
            studentLabDetailsType1LinearLayout = (LinearLayout) view.findViewById(R.id.student_lab_details_root_layout_type_1);
            labLevelImageView = (ImageView) view.findViewById(R.id.iv_lab_level);
            studentNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_student_name);
            noOfProjectsTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_projects);
            noOfLabTimeTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_lab_time);
            noOfDoneTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_done);
            noOfSkippedTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_skipped);
            noOfPendingTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_pending);
            actionImageView = (ImageView) view.findViewById(R.id.iv_action_view);
            actionImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            StudentLabDetailsType1 studentLabDetailsType1 = (StudentLabDetailsType1) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.iv_action_view:
                    studentLabDetailsAdapterClickListener.onViewTypeClick1();
                    break;
            }
        }
    }

    private class StudentLabDetailsType2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout studentLabDetailsType2LinearLayout;
        ImageView labLevelImageView;
        TextViewCustom studentNameTextViewCustom;
        TextViewCustom noOfProjectsTextViewCustom;
        TextViewCustom noOfLabTimeTextViewCustom;
        TextViewCustom noOfDoneTextViewCustom;
        TextViewCustom noOfSkippedTextViewCustom;
        TextViewCustom noOfPendingTextViewCustom;
        ImageView actionImageView;

        StudentLabDetailsAdapterClickListener studentLabDetailsAdapterClickListener;

        StudentLabDetailsType2ViewHolder(View view, StudentLabDetailsAdapterClickListener studentLabDetailsAdapterClickListener) {
            super(view);
            this.studentLabDetailsAdapterClickListener = studentLabDetailsAdapterClickListener;
            studentLabDetailsType2LinearLayout = (LinearLayout) view.findViewById(R.id.student_lab_details_root_layout_type_2);
            labLevelImageView = (ImageView) view.findViewById(R.id.iv_lab_level);
            studentNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_student_name);
            noOfProjectsTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_projects);
            noOfLabTimeTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_lab_time);
            noOfDoneTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_done);
            noOfSkippedTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_skipped);
            noOfPendingTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_pending);
            actionImageView = (ImageView) view.findViewById(R.id.iv_action_view);
            actionImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            StudentLabDetailsType2 studentLabDetailsType2 = (StudentLabDetailsType2) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.iv_action_view:
                    studentLabDetailsAdapterClickListener.onViewTypeClick2();
                    break;
            }
        }
    }

    public StudentLabDetailsAdapter(ArrayList<Object> objectArrayList, Context context, StudentLabDetailsAdapterClickListener studentLabDetailsAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.studentLabDetailsAdapterClickListener = studentLabDetailsAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_DATA_TYPE_1:
                View dataView1 = inflater.inflate(R.layout.item_student_lab_details_type_1, viewGroup, false);
                viewHolder = new StudentLabDetailsType1ViewHolder(dataView1, studentLabDetailsAdapterClickListener);
                break;
            case VIEW_ITEM_DATA_TYPE_2:
                View dataView2 = inflater.inflate(R.layout.item_student_lab_details_type_2, viewGroup, false);
                viewHolder = new StudentLabDetailsType2ViewHolder(dataView2, studentLabDetailsAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_student_lab_details_type_1, viewGroup, false);
                viewHolder = new StudentLabDetailsType1ViewHolder(defaultView, studentLabDetailsAdapterClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof StudentLabDetailsType1) {
            return VIEW_ITEM_DATA_TYPE_1;
        } else if (objectArrayList.get(position) instanceof StudentLabDetailsType2) {
            return VIEW_ITEM_DATA_TYPE_2;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_DATA_TYPE_1:
                StudentLabDetailsType1ViewHolder studentLabDetailsType1ViewHolder = (StudentLabDetailsType1ViewHolder) viewHolder;
                configureStudentLabDetailListType1ViewHolder(studentLabDetailsType1ViewHolder, position);
                break;
            case VIEW_ITEM_DATA_TYPE_2:
                StudentLabDetailsType2ViewHolder studentLabDetailsType2ViewHolder = (StudentLabDetailsType2ViewHolder) viewHolder;
                configureStudentStatsDetailListType2ViewHolder(studentLabDetailsType2ViewHolder, position);
                break;
            default:
                StudentLabDetailsType1ViewHolder defaultViewHolder = (StudentLabDetailsType1ViewHolder) viewHolder;
                configureStudentLabDetailListType1ViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureStudentLabDetailListType1ViewHolder(StudentLabDetailsType1ViewHolder studentLabDetailsType1ViewHolder, int position) {
        StudentLabDetailsType1 studentLabDetailsType1 = (StudentLabDetailsType1) objectArrayList.get(position);
        LabTabUtil.setLabLevelImageResource(studentLabDetailsType1.getLabLevel(), studentLabDetailsType1ViewHolder.labLevelImageView);
        studentLabDetailsType1ViewHolder.studentNameTextViewCustom.setText(studentLabDetailsType1.getStudentName());
        studentLabDetailsType1ViewHolder.noOfProjectsTextViewCustom.setText(studentLabDetailsType1.getNoOfProjects());
        studentLabDetailsType1ViewHolder.noOfLabTimeTextViewCustom.setText(studentLabDetailsType1.getNoOfLabTime());
        studentLabDetailsType1ViewHolder.noOfDoneTextViewCustom.setText(studentLabDetailsType1.getNoOfDone());
        studentLabDetailsType1ViewHolder.noOfSkippedTextViewCustom.setText(studentLabDetailsType1.getNoOfSkipped());
        studentLabDetailsType1ViewHolder.noOfPendingTextViewCustom.setText(studentLabDetailsType1.getNoOfPending());
        studentLabDetailsType1ViewHolder.actionImageView.setImageResource(R.drawable.ic_back);
    }

    private void configureStudentStatsDetailListType2ViewHolder(StudentLabDetailsType2ViewHolder studentLabDetailsType2ViewHolder, int position) {
        StudentLabDetailsType2 studentLabDetailsType2 = (StudentLabDetailsType2) objectArrayList.get(position);
        int studentsLabDetailListLinearLayoutColor;
        int labDetailTextColor;
        if (position % 2 == 0) {
            studentsLabDetailListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
            labDetailTextColor = ContextCompat.getColor(LabTabApplication.getInstance(), android.R.color.black);
        } else {
            studentsLabDetailListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.orange);
            labDetailTextColor = ContextCompat.getColor(LabTabApplication.getInstance(), android.R.color.white);
        }
        studentLabDetailsType2ViewHolder.studentLabDetailsType2LinearLayout.setBackgroundColor(studentsLabDetailListLinearLayoutColor);
        LabTabUtil.setLabLevelImageResource(studentLabDetailsType2.getLabLevel(), studentLabDetailsType2ViewHolder.labLevelImageView);
        studentLabDetailsType2ViewHolder.studentNameTextViewCustom.setText(studentLabDetailsType2.getStudentName());
        studentLabDetailsType2ViewHolder.noOfProjectsTextViewCustom.setText(studentLabDetailsType2.getNoOfProjects());
        studentLabDetailsType2ViewHolder.noOfLabTimeTextViewCustom.setText(studentLabDetailsType2.getNoOfLabTime());
        studentLabDetailsType2ViewHolder.noOfDoneTextViewCustom.setText(studentLabDetailsType2.getNoOfDone());
        studentLabDetailsType2ViewHolder.noOfSkippedTextViewCustom.setText(studentLabDetailsType2.getNoOfSkipped());
        studentLabDetailsType2ViewHolder.noOfPendingTextViewCustom.setText(studentLabDetailsType2.getNoOfPending());
        studentLabDetailsType2ViewHolder.studentNameTextViewCustom.setTextColor(labDetailTextColor);
        studentLabDetailsType2ViewHolder.noOfProjectsTextViewCustom.setTextColor(labDetailTextColor);
        studentLabDetailsType2ViewHolder.noOfLabTimeTextViewCustom.setTextColor(labDetailTextColor);
        studentLabDetailsType2ViewHolder.noOfDoneTextViewCustom.setTextColor(labDetailTextColor);
        studentLabDetailsType2ViewHolder.noOfSkippedTextViewCustom.setTextColor(labDetailTextColor);
        studentLabDetailsType2ViewHolder.noOfPendingTextViewCustom.setTextColor(labDetailTextColor);
        studentLabDetailsType2ViewHolder.actionImageView.setImageResource(R.drawable.ic_action_view);
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }
}