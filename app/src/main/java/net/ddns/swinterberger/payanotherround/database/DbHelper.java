package net.ddns.swinterberger.payanotherround.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper Class for access the Database.
 *
 * @author Stefan Winterberger
 * @version 1.0
 */
public final class DbHelper extends SQLiteOpenHelper {

    public DbHelper(final Context context, final String name,
                    final SQLiteDatabase.CursorFactory factory, final int version) {
        super(context, name, factory, version);
    }

    @Override
    public final void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY, name TEXT NOT NULL, color TEXT, pictureid TEXT)");
        db.execSQL("CREATE TABLE trip (id INTEGER PRIMARY KEY, name TEXT not null)");
        db.execSQL("CREATE TABLE currency (id INTEGER PRIMARY KEY, abbreviation TEXT NOT NULL, exchange_ratio REAL NOT NULL)");
        db.execSQL("CREATE TABLE bill (id INTEGER PRIMARY KEY, description TEXT not null, amountInCent INTEGER NOT NULL, fk_currency INTEGER NOT NULL, fk_trip INTEGER NOT NULL, fk_payer INTEGER NOT NULL, FOREIGN KEY(fk_trip) REFERENCES trip(id), FOREIGN KEY (fk_payer) REFERENCES user(id), FOREIGN KEY (fk_currency) REFERENCES currency(id))");
        db.execSQL("CREATE TABLE attend (fk_user INTEGER NOT NULL, fk_trip INTEGER NOT NULL, FOREIGN KEY (fk_user) REFERENCES user(id), FOREIGN KEY (fk_trip) REFERENCES trip(id), CONSTRAINT pk_attendid PRIMARY KEY (fk_user, fk_trip))");
        db.execSQL("CREATE TABLE debt (fk_creditor INTEGER NOT NULL, fk_debtor INTEGER NOT NULL, fk_bill int not null, amount_in_cent int not null, FOREIGN KEY (fk_creditor) REFERENCES user(id), FOREIGN KEY (fk_debtor) REFERENCES user(id), FOREIGN KEY (fk_bill) REFERENCES bill(id), CONSTRAINT pk_debtid PRIMARY KEY (fk_creditor, fk_debtor, fk_bill))");
        db.execSQL("CREATE TABLE bill_debtors (fk_bill INTEGER NOT NULL, fk_user INTEGER NOT NULL, FOREIGN KEY (fk_bill) REFERENCES bill(id), FOREIGN KEY (fk_user) REFERENCES user(id), CONSTRAINT pk_billdebtorid PRIMARY KEY (fk_bill, fk_user))");

        //Default Data
        db.execSQL("INSERT INTO currency (abbreviation, exchange_ratio) VALUES (\"CHF\", 1.0)");
        db.execSQL("INSERT INTO currency (abbreviation, exchange_ratio) VALUES (\"EUR\", 1.2)");
        db.execSQL("INSERT INTO currency (abbreviation, exchange_ratio) VALUES (\"USD\", 1.01)");

    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }
}
