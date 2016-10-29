package net.ddns.swinterberger.payanotherround.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.ddns.swinterberger.payanotherround.R;
import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.entities.Bill;
import net.ddns.swinterberger.payanotherround.entities.Trip;

import java.util.ArrayList;
import java.util.List;

public final class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_TRIPSETTINGS = 1337;
    private static final String CURRENT_TRIP_ID = "currentTripId";

    private long currentTripId;
    private SharedPreferences preferences;
    private TextView tripTitle;
    private ListView billList;
    private List<Bill> bills;

    private DbAdapter dbAdapter = new DbAdapter(this);
    private Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        tripTitle = (TextView) findViewById(R.id.tv_TripTitleMain);

        loadPreferences();
        setTripTitle();
    }

    private void setTripTitle() {
        if (this.currentTrip != null) {
            tripTitle.setText(this.currentTrip.getName());
        } else {
            tripTitle.setText(R.string.lable_no_trip_selected);
        }
    }

    private void loadPreferences() {
        preferences = getPreferences(MODE_PRIVATE);
        currentTripId = preferences.getLong(CURRENT_TRIP_ID, -1);
        if (currentTripId == -1) {
            //ToDo: Show Dialog to create new Trip.
        } else {
            this.currentTrip = dbAdapter.getCrudTrip().readTripById(currentTripId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_mainscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbaritem_settings:
                openTripSettings();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void openTripSettings() {
        startActivityForResult(new Intent(this, TripChooserActivity.class), REQUEST_CODE_TRIPSETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TRIPSETTINGS && resultCode == RESULT_OK) {
            long tripId = data.getLongExtra("tripId", -1);
            this.currentTripId = tripId;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(CURRENT_TRIP_ID, tripId);
            editor.commit();

            this.currentTrip = dbAdapter.getCrudTrip().readTripById(tripId);
            setTripTitle();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentTripId != 0) {
            loadBills();
            refreshList();
        }
        dbAdapter.open();
    }

    @Override
    protected void onDestroy() {
        dbAdapter.close();
        super.onDestroy();
    }

    private void refreshList() {
        if (this.billList == null) {
            billList = (ListView) findViewById(R.id.lv_Bills);
            registerForContextMenu(billList);
        }
        billList.setAdapter(new SimpleTripListItemAdapter());
    }

    private void loadBills() {
        bills = dbAdapter.getCrudBill().readBillsByTripId(this.currentTripId);
        if (bills == null) {
            bills = new ArrayList<>();
        }
    }

    public void newReceiptClicked(final View v) {
        Intent intent = new Intent(this, CreateBill.class);
        intent.putExtra(getResources().getString(R.string.extra_tripid), currentTripId);
        startActivity(intent);
    }

    public void summaryClicked(final View v) {
        Intent intent = new Intent(this, SummareyTripUsers.class);
        intent.putExtra(getResources().getString(R.string.extra_tripid), currentTripId);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lv_Bills) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu_bills, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                int position = ((AdapterView.AdapterContextMenuInfo) info).position;
                long id = bills.get(position).getId();

                dbAdapter.getCrudBill().deleteBillById(id);
                for (Bill bill : bills) {
                    if (bill.getId() == id) {
                        bills.remove(bill);
                    }
                }
                //TODO: Update Debt Table. Decrease the Bill Amount.
                refreshList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private class SimpleTripListItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bills.size();
        }

        @Override
        public Object getItem(int position) {
            return bills.get(position);
        }

        @Override
        public long getItemId(int position) {
            return bills.get(position).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.listitem_bill_simple, null);

                TextView billDescription = (TextView) convertView.findViewById(R.id.tv_billDescription);
                billDescription.setText(bills.get(position).getDescription());
            }
            return convertView;
        }
    }
}
