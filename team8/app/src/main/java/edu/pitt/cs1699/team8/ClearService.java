package edu.pitt.cs1699.team8;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;


public class ClearService extends Service {
    public static final int MSG_CLEAR_CODE = 56;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CLEAR_CODE:
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("CLEAR_PLS", true);
                    startActivity(intent);
                default:
                    super.handleMessage(msg);
            }
        }
    }

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
