package com.tonkar.volleyballreferee.engine.database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyDate;

@Entity(tableName = "sporty_dates")
public class SportyDateEntity extends ApiSportyDate {

    public SportyDateEntity(int id, @NonNull String date) {
        super(id, date);
    }
}
