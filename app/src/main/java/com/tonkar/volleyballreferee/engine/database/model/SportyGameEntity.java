package com.tonkar.volleyballreferee.engine.database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "sporty_games")
@Getter @Setter
public class SportyGameEntity {

    public SportyGameEntity (String cve, String content) {
        this.cve = cve;
        this.content = content;
    }

    @PrimaryKey
    @NonNull
    private String cve;
    private String content;

}
