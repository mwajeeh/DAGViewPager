package com.example.mwajeeh.dagviewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.commonsware.cwac.pager.PageDescriptor;
import com.commonsware.cwac.pager.v4.ArrayPagerAdapter;
import com.example.mwajeeh.dagviewpager.flowFragments.BasePageFragment;

import java.util.ArrayList;

public class DynamicArgsPagerAdapter extends ArrayPagerAdapter {
    private final Context context;

    public DynamicArgsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, new ArrayList<PageDescriptor>());
        this.context = context;
    }

    @Override
    protected Fragment createFragment(PageDescriptor pageDescriptor) {
        ArgsPageDescriptor descriptor = (ArgsPageDescriptor) pageDescriptor;
        return Fragment.instantiate(context,
                pageDescriptor.getFragmentTag(), descriptor.getArgs());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object item = super.instantiateItem(container, position);
        if (item instanceof Fragment) {
            PageDescriptor pageDescriptor = getPageDescriptor(position);
            if (pageDescriptor instanceof ArgsPageDescriptor) {
                //update args again because this fragment might be already active depending on
                // ArrayPagerAdapter.RetentionStrategy, so we want to keep it up-to-date with latest
                // arguments that might have been changed
                ((BasePageFragment) item).updateArguments(((ArgsPageDescriptor) pageDescriptor).getArgs());
            }
        }
        return item;
    }
}
