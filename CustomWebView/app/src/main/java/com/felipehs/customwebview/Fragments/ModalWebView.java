package com.felipehs.customwebview.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.felipehs.customwebview.R;

/**
 * Created by Felipe Haack Schmitz.
 */
public class ModalWebView extends DialogFragment {

    private String url;

    /**
     * Used to show the website into DialogFragment
     * @param adPath website url
     * @return
     */
    public static ModalWebView newInstance(String adPath){

        ModalWebView modal = new ModalWebView();

        Bundle args = new Bundle();

        args.putString("url", adPath);

        modal.setArguments(args);

        return modal;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        /**
         * Modal style to show (bottom to up) and dismiss (up to bottom)
         * You found it inside anim folder
         */
        getDialog().getWindow().getAttributes().windowAnimations = R.style.webclient_modal_animator;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.url = getArguments().getString("url");

        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.webclient_modal, container, false);

        /**
         * Button to close the DialogFragment
         * Your style is inside res folder
         */
        Button leave = (Button) view.findViewById(R.id.webclient_modal_button);

        leave.setText(R.string.webclient_button);

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        /**
         * A default webview to show website
         * I turn on some feature to improve website
         */
        WebView webView = (WebView) view.findViewById(R.id.webclient_modal_webview);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);

        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(false);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl(this.url);

        return view;
    }
}
