package edu.pitt.cs1699.team8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.e("BROADCAST GOT", intent.toString());

        Intent startLoginScreen = new Intent(context, MainActivity.class);
        Bundle b = intent.getExtras();
        if (b != null && !b.isEmpty())
            startLoginScreen.putExtras(b);

        startLoginScreen.putExtra("request", intent.getAction());

        context.startActivity(startLoginScreen);

    }
}
