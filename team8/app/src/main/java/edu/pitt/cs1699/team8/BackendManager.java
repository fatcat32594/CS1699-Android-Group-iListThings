package edu.pitt.cs1699.team8;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BackendManager extends Service {
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private Uri content_uri = Uri.parse("content://edu.pitt.cs1699.team8.provider/items");
    private ContentObserver objectObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            String[] projection = {"NAME", "PRICE", "QUANTITY"};
            String selection = "ID = ?";
            String[] selctionArgs = {mAuth.getUid()};
            String sortOrder = "NAME";

            Cursor cursor = getContentResolver().query(content_uri, projection, selection, selctionArgs, sortOrder);

            DatabaseReference myRef = database.getReference(mAuth.getUid());
            myRef.setValue(null);

            if (cursor != null ) {
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("NAME"));
                        double price = cursor.getDouble(cursor.getColumnIndex("PRICE"));
                        long quan = cursor.getLong(cursor.getColumnIndex("QUANTITY"));

                        DatabaseReference itemRef = myRef.child(name);
                        itemRef.child("price").setValue(price);
                        itemRef.child("quantity").setValue(quan);

                    } while (cursor.moveToNext() && !cursor.isAfterLast());
                }
                cursor.close();
            }
        }
    };

    public BackendManager() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final DatabaseReference myRef = database.getReference(mAuth.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //items = new HashMap<>();
                getContentResolver().unregisterContentObserver(objectObserver);
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    try {
                        String name = child.getKey();
                        double price = Double.parseDouble(child.child("price").getValue().toString());
                        long quantity = (long) child.child("quantity").getValue();

                        ContentValues values = new ContentValues();
                        values.put("ID", mAuth.getUid());
                        values.put("NAME", name);
                        values.put("PRICE", price);
                        values.put("QUANTITY", quantity);

                        getContentResolver().insert(content_uri, values);

                    } catch (NullPointerException e) {
                        Log.e("SOEMTHING", String.valueOf(child.child("quantity").exists()));
                    }


                }
                getContentResolver().registerContentObserver(content_uri, true, objectObserver);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCreate() {
        getContentResolver().registerContentObserver(content_uri, true, objectObserver);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

}
