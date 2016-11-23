package net.ddns.swinterberger.payanotherround.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.ddns.swinterberger.payanotherround.R;
import net.ddns.swinterberger.payanotherround.currency.Currency;
import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.entities.Debt;
import net.ddns.swinterberger.payanotherround.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to Create a Summary for a User.
 *
 * @author Stefan Winteberger
 * @version 1.0
 */
public final class UserSummary extends AppCompatActivity {

    private ListView userListView;
    private List<Debt> debts = new ArrayList<>();
    private List<User> debtors = new ArrayList<>();

    private long tripId;


    private DbAdapter dbAdapter = new DbAdapter(this);
    private String currentCurrency;
    private Currency currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_summary);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        long userId = getIntent().getLongExtra(getResources().getString(R.string.extra_userId), -1L);
        User user = dbAdapter.getCrudUser().readUserById(userId);

        tripId = getIntent().getLongExtra(getResources().getString(R.string.extra_tripid), -1);

        TextView tvUserName = (TextView) findViewById(R.id.tv_UserName);
        tvUserName.setText(user.getName());

        collectUserDebtsFor(userId);
        refreshListView();
    }

    private void collectUserDebtsFor(final long userId) {

        //Get the Currency to Display.
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        currentCurrency = preferences.getString(getResources().getString(R.string.preferncekey_current_currency), "CHF");
        currency = dbAdapter.getCrudCurrency().readCurrencyByAbbreviation(currentCurrency);


        //Collect User attending Trip.
        List<Long> userIds = dbAdapter.getCrudAttend().readUsersByTripId(tripId);
        for (long userId1 : userIds) {
            if (userId1 != userId) {
                User debtor = dbAdapter.getCrudUser().readUserById(userId1);
                debtors.add(debtor);
            }
        }

        for (User user : debtors) {

            int amountInCent = 0;

            //Get Debts for User
            List<Debt> tempDebts = dbAdapter.getCrudDebt().readDebtByCreditAndDebtor(userId, user.getId());
            for (Debt debt : tempDebts) {
                amountInCent += debt.getAmountInCent();
            }

            //And subtract Credits for user.
            List<Debt> tempDebts2 = dbAdapter.getCrudDebt().readDebtByCreditAndDebtor(user.getId(), userId);
            for (Debt debt : tempDebts2) {
                amountInCent -= debt.getAmountInCent();
            }
            debts.add(new Debt(userId, userId, 0L, amountInCent));
        }
    }

    private void refreshListView() {
        if (userListView == null) {
            userListView = (ListView) findViewById(R.id.lv_Users);
        }
        userListView.setAdapter(new UserAsDebtorListItemAdapter());
    }

    private class UserAsDebtorListItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return debts.size();
        }

        @Override
        public Object getItem(final int position) {
            return debts.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return Long.parseLong(debts.get(position).getCreditorId() + "" + debts.get(position).getDebtorId());
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            View returnView = convertView;

            if (returnView == null) {
                returnView = UserSummary.this.getLayoutInflater().inflate(R.layout.listitem_user_as_debtor, null);

                TextView tvName = (TextView) returnView.findViewById(R.id.tv_CreditorName);
                tvName.setText(debtors.get(position).getName());

                TextView tvAmount = (TextView) returnView.findViewById(R.id.tv_DebtingAmount);
                String valueAsString = currency.amountToString(debts.get(position).getAmountInCent());
                tvAmount.setText(valueAsString);
            }
            return returnView;
        }
    }
}
