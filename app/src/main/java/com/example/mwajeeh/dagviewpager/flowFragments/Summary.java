package com.example.mwajeeh.dagviewpager.flowFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mwajeeh.dagviewpager.R;

public class Summary extends BasePageFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_summary;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout container = (LinearLayout) view.findViewById(R.id.summary);
        Bundle flowParams = getAllFlowParams();
        for (String key : flowParams.keySet()) {
            Bundle bundle = flowParams.getBundle(key);
            if (bundle != null) {
                for (String itemKey : bundle.keySet()) {
                    View item = inflater.inflate(R.layout.summary_item, container, false);
                    TextView title = (TextView) item.findViewById(R.id.title);
                    TextView value = (TextView) item.findViewById(R.id.detail);
                    title.setText(itemKey);
                    value.setText(bundle.get(itemKey).toString());
                    container.addView(item);
                }
            }
        }
        Button next = (Button) view.findViewById(R.id.next);
        next.setText("Finish");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
