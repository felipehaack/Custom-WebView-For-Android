package com.felipehs.customwebview.WebView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.felipehs.customwebview.R;

import java.io.File;

/**
 * Created by Felipe Haack Schmitz
 */
public class WebViewInterface {

    private Activity activity;
    private Context context;
    private CustomWebView customWebView;

    private Integer CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1061;
    private Integer GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 1062;

    /**
     * Interface Constructor
     * @param context the context of current application
     * @param customWebView used to change some customWebView options
     */
    public WebViewInterface(Activity activity, Context context, CustomWebView customWebView){

        this.activity = activity;
        this.context = context;
        this.customWebView = customWebView;
    }

    @JavascriptInterface
    public void lockDrag(){

        customWebView.dragElement = true;
    }

    @JavascriptInterface
    public void unlockDrag(){

        customWebView.dragElement = false;
    }

    /**
     * Where: Use by AD
     * Because: Some moments it can to be useful to fix css3 error
     * OBS: Disable webview use hardware (gpu)
     */
    @JavascriptInterface
    public void disableGpu(){

        if (Build.VERSION.SDK_INT >= 11)
            customWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    /**
     * Where: Use by AD
     * Because: Some moments AD call native android to disable GPU, and if it need to turn on, call this function
     * OBS: By default gpu is enabled
     */
    @JavascriptInterface
    public void enableGpu(){

        if (Build.VERSION.SDK_INT >= 11)
            customWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    /**
     * Where: Use by AD
     * Because: By default android webview doesn't support imgChooser, and then we code an alternative method
     * OBS: It'll show a modal to chooser between gallery or camera
     */
    @JavascriptInterface
    public void imgFileChooser(){

        String option = context.getResources().getString(R.string.webinterface_img_file_chooser_option);
        String option1 = context.getResources().getString(R.string.webinterface_img_file_chooser_option1);
        String option2 = context.getResources().getString(R.string.webinterface_img_file_chooser_option2);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(option);

        builder.setCancelable(true);

        CharSequence chooses[] = new CharSequence[]{option1, option2};

        builder.setItems(chooses, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                switch (which) {

                    case 0: {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        String image = Environment.getExternalStorageDirectory() + "/DCIM/" + System.currentTimeMillis() + ".png";

                        Uri path = Uri.fromFile(new File(image));

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, path);

                        activity.startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                        customWebView.urlFromInterface = image;

                        break;
                    }

                    case 1: {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activity.startActivityForResult(galleryIntent, GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);

                        break;
                    }
                }
            }
        });

        builder.show();
    }
}
