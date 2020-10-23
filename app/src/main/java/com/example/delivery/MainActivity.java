package com.example.delivery;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    ArrayList<listdata> manuList = new ArrayList<>();
    private listdata listdata;
    NaverMap mNaverMap;


    String getAddress, getpostcode, getrequest, menu;
    String  getPhoneNumbers;
    private Marker marker = new Marker();

    private static final int MY_PERMISSION_PHONE = 1111;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        this.manudata();
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("276gbey63g"));


        final ListView listView = (ListView) findViewById(R.id.listView);
        final Adapter myAdapter = new Adapter(this, manuList);
        listView.setAdapter(myAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    mapset();
    setContentView(R.layout.payment);


            }
        });


    }
            //프레그먼트에 네이버맵 표시
    public void mapset() {
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    //맵의 초기설정
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

    //리스트뷰 데이터 삽입
    public void manudata() {
        manuList.add(new listdata(R.drawable.am, "아이스 아메리카노", "2000원"));
    }


        //지오코딩 주소입력시->좌표 변환해 마커 생성
    public void onBtnSetOrderTap(View view) {

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
                    list = geocoder.getFromLocationName(address, 50);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생.");
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
                getAddress = address;

                getpostcode = "77777";
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


    public void lastordercheck(View view) {

        setContentView(R.layout.lastordercheck);

        TextView checkedaddress = (TextView) findViewById(R.id.checkedaddress);
        TextView checkedmenu = (TextView) findViewById(R.id.checkedmenu);


        menu = (String) (manuList.get(0).getName() + manuList.get(0).getPrice());
        checkedaddress.setText(getAddress);
        checkedmenu.setText(menu);
        final EditText editText = (EditText) findViewById(R.id.checkedrequest);


        editText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // 입력되는 텍스트에 변화가 있을 때

            }


            @Override

            public void afterTextChanged(Editable arg0) {

                getrequest = editText.getText().toString();

            }


        });
    }


    public void returnaddress(View view) {


         setContentView(R.layout.activity_main);
    }

    //서버에 있는 데이터 베이스에 데이터저장
    public void upload(View view) {

        getPhonenum();


        InsertData task = new InsertData();

        task.execute("http://pp5273.dothome.co.kr/insertorders.php",menu,getrequest,getAddress,getPhoneNumbers);
        Toast.makeText(MainActivity.this, "주문이 접수되었습니다.", Toast.LENGTH_SHORT).show();
    }


    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String TAG;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
        //    mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String menu = (String) params[1];
            String request = (String) params[2];
            String address = (String) params[3];
            String number = (String) params[4];

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];


            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.

            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.

            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            String postParameters = "&menu=" + menu+ "&request=" + request + "&address=" + address + "&number=" + number;


            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.


                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.

                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.

                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된 변수를 이곳에 입력합니다. 인코딩을 고려해줘야 합니다.

                outputStream.flush();
                outputStream.close();


                // 3. 응답을 읽습니다.

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                } else {

                    // 에러 발생

                    inputStream = httpURLConnection.getErrorStream();
                }


                // 4. StringBuilder를 사용하여 수신되는 데이터를 저장합니다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                // 5. 저장된 데이터를 스트링으로 변환하여 리턴합니다.
                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }


    }




            //핸드폰 전화번호를 가져오기 위한 권한검사 및 권한허용 다이얼로그


    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_NUMBERS)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_PHONE_NUMBERS}, MY_PERMISSION_PHONE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_PHONE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(MainActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    }


    //전화번호 가져오기 및 변환
    @SuppressLint("MissingPermission")
    public void getPhonenum(){
        TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);



        try {
            getPhoneNumbers=telephony.getLine1Number();
        } catch (SecurityException e) {}

        if(getPhoneNumbers==null){
            getPhoneNumbers="010-6608-8659";
        }


        if (getPhoneNumbers.startsWith("+82")) {
            getPhoneNumbers = getPhoneNumbers.replace("+82", "0"); // +8210xxxxyyyy 로 시작되는 번호

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getPhoneNumbers= PhoneNumberUtils.formatNumber(getPhoneNumbers, Locale.getDefault().getCountry());
;
        } else {
            getPhoneNumbers = PhoneNumberUtils.formatNumber(getPhoneNumbers);

        }




    }


}