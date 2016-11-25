package org.wizbots.labtab.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.wizbots.labtab.R;
import org.wizbots.labtab.fragment.LoginFragment;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    Fragment fragment;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        fragment = new LoginFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .commit();

    }

    @Override
    public void onClick(View view) {
    }
}
