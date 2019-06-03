package org.wizbots.labtab.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants.Screens;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.WebViewActivity;
import org.wizbots.labtab.model.BinderItem;

import java.util.List;

public class BinderAdapter extends RecyclerView.Adapter<BinderAdapter.ViewHolder> {

    private List<BinderItem> listItems;
    private Context context;

    public BinderAdapter(List<BinderItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.binder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final BinderItem item = listItems.get(position);

        holder.tvName.setText(item.getName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent;
                intent = new Intent(context, WebViewActivity.class);
                String path = item.getName().toString();
                intent.putExtra("path",path);
                intent.putExtra(Screens.FROM_SCREEN, Screens.SCREEN_NONE);
                context.startActivity(intent);
            }
        });

        int labListLinearLayoutColor;
        if (position % 2 == 0) {
            labListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.white);
        } else {
            labListLinearLayoutColor = ContextCompat.getColor(LabTabApplication.getInstance(), R.color.light_gray);
        }
        holder.linearLayout.setBackgroundColor(labListLinearLayoutColor);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.pdf_name);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.binder_layout);
            context = itemView.getContext();

        }
    }
}
