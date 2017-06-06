package com.example.mwajeeh.dagviewpager;

import android.os.Bundle;
import android.os.Parcel;

import com.commonsware.cwac.pager.PageDescriptor;
import com.example.mwajeeh.dagviewpager.flowFragments.BasePageFragment;

public class ArgsPageDescriptor implements PageDescriptor {
    private final PageDescriptor descriptor;
    private Bundle args;

    public ArgsPageDescriptor(PageDescriptor descriptor, Bundle args) {
        this.descriptor = descriptor;
        this.args = args;
        if (this.args == null) {
            this.args = new Bundle();
        }
        this.args.putString(BasePageFragment.NODE_TAG, descriptor.getFragmentTag());
    }

    public Bundle getArgs() {
        return args;
    }

    @Override
    public String getFragmentTag() {
        return descriptor.getFragmentTag();
    }

    @Override
    public String getTitle() {
        return descriptor.getTitle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.descriptor, flags);
        dest.writeBundle(this.args);
    }

    protected ArgsPageDescriptor(Parcel in) {
        this.descriptor = in.readParcelable(PageDescriptor.class.getClassLoader());
        this.args = in.readBundle(getClass().getClassLoader());
    }

    public static final Creator<ArgsPageDescriptor> CREATOR = new Creator<ArgsPageDescriptor>() {
        @Override
        public ArgsPageDescriptor createFromParcel(Parcel source) {
            return new ArgsPageDescriptor(source);
        }

        @Override
        public ArgsPageDescriptor[] newArray(int size) {
            return new ArgsPageDescriptor[size];
        }
    };
}