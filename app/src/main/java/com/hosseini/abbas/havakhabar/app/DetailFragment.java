package com.hosseini.abbas.havakhabar.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hosseini.abbas.havakhabar.app.Other.Utility;
import com.hosseini.abbas.havakhabar.app.Other.WhichFont;
import com.hosseini.abbas.havakhabar.app.data.WeatherContract;
import com.hosseini.abbas.havakhabar.app.data.WeatherContract.WeatherEntry;

/**
 * Created by Abbas on 10/8/14.
 */

@SuppressLint("ValidFragment")
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private String FORECAST_SHARE_HASHTAG;
    private ShareActionProvider mShareActionProvider;
    private String mLocation;
    private String mForecast;
    private String mDateStr;
    private Context mcontext;

    private static final int DETAIL_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
            WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
            WeatherEntry.COLUMN_DATETEXT,
            WeatherEntry.COLUMN_SHORT_DESC,
            WeatherEntry.COLUMN_MAX_TEMP,
            WeatherEntry.COLUMN_MIN_TEMP,
            WeatherEntry.COLUMN_HUMIDITY,
            WeatherEntry.COLUMN_PRESSURE,
            WeatherEntry.COLUMN_WIND_SPEED,
            WeatherEntry.COLUMN_DEGREES,
            WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };

    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }
    public DetailFragment(Context context) {
        setHasOptionsMenu(true);

        mcontext = context;

        FORECAST_SHARE_HASHTAG = mcontext
                .getString(R.string.share_HashTag);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DetailActivity.LOCATION_KEY, mLocation);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(DetailActivity.DATE_KEY) &&
                mLocation != null &&
                !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mDateStr = arguments.getString(DetailActivity.DATE_KEY);
        }

        if (savedInstanceState != null) {
            mLocation = savedInstanceState.getString(DetailActivity.LOCATION_KEY);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);

        mDateView = (TextView) rootView.findViewById(R.id.detail_date_textview);
        mDateView.setTypeface(WhichFont.Fontmethod(getActivity(), ""));

        mFriendlyDateView = (TextView) rootView.findViewById(R.id.detail_day_textview);
        mFriendlyDateView.setTypeface(WhichFont.Fontmethod(getActivity(), "fonts/Far_TitrDF.ttf"));

        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_forecast_textview);

        mHighTempView = (TextView) rootView.findViewById(R.id.detail_high_textview);
        mHighTempView.setTypeface(WhichFont.Fontmethod(getActivity(), "fonts/Far_TitrDF.ttf"));

        mLowTempView = (TextView) rootView.findViewById(R.id.detail_low_textview);
        mLowTempView.setTypeface(WhichFont.Fontmethod(getActivity(), ""));
        mHumidityView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
        mHumidityView.setTypeface(WhichFont.Fontmethod(getActivity(), ""));
        mWindView = (TextView) rootView.findViewById(R.id.detail_wind_textview);
        mWindView.setTypeface(WhichFont.Fontmethod(getActivity(), ""));
        mPressureView = (TextView) rootView.findViewById(R.id.detail_pressure_textview);
        mPressureView.setTypeface(WhichFont.Fontmethod(getActivity(), ""));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu

        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item

        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.

        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mLocation = savedInstanceState.getString(DetailActivity.LOCATION_KEY);
        }

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(DetailActivity.DATE_KEY)) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        // Sort order:  Ascending, by date.

        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATETEXT + " ASC";

        mLocation = Utility.getPreferredLocation(getActivity());
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                mLocation, mDateStr);


        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.

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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            // Read weather condition ID from cursor

            int weatherId = data.getInt(data.getColumnIndex(WeatherEntry.COLUMN_WEATHER_ID));

            // Use weather art image

            mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

            // Read date from cursor and update views for day of week .

            String date = data.getString(data.getColumnIndex(WeatherEntry.COLUMN_DATETEXT));
            String friendlyDateText = Utility.getDayName(getActivity(), date);
            String dateText = Utility.getFormattedMonthDay(date);

            if (friendlyDateText.equals("Saturday")) {
                mFriendlyDateView.setText(mcontext.getString(R.string.week_day_name_saturday));
            }else if (friendlyDateText.equals("Sunday")) {
                mFriendlyDateView.setText(mcontext.getString(R.string.week_day_name_sunday));
            }else if (friendlyDateText.equals("Monday")) {
                mFriendlyDateView.setText(mcontext.getString(R.string.week_day_name_monday));
            }else if (friendlyDateText.equals("Tuesday")) {
                mFriendlyDateView.setText(mcontext.getString(R.string.week_day_name_tuesday));
            }else if (friendlyDateText.equals("Wednesday")) {
                mFriendlyDateView.setText(mcontext.getString(R.string.week_day_name_wednesday));
            }else if (friendlyDateText.equals("Thursday")) {
                mFriendlyDateView.setText(mcontext.getString(R.string.week_day_name_Thursday));
            }else if (friendlyDateText.equals("Friday")) {
                mFriendlyDateView.setText(mcontext.getString(R.string.week_day_name_friday));
            }else if (friendlyDateText.length() > 4)
                mFriendlyDateView.setText(mcontext.getString(R.string.today));
            else
                mFriendlyDateView.setText(mcontext.getString(R.string.tomorrow));

            //mFriendlyDateView.setText(friendlyDateText);
            mDateView.setText(dateText);

            // Read description from cursor and update view

            String description = data.getString(data.getColumnIndex(
                    WeatherEntry.COLUMN_SHORT_DESC));


            ScrollView appropriateBackground = (ScrollView) getActivity().findViewById(R.id.TabletBackGroundIMG);
            if (getResources().getConfiguration().orientation == 2)
            if (description.equals("Clouds")){

                appropriateBackground.setBackgroundResource(R.drawable.clouds_l);
                description=getResources().getString(R.string.Clouds_fr);

            }else if (description.equals("Clear")){

                appropriateBackground.setBackgroundResource(R.drawable.foggy_l);
                description=getResources().getString(R.string.Clear_fr);

            }else if (description.equals("Rain")){

                appropriateBackground.setBackgroundResource(R.drawable.rain_l);
                description=getResources().getString(R.string.Rain_fr);

            }else if (description.equals("Snow")){

                appropriateBackground.setBackgroundResource(R.drawable.snowl);
                description=getResources().getString(R.string.Snow_fr);

            }else if (description.equals("Storm")){

                appropriateBackground.setBackgroundResource(R.drawable.strom_l);
                description=getResources().getString(R.string.Storm_fr);

            }else if (description.equals("Fog")){

                appropriateBackground.setBackgroundResource(R.drawable.foggy_l);
                description=getResources().getString(R.string.Foggy_fr);

            }


            if (getResources().getConfiguration().orientation == 1)
                if (description.equals("Clouds")){

                    appropriateBackground.setBackgroundResource(R.drawable.clouds_p);
                    description=getResources().getString(R.string.Clouds_fr);

                }else if (description.equals("Clear")){

                    appropriateBackground.setBackgroundResource(R.drawable.foggy_p);
                    description=getResources().getString(R.string.Clear_fr);

                }else if (description.equals("Rain")){

                    appropriateBackground.setBackgroundResource(R.drawable.rain_p);
                    description=getResources().getString(R.string.Rain_fr);

                }else if (description.equals("Snow")){

                    appropriateBackground.setBackgroundResource(R.drawable.snow_p);
                    description=getResources().getString(R.string.Snow_fr);

                }else if (description.equals("Storm")){

                    appropriateBackground.setBackgroundResource(R.drawable.storm_p);
                    description=getResources().getString(R.string.Storm_fr);

                }else if (description.equals("Fog")){

                    appropriateBackground.setBackgroundResource(R.drawable.foggy_p);
                    description=getResources().getString(R.string.Foggy_fr);

                }


            // For accessibility, add a content description to the icon field

            mIconView.setContentDescription(description);

            // Read high temperature from cursor and update view


            double high = data.getDouble(data.getColumnIndex(WeatherEntry.COLUMN_MAX_TEMP));
            String highString = Utility.formatTemperature(getActivity(), high);
            mHighTempView.setText(highString);

            // Read low temperature from cursor and update view

            double low = data.getDouble(data.getColumnIndex(WeatherEntry.COLUMN_MIN_TEMP));
            String lowString = Utility.formatTemperature(getActivity(), low);
            mLowTempView.setText(lowString);

            // Read humidity from cursor and update view

            float humidity = data.getFloat(data.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY));
            mHumidityView.setText(getActivity().getString(R.string.format_humidity, humidity));

            // Read wind speed and direction from cursor and update view

            float windSpeedStr = data.getFloat(data.getColumnIndex(WeatherEntry.COLUMN_WIND_SPEED));
            float windDirStr = data.getFloat(data.getColumnIndex(WeatherEntry.COLUMN_DEGREES));
            mWindView.setText(Utility.getFormattedWind(getActivity(), windSpeedStr, windDirStr));

            // Read pressure from cursor and update view

            float pressure = data.getFloat(data.getColumnIndex(WeatherEntry.COLUMN_PRESSURE));
            mPressureView.setText(getActivity().getString(R.string.format_pressure, pressure));

            // We still need this for the share intent

            mForecast = String.format("%s - %s - %s/%s", dateText, description, high, low);

            // If onCreateOptionsMenu has already happened, we need to update the share intent now.

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }

            boolean isMetric = Utility.isMetric(getActivity());

            TextView DetailCoutBy = (TextView) getActivity().findViewById(R.id.detail_CountBy);
            DetailCoutBy.setTypeface(WhichFont.Fontmethod(getActivity(), ""));
            if (isMetric){
                DetailCoutBy.setText(mcontext.getString(R.string.degrees_c));
            }else{
                DetailCoutBy.setText(mcontext.getString(R.string.degrees_f));
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }
}