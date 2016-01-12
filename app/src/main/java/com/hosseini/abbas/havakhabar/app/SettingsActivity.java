package com.hosseini.abbas.havakhabar.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hosseini.abbas.havakhabar.app.data.WeatherContract;
import com.hosseini.abbas.havakhabar.app.sync.SunshineSyncAdapter;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {
    ListPreference etp = null;
    boolean mBindingPreference;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setContentView(R.layout.devloper);

        TextView dev = (TextView) findViewById(R.id.dev);
        dev.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/CaviarDreams.ttf"));

        TextView sazande = (TextView) findViewById(R.id.sazande);
        sazande.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Far_TitrDF.ttf"));

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_location_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_units_key)));
        //bindPreferenceSummaryToValue(findPreference(getString(R.string.keshvar)));


        /// The reference to the preference in the view heirarchy
        final EditTextPreference mUrlPreference = (EditTextPreference) getPreferenceScreen().findPreference(getString(R.string.pref_location_key));
        // Add a textwatcher
        mUrlPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mUrlPreference.setPositiveButtonText(R.string.okey);
                mUrlPreference.setNegativeButtonText(R.string.cancel);

                Button button = (Button) mUrlPreference.getDialog().findViewById(android.R.id.button1);
                button.setText(R.string.okey);
                button.invalidate(); // if necessary

                Button button2 = (Button) mUrlPreference.getDialog().findViewById(android.R.id.button2);
                button2.setText(R.string.cancel);
                button2.invalidate(); // if necessary

                return false;
            }
        });


//        Toast.makeText(getApplicationContext(), etp.getValue(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), findPreference("location").getSummary()+"1111",Toast.LENGTH_LONG).show();
    }

     // Attaches a listener so the summary is always updated with the preference value.
     // Also fires the listener once, to initialize the summary (so it shows up before the value
     // is changed.)

    private void bindPreferenceSummaryToValue(Preference preference) {
        mBindingPreference = true;

        // Set the listener to watch for value changes.

        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.

        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
        mBindingPreference = false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        String stringValue = value.toString();


        if ( !mBindingPreference ) {
            if (preference.getKey().equals(getString(R.string.pref_location_key))) {
                SunshineSyncAdapter.syncImmediately(this);
            } else {
                // notify code that weather may be impacted
                getContentResolver().notifyChange(WeatherContract.WeatherEntry.CONTENT_URI, null);
            }
        }

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
            Toast.makeText(getApplication(), stringValue, Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

}