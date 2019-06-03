package com.hc.essay.library;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.hc.essay.joke.ProcessConnection;

import static android.content.ContentValues.TAG;

public class GuardService extends Service {

    private final int GuardId = 1;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        startForeground(GuardId , new Notification());

        //绑定建立连接
        bindService(new Intent(this , MessageService.class),mServiceConnection , Context.BIND_IMPORTANT);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {
        };
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接上
            Log.i(TAG, "onServiceConnected1: 建立连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
            Log.i(TAG, "onServiceConnected: 断开连接");
            startService(new Intent(GuardService.this , MessageService.class));

            //绑定建立连接
            bindService(new Intent(GuardService.this , MessageService.class),mServiceConnection , Context.BIND_IMPORTANT);
        }
    };
}
