package org.wizbots.labtab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.interfaces.RosterListAdapterClickListener;

import java.util.ArrayList;

public class RosterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<Object> objectArrayList;
    private Context context;
    private RosterListAdapterClickListener rosterListAdapterClickListener;

    private class RosterListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout labListLinearLayout;
        ImageView labLevelImageView;
        TextViewCustom labNameTextViewCustom;
        ImageView actionImageView,rosterDetailsImageView;
        RosterListAdapterClickListener rosterListAdapterClickListener;

        RosterListViewHolder(View view, RosterListAdapterClickListener rosterListAdapterClickListener) {
            super(view);
            this.rosterListAdapterClickListener = rosterListAdapterClickListener;
            labListLinearLayout = (LinearLayout) view.findViewById(R.id.lab_list_root_layout);
            labLevelImageView = (ImageView) view.findViewById(R.id.iv_lab_level);
            labNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_lab_name);
            actionImageView = (ImageView) view.findViewById(R.id.iv_action);
            actionImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_action:
                    rosterListAdapterClickListener.onActionViewClick();
                    break;
            }
        }
    }

    public RosterListAdapter(ArrayList<Object> objectArrayList, Context context, RosterListAdapterClickListener rosterListAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.rosterListAdapterClickListener = rosterListAdapterClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View defaultView = inflater.inflate(R.layout.item_roster_list, parent, false);
        viewHolder = new RosterListViewHolder(defaultView, rosterListAdapterClickListener);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof String) {
            return VIEW_ITEM_DATA;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RosterListViewHolder defaultViewHolder = (RosterListViewHolder) holder;
        configureLabListViewHolder(defaultViewHolder, position);
    }

    private void configureLabListViewHolder(RosterListViewHolder defaultViewHolder, int position) {
        String title = (String) objectArrayList.get(position);
        int labListLinearLayoutColor;
        if (position % 2 == 0) {
            labListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            labListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }
        defaultViewHolder.labListLinearLayout.setBackgroundColor(labListLinearLayoutColor);
        defaultViewHolder.labNameTextViewCustom.setText(title);
        defaultViewHolder.actionImageView.setImageResource(R.drawable.ic_action_view);
        defaultViewHolder.labLevelImageView.setImageResource(R.drawable.ic_level_novice);
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();

    }
}
