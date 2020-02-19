package org.wizbots.labtab.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.wizbots.labtab.R;

public class ResultActivity extends AppCompatActivity {

    private TextView textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        textView = (TextView)findViewById(R.id.textView1);
        textView.setText("Welcome to the Result Activity");
    }
}
