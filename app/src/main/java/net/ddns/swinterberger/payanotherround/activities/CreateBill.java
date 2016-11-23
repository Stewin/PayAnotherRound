package net.ddns.swinterberger.payanotherround.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
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

import net.ddns.swinterberger.payanotherround.R;
import net.ddns.swinterberger.payanotherround.currency.Currency;
import net.ddns.swinterberger.payanotherround.currency.CurrencyCalculator;
import net.ddns.swinterberger.payanotherround.currency.CurrencyCalculatorFactory;
import net.ddns.swinterberger.payanotherround.database.CrudBillDebtors;
import net.ddns.swinterberger.payanotherround.database.CrudDebt;
import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.entities.Bill;
import net.ddns.swinterberger.payanotherround.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to creating a new Bill.
 *
 * @author Stefan Winteberger
 * @version 1.0.0
 */
public final class CreateBill extends AppCompatActivity {

    private List<User> users;
    private long tripId;

    private DbAdapter dbAdapter = new DbAdapter(this);
    private String[] currencyList;
    private Spinner spinnerCurrency;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_bill);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        spinnerCurrency = (Spinner) findViewById(R.id.sp_Currency);
        List<Currency> currencies = dbAdapter.getCrudCurrency().readAllCurrencies();
        currencyList = new String[currencies.size()];
        for (int i = 0; i < currencies.size(); i++) {
            currencyList[i] = currencies.get(i).getCurrencyAbbreviation();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyList);
        spinnerCurrency.setAdapter(arrayAdapter);
        preferences = getPreferences(MODE_PRIVATE);
        String lastUsedCurrency = preferences.getString(getResources().getString(R.string.preference_currency_lastused), "CHF");

        for (int i = 0; i < currencyList.length; i++) {
            if (currencyList[i].toString().equals(lastUsedCurrency)) {
                spinnerCurrency.setSelection(i);
            }
        }

        tripId = getIntent().getLongExtra(getResources().getString(R.string.extra_tripid), -1);

        users = dbAdapter.getCrudUser().readUsersByTripId(tripId);

        ListView userList = (ListView) findViewById(R.id.lv_users);
        userList.setAdapter(new UserTwoCheckboxesListItemAdapter());
    }

    public final void onSaveButtonClicked(final View v) {

        Bill bill = createBillFromActivity();
        long billId = dbAdapter.getCrudBill().createBill(bill);

        CrudBillDebtors crudBillDebtors = dbAdapter.getCrudBillDebtor();
        for (long debtorId : bill.getDebtorIds()) {
            crudBillDebtors.createBillDebtor(billId, debtorId);
        }
        createDebtEntries(bill);

        finish();
    }

    private void createDebtEntries(Bill bill) {
        //TODO: ERWEITERUNG Evtl. Kosten prozentual aufteilen
        //TODO: Make exchange ratio changeable.

        Currency currency = dbAdapter.getCrudCurrency().readCurrencyById(bill.getCurrencyId());
        CurrencyCalculator calculator = CurrencyCalculatorFactory.getCalculatorForType(currency);
        int amount = bill.getAmountInCent();
        amount = calculator.exchangeAmount(amount);
        int numberOfDebtors = bill.getDebtorIds().size();
        amount = calculator.divideByUsers(amount, numberOfDebtors + 1);

        CrudDebt crudDebt = dbAdapter.getCrudDebt();

        for (long debtorId : bill.getDebtorIds()) {
            crudDebt.createDebt(bill.getPayerId(), debtorId, bill.getId(), amount);
        }
    }

    private Bill createBillFromActivity() {
        Bill newBill = new Bill();

        //TODO: ERWEITERUNG Add Date on Bills
        //TODO: ERWEITERUNG If Payer Selected, selct other Users as Debtors

        //Description
        String description = ((EditText) findViewById(R.id.et_BillTitle)).getText().toString();
        newBill.setDescription(description);

        String currencyAbreviation = currencyList[spinnerCurrency.getSelectedItemPosition()];
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getResources().getString(R.string.preference_currency_lastused), currencyAbreviation);
        editor.apply();

        //AmountInteger
        EditText amountFieldInteger = (EditText) findViewById(R.id.et_BillAmountInteger);
        int amountInCent = 0;
        try {
            amountInCent = Integer.parseInt(amountFieldInteger.getText().toString());
        } catch (NumberFormatException nfe) {
            Log.e("NumberFormatException", nfe.toString());
        }

        //AmmountDecimal
        EditText amountFieldDecimal = (EditText) findViewById(R.id.et_BillAmountDecimal);
        int amountDecimal = 0;
        try {
            amountDecimal = Integer.parseInt(amountFieldDecimal.getText().toString());
        } catch (NumberFormatException nfe) {
            Log.e("NumberFormatException", nfe.toString());
        }

        newBill.setAmountInCent(amountInCent * 100 + amountDecimal);
        newBill.setCurrencyId(dbAdapter.getCrudCurrency().readCurrencyByAbbreviation(currencyAbreviation).getId());


        //Trip
        newBill.setTripId(this.tripId);

        //Payer
        newBill.setPayerId(getIdOfPayer());

        //Debtor
        newBill.setDebtorIds(getIdsOfDebtors());

        return newBill;
    }

    private long getIdOfPayer() {
        ListView userList = (ListView) findViewById(R.id.lv_users);
        for (int i = 0; i < userList.getCount(); i++) {
            View v = userList.getChildAt(i);
            CheckBox cbPayer = (CheckBox) v.findViewById(R.id.cb_userbox);
            if (cbPayer.isChecked()) {
                return users.get(i).getId();
            }
        }
        return 0;
    }

    private List<Long> getIdsOfDebtors() {
        List<Long> debtorIds = new ArrayList<>();
        ListView userList = (ListView) findViewById(R.id.lv_users);
        for (int i = 0; i < userList.getCount(); i++) {
            View v = userList.getChildAt(i);
            CheckBox cbDebtor = (CheckBox) v.findViewById(R.id.cb_userbox2);
            if (cbDebtor.isChecked()) {
                debtorIds.add(users.get(i).getId());
            }
        }
        return debtorIds;
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
            }
            return returnView;
        }
    }
}
