package com.felipehs.customwebview;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.felipehs.customwebview.CoreViewPager.Adapters.CoreViewPagerAdapter;
import com.felipehs.customwebview.CoreViewPager.Extends.CoreViewPager;
import com.felipehs.customwebview.Models.MaganizeData;
import com.felipehs.customwebview.WebView.CustomWebView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Felipe Haack Schmitz
 */
public class MainActivity extends AppCompatActivity {

    CoreViewPager coreViewPager;
    CoreViewPagerAdapter viewerAdapter;
    Integer QTD_PAGES = 5;
    MaganizeData maganizeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        maganizeData = createMagazineData();

        List<String> paths = new ArrayList<>();

        paths.add("http://media.admag.com.br/agencies/27/advertisers/32/advertisements/127/screens/fa1d7002ce030d8ef5789ea227d1623d/index.html");
        paths.add("http://media.admag.com.br/agencies/27/advertisers/32/advertisements/127/screens/650ea1709cc6c20b4dd5b6f624cc02ca/index.html");

        paths.add("android_asset/Features/index.html");

        paths.add("http://media.admag.com.br/agencies/27/advertisers/32/advertisements/73/screens/f49b762f13846804ab78a36ec0803c37/index.html");
        paths.add("http://media.admag.com.br/agencies/27/advertisers/32/advertisements/58/screens/6dfcdd5d68e8ebeda4c82a8a33ecdb7a/index.html");


        viewerAdapter = new CoreViewPagerAdapter(this, getApplicationContext(), QTD_PAGES, paths, maganizeData);

        coreViewPager = (CoreViewPager) findViewById(R.id.core_view_pager);

        coreViewPager.setAdapter(viewerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        View view = coreViewPager.getChildAt(coreViewPager.getCurrentItem() - 1);

        if(view instanceof CustomWebView){

            CustomWebView customWebView = (CustomWebView) view;
            customWebView.onActivityResult(requestCode, resultCode, data);
        }
    }

    public MaganizeData createMagazineData(){

        MaganizeData maganizeData = new MaganizeData();

        maganizeData.setIssueName("The Future Is Here Ed. 2");
        maganizeData.setIssueIdentifier("VGhlIEZ1dHVyZSBJcyBIZXJlIEVkLiAy");
        maganizeData.setIssuePrice("FREE");
        maganizeData.setIssueTotalPages(QTD_PAGES);
        maganizeData.setIssuePublicationDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()));
        maganizeData.setDeviceId(Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));

        return maganizeData;
    }
}
