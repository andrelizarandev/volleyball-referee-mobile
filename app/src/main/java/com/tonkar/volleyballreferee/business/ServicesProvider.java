package com.tonkar.volleyballreferee.business;

import android.content.Context;

import com.tonkar.volleyballreferee.business.history.GamesHistory;
import com.tonkar.volleyballreferee.interfaces.GameService;
import com.tonkar.volleyballreferee.interfaces.GamesHistoryService;
import com.tonkar.volleyballreferee.interfaces.ScoreService;
import com.tonkar.volleyballreferee.interfaces.TeamService;
import com.tonkar.volleyballreferee.interfaces.TimeoutService;

public class ServicesProvider {

    private static ServicesProvider sServicesProvider;
    private        GameService      mCurrentGame;
    private        GamesHistory     mCurrentGamesHistory;

    private ServicesProvider() {}

    public static ServicesProvider getInstance() {
        if (sServicesProvider == null) {
            sServicesProvider = new ServicesProvider();
        }

        return sServicesProvider;
    }

    public GameService getGameService() { return mCurrentGame; }

    public ScoreService getScoreService() {
        return mCurrentGame;
    }

    public TeamService getTeamService() {
        return mCurrentGame;
    }

    public TimeoutService getTimeoutService() {
        return mCurrentGame;
    }

    public GamesHistoryService getGamesHistoryService() {
        return mCurrentGamesHistory;
    }

    private boolean isGameServiceAvailable() {
        return mCurrentGame != null;
    }

    private boolean isGamesHistoryServiceAvailable() {
        return mCurrentGamesHistory != null;
    }

    public boolean areServicesAvailable() {
        return isGameServiceAvailable() && isGamesHistoryServiceAvailable();
    }

    public void initGameService(GameService gameService) {
        mCurrentGame = gameService;
    }

    public void restoreGameService(Context context) {
        restoreGamesHistoryService(context);
        if (mCurrentGamesHistory.hasCurrentGame()) {
            initGameService(mCurrentGamesHistory.loadCurrentGame());
        }
    }

    public void restoreGameServiceForSetup(Context context) {
        restoreGamesHistoryService(context);
        if (mCurrentGamesHistory.hasSetupGame()) {
            initGameService(mCurrentGamesHistory.loadSetupGame());
        }
    }

    public void restoreGamesHistoryService(Context context) {
        if (!isGamesHistoryServiceAvailable()) {
            mCurrentGamesHistory = new GamesHistory(context);
            mCurrentGamesHistory.loadRecordedGames();
        }
    }
}
