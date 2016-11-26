package net.ddns.swinterberger.payanotherround.database.queries.simple;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Queries for the Relation Table between Users (as Debtors) and Bills. (n:m-Relationship)
 *
 * @author Stefan Winterberger
 * @version 1.0
 */
public final class CrudBillDebtors {

    private static final String TABLE_BILL_DEBTORS = "bill_debtors";

    private static final String ATTRIBUTE_BILL_ID = "fk_bill";
    private static final String ATTRIBUTE_USER_ID = "fk_user";


    private final SQLiteDatabase database;

    public CrudBillDebtors(final SQLiteDatabase database) {
        this.database = database;
    }

    public final long createBillDebtor(final long billId, long userId) {
        ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_BILL_ID, billId);
        values.put(ATTRIBUTE_USER_ID, userId);

        return database.insert(TABLE_BILL_DEBTORS, null, values);
    }

    public List<Long> readDebtorsByBillId(final long billId) {
        long user;
        ArrayList<Long> users = new ArrayList<>();

        final Cursor result = database.query(TABLE_BILL_DEBTORS,
                new String[]{ATTRIBUTE_BILL_ID, ATTRIBUTE_USER_ID},
                ATTRIBUTE_BILL_ID + "=" + billId,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            user = result.getLong(1);
            users.add(user);
            result.moveToNext();
        }
        result.close();
        return users;
    }

    public List<Long> readBillsByDebtorId(final long debtorId) {
        long bill;
        ArrayList<Long> bills = new ArrayList<>();

        final Cursor result = database.query(TABLE_BILL_DEBTORS,
                new String[]{ATTRIBUTE_BILL_ID, ATTRIBUTE_USER_ID},
                ATTRIBUTE_USER_ID + "=" + debtorId,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            bill = result.getLong(0);
            bills.add(bill);
            result.moveToNext();
        }
        result.close();
        return bills;
    }

    public boolean deleteBillDebtorByBillId(final long billId) {
        return database.delete(TABLE_BILL_DEBTORS, ATTRIBUTE_BILL_ID + "=" + billId, null) > 0;
    }

    public boolean deleteBillDebtorByUserId(final long userId) {
        return database.delete(TABLE_BILL_DEBTORS, ATTRIBUTE_USER_ID + "=" + userId, null) > 0;
    }
}
