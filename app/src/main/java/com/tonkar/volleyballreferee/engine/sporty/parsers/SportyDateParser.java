package com.tonkar.volleyballreferee.engine.sporty.parsers;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyDate;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;

import java.util.List;

public class SportyDateParser {

    public static String[] parseDateDataToString (List<ApiSportyDate> dateList) {
        String[] dateData = new String[dateList.size()];
        for (int i = 0; i < dateList.size(); i++) {
            dateData[i] = dateList.get(i).getDate();
        }
        return dateData;
    }

    public static String[] getIndexesFromDateList (List<ApiSportyDate> dateList) {
        String[] dateData = new String[dateList.size()];
        for (int i = 0; i < dateList.size(); i++) {
            dateData[i] = String.valueOf(i);
        }
        return dateData;
    }

}
