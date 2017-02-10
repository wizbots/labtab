package org.wizbots.labtab.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.wizbots.labtab.R;
import org.wizbots.labtab.model.LocationResponse;

import java.util.List;

/**
 * Created by ashish on 9/2/17.
 */

public class LocationAdapter extends BaseAdapter {


    private Context mContext;
    private List<LocationResponse> list;



    public LocationAdapter(Context context, List<LocationResponse> list) {
        mContext = context;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private TextView textView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_spinner);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        LocationResponse location = (LocationResponse) getItem(position);

        holder.textView.setText(location.getName());

        return convertView;
    }
}
