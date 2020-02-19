package org.wizbots.labtab.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wizbots.labtab.R;
import org.wizbots.labtab.adapter.ContactInfoAdapter;
import org.wizbots.labtab.adapter.OtherStuffAdapter;
import org.wizbots.labtab.adapter.WizQuizQueAnsAdapter;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.model.BinderItem;

import java.util.ArrayList;
import java.util.List;

public class PdfBinder extends ParentFragment {

    private static final String TAG = PdfBinder.class.getSimpleName();

    private RecyclerView mRvContactInfo, mRvWizQueAns, mRvOtherStuff;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_binder, container, false);

        mRvContactInfo = (RecyclerView) rootView.findViewById(R.id.rvContactInfo);
        mRvContactInfo.setHasFixedSize(true);
        mRvContactInfo.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRvWizQueAns = (RecyclerView) rootView.findViewById(R.id.rvWizQueAns);
        mRvWizQueAns.setHasFixedSize(true);
        mRvWizQueAns.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRvOtherStuff = (RecyclerView) rootView.findViewById(R.id.rvOtherStuff);
        mRvOtherStuff.setHasFixedSize(true);
        mRvOtherStuff.setLayoutManager(new LinearLayoutManager(getActivity()));

        setContactInfoAdapter();
        setWizQuizQueAnsAdapter();
        setOtherStuffAdapter();

        initView();
        return rootView;
    }

    private void setOtherStuffAdapter() {
        String[] files;
        List<BinderItem> otherStuffListItem = new ArrayList<>();
        files = getResources().getStringArray(R.array.other_stuff_pdf_files);
        if (files != null) {
            for (String filename : files) {
                BinderItem binderItem = new BinderItem(filename);
                otherStuffListItem.add(binderItem);
            }
        }

        OtherStuffAdapter mOtherStuffAdapter = new OtherStuffAdapter(otherStuffListItem, getActivity());
        mRvOtherStuff.setAdapter(mOtherStuffAdapter);
    }

    private void setWizQuizQueAnsAdapter() {
        String[] files;
        List<BinderItem> wizQuizQueAnsListItem = new ArrayList<>();
        files = getResources().getStringArray(R.array.wizquiz_que_ans_pdf_files);
        if (files != null) {
            for (String filename : files) {
                BinderItem binderItem = new BinderItem(filename);
                wizQuizQueAnsListItem.add(binderItem);
            }
        }

        WizQuizQueAnsAdapter mWizQuizQueAnsAdapter = new WizQuizQueAnsAdapter(wizQuizQueAnsListItem, getActivity());
        mRvWizQueAns.setAdapter(mWizQuizQueAnsAdapter);
    }

    private void setContactInfoAdapter() {
        String[] files;
        List<BinderItem> contactInfoListItem = new ArrayList<>();
        files = getResources().getStringArray(R.array.contact_info_pdf_files);
        if (files != null) {
            for (String filename : files) {
                BinderItem binderItem = new BinderItem(filename);
                contactInfoListItem.add(binderItem);
            }
        }

        ContactInfoAdapter mContactInfoAdapter = new ContactInfoAdapter(contactInfoListItem, getActivity());
        mRvContactInfo.setAdapter(mContactInfoAdapter);
    }

    @Override
    public String getFragmentName() {
        return PdfBinder.class.getSimpleName();
    }

    public void initView() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        LabTabHeaderLayout labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(R.string.BINDER);
//        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

    }
}
