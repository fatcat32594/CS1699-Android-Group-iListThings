package edu.pitt.cs1699.team8;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;


public class ClearService extends AppCompatActivity {

    private static int CLEAR_CODE = 56;

    Handler mMessageHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMessageHandler = new MessageHandler(this);

    }

    private static class MessageHandler extends Handler {
        private WeakReference<ClearService> mActivity;

        public MessageHandler(ClearService activity){
            mActivity = new WeakReference<ClearService>(activity);
        }

        public void handleMessage(Message message){
            ClearService activity = mActivity.get();

            if(activity == null) {
                return;
            }

            int i = MessageService.getClear(message);

            if(i == CLEAR_CODE){
                Intent intent = new Intent(activity, List.class);

                intent.putExtra("CLEAR_CODE", 56);


            }

        }

    }
}
