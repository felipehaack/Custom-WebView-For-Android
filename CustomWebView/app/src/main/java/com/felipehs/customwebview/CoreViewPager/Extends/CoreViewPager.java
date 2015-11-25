package com.felipehs.customwebview.CoreViewPager.Extends;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.felipehs.customwebview.WebView.CustomWebView;

/**
 * Created by Felipe Haack Schmitz.
 */
public class CoreViewPager extends ViewPager {

    private static final Integer OFFSET_PAGE_LOAD = 1;

    private Context mContext;

    public CoreViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

        this.mContext = context;

        configuration();
    }

    public void configuration() {

        this.setOffscreenPageLimit(OFFSET_PAGE_LOAD);
        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == 0) {

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            View view = getFocusedChild();

                            if (view instanceof CustomWebView) {

                                CustomWebView customWebView = (CustomWebView) view;
                                customWebView.onAppear();
                            }
                        }
                    }, 500);
                }
            }
        });
    }
}