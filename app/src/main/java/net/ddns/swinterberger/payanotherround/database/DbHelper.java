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
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY, name TEXT not null, color TEXT, pictureid TEXT)");
        db.execSQL("CREATE TABLE bill (id INTEGER PRIMARY KEY, description TEXT not null, amount INTEGER NOT NULL, currency TEXT NOT NULL)");
        db.execSQL("CREATE TABLE trip (id INTEGER PRIMARY KEY, name TEXT not null)");
        db.execSQL("CREATE TABLE attend (fk_user INTEGER NOT NULL, fk_trip INTEGER NOT NULL, FOREIGN KEY (fk_user) REFERENCES user(id), FOREIGN KEY (fk_trip) REFERENCES trip(id), CONSTRAINT pk_attendid PRIMARY KEY (fk_user, fk_trip))");
        db.execSQL("CREATE TABLE debt (fk_creditor INTEGER NOT NULL, fk_debtor INTEGER NOT NULL, amount INTEGER not null, FOREIGN KEY (fk_creditor) REFERENCES user(id), FOREIGN KEY (fk_debtor) REFERENCES user(id), CONSTRAINT pk_debtid PRIMARY KEY (fk_creditor, fk_debtor))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
