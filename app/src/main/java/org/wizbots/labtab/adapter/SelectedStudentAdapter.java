package org.wizbots.labtab.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.interfaces.LabListAdapterClickListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

/**
 * Created by aMAN GUPTA on 07/10/17.
 */

public class SelectedStudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Student> dateSet;
    private Context context;

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        ViewHolder(View view) {
            super(view);
            studentName = (TextView) view.findViewById(R.id.tv_student_name);
        }
    }

    public SelectedStudentAdapter(ArrayList<Student> dateSet, Context context) {
        this.dateSet = dateSet;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View dataView = inflater.inflate(R.layout.item_selected_student, viewGroup, false);
        viewHolder = new SelectedStudentAdapter.ViewHolder(dataView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((ViewHolder)viewHolder).studentName.setText(dateSet.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return dateSet.size();
    }

}
