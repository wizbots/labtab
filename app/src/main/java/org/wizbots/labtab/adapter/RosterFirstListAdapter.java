package org.wizbots.labtab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.model.roster.RosterFirstListItem;
import org.wizbots.labtab.model.roster.StudentInOutItem;
import org.wizbots.labtab.util.Animations;

import java.util.ArrayList;

public class RosterFirstListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<RosterFirstListItem> objectArrayList;
    private Context context;
    private SignatureListAdapter signatureListAdapter;
    private ArrayList<StudentInOutItem> studentInOutItemArrayList;

    private static class RosterFirstListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rosterDetailsParentLayout;
        LinearLayout rosterDetailItemRootLayout;
        LinearLayout rosterDetailsExpandLayout;
        TextViewCustom studentName;
        TextViewCustom gender;
        TextViewCustom age;
        TextViewCustom grade;
        TextViewCustom contactName;
        TextViewCustom phone;
        TextViewCustom email;
        TextViewCustom address;
        TextViewCustom allergies;
        RecyclerView rosterSignatureListRecycler;

        public RosterFirstListViewHolder(@NonNull View itemView) {
            super(itemView);
            rosterDetailItemRootLayout = (LinearLayout) itemView.findViewById(R.id.roster_first_list_root_view);
            rosterDetailsParentLayout = (LinearLayout) itemView.findViewById(R.id.roster_first_list_parent_view);
            rosterDetailsExpandLayout = (LinearLayout) itemView.findViewById(R.id.roster_first_list_expand_layout);
            studentName = (TextViewCustom) itemView.findViewById(R.id.tv_student_name_first_list);
            gender = (TextViewCustom) itemView.findViewById(R.id.tv_gender_first_list);
            age = (TextViewCustom) itemView.findViewById(R.id.tv_age_first_list);
            grade = (TextViewCustom) itemView.findViewById(R.id.tv_grade_first_list);
            contactName = (TextViewCustom) itemView.findViewById(R.id.tv_contact_name_first_list);
            phone = (TextViewCustom) itemView.findViewById(R.id.tv_contact_phone_first_list);
            email = (TextViewCustom) itemView.findViewById(R.id.tv_email_first_list);
            address = (TextViewCustom) itemView.findViewById(R.id.tv_address_first_list);
            allergies = (TextViewCustom) itemView.findViewById(R.id.tv_allergies_first_list);
            rosterSignatureListRecycler = (RecyclerView) itemView.findViewById(R.id.roster_signature_list);

        }
    }

    public RosterFirstListAdapter(ArrayList<RosterFirstListItem> objectArrayList, Context context) {
        this.objectArrayList = objectArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RosterFirstListViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View dataView = inflater.inflate(R.layout.item_roster_first_list, parent, false);

        viewHolder = new RosterFirstListViewHolder(dataView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RosterFirstListViewHolder rosterDetailsViewHolder = (RosterFirstListViewHolder) holder;
        configureLabListViewHolder(rosterDetailsViewHolder, position);
    }

    private void configureLabListViewHolder(final RosterFirstListViewHolder rosterDetailsViewHolder, int position) {
        final RosterFirstListItem rosterFirstListItem = objectArrayList.get(position);
        int rosterDetailsLinearLayoutColor;
        if (position % 2 == 0) {
            rosterDetailsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            rosterDetailsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }

        rosterDetailsViewHolder.rosterDetailsParentLayout.setBackgroundColor(rosterDetailsLinearLayoutColor);
        rosterDetailsViewHolder.studentName.setText(rosterFirstListItem.getStudentName());
        rosterDetailsViewHolder.gender.setText(rosterFirstListItem.getGender());
        rosterDetailsViewHolder.age.setText(rosterFirstListItem.getAge());
        rosterDetailsViewHolder.grade.setText(rosterFirstListItem.getGrade());
        rosterDetailsViewHolder.contactName.setText(rosterFirstListItem.getContactName());
        rosterDetailsViewHolder.phone.setText(rosterFirstListItem.getPhone());
        rosterDetailsViewHolder.email.setText(rosterFirstListItem.getEmail());
        rosterDetailsViewHolder.address.setText(rosterFirstListItem.getAddress());
        rosterDetailsViewHolder.allergies.setText(rosterFirstListItem.getAllergies());

        rosterDetailsViewHolder.rosterDetailsParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean show = toggleLayout(!rosterFirstListItem.isExpanded(), rosterDetailsViewHolder.rosterDetailsExpandLayout);
                rosterFirstListItem.setExpanded(show);
//                rosterDetailsViewHolder.rosterDetailsExpandLayout.setVisibility(View.VISIBLE);
            }
        });

        studentInOutItemArrayList = new ArrayList<>();
        studentInOutItemArrayList.add(new StudentInOutItem("08/07/2017", "09-00 AM", "12-00 PM", true, true));
        studentInOutItemArrayList.add(new StudentInOutItem("08/08/2017", "09-00 AM", "12-00 PM", false, false));
        studentInOutItemArrayList.add(new StudentInOutItem("08/09/2017", "09-00 AM", "12-00 PM", false, false));
        studentInOutItemArrayList.add(new StudentInOutItem("08/10/2017", "09-00 AM", "12-00 PM", false, false));
        studentInOutItemArrayList.add(new StudentInOutItem("08/11/2017", "09-00 AM", "12-00 PM", false, false));

        signatureListAdapter = new SignatureListAdapter(studentInOutItemArrayList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rosterDetailsViewHolder.rosterSignatureListRecycler.setLayoutManager(mLayoutManager);
        rosterDetailsViewHolder.rosterSignatureListRecycler.setItemAnimator(new DefaultItemAnimator());
        rosterDetailsViewHolder.rosterSignatureListRecycler.setAdapter(signatureListAdapter);
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

    private boolean toggleLayout(boolean isExpanded, LinearLayout layoutExpand) {
//        Animations.toggleArrow(v, isExpanded);
        if (isExpanded) {
            Animations.expand(layoutExpand);
        } else {
            Animations.collapse(layoutExpand);
        }
        return isExpanded;

    }
}
