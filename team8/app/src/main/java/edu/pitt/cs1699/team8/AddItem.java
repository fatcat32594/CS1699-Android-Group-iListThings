package edu.pitt.cs1699.team8;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

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
        try {
            itemName = itemText.getText().toString();
            itemQuantity = Long.parseLong(quantityText.getText().toString());
            final double itemPrice = Double.parseDouble(priceText.getText().toString());

            ContentValues values = new ContentValues();
            values.put("ID", mAuth.getUid());
            values.put("NAME", itemName);
            values.put("PRICE", itemPrice);
            values.put("QUANTITY", itemQuantity);

            getContentResolver().insert(content_uri, values);

            final Dialog dia = new Dialog(this);
            dia.setContentView(R.layout.dialog_send_data);
            dia.show();

            Button okButton = dia.findViewById(R.id.okButton);
            Button notOkButton = dia.findViewById(R.id.notOkButton);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.der62.battlestocks");
                        intent.setAction("edu.pitt.cs1699.team9.NEW_STOCK");
                        intent.putExtra("company", itemName);
                        intent.putExtra("price", Double.toString(itemPrice));
                        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        startActivity(intent);

                    } catch (NullPointerException npe) {
                        Log.e("NPE", npe.toString());
                    }
                    dia.dismiss();
                    finish();
                }
            });

            notOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dia.dismiss();
                    finish();
                }
            });




        }
        catch(Exception e){
            Log.v("SEND",e.toString());
        }


    }
}
