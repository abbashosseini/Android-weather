package com.hosseini.abbas.havakhabar.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hosseini.abbas.havakhabar.app.data.WeatherContract;
import com.hosseini.abbas.havakhabar.app.dates.CalenderTool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Utility {

    // Format used for storing dates in the database.  ALso used for converting those strings
// back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";

    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key),
                context.getString(R.string.pref_location_default));
    }

    public static String formatTemperature(Context context, double temperature) {
        // Data stored in Celsius by default.  If user prefers to see in Fahrenheit, convert
        // the values here.
        String suffix = "\u00B0";
        if (!isMetric(context)) {
            temperature = (temperature * 1.8) + 32;
        }

        // For presentation, assume the user doesn't care about tenths of a degree.
        return String.format(context.getString(R.string.format_temperature), temperature);
    }

    static String formatDate(String dateString) {
        Date date = WeatherContract.getDateFromDb(dateString);
        return DateFormat.getDateInstance().format(date);
    }

    /**
     * Helper method to convert the database representation of the date into something to display
     * to users.
     *
     * @param context Context to use for resource localization
     * @param dateStr The db formatted date string, expected to be of the form specified
     *                in Utility.DATE_FORMAT
     * @return a user-friendly representation of the date.
     */
    public static String getFriendlyDayString(Context context, String dateStr) {

        // The day string for forecast uses the following logic:
        // For today: "Today, June 8"
        // For tomorrow:  "Tomorrow"
        // For the next 5 days: "Wednesday" (just the day name)
        // For all days after that: "Mon Jun 8"

        Date todayDate = new Date();
        String todayStr = WeatherContract.getDbDateString(todayDate);
        Date inputDate = WeatherContract.getDateFromDb(dateStr);

        // If the date we're building the String for is today's date, the format
        // is "Today, June 24"

        if (todayStr.equals(dateStr)) {

            String today = context.getString(R.string.today);

            return context.getString(
                    R.string.format_full_friendly_date,
                    today,
                    getFormattedMonthDay(context, dateStr));
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(todayDate);
            cal.add(Calendar.DATE, 7);
            String weekFutureString = WeatherContract.getDbDateString(cal.getTime());

            if (dateStr.compareTo(weekFutureString) < 0) {
                // If the input date is less than a week in the future, just return the day name.
                return getDayName(context, dateStr);
            } else {
                // Otherwise, use the form "Mon Jun 3"
                SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MM dd",Locale.ENGLISH);
                String SplitDate =  shortenedDateFormat.format(inputDate);

//                Log.v("rrrrrrrrrttttttttttttttttt",SplitDate);
                String[] ArraySplitDate = SplitDate.split(" ");

                CalenderTool ct = new CalenderTool(Calendar.getInstance().get(Calendar.YEAR),
                                                    Integer.parseInt(ArraySplitDate[1]),
                                                    Integer.parseInt(ArraySplitDate[2]));

                String SplitRealDate =   ct.getIranianDate();
                String[] ArraySplitRealDate = SplitRealDate.split("/");

                String YEAR_Date = ArraySplitRealDate[0];
                int MonthINt = Integer.parseInt(ArraySplitRealDate[1]);
                String MonthStr = ArraySplitRealDate[1];
                int DayStr = Integer.parseInt(ArraySplitRealDate[2]);

                switch (MonthINt) {
                    case 1:
                        if (DayStr % 31 < 31) {
                            MonthStr = "فروردین";
                        }else{
                            MonthStr = "ارديبهشت";
                            DayStr = DayStr % 31;
                        }

                        break;
                    case 2:
                        if (DayStr % 31 < 31) {
                            MonthStr = "ارديبهشت";
                        }else{
                            MonthStr = "خرداد";
                            DayStr = DayStr % 31;
                        }

                        break;
                    case 3:
                        if (DayStr % 31 < 31) {
                            MonthStr = "خرداد";
                        }else{
                            MonthStr = "تير";
                            DayStr = DayStr % 31;
                        }

                        break;
                    case 4:
                        if (DayStr % 31 < 31) {
                            MonthStr = "تير";
                        }else{
                            MonthStr = "مرداد";
                            DayStr = DayStr % 31;
                        }

                        break;
                    case 5:
                        if (DayStr % 31 < 31) {
                            MonthStr = "مرداد";
                        }else{
                            MonthStr = "شهريور";
                            DayStr = DayStr % 31;
                        }

                        break;
                    case 6:
                        if (DayStr % 31 < 31) {
                            MonthStr = "شهريور";
                        }else{
                            MonthStr = "مهر";
                            DayStr = DayStr % 30;
                        }

                        break;
                    case 7:
                        if (DayStr % 30 < 30) {
                            MonthStr = "مهر";
                        }else{
                            MonthStr = "آبان";
                            DayStr = DayStr % 30;
                        }

                        break;
                    case 8:
                        if (DayStr % 30 < 30) {
                            MonthStr = "آبان";
                        }else{
                            MonthStr = "آذر";
                            DayStr = DayStr % 30;
                        }

                        break;
                    case 9:
                        if (DayStr % 30 < 30) {
                            MonthStr = "آذر";
                        }else{
                            MonthStr = "دي";
                            DayStr = DayStr % 30;
                        }

                        break;
                    case 10:
                        if (DayStr % 30 < 30) {
                            MonthStr = "دي";
                        }else{
                            MonthStr = "بهمن";
                            DayStr = DayStr % 30;
                        }

                        break;
                    case 11:

                        if (DayStr % 30 < 30) {
                            MonthStr = "بهمن";
                        }else{
                            MonthStr = "اسفند";
                            DayStr = DayStr % 29;
                        }
                        break;
                    case 12:
                        if (DayStr % 29 < 29) {
                            MonthStr = "اسفند";
                        }else{
                            MonthStr = "فروردین";
                            DayStr = DayStr % 31;
                        }
                        break;
                }

                String DayWeek = "";
                if (ArraySplitDate[0].equals("Sun"))
                    DayWeek = "يکشنبه";
                else if (ArraySplitDate[0].equals("Mon"))
                    DayWeek = "دوشنبه";
                else if (ArraySplitDate[0].equals("Tue"))
                    DayWeek = "سه شنبه";
                else if (ArraySplitDate[0].equals("Wed"))
                    DayWeek = "چهارشنبه";
                else if (ArraySplitDate[0].equals("Thu"))
                    DayWeek = "پنج شنبه";
                else if (ArraySplitDate[0].equals("Fri"))
                    DayWeek = "جمعه";
                else if (ArraySplitDate[0].equals("Sat"))
                    DayWeek = "شنبه";

                return DayWeek+ " " +DayStr+" "+MonthStr;
            }
        }
    }

    /**
     * Given a day, returns just the name to use for that day.
     * E.g "today", "tomorrow", "wednesday".
     *
     * @param context Context to use for resource localization
     * @param dateStr The db formatted date string, expected to be of the form specified
     *                in Utility.DATE_FORMAT
     * @return
     */

    public static String getDayName(Context context, String dateStr) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(Utility.DATE_FORMAT,Locale.ENGLISH);
        try {
            Date inputDate = dbDateFormat.parse(dateStr);
            Date todayDate = new Date();

            // If the date is today, return the localized version of "Today" instead of the actual
            // day name.

            if (WeatherContract.getDbDateString(todayDate).equals(dateStr)) {
                return context.getString(R.string.today);
            } else {
                // If the date is set for tomorrow, the format is "Tomorrow".
                Calendar cal = Calendar.getInstance();
                cal.setTime(todayDate);
                cal.add(Calendar.DATE, 1);
                Date tomorrowDate = cal.getTime();
                if (WeatherContract.getDbDateString(tomorrowDate).equals(
                        dateStr)) {
                    return context.getString(R.string.tomorrow);
                } else {
                    // Otherwise, the format is just the day of the week (e.g "Wednesday".
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE",Locale.ENGLISH);
                    return dayFormat.format(inputDate);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Couldn't process the date correctly.
            return "";
        }
    }

    /**
     * Converts db date format to the format "Month day", e.g "June 24".
     * @param context Context to use for resource localization
     * @param dateStr The db formatted date string, expected to be of the form specified
     *                in Utility.DATE_FORMAT
     * @return The day in the form of a string formatted "December 6"
     */

    public static String getFormattedMonthDay(Context context, String dateStr){
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(Utility.DATE_FORMAT,Locale.ENGLISH);
        SimpleDateFormat monthDayFormat= null;
        String monthDayString = null;
        Date inputDate = null;
        try {
            inputDate = dbDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        monthDayFormat
                = new SimpleDateFormat("MM dd",Locale.ENGLISH);
        monthDayString = monthDayFormat.format(inputDate);

        String[] ArraySplitDate = monthDayString.split(" ");

        CalenderTool ct = new CalenderTool(Calendar.getInstance().get(Calendar.YEAR),
                Integer.parseInt(ArraySplitDate[0]),
                Integer.parseInt(ArraySplitDate[1]));

        String SplitRealDate =   ct.getIranianDate();
        String[] ArraySplitRealDate = SplitRealDate.split("/");

        String YEAR_Date = ArraySplitRealDate[0];
        int MonthINt = Integer.parseInt(ArraySplitRealDate[1]);
        String MonthStr = ArraySplitRealDate[1];
        int DayStr = Integer.parseInt(ArraySplitRealDate[2]);

        switch (MonthINt) {
            case 1:
                if (DayStr % 31 < 31) {
                    MonthStr = "فروردین";
                }else{
                    MonthStr = "ارديبهشت";
                    DayStr = DayStr % 31;
                }

                break;
            case 2:
                if (DayStr % 31 < 31) {
                    MonthStr = "ارديبهشت";
                }else{
                    MonthStr = "خرداد";
                    DayStr = DayStr % 31;
                }

                break;
            case 3:
                if (DayStr % 31 < 31) {
                    MonthStr = "خرداد";
                }else{
                    MonthStr = "تير";
                    DayStr = DayStr % 31;
                }

                break;
            case 4:
                if (DayStr % 31 < 31) {
                    MonthStr = "تير";
                }else{
                    MonthStr = "مرداد";
                    DayStr = DayStr % 31;
                }

                break;
            case 5:
                if (DayStr % 31 < 31) {
                    MonthStr = "مرداد";
                }else{
                    MonthStr = "شهريور";
                    DayStr = DayStr % 31;
                }

                break;
            case 6:
                if (DayStr % 31 < 31) {
                    MonthStr = "شهريور";
                }else{
                    MonthStr = "مهر";
                    DayStr = DayStr % 30;
                }

                break;
            case 7:
                if (DayStr % 30 < 30) {
                    MonthStr = "مهر";
                }else{
                    MonthStr = "آبان";
                    DayStr = DayStr % 30;
                }

                break;
            case 8:
                if (DayStr % 30 < 30) {
                    MonthStr = "آبان";
                }else{
                    MonthStr = "آذر";
                    DayStr = DayStr % 30;
                }

                break;
            case 9:
                if (DayStr % 30 < 30) {
                    MonthStr = "آذر";
                }else{
                    MonthStr = "دي";
                    DayStr = DayStr % 30;
                }

                break;
            case 10:
                if (DayStr % 30 < 30) {
                    MonthStr = "دي";
                }else{
                    MonthStr = "بهمن";
                    DayStr = DayStr % 30;
                }

                break;
            case 11:

                if (DayStr % 30 < 30) {
                    MonthStr = "بهمن";
                }else{
                    MonthStr = "اسفند";
                    DayStr = DayStr % 29;
                }
                break;
            case 12:
                if (DayStr % 29 < 29) {
                    MonthStr = "اسفند";
                }else{
                    MonthStr = "فروردین";
                    DayStr = DayStr % 31;
                }
                break;
        }

        return DayStr+" "+MonthStr;
    }

    /**
     * Returns true if metric unit should be used, or false if
     * imperial units should be used.
     */

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_metric)).equals(
                context.getString(R.string.pref_units_metric));
    }

    public static String getFormattedWind(Context context, float windSpeed, float degrees) {
        int windFormat;
        if (Utility.isMetric(context)) {
            windFormat = R.string.format_wind_kmh;


        } else {

            windFormat = R.string.format_wind_mph;
            windSpeed = .621371192237334f * windSpeed;
        }

        // From wind direction in degrees, determine compass direction as a string (e.g NW)

        String direction = "نامشخص";
        String AzSamte = "از سمت ";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "شمال";//N
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "شمال شرقی";//NE
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "شرق";//E
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "جنوب شرقی";//SE
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "جنوب";//S
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = " جنوب غربی";//SW
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "غرب";//W
        } else if (degrees >= 292.5 || degrees < 22.5) {
            direction = "شمال غرب";//NW
        }


        if (!direction.equals("نامشخص"))
        {
            direction = AzSamte+direction;
        }
        return String.format(context.getString(windFormat), windSpeed, direction);
    }

    /**
     * Helper method to provide the icon resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */

    public static int getIconResourceForWeatherCondition(int weatherId) {

        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes

        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }

    /**
     * Helper method to provide the art resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding image. -1 if no relation is found.
     */

    public static int getArtResourceForWeatherCondition(int weatherId) {

        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
// http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes


        if (weatherId >= 200 && weatherId <= 232) {
            //ForecastAdapter.ViewHolder.backgroundIMgFirtItem.setBackgroundResource(R.drawable.storm_p);
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            //ForecastAdapter.ViewHolder.backgroundIMgFirtItem.setBackgroundResource(R.drawable.rain_p);
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            //ForecastAdapter.ViewHolder.backgroundIMgFirtItem.setBackgroundResource(R.drawable.rain_p);
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            //ForecastAdapter.ViewHolder.backgroundIMgFirtItem.setBackgroundResource(R.drawable.snow_p);
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            //ForecastAdapter.ViewHolder.backgroundIMgFirtItem.setBackgroundResource(R.drawable.rain_p);
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            //ForecastAdapter.ViewHolder.backgroundIMgFirtItem.setBackgroundResource(R.drawable.snow_p);
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            //ForecastAdapter.ViewHolder.backgroundIMgFirtItem.setBackgroundResource(R.drawable.foggy_p);
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            //ForecastAdapter.ViewHolder.backgroundIMgFirtItem.setBackgroundResource(R.drawable.storm_p);
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            //ForecastAdapter.ViewHolder.backgroundIMgFirtItem.setBackgroundResource(R.drawable.clear_p);
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            //ForecastAdapter.ViewHolder.backgroundIMgFirtItem.setBackgroundResource(R.drawable.clouds_p);
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            //backgroundIMgFirtItem.setBackgroundResource(R.drawable.clouds_p);
            //ForecastAdapter.ViewHolder.backgroundIMgFirtItem.setBackgroundResource(R.drawable.clouds_p);
            return R.drawable.art_clouds;
        }
        return -1;
    }

}