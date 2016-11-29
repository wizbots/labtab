package org.wizbots.labtab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.BaseActivity;
import org.wizbots.labtab.customview.LabTabHeaderLayout;

public class HomeFragment extends ParentFragment implements View.OnClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private BaseActivity baseActivityContext;

    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        baseActivityContext = (BaseActivity) context;
        initView();
        initListeners();
        return rootView;
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(getActivity().getResources().getString(R.string.welcome_mentor_name));
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.menu);
        labTabHeaderLayout.getMenuImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseActivityContext.replaceFragment(BaseActivity.FRAGMENT_LOGIN);
            }
        });
    }

    public void initListeners() {
        rootView.findViewById(R.id.cv_lab_list).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_lab_list:
                baseActivityContext.replaceFragment(BaseActivity.FRAGMENT_LAB_LIST);
                break;
        }
    }
}
