package net.ddns.swinterberger.payanotherround.database.queries.simple;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.swinterberger.payanotherround.entities.Debt;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Queries for the Relation Table between User and User with a debt amount.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public final class CrudDebt {

    private static final String TABLE_DEBT = "debt";

    private static final String ATTRIBUTE_CREDITOR = "fk_creditor";
    private static final String ATTRIBUTE_DEBTOR = "fk_debtor";
    private static final String ATTRIBUTE_BILL = "fk_bill";
    private static final String ATTRIBUTE_AMOUNT = "amount_in_cent";


    private final SQLiteDatabase database;

    public CrudDebt(final SQLiteDatabase database) {
        this.database = database;
    }

    public final long createDebt(final long creditorId, final long debtorId, final long billId, final int amountInCent) {
        ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_CREDITOR, creditorId);
        values.put(ATTRIBUTE_DEBTOR, debtorId);
        values.put(ATTRIBUTE_BILL, billId);
        values.put(ATTRIBUTE_AMOUNT, amountInCent);

        return database.insert(TABLE_DEBT, null, values);
    }

    public final List<Debt> readDebtByCreditAndDebtor(final long creditorId, final long debtorId) {

        List<Debt> debts = new ArrayList<>();
        Debt debt;
        final Cursor result = database.query(TABLE_DEBT,
                new String[]{ATTRIBUTE_CREDITOR, ATTRIBUTE_DEBTOR, ATTRIBUTE_BILL, ATTRIBUTE_AMOUNT},
                ATTRIBUTE_CREDITOR + "=" + creditorId + " AND "
                        + ATTRIBUTE_DEBTOR + " = " + debtorId, null, null, null, null);

        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            debt = getNextDebt(result);
            debts.add(debt);
        }
        result.close();

        return debts;
    }

    public List<Debt> readDebtByDebtor(final long userId) {
        List<Debt> debts = new ArrayList<>();
        Debt debt;
        final Cursor result = database.query(TABLE_DEBT,
                new String[]{ATTRIBUTE_CREDITOR, ATTRIBUTE_DEBTOR, ATTRIBUTE_BILL, ATTRIBUTE_AMOUNT},
                ATTRIBUTE_DEBTOR + " = " + userId, null, null, null, null);

        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            debt = getNextDebt(result);
            debts.add(debt);
        }
        result.close();

        return debts;
    }

    private Debt getNextDebt(final Cursor cursor) {

        int creditorId = cursor.getInt(0);
        int debtorId = cursor.getInt(1);
        int billId = cursor.getInt(2);
        int amountInCent = cursor.getInt(3);

        cursor.moveToNext();

        return new Debt(creditorId, debtorId, billId, amountInCent);
    }

    public final boolean updateDebt(final Debt debt) {
        final ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_BILL, debt.getBillId());
        return database.update(TABLE_DEBT, values, ATTRIBUTE_CREDITOR + "=" + debt.getCreditorId() + " AND "
                + ATTRIBUTE_DEBTOR + " = " + debt.getDebtorId(), null) > 0;
    }

    public boolean deleteDebtByBillId(final long billId) {
        return database.delete(TABLE_DEBT, ATTRIBUTE_BILL + "=" + billId, null) > 0;
    }
}
