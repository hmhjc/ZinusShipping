package cn.zinus.shipping.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.zinus.shipping.Config.AppConfig;
import cn.zinus.shipping.R;
import cn.zinus.shipping.SocketConnect.SyncPC;
import cn.zinus.shipping.util.Constant;

import static cn.zinus.shipping.Service.ShippingService.mainThreadFlag;


/**
 * Developer:Spring
 * DataTime :2017/9/19 09:44
 * Main Change:修改成off-line操作,login界面只是一个跳转
 */
public class LoginActivity extends BaseActivity {

    //region VAR
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式
    // UI references.
    private EditText mUserIDView;
    private EditText mPasswordView;
    String userID = "";
    String password = "";
    public static final int RULEREQUEST = 1;
    public static final int VERSIONREQUEST = 2;
    ServerSocket serverSocket = null;
    final int SERVER_PORT = 10087;
    ArrayList<String> list;

    BufferedOutputStream out;
    BufferedInputStream in;
    //endregion

    //region life cycle(生命周期)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initSharedPreferences();
        String sta = AppConfig.getInstance().getLanuageType();
        Log.e("stastasta",sta);
        list = new ArrayList<>();
        list.add("fdasf");
        try {
            Log.e("数据库", "111111111");
            CopySqliteFileFromRawToDatabases(Constant.DATEBASE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread() {
            public void run() {
                doListen();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    //endregion

    //region Function(方法)
    private void initSharedPreferences() {

        String name = AppConfig.getInstance().getString(Constant.UserID, null);
        if (name != null) {
            mUserIDView.setText(name);
            mUserIDView.setSelection(name.length());
        }
    }

    private void initView() {
        mUserIDView = (EditText) findViewById(R.id.UserID);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mbtnlogin = (Button) findViewById(R.id.btn_login);
        mbtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private void Login() {
        userID = mUserIDView.getText().toString();
        password = mPasswordView.getText().toString();
        Intent intent = new Intent(LoginActivity.this, MainNaviActivity.class);
        startActivity(intent);
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        int versioncode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return Integer.toString(versioncode);
    }

    private void CopySqliteFileFromRawToDatabases(String SqliteFileName) throws IOException {
        File dir = new File("data/data/" + LoginActivity.this.getPackageName() + "/databases");
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
                inputStream = LoginActivity.this.getClass().getClassLoader().getResourceAsStream("assets/" + SqliteFileName);
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

    private void doListen() {
        serverSocket = null;
        try {
            Log.e("chl", "doListen()");
            serverSocket = new ServerSocket(SERVER_PORT);
            Log.e("chl", "doListen() 2");
            while (mainThreadFlag) {
                Log.e("chl1", "doListen() 4");
                Socket socket = serverSocket.accept();
                Log.e("chl1", "doListen() 3");
                new Thread(new SyncPC(socket, list, LoginActivity.this)).start();
//                Log.e("chl1", "doListen() 3");
//                Socket socket = serverSocket.accept();
//                Log.e("chl1", "doListen() 4");
//                Message msg = mHandler.obtainMessage();
//                in = new BufferedInputStream(socket.getInputStream());
//                byte[] tempbuffer = new byte[3];
//                try {
//                    msg.what = Integer.parseInt(new String(tempbuffer, 0, 3, "utf-8"));
//                    Log.e("aaaaa", msg.what + "");
//                    msg.obj = socket;
//                    // mHandler.sendMessage(msg);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}