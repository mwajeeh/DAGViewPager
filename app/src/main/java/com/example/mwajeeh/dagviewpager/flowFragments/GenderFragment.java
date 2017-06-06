package com.example.mwajeeh.dagviewpager.flowFragments;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioGroup;

import com.example.mwajeeh.dagviewpager.R;

public class GenderFragment extends BasePageFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gender;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RadioGroup eyeColor = (RadioGroup) view.findViewById(R.id.gender);
        eyeColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Bundle bundle = new Bundle();
                bundle.putString("Gender", checkedId == R.id.male ? "Male" : "Female");
                addFlowParams(bundle);
            }
        });
    }

    @Override
    public boolean validate() {
        Bundle params = getMyFlowParams();
        return params != null && params.getString("Gender", null) != null;
    }
}
