package org.wizbots.labtab.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.interfaces.HorizontalProjectCreatorAdapterClickListener;

import java.util.ArrayList;

public class HorizontalProjectCreatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LabTabConstants {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<Object> objectArrayList;
    private Context context;
    private HorizontalProjectCreatorAdapterClickListener horizontalProjectCreatorAdapterClickListener;

    private class HorizontalProjectCreatorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextViewCustom projectCreatorNameTextViewCustom;
        ImageView projectCreatorDeleteImageView;
        HorizontalProjectCreatorAdapterClickListener horizontalProjectCreatorAdapterClickListener;

        HorizontalProjectCreatorViewHolder(View view, HorizontalProjectCreatorAdapterClickListener horizontalProjectCreatorAdapterClickListener) {
            super(view);
            this.horizontalProjectCreatorAdapterClickListener = horizontalProjectCreatorAdapterClickListener;
            projectCreatorDeleteImageView = (ImageView) view.findViewById(R.id.iv_delete_creator);
            projectCreatorNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_project_creator);
            projectCreatorDeleteImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String projectCreator = (String) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.iv_delete_creator:
                    horizontalProjectCreatorAdapterClickListener.onProjectCreatorDeleteClick(projectCreator);
                    break;
            }
        }
    }

    public HorizontalProjectCreatorAdapter(ArrayList<Object> objectArrayList, Context context, HorizontalProjectCreatorAdapterClickListener horizontalProjectCreatorAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.horizontalProjectCreatorAdapterClickListener = horizontalProjectCreatorAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_DATA:
                View dataView = inflater.inflate(R.layout.item_project_creator_view, viewGroup, false);
                viewHolder = new HorizontalProjectCreatorViewHolder(dataView, horizontalProjectCreatorAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_project_creator_view, viewGroup, false);
                viewHolder = new HorizontalProjectCreatorViewHolder(defaultView, horizontalProjectCreatorAdapterClickListener);
                break;
        }
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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_DATA:
                HorizontalProjectCreatorViewHolder horizontalProjectCreatorViewHolder = (HorizontalProjectCreatorViewHolder) viewHolder;
                configureLabListViewHolder(horizontalProjectCreatorViewHolder, position);
                break;
            default:
                HorizontalProjectCreatorViewHolder defaultViewHolder = (HorizontalProjectCreatorViewHolder) viewHolder;
                configureLabListViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureLabListViewHolder(HorizontalProjectCreatorViewHolder horizontalProjectCreatorViewHolder, int position) {
        String projectCreator = (String) objectArrayList.get(position);
        horizontalProjectCreatorViewHolder.projectCreatorNameTextViewCustom.setText(projectCreator);
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

}