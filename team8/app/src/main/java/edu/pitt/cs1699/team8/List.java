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

public class List extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mAuth=FirebaseAuth.getInstance();

        String user = mAuth.getCurrentUser().getUid();


        BackendManager backendManager = new BackendManager(user);



        ArrayList<String> groceriesList = backendManager.getItemsAsStringArray();


        ListView list = findViewById(R.id.listView);
        if(groceriesList!=null) {
            list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groceriesList));
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
