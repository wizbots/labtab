package org.wizbots.labtab.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.wizbots.labtab.R;
import org.wizbots.labtab.model.LeftDrawerItem;

public class LeftDrawerAdapter extends ArrayAdapter<LeftDrawerItem> {

    Context mContext;
    int layoutResourceId;
    LeftDrawerItem data[] = null;

    public LeftDrawerAdapter(Context mContext, int layoutResourceId, LeftDrawerItem[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.drawer_image);
        TextView textViewName = (TextView) listItem.findViewById(R.id.drawer_text);

        LeftDrawerItem folder = data[position];


        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        return listItem;
    }
}