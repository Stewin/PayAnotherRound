package net.ddns.swinterberger.payanotherround.database.queries.recursive;

import android.content.Context;

import net.ddns.swinterberger.payanotherround.database.DbAdapter;

/**
 * This Class provides Methods to Recursively Manipulate the User Entity on the DB.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public class RecursiveUserManipulator {

    private DbAdapter dbAdapter;
    private Context context;

    public RecursiveUserManipulator(final Context context) {
        this.context = context;
        this.dbAdapter = new DbAdapter(context);
    }

    /**
     * Deletes a User if no Debts are open from him.
     *
     * @param userId User to delete.
     * @return 0 if Successful -1 otherwise.
     */
    public final int deleteUserRecursiveById(final long userId) {

        if (new RecursiveBillManipulator(context).deleteBillByUserIdIfNoDebtsAreLeft(userId) == 0) {
            dbAdapter.getCrudAttend().deleteAttendByUserId(userId);
            dbAdapter.getCrudUser().deleteUserById(userId);
            return 0;
        } else {
            return -1;
        }
    }

}
