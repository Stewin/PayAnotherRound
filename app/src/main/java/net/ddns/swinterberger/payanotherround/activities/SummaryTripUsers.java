package net.ddns.swinterberger.payanotherround.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.ddns.swinterberger.payanotherround.R;
import net.ddns.swinterberger.payanotherround.database.DbAdapter;
import net.ddns.swinterberger.payanotherround.entities.User;

import java.util.List;

/**
 * Activity to Create a Summary for all User of this Trip.
 *
 * @author Stefan Winteberger
 * @version 1.0
 */
public final class SummaryTripUsers extends AppCompatActivity {

    private List<User> allUsers;
    private DbAdapter dbAdapter = new DbAdapter(this);

    private ListView userListView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summarey_trip_users);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        TextView tvTripTitle = (TextView) findViewById(R.id.tv_TripTitle);
        tvTripTitle.setText(getResources().getString(R.string.lable_summary));

        long tripId = getIntent().getLongExtra(getResources().getString(R.string.extra_tripid), -1L);
        allUsers = dbAdapter.getCrudUser().readUsersByTripId(tripId);

        refreshList();
    }

    private void refreshList() {
        if (this.userListView == null) {
            userListView = (ListView) findViewById(R.id.lv_Users);
            registerForContextMenu(userListView);
        }
        userListView.setAdapter(new SummaryTripUsers.UserSimpleListItemAdapter());
    }

    private class UserSimpleListItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return allUsers.size();
        }

        @Override
        public Object getItem(final int position) {
            return allUsers.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return allUsers.get(position).getId();
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View returnView = convertView;
            if (returnView == null) {
                returnView = SummaryTripUsers.this.getLayoutInflater().inflate(R.layout.listitem_user_simple, null);

                TextView nameField = (TextView) returnView.findViewById(R.id.tv_Name);
                nameField.setText(allUsers.get(position).getName());

                returnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SummaryTripUsers.this, UserSummary.class);
                        intent.putExtra(getResources().getString(R.string.extra_userId), allUsers.get(position).getId());
                        startActivity(intent);
                    }
                });
            }
            return returnView;
        }
    }
}
