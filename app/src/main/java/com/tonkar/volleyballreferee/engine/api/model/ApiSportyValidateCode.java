package com.tonkar.volleyballreferee.engine.api.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import lombok.Data;

// Sporty, payload when validating a sporty code
@Data
public class ApiSportyValidateCode {

    private EventData evento;
    private CanchaData[] canchas;
    private EstadoData[] estados;

    @Data
    public static class EventData {
        private String cve;
        private String titulo;
        private String fecha_inicio;
        private String fecha_fin;
    }

    @Data
    public static class CanchaData {
        @PrimaryKey
        @NonNull
        private String cve;
        @NonNull
        private String nombre;
        @NonNull
        private String deporte;
    }

    @Data
    public static class EstadoData {
        @PrimaryKey
        @NonNull
        private String cve;
        @NonNull
        private String title;
    }

}
