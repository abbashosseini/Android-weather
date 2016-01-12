package com.hosseini.abbas.havakhabar.app;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import com.hosseini.abbas.havakhabar.app.data.WeatherContract.LocationEntry;
import com.hosseini.abbas.havakhabar.app.data.WeatherContract.WeatherEntry;

import java.util.Map;
import java.util.Set;

/**
 * Created by paulshi on 9/18/14.
 */

public class TestProvider extends AndroidTestCase{

    public static final String LOG_TAG = TestProvider.class.getSimpleName();
    static final String KALAMAZOO_LOCATION_SETTING = "kalamazoo";
    static final String KALAMAZOO_WEATHER_START_DATE = "20140625";
    long locationRowId;
    String TEST_CITY = "North Pole";
    String TEST_LOCATION = "99705";
    String TEST_DATE = "20141205";

    //Set dummy values for location

    static ContentValues createNorthPoleLocationValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(LocationEntry.COLUMN_LOCATION_SETTING, "99705");
        testValues.put(LocationEntry.COLUMN_CITY_NAME, "North Pole");
        testValues.put(LocationEntry.COLUMN_COORD_LAT, 64.7488);
        testValues.put(LocationEntry.COLUMN_COORD_LONG, -147.353);

        return testValues;
    }

    static ContentValues createWeatherValues(long locationRowId) {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherEntry.COLUMN_DATETEXT, "20141205");
        weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.1);
        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.2);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.3);
        weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 75);
        weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 65);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 5.5);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 321);

        return weatherValues;
    }

    // brings database to an empty state
    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                WeatherEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                LocationEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).

    public void setUp() {
        deleteAllRecords();
    }

    public void testInsertReadProvider() {

        //Test data

        ContentValues values = createNorthPoleLocationValues();

        Uri locationUri = mContext.getContentResolver()
                .insert(LocationEntry.CONTENT_URI, values);
        long locationRowId = ContentUris.parseId(locationUri);

        Log.d(LOG_TAG, "New row id: " + locationRowId);

        ContentValues weatherValues = createWeatherValues(locationRowId);

        Uri weatherInsertUri = mContext.getContentResolver()
                .insert(WeatherEntry.CONTENT_URI, weatherValues);
        long weatherRowId = ContentUris.parseId(weatherInsertUri);
        assertTrue(weatherInsertUri != null);

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back

        addAllContentValues(weatherValues, values);

        // Get the joined Weather and Location data

        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocation(TEST_LOCATION),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        validateCursor(weatherValues, weatherCursor);
        weatherCursor.close();

        // Get the joined Weather and Location data with a start date

        weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithStartDate(
                        TEST_LOCATION, TEST_DATE),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        validateCursor(weatherValues, weatherCursor);

        weatherCursor.close();

        // A cursor is your primary interface to the query results.
        /*Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        validateCursor(values, cursor);

        // Now see if we can successfully query if we include the row id
        cursor = mContext.getContentResolver().query(
                LocationEntry.buildLocationUri(locationRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        validateCursor(values, cursor);*/

        weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithDate(TEST_LOCATION, TEST_DATE),
                null,
                null,
                null,
                null
        );
        validateCursor(weatherValues, weatherCursor);
    }

    static void validateCursor(ContentValues expectedValues, Cursor valueCursor) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();

    }

    public void testGetType(){
        String type = mContext.getContentResolver().getType(WeatherEntry.CONTENT_URI);
        assertEquals(WeatherEntry.CONTENT_TYPE,type);

        String testLocation = "94074";
        type = mContext.getContentResolver().getType(WeatherEntry.buildWeatherLocation(testLocation));
        assertEquals(WeatherEntry.CONTENT_TYPE,type);

        String testDate = "20140612";
        type = mContext.getContentResolver().getType(WeatherEntry.buildWeatherLocationWithDate(testLocation,testDate));
        assertEquals(WeatherEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
        assertEquals(LocationEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(LocationEntry.buildLocationUri(1L));
        assertEquals(LocationEntry.CONTENT_ITEM_TYPE, type);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    void addAllContentValues(ContentValues destination, ContentValues source) {
        for (String key : source.keySet()) {
            destination.put(key, source.getAsString(key));
        }
    }

    public void testUpdateLocation() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestDb.createNorthPoleLocationValues();

        Uri locationUri = mContext.getContentResolver().
                insert(LocationEntry.CONTENT_URI, values);
        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(LocationEntry._ID, locationRowId);
        updatedValues.put(LocationEntry.COLUMN_CITY_NAME, "Santa's Village");

        int count = mContext.getContentResolver().update(
                LocationEntry.CONTENT_URI, updatedValues, LocationEntry._ID + "= ?",
                new String[] { Long.toString(locationRowId)});

        assertEquals(count, 1);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.buildLocationUri(locationRowId),
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null // sort order
        );

        TestDb.validateCursor(updatedValues, cursor);
    }

    // Make sure we can still delete after adding/updating stuff
    public void testDeleteRecordsAtEnd() {
        deleteAllRecords();
    }

    static ContentValues createKalamazooWeatherValues(long locationRowId) {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherEntry.COLUMN_DATETEXT, KALAMAZOO_WEATHER_START_DATE);
        weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.2);
        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.5);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.1);
        weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 85);
        weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 35);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Cats and Dogs");
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 3.4);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 42);

        return weatherValues;
    }

    static ContentValues createKalamazooLocationValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(LocationEntry.COLUMN_LOCATION_SETTING, KALAMAZOO_LOCATION_SETTING);
        testValues.put(LocationEntry.COLUMN_CITY_NAME, "Kalamazoo");
        testValues.put(LocationEntry.COLUMN_COORD_LAT, 42.2917);
        testValues.put(LocationEntry.COLUMN_COORD_LONG, -85.5872);

        return testValues;
    }


    // Inserts both the location and weather data for the Kalamazoo data set.
    public void insertKalamazooData() {
        ContentValues kalamazooLocationValues = createKalamazooLocationValues();
        Uri locationInsertUri = mContext.getContentResolver()
                .insert(LocationEntry.CONTENT_URI, kalamazooLocationValues);
        assertTrue(locationInsertUri != null);

        locationRowId = ContentUris.parseId(locationInsertUri);

        ContentValues kalamazooWeatherValues = createKalamazooWeatherValues(locationRowId);
        Uri weatherInsertUri = mContext.getContentResolver()
                .insert(WeatherEntry.CONTENT_URI, kalamazooWeatherValues);
        assertTrue(weatherInsertUri != null);
    }

    public void testUpdateAndReadWeather() {
        insertKalamazooData();
        String newDescription = "Cats and Frogs (don't warn the tadpoles!)";

        // Make an update to one value.
        ContentValues kalamazooUpdate = new ContentValues();
        kalamazooUpdate.put(WeatherEntry.COLUMN_SHORT_DESC, newDescription);

        mContext.getContentResolver().update(
                WeatherEntry.CONTENT_URI, kalamazooUpdate, null, null);

        // A cursor is your primary interface to the query results.
        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make the same update to the full ContentValues for comparison.
        ContentValues kalamazooAltered = createKalamazooWeatherValues(locationRowId);
        kalamazooAltered.put(WeatherEntry.COLUMN_SHORT_DESC, newDescription);

        TestDb.validateCursor(kalamazooAltered,weatherCursor);
    }

    public void testRemoveHumidityAndReadWeather() {
        insertKalamazooData();

        mContext.getContentResolver().delete(WeatherEntry.CONTENT_URI,
                WeatherEntry.COLUMN_HUMIDITY + " = " + locationRowId, null);

        // A cursor is your primary interface to the query results.
        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make the same update to the full ContentValues for comparison.
        ContentValues kalamazooAltered = createKalamazooWeatherValues(locationRowId);
        kalamazooAltered.remove(WeatherEntry.COLUMN_HUMIDITY);

        TestDb.validateCursor(kalamazooAltered, weatherCursor);
    }
}
