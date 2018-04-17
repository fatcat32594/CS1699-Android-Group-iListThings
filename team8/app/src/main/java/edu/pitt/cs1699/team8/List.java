package edu.pitt.cs1699.team8;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Random;

public class List extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ListView list;
    Uri content_uri = Uri.parse("content://edu.pitt.cs1699.team8.provider/items");
    ArrayList<String> groceriesList;

    private ContentObserver objectObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            renderList();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent startBackend = new Intent(this, BackendManager.class);
        startService(startBackend);

        setContentView(R.layout.activity_list);
        mAuth=FirebaseAuth.getInstance();

        String user = mAuth.getCurrentUser().getUid();

        list = findViewById(R.id.listView);

        renderList();

        Intent receivedIntent = getIntent();
        Bundle receivedBundle = receivedIntent.getExtras();
        String action;
        if (receivedBundle != null) {
            action = receivedBundle.getString("request");
        } else {
            action = "";
        }

        try {
            if (getIntent().getBooleanExtra("CLEAR_PLS", false)&&groceriesList.size()>0) {
                String item = groceriesList.get(new Random().nextInt(groceriesList.size()));
                String itemName = item.split("\n")[0];
                itemName = itemName.substring(6, itemName.length());
                Intent intent = new Intent("edu.pitt.cs1699.team9.OFF_MARKET");
                intent.setPackage("com.example.der62.battlestocks");
                intent.putExtra("company", itemName);
                startForegroundService(intent);
                getContentResolver().delete(content_uri, "ID = ?", new String[]{mAuth.getUid()});
            }
        } catch (Exception e) {
            Log.d("CLEAR",e.toString());
        }

        try {
            if (action.equals("edu.pitt.cs1699.team8.SINGLE")) {
                startSingle = new Intent(this, AddItem.class);
                if (receivedBundle != null && !receivedBundle.isEmpty())
                    startSingle.putExtras(receivedBundle);
                startActivity(startSingle);
            } else if (action.equals("edu.pitt.cs1699.team8.MULTI")) {
                Intent startMulti = new Intent(this, AddRecipe.class);
                if (receivedBundle != null && !receivedBundle.isEmpty())
                    startMulti.putExtras(receivedBundle);
                startActivity(startMulti);
            }
        } catch (NullPointerException n) {
            Log.e("NPE: ", n.toString());
        }


    }


    Intent startSingle;

    protected void onStart() {
            super.onStart();

    }

    protected void onResume() {
        super.onResume();
        renderList();
        getContentResolver().registerContentObserver(content_uri, true, objectObserver);
    }

    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(objectObserver);
    }


    private void renderList() {
        String[] projection = {"NAME", "PRICE", "QUANTITY"};
        String selection = "ID = ?";
        String[] selctionArgs = {mAuth.getUid()};
        String sortOrder = "NAME";

        Cursor cursor = getContentResolver().query(content_uri, projection, selection, selctionArgs, sortOrder);

        groceriesList = new ArrayList<>();

        if (cursor != null ) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("NAME"));
                    double price = cursor.getDouble(cursor.getColumnIndex("PRICE"));
                    long quan = cursor.getLong(cursor.getColumnIndex("QUANTITY"));
                    Item i = new Item(name, price, quan);
                    groceriesList.add(i.toString());
                } while (cursor.moveToNext() && !cursor.isAfterLast());
            }
            cursor.close();
        }

        if(groceriesList!=null) {
            list.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_list_item, groceriesList));
            try {
                Log.e("LIST", groceriesList.get(0));
            } catch (IndexOutOfBoundsException e) {
                Log.e("LIST", "IS EMPTY");
            }
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //remove from ArrayList
                    ArrayList<String> arr = groceriesList;
                    String s = (String) parent.getItemAtPosition(position);
                    arr.remove(s);
                    String[] args = s.split("\n");
                    String result = args[0].substring(6, args[0].length());
                    getContentResolver().delete(content_uri, "NAME = ? AND ID = ?", new String[]{result, mAuth.getUid()});
                    list.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_list_item, arr));
                }
            });
        }
    }

    public void singleClick(View view){
        Intent intent = new Intent(this,AddItem.class);
        startActivity(intent);
    }

    public void multiClick(View view){
        Intent intent = new Intent(this, AddRecipe.class);
        startActivity(intent);

    }

    public void clearClick(View view){
        try {
            if(groceriesList.size()>0) {
                String item = groceriesList.get(new Random().nextInt(groceriesList.size()));
                String itemName = item.split("\n")[0];
                Intent intent = new Intent("edu.pitt.cs1699.team9.OFF_MARKET");
                intent.setPackage("com.example.der62.battlestocks");
                intent.putExtra("company", itemName);
                startForegroundService(intent);
            }
        }catch (Exception e){
            Log.d("CLEAR",e.toString());
        }
        getContentResolver().delete(content_uri, "ID = ?", new String[] {mAuth.getUid()});
    }
}
