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
import org.wizbots.labtab.model.roster.StudentInOutItem;
import java.util.ArrayList;

public class SignatureListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<StudentInOutItem> objectArrayList;
    private Context context;

    private static class SignatureListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout signatureItemRootLayout;
        TextViewCustom date;
        TextViewCustom inTime;
        TextViewCustom outTime;
        TextViewCustom signedIn;
        TextViewCustom signedOut;

        public SignatureListViewHolder(@NonNull View itemView) {
            super(itemView);
            signatureItemRootLayout = (LinearLayout) itemView.findViewById(R.id.signature_list_item_root_layout);
            date = (TextViewCustom) itemView.findViewById(R.id.tv_date_signature);
            inTime = (TextViewCustom) itemView.findViewById(R.id.tv_in_signature);
            outTime = (TextViewCustom) itemView.findViewById(R.id.tv_out_signature);
            signedIn = (TextViewCustom) itemView.findViewById(R.id.tv_signed_in);
            signedOut = (TextViewCustom) itemView.findViewById(R.id.tv_signed_out);
        }
    }

    public SignatureListAdapter(ArrayList<StudentInOutItem> objectArrayList, Context context) {
        this.objectArrayList = objectArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SignatureListViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View dataView = inflater.inflate(R.layout.item_signature_list, parent, false);

        viewHolder = new SignatureListViewHolder(dataView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SignatureListViewHolder signatureListViewHolder = (SignatureListViewHolder) holder;
        configureLabListViewHolder(signatureListViewHolder, position);
    }

    private void configureLabListViewHolder(SignatureListViewHolder signatureListViewHolder, int position) {
        StudentInOutItem studentInOutItem = objectArrayList.get(position);
        int rosterDetailsLinearLayoutColor;
        if (position % 2 == 0) {
            rosterDetailsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            rosterDetailsLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }

        signatureListViewHolder.signatureItemRootLayout.setBackgroundColor(rosterDetailsLinearLayoutColor);
        signatureListViewHolder.date.setText(studentInOutItem.getDate());
        signatureListViewHolder.inTime.setText(studentInOutItem.getInTime());
        signatureListViewHolder.outTime.setText(studentInOutItem.getOutTime());

        int greenColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.green);
        int blue = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_blue);

        if(studentInOutItem.getInSigned()) {
            signatureListViewHolder.signedIn.setText("SIGNED-IN");
            signatureListViewHolder.signedIn.setBackgroundColor(greenColor);
        } else {
            signatureListViewHolder.signedIn.setText("SIGN-IN");
            signatureListViewHolder.signedIn.setBackgroundColor(blue);
        }

        if(studentInOutItem.getOutSigned()) {
            signatureListViewHolder.signedOut.setText("SIGNED-OUT");
            signatureListViewHolder.signedOut.setBackgroundColor(greenColor);
        } else {
            signatureListViewHolder.signedOut.setText("SIGN-OUT");
            signatureListViewHolder.signedOut.setBackgroundColor(blue);
        }
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }
}
