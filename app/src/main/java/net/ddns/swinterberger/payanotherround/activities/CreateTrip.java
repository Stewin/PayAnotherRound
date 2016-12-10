package net.ddns.swinterberger.payanotherround.activities;

import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.ddns.swinterberger.payanotherround.R;
import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.database.queries.recursive.RecursiveUserManipulator;
import net.ddns.swinterberger.payanotherround.entities.Trip;
import net.ddns.swinterberger.payanotherround.entities.User;

import java.util.List;

/**
 * Activity to Create new Trips.
 *
 * @author Stefan Winteberger
 * @version 1.0
 */
public final class CreateTrip extends AppCompatActivity {

    private List<User> allUsers;

    private ListView userListView;
    private EditText etTripTitle;
    private long tripId;

    private DbAdapter dbAdapter = new DbAdapter(this);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        etTripTitle = (EditText) findViewById(R.id.et_TripTitle);

        tripId = getIntent().getLongExtra(getResources().getString(R.string.extra_tripid), -1L);
        allUsers = loadAllUsersFromDb();

        if (tripId == -1) {
            myToolbar.setTitle(getResources().getString(R.string.createTrip));
        } else {
            myToolbar.setTitle(getResources().getString(R.string.editTrip));
            Trip trip = dbAdapter.getCrudTrip().readTripById(tripId);
            etTripTitle.setText(trip.getName());
            List<User> activeUsers = loadUsersByTripId(tripId);
            for (User user : activeUsers) {
                for (User user1 : allUsers) {
                    if (user.equals(user1)) {
                        user1.setCheckboxEnabled(true);
                    }
                }
            }
        }

        setSupportActionBar(myToolbar);
        setupButtons();

        refreshList();
    }

    private void setupButtons() {
        Button newUser = (Button) findViewById(R.id.btn_newUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setCheckboxEnabled(true);
                allUsers.add(user);
                dbAdapter.getCrudUser().createUser(user);
                refreshList();

                //TODO: ERWEITERUNG Fotos and Colors for user
            }
        });

        Button saveTrip = (Button) findViewById(R.id.btn_Save);
        saveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (User user : allUsers) {
                    dbAdapter.getCrudUser().updateUser(user);
                }

                Trip newTrip = new Trip();
                newTrip.setId(tripId);
                newTrip.setName(etTripTitle.getText().toString());
                if (tripId == -1) {
                    tripId = dbAdapter.getCrudTrip().createTrip(newTrip);
                    for (User user : allUsers) {
                        if (user.isCheckboxEnabled()) {
                            dbAdapter.getCrudAttend().createAttend(user.getId(), tripId);
                        }
                    }
                } else {
                    dbAdapter.getCrudTrip().updateTrip(newTrip);

                    for (User user : allUsers) {
                        if (user.isCheckboxEnabled()) {
                            dbAdapter.getCrudAttend().createAttend(user.getId(), tripId);
                        } else {
                            dbAdapter.getCrudAttend().deleteAttend(user.getId(), tripId);
                        }
                    }

                }
                Intent intent = new Intent();
                intent.putExtra("tripId", tripId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private List<User> loadAllUsersFromDb() {
        return dbAdapter.getCrudUser().readAllUsers();
    }

    private List<User> loadUsersByTripId(final long tripId) {
        return dbAdapter.getCrudUser().readUsersByTripId(tripId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbAdapter.open();
    }

    private void refreshList() {
        if (this.userListView == null) {
            userListView = (ListView) findViewById(R.id.lv_Users);
            registerForContextMenu(userListView);
        }
        userListView.setAdapter(new UserOneCheckboxListItemAdapter());
    }

    @Override
    public final void onCreateContextMenu(final ContextMenu menu, final View v,
                                          final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lv_Users) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu_users, menu);
        }
    }

    @Override
    public final boolean onContextItemSelected(final MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                int position = info.position;
                long id = allUsers.get(position).getId();

                if (new RecursiveUserManipulator(this).deleteUserRecursiveById(id) == 0) {
                    removeUserFromList(id);
                    return true;
                } else {
                    Toast.makeText(this, "User has open debts! Can't be deleted.", Toast.LENGTH_SHORT).show();
                    return false;
                }

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void removeUserFromList(long id) {
        for (User user : allUsers) {
            if (user.getId() == id) {
                allUsers.remove(user);
            }
        }
        refreshList();
    }

    @Override
    public final void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private class UserOneCheckboxListItemAdapter extends BaseAdapter {

        @Override
        public final int getCount() {
            return allUsers.size();
        }

        @Override
        public final Object getItem(final int position) {
            return allUsers.get(position);
        }

        @Override
        public final long getItemId(final int position) {
            return allUsers.get(position).getId();
        }

        @Override
        public final View getView(final int position, final View convertView,
                                  final ViewGroup parent) {
            View returnView = convertView;
            if (returnView == null) {
                returnView = CreateTrip.this.getLayoutInflater()
                        .inflate(R.layout.listitem_user_one_checkbox, null);

                EditText nameField = (EditText) returnView.findViewById(R.id.et_Name);
                nameField.setText(allUsers.get(position).getName());
                nameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            allUsers.get(position).setName(((EditText) v).getText().toString());
                        }
                    }
                });

                CheckBox checkBox = (CheckBox) returnView.findViewById(R.id.cb_checkboxdebtor);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                        allUsers.get(position).setCheckboxEnabled(isChecked);
                    }
                });
                checkBox.setChecked(allUsers.get(position).isCheckboxEnabled());
            }
            return returnView;
        }
    }
}
