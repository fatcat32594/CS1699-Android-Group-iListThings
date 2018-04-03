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

import static android.R.layout.simple_list_item_1;

public class List extends AppCompatActivity {

    private FirebaseAuth mAuth;

    ListView list;
    BackendManager backendManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mAuth=FirebaseAuth.getInstance();

        String user = mAuth.getCurrentUser().getUid();


        backendManager = new BackendManager();


        list = findViewById(R.id.listView);
        renderList();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    renderList();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).run();



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
