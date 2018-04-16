package edu.pitt.cs1699.team8;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public class MessageService extends Service {

    private static final String MESSENGER = "MESSENGER";

    private volatile Looper mServiceLooper;

    private volatile ServiceHandler mServiceHandler;

    private static int clearId = 56;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();

        HandlerThread thread = new HandlerThread("MessageServer");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Message message = mServiceHandler.makeClearMessage(intent);

        mServiceHandler.sendMessage(message);

        return Service.START_NOT_STICKY;
    }

    public static int getClear(Message message){
        return message.arg1;
        
    }


    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper){super(looper);}

        public Message makeClearMessage(Intent intent){
            Message message = Message.obtain();

            message.obj = intent;
            message.arg1 = clearId;

            return message;
        }
    }


}