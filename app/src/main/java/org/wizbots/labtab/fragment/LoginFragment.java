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

public class LoginFragment extends ParentFragment implements View.OnClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private BaseActivity baseActivityContext;

    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        baseActivityContext = (BaseActivity) context;
        initView();
        initListeners();
        return rootView;
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(getActivity().getResources().getString(R.string.please_enter_your_name_and_password));
        labTabHeaderLayout.getMenuImageView().setVisibility(View.GONE);
    }

    public void initListeners() {
        rootView.findViewById(R.id.btn_login).setOnClickListener(this);
        rootView.findViewById(R.id.tv_forgot_password).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                baseActivityContext.replaceFragment(BaseActivity.FRAGMENT_HOME);
                break;
            case R.id.tv_forgot_password:
                baseActivityContext.replaceFragment(BaseActivity.FRAGMENT_FORGOT_PASSWORD);
                break;
        }
    }
}
