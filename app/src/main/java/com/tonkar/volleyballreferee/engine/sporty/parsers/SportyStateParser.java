package com.tonkar.volleyballreferee.engine.sporty.parsers;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;

import java.util.List;

public class SportyStateParser {

    public static String[] parseStateDataToString (List<ApiSportyValidateCode.EstadoData> stateList) {
        String[] stateData = new String[stateList.size()];
        for (int i = 0; i < stateList.size(); i++) {
            stateData[i] = stateList.get(i).getTitle();
        }
        return stateData;
    }

    public static String[] getIndexesFromStateList (List<ApiSportyValidateCode.EstadoData> stateList) {
        String[] stateData = new String[stateList.size()];
        for (int i = 0; i < stateList.size(); i++) {
            stateData[i] = String.valueOf(i);
        }
        return stateData;
    }


}
