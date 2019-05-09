package com.tonkar.volleyballreferee.ui.data;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.tonkar.volleyballreferee.R;
import com.tonkar.volleyballreferee.api.ApiGameDescription;
import com.tonkar.volleyballreferee.business.PrefUtils;
import com.tonkar.volleyballreferee.business.data.StoredGames;
import com.tonkar.volleyballreferee.interfaces.GameType;
import com.tonkar.volleyballreferee.interfaces.Tags;
import com.tonkar.volleyballreferee.interfaces.data.DataSynchronizationListener;
import com.tonkar.volleyballreferee.interfaces.data.StoredGamesService;
import com.tonkar.volleyballreferee.interfaces.UsageType;
import com.tonkar.volleyballreferee.ui.NavigationActivity;
import com.tonkar.volleyballreferee.ui.util.UiUtils;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class StoredGamesListActivity extends NavigationActivity implements DataSynchronizationListener {

    private StoredGamesService     mStoredGamesService;
    private StoredGamesListAdapter mStoredGamesListAdapter;
    private SwipeRefreshLayout     mSyncLayout;

    @Override
    protected String getToolbarTitle() {
        return "";
    }

    @Override
    protected int getCheckedItem() {
        return R.id.action_stored_games;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(Tags.STORED_GAMES, "Create stored games list activity");
        setContentView(R.layout.activity_stored_games_list);

        initNavigationMenu();

        mSyncLayout = findViewById(R.id.sync_layout);
        mSyncLayout.setOnRefreshListener(this::updateStoredGamesList);

        mStoredGamesService = new StoredGames(this);

        List<ApiGameDescription> games = mStoredGamesService.listGames();

        final ListView storedGamesList = findViewById(R.id.stored_games_list);
        mStoredGamesListAdapter = new StoredGamesListAdapter(this, getLayoutInflater(), games);
        storedGamesList.setAdapter(mStoredGamesListAdapter);

        storedGamesList.setOnItemClickListener((adapterView, view, i, l) -> {
            ApiGameDescription game = mStoredGamesListAdapter.getItem(i);
            Log.i(Tags.STORED_GAMES, String.format("Start activity to display stored game %s", game.getId()));

            final Intent intent;

            if ((GameType.INDOOR.equals(game.getKind()) || GameType.INDOOR_4X4.equals(game.getKind())) && UsageType.NORMAL.equals(game.getUsage())) {
                intent = new Intent(StoredGamesListActivity.this, StoredIndoorGameActivity.class);
            } else {
                intent = new Intent(StoredGamesListActivity.this, StoredBeachGameActivity.class);
            }

            intent.putExtra("game", game.getId());
            startActivity(intent);
            UiUtils.animateForward(this);
        });

        updateStoredGamesList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_stored_games, menu);

        MenuItem deleteAllGamesItem = menu.findItem(R.id.action_delete_games);
        deleteAllGamesItem.setVisible(mStoredGamesService.hasGames());

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
        switch (item.getItemId()) {
            case R.id.action_search_games:
                return true;
            case R.id.action_sync:
                updateStoredGamesList();
                return true;
            case R.id.action_delete_games:
                deleteAllGames();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAllGames() {
        Log.i(Tags.STORED_GAMES, "Delete all games");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_Dialog);
        builder.setTitle(getResources().getString(R.string.delete_games)).setMessage(getResources().getString(R.string.delete_games_question));
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            mStoredGamesService.deleteAllGames();
            UiUtils.makeText(StoredGamesListActivity.this, getResources().getString(R.string.deleted_games), Toast.LENGTH_LONG).show();
            UiUtils.navigateToHome(StoredGamesListActivity.this);
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
        mStoredGamesListAdapter.updateStoredGamesList(mStoredGamesService.listGames());
        mSyncLayout.setRefreshing(false);
    }

    @Override
    public void onSynchronizationFailed() {
        UiUtils.makeErrorText(this, getResources().getString(R.string.sync_failed_message), Toast.LENGTH_LONG).show();
        mSyncLayout.setRefreshing(false);
    }
}