package edu.pitt.cs1699.team8;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.io.File;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


public class ListContentProvider extends ContentProvider {
    static final String PROVIDER_NAME = "edu.pitt.cs1699.team8.provider";
    static final String URL = "content://" + PROVIDER_NAME + "/items";
    static final Uri CONTENT_URI = Uri.parse(URL);

    String DB_NAME = "SHOPPINGLIST_DB.db";
    String TABLE_NAME = "LIST";
    String ID = "ID";
    String NAME = "NAME";
    String PRICE = "PRICE";
    String QUANTITY = "QUANTITY";

    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + ID + " TEXT, "
            + NAME + " TEXT, "
            + PRICE + " NUMBER NOT NULL, "
            + QUANTITY + " INTEGER NOT NULL, "
            + "primary key (" + ID + "," + NAME +"));";

    SQLiteDatabase db;

    public ListContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = db.delete(TABLE_NAME, selection, selectionArgs);
        if (count > 0)
            getContext().getContentResolver().notifyChange(CONTENT_URI , null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.replace(TABLE_NAME, "", values);
        if (rowID < 0)
            return null;
        Uri res_uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(CONTENT_URI , null);
        return res_uri;
    }

    @Override
    public boolean onCreate() {
        try {
            File dbFile = new File(getContext().getFilesDir(), DB_NAME);
            db = openOrCreateDatabase(dbFile, null);
            db.execSQL(CREATE_TABLE);
            return true;
        } catch (SQLException e) {
            Log.e("ERROR", e.toString());
            return false;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        Log.e("RESULTS", String.valueOf(cursor.getCount()));
        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = db.update(TABLE_NAME, values, selection, selectionArgs);
        if (count > 0)
            getContext().getContentResolver().notifyChange(CONTENT_URI , null);
        return count;
    }
}
