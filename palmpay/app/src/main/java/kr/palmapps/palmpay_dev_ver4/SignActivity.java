package kr.palmapps.palmpay_dev_ver4;

import android.content.Intent;
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

import kr.palmapps.palmpay_dev_ver4.lib.DevToast;
import kr.palmapps.palmpay_dev_ver4.lib.DevLog;

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
                DevToast.s(getApplicationContext(), "Palm Sign In");
                if(emptyChecker(getInputId(), getInputPw())) {
                    DevToast.s(getApplicationContext(), "로그인 성공");
                    startMain();
                }
                break;
            case R.id.palm_id_signup :
                DevToast.s(getApplicationContext(), "PALM ID로 가입");
                goSignup();
                break;
            case R.id.naver_id :
                DevToast.s(getApplicationContext(), "Naver 아이디로 로그인");
                break;
            case R.id.kakao_id :
                DevToast.s(getApplicationContext(), "Kakao 계정으로 로그인");
                break;
            case R.id.facebook_id :
                DevToast.s(getApplicationContext(), "FB 아이디로 로그인");
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
