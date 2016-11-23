package org.wizbots.labtab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.wizbots.labtab.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_forgot_password).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Intent homeIntent = new Intent();
                homeIntent.setClass(this, HomeActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.tv_forgot_password:
                Intent forgotIntent = new Intent();
                forgotIntent.setClass(this, ForgotPasswordActivity.class);
                startActivity(forgotIntent);
                break;
        }
    }
}
