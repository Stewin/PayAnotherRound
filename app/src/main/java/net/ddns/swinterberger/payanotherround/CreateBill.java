package net.ddns.swinterberger.payanotherround;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.entities.Bill;
import net.ddns.swinterberger.payanotherround.entities.User;

import java.util.List;

/**
 * Activity to creating a new Bill.
 *
 * @author Stefan Winteberger
 * @version 1.0
 */
public class CreateBill extends AppCompatActivity {

    private List<User> users;
    private long tripId;

    private DbAdapter dbAdapter = new DbAdapter(this);

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_bill);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Spinner spinnerCurrency = (Spinner) findViewById(R.id.sp_Currency);
        String[] currencyList = {"CHF", "EUR", "USD"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyList);
        spinnerCurrency.setAdapter(arrayAdapter);

        tripId = getIntent().getLongExtra(getResources().getString(R.string.extra_tripid), -1);

        users = dbAdapter.getCrudUser().readUsersByTripId(tripId);

        ListView userList = (ListView) findViewById(R.id.lv_users);
        userList.setAdapter(new UserTwoCheckboxesListItemAdapter());
    }

    public final void onSaveButtonClicked(final View v) {

        Bill bill = createBillFromActivity();


        finish();
    }

    private Bill createBillFromActivity() {
        Bill newBill = new Bill();

        //Description
        String description = ((EditText) findViewById(R.id.et_BillTitle)).getText().toString();
        newBill.setDescription(description);

        //Amount
        EditText amountField = (EditText) findViewById(R.id.et_BillAmount);
        int amount = 0;
        try {
            amount = Integer.parseInt(amountField.getText().toString());
        } catch (NumberFormatException nfe) {
            Log.e("NumberFormatException", nfe.toString());
        }
        newBill.setAmmount(amount);

        //Currency
        String currency = ((Spinner) findViewById(R.id.sp_Currency)).getSelectedItem().toString();
        newBill.setCurrency(currency);

        //Trip
        newBill.setTripId(this.tripId);

        //Payer
        newBill.setPayerId(getIdOfPayer());

        //Debtor

        return newBill;
    }

    private int getIdOfPayer() {
        //TODO: Get ID of the User who is marked (with Checkbox) as Payer.
        return 0;
    }

    private class UserTwoCheckboxesListItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(final int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return users.get(position).getId();
        }

        @Override
        public final View getView(final int position, final View convertView, final ViewGroup parent) {
            View returnView = convertView;
            if (returnView == null) {
                returnView = CreateBill.this.getLayoutInflater().inflate(R.layout.listitem_user_two_checkbox, null);

                TextView nameField = (TextView) returnView.findViewById(R.id.tv_Name);
                nameField.setText(users.get(position).getName());

                CheckBox cbPayer = (CheckBox) findViewById(R.id.cb_userbox);

                CheckBox cbDebtor = (CheckBox) findViewById(R.id.cb_userbox2);
            }
            return returnView;
        }
    }
}
