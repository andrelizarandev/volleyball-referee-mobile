package com.tonkar.volleyballreferee.engine.sporty.parsers;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;
import java.util.List;

public class SportyCourseParser {

    public static String[] parseCourtDataToString (List<ApiSportyValidateCode.CanchaData> courtList) {
        String[] courtData = new String[courtList.size()];
        for (int i = 0; i < courtList.size(); i++) {
            courtData[i] = courtList.get(i).getNombre();
        }
        return courtData;
    }

    public static String[] getIndexesFromCourList (List<ApiSportyValidateCode.CanchaData> courtList) {
        String[] courtData = new String[courtList.size()];
        for (int i = 0; i < courtList.size(); i++) {
            courtData[i] = String.valueOf(i);
        }
        return courtData;
    }


}
