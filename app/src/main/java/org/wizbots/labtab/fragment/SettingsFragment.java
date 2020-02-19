package org.wizbots.labtab.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wizbots.labtab.BuildConfig;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.LabTabHeaderLayout;

public class SettingsFragment extends ParentFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
        ((TextView)view.findViewById(R.id.txt_version)).setText(BuildConfig.VERSION_NAME);
    }

    public void initView() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        LabTabHeaderLayout labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(R.string.settings);

    }

    @Override
    public String getFragmentName() {
        return SettingsFragment.class.getSimpleName();
    }
}
