package net.ddns.swinterberger.payanotherround.database.queries.simple;

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
 * @version 1.0.0
 */
public final class CrudBill {

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_DESCRIPTION = "description";
    private static final String ATTRIBUTE_AMOUNT_INTEGER = "amountInCent";
    private static final String ATTRIBUTE_CURRENCY = "fk_currency";
    private static final String ATTRIBUTE_FK_TRIP = "fk_trip";
    private static final String ATTRIBUTE_FK_PAYERID = "fk_payer";
    private static final String TABLE_BILL = "bill";
    private final SQLiteDatabase database;


    public CrudBill(final SQLiteDatabase database) {
        this.database = database;
    }

    public long createBill(final Bill bill) {
        final ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_DESCRIPTION, bill.getDescription());
        values.put(ATTRIBUTE_AMOUNT_INTEGER, bill.getAmountInCent());
        values.put(ATTRIBUTE_CURRENCY, bill.getCurrencyId());
        values.put(ATTRIBUTE_FK_TRIP, bill.getTripId());
        values.put(ATTRIBUTE_FK_PAYERID, bill.getPayerId());
        final long id = database.insert(TABLE_BILL, null, values);
        bill.setId(id);

        return id;
    }

    public Bill readBillById(final long id) {
        Bill bill = null;
        final Cursor result = database.query(TABLE_BILL,
                new String[]{ATTRIBUTE_ID, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_AMOUNT_INTEGER, ATTRIBUTE_CURRENCY, ATTRIBUTE_FK_PAYERID, ATTRIBUTE_FK_TRIP},
                ATTRIBUTE_ID + "=" + id,
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

        final Cursor result = database.query(TABLE_BILL,
                new String[]{ATTRIBUTE_ID, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_AMOUNT_INTEGER, ATTRIBUTE_CURRENCY, ATTRIBUTE_FK_PAYERID, ATTRIBUTE_FK_TRIP},
                ATTRIBUTE_FK_TRIP + " = " + tripId,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            bill = getNextBill(result);
            bills.add(bill);
        }
        result.close();

        return bills;
    }

    public final List<Bill> readBillsByCreditorId(final long userId) {
        Bill bill;
        ArrayList<Bill> bills = new ArrayList<>();

        final Cursor result = database.query(TABLE_BILL,
                new String[]{ATTRIBUTE_ID, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_AMOUNT_INTEGER, ATTRIBUTE_CURRENCY, ATTRIBUTE_FK_PAYERID, ATTRIBUTE_FK_TRIP},
                ATTRIBUTE_FK_PAYERID + " = " + userId,
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
        final Bill bill = new Bill();
        bill.setId(cursor.getLong(0));
        bill.setDescription(cursor.getString(1));
        bill.setCurrencyId(cursor.getLong(3));
        bill.setAmountInCent(cursor.getInt(2));
        bill.setPayerId(cursor.getInt(4));
        bill.setTripId(cursor.getInt(5));
        cursor.moveToNext();
        return bill;
    }

    public boolean deleteBillById(final long id) {
        return database.delete(TABLE_BILL, ATTRIBUTE_ID + "=" + id, null) > 0;
    }

    public boolean deleteBillByTripId(final long tripId) {
        return database.delete(TABLE_BILL, ATTRIBUTE_FK_TRIP + "=" + tripId, null) > 0;
    }
}
