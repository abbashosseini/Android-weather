package com.hosseini.abbas.havakhabar.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.hosseini.abbas.havakhabar.app.data.WeatherContract.LocationEntry;
import com.hosseini.abbas.havakhabar.app.data.WeatherContract.WeatherEntry;
import com.hosseini.abbas.havakhabar.app.data.WeatherDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by paulshi on 9/18/14.
 */
public class TestDb extends AndroidTestCase{

    public static final String LOG_TAG = TestDb.class.getSimpleName();
    String TEST_CITY = "North Pole";

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    //Set dummy values for location

    ContentValues getLocationContentValues(){

        String testLocationSetting = "99705";
        double testLatitude = 64.772;
        double testLongitude = -147.355;

        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_CITY_NAME, TEST_CITY);
        values.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        values.put(LocationEntry.COLUMN_COORD_LAT, testLatitude);
        values.put(LocationEntry.COLUMN_COORD_LONG, testLongitude);

        return values;
    }

    ContentValues getWeatherContentValues(long locationRowId){

        String testDate = "20141205";
        double testDegrees = 1.1;
        double testHumidity = 1.2;
        double testPressure = 1.3;
        double testMaxTemp = 75;
        double testMinTemp = 65;
        String testShortDesc = "Asteroids";
        double testWind = 5.5;
        double testWeatherId = 321;

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

    public void testInsertReadDb() {
        //Test data

        //Make DB for testing
        WeatherDbHelper dbHelper =
                new WeatherDbHelper(mContext);
        SQLiteDatabase sqLiteDb = dbHelper.getWritableDatabase();
        assertEquals(true, sqLiteDb.isOpen());

        ContentValues values = getLocationContentValues();

        long locationRowId;
        locationRowId = sqLiteDb.insert(LocationEntry.TABLE_NAME, null, values);

        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        Cursor cursor = sqLiteDb.query(
                LocationEntry.TABLE_NAME, //Table to Query
                null, //Just get all columns
                null, //Columns for the "where" clause
                null, //Values for the "where" clause
                null, //columns to group by
                null, //columns to filter by row groups
                null //sort order
        );

        if (cursor.moveToFirst()) {

            validateCursor(values, cursor);

        }
        else {
            fail("No values returned. Womp Womp :(");
        }

        ContentValues weatherValues = getWeatherContentValues(locationRowId);

        long weatherRowId;
        weatherRowId = sqLiteDb.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue(weatherRowId != -1);

        Cursor weatherCursor = sqLiteDb.query(
                WeatherEntry.TABLE_NAME, //Table to Query
                null,
                null, //Columns for the "where" clause
                null, //Values for the "where" clause
                null, //columns to group by
                null, //columns to filter by row groups
                null //sort order
        );

        System.out.println(weatherCursor);

        if (weatherCursor.moveToFirst()) {

            validateCursor(weatherValues, weatherCursor);

        }
        else {
            fail("No weather data returned. Womp Womp :(");
        }
        dbHelper.close();
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

    static ContentValues createNorthPoleLocationValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(LocationEntry.COLUMN_LOCATION_SETTING, "99705");
        testValues.put(LocationEntry.COLUMN_CITY_NAME, "North Pole");
        testValues.put(LocationEntry.COLUMN_COORD_LAT, 64.7488);
        testValues.put(LocationEntry.COLUMN_COORD_LONG, -147.353);

        return testValues;
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

}
