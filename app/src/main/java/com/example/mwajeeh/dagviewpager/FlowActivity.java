package com.example.mwajeeh.dagviewpager;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FlowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
    }

    @Override
    public void onBackPressed() {
        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof BackPressListener) {
            if (!((BackPressListener) fragment).onBackPressed()) {
                super.onBackPressed();
            }
        }
    }

    public interface BackPressListener {
        boolean onBackPressed();
    }
}
