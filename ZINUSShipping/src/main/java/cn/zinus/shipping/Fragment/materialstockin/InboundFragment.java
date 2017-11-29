package cn.zinus.shipping.Fragment.materialstockin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.micube.control.controlUtil.MyNoSlideViewPager;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Adapter.MyFragmentPagerAdapter;
import cn.zinus.shipping.Config.AppConfig;
import cn.zinus.shipping.Fragment.Event;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;

/**
 * Tab1Fragment
 * Created by Spring on 2017/2/18.
 */

public class InboundFragment extends KeyDownFragment {
    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式
    public MyFragmentPagerAdapter mViewPagerAdapter;
    public MyNoSlideViewPager viewPager;
    protected List<KeyDownFragment> lstFrg = new ArrayList<KeyDownFragment>();
    protected List<String> lstTitles = new ArrayList<String>();
    public InboundOrderFragment mOutboundOrderFragment;
    public ConsumeInboundFragment mConsumeInboundFragment;
    public ConsumeLotInboundFragment mConsumeLotInboundFragment;
    private MainNaviActivity mContext;
    TabLayout tabLayout;
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
        return inflater.inflate(R.layout.fragment_inbound, container, false);
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
    //endregion\

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
                mConsumeInboundFragment.actionClearAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
    //endregion

    //region  ◆ Function
    //region initTabs
    private void initTabs() {
        tabLayout = (TabLayout) getView().findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(mContext,
                R.drawable.divider)); //设置分割线的样式
        linearLayout.setDividerPadding(10); //设置分割线间隔
    }
    //endregion

    //region initViewPager
    private void initViewPager() {
        viewPager = (MyNoSlideViewPager) getView().findViewById(R.id.viewpager);
        mViewPagerAdapter = new MyFragmentPagerAdapter(mContext.getSupportFragmentManager(), lstFrg, lstTitles);
        //当viewpager超过2个的时候,要设置这个,才会避免第二个以后的页面重复创建
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mViewPagerAdapter);
    }
    //endregion

    //region initViewPageData
    private void initViewPageData() {
        lstFrg.add(mOutboundOrderFragment);
        lstFrg.add(mConsumeInboundFragment);
        lstFrg.add(mConsumeLotInboundFragment);
        lstTitles.add(getString(R.string.Order));
        lstTitles.add(getString(R.string.Item));
        lstTitles.add(getString(R.string.consumablelot));
    }
    //endregion

    //region jump to other viewpager
    public void jump(int page) {
        viewPager.setCurrentItem(page);
    }
    //endregion

    //region initview
    private void initview() {
        mOutboundOrderFragment = new InboundOrderFragment();
        mConsumeInboundFragment = new ConsumeInboundFragment();
        mConsumeLotInboundFragment = new ConsumeLotInboundFragment();
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
            mConsumeInboundFragment.myOnKeyDown();
        } else if (viewPager.getCurrentItem() == 2) {
            mConsumeLotInboundFragment.myOnKeyDown();
        }
        super.myOnKeyDown();
    }
    //endregion


    @Override
    public void myOnKeyuUp() {
        if (viewPager.getCurrentItem() == 1) {
            mConsumeInboundFragment.myOnKeyuUp();
        } else if (viewPager.getCurrentItem() == 2) {
            mConsumeLotInboundFragment.myOnKeyuUp();
        }
        super.myOnKeyuUp();
    }

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
                        mConsumeInboundFragment.actionClearAll();
                        EventBus.getDefault().post(new Event.ToMainMenuEvent());
                    }
                }).
                setNegativeButton(getString(R.string.NoSave), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        freeUHF(mRFIDWithUHF);
                        freeBarcode(mBarcode2DWithSoft);
                        mConsumeInboundFragment.actionClearAll();
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