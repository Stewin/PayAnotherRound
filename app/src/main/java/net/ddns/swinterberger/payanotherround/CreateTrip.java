package net.ddns.swinterberger.payanotherround;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CreateTrip extends AppCompatActivity implements UserObserver {

    private List<User> users;
    private Button newUser;
    private Button saveTrip;
    private ListView userListView;

    private List<User> readUsers() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.createTrip));
        setSupportActionBar(myToolbar);


        newUser = (Button) findViewById(R.id.btn_newUser);

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.addUserObserver(CreateTrip.this);
                users.add(user);
                refreshList();
            }
        });

//        users = readUsers();
        if (users == null) {
            users = new ArrayList<User>();
        }
    }

    private void refreshList() {
        userListView = (ListView) findViewById(R.id.lv_Users);
        this.userListView.setAdapter(new SimpleUserListItemAdapter(this, users));
    }

    @Override
    public void userChanged() {
        refreshList();
    }

    private class SimpleUserListItemAdapter extends BaseAdapter {

        private List<User> users;
        private LayoutInflater inflater = null;

        public SimpleUserListItemAdapter(final Context context, final List<User> users) {
            this.users = users;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public final int getCount() {
            return users.size();
        }

        @Override
        public final Object getItem(final int position) {
            return users.get(position);
        }

        @Override
        public final long getItemId(final int position) {
            return users.get(position).getId();
        }

        @Override
        public final View getView(final int position, View convertView, final ViewGroup parent) {

            final UserListItemHolder holder;

            if (convertView == null) {
                holder = new UserListItemHolder();
                users.set(position, new User());
                convertView = inflater.inflate(R.layout.listitem_user_simple, null);

                holder.name = (EditText) convertView.findViewById(R.id.et_Name);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_userbox);
                //Picture

                convertView.setTag(holder);
            } else {
                holder = (UserListItemHolder) convertView.getTag();
            }

            holder.ref = position;
            holder.name.setText(users.get(position).getName());

            holder.name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    users.get(holder.ref).setName(s.toString());
                }
            });

            return convertView;
        }

        private class UserListItemHolder {
            CheckBox checkBox;
            EditText name;
            //Picture
            int ref;
        }
    }
}
