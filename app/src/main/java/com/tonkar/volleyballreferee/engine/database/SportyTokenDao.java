package com.tonkar.volleyballreferee.engine.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tonkar.volleyballreferee.engine.database.model.SportyTokenEntity;

import java.util.List;

@Dao
public interface SportyTokenDao {

    @Query("SELECT * FROM sporty_tokens")
    List<SportyTokenEntity> tokenList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SportyTokenEntity> tokenList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SportyTokenEntity tokenEntity);

    @Query("DELETE FROM sporty_tokens")
    void deleteAll();

    @Query("DELETE FROM sporty_tokens WHERE token = :token")
    void deleteById(String token);

    @Query("SELECT COUNT(*) FROM sporty_tokens")
    int count();

}
