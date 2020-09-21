package com.example.delivery;

import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class CustomWebViewClient extends WebViewClient {

    @Override

    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {

// TODO Auto-generated method stub

        super.onReceivedHttpAuthRequest(view, handler, host, realm);

    }

}

