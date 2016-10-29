package net.ddns.swinterberger.payanotherround.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.swinterberger.payanotherround.entities.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Queries for the Bill-Table.
 *
 * @author Stefan Winterberger
 * @version 1.0
 */
public final class CrudBill {

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_DESCRIPTION = "description";
    private static final String ATTRIBUTE_AMMOUNT = "amount";
    private static final String ATTRIBUTE_CURRENCY = "currency";
    private static final String ATTRIBUTE_FK_TRIP = "fk_trip";
    private static final String ATTRIBUTE_FK_PAYERID = "fk_payer";
    private static final String TABLE_BILL = "bill";
    private final SQLiteDatabase database;


    CrudBill(final SQLiteDatabase database) {
        this.database = database;
    }

    public long createBill(final Bill bill) {
        final ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_DESCRIPTION, bill.getDescription());
        values.put(ATTRIBUTE_AMMOUNT, bill.getAmmount());
        values.put(ATTRIBUTE_CURRENCY, bill.getCurrency());
        values.put(ATTRIBUTE_FK_TRIP, bill.getTripId());
        values.put(ATTRIBUTE_FK_PAYERID, bill.getPayerId());
        final long id = database.insert(TABLE_BILL, null, values);
        bill.setId(id);

        return id;
    }

    public Bill readBillById(final long id) {
        Bill bill = null;
        final Cursor result = database.query(TABLE_BILL, new String[]{ATTRIBUTE_ID, ATTRIBUTE_DESCRIPTION}, ATTRIBUTE_ID + id,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        if (found) {
            bill = getNextBill(result);
        }
        result.close();
        return bill;
    }

    public List<Bill> readBillsByTripId(final long tripId) {
        Bill bill;
        ArrayList<Bill> bills = new ArrayList<>();

        final Cursor result = database.query(TABLE_BILL, new String[]{ATTRIBUTE_ID, ATTRIBUTE_DESCRIPTION},
                ATTRIBUTE_FK_TRIP + " = " + tripId, null, null, null, null);
        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            bill = getNextBill(result);
            bills.add(bill);
        }
        result.close();

        return bills;
    }

    public List<Bill> readAllBills() {
        Bill bill;
        ArrayList<Bill> bills = new ArrayList<>();
        final Cursor result = database.query(TABLE_BILL, new String[]{ATTRIBUTE_ID, ATTRIBUTE_DESCRIPTION}, null,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            bill = getNextBill(result);
            bills.add(bill);
        }
        result.close();
        return bills;
    }

    private Bill getNextBill(final Cursor cursor) {
        final Bill user = new Bill();
        user.setId(cursor.getLong(0));
        user.setDescription(cursor.getString(1));
        cursor.moveToNext();
        return user;
    }

    public boolean updateBill(final Bill user) {
        final ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_DESCRIPTION, user.getDescription());
        return database.update(TABLE_BILL, values, ATTRIBUTE_ID + user.getId(), null) > 0;
    }

    public boolean deleteBillById(final long id) {
        return database.delete(TABLE_BILL, ATTRIBUTE_ID + "=" + id, null) > 0;
    }
}
