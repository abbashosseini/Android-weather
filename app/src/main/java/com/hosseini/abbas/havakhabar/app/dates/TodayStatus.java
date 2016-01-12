package com.hosseini.abbas.havakhabar.app.dates;

/**
 * Created by root on 5/12/15.
 */
public class TodayStatus {


    public TodayStatus(){}

    public String TodayStatus(String GetDate){

        String DT = GetDate;
        String[] Spt = DT.split("/");
        String YEAR_FOR_STATUS = Spt[0];
        String MONTH_FOR_STATUS = Spt[1];
        String Display_Month_on_Status = null;
        String DAY_FOR_STATUS = Spt[2];

        switch (Integer.parseInt(MONTH_FOR_STATUS)) {
            case 1:
                    Display_Month_on_Status = "فروردین";
                break;
            case 2:
                    Display_Month_on_Status = "ارديبهشت";
                break;
            case 3:
                    Display_Month_on_Status = "خرداد";
                break;
            case 4:
                    Display_Month_on_Status = "تير";

                break;
            case 5:
                    Display_Month_on_Status = "مرداد";

                break;
            case 6:
                    Display_Month_on_Status = "شهريور";
                break;
            case 7:
                    Display_Month_on_Status = "مهر";
                break;
            case 8:
                    Display_Month_on_Status = "آبان";
                break;
            case 9:
                    Display_Month_on_Status = "آذر";
                break;
            case 10:
                    Display_Month_on_Status = "دي";
                break;
            case 11:
                    Display_Month_on_Status = "بهمن";
                break;
            case 12:
                    Display_Month_on_Status = "اسفند";
                break;
        }

        return DAY_FOR_STATUS + " " +Display_Month_on_Status + " " + YEAR_FOR_STATUS;

    }
}
