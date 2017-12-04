package cn.zinus.warehouse.Fragment.stocksearch;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.micube.control.controlUtil.MyNoSlideViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.MyFragmentPagerAdapter;
import cn.zinus.warehouse.Config.AppConfig;
import cn.zinus.warehouse.Fragment.Event;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;

/**
 * Developer:Spring
 * DataTime :2017/9/27 13:32
 * Main Change:
 */

public class StockSearchFragment extends KeyDownFragment {
    //region ◆ 변수(Variables)
    public MyFragmentPagerAdapter mViewPagerAdapter;
    public MyNoSlideViewPager viewPager;
    protected List<KeyDownFragment> lstFrg = new ArrayList<KeyDownFragment>();
    protected List<String> lstTitles = new ArrayList<String>();
    public StockConsumeSearchFragment mStockConsumeSearchFragment;
    public StockLotConsumeSearchFragment mStockLotConsumeSearchFragment;
    private MainNaviActivity mContext;
    private String UserID;
    private ProgressDialog mypDialog;
    //endregion

    //region ◆ 생성자(Creator)
    //region onCreate
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = (MainNaviActivity) getActivity();
        UserID = AppConfig.getInstance().getString(Constant.UserID, null);
    }
    //endregion

    //region onDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //endregion

    //region onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stocksearch, container, false);
    }
    //endregion

    //region onActivityCreated
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
        initViewPageData();
        initViewPager();
        initTabs();
    }
    //endregion

    //region onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                //save的业务代码,TestRule
                if (judgeSave()) {
                    save();
                } else {
                    AlertDialog prompt = new AlertDialog.Builder(mContext).
                            setTitle(getString(R.string.prompt)).
                            setMessage(getString(R.string.errorQty)).
                            setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).create();
                    // 显示对话框
                    prompt.show();
                }
                break;
            case R.id.action_ClearAll:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
    //endregion

    //region  ◆ Function
    //region initTabs
    private void initTabs() {
        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.stocksearch_tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }
    //endregion

    //region initViewPager
    private void initViewPager() {
        viewPager = (MyNoSlideViewPager) getView().findViewById(R.id.stocksearch_viewpager);
        mViewPagerAdapter = new MyFragmentPagerAdapter(mContext.getSupportFragmentManager(), lstFrg, lstTitles);
        //当viewpager超过2个的时候,要设置这个,才会避免第二个以后的页面重复创建
        //viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(mViewPagerAdapter);
    }
    //endregion

    //region initViewPageData
    private void initViewPageData() {
        lstFrg.add(mStockConsumeSearchFragment);
        lstFrg.add(mStockLotConsumeSearchFragment);
        lstTitles.add(getString(R.string.Item));
        lstTitles.add(getString(R.string.consumablelot));
    }
    //endregion

    //region initview
    private void initview() {
        mStockConsumeSearchFragment = new StockConsumeSearchFragment();
        mStockLotConsumeSearchFragment = new StockLotConsumeSearchFragment();
    }
    //endregion

    //region judgeSave
    private boolean judgeSave() {
        boolean returnflag = true;
        return returnflag;
    }
    //endregion

    //region save
    private void save() {
    }
    //endregion

    //endregion

    //region ◆ extends
    //region myOnKeyDown
    @Override
    public void myOnKeyDown() {
        //当前页面是Item的时候才触发
        if (viewPager.getCurrentItem() == 1) {
            mStockConsumeSearchFragment.myOnKeyDown();
        } else if (viewPager.getCurrentItem() == 2) {
            mStockLotConsumeSearchFragment.myOnKeyDown();
        }
        super.myOnKeyDown();
    }
    //endregion

    //region myBackKeyDwon
    @Override
    public void myBackKeyDwon() {
        AlertDialog isExit = new AlertDialog.Builder(mContext).
                setTitle(getString(R.string.prompt)).
                setMessage(getString(R.string.SaveAndExit)).
                setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save();
                        freeUHF(mRFIDWithUHF);
                        freeBarcode(mBarcode2DWithSoft);

                        EventBus.getDefault().post(new Event.ToMainMenuEvent());
                    }
                }).
                setNegativeButton(getString(R.string.NoSave), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        freeUHF(mRFIDWithUHF);
                        freeBarcode(mBarcode2DWithSoft);

                        EventBus.getDefault().post(new Event.ToMainMenuEvent());
                    }
                }).
                setNeutralButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        // 显示对话框
        isExit.show();

    }
    //endregion
    //endregion

}