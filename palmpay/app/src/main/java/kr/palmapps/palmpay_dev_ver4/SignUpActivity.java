package kr.palmapps.palmpay_dev_ver4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.palmapps.palmpay_dev_ver4.Item.MemberInfoItem;
import kr.palmapps.palmpay_dev_ver4.Remote.RemoteService;
import kr.palmapps.palmpay_dev_ver4.Remote.ServiceGenerator;
import kr.palmapps.palmpay_dev_ver4.lib.DevLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    // Conditions
    private static int NOT_SATISFIED = 0;
    private static int YET_SATISFIED = 1;
    private static int SATISFIED = 2;

    // Layouts
    RelativeLayout midLayout;
    LinearLayout bottomLayout;

    //Buttons
    ImageView agree_grant;
    Button do_sign_up;
    Button canIuseThisId;

    //EditTexts, Spinner
    EditText signup_id;
    EditText signup_pw;
    EditText signup_pw2;
    EditText signup_username;
    EditText signup_nickname;
    EditText signup_phone;

    Spinner signup_sex;

    //ImageViews;
    ImageView signup_pw_length;
    ImageView signup_pw_check;

    private static Boolean isGrantAgree = false;
    private static int isSatisfied = NOT_SATISFIED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setViewLayouts();
        setViewBts();
        setViewEditTexts();
        setViewImageViews();
        setViewSpinner();

        setTextChangedListener(signup_pw);
        setTextChangedListener(signup_pw2);

        initVisibility();

    }

    /**
     * button click listener
     * ImageView 버튼인 agree_grant 와 Button do_sign_up 이 있다.
     * @param view 현재 액티비티의 뷰
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.agree_grant :
                changeAgreeGrantChanger(isGrantAgree);
                break;

            case R.id.canIuseThisID :
                isPossibleId();
                break;

            case R.id.do_sign_up :
                isGrantAgree = isCanDoSignUp();
                if (isGrantAgree) {
                    // 서버와 통신해서 저장하고
                    sendSignUpInfo();

                } else {
                    if (canIuseThisId.getText().toString().equals("사용가능")){
                        Toast.makeText(getApplicationContext(), "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "아이디 중복 확인이 필요합니다", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    public void initVisibility() {
        midLayout.setVisibility(View.INVISIBLE);
        bottomLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * Setting Layouts
     */
    public void setViewLayouts() {
        midLayout    = (RelativeLayout) findViewById(R.id.midLayout);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
    }

    /**
     * Setting Buttons
     */
    public void setViewBts() {
        agree_grant  = (ImageView) findViewById(R.id.agree_grant);
        agree_grant.setOnClickListener(this);
        do_sign_up   = (Button) findViewById(R.id.do_sign_up);
        do_sign_up.setOnClickListener(this);
        canIuseThisId = (Button) findViewById(R.id.canIuseThisID);
        canIuseThisId.setOnClickListener(this);
    }

    /**
     * Setting EditTexts
     */
    public void setViewEditTexts() {
        signup_id   = (EditText) findViewById(R.id.signup_id);
        signup_pw   = (EditText) findViewById(R.id.signup_pw);
        signup_pw2  = (EditText) findViewById(R.id.signup_pw2);
        signup_username = (EditText) findViewById(R.id.signup_username);
        signup_nickname = (EditText) findViewById(R.id.signup_nickname);
        signup_phone= (EditText) findViewById(R.id.signup_phone);
    }

    public void setViewImageViews() {
        signup_pw_length = (ImageView) findViewById(R.id.signup_pw_length);
        signup_pw_check  = (ImageView) findViewById(R.id.signup_pw_check);
    }

    public void setViewSpinner() {
        signup_sex = (Spinner) findViewById(R.id.signup_sex);
    }

    /**
     * AgreeGrant Changer
     */
    public void changeAgreeGrantChanger(Boolean bool) {
        if (bool) {
            isGrantAgree = false;
            agree_grant.setImageResource(R.drawable.ic_check_circle_green_24dp);
            midLayout.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);
            signup_id.clearFocus();
            editTextInitializer();
            everyEditTextClearFocus();

        } else if (!bool) {
            isGrantAgree = true;
            agree_grant.setImageResource(R.drawable.ic_radio_button_unchecked_grey_24dp);
            midLayout.setVisibility(View.INVISIBLE);
            bottomLayout.setVisibility(View.INVISIBLE);
            editTextFocusable(signup_id);
        }
    }

    /**
     * 약관 동의를 해제하면 EditText들을 초기화 시키는 메서드
     */
    public void editTextInitializer() {
        signup_id.setText(null);
        signup_pw.setText(null);
        signup_pw2.setText(null);
        signup_username.setText(null);
        signup_nickname.setText(null);
        signup_phone.setText(null);

        canIuseThisId.setBackgroundResource(R.drawable.bg_basic_red);
        canIuseThisId.setText("중복확인");
    }

    public void everyEditTextClearFocus() {
        signup_id.clearFocus();
        signup_pw.clearFocus();
        signup_pw2.clearFocus();
        signup_username.clearFocus();
        signup_nickname.clearFocus();
        signup_phone.clearFocus();
    }


    /**
     * TextChangedListener 를 연결하는 메서드
     * @param et setTextChangedListener 연결할 EditText 객체
     */
    public void setTextChangedListener(final EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et == signup_pw) {
                    //imageview signup_pw_length
                    conditionChecker(pwConditionChecker(et), signup_pw_length);

                } else if (et == signup_pw2) {
                    //imageview signup_pw_check
                    conditionChecker(twoPwSameChecker(et), signup_pw_check);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * icon의 색상을 condition에 따라 변경하는 메서드
     *
     * @param condition SATISFIED / YET_SATISFIED / NOT_SATISFIED 셋 중 하나의 상태
     * @param img 어떤 ImageView를 바꿀지 설정
     */
    public void conditionChecker(int condition, ImageView img) {
        if (condition == SATISFIED) {
            img.setImageResource(R.drawable.ic_check_circle_green_24dp);

        } else if (condition == YET_SATISFIED) {
            img.setImageResource(R.drawable.ic_check_circle_yellow_24dp);

        } else if (condition == NOT_SATISFIED) {
            img.setImageResource(R.drawable.ic_check_circle_red_24dp);
        }
    }

    /**
     * 비밀번호의 조건을 체크하는 메서드
     * @param et 입력된 EditText
     * @return 조건에 따라 SATISFIED / YET_SATISFIED / NOT_SATISFIED 중 하나를 반환
     */
    public int pwConditionChecker(EditText et) {
        // 만족 - 글자수 8~16 자 and 대소문자숫자 모두 포함
        // 부족 - 글자수 8자 미만 대소문자숫자 모두포함
        // 8자 이상 대소문자숫자 부족
        // 불만족 = 8자 미만


        String input = et.getText().toString();
        if (input.length() < 8) {
            return NOT_SATISFIED;
        }

        return SATISFIED;
    }
//
//        String pwPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{8,16}$";
//        Matcher matcher = Pattern.compile(pwPattern).matcher(input);
//
//        pwPattern = "(.)\\1\\1\\1";
//        Matcher matcher2 = Pattern.compile(pwPattern).matcher(input);
//
//        if (!matcher.matches()) {
//            // !matcher.matches() 가 true 일
//
//        } else if (matcher2.find()) {
//
//
//        } else if (input.contains(" ")) {
//            return NOT_SATISFIED;
//
//        } else {
//
//        }

    /**
     * 입력한 두개의 비밀번호가 일치하는지 확인
     * @param et 설정할 Edit Text 객체
     * @return 조건에 따라 SATISFIED / YET_SATISFIED / NOT_SATISFIED 중 하나를 반환
     */
    public int twoPwSameChecker(EditText et) {
        String pw1 = signup_pw.getText().toString();
        String pw2 = et.getText().toString();

        if (pw2.equals(pw1) && pwConditionChecker(signup_pw) == SATISFIED) {
            DevLog.d(TAG, pw1 + "////" + pw2);
            return SATISFIED;
        }
        return NOT_SATISFIED;
    }

    public void editTextUnfocusable(EditText editText){
        editText.setFocusable(false);
    }

    public void editTextFocusable(EditText editText){
        editText.setFocusable(true);
    }

    public void isPossibleId() {
        String checkId = signup_id.getText().toString();
        DevLog.d(TAG, checkId);

        if(checkId.equals("")) {
            Toast.makeText(getApplicationContext(), "아이디를 입력해주세요", Toast.LENGTH_LONG).show();

        } else {
            RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

            Call<JsonObject> call = remoteService.isPossibleId(checkId);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    JsonElement state = response.body().get("state");

                    DevLog.d(TAG, response.body().toString());

                    if ( state.toString().equals("\"possible\"") ) {
                        isPossible();
                        editTextUnfocusable(signup_id);
                    } else if ( state.toString().equals("\"impossible\"") ) {
                        isImpossible();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Server Error [error code : 408]", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * response 를 받아 사용가능으로 canIuseThisId 버튼의 속성을 변경하는 메서드
     */
    public void isPossible() {
        canIuseThisId.setBackgroundResource(R.drawable.bg_basic_greed_possible);
        canIuseThisId.setText("사용가능");
    }

    public void isImpossible() {
        Toast.makeText(getApplicationContext(), "이미 사용중인 아이디입니다", Toast.LENGTH_LONG).show();
    }

    public void sendSignUpInfo() {

        MemberInfoItem memberInfo = new MemberInfoItem();

        memberInfo.email    = signup_id.getText().toString();
        memberInfo.password = signup_pw.getText().toString();
        memberInfo.username = signup_username.getText().toString();
        memberInfo.nickname = signup_nickname.getText().toString();
        memberInfo.sex      = signup_sex.getSelectedItem().toString();
        memberInfo.phone    = signup_phone.getText().toString();

        DevLog.d(TAG, memberInfo.toString());

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<JsonObject> call = remoteService.sendSignUpInfo(memberInfo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonElement state = response.body().get("state");

                DevLog.d(TAG, response.body().toString());

                if ( state.toString().equals("\"success\"") ) {
                    goSignActivity();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                DevLog.d(TAG, "FAILURE");
            }
        });
    }

    /**
     * 회원가입을 완료할 수 있는지 확인하는 메서드
     * @return 회원가입이 가능하다면 true를 불가능하다면 false를 반환
     */
    public Boolean isCanDoSignUp() {
        if ( signup_id.getText().toString().length() != 0 &&
                pwConditionChecker(signup_pw) == SATISFIED &&
                twoPwSameChecker(signup_pw2) == SATISFIED &&
                signup_username.getText().toString().length() != 0 &&
                signup_nickname.getText().toString().length() != 0 ) {

            return true;
        }

        return false;
    }

    public void goSignActivity() {
        Toast.makeText(getApplicationContext(), signup_username.getText().toString() + "님 회원가입에 성공하셨습니다.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignUpActivity.this, SignActivity.class);
        startActivity(intent);
        finish();
    }
}