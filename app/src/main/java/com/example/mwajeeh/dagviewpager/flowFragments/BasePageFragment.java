package com.example.mwajeeh.dagviewpager.flowFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mwajeeh.dagviewpager.DAGViewPagerFragment;
import com.example.mwajeeh.dagviewpager.R;

public abstract class BasePageFragment extends Fragment {

    public static final String NODE_TAG = "node_tag";
    private String nodeTag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nodeTag = getArguments().getString(NODE_TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    protected abstract int getLayoutId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_action_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
        View next = view.findViewById(R.id.next);
        if (next != null) {
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswers();
                    if (validate()) {
                        ((DAGViewPagerFragment) getParentFragment()).next(nodeTag);
                    } else {
                        showError();
                    }
                }
            });
        }
    }

    public void setAnswers() {
    }

    private void showError() {
        Toast.makeText(getContext(), R.string.no_answer, Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {
        return true;
    }

    public void addFlowParams(Bundle args) {
        ((DAGViewPagerFragment) getParentFragment()).addFlowParams(nodeTag, args);
    }

    public Bundle getAllFlowParams() {
        return ((DAGViewPagerFragment) getParentFragment()).getAllFlowParams();
    }

    public Bundle getMyFlowParams() {
        return ((DAGViewPagerFragment) getParentFragment()).getAllFlowParams().getBundle(nodeTag);
    }

    public void updateArguments(Bundle args){
        nodeTag = args.getString(NODE_TAG);
        //do other necessary updates
    }
}
