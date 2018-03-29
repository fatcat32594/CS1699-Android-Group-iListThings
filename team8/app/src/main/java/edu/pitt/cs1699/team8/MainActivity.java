package edu.pitt.cs1699.team8;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

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
    }
}
