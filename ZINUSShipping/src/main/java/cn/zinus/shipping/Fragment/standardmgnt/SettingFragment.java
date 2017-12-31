package cn.zinus.shipping.Fragment.standardmgnt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.zinus.shipping.Activity.LoginActivity;
import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Config.AppConfig;
import cn.zinus.shipping.Fragment.Event;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;
import cn.zinus.shipping.util.SoundUtil;

import static cn.zinus.shipping.util.Utils.showToast;

/**
 * 1:原先的设置Power功能
 * 2:增加了临时按钮,作为通信的Demo作用
 * Created by Spring on 2017/7/04.
 */

public class SettingFragment extends KeyDownFragment implements View.OnClickListener {

    //region VAR
    MainNaviActivity mContext;
    //Volume
    public SeekBar soundBar_System;
    public AudioManager audiomanage;
    private TextView mVolume_System;
    private int maxVolume_System, currentVolume_System;
    //Change IP
    private TextView mTvIP;
    private Button mBtnChangeIP;
    //Antenna
    private int arrPow; //输出功率
    private Spinner spPower;
    private Button mbtnSetPower;
    private Button mbtnGetPower;
    //Exit
    private Button mbtnExitAccount;
    //语言选择
    private int yourChoice;
    //sqlite
    MyDateBaseHelper mHelper;
    private SQLiteDatabase db;
    //endregion

    //region 系统方法
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainNaviActivity) getActivity();
        initView();
        initListener();
        //initSharedPreferences();
        //handlerFunction();
        mHelper = DBManger.getIntance(mContext);
        db = mHelper.getWritableDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("生命周期", "onResume");
        //region Volume_System
        maxVolume_System = audiomanage.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);  //获取系统最大音量
        soundBar_System.setMax(maxVolume_System);   //拖动条最高值与系统最大声匹配
        currentVolume_System = audiomanage.getStreamVolume(AudioManager.STREAM_SYSTEM);  //获取当前值
        audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        soundBar_System.setProgress(currentVolume_System);
        mVolume_System.setText(currentVolume_System * 100 / maxVolume_System + " %");
        soundBar_System.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audiomanage.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, 0);
                currentVolume_System = audiomanage.getStreamVolume(AudioManager.STREAM_SYSTEM);  //获取当前值
                soundBar_System.setProgress(currentVolume_System);
                mVolume_System.setText(currentVolume_System * 100 / maxVolume_System + " %");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SoundUtil.play(R.raw.pegconn, 0);
            }
        });
        //endregion

        String ver = mContext.mRFIDWithUHF.getHardwareType();
        arrPow = R.array.arrayPower;
        if (ver != null && ver.contains("RLM")) {
            arrPow = R.array.arrayPower2;
        }
        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, arrPow, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPower.setAdapter(adapter);

//        //region Antenna
//        String ver = mContext.mRFIDWithUHF.getHardwareType();
//        Resources resources =getResources();
//        arrPow=resources.getStringArray(R.array.arrayPower);
//        if (ver != null && ver.contains("RLM")) {
//            arrPow=resources.getStringArray(R.array.arrayPower2);
//        }
//        maxAntenna =Integer.parseInt(arrPow[arrPow.length-1]);
//        //Log.e("1111111111max",maxAntenna+"");
//        minAntenna =Integer.parseInt(arrPow[0]);
//        //Log.e("1111111111max",minAntenna+"");
//        msbAntenna.setMax(maxAntenna);   //拖动条最高值与系统最大声匹配
//        currentAntenna = mContext.mRFIDWithUHF.getPower();
//        //Log.e("1111111111curr",currentAntenna+"");
//       // currentVolume_System = audiomanage.getStreamVolume(AudioManager.STREAM_SYSTEM);  //获取当前值
//        msbAntenna.setProgress(currentAntenna);
//        mtvsbAntenna.setText(currentAntenna);
//        msbAntenna.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                currentVolume_System = audiomanage.getStreamVolume(AudioManager.STREAM_SYSTEM);  //获取当前值
//                msbAntenna.setProgress(currentVolume_System);
//                mtvsbAntenna.setText(currentVolume_System * 100 / maxVolume_System + " %");
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//        //endregion
    }

    @Override
    public void myBackKeyDwon() {
        AlertDialog isExit = new AlertDialog.Builder(mContext).
                setTitle("系统提示").
                setMessage("退出吗").
                setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        freeUHF(mRFIDWithUHF);
                        freeBarcode(mBarcode2DWithSoft);
                        EventBus.getDefault().post(new Event.ToMainMenuEvent());
                    }
                }).
                setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        // 显示对话框
        isExit.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSetPower:
                setPower();
                break;
            case R.id.btnGetPower:
                getPower();
                break;
            case R.id.btnChangeIP:
                //changeIP(mTvIP.getText().toString());
                cleanDB();
                break;
            case R.id.btnExit:
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                mContext.freeBarcode();
                mContext.freeUHF();
                mContext.finish();
                break;
        }
    }

    private void cleanDB() {
        String DeleteTableLOTSHIPPING = "DELETE  FROM " + Constant.SF_SHIPPINGLOT;
        SQLiteStatement statementLOTSHIPPING = db.compileStatement(DeleteTableLOTSHIPPING);
        try {
            statementLOTSHIPPING.executeInsert();
        } catch (Exception e) {
            Log.e("SF_LOTSHIPPING保存出错", e.getMessage().toString());
        }

        String DeleteTableLOT = "DELETE  FROM " + Constant.SF_LOT;
        SQLiteStatement statementLOT = db.compileStatement(DeleteTableLOT);
        try {
            statementLOT.executeInsert();
        } catch (Exception e) {
            Log.e("SF_LOT保存出错", e.getMessage().toString());
        }

        String DeleteTableSHIPPINGPLAN = "DELETE  FROM " + Constant.SF_SHIPPINGPLAN;
        SQLiteStatement statementSHIPPINGPLAN = db.compileStatement(DeleteTableSHIPPINGPLAN);
        try {
            statementSHIPPINGPLAN.executeInsert();
        } catch (Exception e) {
            Log.e("SF_SHIPPINGPLAN保存出错", e.getMessage().toString());
        }
    }

    private void CopySqliteFileFromRawToDatabases(String SqliteFileName) throws IOException {
        File dir = new File("data/data/" + mContext.getPackageName() + "/databases");
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        File file = new File(dir, SqliteFileName);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.e("sqlitesqlite", "开始复制数据库");
                inputStream = mContext.getClass().getClassLoader().getResourceAsStream("assets/" + SqliteFileName);
                outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len;

                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }


            } catch (IOException e) {
                e.printStackTrace();

            } finally {

                if (outputStream != null) {

                    outputStream.flush();
                    outputStream.close();

                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
    }


    //endregion

    //region ★ Function

    //region Init Function
    private void initView() {
        audiomanage = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        soundBar_System = (SeekBar) getView().findViewById(R.id.sound_System);
        mVolume_System = (TextView) getView().findViewById(R.id.tvVolume_System);
        spPower = (Spinner) getView().findViewById(R.id.spPower);
        mbtnSetPower = (Button) getView().findViewById(R.id.btnSetPower);
        mbtnGetPower = (Button) getView().findViewById(R.id.btnGetPower);
        mBtnChangeIP = (Button) getView().findViewById(R.id.btnChangeIP);
        mbtnExitAccount = (Button) getView().findViewById(R.id.btnExit);
    }

    private void initListener() {
        mbtnSetPower.setOnClickListener(this);
        mbtnGetPower.setOnClickListener(this);
        mBtnChangeIP.setOnClickListener(this);
        mbtnExitAccount.setOnClickListener(this);
    }

    private void initSharedPreferences() {
        mTvIP = (TextView) getView().findViewById(R.id.tvIp);
        String IP = AppConfig.getInstance().getString(Constant.URL, null);
        IP = IP.substring(7, 22);
        String[] aa = IP.split(":");
        mTvIP.setText(aa[0]);
    }
    //endregion

    //region Function
    private void getPower() {
        int iPower = mContext.mRFIDWithUHF.getPower();

        Log.i("UHFSetFragment", "OnClick_GetPower() iPower=" + iPower);

        if (iPower > -1) {
            int position = iPower - 5;
            int count = spPower.getCount();
            spPower.setSelection(position > count - 1 ? count - 1 : position);
        } else {
            showToast(mContext, "读取失败", 0);
        }
    }

    private void setPower() {
        int iPower = spPower.getSelectedItemPosition() + 5;

        if (mContext.mRFIDWithUHF.setPower(iPower)) {
            showToast(mContext, "设置成功", 0);
        } else {
            showToast(mContext, "设置失败", 0);
        }
    }

    private void changeIP(String content) {
        final EditText editText = new EditText(mContext);
        editText.setText(content);
        editText.setSelection(content.length());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(mContext);
        inputDialog.setTitle("请输入IP").setView(editText);
        inputDialog.setPositiveButton(getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvIP.setText(editText.getText().toString());
                        AppConfig.getInstance().putString(Constant.URL, "http://" + editText.getText().toString() + getString(R.string.WEBURLend));
                    }
                }).show();
    }

    private void changeLanguage() {
        final String[] items = {getString(R.string.ZH_CN), getString(R.string.EN_US), getString(R.string.KO_KR)};
        yourChoice = 0;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(mContext);
        singleChoiceDialog.setTitle(getString(R.string.ChooseLanguage));
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton(getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (yourChoice) {
                            case 0:
                                if (!AppConfig.getInstance().getLanuageType().equals(Constant.ZH_CN)) {
                                    AppConfig.getInstance().setLanuageType(Constant.ZH_CN);
                                    EventBus.getDefault().post(new Event.ChangeLanguageEvent());
                                }
                                break;
                            case 1:
                                if (!AppConfig.getInstance().getLanuageType().equals(Constant.EN_US)) {
                                    AppConfig.getInstance().setLanuageType(Constant.EN_US);
                                    EventBus.getDefault().post(new Event.ChangeLanguageEvent());
                                }
                                break;
                            case 2:
                                if (!AppConfig.getInstance().getLanuageType().equals(Constant.KO_KR)) {
                                    AppConfig.getInstance().setLanuageType(Constant.KO_KR);
                                    EventBus.getDefault().post(new Event.ChangeLanguageEvent());
                                }
                                break;
                        }
                    }
                });
        singleChoiceDialog.show();
    }


    //endregion

    //endregion

}