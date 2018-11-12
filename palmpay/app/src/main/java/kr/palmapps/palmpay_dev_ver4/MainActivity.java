package kr.palmapps.palmpay_dev_ver4;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import kr.palmapps.palmpay_dev_ver4.Adapter.MenuRecyclerViewAdapter;
import kr.palmapps.palmpay_dev_ver4.Adapter.OrderlistRecyclerViewAdapter;
import kr.palmapps.palmpay_dev_ver4.Adapter.PartnerRecyclerViewAdapter;
import kr.palmapps.palmpay_dev_ver4.Handler.BackPressButtonHandler;
import kr.palmapps.palmpay_dev_ver4.Item.MenuListItem;
import kr.palmapps.palmpay_dev_ver4.Item.OrderListItem;
import kr.palmapps.palmpay_dev_ver4.Item.PartnerListItem;
import kr.palmapps.palmpay_dev_ver4.Remote.RemoteService;
import kr.palmapps.palmpay_dev_ver4.Remote.ServiceGenerator;
import kr.palmapps.palmpay_dev_ver4.lib.DevLog;
import kr.palmapps.palmpay_dev_ver4.lib.DevToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 어플리케이션의 메인화면
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    public Boolean isOpened = false;
    public Boolean isBeaconDetected = true;

    // 화면 요소들
    Toolbar toolbar;
    DrawerLayout drawer;
    LinearLayout bottomLayout;
    NavigationView navigationView;
    View headerLayout;

    // 버튼들
    Button palm_fast_order;
    Button normal_order;
    ImageButton fab;

    // nav_header_main textview들
    TextView signin_email;
    TextView signin_username;
    TextView signin_nickname;

    // 뒤로가기 한번에 종료되는거 방지
    BackPressButtonHandler backPressButtonHandler;

    // Layout
    RelativeLayout now_orderlist;

    RecyclerView content_main_recycler;

    // RecyclerView
    RecyclerView recyclerView_partner;
    RecyclerView.LayoutManager layoutManager_partner;

    RecyclerView recyclerView_order;
    RecyclerView.LayoutManager layoutManager_order;

    RecyclerView recyclerView_menu;
    RecyclerView.LayoutManager layoutManager_menu;

    ArrayList<PartnerListItem> partnerList = new ArrayList<>();
    ArrayList<MenuListItem> menuList = new ArrayList<>();

    public static ArrayList<OrderListItem> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = getIntent();
//        isBeaconDetected = Boolean.parseBoolean(intent.getStringExtra("isBeaconDetected"));
//        DevLog.d(TAG, intent.getStringExtra("isBeaconDetected"));

//        dev_isBeaconDetectedController();
        setViewToolbar();
        setViewFloatingButton();
        setViewDrawer();
        setViewBts();
        setViewLayouts();
        setBackPressButtonHandler();
        setContent(isBeaconDetected);
        setNavigationView();
        setNavHeaders();

        initVisibilities();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(isOpened){
            // 장바구니 목록이 열려있으면 장바구니 목록을 닫음
            isOpened = false;
            fab.setImageResource(R.drawable.ic_shopping_cart_white_24dp);
            DevToast.s(this, "noworderlist close");
            // noworderlist 목록 화면 클로스
            now_orderlist.setVisibility(View.INVISIBLE);

        } else {
            //super.onBackPressed();
            backPressButtonHandler.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();

            editor.commit();

            goSignActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_coupon) {
            goCoupon();

        } else if (id == R.id.nav_orderedlist) {
            goMyOrdered();

        } else if (id == R.id.nav_info) {
            goPalmpayInfo();

        } else if (id == R.id.nav_contact) {
            goContactUs();

        } else if (id == R.id.nav_team) {
            goTeamJoin();

        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.palm_fast_order:
                if(orderList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "주문할 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    DevLog.d(TAG, "palm_fast_order");
                    JsonArray orderList = orderListToJsonArray();
                    sendUserOrderList(orderList);
                }
                break;
            case R.id.normal_order:
                // 프로토타입 테스트 버전에서는 지원하지 않는 기능
                Toast.makeText(getApplicationContext(), "서비스 준비중 입니다.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * layout 세팅 메서드
     */
    public void setViewLayouts() {
        now_orderlist = (RelativeLayout) findViewById(R.id.now_orderlist);
        content_main_recycler = (RecyclerView) findViewById(R.id.content_main_recycler);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
    }

    /**
     * Toolbar 세팅 메서드
     */
    public void setViewToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    /**
     * FloatingButton 세팅 메서드
     */
    public void setViewFloatingButton() {
        fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButtonAction();
            }
        });
    }

    /**
     * FloatingButton 의 동작을 설정하는 메서드
     * Boolean isOpened 변수가 false 이면 isOpened를 true로 바꾸고 noworderlist 프래그먼트를 열고 아이콘을 X 모양으로 바꿈
     * Boolean isOpened 변수가 true 이면 isOpened를 false로 바꾸고 noworderlist 프래그먼트를 닫고 아이콘을 장바구니 모양으로 바꿈
     */
    public void floatingButtonAction() {
         if(isOpened) {
             floatingButtonClose();
         } else {
             floatingButtonOpen();
         }
    }

    public void floatingButtonClose() {
        isOpened = false;
        fab.setImageResource(R.drawable.ic_shopping_cart_white_24dp);
        // noworderlist 목록 화면 close
        now_orderlist.setVisibility(View.INVISIBLE);

    }

    public void floatingButtonOpen() {
        isOpened = true;
        fab.setImageResource(R.drawable.ic_close_white_24dp);
        // noworderlist 목록 화면 오픈
        now_orderlist.setVisibility(View.VISIBLE);
        setOrderlistRecyclerView();
    }

    /**
     * Drawer 세팅 메서드
     */
    public void setViewDrawer(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Button 세팅 메서드
     */
    public void setViewBts() {
        palm_fast_order = findViewById(R.id.palm_fast_order);
        palm_fast_order.setOnClickListener(this);
        normal_order = findViewById(R.id.normal_order);
        normal_order.setOnClickListener(this);
    }

    public void setNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerLayout = navigationView.getHeaderView(0);
    }

    /**
     * content 안에 표시할 내용을 정의하는 메서드
     * isDetected 의 상태에 따라 표시하는 리스트가 달라짐
     * false 일 경우 PartnerRecyclerView 가
     * true 일 경우 MenuRecyclerView 가 표시됨
     * @param bool isDetected 가 들어감
     */
    public void setContent(Boolean bool) {
        if (bool) {
            // beacon detected
            setToolBarString("@상호명");
            initMenuListRecyclerView();
            controlButtonsTransparent(true);
            setContentMainHeight(true);

        } else {
            // beacon undetected
            setToolBarString("PALM PAY HOME");
            setPartnerRecyclerView();
            controlButtonsTransparent(bool);
            setContentMainHeight(bool);
        }
    }

    /**
     * init visibility layout들 속성에 관계없이 시작할 때 visibility 가 false 인 요소들은
     * 모두 invisible 로 변경하고 시작함
     */
    public void initVisibilities() {
        // Layout 설정에 상관없이 시작은 Invisible
        now_orderlist.setVisibility(View.INVISIBLE);
//        bottomLayout.setVisibility(View.INVISIBLE);
//        fab.setVisibility(View.INVISIBLE);
    }

    /**
     * PartnerRecyclerView를 세팅하는 메서드
     */
    public void setPartnerRecyclerView() {
        getAllPartnersList();
//        dev_VersionPartnerList();

        recyclerView_partner = findViewById(R.id.content_main_recycler);
        recyclerView_partner.setHasFixedSize(true);

        layoutManager_partner = new LinearLayoutManager(this);
        recyclerView_partner.setLayoutManager(layoutManager_partner);
    }

    /**
     * 서버와 통신해서 PartnersList를 받아오는 메서드
     */
    public void getAllPartnersList() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<JsonArray> call = remoteService.getAllPartnersList();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                jsonArraytoPartnerList(jsonArray);

                partnerRVAdapterSetter(partnerList);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.checknetwork, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 서버의 응답을 받고 화면을 그리기 위해 만든 메서드
     * @param partnerList 무조건 partnerList 가 들어간다
     */
    public void partnerRVAdapterSetter(ArrayList<PartnerListItem> partnerList) {
        PartnerRecyclerViewAdapter pRV = new PartnerRecyclerViewAdapter(partnerList);
        recyclerView_partner.setAdapter(pRV);
    }

    /**
     * 서버에서 응답으로 받은 jsonArray 형식의 데이터를 ArrayList<PartnerListItem> 에 변환해서
     * ArrayList element 로 삽입하는 메서드
     * @param jsonArray ArrayList 에 넣을 jsonArray 데이터
     */
    public void jsonArraytoPartnerList(JsonArray jsonArray) {
        PartnerListItem items;

        for (int i = 0; i < jsonArray.size(); i++ ) {
            JsonObject tmpObject = jsonArray.get(i).getAsJsonObject();

            String partner_name = tmpObject.get("store_name").getAsString();
            String partner_type = tmpObject.get("store_type").getAsString();
            String partner_desc = tmpObject.get("desc").getAsString();
            DevLog.d(TAG, partner_name + " " + partner_type + " " + partner_desc);

            items = new PartnerListItem(partner_name, partner_type, partner_desc);
            DevLog.d(TAG, items.toString());

            partnerList.add(items);
            DevLog.d(TAG, partnerList.toString());
        }
    }

    /**
     * 서버에 주문을 요청하는 메서드
     *
     * @param jsonArray
     */
    public void sendUserOrderList(JsonArray jsonArray) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<JsonObject> call = remoteService.sendUserOrderList(jsonArray);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonElement result = response.body().get("state");

                if (result.getAsString().equals("success")) {
                    getResultOfSendOrderList();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.checknetwork, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 서버로 보낼 request 를 위해 ArrayList<OrderListItem> 을 JsonArray 형식으로 변환하는 메서드
     * @return jsonArray 형식으로 변환한 orderList 객체
     */
    public JsonArray orderListToJsonArray() {

        JsonArray jsonArray = new JsonArray();

        for ( int i = 0; i < orderList.size(); i++ ) {
            JsonObject jsonObject = new JsonObject();

            String order_name = orderList.get(i).getOrder_name();
            String order_count = orderList.get(i).getOrder_count();

            SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
            String email = sharedPreferences.getString("email", null);

            jsonObject.addProperty("order_name", order_name);
            jsonObject.addProperty("order_count", order_count);
            jsonObject.addProperty("paytype", "palmcredit");
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("orderee", dev_getBeaconId());

            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    /**
     * sendUserOrderList request 가 서버에 정상적으로 요청되고 response 를 정상적으로 받을 경우 실행되는 메서드
     * Toast 메시지를 띠워주고 orderList 를 비운다.
     */
    public void getResultOfSendOrderList() {
        Toast.makeText(getApplicationContext(), "주문이 성공적으로 완료되었습니다.", Toast.LENGTH_LONG).show();
        orderList.clear();
        floatingButtonClose();
        setMenuListRecyclerView();
        goMyOrdered();
    }

    public void setOrderlistRecyclerView() {
        recyclerView_order = findViewById(R.id.now_orderlist_recycler);
        recyclerView_order.setHasFixedSize(true);

        layoutManager_order = new LinearLayoutManager(this);
        recyclerView_order.setLayoutManager(layoutManager_order);

        OrderlistRecyclerViewAdapter orderlistRecyclerViewAdapter = new OrderlistRecyclerViewAdapter(orderList);
        recyclerView_order.setAdapter(orderlistRecyclerViewAdapter);

        orderlistRecyclerViewAdapter.notifyDataSetChanged();

    }

    public void getAllMenusList() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<JsonArray> call = remoteService.getAllMenusList(dev_getBeaconId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                jsonArraytoMenuList(jsonArray);

                menuListRVAdapterSetter(menuList);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.checknetwork, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void jsonArraytoMenuList(JsonArray jsonArray) {
        MenuListItem items;

        for (int i = 0; i < jsonArray.size(); i++ ) {
            JsonObject tmpObject = jsonArray.get(i).getAsJsonObject();

            String menu_name = tmpObject.get("menu_name").getAsString();
            String menu_price = tmpObject.get("menu_price").getAsString();
            DevLog.d(TAG, menu_name + " " + menu_price);

            items = new MenuListItem(menu_name, menu_price);
            DevLog.d(TAG, items.toString());

            menuList.add(items);
            DevLog.d(TAG, menuList.toString());
        }
    }

    /**
     * 구현 이전에 비콘 id 전달하는 메서드
     * @return (String) 2 - 앤댄썸
     */
    public String dev_getBeaconId() {
        return "2";
    }

    public void initMenuListRecyclerView() {
        getAllMenusList();

        recyclerView_menu = findViewById(R.id.content_main_recycler);
        recyclerView_menu.setLayoutManager(new GridLayoutManager(this, 2));
    }

    /**
     * RecyclerView 렌더링을 다시하기 위한 메서드
     */
    public void setMenuListRecyclerView() {
        recyclerView_menu = findViewById(R.id.content_main_recycler);
        recyclerView_menu.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void menuListRVAdapterSetter(ArrayList<MenuListItem> menuList) {
        MenuRecyclerViewAdapter mRV = new MenuRecyclerViewAdapter(menuList);
        recyclerView_menu.setAdapter(mRV);
    }

    /**
     * button 부분을 보이게 할지 말지 결정하는 메서드
     * button 부분은 장바구니에 무언가 담겨있을 때 생성된다.
     * @param bool true일 경우 보이고, false 일 경우 보이지 않는다
     */
    public void controlButtonsTransparent(Boolean bool) {
        if (bool) {
            fab.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
            bottomLayout.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * param 값에 따라 RecyclerView 의 표시 영역을 바꿔주는 메서드
     * @param bool false 일 경우 아래 버튼부가 없으므로 하단을 채우고, true 일 경우 버튼부를 표시해야하므로 하단부 bottomLayout 위로 올린다.
     */
    public void setContentMainHeight(Boolean bool) {
        if (!bool) {
            DevLog.d(TAG, "SET CONTENT MAIN HEIGHT MATCH PARENT");
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) content_main_recycler.getLayoutParams();
            params.removeRule(RelativeLayout.ABOVE);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) content_main_recycler.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.bottomLayout);
        }
    }

//    /**
//     * orderlist 존재 여부에 따라 boolean을 반환
//     * 이 메소드는 하단 버튼부(bottomLayout)과 floatingbtn의 visibility를 결정하기 위해 만듦
//     *
//     * 베타버전에서는 구현하지 않을 부분, UX적인 요소가 강함
//     *
//     * @param list 확인할 orderlist
//     * @return 존재하면 true를 반환하고 존재하지 않으면 false를 반환
//     */
//    public Boolean isOrderExist(ArrayList<OrderListItem> list) {
//        if (list.size() != 0) {
//            return true;
//        }
//
//        return false;
//    }

    /**
     * ActionBar 세팅 메서드
     * isBeaconDetect 가 false 일 경우는 PLAM PAY HOME 이 param 으로 들어가고
     * isBeaconDetect 가 true 일 경우는 @상호명을 DB에서 받아와 param 으로 들어감
     * @param string isBeaconDetect에 따라 달라지는 string
     */
    public void setToolBarString(String string) {
        getSupportActionBar().setTitle(string);
    }

    /**
     * 뒤로가기 버튼 세팅 메서드
     */
    public void setBackPressButtonHandler() {
        backPressButtonHandler = new BackPressButtonHandler(this);
    }

    public void dev_isBeaconDetectedController(/*final Intent intent*/) {
        // 비콘 인식 구현 이전에 루틴 컨트롤을 위해
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("DEV MODE")
                .setMessage("isBeaconDetected Controller")
                .setCancelable(false)
                .setPositiveButton("true", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isBeaconDetected = true;

//                        intent.putExtra("isBeaconDetected", String.valueOf(isBeaconDetected));
//                        startActivity(intent);

                        finish();
                    }
                })
                .setNegativeButton("false", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isBeaconDetected = false;

//                        intent.putExtra("isBeaconDetected", String.valueOf(isBeaconDetected));
//                        startActivity(intent);

                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setNavHeaders() {
        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);

        String email = sharedPreferences.getString("email", null);
        String username = sharedPreferences.getString("username", null);
        String nickname = sharedPreferences.getString("nickname", null);

        signin_email = (TextView) headerLayout.findViewById(R.id.signin_email);
        signin_email.setText(email);
        signin_username = (TextView) headerLayout.findViewById(R.id.signin_username);
        signin_username.setText(username);
        signin_nickname = (TextView) headerLayout.findViewById(R.id.signin_nickname);
        signin_nickname.setText(nickname);
    }

    public void goCoupon() {
        Intent intent = new Intent(MainActivity.this, CouponActivity.class);
        startActivity(intent);
    }

    public void goMyOrdered() {
        Intent intent = new Intent(MainActivity.this, MyOrderedActivity.class);
        startActivity(intent);
    }

    public void goTeamJoin() {
        Intent intent = new Intent(MainActivity.this, TeamJoinActivity.class);
        startActivity(intent);
    }

    public void goContactUs() {
        Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
        startActivity(intent);
    }

    public void goPalmpayInfo() {
        Intent intent = new Intent(MainActivity.this, PalmpayInfoActivity.class);
        startActivity(intent);
    }

    public void goSignActivity() {
        Intent intent = new Intent(MainActivity.this, SignActivity.class);
        startActivity(intent);

        Toast.makeText(getApplicationContext(), "로그아웃 되었습니다", Toast.LENGTH_LONG).show();

        finish();
    }
}
