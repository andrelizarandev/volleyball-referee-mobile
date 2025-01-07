package com.tonkar.volleyballreferee.ui.sporty.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.tonkar.volleyballreferee.R;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tonkar.volleyballreferee.engine.PrefUtils;
import com.tonkar.volleyballreferee.engine.Tags;
import com.tonkar.volleyballreferee.engine.api.model.ActionVR;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyPostResponseFilterGames;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;
import com.tonkar.volleyballreferee.engine.api.model.ApiUserSummary;
import com.tonkar.volleyballreferee.engine.database.VbrRepository;
import com.tonkar.volleyballreferee.engine.game.BeachGame;
import com.tonkar.volleyballreferee.engine.game.GameFactory;
import com.tonkar.volleyballreferee.engine.game.IndoorGame;
import com.tonkar.volleyballreferee.engine.rules.Rules;
import com.tonkar.volleyballreferee.engine.service.StoredGamesManager;
import com.tonkar.volleyballreferee.engine.service.StoredGamesService;
import com.tonkar.volleyballreferee.ui.setup.GameSetupActivity;
import com.tonkar.volleyballreferee.ui.setup.QuickGameSetupActivity;
import com.tonkar.volleyballreferee.ui.util.UiUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SportyGameListAdapter extends RecyclerView.Adapter<SportyGameHolder> {

    private final Context context;
    private final List<ApiSportyPostResponseFilterGames.JuegosData> listGames;
    private final VbrRepository vbrRepository;
    private final StoredGamesService mStoredGamesService;

    public SportyGameListAdapter (
        List<ApiSportyPostResponseFilterGames.JuegosData> listGames,
        Context context
    ) {
        this.listGames = listGames;
        this.context = context;
        this.vbrRepository = new VbrRepository(context);
        mStoredGamesService = new StoredGamesManager(context);
    }

    @NonNull
    @Override
    public SportyGameHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sporty_game_container, viewGroup, false);
        return new SportyGameHolder(view);
    }

    public int getItemCount () {
        return listGames.size();
    }

    public void onBindViewHolder (SportyGameHolder holder, int position) {

        List<ApiSportyValidateCode.EstadoData> stateList = vbrRepository.listStates();
        ApiSportyPostResponseFilterGames.JuegosData game = listGames.get(position);
        holder.bind(game, stateList);

        ActionVR currentGameType = game.confiGame.actionvr;
        boolean isGamePlayed = game.estado.equals("1");

        System.out.println(currentGameType);

        if (isGamePlayed) {
            holder.itemView.setClickable(false);
            holder.itemView.setFocusable(false);
        }

        holder.itemView.setOnClickListener(v -> {
            System.out.println(currentGameType);
            if (isGamePlayed) UiUtils.makeText(context, context.getString(R.string.sporty_already_played_game), Toast.LENGTH_LONG).show();
            else if (currentGameType == ActionVR.vol) startIndoorGame(position);
            else if (currentGameType == ActionVR.sb) startScoreboardGame(position);
            else if (currentGameType == ActionVR.vol_play) startBeachGame(position);
        });

    }

    public void startIndoorGame (int position) {
        Log.i(Tags.GAME_UI, "Start an indoor game");
        ApiUserSummary user = PrefUtils.getUser(context);
        IndoorGame game = GameFactory.createIndoorGame(
                UUID.randomUUID().toString(),
                user.getId(),
                user.getPseudo(),
                System.currentTimeMillis(),
                0L,
                Rules.officialIndoorRules()
        );
        mStoredGamesService.saveSetupGame(game);
        Log.i(Tags.GAME_UI, "Start activity to setup game");
        final Intent intent = new Intent(context, GameSetupActivity.class);
        intent.putExtra("sporty_game_position", position);
        context.startActivity(intent);
    }

    public void startScoreboardGame (int position) {
        Log.i(Tags.GAME_UI, "Start a scoreboard game");
        ApiUserSummary user = PrefUtils.getUser(context);
        IndoorGame game = GameFactory.createPointBasedGame(
                UUID.randomUUID().toString(),
                user.getId(),
                user.getPseudo(),
                System.currentTimeMillis(),
                0L,
                Rules.officialIndoorRules()
        );
        mStoredGamesService.saveSetupGame(game);
        Log.i(Tags.GAME_UI, "Start activity to setup game quickly");
        final Intent intent = new Intent(context, QuickGameSetupActivity.class);
        intent.putExtra("sporty_game_position", position);
        context.startActivity(intent);
    }

    public void startBeachGame (int position) {
        ApiUserSummary user = PrefUtils.getUser(context);
        BeachGame game = GameFactory.createBeachGame(
            UUID.randomUUID().toString(),
            user.getId(),
            user.getPseudo(),
            System.currentTimeMillis(),
            0L,
            Rules.officialBeachRules()
        );
        mStoredGamesService.saveSetupGame(game);
        Log.i(Tags.GAME_UI, "Start activity to setup game quickly");
        final Intent intent = new Intent(context, QuickGameSetupActivity.class);
        intent.putExtra("sporty_game_position", position);
        context.startActivity(intent);
    }

}
