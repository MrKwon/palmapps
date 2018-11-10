package kr.palmapps.palmpay_dev_ver4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.palmapps.palmpay_dev_ver4.Item.MemberInfoItem;
import kr.palmapps.palmpay_dev_ver4.Remote.RemoteService;
import kr.palmapps.palmpay_dev_ver4.Remote.ServiceGenerator;
import kr.palmapps.palmpay_dev_ver4.lib.DevToast;
import kr.palmapps.palmpay_dev_ver4.lib.DevLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * SignIn에 사용할 아이디 선택하는 액티비티
 *
 * PalmApps 로그인
 * 카카오 계정 로그인
 * 네이버 아이디 로그인
 * 페이스북 아이디 로그인
 *
 * node.js 서버에 연동해서 id와 pw 일치 확인하는 과정 거침
 */
public class SignActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    EditText user_id;
    EditText user_pw;

    Button palm_sign_in;
    ImageView palm_id_signup;
    ImageView naver_id;
    ImageView kakao_id;
    ImageView facebook_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        setViewEts();
        setViewBtns();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.palm_sign_in :
                DevLog.d(TAG, "BUTTON CLICKED PALM SIGN IN");
                if(emptyChecker(getInputId(), getInputPw())) {
                    goSignIn();
                }
                break;
            case R.id.palm_id_signup :
                DevToast.s(getApplicationContext(), "PALM ID로 가입");
                goSignup();
                break;
            case R.id.naver_id :
                DevToast.s(getApplicationContext(), "서비스 준비중입니다");
                break;
            case R.id.kakao_id :
                DevToast.s(getApplicationContext(), "서비스 준비중입니다");
                break;
            case R.id.facebook_id :
                DevToast.s(getApplicationContext(), "서비스 준비중입니다");
                break;
        }
    }

    /**
     * 액티비티 버튼 설정
     */
    public void setViewBtns() {
        palm_sign_in = (Button) findViewById(R.id.palm_sign_in);
        palm_sign_in.setOnClickListener(this);

        palm_id_signup = (ImageView) findViewById(R.id.palm_id_signup);
        palm_id_signup.setOnClickListener(this);

        naver_id = (ImageView) findViewById(R.id.naver_id);
        naver_id.setOnClickListener(this);

        kakao_id = (ImageView) findViewById(R.id.kakao_id);
        kakao_id.setOnClickListener(this);

        facebook_id = (ImageView) findViewById(R.id.facebook_id);
        facebook_id.setOnClickListener(this);
    }

    /**
     * 액티비티 EditText 설정
     */
    public void setViewEts() {
        user_id = (EditText) findViewById(R.id.user_id);
        user_pw = (EditText) findViewById(R.id.user_password);
    }

    /**
     * @return user_id EditText 입력값
     */
    public String getInputId() {
        return user_id.getText().toString();
    }

    /**
     * @return user_pw EditText 입력값
     */
    public String getInputPw() {
        return user_pw.getText().toString();
    }

    /**
     * 아이디 입력란이 공백인지 아닌지 확인하는 함수
     * @param id 입력받은 아이디
     * @param pw 입력받은 비밀번호
     */
    public Boolean emptyChecker(String id, String pw) {
        if(id.length() == 0 || pw.length() == 0) {
            Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void goSignIn() {

        MemberInfoItem memberInfo = new MemberInfoItem();

        memberInfo.email = user_id.getText().toString();
        memberInfo.password = user_pw.getText().toString();

        DevLog.d(TAG, memberInfo.toString());

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<JsonObject> call = remoteService.goSignIn(memberInfo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonElement state = response.body().get("state");
                DevLog.d(TAG, response.body().toString());

                if (state.getAsString().equals("success")) {
                    saveAutoSignIn(response.body());
                    startMain();
                } else if (state.getAsString().equals("pwerror") ||
                        state.getAsString().equals("notexist")) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "[error code : 520]", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server Error [error code : 408]", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void saveAutoSignIn(JsonObject jsonObject) {
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        DevLog.d("Auto Sign In", jsonObject.toString());

        editor.putString("autoSignIn", "true");
        editor.putString("email", jsonObject.get("email").getAsString());
        editor.putString("username", jsonObject.get("username").getAsString());
        editor.putString("nickname", jsonObject.get("nickname").getAsString());

        editor.apply();
    }

    public void startMain(){
        Intent intent = new Intent(SignActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    public void goSignup() {
        Intent intent = new Intent(SignActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
