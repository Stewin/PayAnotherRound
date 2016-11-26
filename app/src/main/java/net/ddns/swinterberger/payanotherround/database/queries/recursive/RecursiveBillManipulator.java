package net.ddns.swinterberger.payanotherround.database.queries.recursive;

import android.content.Context;

import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.entities.Bill;

import java.util.List;

/**
 * This Class provides Methods to Recursively Manipulate the Bill Entity on the DB.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public final class RecursiveBillManipulator {

    private DbAdapter dbAdapter;

    public RecursiveBillManipulator(final Context context) {
        this.dbAdapter = new DbAdapter(context);
    }

    public void deleteBillRecursiveById(final long billId) {
        //1. Delete Debts by Bill Id
        dbAdapter.getCrudDebt().deleteDebtByBillId(billId);

        //2. Delete Bill_Debtors by BillId
        dbAdapter.getCrudBillDebtor().deleteBillDebtorByBillId(billId);

        //3. Delete Bill
        dbAdapter.getCrudBill().deleteBillById(billId);
    }

    public int deleteBillRecursiveByTripId(final long tripId) {

        List<Bill> bills = dbAdapter.getCrudBill().readBillsByTripId(tripId);
        for (Bill b : bills) {
            deleteBillRecursiveById(b.getId());
        }
        return 0;
    }

    public int deleteBillRecursiveByUserId(final long userId) {

        //Delete Bills where User is Creditor.
        List<Bill> bills = dbAdapter.getCrudBill().readBillsByCreditorId(userId);
        for (Bill b : bills) {
            deleteBillRecursiveById(b.getId());
        }

        //Remove User from Bills as Debtor.
        dbAdapter.getCrudBillDebtor().deleteBillDebtorByUserId(userId);

        //TODO: ERWEITERUNG Update the Debt-Entries for recalculated  Debts from the Bill where the user is removed.
        //Alternative. Decide that its forbidden to remove Users when there are open bills.

        return 0;
    }

}
