package net.ddns.swinterberger.payanotherround.database.queries.recursive;

import android.content.Context;

import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.entities.Bill;
import net.ddns.swinterberger.payanotherround.entities.Debt;

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

        return 0;
    }

    /**
     * Deletes a User only if the User has no Debts open.
     *
     * @param userId User to delete.
     * @return 0 if Successful -1 otherwise.
     */
    public int deleteBillByUserIdIfNoDebtsAreLeft(final long userId) {

        //Check if User still have Debts.
        List<Debt> debts = dbAdapter.getCrudDebt().readDebtByDebtor(userId);
        if (!debts.isEmpty()) {
            return -1;
        } else {
            return deleteBillRecursiveByUserId(userId);
        }
    }

}
