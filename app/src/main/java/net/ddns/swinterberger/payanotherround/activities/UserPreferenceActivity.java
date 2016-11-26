package net.ddns.swinterberger.payanotherround.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import net.ddns.swinterberger.payanotherround.R;

/**
 * Activity for User-Settings.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public class UserPreferenceActivity extends PreferenceActivity {

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new UserPreferenceInitializer())
                .commit();
    }


    public static final class UserPreferenceInitializer extends PreferenceFragment {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferneces);
        }
    }
}
