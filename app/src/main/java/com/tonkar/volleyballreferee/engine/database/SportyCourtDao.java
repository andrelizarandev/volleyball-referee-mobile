package com.tonkar.volleyballreferee.engine.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;
import com.tonkar.volleyballreferee.engine.database.model.SportyCourtEntity;

import java.util.List;

@Dao
public interface SportyCourtDao {

    @Query("SELECT * FROM sporty_courts")
    List<ApiSportyValidateCode.CanchaData> courtList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<SportyCourtEntity> courtList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SportyCourtEntity courtEntity);

    @Query("DELETE FROM sporty_courts")
    void deleteAll();

    @Query("DELETE FROM sporty_courts WHERE cve = :cve")
    void deleteById(String cve);

    @Query("SELECT COUNT(*) FROM sporty_courts")
    int count();

}
