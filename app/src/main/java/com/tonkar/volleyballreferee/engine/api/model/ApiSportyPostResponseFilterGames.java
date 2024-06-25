package com.tonkar.volleyballreferee.engine.api.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiSportyPostResponseFilterGames {

    public JuegosData[] juegos;

    public static class JuegosData {
        public String cve;
        public CompetenciaData competencia;
        public EquipoData equipo1;
        public EquipoData equipo2;
        public String fase;
        public ConfigGame confiGame;
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
        public String nombre;
        public MarcadorData marcador;
        public JugadorData[] jugadores;
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

    public static class JugadorData {
        public String cve;
        public String nombre_completo;
        public int dorsal;
        public Boolean es_capitan;
        public Boolean es_libero;
    }

}
