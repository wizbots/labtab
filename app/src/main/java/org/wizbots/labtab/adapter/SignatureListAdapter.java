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
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.model.roster.StudentInOutItem;
import org.wizbots.labtab.util.SignatureDialog;

import java.util.ArrayList;

public class SignatureListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_DATA = 1;
    private ArrayList<StudentInOutItem> objectArrayList;
    private Context context;
    private SignatureDialog signatureDialog;
    int greenColor;
    int blue;

    private static class SignatureListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout signatureItemRootLayout;
        TextViewCustom date;
        TextViewCustom inTime;
        TextViewCustom outTime;
        TextViewCustom signedIn;
        TextViewCustom signedOut;

        SignatureListViewHolder(@NonNull View itemView) {
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

    private void configureLabListViewHolder(final SignatureListViewHolder signatureListViewHolder, int position) {
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

        greenColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.green);
        blue = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_blue);

        if(studentInOutItem.getInSigned()) {
            signatureListViewHolder.signedIn.setText(LabTabConstants.Signature.SIGNED_IN);
            signatureListViewHolder.signedIn.setBackgroundColor(greenColor);
        } else {
            signatureListViewHolder.signedIn.setText(LabTabConstants.Signature.SIGN_IN);
            signatureListViewHolder.signedIn.setBackgroundColor(blue);
        }

        if(studentInOutItem.getOutSigned()) {
            signatureListViewHolder.signedOut.setText(LabTabConstants.Signature.SIGNED_OUT);
            signatureListViewHolder.signedOut.setBackgroundColor(greenColor);
        } else {
            signatureListViewHolder.signedOut.setText(LabTabConstants.Signature.SIGN_OUT);
            signatureListViewHolder.signedOut.setBackgroundColor(blue);
        }

        signatureListViewHolder.signedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signatureListViewHolder.signedIn.getText().toString().equals(LabTabConstants.Signature.SIGN_IN)) {
                    SignatureDialog.ClickListener mClickListener = new SignatureDialog.ClickListener() {
                        @Override
                        public void onConfirmClick() {
                            if(signatureDialog != null) {
                                signatureDialog.dismiss();
                            }
                            signatureListViewHolder.signedIn.setText(LabTabConstants.Signature.SIGNED_IN);
                            signatureListViewHolder.signedIn.setBackgroundColor(greenColor);
                        }

                        @Override
                        public void onCancelClick() {
                            if(signatureDialog != null) {
                                signatureDialog.dismiss();
                            }
                        }
                    };

                    signatureDialog = new SignatureDialog(context, mClickListener);
                    signatureDialog.show();
                }
            }
        });

        signatureListViewHolder.signedOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signatureListViewHolder.signedOut.getText().toString().equals(LabTabConstants.Signature.SIGN_OUT)) {
                    SignatureDialog.ClickListener mClickListener = new SignatureDialog.ClickListener() {
                        @Override
                        public void onConfirmClick() {
                            if(signatureDialog != null) {
                                signatureDialog.dismiss();
                            }
                            signatureListViewHolder.signedOut.setText(LabTabConstants.Signature.SIGNED_OUT);
                            signatureListViewHolder.signedOut.setBackgroundColor(greenColor);
                        }

                        @Override
                        public void onCancelClick() {
                            if(signatureDialog != null) {
                                signatureDialog.dismiss();
                            }
                        }
                    };

                    signatureDialog = new SignatureDialog(context, mClickListener);
                    signatureDialog.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }
}
