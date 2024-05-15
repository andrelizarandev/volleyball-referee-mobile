package com.tonkar.volleyballreferee.engine.api.model;
import androidx.room.Embedded;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ApiSportyPostResponseFilterGames {

    public JuegosData[] juegos;

    public static class JuegosData {
        public String cve;
        public ApiSportyPostResponseFilterGames.CompetenciaData competencia;
        public ApiSportyPostResponseFilterGames.EquipoData equipo1;
        public ApiSportyPostResponseFilterGames.EquipoData equipo2;
        public ApiSportyPostResponseFilterGames.ConfigGame confiGame;
        public String fase;
        public String estado;
        public String dia;
        public String hora;
    }

    public static class CompetenciaData {
        public String deporte;
        public String categoria;
        public String rama;
        public String clasificatorio;
    }

    public static class EquipoData {
        public String cve;
        public String nombre;
        public MarcadorData marcador;
    }

    public static class MarcadorData {
        public String set1_1;
        public String set1_2;
        public String set1_3;
    }

    public static class ConfigGame {
        public String cntSets;
        public int pntSet;
    }

}
