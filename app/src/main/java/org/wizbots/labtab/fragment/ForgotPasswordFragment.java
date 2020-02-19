package org.wizbots.labtab.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.customview.LabTabHeaderLayout;

public class ForgotPasswordFragment extends ParentFragment implements View.OnClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private HomeActivity homeActivityContext;

    public ForgotPasswordFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        homeActivityContext = (HomeActivity) context;
        initView();
        initListeners();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return ForgotPasswordFragment.class.getSimpleName();
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout = (LabTabHeaderLayout) getActivity().findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.FORGOT_PASSWORD);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.GONE);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
    }

    public void initListeners() {
        rootView.findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                homeActivityContext.replaceFragment(Fragments.LOGIN, new Bundle());
                break;
        }
    }
}
