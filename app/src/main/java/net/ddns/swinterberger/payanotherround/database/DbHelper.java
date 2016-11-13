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
        db.execSQL("CREATE TABLE bill (id INTEGER PRIMARY KEY, description TEXT not null, amount REAL NOT NULL, currency TEXT NOT NULL, fk_trip INTEGER NOT NULL, fk_payer INTEGER NOT NULL, FOREIGN KEY(fk_trip) REFERENCES trip(id), FOREIGN KEY (fk_payer) REFERENCES user(id))");
        db.execSQL("CREATE TABLE attend (fk_user INTEGER NOT NULL, fk_trip INTEGER NOT NULL, FOREIGN KEY (fk_user) REFERENCES user(id), FOREIGN KEY (fk_trip) REFERENCES trip(id), CONSTRAINT pk_attendid PRIMARY KEY (fk_user, fk_trip))");
        db.execSQL("CREATE TABLE debt (fk_creditor INTEGER NOT NULL, fk_debtor INTEGER NOT NULL, amountIntegerPart int not null, amountDecimalPart int not null, FOREIGN KEY (fk_creditor) REFERENCES user(id), FOREIGN KEY (fk_debtor) REFERENCES user(id), CONSTRAINT pk_debtid PRIMARY KEY (fk_creditor, fk_debtor))");
        db.execSQL("CREATE TABLE bill_debtors (fk_bill INTEGER NOT NULL, fk_user INTEGER NOT NULL, FOREIGN KEY (fk_bill) REFERENCES bill(id), FOREIGN KEY (fk_user) REFERENCES user(id), CONSTRAINT pk_billdebtorid PRIMARY KEY (fk_bill, fk_user))");
    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }
}
