package org.wizbots.labtab.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.model.program.Student;

import java.util.ArrayList;

public class ProjectCreatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LabTabConstants {
    private ArrayList<Student> studentArrayList;
    private ArrayList<Student> selectedStudentArrayList;

    public ProjectCreatorAdapter(ArrayList<Student> studentArrayList, ArrayList<Student> selectedStudentArrayList) {
        this.studentArrayList = studentArrayList;
        this.selectedStudentArrayList = selectedStudentArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View dataView = inflater.inflate(R.layout.project_creator_list_child, parent, false);
        viewHolder = new ProjectCreatorViewHolder(dataView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Student student = studentArrayList.get(position);
        ProjectCreatorViewHolder projectCreatorViewHolder = (ProjectCreatorViewHolder) holder;
        projectCreatorViewHolder.text.setText(student.getName());
        projectCreatorViewHolder.checkBox.setChecked(isSelectedStudent(student));
        projectCreatorViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    selectedStudentArrayList.add(student);
                } else {
                    removeFromList(student);
                    selectedStudentArrayList.remove(student);
                }
            }
        });
    }

    private void removeFromList(Student student) {
        Student selectedStudent = null;
        for (Student s : selectedStudentArrayList) {
            if (student.getStudent_id().equals(s.getStudent_id())) {
                selectedStudent = s;
                break;
            }
        }
        if (selectedStudent != null)
            selectedStudentArrayList.remove(selectedStudent);
    }

    private boolean isSelectedStudent(Student student) {
        for (Student s :
                selectedStudentArrayList) {
            if (s.getStudent_id().equals(student.getStudent_id())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }

    private class ProjectCreatorViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        CheckBox checkBox;

        public ProjectCreatorViewHolder(View dataView) {
            super(dataView);
            text = (TextView) dataView.findViewById(R.id.lblListHeader);
            checkBox = (CheckBox) dataView.findViewById(R.id.lbbcheckbox);
        }
    }

    /*private final int VIEW_ITEM_DATA = 1;
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
    }*/

}