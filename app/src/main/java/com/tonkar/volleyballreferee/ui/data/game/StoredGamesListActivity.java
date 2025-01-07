package com.tonkar.volleyballreferee.ui.data.game;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.tonkar.volleyballreferee.R;
import com.tonkar.volleyballreferee.engine.PrefUtils;
import com.tonkar.volleyballreferee.engine.Tags;
import com.tonkar.volleyballreferee.engine.api.VbrApi;
import com.tonkar.volleyballreferee.engine.api.model.ApiGameSummary;
import com.tonkar.volleyballreferee.engine.api.model.ApiPlayer;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyUpdateGame;
import com.tonkar.volleyballreferee.engine.database.VbrRepository;
import com.tonkar.volleyballreferee.engine.database.model.SportyGameEntity;
import com.tonkar.volleyballreferee.engine.game.GameType;
import com.tonkar.volleyballreferee.engine.game.UsageType;
import com.tonkar.volleyballreferee.engine.service.DataSynchronizationListener;
import com.tonkar.volleyballreferee.engine.service.IStoredGame;
import com.tonkar.volleyballreferee.engine.service.StoredGamesManager;
import com.tonkar.volleyballreferee.engine.service.StoredGamesService;
import com.tonkar.volleyballreferee.engine.team.TeamType;
import com.tonkar.volleyballreferee.ui.NavigationActivity;
import com.tonkar.volleyballreferee.ui.util.UiUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StoredGamesListActivity extends NavigationActivity  implements DataSynchronizationListener {

    private StoredGamesService     mStoredGamesService;
    private StoredGamesListAdapter mStoredGamesListAdapter;
    private SwipeRefreshLayout     mSyncLayout;
    private MenuItem               mDeleteSelectedGamesItem;
    private VbrRepository          vbrRepository;
    private Button                 sendNoSynchronizedGamesButton;

    @Override
    protected String getToolbarTitle() {
        return "";
    }

    @Override
    protected int getCheckedItem() {
        return R.id.action_stored_games;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(Tags.STORED_GAMES, "Create stored games list activity");
        setContentView(R.layout.activity_stored_games_list);

        initNavigationMenu();

        mSyncLayout = findViewById(R.id.stored_games_sync_layout);
        mSyncLayout.setOnRefreshListener(this::updateStoredGamesList);
        sendNoSynchronizedGamesButton = findViewById(R.id.sporty_send_no_synchronized_games_button);

        vbrRepository = new VbrRepository(this);
        mStoredGamesService = new StoredGamesManager(this);

        List<ApiGameSummary> games = mStoredGamesService.listGames();

        final ListView storedGamesList = findViewById(R.id.stored_games_list);

        mStoredGamesListAdapter = new StoredGamesListAdapter(this, getLayoutInflater(), games);
        storedGamesList.setAdapter(mStoredGamesListAdapter);

        storedGamesList.setOnItemClickListener((parent, view, position, l) -> {

            ApiGameSummary game = mStoredGamesListAdapter.getItem(position);

            if (mStoredGamesListAdapter.hasSelectedItems()) {

                mStoredGamesListAdapter.toggleItemSelection(game.getId());
                mDeleteSelectedGamesItem.setVisible(mStoredGamesListAdapter.hasSelectedItems());

            } else {

                Log.i(Tags.STORED_GAMES, String.format("Start activity to display stored game %s", game.getId()));

                final Intent intent;

                if (UsageType.POINTS_SCOREBOARD.equals(game.getUsage()) || GameType.TIME.equals(game.getKind())) {
                    intent = new Intent(StoredGamesListActivity.this, StoredBasicGameActivity.class);
                    Log.i("STORE GAMES", "basic");
                } else {
                    intent = new Intent(StoredGamesListActivity.this, StoredAdvancedGameActivity.class);
                    Log.i("STORE GAMES", "advanced");
                }

                intent.putExtra("game", game.getId());
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "listItemToDetails").toBundle());

            }
        });


        storedGamesList.setOnItemLongClickListener((parent, view, position, id) -> {
            ApiGameSummary game = mStoredGamesListAdapter.getItem(position);
            mStoredGamesListAdapter.toggleItemSelection(game.getId());
            mDeleteSelectedGamesItem.setVisible(mStoredGamesListAdapter.hasSelectedItems());
            return true;
        });

        sendNoSynchronizedGamesButton.setOnClickListener(v -> sendNoSynchronizedGames());

        sendNoSynchronizedGames();

        updateStoredGamesList();

    }

    private void sendNoSynchronizedGames() {
        List<ApiGameSummary> games = mStoredGamesService.listGames();
        for (ApiGameSummary game : games) {
            if (!game.isSynced() && !game.getCve().equals("null")) {
                IStoredGame storedGame = mStoredGamesService.getGame(game.getId());
                mStoredGamesService.pushGameToServer(storedGame, game.getCve(), () -> runOnUiThread(() -> {
                    UiUtils.makeText(StoredGamesListActivity.this, StoredGamesListActivity.this.getString(R.string.sporty_games_synced), Toast.LENGTH_LONG).show();
                    updateAdapter();
                }));
            }
        }
    }

    private void updateAdapter () {
        try {
            Thread.sleep(5000);
            List<ApiGameSummary> games = mStoredGamesService.listGames();
            mStoredGamesListAdapter.updateStoredGamesList(games);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_stored_games, menu);

        mDeleteSelectedGamesItem = menu.findItem(R.id.action_delete_games);
        mDeleteSelectedGamesItem.setVisible(false);

        MenuItem searchGamesItem = menu.findItem(R.id.action_search_games);
        SearchView searchGamesView = (SearchView) searchGamesItem.getActionView();

        searchGamesView.setOnQueryTextFocusChangeListener((view, hasFocus) -> {});

        searchGamesView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                mStoredGamesListAdapter.getFilter().filter(searchQuery.trim());
                return true;
            }
        });

        MenuItem syncItem = menu.findItem(R.id.action_sync);
        syncItem.setVisible(PrefUtils.canSync(this));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search_games) {
            return true;
        } else if (itemId == R.id.action_sync) {
            updateStoredGamesList();
            return true;
        } else if (itemId == R.id.action_delete_games) {
            deleteSelectedGames();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mStoredGamesListAdapter.hasSelectedItems()){
            mStoredGamesListAdapter.clearSelectedItems();
        } else {
            super.onBackPressed();
        }
    }

    private void deleteSelectedGames() {
        Log.i(Tags.STORED_GAMES, "Delete selected games");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_Dialog);
        builder.setTitle(getString(R.string.delete_selected_games)).setMessage(getString(R.string.delete_selected_games_question));
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            mStoredGamesService.deleteGames(mStoredGamesListAdapter.getSelectedItems(), this);
            UiUtils.makeText(StoredGamesListActivity.this, getString(R.string.deleted_selected_games), Toast.LENGTH_LONG).show();
        });
        builder.setNegativeButton(android.R.string.no, (dialog, which) -> {});
        AlertDialog alertDialog = builder.show();
        UiUtils.setAlertDialogMessageSize(alertDialog, getResources());
    }

    private void updateStoredGamesList() {
        if (PrefUtils.canSync(this)) {
            mSyncLayout.setRefreshing(true);
            mStoredGamesService.syncGames(this);
        }
    }

    @Override
    public void onSynchronizationSucceeded() {
        runOnUiThread(() -> {
            mStoredGamesListAdapter.updateStoredGamesList(mStoredGamesService.listGames());
            if (mDeleteSelectedGamesItem != null) {
                mDeleteSelectedGamesItem.setVisible(mStoredGamesListAdapter.hasSelectedItems());
            }
            mSyncLayout.setRefreshing(false);
        });
    }

    @Override
    public void onSynchronizationFailed() {
        runOnUiThread(() -> {
            UiUtils.makeErrorText(this, getString(R.string.sync_failed_message), Toast.LENGTH_LONG).show();
            mSyncLayout.setRefreshing(false);
        });
    }

}

