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
import net.ddns.swinterberger.payanotherround.currency.CurrencyFactory;
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
        String currentCurrency = preferences.getString(getResources().getString(R.string.preferncekey_current_currency), "CHF");


        //Collect User attending Trip.
        List<Long> userIds = dbAdapter.getCrudAttend().readUsersByTripId(tripId);
        for (long userId1 : userIds) {
            if (userId1 != userId) {
                User debtor = dbAdapter.getCrudUser().readUserById(userId1);
                debtors.add(debtor);
            }
        }

        for (User user : debtors) {

            Currency totalAmount = CurrencyFactory.getCurrencyOfType(currentCurrency);

            //Get Debts for User
            Debt debt = dbAdapter.getCrudDebt().readDebtByPrimaryKey(userId, user.getId());
            if (debt != null) {
                totalAmount = debt.getAmount();
            } else {
                totalAmount.setAmount(0);
            }

            //And subtract Credits for user.
            Debt debt2 = dbAdapter.getCrudDebt().readDebtByPrimaryKey(user.getId(), userId);
            if (debt2 != null) {
                Currency amount = debt2.getAmount();
                totalAmount.subtractAmount(amount.getAmountInCent());
            }
            debts.add(new Debt(userId, userId, totalAmount));
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
                tvAmount.setText(debts.get(position).getAmount().toString());

            }
            return returnView;

            //TODO: OnLongClick Delete payed Debts and update DB.
        }
    }
}
