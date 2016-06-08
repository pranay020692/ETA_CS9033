package com.nyu.cs9033.eta.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pranay on 11/5/2015.
 * various Database Operations for the Trip database are handeled.
 */
public class DBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESTINATION = "destination";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "Trips";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table Trips (_id integer primary key autoincrement, "
            + "name text not null, destination text not null);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Trips");
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    //insert a trip into the database
    public long insertTrip(String name, String destination) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_DESTINATION, destination);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //get all trips from the database
    public Cursor getAllTrips() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME,
                KEY_DESTINATION}, null, null, null, null, null);
    }

}
