package net.ddns.swinterberger.payanotherround;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.entities.User;

import java.util.List;

public class CreateTrip extends AppCompatActivity {

    private List<User> users;
    private Button newUser;
    private Button saveTrip;
    private ListView userListView;

    private DbAdapter dbAdapter = new DbAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.createTrip));
        setSupportActionBar(myToolbar);

        users = loadAllUsersFromDb();

        newUser = (Button) findViewById(R.id.btn_newUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                users.add(user);
                dbAdapter.getCrudUser().createUser(user);
                refreshList();
            }
        });

        saveTrip = (Button) findViewById(R.id.btn_Save);
        saveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (User user : users) {
                    dbAdapter.getCrudUser().updateUser(user);
                }
                finish();
            }
        });
        refreshList();
    }

    private List<User> loadAllUsersFromDb() {
        return dbAdapter.getCrudUser().readAllUsers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbAdapter.open();
    }

    @Override
    protected void onPause() {
        dbAdapter.close();
        super.onPause();
    }

    private void refreshList() {
        if (this.userListView == null) {
            userListView = (ListView) findViewById(R.id.lv_Users);
            registerForContextMenu(userListView);
        }
        userListView.setAdapter(new SimpleUserListItemAdapter());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lv_Users) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu_users, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                int position = ((AdapterView.AdapterContextMenuInfo) info).position;
                long id = users.get(position).getId();
                dbAdapter.getCrudUser().deleteUserById(id);
                for (User user : users) {
                    if (user.getId() == id) {
                        users.remove(user);
                    }
                }
                refreshList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
