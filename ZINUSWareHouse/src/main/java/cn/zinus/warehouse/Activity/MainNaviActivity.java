package cn.zinus.warehouse.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.deviceapi.exception.ConfigurationException;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.zinus.warehouse.Fragment.Event;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.Fragment.MainMenuFragment;
import cn.zinus.warehouse.Fragment.inventorymgnt.StockTakingFragment;
import cn.zinus.warehouse.Fragment.materialstockin.InboundFragment;
import cn.zinus.warehouse.Fragment.materialstockout.OutboundFragment;
import cn.zinus.warehouse.Fragment.standardmgnt.RegisterTagFragment;
import cn.zinus.warehouse.Fragment.standardmgnt.SettingFragment;
import cn.zinus.warehouse.Fragment.stocksearch.StockSearchFragment;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.BarCode2DHelper;

import static cn.zinus.warehouse.util.Utils.showToast;

public class MainNaviActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //region VAR
    private DrawerLayout drawer_main;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    //Active Fragment
    private Fragment isFragment;
    //Main Menu
    private MainMenuFragment mMainMenuFragment;
    //Standard Mgnt
    private RegisterTagFragment mRegisterTagFragment;
    private SettingFragment mSettingFragment;
    //Material Stock In
    private InboundFragment mInboundFragment;
    //Material Stock Out
    private OutboundFragment mOutBoundFragment;
    //Inventoruy Mgnt
    private StockTakingFragment mStockTakingFragment;
    //Information Search
    private StockSearchFragment mStockSearchFragment;
    //Lot Shipping
  //  private ShippingFragment mShippingFragment;
    public RFIDWithUHF mRFIDWithUHF;
    public Barcode2DWithSoft mBarcode2DWithSoft;
    public BarCode2DHelper barcodeHelper;
    private boolean isMenuSave = false;
    private boolean isMenuTemp = false;
    //endregion

    //region 生命周期
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navi);
        initBarCode();
        initUHF();
        initToolbar();
        initFragment(savedInstanceState);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        EventBus.getDefault().register(MainNaviActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MainNaviActivity.this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ////Log.e("isMenuSave",isMenuSave + "");
        if (isMenuSave) {
            menu.findItem(R.id.action_save).setVisible(true);
        } else {
            menu.findItem(R.id.action_save).setVisible(false);
        }

        if (isMenuTemp) {
            menu.findItem(R.id.action_Temp).setVisible(true);
        } else {
            menu.findItem(R.id.action_Temp).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139) {
            if (event.getRepeatCount() == 0) {
                KeyDownFragment kf = (KeyDownFragment) isFragment;
                kf.myOnKeyDown();
            }
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.getRepeatCount() == 0) {
                KeyDownFragment kf = (KeyDownFragment) isFragment;
                kf.myBackKeyDwon();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 139)  //成为扳机键（139）
        {
            KeyDownFragment kf = (KeyDownFragment) isFragment;
            kf.myOnKeyuUp();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navi, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Set ToolBarName
        String menuName = item.getTitle().toString();
        int id = item.getItemId();
        switchtofragment(id, menuName);
        return true;
    }
    //endregion

    //endregion

    //region RFID&BARCODE 初始化相关
    public void initUHF() {
        try {
            mRFIDWithUHF = RFIDWithUHF.getInstance();
        } catch (ConfigurationException e) {
            showToast(MainNaviActivity.this, e.getMessage(), 0);
            return;
        }
        if (mRFIDWithUHF != null) {
            new InitUHFTask().execute();
        }
    }

    public void freeUHF() {
        if (mRFIDWithUHF != null) {
            mRFIDWithUHF.free();
        }
    }

    public void initBarCode() {
//        barcodeHelper = new BarCode2DHelper(MainNaviActivity.this);
//        barcodeHelper.iniBarCode2D();
        try {
            mBarcode2DWithSoft = Barcode2DWithSoft.getInstance();
        } catch (Exception ex) {
            showToast(MainNaviActivity.this, ex.getMessage(), 0);
            return;
        }
        if (mBarcode2DWithSoft != null) {
            new InitBarCodeTask().execute();
        }
    }

    public void freeBarcode() {
        if (mBarcode2DWithSoft != null) {
            mBarcode2DWithSoft.close();
        }
    }

    /**
     * UHF设备异步上电
     */
    public class InitUHFTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            return mRFIDWithUHF.init();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            mypDialog.cancel();

            if (!result) {
//                Toast.makeText(mcontext, "init UHF fail",
//                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mypDialog = new ProgressDialog(MainNaviActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }

    /**
     * 设备上电异步类
     */
    public class InitBarCodeTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {

            boolean result = false;

            if (mBarcode2DWithSoft != null) {
                result = mBarcode2DWithSoft.open(MainNaviActivity.this);
                if (result) {
//                    mBarcode2DWithSoft.setParameter(9, 0);
                    mBarcode2DWithSoft.setParameter(324, 1);
                    mBarcode2DWithSoft.setParameter(300, 0); // Snapshot Aiming
                    mBarcode2DWithSoft.setParameter(361, 0); // Image Capture Illumination
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            mypDialog.cancel();

            if (!result) {
                showToast(MainNaviActivity.this, "init Barcode fail", 0);
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mypDialog = new ProgressDialog(MainNaviActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }
    //endregion

    //region 初始化页面相关
    public void initToolbar() {
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.MainMenu);                     // 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(toolbar);

        //为了生成，工具栏左上角的动态图标，要使用下面的方法
        drawer_main = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer_main, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerToggle.syncState();
        drawer_main.setDrawerListener(mDrawerToggle);
    }

    public void initFragment(Bundle savedInstanceState) {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (mMainMenuFragment == null) {
                mMainMenuFragment = new MainMenuFragment();
            }
            isFragment = mMainMenuFragment;
            ft.replace(R.id.frame_main, mMainMenuFragment).commit();
        }
    }

    @SuppressLint("RestrictedApi")
    private void switchtofragment(int id, String menuName) {
        getSupportActionBar().setTitle(menuName);
        if (id == R.id.item_MianMenu) {
            if (mMainMenuFragment == null) {
                mMainMenuFragment = new MainMenuFragment();
            }
            switchContent(isFragment, mMainMenuFragment);
        } else if (id == R.id.item_setting) {
            if (mSettingFragment == null) {
                mSettingFragment = new SettingFragment();
            }
            switchContent(isFragment, mSettingFragment);
            isMenuSave = true;
            isMenuTemp = false;
        } else if (id == R.id.item_Receipt) {
            if (mInboundFragment == null) {
                mInboundFragment = new InboundFragment();
            }
            switchContent(isFragment, mInboundFragment);
            isMenuSave = true;
            isMenuTemp = false;
        } else if (id == R.id.item_Export) {
            if (mOutBoundFragment == null) {
                mOutBoundFragment = new OutboundFragment();
            }
            switchContent(isFragment, mOutBoundFragment);
            isMenuSave = true;
            isMenuTemp = false;
        } else if (id == R.id.item_StockCheck) {
            if (mStockTakingFragment == null) {
                mStockTakingFragment = new StockTakingFragment();
            }
            switchContent(isFragment, mStockTakingFragment);
            isMenuSave = true;
            isMenuTemp = false;
        }
//        else if (id == R.id.item_StockSearch) {
//            if (mStockSearchFragment == null) {
//                mStockSearchFragment = new StockSearchFragment();
//            }
//            switchContent(isFragment, mStockSearchFragment);
//            isMenuSave = true;
//            isMenuTemp = true;
//        }
//        else if (id == R.id.item_Shipping) {
//            if (mShippingFragment == null) {
//                mShippingFragment = new ShippingFragment();
//            }
//            switchContent(isFragment, mShippingFragment);
//            isMenuSave = true;
//            isMenuTemp = true;
//        }
//        else if (id == R.id.item_registtag) {
//            if (mRegisterTagFragment == null) {
//                mRegisterTagFragment = new RegisterTagFragment();
//            }
//            switchContent(isFragment, mRegisterTagFragment);
//            isMenuSave = true;
//            isMenuTemp = true;
//        }
        invalidateOptionsMenu();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void switchContent(Fragment from, Fragment to) {
        if (isFragment != to) {
            isFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                ft.hide(from).add(R.id.frame_main, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
    //endregion

    //region EventBus

    @Subscribe
    public void onEventMainThread(Event.ToMainMenuEvent event) {
        getSupportActionBar().setTitle(getString(R.string.MainMenu));
        switchContent(isFragment, mMainMenuFragment);
    }

    @Subscribe
    public void onEventMainThread(Event.ToFragmentEvent event) {
        switchtofragment(event.getfragmentID(), event.getfragmentName());
    }

    @Subscribe
    public void onEventMainThread(Event.InboundClearOrderItemEvent event) {
        mInboundFragment.mConsumeInboundFragment.actionClearAll();
        mInboundFragment.mConsumeLotInboundFragment.actionClearAll();
    }

    @Subscribe
    public void onEventMainThread(Event.OutboundClearOrderItemEvent event) {
        mOutBoundFragment.mConsumeOutboundFragment.actionClearAll();
        mOutBoundFragment.mConsumeLotOutboundFragment.actionClearAll();
    }

    @Subscribe
    public void onEventMainThread(Event.StockCheckClearOrderItemEvent event) {
        mStockTakingFragment.mStockCheckDetailFragment.actionClearAll();
        mStockTakingFragment.mStockLotCheckDetailFragment.actionClearAll();
    }

    @Subscribe
    public void onEventMainThread(Event.ShippingClearLotShippingItemEvent event) {
//        mShippingFragment.mLotShippingFragment.actionClearAll();
    }


    //多国语切换
    @Subscribe
    public void onEventMainThread(Event.ChangeLanguageEvent event) {
        //changeAppLanguage();
        //刷新界面
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    //查询ConsumeInbound
    @Subscribe
    public void onEventMainThread(Event.ConsumeInboundByOrderEvent event) {
        mInboundFragment.mConsumeInboundFragment.actionClearAll();
        mInboundFragment.mConsumeInboundFragment.getConsumeInboundByInboundOrder(event.getInboundOrderNo());
//        mInboundFragment.mConsumeLotInboundFragment.actionClearAll();
//        mInboundFragment.mConsumeLotInboundFragment.getConsumeLotInboundByConsumeDefID(event.getInboundOrderNo());
        mInboundFragment.jump(1);
    }

    //查询ConsumeLotInbound
    @Subscribe
    public void onEventMainThread(Event.ConsumeLotInboundByConsumeDefIDEvent event) {
        mInboundFragment.mConsumeLotInboundFragment.actionClearAll();
        mInboundFragment.mConsumeLotInboundFragment.getConsumeLotInboundByConsumeDefID(event.getConsumeInboundData());
        mInboundFragment.jump(2);
    }

    //ConsumeLotInbound数据变化之后，修改ConsumeInbound的数据
    @Subscribe
    public void onEventMainThread(Event.ConsumeInboundbyLotCheckEvent event) {
        mInboundFragment.mConsumeInboundFragment.updaCheckQtyByLot(event.getConsumeLotInboundData(),event.getSumqty());
    }

    //查询ConsumeOutbound
    @Subscribe
    public void onEventMainThread(Event.ConsumeOutboundByConsumeRequestEvent event) {
        mOutBoundFragment.mConsumeOutboundFragment.actionClearAll();
        mOutBoundFragment.mConsumeOutboundFragment.getConsumeOutboundByConsumeRequest(event.getConsumeRequestNo());
        mOutBoundFragment.mConsumeLotOutboundFragment.actionClearAll();
        //mOutBoundFragment.mConsumeLotOutboundFragment.getConsumeLotOutboundByConsumeRequest(event.getConsumeRequestNo());
        mOutBoundFragment.jump(1);
    }

    //查询ConsumeLotOutbound
    @Subscribe
    public void onEventMainThread(Event.ConsumeLotOutboundByConsumeDefIDEvent event) {
        mOutBoundFragment.mConsumeLotOutboundFragment.actionClearAll();
        mOutBoundFragment.mConsumeLotOutboundFragment.getConsumeLotOutboundByConsumeDefID(event.getConsumeOutboundData());
        mOutBoundFragment.jump(2);
    }

    //查询StockCheck
    @Subscribe
    public void onEventMainThread(Event.StockCheckDetailbyCheckMonthEvent event) {
        mStockTakingFragment.mStockCheckDetailFragment.actionClearAll();
        mStockTakingFragment.mStockCheckDetailFragment.getStockCheckDetailbyCheckMonth(event.getStockCheck());

        mStockTakingFragment.jump(1);
    }


    //查询LotStockCheck
    @Subscribe
    public void onEventMainThread(Event.StockLotCheckDetailbyConsumedefidEvent event) {
        mStockTakingFragment.mStockLotCheckDetailFragment.actionClearAll();
        mStockTakingFragment.mStockLotCheckDetailFragment.getStockLotCheckDetailbystockCheckDeatilData(event.getStockCheckDeatilData());
        mStockTakingFragment.jump(2);
    }

    //stockcheckLotdetail数据变化之后，修改stockcheckdetail的数据
    @Subscribe
    public void onEventMainThread(Event.StockCheckDetailbyLotCheckEvent event) {
        mStockTakingFragment.mStockCheckDetailFragment.updaCheckQtyByLot(event.getStockLotCheckDeatilData());
    }

    //刷新界面
    @Subscribe
    public void onEventMainThread(Event.RefreshActivityEvent event) {
        //刷新界面
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //endregion

}