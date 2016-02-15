package com.hosseini.abbas.havakhabar.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dateparser.hosseini.persiantime.dates.PersianDate.CurrentDate;
import com.dateparser.hosseini.persiantime.dates.PersianDate.GenerateDates;
import com.hosseini.abbas.havakhabar.app.Other.Utility;
import com.hosseini.abbas.havakhabar.app.Other.WhichFont;
import com.hosseini.abbas.havakhabar.app.data.WeatherContract;
import com.hosseini.abbas.havakhabar.app.sync.SSyncAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    // Flag to determine if we want to use a separate view for "today".
    private boolean mUseTodayLayout = true;

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /**
     * Copy/paste note: Replace existing newView() method in ForecastAdapter with this one.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        if (viewType == VIEW_TYPE_TODAY){
            layoutId = R.layout.list_item_forecast_today;
        }
        else if (viewType == VIEW_TYPE_FUTURE_DAY) {
            layoutId = R.layout.list_item_forecast;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Set appropriate values through vieHolder references instead of using findViewById calls
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read weather icon ID from cursor
        //int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_ID);
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                // Get weather icon
                viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(
                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                // Get weather icon
                viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(
                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
        }

        // Read date from cursor
        String dateString = cursor.getString(ForecastFragment.COL_WEATHER_DATE);
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.


        //Status Today
        if(Utility.getFriendlyDayString(context, dateString)
                .contains(context.getString(R.string.today))) {

            int icon = R.drawable.app_icon;
            CharSequence tickerText = context.getResources().getString(R.string.app_name);
            long when = System.currentTimeMillis();

            Notification notification = new Notification(icon, tickerText, when);

            String currentdateOS = new CurrentDate()
                                    .getdateWithMonthLetters(GenerateDates.getCurrentDate());

            if (Locale.getDefault().getLanguage().equals("en")){
                SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd", Locale.ENGLISH);
                currentdateOS = format.format(new Date());
            }


            String contentTitle = context.getString(R.string.app_name)+
                                        " - " +
                                        currentdateOS;
            String contentText =
                    context.getString(R.string.statusbar_degrees_max) + Utility.formatTemperature(context, cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP)) +
                    context.getString(R.string.statusbar_degrees_min) + Utility.formatTemperature(context, cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));


            notification.setLatestEventInfo(context, contentTitle, contentText, PendingIntent.getActivity(context, 0, new Intent(), 0));
            notification.flags |= Notification.FLAG_NO_CLEAR;
            mNotificationManager.notify(1, notification);

        }

        // Find TextView and set formatted date on it
        TextView dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
        dateView.setTypeface(WhichFont.Fontmethod(context.getApplicationContext(), ""));
        if (Utility.getFriendlyDayString(context, dateString).equals("Saturday")) {
            viewHolder.dateView.setText(context.getString(R.string.week_day_name_saturday));
        }else if (Utility.getFriendlyDayString(context, dateString).equals("Sunday")) {
            viewHolder.dateView.setText(context.getString(R.string.week_day_name_sunday));
        }else if (Utility.getFriendlyDayString(context, dateString).equals("Monday")) {
            viewHolder.dateView.setText(context.getString(R.string.week_day_name_monday));
        }else if (Utility.getFriendlyDayString(context, dateString).equals("Tuesday")) {
            viewHolder.dateView.setText(context.getString(R.string.week_day_name_tuesday));
        }else if (Utility.getFriendlyDayString(context, dateString).equals("Wednesday")) {
            viewHolder.dateView.setText(context.getString(R.string.week_day_name_wednesday));
        }else if (Utility.getFriendlyDayString(context, dateString).equals("Thursday")) {
            viewHolder.dateView.setText(context.getString(R.string.week_day_name_Thursday));
        }else if (Utility.getFriendlyDayString(context, dateString).equals("Friday")) {
            viewHolder.dateView.setText(context.getString(R.string.week_day_name_friday));
        }else{
            viewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateString));
        }

        //Toast.makeText(context.getApplicationContext(),
          //      Utility.getFriendlyDayString(context, dateString),Toast.LENGTH_LONG).show();
        // Read weather forecast from cursor
        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        // Find TextView and set weather forecast on it
        //viewHolder.descriptionView.setText(description);

        if (SSyncAdapter.City_Name != ""){

            viewHolder.City__name.setText(SSyncAdapter.City_Name);

        }else {

            viewHolder.City__name.setText(WeatherContract.LocationEntry.COLUMN_CITY_NAME);

        }

        viewHolder.City__name.setTypeface(WhichFont.Fontmethod(context,  "fonts/Existence-Light.otf"));

        viewHolder.descriptionView.setTypeface(WhichFont.Fontmethod(context, "fonts/AAfsaneh.ttf"));
        if (description.equalsIgnoreCase("Clouds")){
            viewHolder.descriptionView.setText(context.getString(R.string.sky_state_clouds));
        }else if (description.equalsIgnoreCase("Clear")){
            viewHolder.descriptionView.setText(context.getString(R.string.sky_state_clear));
        }else if (description.equalsIgnoreCase("Rain")){
            viewHolder.descriptionView.setText(context.getString(R.string.sky_state_rain));
        }else if (description.equalsIgnoreCase("Snow")){
            viewHolder.descriptionView.setText(context.getString(R.string.sky_state_snowy));
        }else if (description.equalsIgnoreCase("Storm")){
            viewHolder.descriptionView.setText(context.getString(R.string.sky_state_storm));
        }else if (description.equalsIgnoreCase("Fog")){
            viewHolder.descriptionView.setText(context.getString(R.string.sky_state_foggy));
        }


        // For accessibility, add a content description to the icon field
        viewHolder.iconView.setContentDescription(description);

        // Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        //TextView highView = (TextView) view.findViewById(R.id.list_item_high_textview);
        viewHolder.highTempView.setTypeface(WhichFont.Fontmethod(context, ""));
        viewHolder.highTempView.setText(Utility.formatTemperature(context, high));

        // Read low temperature from cursor
        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        //TextView lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
        viewHolder.lowTempView.setTypeface(WhichFont.Fontmethod(context, ""));
        viewHolder.lowTempView.setText(Utility.formatTemperature(context, low));
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;

    }

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView City__name;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            City__name = (TextView) view.findViewById(R.id.City__name);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }
}