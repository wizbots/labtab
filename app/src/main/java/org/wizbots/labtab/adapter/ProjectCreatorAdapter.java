package org.wizbots.labtab.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.interfaces.ProjectCreatorAdapterClickListener;
import org.wizbots.labtab.model.program.Student;

import java.util.ArrayList;

public class ProjectCreatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LabTabConstants {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<Student> studentArrayList;
    private Context context;
    private ProjectCreatorAdapterClickListener projectCreatorAdapterClickListener;

    private class ProjectCreatorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout labListLinearLayout;
        TextViewCustom projectCreatorNameTextViewCustom;
        ProjectCreatorAdapterClickListener projectCreatorAdapterClickListener;

        ProjectCreatorViewHolder(View view, ProjectCreatorAdapterClickListener projectCreatorAdapterClickListener) {
            super(view);
            this.projectCreatorAdapterClickListener = projectCreatorAdapterClickListener;
            labListLinearLayout = (LinearLayout) view.findViewById(R.id.project_creator_root_layout);
            projectCreatorNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_project_creator);
            projectCreatorNameTextViewCustom.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Student projectCreator = (Student) studentArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.tv_project_creator:
                    projectCreatorAdapterClickListener.onProjectCreatorClick(projectCreator);
                    break;
            }
        }
    }

    public ProjectCreatorAdapter(ArrayList<Student> studentArrayList, Context context, ProjectCreatorAdapterClickListener projectCreatorAdapterClickListener) {
        this.studentArrayList = studentArrayList;
        this.context = context;
        this.projectCreatorAdapterClickListener = projectCreatorAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_DATA:
                View dataView = inflater.inflate(R.layout.item_project_creator, viewGroup, false);
                viewHolder = new ProjectCreatorViewHolder(dataView, projectCreatorAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_project_creator, viewGroup, false);
                viewHolder = new ProjectCreatorViewHolder(defaultView, projectCreatorAdapterClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (studentArrayList.get(position) != null) {
            return VIEW_ITEM_DATA;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_DATA:
                ProjectCreatorViewHolder projectCreatorViewHolder = (ProjectCreatorViewHolder) viewHolder;
                configureLabListViewHolder(projectCreatorViewHolder, position);
                break;
            default:
                ProjectCreatorViewHolder defaultViewHolder = (ProjectCreatorViewHolder) viewHolder;
                configureLabListViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureLabListViewHolder(ProjectCreatorViewHolder projectCreatorViewHolder, int position) {
        Student projectCreator = studentArrayList.get(position);
        int projectCreatorLinearLayoutColor;
        if (position % 2 == 0) {
            projectCreatorLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            projectCreatorLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }
        projectCreatorViewHolder.labListLinearLayout.setBackgroundColor(projectCreatorLinearLayoutColor);
        projectCreatorViewHolder.projectCreatorNameTextViewCustom.setText(projectCreator.getName());
    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }

}