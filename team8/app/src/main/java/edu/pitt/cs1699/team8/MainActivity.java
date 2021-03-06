package edu.pitt.cs1699.team8;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText userText;
    EditText passText;

    Button loginButton;
    Button registerButton;
    Button logoutButton;


    TextView accountName;

    Intent startList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent startICCService = new Intent(this, ClearService.class);
        startService(startICCService);


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
            mAuth.signOut();

        startList = new Intent(this, List.class);

        Bundle b = getIntent().getExtras();
        if (b != null && !b.isEmpty())
            startList.putExtras(b);
        startList.putExtra("request", getIntent().getAction());

    }

    public void onStart() {
        super.onStart();

        //set the reference ids for everything we need
        userText = findViewById(R.id.editTextEmail);
        passText = findViewById(R.id.editTextPassword);

        loginButton = findViewById(R.id.buttonLogIn);
        logoutButton = findViewById(R.id.buttonLogOut);
        registerButton = findViewById(R.id.buttonCreateAccount);
        accountName = findViewById(R.id.textViewLoginStatus);

        //now see if we're logged in, and update the UI to match
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        try {
            if (getIntent().getStringExtra("request").equals("edu.pitt.cs1699.team8.StoreArrival")) {
                double lat = getIntent().getDoubleExtra("Latitude", 0.0);
                double lon = getIntent().getDoubleExtra("Longitude", 0.0);
                arriveLocation(lon, lat);
            }
        } catch (NullPointerException e) {
            Log.e("NullPointerException", e.toString());
        }


    }


    // ========================== ACTIVITY LOGIC

    private void updateUI(FirebaseUser currentUser) {
        //update the ui based on whether the user is logged in
        boolean loggedIn = (currentUser != null);

        //disable login functionality if so, and enable gameplay and score views
        //do the opposite if not logged in
        userText.setEnabled(!loggedIn);
        passText.setEnabled(!loggedIn);

        loginButton.setEnabled(!loggedIn);
        registerButton.setEnabled(!loggedIn);
        logoutButton.setEnabled(loggedIn);

        userText.setText("");
        passText.setText("");

        if (loggedIn) {
            //set the display text to show username
            accountName.setText(currentUser.getEmail());

        } else {
            //not logged in, so set to default
            accountName.setText("Not Logged In");

        }


    }

    //don't try to login with empty passwords or usernames - this checks for those
    private boolean validate(String userText, String passText) {
        if (userText.isEmpty() || passText.isEmpty()) {
            Toast.makeText(MainActivity.this, "Username or password is blank", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    //create an account if one doesn't exist
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginNotice", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Account Created.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                            startListIntent();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LoginNotice", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    //login to a preexisting account
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginNotice", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startListIntent();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LoginNotice", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    //================================= functions attached to buttons through XML

    //login button
    public void loginClick(View v) {
        if (validate(userText.getText().toString(), passText.getText().toString())) {
            signIn(userText.getText().toString(), passText.getText().toString());
        }

    }

    //create account button
    public void createAccountClick(View v) {
        if (validate(userText.getText().toString(), passText.getText().toString())) {
            createAccount(userText.getText().toString(), passText.getText().toString());
        }


    }

    //log out button
    public void logoutClick(View v) {
        mAuth.signOut();
        updateUI(null);

    }

    private void startListIntent() {
        if (mAuth.getCurrentUser() != null) {
            startActivity(startList);
        }

    }

    private void arriveLocation(final double lat, final double lon) {
        final Context appContext = this;
        final Dialog dia = new Dialog(this);
        dia.setContentView(R.layout.dialog_near_store);
        dia.show();

        TextView mainText = dia.findViewById(R.id.dialogMain);
        mainText.setText(String.format("You are near a grocery store!\nThe store is located at\n%.2f\n%.2f", lon, lat));
        Button closeDiag = dia.findViewById(R.id.dialogButton);

        closeDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dia2 = new Dialog(appContext);
                dia2.setContentView(R.layout.dialog_send_data);
                dia2.show();

                Button okButton = dia2.findViewById(R.id.okButton);
                Button notOkButton = dia2.findViewById(R.id.notOkButton);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent("edu.pitt.cs1699.team9.CRASH");
                            intent.setAction("edu.pitt.cs1699.team9.CRASH");

                            double crashValue = Math.abs(lat * lon);

                            intent.putExtra("pct_decrease", Double.toString(crashValue));
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

                            sendBroadcast(intent);
                        }catch (Exception e){
                            Log.d("CRASH",e.toString());
                        }
                        dia2.dismiss();
                        dia.dismiss();
                    }
                });

                notOkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia2.dismiss();
                        dia.dismiss();
                    }
                });

            }


        });
    }


}
