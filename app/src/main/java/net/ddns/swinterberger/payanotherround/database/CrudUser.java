package net.ddns.swinterberger.payanotherround.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.swinterberger.payanotherround.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 24.09.2016.
 */
public class CrudUser {

    private final String TABLE_USER = "user";
    private final SQLiteDatabase database;


    public CrudUser(final SQLiteDatabase database) {
        this.database = database;
    }

    public boolean createUser(final User user) {
        final ContentValues values = new ContentValues();
        String name = user.getName();
        if (name == null) {
            name = "new";
        }
        values.put("name", name);
        final long id = database.insert(TABLE_USER, null, values);
        user.setId(id);
        return id != -1;
    }

    public User readUserById(final long id) {
        User user = null;
        final Cursor result = database.query(TABLE_USER, new String[]{"id", "name"}, "id =" + id,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        if (found) {
            user = getNextUser(result);
        }
        result.close();
        return user;
    }

    public List<User> readAllUsers() {
        User user = null;
        ArrayList<User> users = new ArrayList<>();
        final Cursor result = database.query(TABLE_USER, new String[]{"id", "name"}, null,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            user = getNextUser(result);
            users.add(user);
        }
        result.close();
        return users;
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
        String name = user.getName();
        if (name == null) {
            name = "new";
        }
        values.put("name", name);
        return database.update(TABLE_USER, values, "id = " + user.getId(), null) > 0;
    }

    public boolean deleteUserById(final long id) {
        int response = database.delete(TABLE_USER, "id=" + id, null);
        return response > 0;
    }
}
