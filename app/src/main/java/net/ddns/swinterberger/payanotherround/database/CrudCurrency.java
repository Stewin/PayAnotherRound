package net.ddns.swinterberger.payanotherround.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.swinterberger.payanotherround.currency.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Queries for the Currency-Table.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public final class CrudCurrency {

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_ABBREVIATION = "abbreviation";
    private static final String ATTRIBUTE_EXCHANGE_RATIO = "exchange_ratio";
    private static final String TABLE_CURRENCY = "currency";

    private final SQLiteDatabase database;

    CrudCurrency(final SQLiteDatabase database) {
        this.database = database;
    }


    public final List<Currency> readAllCurrencies() {

        Currency currency;
        ArrayList<Currency> bills = new ArrayList<>();
        final Cursor result = database.query(TABLE_CURRENCY,
                new String[]{ATTRIBUTE_ID, ATTRIBUTE_ABBREVIATION, ATTRIBUTE_EXCHANGE_RATIO},
                null, null, null, null, null);
        final boolean found = result.moveToFirst();
        while (found && !result.isAfterLast()) {
            currency = getNextCurrency(result);
            bills.add(currency);
        }
        result.close();
        return bills;

    }

    public Currency readCurrencyByAbbreviation(final String currencyAbreviation) {
        Currency currency = null;
        final Cursor result = database.query(TABLE_CURRENCY,
                new String[]{ATTRIBUTE_ID, ATTRIBUTE_ABBREVIATION, ATTRIBUTE_EXCHANGE_RATIO},
                ATTRIBUTE_ABBREVIATION + "= \"" + currencyAbreviation + "\"",
                null, null, null, null);
        final boolean found = result.moveToFirst();
        if (found) {
            currency = getNextCurrency(result);
        }
        result.close();
        return currency;
    }

    private Currency getNextCurrency(final Cursor result) {
        Currency currency = new Currency();
        currency.setId(result.getLong(0));
        currency.setCurrencyAbbreviation(result.getString(1));
        currency.setExchangeRatio(result.getFloat(2));

        result.moveToNext();

        return currency;
    }

    public Currency readCurrencyById(final long currencyId) {
        Currency currency = null;
        final Cursor result = database.query(TABLE_CURRENCY,
                new String[]{ATTRIBUTE_ID, ATTRIBUTE_ABBREVIATION, ATTRIBUTE_EXCHANGE_RATIO},
                ATTRIBUTE_ID + "=" + currencyId,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        if (found) {
            currency = getNextCurrency(result);
        }
        result.close();
        return currency;
    }
}
