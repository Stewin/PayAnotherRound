package net.ddns.swinterberger.payanotherround.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.swinterberger.payanotherround.entities.Bill;

/**
 * Created by Stefan on 24.09.2016.
 */
public class CrudBill {

    private final String TABLE_BILL = "bill";
    private final SQLiteDatabase database;


    public CrudBill(final SQLiteDatabase database) {
        this.database = database;
    }

    public boolean createBill(final Bill bill) {
        final ContentValues values = new ContentValues();
        values.put("name", bill.getDescription());
        final long id = database.insert(TABLE_BILL, null, values);
        bill.setId(id);

        return id != -1;
    }

    public Bill readBillById(final long id) {
        Bill bill = null;
        final Cursor result = database.query(TABLE_BILL, new String[]{"id", "name"}, "id =" + id,
                null, null, null, null);
        final boolean found = result.moveToFirst();
        if (found) {
            bill = getNextBill(result);
        }
        result.close();
        return bill;
    }

    private Bill getNextBill(final Cursor cursor) {
        final Bill user = new Bill();
        user.setId(cursor.getLong(0));
        user.setDescription(cursor.getString(1));
        cursor.moveToNext();
        return user;
    }

    public boolean updateUser(final Bill user) {
        final ContentValues values = new ContentValues();
        values.put("name", user.getDescription());
        return database.update(TABLE_BILL, values, "id = " + user.getId(), null) > 0;
    }

    public boolean deleteUserById(final long id) {
        return database.delete(TABLE_BILL, "id=" + id, null) > 0;
    }
}
