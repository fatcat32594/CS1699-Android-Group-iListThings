package edu.pitt.cs1699.team8;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BackendManager {
    private FirebaseDatabase database = null;
    private HashMap<String, Item> items;
    private FirebaseAuth mAuth;

    public BackendManager() {
        if (database == null) {
            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            items = new HashMap<>();
            final DatabaseReference myRef = database.getReference(mAuth.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    items = new HashMap<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        String name = child.getKey();
                        double price = Double.parseDouble(child.child("price").getValue().toString());
                        long quantity = (long) child.child("quantity").getValue();

                        Item i = new Item(name, price, quantity);
                        items.put(name, i);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void addItem(String name, final double price, final long quantity) {
        double oldPrice;
        long oldQuantity;
        if (items.containsKey(name)) {
            oldPrice = items.get(name).getPrice();
            oldQuantity = items.get(name).getQuantity();
        } else {
            oldPrice = 0;
            oldQuantity = 0;
        }

        Item i = new Item(name, price, quantity);
        items.put(name, i);

        double newPrice = price;
        double newQuantity = oldQuantity + quantity;

        DatabaseReference myRef = database.getReference(mAuth.getUid()+"/"+name);
        myRef.child("price").setValue(newPrice);
        myRef.child("quantity").setValue(newQuantity);
    }

    boolean ready=false;

    public boolean getReady(){
        return ready;
    }

    public ArrayList<String> getItemsAsStringArray() {
        ArrayList<String> strings = new ArrayList<>();
        for (Item i: items.values()) {
            strings.add(i.toString());
        }
        ready=true;
        return strings;

    }



}
