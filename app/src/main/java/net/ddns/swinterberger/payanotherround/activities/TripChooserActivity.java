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
import android.widget.ListView;
import android.widget.TextView;

import net.ddns.swinterberger.payanotherround.R;
import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.entities.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to choose a Trip.
 *
 * @author Stefan Winteberger
 * @version 1.0
 */
public final class TripChooserActivity extends AppCompatActivity {

    private List<Trip> trips;
    private ListView tripList;

    private DbAdapter dbAdapter = new DbAdapter(this);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_chooser);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(this.getResources().getString(R.string.title_trip_settings));
        setSupportActionBar(myToolbar);

        Button buttonNewTrip = (Button) findViewById(R.id.btn_newTrip);
        buttonNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TripChooserActivity.this, CreateTrip.class));
            }
        });

        tripList = (ListView) findViewById(R.id.lv_TripList);
        registerForContextMenu(tripList);
    }

    private void refreshList() {
        tripList.setAdapter(new SimpleTripListItemAdapter());
    }

    private void loadTrips() {
        trips = dbAdapter.getCrudTrip().readAllTrips();
        if (trips == null) {
            trips = new ArrayList<>();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbAdapter.open();
        loadTrips();
        refreshList();
    }

    @Override
    protected void onPause() {
//        dbAdapter.close();
        super.onPause();
    }

    @Override
    public final void onCreateContextMenu(final ContextMenu menu, final View v,
                                          final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lv_TripList) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu_trips, menu);
        }
    }

    @Override
    public final boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = info.position;
        long id = trips.get(position).getId();

        switch (item.getItemId()) {

            case R.id.edit:
                Intent intent = new Intent(this, CreateTrip.class);
                String string = getResources().getString(R.string.extra_tripid);
                intent.putExtra(string, id);
                startActivity(intent);
                break;

            case R.id.delete:

                for (Trip trip : trips) {
                    if (trip.getId() == id) {
                        trips.remove(trip);
                        break;
                    }
                }
                dbAdapter.getCrudBill().deleteBillByTripId(id);
                dbAdapter.getCrudAttend().deleteAttendByTripId(id);
                dbAdapter.getCrudTrip().deleteTripById(id);
                refreshList();
                break;

            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private class SimpleTripListItemAdapter extends BaseAdapter {

        @Override
        public final int getCount() {
            return trips.size();
        }

        @Override
        public final Object getItem(int position) {
            return trips.get(position);
        }

        @Override
        public final long getItemId(int position) {
            return trips.get(position).getId();
        }

        @Override
        public final View getView(final int position, View convertView,
                                  final ViewGroup parent) {
            if (convertView == null) {
                convertView = TripChooserActivity.this.getLayoutInflater()
                        .inflate(R.layout.listitem_trip_simple, null);

                TextView tripTitle = (TextView) convertView.findViewById(R.id.tv_tripTitle);
                tripTitle.setText(trips.get(position).getName());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("tripId", trips.get(position).getId());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
            return convertView;
        }
    }


}
