package edu.pitt.cs1699.testapp8;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static int CLEAR_CODE = 56;

    Handler mMessengerHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onSingleIntentClick(View v){
        Intent intent = new Intent("edu.pitt.cs1699.team8.SINGLE");
        Bundle b = new Bundle();
        JSONObject singleItemData = new JSONObject();

        try{

            singleItemData.put("Name", "Apple");
            singleItemData.put("Price", "100");
            singleItemData.put("Quantity", "4");
        }catch(JSONException e){
            e.printStackTrace();
        }
        b.putString("singleItemData", singleItemData.toString());
        intent.putExtra("singleItemData", b);
        startActivity(intent);
    }
    public void onMultipleIntentClick(View v){
        Intent intent = new Intent("edu.pitt.cs1699.team8.MULTI");
        Bundle b = new Bundle();
        JSONArray multipleItemData = new JSONArray();
        JSONObject items = new JSONObject();
        JSONObject item1 = new JSONObject();
        JSONObject item2 = new JSONObject();
        JSONObject item3 = new JSONObject();

        try{
            item1.put("Name", "Banana");
            item1.put("Price", "150");
            item1.put("Quantity", "2");

            item2.put("Name","Bacon");
            item2.put("Price","1000");
            item2.put("Quantity","2");

            item3.put("Name","Tomato");
            item3.put("Price","250");
            item3.put("Quantity","1");

            multipleItemData.put(item1);
            multipleItemData.put(item2);
            multipleItemData.put(item3);

            items.put("Items", multipleItemData);
        }catch(JSONException e){
            e.printStackTrace();
        }
        Log.e("MULTI data: ", items.toString());
        intent.putExtra("multipleItemData", items.toString());
        startActivity(intent);
    }
    public void onIpcClick(View v) throws RemoteException {
        Message message = new Message();
        message.arg1 = CLEAR_CODE;

        Intent intent = new Intent("edu.pitt.cs1699.team8.ClearService.class");

        intent.putExtra("Message", message);

        startActivity(intent);

    }
    public void onBroadcastClick(View v){
        Intent intent = new Intent("edu.pitt.cs1699.team8.StoreArrival");
        intent.putExtra("Longitude", 2.123);
        intent.putExtra("Latitude", 1.234);
        sendBroadcast(intent);
        Log.e("BROADCAST SENT", intent.toString());
    }
}

