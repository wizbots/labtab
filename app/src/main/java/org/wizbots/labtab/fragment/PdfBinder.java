package org.wizbots.labtab.fragment;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wizbots.labtab.R;
import org.wizbots.labtab.adapter.BinderAdapter;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.model.BinderItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfBinder extends ParentFragment {

    private static final String TAG = PdfBinder.class.getSimpleName();

    private RecyclerView binderRecyclerView;
    private RecyclerView.Adapter binderAdapter;
    List<BinderItem> listItem;
    String[] assetManager;
    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_binder, container, false);
        binderRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_pdf_list);
        binderRecyclerView.setHasFixedSize(true);
        binderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        String[] files;
        listItem = new ArrayList<>();
        files = getResources().getStringArray(R.array.pdf_files);
        if (files!=null) {
            for (String filename : files) {
                BinderItem binderItem = new BinderItem(filename);
                listItem.add(binderItem);
            }
        }

        binderAdapter = new BinderAdapter(listItem, getActivity());
        binderRecyclerView.setAdapter(binderAdapter);
        initView();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return PdfBinder.class.getSimpleName();
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(R.string.BINDER);
//        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);

    }
}
