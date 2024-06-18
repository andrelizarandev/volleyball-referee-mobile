package com.tonkar.volleyballreferee.engine.database;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tonkar.volleyballreferee.engine.Tags;
import com.tonkar.volleyballreferee.engine.api.JsonConverters;
import com.tonkar.volleyballreferee.engine.api.model.ApiFriend;
import com.tonkar.volleyballreferee.engine.api.model.ApiGame;
import com.tonkar.volleyballreferee.engine.api.model.ApiGameSummary;
import com.tonkar.volleyballreferee.engine.api.model.ApiLeague;
import com.tonkar.volleyballreferee.engine.api.model.ApiLeagueSummary;
import com.tonkar.volleyballreferee.engine.api.model.ApiRules;
import com.tonkar.volleyballreferee.engine.api.model.ApiRulesSummary;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyDate;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyPostResponseFilterGames;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;
import com.tonkar.volleyballreferee.engine.api.model.ApiTeam;
import com.tonkar.volleyballreferee.engine.api.model.ApiTeamSummary;
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
import com.tonkar.volleyballreferee.engine.game.BaseGame;
import com.tonkar.volleyballreferee.engine.game.GameType;
import com.tonkar.volleyballreferee.engine.game.IGame;
import com.tonkar.volleyballreferee.engine.service.IStoredGame;
import com.tonkar.volleyballreferee.engine.service.StoredGame;
import com.tonkar.volleyballreferee.engine.team.GenderType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VbrRepository {

    private static final String sCurrentGame = "current";
    private static final String sSetupGame   = "setup";

    private final FriendDao      mFriendDao;
    private final FullGameDao    mFullGameDao;
    private final GameDao        mGameDao;
    private final LeagueDao      mLeagueDao;
    private final RulesDao       mRulesDao;
    private final TeamDao        mTeamDao;
    private final SportyCourtDao mSportyCourtDao;
    private final SportyDateDao  mSportyDateDao;
    private final SportyStateDao mSportyStateDao;
    private final SportyGameDao  mSportyGameDao;
    private final SportyTokenDao mSportyTokenDao;

    public VbrRepository (Context context) {
        VbrDatabase db = VbrDatabase.getInstance(context);
        mFriendDao = db.friendDao();
        mFullGameDao = db.fullGameDao();
        mGameDao = db.gameDao();
        mLeagueDao = db.leagueDao();
        mRulesDao = db.rulesDao();
        mTeamDao = db.teamDao();
        mSportyCourtDao = db.sportyCourtDao();
        mSportyDateDao = db.sportyDateDao();
        mSportyStateDao = db.sportyStateDao();
        mSportyGameDao = db.sportyGameDao();
        mSportyTokenDao = db.sportyTokenDao();
    }

    public void insertFriend(final String friendId, final String friendPseudo, boolean syncInsertion) {
        Runnable runnable = () -> {
            FriendEntity friendEntity = new FriendEntity();
            friendEntity.setId(friendId);
            friendEntity.setPseudo(friendPseudo);
            mFriendDao.insert(friendEntity);
        };

        if (syncInsertion) {
            runnable.run();
        } else {
            VbrDatabase.sDatabaseWriteExecutor.execute(runnable);
        }
    }

    public void removeFriend (final String friendId) {
        VbrDatabase.sDatabaseWriteExecutor.execute(() -> mFriendDao.deleteById(friendId));
    }

    public void insertFriends (final List<ApiFriend> friends, boolean syncInsertion) {
        Runnable runnable = () -> {
            mFriendDao.deleteAll();
            List<FriendEntity> friendEntities = new ArrayList<>();
            for (ApiFriend friend : friends) {
                FriendEntity friendEntity = new FriendEntity();
                friendEntity.setId(friend.getId());
                friendEntity.setPseudo(friend.getPseudo());
                friendEntities.add(friendEntity);
            }
            mFriendDao.insertAll(friendEntities);
        };

        if (syncInsertion) {
            runnable.run();
        } else {
            VbrDatabase.sDatabaseWriteExecutor.execute(runnable);
        }
    }

    public List<ApiLeagueSummary> listLeagues() {
        return mLeagueDao.listLeagues();
    }

    public List<ApiLeagueSummary> listLeagues(GameType kind) {
        return mLeagueDao.listLeaguesByKind(kind);
    }

    public ApiLeague getLeague(String id) {
        String json = mLeagueDao.findContentById(id);
        return JsonConverters.GSON.fromJson(json, ApiLeague.class);
    }

    public int countLeagues(String name, GameType kind) {
        return mLeagueDao.countByNameAndKind(name, kind);
    }

    public int countLeagues(String id) {
        return mLeagueDao.countById(id);
    }

    public ApiLeague getLeague(String name, GameType kind) {
        String json = mLeagueDao.findContentByNameAndKind(name, kind);
        return JsonConverters.GSON.fromJson(json, ApiLeague.class);
    }

    public void insertLeague(final ApiLeague league, boolean synced, boolean syncInsertion) {
        Runnable runnable = () -> {
            String json = JsonConverters.GSON.toJson(league, ApiLeague.class);
            LeagueEntity leagueEntity = new LeagueEntity();
            leagueEntity.setId(league.getId());
            leagueEntity.setCreatedBy(league.getCreatedBy());
            leagueEntity.setCreatedAt(league.getCreatedAt());
            leagueEntity.setUpdatedAt(league.getUpdatedAt());
            leagueEntity.setKind(league.getKind());
            leagueEntity.setName(league.getName());
            leagueEntity.setSynced(synced);
            leagueEntity.setContent(json);
            mLeagueDao.insert(leagueEntity);
        };

        if (syncInsertion) {
            runnable.run();
        } else {
            VbrDatabase.sDatabaseWriteExecutor.execute(runnable);
        }
    }

    public void deleteLeague(final String id) {
        VbrDatabase.sDatabaseWriteExecutor.execute(() -> mLeagueDao.deleteById(id));
    }

    public List<ApiRulesSummary> listRules() {
        return mRulesDao.listRules();
    }

    public List<ApiRulesSummary> listRules(GameType kind) {
        return mRulesDao.listRulesByKind(kind);
    }

    public ApiRules getRules(String id) {
        String json = mRulesDao.findContentById(id);
        return JsonConverters.GSON.fromJson(json, ApiRules.class);
    }

    public ApiRules getRules(String name, GameType kind) {
        String json = mRulesDao.findContentByNameAndKind(name, kind);
        return JsonConverters.GSON.fromJson(json, ApiRules.class);
    }

    public void insertRules(final ApiRules rules, boolean synced, boolean syncInsertion) {
        Runnable runnable = () -> {
            String json = JsonConverters.GSON.toJson(rules);
            RulesEntity rulesEntity = new RulesEntity();
            rulesEntity.setId(rules.getId());
            rulesEntity.setCreatedBy(rules.getCreatedBy());
            rulesEntity.setCreatedAt(rules.getCreatedAt());
            rulesEntity.setUpdatedAt(rules.getUpdatedAt());
            rulesEntity.setKind(rules.getKind());
            rulesEntity.setName(rules.getName());
            rulesEntity.setSynced(synced);
            rulesEntity.setContent(json);
            mRulesDao.insert(rulesEntity);
        };

        if (syncInsertion) {
            runnable.run();
        } else {
            VbrDatabase.sDatabaseWriteExecutor.execute(runnable);
        }
    }

    public void deleteRules(String id) {
        VbrDatabase.sDatabaseWriteExecutor.execute(() -> mRulesDao.deleteById(id));
    }

    public void deleteRules(Set<String> ids) {
        VbrDatabase.sDatabaseWriteExecutor.execute(() -> mRulesDao.deleteByIdIn(ids));
    }

    public int countRules(String id) {
        return mRulesDao.countById(id);
    }

    public int countRules(String name, GameType kind) {
        return mRulesDao.countByNameAndKind(name, kind);
    }

    public List<ApiTeamSummary> listTeams() {
        return mTeamDao.listTeams();
    }

    public List<ApiTeamSummary> listTeams(GameType kind) {
        return mTeamDao.listTeamsByKind(kind);
    }

    public List<ApiTeamSummary> listTeams(GenderType genderType, GameType kind) {
        return mTeamDao.listTeamsByGenderAndKind(genderType, kind);
    }

    public ApiTeam getTeam(String id) {
        String json = mTeamDao.findContentById(id);
        return JsonConverters.GSON.fromJson(json, ApiTeam.class);
    }

    public ApiTeam getTeam(String name, GenderType genderType, GameType kind) {
        String json = mTeamDao.findContentByNameAndGenderAndKind(name, genderType, kind);
        return JsonConverters.GSON.fromJson(json, ApiTeam.class);
    }

    public void insertTeam(final ApiTeam team, boolean synced, boolean syncInsertion) {
        Runnable runnable = () -> {
            String json = JsonConverters.GSON.toJson(team, ApiTeam.class);
            TeamEntity teamEntity = new TeamEntity();
            teamEntity.setId(team.getId());
            teamEntity.setCreatedBy(team.getCreatedBy());
            teamEntity.setCreatedAt(team.getCreatedAt());
            teamEntity.setUpdatedAt(team.getUpdatedAt());
            teamEntity.setKind(team.getKind());
            teamEntity.setGender(team.getGender());
            teamEntity.setName(team.getName());
            teamEntity.setSynced(synced);
            teamEntity.setContent(json);
            mTeamDao.insert(teamEntity);
        };

        if (syncInsertion) {
            runnable.run();
        } else {
            VbrDatabase.sDatabaseWriteExecutor.execute(runnable);
        }
    }

    public void deleteTeam(String id) {
        VbrDatabase.sDatabaseWriteExecutor.execute(() -> mTeamDao.deleteById(id));
    }

    public void deleteTeams(Set<String> ids) {
        VbrDatabase.sDatabaseWriteExecutor.execute(() -> mTeamDao.deleteByIdIn(ids));
    }

    public int countTeams(String name, GenderType genderType, GameType kind) {
        return mTeamDao.countByNameAndGenderAndKind(name, genderType, kind);
    }

    public int countTeams(String id) {
        return mTeamDao.countById(id);
    }

    public List<ApiGameSummary> listGames() {
        return mGameDao.listGames();
    }

    public IStoredGame getGame(String id) {
        String json = mGameDao.findContentById(id);
        return JsonConverters.GSON.fromJson(json, StoredGame.class);
    }

    public boolean isGameIndexed(String id) {
        return mGameDao.isGameIndexed(id);
    }

    private String getCurrentSportyGame () {
        List<SportyGameEntity> sportyGames = this.getRunningSportyGame();
        return (!sportyGames.isEmpty()) ? sportyGames.get(0).getCve() : "null";
    }

    public void insertGame (final ApiGame game, boolean synced, boolean syncInsertion) {

        String result = getCurrentSportyGame();

        Runnable runnable = () -> {
            game.setScore(game.buildScore());
            GameEntity gameEntity = new GameEntity();
            gameEntity.setCve(result);
            gameEntity.setId(game.getId());
            gameEntity.setCreatedBy(game.getCreatedBy());
            gameEntity.setCreatedAt(game.getCreatedAt());
            gameEntity.setUpdatedAt(game.getUpdatedAt());
            gameEntity.setScheduledAt(game.getScheduledAt());
            gameEntity.setRefereedBy(game.getRefereedBy());
            gameEntity.setRefereeName(game.getRefereeName());
            gameEntity.setReferee1Name(game.getReferee1Name());
            gameEntity.setReferee2Name(game.getReferee2Name());
            gameEntity.setScorerName(game.getScorerName());
            gameEntity.setKind(game.getKind());
            gameEntity.setGender(game.getGender());
            gameEntity.setUsage(game.getUsage());
            gameEntity.setSynced(synced);
            gameEntity.setIndexed(game.isIndexed());
            if (game.getLeague() == null) {
                gameEntity.setLeagueName("");
                gameEntity.setDivisionName("");
            } else {
                gameEntity.setLeagueName(game.getLeague().getName());
                gameEntity.setDivisionName(game.getLeague().getDivision());
            }
            gameEntity.setHomeTeamName(game.getHomeTeam().getName());
            gameEntity.setGuestTeamName(game.getGuestTeam().getName());
            gameEntity.setHomeSets(game.getHomeSets());
            gameEntity.setGuestSets(game.getGuestSets());
            gameEntity.setScore(game.getScore());
            gameEntity.setContent(JsonConverters.GSON.toJson(game, ApiGame.class));
            mGameDao.insert(gameEntity);
        };

        if (syncInsertion) {
            runnable.run();
        } else {
            VbrDatabase.sDatabaseWriteExecutor.execute(runnable);
        }
    }

    public void deleteGame(String id) {
        VbrDatabase.sDatabaseWriteExecutor.execute(() -> mGameDao.deleteById(id));
    }

    public void deleteGames(Set<String> ids) {
        VbrDatabase.sDatabaseWriteExecutor.execute(() -> mGameDao.deleteByIdIn(ids));
    }

    private boolean hasFullGameGame(String type) {
        return mFullGameDao.countByType(type) > 0;
    }

    private IGame getFullGame(String type) {
        String json = mFullGameDao.findContentByType(type);
        return JsonConverters.GSON.fromJson(json, new TypeToken<BaseGame>(){}.getType());
    }

    private void insertFullGame(String type, IGame game, boolean syncInsertion) {
        String json = JsonConverters.GSON.toJson(game, new TypeToken<BaseGame>(){}.getType());
        final FullGameEntity fullGameEntity = new FullGameEntity(type, json);

        if (syncInsertion) {
            mFullGameDao.insert(fullGameEntity);
        } else {
            VbrDatabase.sDatabaseWriteExecutor.execute(() -> mFullGameDao.insert(fullGameEntity));
        }
    }

    private void deleteFullGame(String type) {
        VbrDatabase.sDatabaseWriteExecutor.execute(() -> mFullGameDao.deleteByType(type));
    }

    public boolean hasCurrentGame() {
        return hasFullGameGame(sCurrentGame);
    }

    public IGame getCurrentGame() {
        return getFullGame(sCurrentGame);
    }

    public void insertCurrentGame(IGame game, boolean syncInsertion) {
        insertFullGame(sCurrentGame, game, syncInsertion);
    }

    public void deleteCurrentGame() {
        deleteFullGame(sCurrentGame);
    }

    public boolean hasSetupGame() {
        return hasFullGameGame(sSetupGame);
    }

    public IGame getSetupGame() {
        return getFullGame(sSetupGame);
    }

    public void insertSetupGame(IGame game, boolean syncInsertion) {
        insertFullGame(sSetupGame, game, syncInsertion);
    }

    public void deleteSetupGame() {
        deleteFullGame(sSetupGame);
    }

    // Sporty games
    public List<SportyGameEntity> listSportyGames () {
        return mSportyGameDao.gameList();
    }

    public List<SportyGameEntity> getRunningSportyGame () {
        return mSportyGameDao.getRunningGame();
    }

    public ApiSportyPostResponseFilterGames.JuegosData getSportyGameByIndex (int index) {
        List<SportyGameEntity> gameList = this.listSportyGames();
        SportyGameEntity selectedGame = gameList.get(index);
        return new Gson().fromJson(selectedGame.getContent(), ApiSportyPostResponseFilterGames.JuegosData.class);
    }

    public void insertSportyGame (SportyGameEntity game) {
        mSportyGameDao.insert(game);
    }

    public void deleteAllSportyGames () {
        mSportyGameDao.deleteAll();
    }

    public void setAsRunningSportyGame (String cve) {
        mSportyGameDao.setIsRunning(cve);
    }

    public void setAllAsNotRunningSportyGame () {
        mSportyGameDao.setAllIsNotRunning();
    }

    // Sporty courts
    public List<ApiSportyValidateCode.CanchaData> listCourts () {
        return mSportyCourtDao.courtList();
    }

    public void insertAllCourts (List<ApiSportyValidateCode.CanchaData> courtlist) {
        List<SportyCourtEntity> parsedList = new ArrayList<>();
        for (ApiSportyValidateCode.CanchaData canchaData : courtlist) {
            SportyCourtEntity courtEntity = new SportyCourtEntity(canchaData.getCve(), canchaData.getNombre(), canchaData.getDeporte());
            parsedList.add(courtEntity);
        }
        mSportyCourtDao.insertAll(parsedList);
    }

    public void deleteAllCourts () {
        mSportyCourtDao.deleteAll();
    }

    // Sporty dates
    public List<ApiSportyDate> listDates () {
        return mSportyDateDao.dateList();
    }

    public void insertAllDates (List<ApiSportyDate> dateList) {
        List<SportyDateEntity> parsedList = new ArrayList<>();
        for (ApiSportyDate sportyDate : dateList) {
            SportyDateEntity dateEntity = new SportyDateEntity(sportyDate.getId(), sportyDate.getDate());
            parsedList.add(dateEntity);
        }
        mSportyDateDao.insertAll(parsedList);
    }

    public void deleteAllDates () {
        mSportyDateDao.deleteAll();
    }

    // Sporty states
    public List <ApiSportyValidateCode.EstadoData> listStates () {
        return mSportyStateDao.stateList();
    }

    public void insertAllStates (List<ApiSportyValidateCode.EstadoData> stateList) {
        List<SportyStateEntity> parsedList = new ArrayList<>();
        for (ApiSportyValidateCode.EstadoData state : stateList) {
            SportyStateEntity stateEntity = new SportyStateEntity(state.getCve(), state.getTitle());
            parsedList.add(stateEntity);
        }
        mSportyStateDao.insertAll(parsedList);
    }

    public void deleteAllStates () {
        mSportyStateDao.deleteAll();
    }

    // Sporty tokens
    public void insertSportyToken (SportyTokenEntity data) {
        mSportyTokenDao.insert(data);
    }

    public List<SportyTokenEntity> getSportyTokenList() {
       return mSportyTokenDao.tokenList();
    }

    public void deleteSportyToken () {
        mSportyTokenDao.deleteAll();
    }

}
