package com.tonkar.volleyballreferee.ui;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tonkar.volleyballreferee.R;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyPostResponseFilterGames;
import com.tonkar.volleyballreferee.engine.database.VbrRepository;
import com.tonkar.volleyballreferee.engine.database.model.SportyGameEntity;
import com.tonkar.volleyballreferee.ui.sporty.adapters.SportyGameListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListSportyGamesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VbrRepository vbrRepository = new VbrRepository(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_sporty_games);
        configureToolbar();
        getComponents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void configureToolbar () {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Seleccionar Juego");
        setSupportActionBar(toolbar);
    }

    private List<ApiSportyPostResponseFilterGames.JuegosData> getGamesFromDb () {

        List<ApiSportyPostResponseFilterGames.JuegosData> parsedList = new ArrayList<>();

        List<SportyGameEntity> sportyListGames = vbrRepository.listSportyGames();

        for (SportyGameEntity sportyGameData : sportyListGames) {
            ApiSportyPostResponseFilterGames.JuegosData gameData = new Gson().fromJson(sportyGameData.getContent(), ApiSportyPostResponseFilterGames.JuegosData.class);
            parsedList.add(gameData);
        }

        return parsedList;

    }

    public void getComponents () {
        recyclerView = findViewById(R.id.list_sporty_games_rv_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SportyGameListAdapter adapter = new SportyGameListAdapter(getGamesFromDb(), this);
        recyclerView.setAdapter(adapter);
    }

}