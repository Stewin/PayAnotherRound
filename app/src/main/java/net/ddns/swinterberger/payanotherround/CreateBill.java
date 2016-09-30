package net.ddns.swinterberger.payanotherround;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import net.ddns.swinterberger.payanotherround.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 24.09.2016.
 */
public class CreateBill extends AppCompatActivity {

    private List<User> users;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_bill);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Spinner spinnerCurrency = (Spinner) findViewById(R.id.sp_Currency);
        String[] currencyList = {"CHF", "EUR", "USD"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyList);
        spinnerCurrency.setAdapter(arrayAdapter);

        users = new ArrayList<>();
        //Zu Testzwecken
        User user1 = new User();
        user1.setName("Stufi");
        users.add(user1);
        User user2 = new User();
        user2.setName("Thomi");
        users.add(user2);

        ListView userList = (ListView) findViewById(R.id.lw_users);
        userList.setAdapter(new UserTwoCheckboxesListItemAdapter());
    }

    public void onSaveButtonClicked(final View v) {
        finish();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = CreateBill.this.getLayoutInflater().inflate(R.layout.listitem_user_two_checkbox, null);

                TextView nameField = (TextView) convertView.findViewById(R.id.tv_Name);
                nameField.setText(users.get(position).getName());

                CheckBox cbPayer = (CheckBox) findViewById(R.id.cb_userbox);

                CheckBox cbDebtor = (CheckBox) findViewById(R.id.cb_userbox2);
            }
            return convertView;
        }
    }
}
