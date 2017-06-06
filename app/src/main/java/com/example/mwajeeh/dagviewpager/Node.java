package com.example.mwajeeh.dagviewpager;

import android.os.Bundle;

import com.commonsware.cwac.pager.PageDescriptor;

public class Node {
    public final PageDescriptor descriptor;
    public final NodeSelector selector;

    public Node(PageDescriptor descriptor, NodeSelector selector) {
        this.descriptor = descriptor;
        this.selector = selector;
    }

    public String getTag() {
        return descriptor.getFragmentTag();
    }

    public boolean select(Bundle flowParams) {
        return selector.select(flowParams);
    }
}