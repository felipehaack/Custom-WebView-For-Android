package com.felipehs.customwebview.WebView;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.ViewParent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.felipehs.customwebview.Fragments.ModalWebView;
import com.felipehs.customwebview.R;

/**
 * Created by Felipe Haack Schmitz
 */
public class CustomWebViewClient extends WebViewClient{

    private Activity activity;
    private CustomWebView customWebView;
    private ViewParent viewParent;
    private String adPath;

    public CustomWebViewClient(Activity activity, CustomWebView customWebView, ViewParent viewParent, String adPath) {

        this.activity = activity;
        this.customWebView = customWebView;
        this.viewParent = viewParent;
        this.adPath = adPath;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

        Log.i("onPageStarted", url);

        super.onPageStarted(view, url, favicon);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        Log.i("shouldOverride", url);

        if (url.contains("WebViewFixerLock")) {

            customWebView.dragElement = true;

            return true;
        }

        if (url.contains("WebViewFixerUnlock")) {

            customWebView.dragElement = false;

            return true;
        }

        if (url.contains("WebViewFixerUrl")) {

            customWebView.loadUrl("file://" + adPath);

            customWebView.postDelayed(new Runnable() {
                @Override
                public void run() {

                    customWebView.onAppear();
                }
            }, 500);

            return true;
        }

        if (url.startsWith("mailto:")) {

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(Intent.createChooser(intent, view.getResources().getString(R.string.webclient_mailto)));

            return true;
        }

        if (url.matches("^((http|https)?):\\/{2,2}play\\.google\\.com\\/store\\/apps\\/details.*")) {

            try{

                view.getContext().getPackageManager().getPackageInfo("com.android.vending", 0);

                url = "market://" + url.substring(url.indexOf("details?id"), url.length());

                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(url));

                view.getContext().startActivity(intent);

                return true;
            }catch (Exception exception){

                Log.e("CustomWebClient", "shouldOverrideUrlLoading", exception);
            }
        }

        if (url.matches("^((http|https)?):\\/{2,2}.*") && !url.contains(view.getUrl())) {

            if (!customWebView.lockRedirectUrl) {

                customWebView.lockRedirectUrl = true;

                try {

                    FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();

                    DialogFragment modal = ModalWebView.newInstance(url);

                    modal.show(fragmentTransaction, "custom_webclient_modal");

                } catch (Exception exception) {

                    Log.i("WebViewClient", "Maybe this error is because you don't insert a context instance of activity when you get an AD. Where: getAd(context instance of activity, ...)", exception);
                }

                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        customWebView.lockRedirectUrl = false;
                    }
                }, 3000);
            }

            return true;
        }

        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        Log.i("onPageFinished", url);

        customWebView.onLibJs();
        customWebView.injectMagazineData();

        if(customWebView.callOnAppear)
            customWebView.onAppear();
    }
}
