package net.ddns.swinterberger.payanotherround.database.queries.simple;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.swinterberger.payanotherround.entities.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Queries for the Trip Table.
 *
 * @author Stefan Winterberger
 * @version 1.0
 */
public final class CrudTrip {

    public static final String TABLE_TRIP = "trip";
    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_NAME = "name";

    private final SQLiteDatabase database;


    public CrudTrip(final SQLiteDatabase database) {
        this.database = database;
    }

    public final long createTrip(final Trip trip) {
        final ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_NAME, trip.getName());
        final long id = database.insert(TABLE_TRIP, null, values);
        trip.setId(id);

        return id;
    }

    public final Trip readTripById(final long id) {
        Trip trip = null;
        final Cursor result = database.query(TABLE_TRIP, new String[]{ATTRIBUTE_ID, ATTRIBUTE_NAME}, "id =" + id,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        if (found) {
            trip = getNextTrip(result);
        }
        result.close();
        return trip;
    }

    public final List<Trip> readAllTrips() {
        Trip trip;
        ArrayList<Trip> users = new ArrayList<>();
        final Cursor result = database.query(TABLE_TRIP, new String[]{ATTRIBUTE_ID, ATTRIBUTE_NAME}, null,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            trip = getNextTrip(result);
            users.add(trip);
        }
        result.close();
        return users;
    }

    private Trip getNextTrip(final Cursor cursor) {
        final Trip user = new Trip();
        user.setId(cursor.getLong(0));
        user.setName(cursor.getString(1));
        cursor.moveToNext();
        return user;
    }

    public final boolean updateTrip(final Trip trip) {
        final ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_NAME, trip.getName());
        return database.update(TABLE_TRIP, values, ATTRIBUTE_ID + "=" + trip.getId(), null) > 0;
    }

    public final boolean deleteTripById(final long id) {
        return database.delete(TABLE_TRIP, ATTRIBUTE_ID + "=" + id, null) > 0;
    }
}
