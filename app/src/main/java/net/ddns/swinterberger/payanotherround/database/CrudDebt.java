package net.ddns.swinterberger.payanotherround.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.swinterberger.payanotherround.currency.Currency;
import net.ddns.swinterberger.payanotherround.currency.SwissFranc;
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
    private static final String AMOUNT_INTEGER_PART = "amountIntegerPart";
    private static final String AMOUNT_DECIMAL_PART = "amountDecimalPart";


    private final SQLiteDatabase database;

    CrudDebt(final SQLiteDatabase database) {
        this.database = database;
    }

    public final long createDebt(final long creditorId, final long debtorId, final int amountIntegerPart, final int amountDecimalPart) {
        ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_CREDITOR, creditorId);
        values.put(ATTRIBUTE_DEBTOR, debtorId);
        values.put(AMOUNT_INTEGER_PART, amountIntegerPart);
        values.put(AMOUNT_DECIMAL_PART, amountDecimalPart);

        return database.insert(TABLE_DEBT, null, values);
    }

    public final Debt readDebtByPrimaryKey(final long creditorId, final long debtorId) {

        Debt debt = null;
        final Cursor result = database.query(TABLE_DEBT,
                new String[]{ATTRIBUTE_CREDITOR, ATTRIBUTE_DEBTOR, AMOUNT_INTEGER_PART, AMOUNT_DECIMAL_PART},
                ATTRIBUTE_CREDITOR + "=" + creditorId + " AND "
                        + ATTRIBUTE_DEBTOR + " = " + debtorId, null, null, null, null);

        final boolean found = result.moveToFirst();
        if (found) {
            debt = getNextDebt(result);
        }
        return debt;
    }

    private Debt getNextDebt(final Cursor cursor) {

        int creditorId = cursor.getInt(0);
        int debtorId = cursor.getInt(1);

        Currency amount = new SwissFranc();
        amount.setAmount(cursor.getInt(2), cursor.getInt(3));
        cursor.moveToNext();

        return new Debt(creditorId, debtorId, amount);
    }

    public final boolean updateDebt(final Debt debt) {
        final ContentValues values = new ContentValues();
        values.put(AMOUNT_INTEGER_PART, debt.getAmount().getIntPart());
        values.put(AMOUNT_DECIMAL_PART, debt.getAmount().getDecimalPart());
        return database.update(TABLE_DEBT, values, ATTRIBUTE_CREDITOR + "=" + debt.getCreditorId() + " AND "
                + ATTRIBUTE_DEBTOR + " = " + debt.getDebtorId(), null) > 0;
    }

    public final List<Debt> readDebtByCreditorId(final long userId) {
        Debt debt;
        List<Debt> debts = new ArrayList<>();

        final Cursor result = database.query(TABLE_DEBT,
                new String[]{ATTRIBUTE_CREDITOR, ATTRIBUTE_DEBTOR, AMOUNT_INTEGER_PART, AMOUNT_DECIMAL_PART},
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

    public List<Debt> readDebtByDebtorId(final long userId) {
        Debt debt;
        List<Debt> debts = new ArrayList<>();

        final Cursor result = database.query(TABLE_DEBT,
                new String[]{ATTRIBUTE_CREDITOR, ATTRIBUTE_DEBTOR, AMOUNT_INTEGER_PART, AMOUNT_DECIMAL_PART},
                ATTRIBUTE_DEBTOR + "=" + userId,
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
