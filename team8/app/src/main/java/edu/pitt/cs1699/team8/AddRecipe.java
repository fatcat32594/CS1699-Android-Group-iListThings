package edu.pitt.cs1699.team8;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class AddRecipe extends AppCompatActivity {

    ArrayList<Item> items;
    ListView itemView;
    Context addRecContext;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Uri content_uri = Uri.parse("content://edu.pitt.cs1699.team8.provider/items");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipie);
        items = new ArrayList<>();
        Bundle receivedBundle = getIntent().getExtras();
        if(receivedBundle != null){
            String multiItemString = receivedBundle.getString("multipleItemData");
            try{
                JSONObject multiItemJson = new JSONObject(multiItemString);
                JSONArray itemsJSON = multiItemJson.getJSONArray("Items");
                for (int i = 0; i < itemsJSON.length(); i++) {
                    JSONObject itemJSON = itemsJSON.getJSONObject(i);
                    String name = itemJSON.getString("Name");
                    double price = itemJSON.getDouble("Price");
                    long quan = itemJSON.getLong("Quantity");
                    Item item = new Item(name, price, quan);
                    items.add(item);
                }
            }catch(Exception e){
                Log.v("STUFF",e.toString());
                Log.v("STUFF",multiItemString);
            }
        }
        itemView = findViewById(R.id.itemView);
        itemView.setAdapter(new ArrayAdapter<>(this, R.layout.custom_list_item, items));
        itemView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //remove from ArrayList
                ArrayList<Item> arr = items;
                Item s = (Item) parent.getItemAtPosition(position);
                arr.remove(s);
                itemView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.custom_list_item, arr));
            }
        });
        addRecContext = this;
    }

    public void addItemClick(View v) {
        final Dialog dia = new Dialog(this);
        dia.setContentView(R.layout.dialog_add_item);
        dia.show();
        Button getButton = dia.findViewById(R.id.addItemButton);

        final EditText getName = dia.findViewById(R.id.item_input);
        final EditText getPrice = dia.findViewById(R.id.price_input);
        final EditText getQuantity = dia.findViewById(R.id.quantity_input);

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = getName.getText().toString();
                    String priceString = getPrice.getText().toString();
                    double price = Double.parseDouble(priceString);
                    String quanString = getQuantity.getText().toString();
                    int quantity = Integer.parseInt(quanString);
                    Item a = new Item(name, price, quantity);

                    items.add(a);
                    itemView.invalidate();
                    itemView.setAdapter(new ArrayAdapter<>(addRecContext, R.layout.custom_list_item, items));
                }catch (Exception e){
                    Log.e("NPE", e.toString());
                }
                dia.dismiss();
            }
        });

    }

    public void submitClick(View v) {
        for (Item i : items) {
            String name = i.getName();
            double price = i.getPrice();
            long quan = i.getQuantity();


            ContentValues values = new ContentValues();

            values.put("ID", mAuth.getUid());
            values.put("NAME", name);
            values.put("PRICE", price);
            values.put("QUANTITY", quan);

            getContentResolver().insert(content_uri, values);
        }

        final Dialog dia = new Dialog(this);
        dia.setContentView(R.layout.dialog_send_data);
        dia.show();

        Button okButton = dia.findViewById(R.id.okButton);
        Button notOkButton = dia.findViewById(R.id.notOkButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!items.isEmpty()){

                        Item i = items.get(new Random().nextInt(items.size()));
                        String name = i.getName();
                        double price = i.getPrice();
                        try {
                            Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.der62.battlestocks");
                            intent.setAction("edu.pitt.cs1699.team9.PRICE_CHANGE");
                            intent.putExtra("company", name);
                            intent.putExtra("price",Double.toString(price));
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        }catch(Exception e){
                            Log.v("SEND",e.toString());
                        }
                    }

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

}
