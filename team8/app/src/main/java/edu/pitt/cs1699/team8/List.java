package edu.pitt.cs1699.team8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static android.R.layout.simple_list_item_1;

public class List extends AppCompatActivity {

    private FirebaseAuth mAuth;

    ListView list;
    BackendManager backendManager;


    private FirebaseDatabase database = null;
    private HashMap<String, Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mAuth=FirebaseAuth.getInstance();

        String user = mAuth.getCurrentUser().getUid();


        backendManager = new BackendManager();


        list = findViewById(R.id.listView);
        
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        items = new HashMap<>();
        final DatabaseReference myRef = database.getReference(mAuth.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items = new HashMap<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    try {
                        String name = child.getKey();
                        double price = Double.parseDouble(child.child("price").getValue().toString());
                        long quantity = (long) child.child("quantity").getValue();

                        Item i = new Item(name, price, quantity);
                        items.put(name, i);
                    }
                    catch (NullPointerException e) {
                        Log.e("SOEMTHING", String.valueOf(child.child("quantity").exists()));
                    }
                }
                renderList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void renderList() {
        ArrayList<String> groceriesList = backendManager.getItemsAsStringArray();
        if(groceriesList!=null) {
            list.setAdapter(new ArrayAdapter<String>(this, simple_list_item_1, groceriesList));
            try {
                Log.e("LIST", groceriesList.get(0));
            } catch (IndexOutOfBoundsException e) {
                Log.e("LIST", "IS EMPTY");
            }
        }
    }

    protected void singleClick(View view){
        Intent intent = new Intent(this,AddItem.class);
        startActivity(intent);
    }

    protected void multiClick(View view){

    }

    protected void clearClick(View view){

    }
}
