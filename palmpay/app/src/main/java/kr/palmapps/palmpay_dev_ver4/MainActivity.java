package kr.palmapps.palmpay_dev_ver4;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import kr.palmapps.palmpay_dev_ver4.Adapter.MenuRecyclerViewAdapter;
import kr.palmapps.palmpay_dev_ver4.Adapter.OrderlistRecyclerViewAdapter;
import kr.palmapps.palmpay_dev_ver4.Adapter.PartnerRecyclerViewAdapter;
import kr.palmapps.palmpay_dev_ver4.Handler.BackPressButtonHandler;
import kr.palmapps.palmpay_dev_ver4.Item.MenuListItem;
import kr.palmapps.palmpay_dev_ver4.Item.OrderListItem;
import kr.palmapps.palmpay_dev_ver4.Item.PartnerListItem;
import kr.palmapps.palmpay_dev_ver4.lib.DevLog;
import kr.palmapps.palmpay_dev_ver4.lib.DevToast;

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
    FloatingActionButton fab;
    DrawerLayout drawer;
    LinearLayout bottomLayout;

    // 버튼들
    Button palm_fast_order;
    Button normal_order;

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

        setViewToolbar();
        setViewFloatingButton();
        setViewDrawer();
        setViewBts();
        setViewLayouts();
        setBackPressButtonHandler();
        setContent(isBeaconDetected);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        if (id == R.id.action_settings) {
            return true;
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
                DevLog.d(TAG, "palm_fast_order");
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
        fab = (FloatingActionButton) findViewById(R.id.fab);
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
            isOpened = false;
            fab.setImageResource(R.drawable.ic_shopping_cart_white_24dp);
            // noworderlist 목록 화면 close
            now_orderlist.setVisibility(View.INVISIBLE);

        } else if (!isOpened) {
            isOpened = true;
            fab.setImageResource(R.drawable.ic_close_white_24dp);
            // noworderlist 목록 화면 오픈
            now_orderlist.setVisibility(View.VISIBLE);
            setOrderlistRecyclerView();
        }
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
            setMenuListRecyclerView();
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
        dev_VersionPartnerList();

        recyclerView_partner = findViewById(R.id.content_main_recycler);
        recyclerView_partner.setHasFixedSize(true);
        layoutManager_partner = new LinearLayoutManager(this);
        recyclerView_partner.setLayoutManager(layoutManager_partner);

        PartnerRecyclerViewAdapter partnerRecyclerView = new PartnerRecyclerViewAdapter(partnerList);
        recyclerView_partner.setAdapter(partnerRecyclerView);
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

    public void setMenuListRecyclerView() {
        dev_VersionMenuList();

        recyclerView_menu = findViewById(R.id.content_main_recycler);
        recyclerView_menu.setLayoutManager(new GridLayoutManager(this, 2));

        MenuRecyclerViewAdapter menuRecyclerViewAdapter = new MenuRecyclerViewAdapter(menuList);
        recyclerView_menu.setAdapter(menuRecyclerViewAdapter);
    }

    /**
     * 서버와 연결하기 전 데이터를 임의로 생성하는 메서드
     */
    public void dev_VersionPartnerList() {
        PartnerListItem items;
        for(int i = 0; i < 9; i++){
            items = new PartnerListItem("상호명" + String.valueOf(i), "카페/식당", "데이터베이스 연결이 되어있지 않은 리스트입니다.");
            partnerList.add(items);
        }
    }

    public void dev_VersionMenuList() {
        MenuListItem items;
        for(int i = 0; i < 5; i++) {
            items = new MenuListItem("메뉴 " + String.valueOf(i), "100", "0");
            menuList.add(items);
        }
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

    /**
     * orderlist 존재 여부에 따라 boolean을 반환
     * 이 메소드는 하단 버튼부(bottomLayout)과 floatingbtn의 visibility를 결정하기 위해 만듦
     *
     * 베타버전에서는 구현하지 않을 부분, UX적인 요소가 강함
     *
     * @param list 확인할 orderlist
     * @return 존재하면 true를 반환하고 존재하지 않으면 false를 반환
     */
    public Boolean isOrderExist(ArrayList<OrderListItem> list) {
        if (list.size() != 0) {
            return true;
        }

        return false;
    }

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
}
