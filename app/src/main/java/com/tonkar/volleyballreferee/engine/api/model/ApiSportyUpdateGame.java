package com.tonkar.volleyballreferee.engine.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ApiSportyUpdateGame {

    @NonNull
    private String cve;
    private String content;

}
