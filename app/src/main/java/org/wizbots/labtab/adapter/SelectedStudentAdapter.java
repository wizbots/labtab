package org.wizbots.labtab.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wizbots.labtab.R;
import org.wizbots.labtab.model.program.Student;

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
