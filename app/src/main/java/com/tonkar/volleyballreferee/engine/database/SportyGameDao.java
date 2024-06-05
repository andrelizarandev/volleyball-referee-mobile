package com.tonkar.volleyballreferee.engine.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tonkar.volleyballreferee.engine.database.model.SportyGameEntity;

import java.util.List;

@Dao
public interface SportyGameDao {

    @Query("SELECT * FROM sporty_games")
    List<SportyGameEntity> gameList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SportyGameEntity> gameList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SportyGameEntity gameEntity);

    @Query("DELETE FROM sporty_games")
    void deleteAll();

    @Query("DELETE FROM sporty_games WHERE cve = :cve")
    void deleteById(String cve);

    @Query("SELECT COUNT(*) FROM sporty_games")
    int count();

    @Query("UPDATE sporty_games SET isRunning = 1 WHERE cve = :cve")
    void setIsRunning(String cve);

    @Query("UPDATE sporty_games SET isRunning = 0")
    void setAllIsNotRunning();

    @Query("SELECT * FROM sporty_games WHERE isRunning = 1")
    List<SportyGameEntity> getRunningGame();

}
