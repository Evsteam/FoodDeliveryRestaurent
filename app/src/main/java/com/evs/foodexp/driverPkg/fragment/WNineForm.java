package com.evs.foodexp.driverPkg.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.evs.foodexp.R;
import com.evs.foodexp.utils.Global;

public class WNineForm extends Fragment {
    WebView webView;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.w_nine_form,container,false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("W-9 Form");
        webView=view.findViewById(R.id.webView);
        if(Global.isOnline(getContext())){
//            startWebView("https://www.expressplusnow.com/online-signature/");
//            startWebView("https://www.expressplusnow.com/upload-w9-form/");
            String url = "https://www.expressplusnow.com/upload-w9-form/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }else Global.showDialog(getActivity());




        return view;
    }

    private void startWebView(String url) {


        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        progress = new ProgressDialog(getContext());
        progress.setMessage("Loading...");
        progress.show();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getContext(), "Error:" + description, Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
        webView.loadUrl(url);
    }
}
