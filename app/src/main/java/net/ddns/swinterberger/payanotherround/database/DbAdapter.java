package net.ddns.swinterberger.payanotherround.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Stefan on 18.09.2016.
 */
public class DbAdapter {

    private final DbHelper dbHelper;
    private CrudUser crudUser;

    private SQLiteDatabase db;

    public DbAdapter(final Context context) {
        dbHelper = new DbHelper(context, "ParDatabase", null, 1);
    }

    public void open() {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        dbHelper.close();
    }

    public CrudUser getCrudUser() {
        open();
        if (crudUser == null) {
            crudUser = new CrudUser(db);
        }
        return crudUser;
    }
}
