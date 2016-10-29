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
 * Created by Stefan on 29.10.2016.
 */
public final class UserSummary extends AppCompatActivity {

    private User user;
    private TextView tvUserName;
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

        long userId = getIntent().getLongExtra(getResources().getString(R.string.extra_userId), -1l);
        this.user = dbAdapter.getCrudUser().readUserById(userId);


        tvUserName = (TextView) findViewById(R.id.tv_UserName);
        tvUserName.setText(user.getName());

        collectUserDebtsFor(userId);
        refreshListView();
    }

    private void collectUserDebtsFor(long userId) {
        debts = dbAdapter.getCrudDebt().readDebtByCreditorId(userId);

        for (Debt debt : debts) {
            User debtor = dbAdapter.getCrudUser().readUserById(debt.getDebitorId());
            debtors.add(debtor);
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
            return Long.parseLong(debts.get(position).getCreditorId() + "" + debts.get(position).getDebitorId());
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            View returnView = convertView;

            if (returnView == null) {
                returnView = UserSummary.this.getLayoutInflater().inflate(R.layout.listitem_user_as_debtor, null);

                TextView tvName = (TextView) returnView.findViewById(R.id.tv_CreditorName);
                tvName.setText(debtors.get(position).getName());

                TextView tvAmmount = (TextView) returnView.findViewById(R.id.tv_DebtingAmmount);
                tvAmmount.setText(String.valueOf(debts.get(position).getAmmount()));

            }
            return returnView;
        }
    }
}