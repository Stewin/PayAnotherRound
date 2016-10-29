package net.ddns.swinterberger.payanotherround.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.swinterberger.payanotherround.entities.Debt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 29.10.2016.
 */

public class CrudDebt {

    public static final String TABLE_DEBT = "debt";

    public static final String ATTRIBUTE_CREDITOR = "fk_creditor";
    public static final String ATTRIBUTE_DEBTOR = "fk_debtor";
    public static final String ATTRIBUTE_AMOUNT = "amount";


    private final SQLiteDatabase database;

    CrudDebt(final SQLiteDatabase database) {
        this.database = database;
    }

    public final long createDebt(final long creditorId, long debtorId, long amount) {
        ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_CREDITOR, creditorId);
        values.put(ATTRIBUTE_DEBTOR, debtorId);
        values.put(ATTRIBUTE_AMOUNT, amount);

        long id = database.insert(TABLE_DEBT, null, values);

        return id;
    }

    public final Debt readDebtByPrimaryKey(long creditorId, long debtorId) {

        Debt debt = null;
        final Cursor result = database.query(TABLE_DEBT,
                new String[]{ATTRIBUTE_CREDITOR, ATTRIBUTE_DEBTOR, ATTRIBUTE_AMOUNT},
                ATTRIBUTE_CREDITOR + "=" + creditorId + " AND "
                        + ATTRIBUTE_DEBTOR + " = " + debtorId, null, null, null, null);

        final boolean found = result.moveToFirst();
        if (found) {
            debt = getNextDebt(result);
        }
        return debt;
    }

    private Debt getNextDebt(final Cursor cursor) {

        int creditoId = cursor.getInt(0);
        int debtorId = cursor.getInt(1);
        int amount = cursor.getInt(2);
        cursor.moveToNext();

        final Debt debt = new Debt(creditoId, debtorId, amount);
        return debt;
    }

    public boolean updateDebt(final Debt debt) {
        final ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_AMOUNT, debt.getAmmount());
        return database.update(TABLE_DEBT, values, ATTRIBUTE_CREDITOR + "=" + debt.getCreditorId() + " AND "
                + ATTRIBUTE_DEBTOR + " = " + debt.getDebitorId(), null) > 0;
    }

    public List<Debt> readDebtByCreditorId(long userId) {
        Debt debt;
        List<Debt> debts = new ArrayList<>();

        final Cursor result = database.query(TABLE_DEBT,
                new String[]{ATTRIBUTE_CREDITOR, ATTRIBUTE_DEBTOR, ATTRIBUTE_AMOUNT},
                ATTRIBUTE_CREDITOR + "=" + userId,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            debt = getNextDebt(result);
            debts.add(debt);
        }
        result.close();

        return debts;
    }
}
