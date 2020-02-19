package org.wizbots.labtab.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.wizbots.labtab.R;
import org.wizbots.labtab.model.Mentor;

import java.util.List;

public class MentorAdapter extends BaseAdapter {
    private Context mContext;
    private List<Mentor> mentorList;

    public MentorAdapter(Context context, List<Mentor> mentorList) {
        mContext = context;
        this.mentorList = mentorList;
    }

    @Override
    public int getCount() {
        return mentorList != null ? mentorList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mentorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_spinner);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Mentor mentor = (Mentor) getItem(position);

        holder.textView.setText(mentor.getFirst_name()+" "+mentor.getLast_name());

        return convertView;
    }
}
