package com.tonkar.volleyballreferee.ui.sporty.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyPostResponseFilterGames;
import com.tonkar.volleyballreferee.R;
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyValidateCode;

import java.util.List;

public class SportyGameHolder extends RecyclerView.ViewHolder {

    TextView tvCategory = itemView.findViewById(R.id.item_sporty_game_tv_category);
    TextView tvPhase = itemView.findViewById(R.id.item_sporty_game_tv_phase);
    TextView tvTeam1 = itemView.findViewById(R.id.item_sporty_game_tv_team1);
    TextView tvTeam2 = itemView.findViewById(R.id.item_sporty_game_tv_team2);
    TextView tvScore1 = itemView.findViewById(R.id.item_sporty_game_tv_score1);
    TextView tvScore2 = itemView.findViewById(R.id.item_sporty_game_tv_score1);
    TextView tvState = itemView.findViewById(R.id.item_sporty_game_tv_state);
    TextView tvDateAndHour = itemView.findViewById(R.id.item_sporty_game_tv_date_and_hour);

    public SportyGameHolder (View itemView) {
        super(itemView);
    }

    public ApiSportyValidateCode.EstadoData getCurrentState (List<ApiSportyValidateCode.EstadoData> stateList, String state) {
        for (ApiSportyValidateCode.EstadoData stateData : stateList) {
            if (stateData.cve.equals(state)) {
                return stateData;
            }
        }
        return null;
    }

    // You can add methods to update the views based on your data here
    public void bind (
        ApiSportyPostResponseFilterGames.JuegosData data,
        List<ApiSportyValidateCode.EstadoData> stateList
    ) {

        ApiSportyValidateCode.EstadoData stateData = getCurrentState(stateList, data.estado);

        tvCategory.setText(data.competencia.rama + " - " + data.competencia.categoria);
        tvPhase.setText("Fase: " + data.fase);
        tvTeam1.setText(data.equipo1.nombre);
        tvTeam2.setText(data.equipo2.nombre);

        String score1 = (data.equipo1.marcador.set1_1 != null)
                ? data.equipo1.marcador.set1_1
                : "0";

        String score2 = (data.equipo1.marcador.set1_2!= null)
                ? data.equipo1.marcador.set1_2
                : "0";

        String score3 = (data.equipo1.marcador.set1_3 != null)
                ? data.equipo1.marcador.set1_3
                : "0";

        String score4 = (data.equipo2.marcador.set1_1 != null)
                ? data.equipo2.marcador.set1_1
                : "0";

        String score5 = (data.equipo2.marcador.set1_2 != null)
                ? data.equipo2.marcador.set1_2
                : "0";

        String score6 = (data.equipo2.marcador.set1_3 != null)
                ? data.equipo2.marcador.set1_3
                : "0";

        tvScore1.setText(score1 + " - " + score2 + " - " + score3);
        tvScore2.setText(score4 + " - " + score5 + " - " + score6);

        tvState.setText(stateData.title);

        tvDateAndHour.setText(data.dia + " " + data.hora);

    }

}