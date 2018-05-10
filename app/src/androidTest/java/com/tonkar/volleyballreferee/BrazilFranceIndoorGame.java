package com.tonkar.volleyballreferee;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.tonkar.volleyballreferee.business.ServicesProvider;
import com.tonkar.volleyballreferee.business.game.GameFactory;
import com.tonkar.volleyballreferee.business.game.IndoorGame;
import com.tonkar.volleyballreferee.business.data.PdfGameWriter;
import com.tonkar.volleyballreferee.interfaces.ActionOriginType;
import com.tonkar.volleyballreferee.interfaces.GameService;
import com.tonkar.volleyballreferee.interfaces.data.UserId;
import com.tonkar.volleyballreferee.interfaces.team.GenderType;
import com.tonkar.volleyballreferee.interfaces.team.PositionType;
import com.tonkar.volleyballreferee.interfaces.data.RecordedGameService;
import com.tonkar.volleyballreferee.interfaces.team.TeamType;
import com.tonkar.volleyballreferee.rules.Rules;
import com.tonkar.volleyballreferee.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BrazilFranceIndoorGame {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void playGame_complete() {
        IndoorGame indoorGame = GameFactory.createIndoorGame("VBR", UserId.VBR_USER_ID);

        defineTeams(indoorGame);

        composeTeamsSet1(indoorGame);
        playSet1_complete(indoorGame);

        composeTeamsSet2(indoorGame);
        playSet2_complete(indoorGame);

        composeTeamsSet3(indoorGame);
        playSet3_complete(indoorGame);

        composeTeamsSet4(indoorGame);
        playSet4_complete(indoorGame);

        composeTeamsSet5(indoorGame);
        playSet5_complete(indoorGame);

        RecordedGameService recordedGameService = ServicesProvider.getInstance().getRecordedGamesService().getRecordedGameService(indoorGame.getGameDate());
        PdfGameWriter.writeRecordedGame(mActivityRule.getActivity(), recordedGameService);
    }

    @Test
    public void playGame_lastSetEnd() {
        IndoorGame indoorGame = GameFactory.createIndoorGame("VBR", UserId.VBR_USER_ID);

        defineTeams(indoorGame);

        composeTeamsSet1(indoorGame);
        playSet1_complete(indoorGame);

        composeTeamsSet2(indoorGame);
        playSet2_complete(indoorGame);

        composeTeamsSet3(indoorGame);
        playSet3_complete(indoorGame);

        composeTeamsSet4(indoorGame);
        playSet4_complete(indoorGame);

        composeTeamsSet5(indoorGame);
        playSet5_lastSetEnd(indoorGame);
    }

    @Test
    public void playGame_substitutions() {
        IndoorGame indoorGame = GameFactory.createIndoorGame("VBR", UserId.VBR_USER_ID);

        defineTeams(indoorGame);

        composeTeamsSet1(indoorGame);
        playSet1_complete(indoorGame);

        composeTeamsSet2(indoorGame);
        playSet2_complete(indoorGame);

        composeTeamsSet3(indoorGame);
        playSet3_complete(indoorGame);

        composeTeamsSet4(indoorGame);
        playSet4_substitutions(indoorGame);
    }

    @Test
    public void playGame_io() {
        IndoorGame indoorGame = GameFactory.createIndoorGame("VBR", UserId.VBR_USER_ID);

        defineTeams(indoorGame);

        composeTeamsSet1(indoorGame);
        playSet1_complete(indoorGame);

        composeTeamsSet2(indoorGame);
        playSet2_complete(indoorGame);

        composeTeamsSet3(indoorGame);
        playSet3_complete(indoorGame);

        composeTeamsSet4(indoorGame);
        playSet4_substitutions(indoorGame);

        for (int index = 0; index < 1000; index++) {
            Log.i("VBR-Test", "playGame_io index #" + index);
            ServicesProvider.getInstance().getRecordedGamesService().saveCurrentGame();
            GameService gameService = ServicesProvider.getInstance().getRecordedGamesService().loadCurrentGame();
            assertNotEquals(null, indoorGame);
            assertEquals(indoorGame, gameService);
        }
    }

    private void defineTeams(IndoorGame indoorGame) {
        indoorGame.setRules(Rules.DEFAULT_UNIVERSAL_RULES);
        indoorGame.setGameDate(System.currentTimeMillis());
        indoorGame.setGameSchedule(System.currentTimeMillis());

        indoorGame.setGenderType(GenderType.GENTS);

        indoorGame.setLeagueName("FIVB Volleyball World League 2017");

        indoorGame.setTeamName(TeamType.HOME, "BRAZIL");
        indoorGame.setTeamName(TeamType.GUEST, "FRANCE");
        indoorGame.setTeamColor(TeamType.HOME, ContextCompat.getColor(mActivityRule.getActivity(), R.color.colorShirt10));
        indoorGame.setTeamColor(TeamType.GUEST, ContextCompat.getColor(mActivityRule.getActivity(), R.color.colorShirt3));
        indoorGame.setLiberoColor(TeamType.HOME, ContextCompat.getColor(mActivityRule.getActivity(), R.color.colorShirt3));
        indoorGame.setLiberoColor(TeamType.GUEST, ContextCompat.getColor(mActivityRule.getActivity(), R.color.colorShirt8));

        indoorGame.addPlayer(TeamType.HOME, 1);
        indoorGame.addPlayer(TeamType.HOME, 3);
        indoorGame.addPlayer(TeamType.HOME, 4);
        indoorGame.addPlayer(TeamType.HOME, 5);
        indoorGame.addPlayer(TeamType.HOME, 6);
        indoorGame.addPlayer(TeamType.HOME, 8);
        indoorGame.addPlayer(TeamType.HOME, 9);
        indoorGame.addPlayer(TeamType.HOME, 10);
        indoorGame.addPlayer(TeamType.HOME, 11);
        indoorGame.addPlayer(TeamType.HOME, 13);
        indoorGame.addPlayer(TeamType.HOME, 16);
        indoorGame.addPlayer(TeamType.HOME, 18);
        indoorGame.addPlayer(TeamType.HOME, 19);
        indoorGame.addPlayer(TeamType.HOME, 20);

        indoorGame.addPlayer(TeamType.GUEST, 2);
        indoorGame.addPlayer(TeamType.GUEST, 5);
        indoorGame.addPlayer(TeamType.GUEST, 6);
        indoorGame.addPlayer(TeamType.GUEST, 8);
        indoorGame.addPlayer(TeamType.GUEST, 9);
        indoorGame.addPlayer(TeamType.GUEST, 10);
        indoorGame.addPlayer(TeamType.GUEST, 11);
        indoorGame.addPlayer(TeamType.GUEST, 12);
        indoorGame.addPlayer(TeamType.GUEST, 14);
        indoorGame.addPlayer(TeamType.GUEST, 16);
        indoorGame.addPlayer(TeamType.GUEST, 17);
        indoorGame.addPlayer(TeamType.GUEST, 18);
        indoorGame.addPlayer(TeamType.GUEST, 20);
        indoorGame.addPlayer(TeamType.GUEST, 21);

        indoorGame.addLibero(TeamType.HOME, 6);
        indoorGame.addLibero(TeamType.HOME, 8);

        indoorGame.addLibero(TeamType.GUEST, 2);
        indoorGame.addLibero(TeamType.GUEST, 20);

        indoorGame.setCaptain(TeamType.HOME, 1);
        indoorGame.setCaptain(TeamType.GUEST, 6);

        indoorGame.startMatch();

        ServicesProvider.getInstance().getRecordedGamesService().connectGameRecorder();
    }

    private void composeTeamsSet1(IndoorGame indoorGame) {
        indoorGame.substitutePlayer(TeamType.HOME, 18, PositionType.POSITION_1, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 13, PositionType.POSITION_2, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 1, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 19, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 16, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 4, PositionType.POSITION_6, ActionOriginType.USER);

        indoorGame.substitutePlayer(TeamType.GUEST, 6, PositionType.POSITION_1, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 9, PositionType.POSITION_2, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 10, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 12, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 5, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 21, PositionType.POSITION_6, ActionOriginType.USER);

        indoorGame.confirmStartingLineup();

        indoorGame.substitutePlayer(TeamType.HOME, 8, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 2, PositionType.POSITION_6, ActionOriginType.USER);
    }

    private void playSet1_complete(IndoorGame indoorGame) {
        indoorGame.swapServiceAtStart();

        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.substitutePlayer(TeamType.HOME, 3, PositionType.POSITION_1, ActionOriginType.USER);
        indoorGame.callTimeout(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
    }

    private void composeTeamsSet2(IndoorGame indoorGame) {
        indoorGame.substitutePlayer(TeamType.HOME, 19, PositionType.POSITION_1, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 16, PositionType.POSITION_2, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 4, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 18, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 13, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 1, PositionType.POSITION_6, ActionOriginType.USER);

        indoorGame.substitutePlayer(TeamType.GUEST, 5, PositionType.POSITION_1, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 21, PositionType.POSITION_2, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 6, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 9, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 10, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 12, PositionType.POSITION_6, ActionOriginType.USER);

        indoorGame.confirmStartingLineup();

        indoorGame.substitutePlayer(TeamType.HOME, 8, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 2, PositionType.POSITION_5, ActionOriginType.USER);
    }

    private void playSet2_complete(IndoorGame indoorGame) {
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.substitutePlayer(TeamType.HOME, 20, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 9, PositionType.POSITION_6, ActionOriginType.USER);
        indoorGame.setActingCaptain(TeamType.HOME, 20);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
    }

    private void composeTeamsSet3(IndoorGame indoorGame) {
        indoorGame.substitutePlayer(TeamType.HOME, 4, PositionType.POSITION_1, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 18, PositionType.POSITION_2, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 13, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 1, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 19, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 16, PositionType.POSITION_6, ActionOriginType.USER);

        indoorGame.substitutePlayer(TeamType.GUEST, 5, PositionType.POSITION_1, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 21, PositionType.POSITION_2, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 6, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 9, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 10, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 12, PositionType.POSITION_6, ActionOriginType.USER);

        indoorGame.confirmStartingLineup();

        indoorGame.substitutePlayer(TeamType.HOME, 8, PositionType.POSITION_6, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 2, PositionType.POSITION_5, ActionOriginType.USER);
    }

    private void playSet3_complete(IndoorGame indoorGame) {
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.substitutePlayer(TeamType.HOME, 3, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.substitutePlayer(TeamType.GUEST, 11, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 8, PositionType.POSITION_2, ActionOriginType.USER);
        indoorGame.setActingCaptain(TeamType.GUEST, 8);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
    }

    private void composeTeamsSet4(IndoorGame indoorGame) {
        indoorGame.substitutePlayer(TeamType.HOME, 19, PositionType.POSITION_1, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 16, PositionType.POSITION_2, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 4, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 18, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 3, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 1, PositionType.POSITION_6, ActionOriginType.USER);

        indoorGame.substitutePlayer(TeamType.GUEST, 12, PositionType.POSITION_1, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 5, PositionType.POSITION_2, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 21, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 6, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 9, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 10, PositionType.POSITION_6, ActionOriginType.USER);

        indoorGame.confirmStartingLineup();

        indoorGame.substitutePlayer(TeamType.HOME, 8, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 2, PositionType.POSITION_6, ActionOriginType.USER);
    }

    private void playSet4_complete(IndoorGame indoorGame) {
        playSet4_substitutions(indoorGame);

        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
    }

    private void playSet4_substitutions(IndoorGame indoorGame) {
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.callTimeout(TeamType.GUEST);
        indoorGame.substitutePlayer(TeamType.GUEST, 8, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.callTimeout(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.substitutePlayer(TeamType.HOME, 9, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 20, PositionType.POSITION_6, ActionOriginType.USER);
        indoorGame.setActingCaptain(TeamType.HOME, 9);
        indoorGame.substitutePlayer(TeamType.GUEST, 11, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.setActingCaptain(TeamType.GUEST, 9);
        indoorGame.substitutePlayer(TeamType.GUEST, 5, PositionType.POSITION_2, ActionOriginType.USER);
    }

    private void composeTeamsSet5(IndoorGame indoorGame) {
        indoorGame.swapTeams(ActionOriginType.USER);

        indoorGame.substitutePlayer(TeamType.HOME, 19, PositionType.POSITION_1, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 16, PositionType.POSITION_2, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 4, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 18, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 3, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.HOME, 1, PositionType.POSITION_6, ActionOriginType.USER);

        indoorGame.substitutePlayer(TeamType.GUEST, 6, PositionType.POSITION_1, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 9, PositionType.POSITION_2, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 10, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 12, PositionType.POSITION_4, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 5, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 21, PositionType.POSITION_6, ActionOriginType.USER);

        indoorGame.confirmStartingLineup();

        indoorGame.substitutePlayer(TeamType.HOME, 8, PositionType.POSITION_5, ActionOriginType.USER);
        indoorGame.substitutePlayer(TeamType.GUEST, 2, PositionType.POSITION_6, ActionOriginType.USER);
    }

    private void playSet5_complete(IndoorGame indoorGame) {
        playSet5_lastSetEnd(indoorGame);

        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
    }

    private void playSet5_lastSetEnd(IndoorGame indoorGame) {
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.callTimeout(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.callTimeout(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.substitutePlayer(TeamType.HOME, 13, PositionType.POSITION_3, ActionOriginType.USER);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.callTimeout(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.GUEST);
        indoorGame.addPoint(TeamType.HOME);
        indoorGame.addPoint(TeamType.GUEST);
    }
}