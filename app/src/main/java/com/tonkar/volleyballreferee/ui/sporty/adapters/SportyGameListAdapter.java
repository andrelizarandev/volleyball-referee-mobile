package com.tonkar.volleyballreferee.ui.sporty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.tonkar.volleyballreferee.R;
import androidx.recyclerview.widget.RecyclerView;

import com.tonkar.volleyballreferee.engine.api.model.ApiSportyPostResponseFilterGames;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;
import com.tonkar.volleyballreferee.engine.database.VbrRepository;

import java.util.List;

public class SportyGameListAdapter extends RecyclerView.Adapter<SportyGameHolder> {

    private final Context context;
    private final List<ApiSportyPostResponseFilterGames.JuegosData> listGames;
    private VbrRepository vbrRepository;


    public SportyGameListAdapter (List<ApiSportyPostResponseFilterGames.JuegosData> listGames, Context context) {
        this.listGames = listGames;
        this.context = context;
        this.vbrRepository = new VbrRepository(context);
    }

    @NonNull
    @Override
    public SportyGameHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sporty_game_container, viewGroup, false);
        return new SportyGameHolder(view);
    }

    public int getItemCount() {
        return listGames.size();
    }

    public void onBindViewHolder (SportyGameHolder holder, int position) {

        List<ApiSportyValidateCode.EstadoData> stateList = vbrRepository.listStates();

        ApiSportyPostResponseFilterGames.JuegosData game = listGames.get(position);

        holder.bind(listGames.get(position), stateList);

        holder.itemView.setOnClickListener(v -> {
            // Intent intent = new Intent(context, SportyGameDetailActivity.class);
            // intent.putExtra("selected_game", game.cve);
            // context.startActivity(intent);
        });

    }

}
