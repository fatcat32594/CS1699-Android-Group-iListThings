package edu.pitt.cs1699.team8;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static BackendManager manager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Toast.makeText(this, getIntent().getAction().toString(), Toast.LENGTH_LONG).show();

        String callingAction = getIntent().getAction().toString();

        if (callingAction.equals("edu.pitt.cs1699.team8.SINGLE")) {
            //do shit
        } else if (callingAction.equals("edu.pitt.cs1699.team8.MULTI")) {
            //do other shit
        }

        String uid = mAuth.getUid();
        if (uid == null)
            uid = "null";

        manager = new BackendManager(uid);
        manager.addItem(uid, "Apples", 0.50, 10);
        try {
            Log.e("FFF", manager.getItemsAsStringArray().get(0));
        } catch (Exception e) {

        }
    }
}
