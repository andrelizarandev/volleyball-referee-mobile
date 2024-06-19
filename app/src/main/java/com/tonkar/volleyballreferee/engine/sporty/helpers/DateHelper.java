package com.tonkar.volleyballreferee.engine.sporty.helpers;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class DateHelper {

    public static ArrayList<ApiSportyDate> getDatesInBetween (String initDate, String endDate) {

        ArrayList<ApiSportyDate> dates = new ArrayList<>();

        ArrayList<ApiSportyDate> parsedDates = new ArrayList<>();

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

        }   catch (ParseException e) {

            e.printStackTrace();

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            for (ApiSportyDate row : dates) {

                String formattedDate = formatDateFromYMDtoDMY(row.date);

                ApiSportyDate newRow = new ApiSportyDate(row.id, formattedDate);

                parsedDates.add(newRow);

            }

        }

        return !parsedDates.isEmpty() ? parsedDates : dates;

    }

    public static String formatDateFromYMDtoDMY (String date) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate parsedDate = LocalDate.parse(date, inputFormatter);

            return parsedDate.format(outputFormatter);

        }

        return date;

    }


    public static String formatDateFromDMYtoYMD (String date) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate parsedDate = LocalDate.parse(date, inputFormatter);

            return parsedDate.format(outputFormatter);

        }

        return date;

    }

}
