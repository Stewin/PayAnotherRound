package net.ddns.swinterberger.payanotherround.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.ddns.swinterberger.payanotherround.R;
import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.entities.Bill;
import net.ddns.swinterberger.payanotherround.entities.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity, is used to see an Overview over the Bills for the selected Trip.
 *
 * @author Stefan Winteberger
 * @version 1.0.0
 */
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
            showNoTripsDialogue(this);
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
            case R.id.actionbaritem_trip_settings:
                openTripSettings();
                return true;

            case R.id.menuItemSettings:
                Intent intent = new Intent(MainActivity.this, UserPreferenceActivity.class);
                startActivity(intent);
                return true;

            case R.id.menuItemAbout:
                showAboutDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showAboutDialog() {
        final Dialog aboutDialog = new Dialog(this);
        aboutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aboutDialog.setContentView(R.layout.main_about);

        TextView aboutTitle = (TextView) aboutDialog.findViewById(R.id.tv_AboutTitle);

        String appVersion = "";
        try {
            appVersion = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(MainActivity.class.toString(), e.getMessage());
        }

        String title = getResources().getString(R.string.app_name) + " - " + appVersion;
        aboutTitle.setText(title);

        final Button button = (Button) aboutDialog
                .findViewById(R.id.ok_Button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                aboutDialog.dismiss();
            }
        });
        aboutDialog.show();
    }

    private void openTripSettings() {
        startActivityForResult(new Intent(this, TripChooserActivity.class), REQUEST_CODE_TRIPSETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TRIPSETTINGS && resultCode == RESULT_OK) {
            this.currentTripId = data.getLongExtra("tripId", -1);
            this.currentTrip = dbAdapter.getCrudTrip().readTripById(currentTripId);
            setTripTitle();

        } else if (requestCode == REQUEST_CODE_TRIPSETTINGS && resultCode == RESULT_CANCELED) {
            currentTripId = -1;
            this.currentTrip = null;
            setTripTitle();
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(CURRENT_TRIP_ID, currentTripId);
        editor.commit();
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
        if (currentTripId == -1) {
            Toast.makeText(this, "Bitte Trip wählen oder erstellen.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, CreateBill.class);
            intent.putExtra(getResources().getString(R.string.extra_tripid), currentTripId);
            startActivity(intent);
        }
    }

    public void summaryClicked(final View v) {
        Intent intent = new Intent(this, SummaryTripUsers.class);
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

                //TODO: Auslagern und im TripChooserActivity wieder verwenden.
                //Delete Bill
                int position = ((AdapterView.AdapterContextMenuInfo) info).position;
                long billId = bills.get(position).getId();

                //1. Delete Debts by Bill Id
                dbAdapter.getCrudDebt().deleteDebtByBillId(billId);

                //2. Delete Bill_Debtors by BillId
                dbAdapter.getCrudBillDebtor().deleteBillDebtorByBillId(billId);

                //3. Delete Bill
                dbAdapter.getCrudBill().deleteBillById(billId);

                for (Bill bill : bills) {
                    if (bill.getId() == billId) {
                        bills.remove(bill);
                    }
                }

                refreshList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showNoTripsDialogue(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getResources().getString(R.string.text_no_trip_selected));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.lable_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openTripSettings();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.lable_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
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
