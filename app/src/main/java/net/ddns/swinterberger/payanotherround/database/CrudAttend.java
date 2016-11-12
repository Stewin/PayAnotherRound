package net.ddns.swinterberger.payanotherround.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

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

    public final boolean deleteAttend(final long userId, final long tripId) {
        int rowsAffected = database.delete(TABLE_ATTEND, userId + " = " + ATTRIBUTE_USER_ID + " AND " + tripId + " = " + ATTRIBUTE_TRIP_ID, null);
        return rowsAffected > 0;
    }

    public boolean deleteAttendByTripId(final long tripId) {
        int rowsAffected = database.delete(TABLE_ATTEND, tripId + " = " + ATTRIBUTE_TRIP_ID, null);
        return rowsAffected > 0;
    }
}
