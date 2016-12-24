package org.wizbots.labtab.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.wizbots.labtab.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> spinnerData;
    private static LayoutInflater inflater = null;

    public SpinnerAdapter(Context context, List<String> albumList) {
        mContext = context;
        spinnerData = albumList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return spinnerData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {

        private TextView textView;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        try {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.spinner_item, null);

                holder = new ViewHolder();

                holder.textView = (TextView) convertView
                        .findViewById(R.id.tv_spinner);

                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView.setText(spinnerData.get(position));
            holder.textView.setSelected(true);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}