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
import android.widget.Toast;

import kr.palmapps.palmpay_dev_ver4.lib.DevLog;


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

    //EditTexts
    EditText signup_id;
    EditText signup_pw;
    EditText signup_pw2;
    EditText signup_name;
    EditText signup_phone;

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

        setTextChangedListener(signup_pw);
        setTextChangedListener(signup_pw2);

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
                changeAgreeGrantChanger();
                break;
            case R.id.do_sign_up :
                isGrantAgree = isCanDoSignUp();
                if (isGrantAgree) {
                    // 서버와 통신해서 저장하고


                    // MainActivity로 넘어감
                    goMainActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }

                break;
        }
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
    }

    /**
     * Setting EditTexts
     */
    public void setViewEditTexts() {
        signup_id   = (EditText) findViewById(R.id.signup_id);
        signup_pw   = (EditText) findViewById(R.id.signup_pw);
        signup_pw2  = (EditText) findViewById(R.id.signup_pw2);
        signup_name = (EditText) findViewById(R.id.signup_name);
        signup_phone= (EditText) findViewById(R.id.signup_phone);
    }

    public void setViewImageViews() {
        signup_pw_length = (ImageView) findViewById(R.id.signup_pw_length);
        signup_pw_check  = (ImageView) findViewById(R.id.signup_pw_check);
    }

    /**
     * AgreeGrant Changer
     */
    public void changeAgreeGrantChanger() {
        if (isGrantAgree) {
            isGrantAgree = false;
            agree_grant.setImageResource(R.drawable.ic_check_circle_green_24dp);
            midLayout.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);
            signup_id.clearFocus();

            signup_id.setText("");
            signup_pw.setText("");
            signup_pw2.setText("");
            signup_name.setText("");

        } else if (!isGrantAgree) {
            isGrantAgree = true;
            agree_grant.setImageResource(R.drawable.ic_radio_button_unchecked_grey_24dp);
            midLayout.setVisibility(View.INVISIBLE);
            bottomLayout.setVisibility(View.INVISIBLE);
        }
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

    /**
     * 회원가입을 완료할 수 있는지 확인하는 메서드
     * @return 회원가입이 가능하다면 true를 불가능하다면 false를 반환
     */
    public Boolean isCanDoSignUp() {
        if ( signup_id.getText().toString().length() != 0 &&
                pwConditionChecker(signup_pw) == SATISFIED &&
                twoPwSameChecker(signup_pw2) == SATISFIED &&
                signup_name.getText().toString().length() != 0 ) {
            return true;
        }

        return false;
    }

    public void goMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}