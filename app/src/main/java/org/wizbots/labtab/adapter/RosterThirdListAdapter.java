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
import org.wizbots.labtab.model.roster.RosterThirdListItem;
import java.util.ArrayList;

public class RosterThirdListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<RosterThirdListItem> objectArrayList;
    private Context context;

    private static class RosterThirdListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rosterDetailItemRootLayout;
        TextViewCustom serialNumberText;
        TextViewCustom studentName;
        TextViewCustom pickUp;
        TextViewCustom dropOff;
        TextViewCustom afterCareProvider;
        TextViewCustom afterCarePhone;

        public RosterThirdListViewHolder(@NonNull View itemView) {
            super(itemView);
            rosterDetailItemRootLayout = (LinearLayout) itemView.findViewById(R.id.roster_third_list_item_root_view);
            serialNumberText = (TextViewCustom) itemView.findViewById(R.id.tv_roster_serial_number_header);
            studentName = (TextViewCustom) itemView.findViewById(R.id.tv_student_name);
            pickUp = (TextViewCustom) itemView.findViewById(R.id.tv_pick_up);
            dropOff = (TextViewCustom) itemView.findViewById(R.id.tv_drop_off);
            afterCareProvider = (TextViewCustom) itemView.findViewById(R.id.tv_after_care_provider);
            afterCarePhone = (TextViewCustom) itemView.findViewById(R.id.tv_after_care_phone);
        }
    }

    public RosterThirdListAdapter(ArrayList<RosterThirdListItem> objectArrayList, Context context) {
        this.objectArrayList = objectArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RosterThirdListViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View dataView = inflater.inflate(R.layout.item_roster_third_list, parent, false);

        viewHolder = new RosterThirdListViewHolder(dataView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RosterThirdListViewHolder rosterDetailsViewHolder = (RosterThirdListViewHolder) holder;
        configureLabListViewHolder(rosterDetailsViewHolder, position);
    }

    private void configureLabListViewHolder(RosterThirdListViewHolder rosterDetailsViewHolder, int position) {
        RosterThirdListItem rosterThirdListItem = objectArrayList.get(position);
        int rosterDetailsLinearLayoutColor;
        if (position % 2 == 0) {
            rosterDetailsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            rosterDetailsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }

        rosterDetailsViewHolder.rosterDetailItemRootLayout.setBackgroundColor(rosterDetailsLinearLayoutColor);
        rosterDetailsViewHolder.serialNumberText.setText(String.valueOf(position));
        rosterDetailsViewHolder.studentName.setText(rosterThirdListItem.getStudentName());
        rosterDetailsViewHolder.pickUp.setText(rosterThirdListItem.getPickup());
        rosterDetailsViewHolder.dropOff.setText(rosterThirdListItem.getDropOff());
        rosterDetailsViewHolder.afterCareProvider.setText(rosterThirdListItem.getAfterCareProvider());
        rosterDetailsViewHolder.afterCarePhone.setText(rosterThirdListItem.getAfterCarePhone());
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }
}
