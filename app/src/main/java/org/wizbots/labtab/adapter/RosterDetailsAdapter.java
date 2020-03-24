package org.wizbots.labtab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.model.roster.StudentListItem;

import java.util.ArrayList;

public class RosterDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<StudentListItem> objectArrayList;
    private Context context;

    private static class RosterDetailsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rosterDetailItemRootLayout;
        TextViewCustom serialNumberText;
        TextViewCustom studentMorning;
        TextViewCustom studentLunch;
        TextViewCustom studentAfternoon;
        TextViewCustom studentLatePickup;

        public RosterDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            rosterDetailItemRootLayout = (LinearLayout) itemView.findViewById(R.id.roster_details_root_layout);
            serialNumberText = (TextViewCustom) itemView.findViewById(R.id.tv_roster_serial_number);
            studentMorning = (TextViewCustom) itemView.findViewById(R.id.tv_morning);
            studentLunch = (TextViewCustom) itemView.findViewById(R.id.tv_lunch);
            studentAfternoon = (TextViewCustom) itemView.findViewById(R.id.tv_afternoon);
            studentLatePickup = (TextViewCustom) itemView.findViewById(R.id.tv_late_pickup);

        }
    }

    public RosterDetailsAdapter(ArrayList<StudentListItem> objectArrayList, Context context) {
        this.objectArrayList = objectArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RosterDetailsViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View dataView = inflater.inflate(R.layout.item_roster_detail, parent, false);

        viewHolder = new RosterDetailsViewHolder(dataView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RosterDetailsViewHolder rosterDetailsViewHolder = (RosterDetailsViewHolder) holder;
        configureLabListViewHolder(rosterDetailsViewHolder, position);
    }

    private void configureLabListViewHolder(RosterDetailsViewHolder rosterDetailsViewHolder, int position) {
        StudentListItem studentListItem = objectArrayList.get(position);
        int rosterDetailsLinearLayoutColor;
        if (position % 2 == 0) {
            rosterDetailsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            rosterDetailsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }

        rosterDetailsViewHolder.rosterDetailItemRootLayout.setBackgroundColor(rosterDetailsLinearLayoutColor);
        rosterDetailsViewHolder.serialNumberText.setText(String.valueOf(position));
        rosterDetailsViewHolder.studentMorning.setText(studentListItem.getMorning());
        rosterDetailsViewHolder.studentLunch.setText(studentListItem.getLunch());
        rosterDetailsViewHolder.studentAfternoon.setText(studentListItem.getAfternoon());
        rosterDetailsViewHolder.studentLatePickup.setText(studentListItem.getLatePickUp());
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }
}
