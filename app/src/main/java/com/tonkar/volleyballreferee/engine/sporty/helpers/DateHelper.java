package com.tonkar.volleyballreferee.engine.sporty.helpers;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateHelper {

    public static ArrayList<ApiSportyDate> getDatesInBetween (String initDate, String endDate) {
        ArrayList<ApiSportyDate> dates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(initDate);
            Date date2 = sdf.parse(endDate);
            long diff = date2.getTime() - date1.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            for (int i = 0; i <= diffDays; i++) {
                String date = sdf.format(new Date(date1.getTime() + (i * 24 * 60 * 60 * 1000)));
                ApiSportyDate apiSportyDate = new ApiSportyDate(i, date);
                dates.add(apiSportyDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }

}
