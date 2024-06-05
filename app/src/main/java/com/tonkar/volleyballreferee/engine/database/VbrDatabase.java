package com.tonkar.volleyballreferee.engine.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tonkar.volleyballreferee.engine.database.model.FriendEntity;
import com.tonkar.volleyballreferee.engine.database.model.FullGameEntity;
import com.tonkar.volleyballreferee.engine.database.model.GameEntity;
import com.tonkar.volleyballreferee.engine.database.model.LeagueEntity;
import com.tonkar.volleyballreferee.engine.database.model.RulesEntity;
import com.tonkar.volleyballreferee.engine.database.model.SportyCourtEntity;
import com.tonkar.volleyballreferee.engine.database.model.SportyDateEntity;
import com.tonkar.volleyballreferee.engine.database.model.SportyGameEntity;
import com.tonkar.volleyballreferee.engine.database.model.SportyStateEntity;
import com.tonkar.volleyballreferee.engine.database.model.SportyTokenEntity;
import com.tonkar.volleyballreferee.engine.database.model.TeamEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                RulesEntity.class,
                TeamEntity.class,
                GameEntity.class,
                FullGameEntity.class,
                LeagueEntity.class,
                FriendEntity.class,
                SportyCourtEntity.class,
                SportyDateEntity.class,
                SportyStateEntity.class,
                SportyGameEntity.class,
                SportyTokenEntity.class
        }, version = 7)

@TypeConverters({DatabaseConverters.class})

public abstract class VbrDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;

    private static VbrDatabase sInstance = null;

    static final ExecutorService sDatabaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static VbrDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (VbrDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context, VbrDatabase.class, "vbr-db")
                            .fallbackToDestructiveMigration()
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .addMigrations(MIGRATION_5_6)
                            .addMigrations(MIGRATION_6_7)
                            .allowMainThreadQueries()
                            .build();
                }
            }
            sInstance = Room.databaseBuilder(context, VbrDatabase.class, "vbr-db")
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .addMigrations(MIGRATION_3_4)
                    .addMigrations(MIGRATION_4_5)
                    .addMigrations(MIGRATION_5_6)
                    .addMigrations(MIGRATION_6_7)
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    public abstract RulesDao rulesDao();

    public abstract TeamDao teamDao();

    public abstract GameDao gameDao();

    public abstract FullGameDao fullGameDao();

    public abstract LeagueDao leagueDao();

    public abstract FriendDao friendDao();

    public abstract SportyCourtDao sportyCourtDao();

    public abstract SportyDateDao sportyDateDao();

    public abstract SportyStateDao sportyStateDao();

    public abstract SportyGameDao sportyGameDao();

    public abstract SportyTokenDao sportyTokenDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `syncs` (`item` TEXT NOT NULL, `type` TEXT NOT NULL, PRIMARY KEY(`item`))");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS `syncs`");
            database.execSQL("DROP TABLE IF EXISTS `full_games`");
            database.execSQL("DROP TABLE IF EXISTS `games`");
            database.execSQL("DROP TABLE IF EXISTS `rules`");
            database.execSQL("DROP TABLE IF EXISTS `teams`");
            database.execSQL("DROP TABLE IF EXISTS `syncs`");

            database.execSQL("CREATE TABLE `friends` (`id` TEXT NOT NULL, `pseudo` TEXT NOT NULL, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE `leagues` (`id` TEXT NOT NULL, `createdBy` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `synced` INTEGER NOT NULL, `name` TEXT NOT NULL, `kind` TEXT NOT NULL, `content` TEXT NOT NULL, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE `full_games` (`type` TEXT NOT NULL, `content` TEXT NOT NULL, PRIMARY KEY(`type`))");
            database.execSQL("CREATE TABLE `games` (`id` TEXT NOT NULL, `createdBy` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `scheduledAt` INTEGER NOT NULL, `synced` INTEGER NOT NULL, `kind` TEXT NOT NULL, `gender` TEXT NOT NULL, `usage` TEXT NOT NULL, `public` INTEGER NOT NULL, `leagueName` TEXT, `divisionName` TEXT, `homeTeamName` TEXT NOT NULL, `guestTeamName` TEXT NOT NULL, `homeSets` INTEGER NOT NULL, `guestSets` INTEGER NOT NULL, `score` TEXT NOT NULL, `content` TEXT NOT NULL, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE `teams` (`id` TEXT NOT NULL, `createdBy` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `synced` INTEGER NOT NULL, `name` TEXT NOT NULL, `kind` TEXT NOT NULL, `gender` TEXT NOT NULL, `content` TEXT NOT NULL, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE `rules` (`id` TEXT NOT NULL, `createdBy` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `synced` INTEGER NOT NULL, `name` TEXT NOT NULL, `kind` TEXT NOT NULL, `content` TEXT NOT NULL, PRIMARY KEY(`id`))");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Sporty
            database.execSQL("CREATE TABLE `sporty_courts` (`cve` TEXT NOT NULL, `deporte` TEXT NOT NULL, `nombre` TEXT NOT NULL, PRIMARY KEY(`cve`))");
            database.execSQL("CREATE TABLE `sporty_dates` (`id` INTEGER NOT NULL, `date` TEXT NOT NULL, PRIMARY KEY(`id`))");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Sporty
            database.execSQL("CREATE TABLE `sporty_states` (`cve` TEXT NOT NULL, `title` TEXT NOT NULL, PRIMARY KEY(`cve`))");
        }
    };

    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Sporty
            database.execSQL("CREATE TABLE `sporty_tokens` (`token` TEXT NOT NULL, PRIMARY KEY(`token`))");
        }
    };

    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Sporty
            database.execSQL("CREATE TABLE IF NOT EXISTS `sporty_games` (`content` TEXT NOT NULL, `cve` TEXT NOT NULL, `isRunning` INTEGER NOT NULL, PRIMARY KEY(`cve`))");
        }
    };

}