package com.tonkar.volleyballreferee.engine.database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "sporty_tokens")
@Getter
@Setter
public class SportyTokenEntity {

    @PrimaryKey
    @NonNull
    public String token;

    public SportyTokenEntity (@NonNull String token) {
        this.token = token;
    }

}
