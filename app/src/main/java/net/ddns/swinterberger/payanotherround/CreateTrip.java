package net.ddns.swinterberger.payanotherround;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import net.ddns.swinterberger.payanotherround.entities.User;

import java.util.ArrayList;
import java.util.List;

public class CreateTrip extends AppCompatActivity {

    private List<User> users;
    private Button newUser;
    private Button saveTrip;
    private ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.createTrip));
        setSupportActionBar(myToolbar);

        users = new ArrayList<>();

        newUser = (Button) findViewById(R.id.btn_newUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                users.add(user);
                refreshList();
            }
        });

        saveTrip = (Button) findViewById(R.id.btn_Save);
        saveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        refreshList();
    }

    private void refreshList() {
        ListView userListView = (ListView) findViewById(R.id.lv_Users);
        userListView.setAdapter(new SimpleUserListItemAdapter());
    }

    private class SimpleUserListItemAdapter extends BaseAdapter {

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
                convertView = CreateTrip.this.getLayoutInflater().inflate(R.layout.listitem_user_simple, null);

                EditText nameField = (EditText) convertView.findViewById(R.id.et_Name);
                nameField.setText(users.get(position).getName());
                nameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            int position = v.getId();
                            users.get(position).setName(((EditText) v).getText().toString());
                        }
                    }
                });
                nameField.setId(position);
            }
            return convertView;
        }
    }
}
