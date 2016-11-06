package net.ddns.swinterberger.payanotherround.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Adapter Class for Queries to the Database.
 *
 * @author Stefan Winterberger
 * @version 1.0
 */
public final class DbAdapter {

    private final DbHelper dbHelper;

    private CrudUser crudUser;
    private CrudBill crudBill;
    private CrudTrip crudTrip;
    private CrudAttend crudAttend;
    private CrudBillDebtors crudBillDebtor;
    private CrudDebt crudDebt;

    private SQLiteDatabase db;

    public DbAdapter(final Context context) {
        dbHelper = new DbHelper(context, "ParDatabase", null, 1);
    }

    public final void open() {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    public final void close() {
        dbHelper.close();
    }

    public final CrudUser getCrudUser() {
        open();
        if (crudUser == null) {
            crudUser = new CrudUser(db);
        }
        return crudUser;
    }

    public final CrudBill getCrudBill() {
        open();
        if (crudBill == null) {
            crudBill = new CrudBill(db);
        }
        return crudBill;
    }

    public final CrudTrip getCrudTrip() {
        open();
        if (crudTrip == null) {
            crudTrip = new CrudTrip(db);
        }
        return crudTrip;
    }

    public final CrudAttend getCrudAttend() {
        open();
        if (crudAttend == null) {
            crudAttend = new CrudAttend(db);
        }
        return crudAttend;
    }

    public final CrudBillDebtors getCrudBillDebtor() {
        open();
        if (crudBillDebtor == null) {
            crudBillDebtor = new CrudBillDebtors(db);
        }
        return crudBillDebtor;
    }

    public final CrudDebt getCrudDebt() {
        open();
        if (crudDebt == null) {
            crudDebt = new CrudDebt(db);
        }
        return crudDebt;
    }
}
