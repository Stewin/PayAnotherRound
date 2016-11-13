package net.ddns.swinterberger.payanotherround.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.ddns.swinterberger.payanotherround.R;
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
    private List<Debt> debts;
    private List<User> debtors = new ArrayList<>();


    private DbAdapter dbAdapter = new DbAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_summary);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        long userId = getIntent().getLongExtra(getResources().getString(R.string.extra_userId), -1L);
        User user = dbAdapter.getCrudUser().readUserById(userId);


        TextView tvUserName = (TextView) findViewById(R.id.tv_UserName);
        tvUserName.setText(user.getName());

        collectUserDebtsFor(userId);
        refreshListView();
    }

    private void collectUserDebtsFor(final long userId) {

        //TODO: Anderen Ansatz wählen. Für jeden Benutzer der dem Trip zugeordnet ist... (Attend Tabelle)

        debts = dbAdapter.getCrudDebt().readDebtByCreditorId(userId);

        for (Debt debt : debts) {
            User debtor = dbAdapter.getCrudUser().readUserById(debt.getDebtorId());
            debtors.add(debtor);

            Debt debt1 = dbAdapter.getCrudDebt().readDebtByPrimaryKey(debtor.getId(), userId);
            if (debt1 != null) {
                int debtAmount = debt1.getAmountIntegerPart();
                debt.decreaseAmountIntegerPart(debtAmount);
            }
        }


        //Add Entries where the current User is Debtor and have no Credits.
        List<Debt> debts2 = dbAdapter.getCrudDebt().readDebtByDebtorId(userId);

        for (Debt debt : debts2) {

            User creditorOfDebt = dbAdapter.getCrudUser().readUserById(debt.getCreditorId());

            if (!debtors.contains(creditorOfDebt)) {
                debtors.add(creditorOfDebt);
                debts.add(new Debt(userId, creditorOfDebt.getId(), -debt.getAmountIntegerPart(), debt.getAmountDecimalPart()));
            }
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

                TextView tvAmmount = (TextView) returnView.findViewById(R.id.tv_DebtingAmmount);
                tvAmmount.setText(String.valueOf(debts.get(position).getAmountIntegerPart()));

            }
            return returnView;

            //TODO: OnLongClick Delete payed Debts andupdate DB.
        }
    }
}
