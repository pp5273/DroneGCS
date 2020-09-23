package com.example.delivery;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    ArrayList<listdata> manuList;
    private listdata listdata;
 //   public payment2 payment2;
    NaverMap mNaverMap;
   private WebView webView;
   TextView result;
String getAddress;
    private Handler handler;
    private Marker marker = new Marker();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 //       result = (TextView) findViewById(R.id.address);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.manudata();
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("276gbey63g"));


        ListView listView = (ListView)findViewById(R.id.listView);
        final Adapter myAdapter = new Adapter(this,manuList);
        listView.setAdapter(myAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mapset();
                setContentView(R.layout.payment);

           //     result = (TextView) findViewById(R.id.address);
          //      result.setText(String.format("주소를 입력해 주세요."));
      //          postcode = (TextView) findViewById(R.id.postcode);
            }
        });


        // 핸들러를 통한 JavaScript 이벤트 반응
     //   handler = new Handler();


    }
public void mapset(){
    FragmentManager fm = getSupportFragmentManager();
    MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
    if (mapFragment == null) {
        mapFragment = MapFragment.newInstance();
        fm.beginTransaction().add(R.id.map, mapFragment).commit();
    }
    mapFragment.getMapAsync(this);
}


    @UiThread
    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        this.mNaverMap = naverMap;


        // 최초 위치, 줌 설정 //
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(35.9424531, 126.6811309), // 대상 지점
                17 // 줌 레벨
        );
        naverMap.setCameraPosition(cameraPosition);


    }







//    @SuppressLint("SetJavaScriptEnabled")
//    public void init_webView(){
//
//
//        // WebView 설정
//        webView = findViewById(R.id.webview);
//
//        // JavaScript 허용
//
//        webView.getSettings().setJavaScriptEnabled(true);
//
//        // JavaScript의 window.open 허용
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
//        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
//        webView.addJavascriptInterface(new AndroidBridge(), "Delivery");
//
//
//        webView.getSettings().setSupportMultipleWindows(true);
//
//
//        webView.setWebViewClient(new CustomWebViewClient());
//        //webView.setWebChromeClient(new WebChromeClient());
//
//
//
//      //  webView.loadUrl("http://pp5273.ivyro.net/address.html");
//
//        //webView.loadUrl("http://pp5273.ivyro.net/address.php/");
//
//
//        webView.loadUrl("http://pp5273.ivyro.net/test.php/");
//
//    }
////    public void goToWeb(View view) {
////
//////        setContentView(R.layout.gotowebview);
//////        // WebView 초기화
//////        init_webView();
////
////
////
////    }
//
//    private class AndroidBridge {
//
//        @JavascriptInterface
//        public void setAddress(final String arg1, final String arg2, final String arg3) {
//
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//
//                    setContentView(R.layout.payment2);
//                    postcode.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
//
//                    // WebView를 초기화 하지않으면 재사용할 수 없음
//
//
//
//                }
//            });
//
//        }
//    }



    public  void manudata(){
    manuList= new ArrayList<listdata>();
    manuList.add(new listdata(R.drawable.am,"아이스 아메리카노", "2000원"));
}




    public void onBtnSetOrderTap(View view) {
        mapset();

        View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        final Geocoder geocoder = new Geocoder(this);



        TextView title = dialogView.findViewById(R.id.title);
        title.setText("주소를 입력해 주세요");
        TextView message = dialogView.findViewById(R.id.message);
        message.setVisibility(View.GONE);
        final EditText editText = dialogView.findViewById(R.id.addressBox);
        final TextView postcode = findViewById(R.id.postcode);
        Button btnPositive = dialogView.findViewById(R.id.btnPositive);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Address> list = null;
                String address = editText.getText().toString();

                try {
                    list = geocoder.getFromLocationName(address,50);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생.");
                }

                if (list != null) {
                    if (list.size() == 0) {
                    } else {
                        LatLng mar = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
                        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(mar).animate(CameraAnimation.Linear);
                        marker.setPosition(mar);
                        marker.setMap(mNaverMap);
                        mNaverMap.moveCamera(cameraUpdate);
                        postcode.setText(address);
                    }
                }
                alertDialog.dismiss();
                getAddress= address;

            }
        });
        Button btnNegative = dialogView.findViewById(R.id.btnNegative);
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }


    public void lastordercheck(View view){
      //  final Button ordercheck = (Button) findViewById(R.id.checkedorder);
        setContentView(R.layout.lastordercheck);
       // final TextView checkedaddress = findViewById(R.id.checkedaddress);
        TextView checkedaddress = (TextView) findViewById(R.id.checkedaddress);
        checkedaddress.setText(getAddress);



    }

public void returnaddress(View view){
       // setContentView(R.layout.payment);
//    Intent sendIntent = new Intent();
//
//    sendIntent.setAction(Intent.ACTION_SEND);
//    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//    sendIntent.setType("text/plain");
//
//    Intent shareIntent = Intent.createChooser(sendIntent, null);
//    startActivity(shareIntent);

    Intent intent = new Intent();
    intent.setClassName("com.example.dronedelivery", "com.example.dronedelivery.MainActivity");
    intent.putExtra("putData", "UserData");
    startActivityForResult(intent, 0);

}

}