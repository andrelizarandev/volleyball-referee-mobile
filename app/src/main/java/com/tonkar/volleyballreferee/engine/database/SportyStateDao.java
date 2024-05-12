package com.tonkar.volleyballreferee.engine.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;
import com.tonkar.volleyballreferee.engine.database.model.SportyStateEntity;

import java.util.List;

@Dao
public interface SportyStateDao {

    @Query("SELECT * FROM sporty_states")
    List<ApiSportyValidateCode.EstadoData> stateList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SportyStateEntity> stateList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SportyStateEntity stateEntity);

    @Query("DELETE FROM sporty_states")
    void deleteAll();

    @Query("DELETE FROM sporty_states WHERE cve = :cve")
    void deleteById(String cve);

    @Query("SELECT COUNT(*) FROM sporty_states")
    int count();

}
