package net.ddns.swinterberger.payanotherround.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Queries for the Relation Table between Users and Trips. (n:m-Relationship)
 *
 * @author Stefan Winterberger
 * @version 1.0
 */

public final class CrudAttend {

    public static final String TABLE_ATTEND = "attend";

    public static final String ATTRIBUTE_USER_ID = "fk_user";
    public static final String ATTRIBUTE_TRIP_ID = "fk_trip";


    private final SQLiteDatabase database;

    CrudAttend(final SQLiteDatabase database) {
        this.database = database;
    }

    public final long createAttend(final long userId, long tripId) {
        ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_USER_ID, userId);
        values.put(ATTRIBUTE_TRIP_ID, tripId);

        return database.insert(TABLE_ATTEND, null, values);
    }

    public List<Long> readUsersByTripId(final long tripId) {
        List<Long> userIds = new ArrayList<>();

        final Cursor result = database.query(TABLE_ATTEND, new String[]{ATTRIBUTE_USER_ID}, ATTRIBUTE_TRIP_ID + "=" + tripId,
                null, null, null, null);
        final boolean found = result.moveToFirst();

        while (found && !result.isAfterLast()) {
            userIds.add(result.getLong(0));
            result.moveToNext();
        }
        result.close();
        return userIds;
    }

    public final boolean deleteAttend(final long userId, final long tripId) {
        int rowsAffected = database.delete(TABLE_ATTEND, userId + " = " + ATTRIBUTE_USER_ID + " AND " + tripId + " = " + ATTRIBUTE_TRIP_ID, null);
        return rowsAffected > 0;
    }

    public boolean deleteAttendByTripId(final long tripId) {
        int rowsAffected = database.delete(TABLE_ATTEND, tripId + " = " + ATTRIBUTE_TRIP_ID, null);
        return rowsAffected > 0;
    }
}
