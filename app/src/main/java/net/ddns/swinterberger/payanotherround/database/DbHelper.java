package net.ddns.swinterberger.payanotherround.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Stefan on 18.09.2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY, name TEXT not null)");
        db.execSQL("CREATE TABLE bill (id INTEGER PRIMARY KEY, description TEXT not null)");
        db.execSQL("CREATE TABLE trip (id INTEGER PRIMARY KEY, name TEXT not null)");
        db.execSQL("CREATE TABLE debt (id INTEGER PRIMARY KEY, amount INTEGER not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
