package edu.pitt.cs1699.team8;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    String itemName;
    long itemQuantity;
    boolean receivedItem;
    private FirebaseAuth mAuth;

    Uri content_uri = Uri.parse("content://edu.pitt.cs1699.team8.provider/items");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mAuth = FirebaseAuth.getInstance();
        itemText = findViewById(R.id.item_input);
        priceText = findViewById(R.id.price_input);
        quantityText = findViewById(R.id.quantity_input);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent receivedIntent = getIntent();
        Bundle receivedBundle = receivedIntent.getBundleExtra("singleItemData");
        String singleItem;
        if(receivedBundle != null){
            singleItem = receivedBundle.getString("singleItemData");
        }else{
            singleItem = "";
        }

        try{
            JSONObject singleItemData = new JSONObject(singleItem);
            itemName = (String) singleItemData.get("Name");
            itemQuantity = Long.parseLong(singleItemData.get("Quantity").toString());
            double itemPrice = Double.parseDouble(singleItemData.get("Price").toString());
            itemText.setText(itemName);
            priceText.setText(Double.toString(itemPrice));
            quantityText.setText(Long.toString(itemQuantity));
        }catch(Exception e){
            Log.v("STUFF",e.toString());
            Log.v("STUFF",singleItem);
        }
    }

    public void addClick(View v){

        itemName = itemText.getText().toString();
        itemQuantity = Long.parseLong(quantityText.getText().toString());
        final double itemPrice = Double.parseDouble(priceText.getText().toString());


        ContentValues values = new ContentValues();
        values.put("ID", mAuth.getUid());
        values.put("NAME", itemName);
        values.put("PRICE", itemPrice);
        values.put("QUANTITY", itemQuantity);

        getContentResolver().insert(content_uri, values);

        try {
            Intent intent = new Intent("edu.pitt.cs1699.team9.NEW_STOCK");
            intent.putExtra("company", itemName);
            intent.putExtra("price", itemPrice);
            startActivity(intent);
        }catch(Exception e){
            Log.v("SEND",e.toString());
        }

        finish();
    }
}
