package com.tonkar.volleyballreferee.ui.sporty.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyPostResponseFilterGames;
import com.tonkar.volleyballreferee.R;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;

import java.util.List;

public class SportyGameHolder extends RecyclerView.ViewHolder {

    TextView tvName = itemView.findViewById(R.id.item_sporty_game_tv_name);
    TextView tvState = itemView.findViewById(R.id.item_sporty_game_tv_state);
    TextView tvDate = itemView.findViewById(R.id.item_sporty_game_tv_date_time);

    public SportyGameHolder (View itemView) {
        super(itemView);
    }

    // You can add methods to update the views based on your data here
    public void bind (ApiSportyPostResponseFilterGames.JuegosData data, List<ApiSportyValidateCode.EstadoData> stateList) {
        ApiSportyValidateCode.EstadoData stateData = searchState(stateList, data.estado);
        tvName.setText("Juego: " + data.cve);
        tvDate.setText("Hora y Fecha: " + data.dia + " - " + data.hora);
        tvState.setText("Estado: " + stateData.getTitle());
    }

    public ApiSportyValidateCode.EstadoData searchState (List<ApiSportyValidateCode.EstadoData> stateList, String state) {
        for (ApiSportyValidateCode.EstadoData stateData : stateList) {
            if (stateData.cve.equals(state)) { return stateData; }
        }
        return null;
    }

}