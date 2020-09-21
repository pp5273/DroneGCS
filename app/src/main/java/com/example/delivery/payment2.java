//package com.example.delivery;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.os.Handler;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentManager;
//
//import com.naver.maps.map.MapFragment;
//import com.naver.maps.map.NaverMap;
//import com.naver.maps.map.NaverMapSdk;
//import com.naver.maps.map.OnMapReadyCallback;
//
//
//public class payment2 extends MainActivity implements OnMapReadyCallback {
//
//    private WebView webView;
//    private TextView result;
//    private Handler handler;
//
//
//
//    protected void map() {
//
//        NaverMapSdk.getInstance(this).setClient(
//                new NaverMapSdk.NaverCloudPlatformClient("276gbey63g"));
//        FragmentManager fm = getSupportFragmentManager();
//        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.mNavermap);
//        if (mapFragment == null) {
//            mapFragment = MapFragment.newInstance();
//            fm.beginTransaction().add(R.id.mNavermap, mapFragment).commit();
//        }
//        mapFragment.getMapAsync(this);
//    }
//
////    public void address(){
////
////        result = (TextView) findViewById(R.id.result);
////
////        // WebView 초기화
////        payment2.init_webView();
////
////        // 핸들러를 통한 JavaScript 이벤트 반응
////        handler = new Handler();
////    }
////
////
////    public void init_webView(){
////        // WebView 설정
////        webView = (WebView) findViewById(R.id.webView);
////        // JavaScript 허용
////        webView.getSettings().setJavaScriptEnabled(true);
////        // JavaScript의 window.open 허용
////        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
////        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
////        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
////        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
////        // web client 를 chrome 으로 설정
////        webView.setWebChromeClient(new WebChromeClient());
////        // webview url load
////        webView.loadUrl("http://pp5273.ivyro.net/address.html");
////
////
////    }
////    private class AndroidBridge {
////        @JavascriptInterface
////        public void setAddress(final String arg1, final String arg2, final String arg3) {
////            handler.post(new Runnable() {
////                @Override
////                public void run() {
////                    result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
////                    // WebView를 초기화 하지않으면 재사용할 수 없음
////                    init_webView();
////                }
////            });
////        }
////    }
//
//    @Override
//    public void onMapReady(@NonNull NaverMap naverMap) {
//
//    }
//
//
//
//}
