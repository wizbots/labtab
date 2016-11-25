package org.wizbots.labtab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.wizbots.labtab.R;
import org.wizbots.labtab.adapter.LabListAdapter;
import org.wizbots.labtab.model.LabLevel;
import org.wizbots.labtab.model.LabList;

import java.util.ArrayList;

public class LabListActivity extends AppCompatActivity implements View.OnClickListener {

    LabListAdapter labListAdapter;
    RecyclerView recyclerViewLabList;
    ArrayList<Object> objectArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_list);
        findViewById(R.id.iv_menu).setOnClickListener(this);

        recyclerViewLabList = (RecyclerView) findViewById(R.id.recycler_view);
        objectArrayList = new ArrayList<>();

        labListAdapter = new LabListAdapter(objectArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewLabList.setLayoutManager(mLayoutManager);
        recyclerViewLabList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLabList.setAdapter(labListAdapter);
        prepareDummyList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                Intent loginIntent = new Intent();
                loginIntent.setClass(this, LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
    }

    public void prepareDummyList() {
//        objectArrayList.add(new LabListHeader("Level", "Name", "Actions"));
        objectArrayList.add(new LabList(LabLevel.APPRENTICE.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.EXPLORER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.IMAGINEER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.LAB_CERTIFIED.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MAKER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MASTER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.WIZARD.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.APPRENTICE.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.EXPLORER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.IMAGINEER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.LAB_CERTIFIED.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MAKER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MASTER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.WIZARD.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.APPRENTICE.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.EXPLORER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.IMAGINEER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.LAB_CERTIFIED.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MAKER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.MASTER.getValue(), "Lab Name", R.drawable.action_view));
        objectArrayList.add(new LabList(LabLevel.WIZARD.getValue(), "Lab Name", R.drawable.action_view));
        labListAdapter.notifyDataSetChanged();
    }
}
