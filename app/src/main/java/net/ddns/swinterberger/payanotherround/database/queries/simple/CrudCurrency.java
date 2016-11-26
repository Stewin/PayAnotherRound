package net.ddns.swinterberger.payanotherround.database.queries.simple;

import android.content.ContentValues;
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

    public CrudCurrency(final SQLiteDatabase database) {
        this.database = database;
    }


    public long createCurrency(final Currency currency) {
        ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_ABBREVIATION, currency.getCurrencyAbbreviation());
        values.put(ATTRIBUTE_EXCHANGE_RATIO, currency.getExchangeRatio());

        return database.insert(TABLE_CURRENCY, null, values);
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

    public boolean updateCurrency(final Currency currency) {
        final ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_ABBREVIATION, currency.getCurrencyAbbreviation());
        values.put(ATTRIBUTE_EXCHANGE_RATIO, currency.getExchangeRatio());
        return database.update(TABLE_CURRENCY, values, ATTRIBUTE_ID + "=" + currency.getId(), null) > 0;
    }

    private Currency getNextCurrency(final Cursor result) {
        Currency currency = new Currency();
        currency.setId(result.getLong(0));
        currency.setCurrencyAbbreviation(result.getString(1));
        currency.setExchangeRatio(result.getFloat(2));

        result.moveToNext();

        return currency;
    }
}
