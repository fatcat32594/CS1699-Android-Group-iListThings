package edu.pitt.cs1699.team8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class AddItem extends AppCompatActivity {

    EditText itemText;
    EditText priceText;
    EditText quantityText;
    BackendManager backendManager;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mAuth = FirebaseAuth.getInstance();
        backendManager = new BackendManager();
    }

    public void addClick(View view){
        itemText = findViewById(R.id.item_input);
        priceText = findViewById(R.id.price_input);
        quantityText = findViewById(R.id.quantity_input);

        String itemName = itemText.getText().toString();
        long itemQuantity = Long.parseLong(quantityText.getText().toString());
        final double itemPrice = Double.parseDouble(priceText.getText().toString());

        backendManager.addItem(itemName, itemPrice, itemQuantity);

        try {
            Intent intent = new Intent("edu.pitt.cs1699.team9.NEW_STOCK");
            intent.putExtra("company", itemName);
            intent.putExtra("price", itemPrice);
            startActivity(intent);
        }catch(Exception e){
        }

        finish();
    }
}
