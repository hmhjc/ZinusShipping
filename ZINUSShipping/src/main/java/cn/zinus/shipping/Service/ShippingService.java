package cn.zinus.shipping.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cn.zinus.shipping.SocketConnect.SyncPC;

public class ShippingService extends Service {
    ServerSocket serverSocket = null;
    final int SERVER_PORT = 10087;
    public static Boolean mainThreadFlag = true;
    private ArrayList<?> list;

    public ShippingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        list = new ArrayList<>();
        new Thread() {
            public void run() {
                doListen();
            }
        }.start();
    }

    private void doListen() {
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            while (mainThreadFlag) {
                Log.e("chl1aaa", "doListen() 3");
                Socket socket = serverSocket.accept();
                Log.e("chl1aaa", "doListen() 4");
                new Thread(new SyncPC(socket, list, ShippingService.this)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
