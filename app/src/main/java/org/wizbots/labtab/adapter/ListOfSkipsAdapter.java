package org.wizbots.labtab.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.interfaces.ListOfSkipsAdapterClickListener;
import org.wizbots.labtab.model.ListOfSkips;

import java.util.ArrayList;

public class ListOfSkipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<Object> objectArrayList;
    private Context context;
    private ListOfSkipsAdapterClickListener listOfSkipsAdapterClickListener;

    private class ListOfSkipsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout listOfSkipsLinearLayout;
        CheckBox checkBoxListOfSkips;
        TextViewCustom studentNameTextViewCustom;
        TextViewCustom noOfHoursTextViewCustom;
        TextViewCustom noOfGeneralTextViewCustom;
        TextViewCustom notesTextViewCustom;
        ListOfSkipsAdapterClickListener listOfSkipsAdapterClickListener;

        ListOfSkipsViewHolder(View view, final ListOfSkipsAdapterClickListener listOfSkipsAdapterClickListener) {
            super(view);
            this.listOfSkipsAdapterClickListener = listOfSkipsAdapterClickListener;
            listOfSkipsLinearLayout = (LinearLayout) view.findViewById(R.id.list_of_skips_root_layout);
            checkBoxListOfSkips = (CheckBox) view.findViewById(R.id.cb_list_of_skips);
            studentNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_student_name);
            noOfHoursTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_hours);
            noOfGeneralTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_no_of_general);
            notesTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_notes);
            listOfSkipsLinearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            LabDetails labDetails = (LabDetails) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.list_of_skips_root_layout:
                    ;
                    listOfSkipsAdapterClickListener.onActionViewClick();
                    break;
            }
        }
    }

    public ListOfSkipsAdapter(ArrayList<Object> objectArrayList, Context context, ListOfSkipsAdapterClickListener listOfSkipsAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.listOfSkipsAdapterClickListener = listOfSkipsAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_DATA:
                View dataView = inflater.inflate(R.layout.item_list_of_skips_list, viewGroup, false);
                viewHolder = new ListOfSkipsViewHolder(dataView, listOfSkipsAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_list_of_skips_list, viewGroup, false);
                viewHolder = new ListOfSkipsViewHolder(defaultView, listOfSkipsAdapterClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof ListOfSkips) {
            return VIEW_ITEM_DATA;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_DATA:
                ListOfSkipsViewHolder listOfSkipsViewHolder = (ListOfSkipsViewHolder) viewHolder;
                configureLabListViewHolder(listOfSkipsViewHolder, position);
                break;
            default:
                ListOfSkipsViewHolder defaultViewHolder = (ListOfSkipsViewHolder) viewHolder;
                configureLabListViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureLabListViewHolder(ListOfSkipsViewHolder listOfSkipsViewHolder, int position) {
        ListOfSkips listOfSkips = (ListOfSkips) objectArrayList.get(position);
        int listOfSkipsLinearLayoutColor;
        if (position % 2 == 0) {
            listOfSkipsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            listOfSkipsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }
        listOfSkipsViewHolder.listOfSkipsLinearLayout.setBackgroundColor(listOfSkipsLinearLayoutColor);
        listOfSkipsViewHolder.checkBoxListOfSkips.setChecked(listOfSkips.isCheck());
        listOfSkipsViewHolder.studentNameTextViewCustom.setText(listOfSkips.getStudentName());
        listOfSkipsViewHolder.noOfHoursTextViewCustom.setText(listOfSkips.getNo_of_hours());
        listOfSkipsViewHolder.noOfGeneralTextViewCustom.setText(listOfSkips.getNo_of_generals());
        listOfSkipsViewHolder.notesTextViewCustom.setText(listOfSkips.getNotes());
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

}