package net.ddns.swinterberger.payanotherround.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.ddns.swinterberger.payanotherround.R;
import net.ddns.swinterberger.payanotherround.currency.Currency;
import net.ddns.swinterberger.payanotherround.currency.CurrencyCalculator;
import net.ddns.swinterberger.payanotherround.currency.CurrencyCalculatorFactory;
import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.database.queries.simple.CrudBillDebtors;
import net.ddns.swinterberger.payanotherround.database.queries.simple.CrudDebt;
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
    private ListView userList;
    private long tripId;
    private long billId;

    private DbAdapter dbAdapter = new DbAdapter(this);
    private String[] currencyList;
    private Spinner spinnerCurrency;
    private SharedPreferences preferences;
    private UserTwoCheckboxesListItemAdapter userListItemAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_bill);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initializeCurrencySpinner();

        billId = getIntent().getLongExtra(getResources().getString(R.string.extra_billId), -1);
        tripId = getIntent().getLongExtra(getResources().getString(R.string.extra_tripid), -1);
        users = dbAdapter.getCrudUser().readUsersByTripId(tripId);

        userListItemAdapter = new UserTwoCheckboxesListItemAdapter();

        if (billId != -1) {
            loadExistingBill();
        } else {
            prepareNewBill();
        }

        refreshList();
    }

    private void prepareNewBill() {
        preferences = getPreferences(MODE_PRIVATE);
        String lastUsedCurrency = preferences.getString(getResources().getString(R.string.preference_currency_lastused), "CHF");

        for (int i = 0; i < currencyList.length; i++) {
            if (currencyList[i].equals(lastUsedCurrency)) {
                spinnerCurrency.setSelection(i);
            }
        }
    }

    private void loadExistingBill() {
        Bill billToEdit = dbAdapter.getCrudBill().readBillById(billId);
        displayBill(billToEdit);
        setUserCheckboxesForBill(billToEdit);
    }

    private void initializeCurrencySpinner() {
        spinnerCurrency = (Spinner) findViewById(R.id.sp_Currency);
        List<Currency> currencies = dbAdapter.getCrudCurrency().readAllCurrencies();
        currencyList = new String[currencies.size()];
        for (int i = 0; i < currencies.size(); i++) {
            currencyList[i] = currencies.get(i).getCurrencyAbbreviation();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyList);
        spinnerCurrency.setAdapter(arrayAdapter);
    }

    private void setUserCheckboxesForBill(Bill bill) {
        for (User user : users) {
            if (user.getId() == bill.getPayerId()) {
                user.setPayer(true);
            }
        }

        bill.setDebtorIds(dbAdapter.getCrudBillDebtor().readDebtorsByBillId(billId));

        for (long debtorId : bill.getDebtorIds()) {
            for (User user : users) {
                if (user.getId() == debtorId) {
                    user.setDebtor(true);
                }
            }
        }
    }

    private void displayBill(Bill billToDisplay) {
        EditText billDescription = (EditText) this.findViewById(R.id.et_BillTitle);
        billDescription.setText(billToDisplay.getDescription());

        EditText amountFieldInteger = (EditText) this.findViewById(R.id.et_BillAmountInteger);
        amountFieldInteger.setText(String.valueOf(billToDisplay.getAmountInCent() / 100));

        EditText amountFieldDecimal = (EditText) this.findViewById(R.id.et_BillAmountDecimal);
        amountFieldDecimal.setText(String.valueOf(billToDisplay.getAmountInCent() % 100));
    }

    private void refreshList() {
        userList = (ListView) findViewById(R.id.lv_users);
        userList.setAdapter(userListItemAdapter);
    }

    public final void onSaveButtonClicked(final View v) {

        if (!isPayerSelected()) {
            Toast.makeText(this, "Pleas choose a Payer first!", Toast.LENGTH_SHORT).show();
        } else {
            if (billId >= 0) {
                Bill bill = createBillFromActivity();
                bill.setId(billId);

                //UpdateBillDebtors
                dbAdapter.getCrudBillDebtor().deleteBillDebtorByBillId(billId);
                CrudBillDebtors crudBillDebtors = dbAdapter.getCrudBillDebtor();
                for (long debtorId : bill.getDebtorIds()) {
                    crudBillDebtors.createBillDebtor(billId, debtorId);
                }

                //UpdateDebtEntries
                dbAdapter.getCrudDebt().deleteDebtByBillId(billId);
                createDebtEntries(bill);

                //UpdateBill
                dbAdapter.getCrudBill().updateBill(bill);

            } else {

                Bill bill = createBillFromActivity();
                long billId = dbAdapter.getCrudBill().createBill(bill);

                CrudBillDebtors crudBillDebtors = dbAdapter.getCrudBillDebtor();
                for (long debtorId : bill.getDebtorIds()) {
                    crudBillDebtors.createBillDebtor(billId, debtorId);
                }
                createDebtEntries(bill);
            }
            finish();
        }
    }

    private boolean isPayerSelected() {
        for (User user : users) {
            if (user.isPayer()) {
                return true;
            }
        }
        return false;
    }

    private void createDebtEntries(Bill bill) {

        //TODO: Testen stabilisieren (Fehleingaben, Listenanzeige etc.)
        //TODO: Translate & Publish (Alpha Test).
        //TODO: ERWEITERUNG Special Cases. (a.e. If the Creditor don't have to pay etc.)
        //TODO: ERWEITERUNG Evtl. Kosten prozentual aufteilen

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
            if (users.get(i).isPayer()) {
                return users.get(i).getId();
            }
        }
        return 0;
    }

    private List<Long> getIdsOfDebtors() {
        List<Long> debtorIds = new ArrayList<>();
        for (int i = 0; i < userList.getCount(); i++) {

            if (users.get(i).isDebtor()) {
                debtorIds.add(users.get(i).getId());
            }
        }
        return debtorIds;
    }

    private void setDebtors(final int position) {
        for (int i = 0; i < users.size(); i++) {
            if (i == position) {
                users.get(i).setPayer(true);
                users.get(i).setDebtor(false);
            } else {
                users.get(i).setPayer(false);
                users.get(i).setDebtor(true);
            }
        }
        refreshList();
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
            }

            TextView nameField = (TextView) returnView.findViewById(R.id.tv_Name);
            nameField.setText(users.get(position).getName());

            final CheckBox checkBoxCreditor = (CheckBox) returnView.findViewById(R.id.cb_checkboxcreditor);
            checkBoxCreditor.setChecked(users.get(position).isPayer());
            checkBoxCreditor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setDebtors(position);
                }
            });

            final CheckBox checkBoxDebtor = (CheckBox) returnView.findViewById(R.id.cb_checkboxDebtor);
            checkBoxDebtor.setChecked(users.get(position).isDebtor());
            checkBoxDebtor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    users.get(position).setDebtor(b);
                }
            });

            return returnView;
        }
    }
}
