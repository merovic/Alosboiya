package alosboiya.jeddahwave.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.TinyDB;
import im.delight.android.webview.AdvancedWebView;

public class AboutUsFragment extends Fragment implements AdvancedWebView.Listener{

    public static final String TAG = "ass6";

    private AdvancedWebView mWebView;

    Toolbar toolbar;

    TinyDB tinyDB;

    public AboutUsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_my, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tinyDB = new TinyDB(getContext());

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        mWebView = getActivity().findViewById(R.id.webview);
        mWebView.setListener(getActivity(), this);
        mWebView.loadUrl("http://alosboiya.com.sa/about.html?fbclid=IwAR0J3izKKdCREOYq58Shwet2TFyF7BqTM-D6cBlyN_9YwjmjMYqW4Otawlk");
        mWebView.zoomOut();
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
    }


    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();

    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        mWebView.onPause();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        mWebView.onDestroy();

        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
    }


    @Override
    public void onPageStarted(String url, Bitmap favicon) {

        mWebView.zoomOut();
    }

    @Override
    public void onPageFinished(String url) {

        mWebView.zoomOut();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

        mWebView.zoomOut();
    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
