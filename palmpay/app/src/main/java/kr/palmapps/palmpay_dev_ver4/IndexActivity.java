package kr.palmapps.palmpay_dev_ver4;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;

import kr.palmapps.palmpay_dev_ver4.Remote.RemoteService;
import kr.palmapps.palmpay_dev_ver4.Remote.ServiceGenerator;
import kr.palmapps.palmpay_dev_ver4.lib.DevLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 로고화면을 띄워주고 각종 권한을 기본 권한을 요청하는 액티비티
 * 요구해야 할 권한
 * 1. GPS
 * 2. BLUETOOTH
 * 3. 인터넷 연결
 *
 * 자동 로그인 설정은 폰 내부에 데이터로 설정
 *
 * 자동 로그인이 설정되어 있으면 : startSignin();
 * 자동 로그인이 설정되어 있지 않으면 : startMain();
 */
public class IndexActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private Boolean autoSignIn = false;
    private String store_id;

//    // Beacon 관련
//    private Boolean isBeaconDetected = false;
//    private BeaconManager beaconManager;
//    private BeaconRegion region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

//        DevLog.d(TAG, "setBeaconManager");
//        setBeaconManager();
//
//        region = setBeaconBasicRegion();

//        getAutoSignInState();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                DevLog.d(TAG, "AutoSignIn.State : " + autoSignIn.toString());
//
//                if (!autoSignIn) {
//                    startSign();
//
//                } else {
//                    startMain();
//                }
//            }
//        }, 2500);
    }

    /**
     * 시작 후 2.5초 동안 로고 화면을 보여주고
     * autoSignIn 여부에 따라
     * MainActivity로 이동할 지
     * SignActivity로 이동할 지 결정하는 함수
     */
    @Override
    protected void onStart() {
        super.onStart();

        getAutoSignInState();

//        setBeaconManager();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DevLog.d("AutoSignIn.State : ", autoSignIn.toString());

                if (!autoSignIn) {
                    startSign();

                } else {
                    startMain();
                }
            }
        }, 2500);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        SystemRequirementsChecker.checkWithDefaultDialogs(this);
//
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override
//            public void onServiceReady() {
//                beaconManager.startRanging(region);
//            }
//        });
//    }
//
//    @Override
//    protected void onPause() {
//        beaconManager.stopRanging(region);
//        super.onPause();
//    }

    /**
     * 자동 로그인 설정 여부를 SharedPreference 에서 가져오는 메서드
     * 로그인시 SharedPreference 에 로그인 정보를 저장하고 autoSignIn의 state 를 가져온다.
     * SharedPreference 에 정보가 없으면 기본값인 false 를 가져온다.
     */
    public void getAutoSignInState() {
        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        String autoSign = sharedPreferences.getString("autoSignIn", "false");

        if ( autoSign.equals("true") ) {
            autoSignIn = true;
        } else {
            autoSignIn = false;
        }

        DevLog.d(TAG + "sharedPreferences", autoSign);

        String email = sharedPreferences.getString("email", null);
        String username = sharedPreferences.getString("username", null);
        String nickname = sharedPreferences.getString("nickname", null);

        DevLog.d(TAG + "sharedPreferences", email + username + nickname);

    }

//    public void setBeaconManager() {
//        DevLog.d(TAG, "setBeaconManager Start");
//        beaconManager = new BeaconManager(this);
//
//        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
//            @Override
//            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> list) {
//                DevLog.d("setBeaconManager", list.toString());
//                if(!list.isEmpty()) {
//                    Beacon nearestBeacon = list.get(0);
//                    DevLog.d(TAG, "setBeaconManager Beacon RSSI : " + nearestBeacon.getRssi());
//
//                    if (!isBeaconDetected && nearestBeacon.getRssi() > -70) {
//                        DevLog.d(TAG, "setBeaconManager Detected");
//                        isBeaconDetected = true;
//
//                        String store_beacon_info = String.valueOf(nearestBeacon.getMajor());
//                        DevLog.d(TAG, store_beacon_info);
//                        getStoreIdUsingBeacon(store_beacon_info);
//
//                    } else if (isBeaconDetected && nearestBeacon.getRssi() < -70) {
//                        DevLog.d(TAG, "setBeaconManager Undetected");
//                        isBeaconDetected = false;
//
//                    } else {
//                        DevLog.d(TAG, "setBeaconManager 아무것도 안함");
//                    }
//                }
//            }
//        });
//    }
//
//
//    /**
//     * Beacon 의 Major 값을 통해 서버에서 store_id 를 찾는 메서드
//     * @param string
//     */
//    public void getStoreIdUsingBeacon(String string) {
//        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
//        Call<JsonObject> call = remoteService.getStoreIdUsingBeacon(string);
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonObject jsonObject = response.body();
//
//                if (!jsonObject.isJsonNull()){
//                    String store_id = jsonObject.get("store_id").getAsString();
//                    setStoreId(store_id);
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "주문이 불가능한 장소입니다.", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
//    }
//
//    public BeaconRegion setBeaconBasicRegion(){
//        BeaconRegion beaconRegion = new BeaconRegion("ranged region",
//                UUID.fromString("D316DA35-FDCA-4668-9A13-86FB2AC5BF35"),
//                null, null);
//        return beaconRegion;
//    }
//
//    /**
//     * store_id setter
//     * @param string setting 할 store_id
//     */
//    public void setStoreId(String string) {
//        store_id = string;
//    }


    /**
     * IndexActivity를 종료하고
     * MainActivity를 실행하는 함수
     */
    public void startMain() {
//        if(store_id == null) {
//            Intent intent = new Intent(IndexActivity.this, MainActivity.class);
//            intent.putExtra("isBeaconDetected", isBeaconDetected);
//
//            startActivity(intent);
//            finish();
//
//        } else {
            Intent intent = new Intent(IndexActivity.this, MainActivity.class);
//            intent.putExtra("store_id", store_id);
//            intent.putExtra("isBeaconDetected", isBeaconDetected);

            startActivity(intent);
            finish();
//        }
    }

    /**
     * IndexActivity를 종료하고
     * SigninActivity를 실행하는 함수
     */
    public void startSign() {

        Intent intent = new Intent(IndexActivity.this, SignActivity.class);
        startActivity(intent);

        finish();

    }
}
