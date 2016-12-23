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
import org.wizbots.labtab.interfaces.AdditionalInformationAdapterClickListener;
import org.wizbots.labtab.model.AdditionalInformation;

import java.util.ArrayList;

public class AdditionalInformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<Object> objectArrayList;
    private Context context;
    private AdditionalInformationAdapterClickListener additionalInformationAdapterClickListener;

    private class AdditionalInformationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout listOfAddtionalInformationLinearLayout;
        CheckBox checkBoxAdditionalInformation;
        TextViewCustom studentNameTextViewCustom;
        TextViewCustom selfCheckoutTextViewCustom;
        TextViewCustom pickupInstructionsTextViewCustom;
        TextViewCustom inOutTimesTextViewCustom;
        TextViewCustom notesTextViewCustom;
        AdditionalInformationAdapterClickListener additionalInformationAdapterClickListener;

        AdditionalInformationViewHolder(View view, final AdditionalInformationAdapterClickListener additionalInformationAdapterClickListener) {
            super(view);
            this.additionalInformationAdapterClickListener = additionalInformationAdapterClickListener;
            listOfAddtionalInformationLinearLayout = (LinearLayout) view.findViewById(R.id.list_of_additional_information_root_layout);
            checkBoxAdditionalInformation = (CheckBox) view.findViewById(R.id.cb_additional_information);
            studentNameTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_student_name);
            selfCheckoutTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_self_checkout);
            pickupInstructionsTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_pickup_instructions);
            inOutTimesTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_in_out_times);
            notesTextViewCustom = (TextViewCustom) view.findViewById(R.id.tv_notes);
            listOfAddtionalInformationLinearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            LabDetails labDetails = (LabDetails) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.list_of_additional_information_root_layout:
                    additionalInformationAdapterClickListener.onActionViewClick();
                    break;
            }
        }
    }

    public AdditionalInformationAdapter(ArrayList<Object> objectArrayList, Context context, AdditionalInformationAdapterClickListener additionalInformationAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.additionalInformationAdapterClickListener = additionalInformationAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_DATA:
                View dataView = inflater.inflate(R.layout.item_additional_information, viewGroup, false);
                viewHolder = new AdditionalInformationViewHolder(dataView, additionalInformationAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_additional_information, viewGroup, false);
                viewHolder = new AdditionalInformationViewHolder(defaultView, additionalInformationAdapterClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof AdditionalInformation) {
            return VIEW_ITEM_DATA;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_DATA:
                AdditionalInformationViewHolder additionalInformationViewHolder = (AdditionalInformationViewHolder) viewHolder;
                configureLabListViewHolder(additionalInformationViewHolder, position);
                break;
            default:
                AdditionalInformationViewHolder defaultViewHolder = (AdditionalInformationViewHolder) viewHolder;
                configureLabListViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureLabListViewHolder(AdditionalInformationViewHolder additionalInformationViewHolder, int position) {
        AdditionalInformation listOfSkips = (AdditionalInformation) objectArrayList.get(position);
        int additionalInformationLinearLayoutColor;
        if (position % 2 == 0) {
            additionalInformationLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            additionalInformationLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }
        additionalInformationViewHolder.listOfAddtionalInformationLinearLayout.setBackgroundColor(additionalInformationLinearLayoutColor);
        additionalInformationViewHolder.checkBoxAdditionalInformation.setChecked(listOfSkips.isCheck());
        additionalInformationViewHolder.studentNameTextViewCustom.setText(listOfSkips.getStudentName());
        additionalInformationViewHolder.selfCheckoutTextViewCustom.setText(listOfSkips.getSelfCheckout());
        additionalInformationViewHolder.pickupInstructionsTextViewCustom.setText(listOfSkips.getPickupInstructions());
        additionalInformationViewHolder.inOutTimesTextViewCustom.setText(listOfSkips.getIn_out_times());
        additionalInformationViewHolder.notesTextViewCustom.setText(listOfSkips.getNotes());
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

}