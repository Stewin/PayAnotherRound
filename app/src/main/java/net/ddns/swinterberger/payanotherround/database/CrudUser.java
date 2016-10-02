package net.ddns.swinterberger.payanotherround.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.ddns.swinterberger.payanotherround.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Queries for the Users Table.
 *
 * @author Stefan Winterberger
 * @version 1.0
 */
public final class CrudUser {

    public static final String TABLE_USER = "user";
    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_COLOR = "color";
    public static final String ATTRIBUTE_PICTUREID = "pictureid";


    private final SQLiteDatabase database;


    CrudUser(final SQLiteDatabase database) {
        this.database = database;
    }

    public long createUser(final User user) {
        final ContentValues values = new ContentValues();
        String name = user.getName();
        if (name == null) {
            name = "new";
        }
        values.put(ATTRIBUTE_NAME, name);
        final long id = database.insert(TABLE_USER, null, values);
        user.setId(id);
        return id;
    }

    public final User readUserById(final long id) {
        User user = null;
        final Cursor result = database.query(TABLE_USER, new String[]{ATTRIBUTE_ID, ATTRIBUTE_NAME}, ATTRIBUTE_ID + "=" + id,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        if (found) {
            user = getNextUser(result);
        }
        result.close();
        return user;
    }

    public final List<User> readAllUsers() {
        User user;
        ArrayList<User> users = new ArrayList<>();
        final Cursor result = database.query(TABLE_USER, new String[]{ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_COLOR, ATTRIBUTE_PICTUREID}, null,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            user = getNextUser(result);
            users.add(user);
        }
        result.close();
        return users;
    }

    public final List<User> readUsersByTripId(final long tripId) {
        User user;
        ArrayList<User> users = new ArrayList<>();

        final String query = "SELECT * " +
                "FROM " + TABLE_USER + " as user," + CrudAttend.TABLE_ATTEND + " as attend," + CrudTrip.TABLE_TRIP + " as trip "
                + "WHERE user." + CrudUser.ATTRIBUTE_ID + " = attend." + CrudAttend.ATTRIBUTE_USER_ID
                + " AND attend." + CrudAttend.ATTRIBUTE_TRIP_ID + " = " + "trip." + CrudTrip.ATTRIBUTE_ID
                + " AND trip." + CrudTrip.ATTRIBUTE_ID + " = " + tripId;
        Log.i("DB-Query", query);


        final Cursor result = database.rawQuery(query, null);
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

    public final boolean updateUser(final User user) {
        final ContentValues values = new ContentValues();
        String name = user.getName();
        if (name == null) {
            name = "new";
        }
        values.put(ATTRIBUTE_NAME, name);
        return database.update(TABLE_USER, values, "id = " + user.getId(), null) > 0;
    }


    public final boolean deleteUserById(final long id) {
        int response = database.delete(TABLE_USER, ATTRIBUTE_ID + "=" + id, null);
        return response > 0;
    }
}
