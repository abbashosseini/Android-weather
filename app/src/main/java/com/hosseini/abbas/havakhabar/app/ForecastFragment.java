package com.hosseini.abbas.havakhabar.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hosseini.abbas.havakhabar.app.data.WeatherContract;
import com.hosseini.abbas.havakhabar.app.data.WeatherContract.LocationEntry;
import com.hosseini.abbas.havakhabar.app.data.WeatherContract.WeatherEntry;
import com.hosseini.abbas.havakhabar.app.sync.SSyncAdapter;

import java.util.Date;

/**
 * Created by Paul Shi on 9/15/14.
 */

public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = ForecastFragment.class.getSimpleName();
    private static final int FORECAST_LOADER = 0;
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private boolean mUseTodayLayout;

    private static final String SELECTED_KEY = "selected_position";
    private String mLocation;

    // Specify the columns we need.

    private static final String[] FORECAST_COLUMNS = {

            WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
            WeatherEntry.COLUMN_DATETEXT,
            WeatherEntry.COLUMN_SHORT_DESC,
            WeatherEntry.COLUMN_MAX_TEMP,
            WeatherEntry.COLUMN_MIN_TEMP,
            LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherEntry.COLUMN_WEATHER_ID,
            LocationEntry.COLUMN_COORD_LAT,
            LocationEntry.COLUMN_COORD_LONG
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.

    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DATE = 1;
    public static final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_LOCATION_SETTING = 5;
    public static final int COL_WEATHER_CONDITION_ID = 6;
    public static final int COL_COORD_LAT = 7;
    public static final int COL_COORD_LONG = 8;

    private ForecastAdapter mForecastAdapter;

    public ProgressBar PB = null;
    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String date);
    }

    public ForecastFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PB = (ProgressBar) getActivity().findViewById(R.id.progressBar);

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mForecastAdapter = new ForecastAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        mListView.setAdapter(mForecastAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mForecastAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    ((Callback)getActivity())
                            .onItemSelected(cursor.getString(COL_WEATHER_DATE));
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mForecastAdapter.setUseTodayLayout(mUseTodayLayout);

        return rootView;
    }

    private void updateWeather(){

        SSyncAdapter.syncImmediately(getActivity());

    }

    private void openPreferredLocationInMap() {

        if ( null != mForecastAdapter ) {
            Cursor c = mForecastAdapter.getCursor();
            if ( null != c ) {
                c.moveToPosition(0);
                String posLat = c.getString(COL_COORD_LAT);
                String posLong = c.getString(COL_COORD_LONG);
                Uri geoLocation = Uri.parse("geo:" + posLat + "," + posLong);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(geoLocation);

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(),"لطفا نام شهر را وارد کرده و منتظر اطلاعات بمانید .",Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String startDate = WeatherContract.getDbDateString(new Date());

        // Sort order:  Ascending, by date.

        String sortOrder = WeatherEntry.COLUMN_DATETEXT + " ASC";

        mLocation = Utility.getPreferredLocation(getActivity());
        Uri weatherForLocationUri = WeatherEntry.buildWeatherLocationWithStartDate(
                mLocation, startDate);

        return new CursorLoader(
                getActivity(),
                weatherForLocationUri,
                FORECAST_COLUMNS,
                null,
                null,
                sortOrder
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
        mForecastAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mForecastAdapter.swapCursor(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocation != null && !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
            getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
        if (mForecastAdapter != null) {
            mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
        }
    }
}