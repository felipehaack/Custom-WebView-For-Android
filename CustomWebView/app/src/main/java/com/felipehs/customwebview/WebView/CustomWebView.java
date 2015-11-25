package com.felipehs.customwebview.WebView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.felipehs.customwebview.Models.MaganizeData;
import com.felipehs.customwebview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Felipe Haack Schmitz.
 */
public class CustomWebView extends WebView {

    private Activity activity;
    private Context context;
    private ViewParent viewParent;
    private String adPath;
    private MaganizeData maganizeData;

    /**
     * JS to push into webview
     */
    public String jsLib = "javascript:(function(){if(typeof WebViewFixer === 'undefined'){WebViewFixer = {appear: false,draggable: false,invalide: false,tasks: {lock: 'http://www.WebViewFixer.com.br/WebViewFixerLock',unlock: 'http://www.WebViewFixer.com.br/WebViewFixerUnlock',invalide: 'http://www.WebViewFixer.com.br/WebViewFixerUrl'},preventDefault: {old: Event.prototype.preventDefault, new : function() {if (!WebViewFixer.draggable && this.returnValue) {WebViewFixer.draggable = true;window.location.href = WebViewFixer.tasks.lock;WebViewInterface.lockDrag();}WebViewFixer.preventDefault.old.apply(this, arguments);}},is: {draggable: function(version){if(version === '19'){return WebViewFixer.draggable.toString();}else{if(!WebViewFixer.draggable){window.location.href = WebViewFixer.tasks.unlock;WebViewInterface.unlockDrag();}}}},onAppear:{json: '',interval: '',reset: function(){WebViewFixer.onAppear.interval = window.setInterval(function(){if(!WebViewFixer.invalide){window.location.href = WebViewFixer.tasks.invalide;}else{WebViewFixer.invalide = false;window.clearInterval(WebViewFixer.onAppear.interval);}}, 33);},start: function(){if(!WebViewFixer.appear){if(document !== null){if(document.readyState === 'complete' && typeof window.onAppear === 'function'){WebViewFixer.appear = true;if(typeof jAdmagReport === 'object'){try{jAdmagReport.initialize.platform.admagSDK(WebViewFixer.onAppear.json);}catch(e){}}window.onAppear();return;}}window.setTimeout(WebViewFixer.onAppear.start, 100);}},init: function(){var href = window.location.href;if(href.indexOf('about:blank') < 0 && href.length > 5){WebViewFixer.onAppear.start();}else{WebViewFixer.onAppear.reset();}}}};Event.prototype.preventDefault = WebViewFixer.preventDefault.new;}})();";
    public String jsOnAppear = "javascript:(function(){try{ WebViewFixer.onAppear.init();}catch(e){ }})();";
    public String jsDisableDrag = "javascript:(function(){try{ WebViewFixer.draggable = false;}catch(e){ }})();";
    public String jsIsDraggable19 = "try{ WebViewFixer.is.draggable('19');}catch(e){ }";
    public String jsIsDraggable18 = "javascript:(function(){try{ WebViewFixer.is.draggable();}catch(e){ }})();";

    /**
     * onAppear controllers
     */
    public Boolean callOnAppear = false;
    public Boolean documetLoaded = false;

    /**
     * Used by onResultActivity
     */
    public String urlFromInterface;

    /**
     * Used to lock webview redirect url and drag element
     */
    public Boolean lockRedirectUrl = false;
    public Boolean dragElement = false;
    public Boolean dragElementCall = false;
    public long dragTimestamp;

    /**
     * Used to detect results from intent used by javascript interface
     */
    private Integer CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1061;
    private Integer GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 1062;

    /**
     * Start webview with settings
     *
     * @param context    It need to be an instance of activity
     * @param viewParent Used to lock drag inside webview
     */
    public CustomWebView(Activity activity, Context context, ViewParent viewParent, String adURL, MaganizeData maganizeData) {

        super(context);

        this.activity = activity;
        this.context = context;
        this.viewParent = viewParent;
        this.adPath = adURL;
        this.maganizeData = maganizeData;

        configureWebView();
    }

    private void configureWebView() {

        WebSettings settings = getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(settings.LOAD_NO_CACHE);
        settings.setGeolocationEnabled(true);

        setInitialScale(1);
        setSaveEnabled(true);
        setDrawingCacheEnabled(true);

        setOverScrollMode(OVER_SCROLL_NEVER);
        setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
        setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);

        if (Build.VERSION.SDK_INT >= 17)
            settings.setMediaPlaybackRequiresUserGesture(false);

        if (Build.VERSION.SDK_INT >= 19)
            WebView.setWebContentsDebuggingEnabled(true);

        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        setWebViewClient(new CustomWebViewClient(activity, this, viewParent, adPath));

        setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                new AlertDialog.Builder(activity)
                        .setTitle(R.string.app_name)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();

                return super.onJsAlert(view, url, message, result);
            }

            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {

                final GeolocationPermissions.Callback callbackF = callback;
                final String originF = origin;

                callbackF.invoke(originF, true, false);
            }
        });

        addJavascriptInterface(new WebViewInterface(activity, context, this), "WebViewInterface");

        settings.setGeolocationDatabasePath("/data/data/" + context.getPackageName() + "/databases/");

        if (adPath.matches("^((http|https)?):\\/{2,2}.*"))
            loadUrl(adPath);
        else
            loadUrl("file:///" + adPath);
    }

    private void scroll() {

        int temp_ScrollX = getScrollX();
        int temp_ScrollY = getScrollY();

        scrollTo(temp_ScrollX - 1, getScrollY() + 1);
        scrollTo(temp_ScrollX, temp_ScrollY);
    }

    public void onAppear() {

        loadUrl(jsLib);
        loadUrl(jsOnAppear);

        callOnAppear = true;

        Log.i("onAppear", "called");
    }

    public void injectMagazineData() {

        if (maganizeData != null) {

            JSONObject jsonObject = new JSONObject();

            try {

                jsonObject.put("issue_name", maganizeData.getIssueName());
                jsonObject.put("issue_identifier", maganizeData.getIssueIdentifier());
                jsonObject.put("issue_price", maganizeData.getIssuePrice());
                jsonObject.put("issue_total_pages", maganizeData.getIssueTotalPages());
                jsonObject.put("issue_publication_date", maganizeData.getIssuePublicationDate());
                jsonObject.put("device_id", maganizeData.getDeviceId());

                loadUrl("javascript:(function(){maganizeData = " + jsonObject.toString() + ";})();");
            } catch (Exception exception) {

                exception.printStackTrace();
            }
        }
    }

    public void onLibJs() {

        loadUrl(jsLib);
    }

    private void onDisableDrag() {

        postDelayed(new Runnable() {
            @Override
            public void run() {

                dragElementCall = false;

                loadUrl(jsDisableDrag);
            }
        }, 50);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {

        super.onSizeChanged(w, h, ow, oh);

        if (ow + oh > 0) {

            postDelayed(new Runnable() {
                @Override
                public void run() {

                    onAppear();
                }
            }, 1000);
        }
    }

    private String getRealPathFromURI(Context context, Uri uri) {

        String[] proj = {MediaStore.Images.Media.DATA};

        CursorLoader loader = new CursorLoader(context, uri, proj, null, null, null);

        Cursor cursor = loader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String path = "";

        if (resultCode == Activity.RESULT_OK) {

            if (context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {

                if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

                    path = urlFromInterface;

                    urlFromInterface = "";
                }

                if (requestCode == GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {

                    Uri uri = data.getData();

                    path = getRealPathFromURI(context, uri);
                }
            } else {

                Log.e("Device", "There's any permission to access the files!");
            }
        }

        Pattern pattern = Pattern.compile("[^\\/].+");
        Matcher matcher = pattern.matcher(path);

        if (matcher.find())
            path = matcher.group();
        else
            path = "";

        loadUrl("javascript:WebViewInterface.imgFileChooserResult('file:///" + path + "');");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN: {

                scroll();

                dragElement = true;
                dragElementCall = false;
                dragTimestamp = event.getEventTime();

                break;
            }

            case MotionEvent.ACTION_MOVE: {

                if (event.getEventTime() - dragTimestamp > 150) {

                    if (!dragElement)
                        requestDisallowInterceptTouchEvent(false);
                    else
                        requestDisallowInterceptTouchEvent(true);

                    return super.onTouchEvent(event);
                } else {

                    if (Build.VERSION.SDK_INT >= 19) {

                        evaluateJavascript(jsIsDraggable19, new ValueCallback<String>() {

                            @Override
                            public void onReceiveValue(String result) {

                                Log.i("onReceiveValue", result);

                                if (dragElementCall) {

                                    if (result.contains("false")) {

                                        dragElement = false;
                                    }
                                } else {

                                    dragElementCall = true;
                                }
                            }
                        });
                    } else {

                        loadUrl(jsIsDraggable18);
                    }
                }

                break;
            }

            case MotionEvent.ACTION_UP: {

                if (event.getPointerCount() == 1) {

                    onDisableDrag();

                    break;
                }
            }

            case MotionEvent.ACTION_CANCEL: {

                onDisableDrag();

                break;
            }
        }

        requestDisallowInterceptTouchEvent(true);

        return super.onTouchEvent(event);
    }
}
