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
import org.wizbots.labtab.interfaces.StudentStatsDetailsAdapterClickListener;
import org.wizbots.labtab.model.LabListHeader;
import org.wizbots.labtab.model.StudentStatisticsDetail;
import org.wizbots.labtab.model.StudentStatisticsDetailProjects;
import org.wizbots.labtab.model.StudentStatisticsDetailsListHeader;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class StudentStatsDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LabTabConstants {

    private final int VIEW_ITEM_HEADER = 0;
    private final int VIEW_ITEM_DATA_TYPE_1 = 1;
    private final int VIEW_ITEM_DATA_TYPE_2 = 2;
    private ArrayList<Object> objectArrayList;
    private Context context;
    private StudentStatsDetailsAdapterClickListener studentStatsDetailsAdapterClickListener;

    private class StudentStatsDetailsType1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout studentStatsDetailsType1LinearLayout;
        ImageView labLevelImageView;
        TextViewCustom studentNameTextViewCustom;
        TextViewCustom noOfProjectsTextViewCustom;
        TextViewCustom noOfLabTimeTextViewCustom;
        TextViewCustom noOfDoneTextViewCustom;
        TextViewCustom noOfSkippedTextViewCustom;
        TextViewCustom noOfPendingTextViewCustom;

        StudentStatsDetailsAdapterClickListener studentStatsDetailsAdapterClickListener;

        StudentStatsDetailsType1ViewHolder(View view, StudentStatsDetailsAdapterClickListener studentStatsDetailsAdapterClickListener) {
            super(view);
            this.studentStatsDetailsAdapterClickListener = studentStatsDetailsAdapterClickListener;
            studentStatsDetailsType1LinearLayout = (LinearLayout) view.findViewById(R.id.student_stats_details_root_layout_type_1);
            labLevelImageView = (ImageView) view.findViewById(R.id.iv_lab_level);
            studentNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_student_name);
            noOfProjectsTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_projects);
            noOfLabTimeTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_lab_time);
            noOfDoneTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_done);
            noOfSkippedTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_skipped);
            noOfPendingTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_pending);
            studentStatsDetailsType1LinearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            StudentStatisticsDetail studentStatisticsDetail = (StudentStatisticsDetail) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.student_stats_details_root_layout_type_1:
                    studentStatsDetailsAdapterClickListener.onViewTypeClick1();
                    break;
            }
        }
    }

    private class StudentStatsDetailsType2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout studentStatsDetailsType2LinearLayout;
        ImageView labStepImageView;
        TextViewCustom projectCategoryTextViewCustom;
        ImageView projectStatusImageView;

        StudentStatsDetailsAdapterClickListener studentStatsDetailsAdapterClickListener;

        StudentStatsDetailsType2ViewHolder(View view, StudentStatsDetailsAdapterClickListener studentStatsDetailsAdapterClickListener) {
            super(view);
            this.studentStatsDetailsAdapterClickListener = studentStatsDetailsAdapterClickListener;
            studentStatsDetailsType2LinearLayout = (LinearLayout) view.findViewById(R.id.student_stats_details_root_layout_type_2);
            labStepImageView = (ImageView) view.findViewById(R.id.iv_lab_step);
            projectCategoryTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_project_category);
            projectStatusImageView = (ImageView) view.findViewById(R.id.iv_project_status);
            studentStatsDetailsType2LinearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            StudentStatisticsDetailProjects studentStatisticsDetailProjects = (StudentStatisticsDetailProjects) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.student_stats_details_root_layout_type_2:
                    studentStatsDetailsAdapterClickListener.onViewTypeClick2();
                    break;
            }
        }
    }

    private class StudentStatsDetailsHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout studentStatsDetailsHeaderLinearLayout;

        StudentStatsDetailsAdapterClickListener studentStatsDetailsAdapterClickListener;

        StudentStatsDetailsHeaderHolder(View view, StudentStatsDetailsAdapterClickListener studentStatsDetailsAdapterClickListener) {
            super(view);
            this.studentStatsDetailsAdapterClickListener = studentStatsDetailsAdapterClickListener;
            studentStatsDetailsHeaderLinearLayout = (LinearLayout) view.findViewById(R.id.student_stats_details_root_layout);
            studentStatsDetailsHeaderLinearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            StudentStatisticsDetailsListHeader studentStatisticsDetailsListHeader = (StudentStatisticsDetailsListHeader) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.student_stats_details_root_layout:
                    studentStatsDetailsAdapterClickListener.onViewTypeClick1();
                    break;
            }
        }
    }

    public StudentStatsDetailsAdapter(ArrayList<Object> objectArrayList, Context context, StudentStatsDetailsAdapterClickListener studentStatsDetailsAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.studentStatsDetailsAdapterClickListener = studentStatsDetailsAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_HEADER:
                View headerView = inflater.inflate(R.layout.item_student_stats, viewGroup, false);
                viewHolder = new StudentStatsDetailsHeaderHolder(headerView, studentStatsDetailsAdapterClickListener);
                break;
            case VIEW_ITEM_DATA_TYPE_1:
                View dataView1 = inflater.inflate(R.layout.item_student_stats_details_type_1, viewGroup, false);
                viewHolder = new StudentStatsDetailsType1ViewHolder(dataView1, studentStatsDetailsAdapterClickListener);
                break;
            case VIEW_ITEM_DATA_TYPE_2:
                View dataView2 = inflater.inflate(R.layout.item_student_stats_details_type_2, viewGroup, false);
                viewHolder = new StudentStatsDetailsType2ViewHolder(dataView2, studentStatsDetailsAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_student_stats, viewGroup, false);
                viewHolder = new StudentStatsDetailsHeaderHolder(defaultView, studentStatsDetailsAdapterClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof StudentStatisticsDetail) {
            return VIEW_ITEM_DATA_TYPE_1;
        } else if (objectArrayList.get(position) instanceof StudentStatisticsDetailProjects) {
            return VIEW_ITEM_DATA_TYPE_2;
        } else if (objectArrayList.get(position) instanceof StudentStatsDetailsHeaderHolder) {
            return VIEW_ITEM_HEADER;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_HEADER:
                StudentStatsDetailsHeaderHolder studentStatsDetailsHeaderHolder = (StudentStatsDetailsHeaderHolder) viewHolder;
                configureStudentStatsDetailHeaderHolder(studentStatsDetailsHeaderHolder, position);
                break;
            case VIEW_ITEM_DATA_TYPE_1:
                StudentStatsDetailsType1ViewHolder studentStatsDetailsType1ViewHolder = (StudentStatsDetailsType1ViewHolder) viewHolder;
                configureStudentStatsDetailListType1ViewHolder(studentStatsDetailsType1ViewHolder, position);
                break;
            case VIEW_ITEM_DATA_TYPE_2:
                StudentStatsDetailsType2ViewHolder studentStatsDetailsType2ViewHolder = (StudentStatsDetailsType2ViewHolder) viewHolder;
                configureStudentStatsDetailListType2ViewHolder(studentStatsDetailsType2ViewHolder, position);
                break;
            default:
                StudentStatsDetailsHeaderHolder defaultHeaderHolder = (StudentStatsDetailsHeaderHolder) viewHolder;
                configureStudentStatsDetailHeaderHolder(defaultHeaderHolder, position);
                break;
        }
    }

    private void configureStudentStatsDetailListType1ViewHolder(StudentStatsDetailsType1ViewHolder studentStatsDetailsType1ViewHolder, int position) {
        StudentStatisticsDetail studentStatisticsDetail = (StudentStatisticsDetail) objectArrayList.get(position);
//        int studentsStatsDetailListLinearLayoutColor;
//        if (position % 2 == 0) {
//            studentsStatsDetailListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
//        } else {
//            studentsStatsDetailListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
//        }
//        studentStatsDetailsType1ViewHolder.studentStatsDetailsType1LinearLayout.setBackgroundColor(studentsStatsDetailListLinearLayoutColor);
        LabTabUtil.setLabLevelImageResource(studentStatisticsDetail.getLabLevel(), studentStatsDetailsType1ViewHolder.labLevelImageView);
        studentStatsDetailsType1ViewHolder.studentNameTextViewCustom.setText(studentStatisticsDetail.getStudentName());
        studentStatsDetailsType1ViewHolder.noOfProjectsTextViewCustom.setText(studentStatisticsDetail.getNoOfProjects());
        studentStatsDetailsType1ViewHolder.noOfLabTimeTextViewCustom.setText(studentStatisticsDetail.getNoOfLabTime());
        studentStatsDetailsType1ViewHolder.noOfDoneTextViewCustom.setText(studentStatisticsDetail.getNoOfDone());
        studentStatsDetailsType1ViewHolder.noOfSkippedTextViewCustom.setText(studentStatisticsDetail.getNoOfSkipped());
        studentStatsDetailsType1ViewHolder.noOfPendingTextViewCustom.setText(studentStatisticsDetail.getNoOfPending());
    }

    private void configureStudentStatsDetailListType2ViewHolder(StudentStatsDetailsType2ViewHolder studentStatsDetailsType2ViewHolder, int position) {
        StudentStatisticsDetailProjects studentStatisticsDetailProjects = (StudentStatisticsDetailProjects) objectArrayList.get(position);
        int studentsStatsDetailListLinearLayoutColor;
        int projectCategoryColor;
        if (position % 2 == 0) {
            studentsStatsDetailListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_blue);
            projectCategoryColor = ContextCompat.getColor(LabTabApplication.getInstance(), android.R.color.white);
        } else {
            studentsStatsDetailListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
            projectCategoryColor = ContextCompat.getColor(LabTabApplication.getInstance(), android.R.color.black);
        }
        studentStatsDetailsType2ViewHolder.studentStatsDetailsType2LinearLayout.setBackgroundColor(studentsStatsDetailListLinearLayoutColor);
        LabTabUtil.setLabStepImageResource(studentStatisticsDetailProjects.getLabStep(), studentStatsDetailsType2ViewHolder.labStepImageView);
        studentStatsDetailsType2ViewHolder.projectCategoryTextViewCustom.setText(studentStatisticsDetailProjects.getProjectCategory());
        studentStatsDetailsType2ViewHolder.projectCategoryTextViewCustom.setTextColor(projectCategoryColor);
        LabTabUtil.setProjectStatusImageResource(studentStatisticsDetailProjects.getProjectStatus(), studentStatsDetailsType2ViewHolder.projectStatusImageView);
    }

    private void configureStudentStatsDetailHeaderHolder(StudentStatsDetailsHeaderHolder studentStatsDetailsHeaderHolder, int position) {
        LabListHeader labListHeader = (LabListHeader) objectArrayList.get(position);
        int labListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }
}