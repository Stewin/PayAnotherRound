package net.ddns.swinterberger.payanotherround.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.swinterberger.payanotherround.entities.User;

/**
 * Created by Stefan on 18.09.2016.
 */
public class DbAdapter {

    private final DbHelper dbHelper;
    private final String TABLE_USER = "user";
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

    public boolean createUser(final User user) {
        final ContentValues values = new ContentValues();
        values.put("name", user.getName());
        final long id = db.insert(TABLE_USER, null, values);
        user.setId(id);

        return id != -1;
    }

    public User readUserById(final long id) {
        User user = null;
        final Cursor result = db.query(TABLE_USER, new String[]{"id", "name"}, "id =" + id,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        if (found) {
            user = getNextUser(result);
        }
        result.close();
        return user;
    }

    private User getNextUser(final Cursor cursor) {
        final User user = new User();
        user.setId(cursor.getLong(0));
        user.setName(cursor.getString(1));
        cursor.moveToNext();
        return user;
    }

    public boolean updateUser(final User user) {
        final ContentValues values = new ContentValues();
        values.put("name", user.getName());
        return db.update(TABLE_USER, values, "id = " + user.getId(), null) > 0;
    }

    public boolean deleteUserById(final long id) {
        return db.delete(TABLE_USER, "id=" + id, null) > 0;
    }
}
