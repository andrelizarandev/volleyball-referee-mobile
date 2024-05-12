package com.tonkar.volleyballreferee.engine.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyDate;
import com.tonkar.volleyballreferee.engine.database.model.SportyDateEntity;

import java.util.List;

@Dao
public interface SportyDateDao {

    @Query("SELECT * FROM sporty_dates")
    List<ApiSportyDate> dateList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SportyDateEntity> dateList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SportyDateEntity dateEntity);

    @Query("DELETE FROM sporty_dates")
    void deleteAll();

    @Query("DELETE FROM sporty_dates WHERE id = :id")
    void deleteById(String id);

    @Query("SELECT COUNT(*) FROM sporty_dates")
    int count();

}
