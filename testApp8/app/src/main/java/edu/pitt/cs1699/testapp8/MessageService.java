package edu.pitt.cs1699.testapp8;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MessageService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
