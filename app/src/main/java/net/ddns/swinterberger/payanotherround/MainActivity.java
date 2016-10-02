package net.ddns.swinterberger.payanotherround;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.entities.Trip;

public final class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_TRIPSETTINGS = 1337;
    private static final String CURRENT_TRIP_ID = "currentTripId";

    private long currentTripId;
    private SharedPreferences preferences;
    private TextView tripTitle;

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
        dbAdapter.open();
    }

    @Override
    protected void onDestroy() {
        dbAdapter.close();
        super.onDestroy();
    }

    public void newReceiptClicked(final View v) {
        Intent intent = new Intent(this, CreateBill.class);
        intent.putExtra(getResources().getString(R.string.extra_tripid), currentTripId);
        startActivity(intent);
    }

    public void summaryClicked(final View v) {

    }
}
