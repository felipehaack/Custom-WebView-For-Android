package com.felipehs.customwebview.CoreViewPager.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.felipehs.customwebview.Models.MaganizeData;
import com.felipehs.customwebview.WebView.CustomWebView;

import java.util.List;

/**
 * Created by Felipe Haack Schmitz.
 */
public class CoreViewPagerAdapter extends PagerAdapter {

    private Activity mActivity;
    private Context mContext;
    private Integer mTotalPages;
    private List<String> paths;
    private MaganizeData maganizeData;

    public CoreViewPagerAdapter(Activity mActivity, Context context, Integer totalPages, List<String> paths, MaganizeData maganizeData) {

        super();

        this.mActivity = mActivity;
        this.mContext = context;
        this.mTotalPages = totalPages;
        this.paths = paths;
        this.maganizeData = maganizeData;
    }

    @Override
    public int getCount() {

        return mTotalPages;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        CustomWebView customWebView = new CustomWebView(mActivity, mContext, container, paths.get(position), maganizeData);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        customWebView.setLayoutParams(params);

        container.addView(customWebView);

        return customWebView;
    }

    @Override
    public int getItemPosition(Object object) {

        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }
}
