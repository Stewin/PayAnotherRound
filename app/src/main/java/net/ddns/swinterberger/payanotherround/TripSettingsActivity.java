package net.ddns.swinterberger.payanotherround;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import net.ddns.swinterberger.payanotherround.entities.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripSettingsActivity extends AppCompatActivity {

    private List<Trip> trips;
    private ListView tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_settings);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(this.getResources().getString(R.string.title_trip_settings));
        setSupportActionBar(myToolbar);

        Button buttonNewTrip = (Button) findViewById(R.id.btn_newTrip);
        buttonNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TripSettingsActivity.this, CreateTrip.class));
            }
        });

        tripList = (ListView) findViewById(R.id.lv_TripList);

        loadTrips();
        if (trips == null) {
            trips = new ArrayList<>();
        }

        //Zum Testen
        Trip trip1 = new Trip();
        trip1.setName("Ausgang");
        trip1.setId(0);
        trips.add(trip1);
        Trip trip2 = new Trip();
        trip2.setName("Ferien");
        trip2.setId(1);
        trips.add(trip2);

        refreshList();
    }

    private void refreshList() {
        tripList.setAdapter(new ArrayAdapter<Trip>(TripSettingsActivity.this, android.R.layout.simple_expandable_list_item_1, trips));
    }

    private void loadTrips() {
        //ToDo: Load existing Trips from DB
    }


}
