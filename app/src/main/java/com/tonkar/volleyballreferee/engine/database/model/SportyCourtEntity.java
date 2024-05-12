package com.tonkar.volleyballreferee.engine.database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "sporty_courts")
@Getter @Setter
public class SportyCourtEntity extends ApiSportyValidateCode.CanchaData {

    public SportyCourtEntity(@NonNull String cve, @NonNull String nombre, @NonNull String deporte) {
        super(cve, nombre, deporte);
    }

}
