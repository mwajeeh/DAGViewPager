package com.example.mwajeeh.dagviewpager.flowFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.mwajeeh.dagviewpager.R;

public class AgeTriage extends BasePageFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_age_triage;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button next = (Button) view.findViewById(R.id.next);
        next.setText("OK, I Understand");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
