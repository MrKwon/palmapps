package kr.palmapps.palmpay_dev_ver4;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kr.palmapps.palmpay_dev_ver4.lib.DevLog;

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

    // 추가 필요
    private final Boolean autoSignIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
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

    /**
     * IndexActivity를 종료하고
     * MainActivity를 실행하는 함수
     */
    public void startMain() {
        Intent intent = new Intent(IndexActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
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
