

package com.tonkar.volleyballreferee.engine.database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "sporty_states")
@Getter
@Setter
public class SportyStateEntity extends ApiSportyValidateCode.EstadoData {

    public SportyStateEntity(@NonNull String cve, @NonNull String title) {
        super(cve, title);
    }

}