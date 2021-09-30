package com.evs.foodexp.commonPkg.common.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.evs.foodexp.R;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;


public class StipeAccounteWeb extends AppCompatActivity {

    WebView myWebView;
    SharedPreferences prefe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stripe_account);
        myWebView = (WebView) findViewById(R.id.webview);

        Utills.StatusBarColour(StipeAccounteWeb.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Stripe Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        prefe= PreferenceManager.getDefaultSharedPreferences(StipeAccounteWeb.this);
        Log.e("stripe Url",SessionManager.get_StripeAccountUrl(prefe));
//        myWebView.loadUrl(SessionManager.get_StripeAccountUrl(prefe));

//        myWebView.setWebViewClient(new MyWebViewClient());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(SessionManager.get_StripeAccountUrl(prefe)));
        startActivity(intent);
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (SessionManager.get_StripeAccountUrl(prefe).equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
}
