package net.ddns.swinterberger.payanotherround.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.swinterberger.payanotherround.entities.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 24.09.2016.
 */
public class CrudTrip {

    private final String TABLE_TRIP = "trip";
    private final SQLiteDatabase database;


    public CrudTrip(final SQLiteDatabase database) {
        this.database = database;
    }

    public boolean createTrip(final Trip trip) {
        final ContentValues values = new ContentValues();
        values.put("name", trip.getName());
        final long id = database.insert(TABLE_TRIP, null, values);
        trip.setId(id);

        return id != -1;
    }

    public Trip readTripById(final long id) {
        Trip trip = null;
        final Cursor result = database.query(TABLE_TRIP, new String[]{"id", "name"}, "id =" + id,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        if (found) {
            trip = getNextTrip(result);
        }
        result.close();
        return trip;
    }

    public List<Trip> readAllTrips() {
        Trip trip = null;
        ArrayList<Trip> users = new ArrayList<>();
        final Cursor result = database.query(TABLE_TRIP, new String[]{"id", "name"}, null,
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

    public boolean updateTrip(final Trip user) {
        final ContentValues values = new ContentValues();
        values.put("name", user.getName());
        return database.update(TABLE_TRIP, values, "id = " + user.getId(), null) > 0;
    }

    public boolean deleteTripById(final long id) {
        return database.delete(TABLE_TRIP, "id=" + id, null) > 0;
    }
}
