package org.wizbots.labtab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.wizbots.labtab.R;

public class LabListActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_list);
        findViewById(R.id.iv_menu).setOnClickListener(this);
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

}
