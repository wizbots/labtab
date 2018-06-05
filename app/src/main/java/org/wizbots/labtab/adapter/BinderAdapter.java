package org.wizbots.labtab.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wizbots.labtab.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.binder_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BinderItem item = listItems.get(position);

        holder.tvName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView)itemView.findViewById(R.id.pdf_name);
        }
    }
}
