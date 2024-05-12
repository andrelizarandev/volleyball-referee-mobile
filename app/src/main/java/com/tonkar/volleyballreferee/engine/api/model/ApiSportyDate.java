package com.tonkar.volleyballreferee.engine.api.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiSportyDate {
    @PrimaryKey
    private int id;
    @NonNull
    private String date;
}
