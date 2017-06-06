package com.example.mwajeeh.dagviewpager.flowFragments;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mwajeeh.dagviewpager.R;

public class DateOfBirthFragment extends BasePageFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_date_of_birth;
    }

    @Override
    public void setAnswers() {
        super.setAnswers();
        TextView age = (TextView) getView().findViewById(R.id.age);
        if (age.getText().length() == 0) {
            return;
        }
        Bundle args = new Bundle();
        args.putInt("Your Age", Integer.parseInt(age.getText().toString()));
        addFlowParams(args);
    }

    @Override
    public boolean validate() {
        Bundle params = getMyFlowParams();
        return params != null && params.getInt("Your Age", -1) != -1;
    }
}
