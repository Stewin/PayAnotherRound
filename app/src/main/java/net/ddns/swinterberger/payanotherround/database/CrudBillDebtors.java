package net.ddns.swinterberger.payanotherround.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database Queries for the Relation Table between Users (as Debtors) and Bills. (n:m-Relationship)
 *
 * @author Stefan Winterberger
 * @version 1.0
 */

public class CrudBillDebtors {

    public static final String TABLE_BILL_DEBTORS = "bill_debtors";

    public static final String ATTRIBUTE_BILL_ID = "fk_bill";
    public static final String ATTRIBUTE_USER_ID = "fk_user";


    private final SQLiteDatabase database;

    CrudBillDebtors(final SQLiteDatabase database) {
        this.database = database;
    }

    public final long createBillDebtor(final long billId, long userId) {
        ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_BILL_ID, billId);
        values.put(ATTRIBUTE_USER_ID, userId);

        long id = database.insert(TABLE_BILL_DEBTORS, null, values);

        return id;
    }
}
