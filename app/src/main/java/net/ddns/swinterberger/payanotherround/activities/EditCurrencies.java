package net.ddns.swinterberger.payanotherround.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import net.ddns.swinterberger.payanotherround.R;
import net.ddns.swinterberger.payanotherround.currency.Currency;
import net.ddns.swinterberger.payanotherround.database.DbAdapter;

import java.util.List;

/**
 * Class to editing Currencies.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public class EditCurrencies extends AppCompatActivity {


    private List<Currency> allCurrencies;
    private DbAdapter dbAdapter = new DbAdapter(this);
    private ListView currencyListView;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_currencies);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        TextView currencyLable = (TextView) findViewById(R.id.tv_currencylable);
        currencyLable.setText(getResources().getString(R.string.lable_currency));

        allCurrencies = dbAdapter.getCrudCurrency().readAllCurrencies();
        refreshList();
    }

    private void refreshList() {
        if (this.currencyListView == null) {
            currencyListView = (ListView) findViewById(R.id.lv_Currencies);
        }
        currencyListView.setAdapter(new CurrencyListItemAdapter());
    }

    public final void onAddCurrencyButtonClicked(final View view) {
        Currency currency = new Currency("NEW", 1.0F);
        long currencyId = dbAdapter.getCrudCurrency().createCurrency(currency);
        currency.setId(currencyId);
        allCurrencies.add(currency);
        refreshList();
    }

    @Override
    protected final void onPause() {
        for (Currency currency : allCurrencies) {
            dbAdapter.getCrudCurrency().updateCurrency(currency);
        }
        super.onPause();
    }

    private class CurrencyListItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return allCurrencies.size();
        }

        @Override
        public Object getItem(final int position) {
            return allCurrencies.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return allCurrencies.get(position).getId();
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View returnView = convertView;
            if (returnView == null) {
                returnView = EditCurrencies.this.getLayoutInflater().inflate(R.layout.listitem_currency, null);

                EditText currencyName = (EditText) returnView.findViewById(R.id.et_CurrencyAbbreviation);
                currencyName.setText(allCurrencies.get(position).getCurrencyAbbreviation());
                currencyName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            int position = v.getId();
                            allCurrencies.get(position).setCurrencyAbbreviation(((EditText) v).getText().toString());
                        }
                    }
                });
                currencyName.setId(position);

                EditText etExchangeRatio = (EditText) returnView.findViewById(R.id.et_exchangeRatio);
                final Float exchangeRatio = allCurrencies.get(position).getExchangeRatio();
                etExchangeRatio.setText(exchangeRatio.toString());
                etExchangeRatio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            int position = v.getId();
                            String exchangeRatioAsText = ((EditText) v).getText().toString();
                            allCurrencies.get(position).setExchangeRatio(Float.parseFloat(exchangeRatioAsText));
                        }
                    }
                });
                etExchangeRatio.setId(position);
            }
            return returnView;
        }
    }
}
