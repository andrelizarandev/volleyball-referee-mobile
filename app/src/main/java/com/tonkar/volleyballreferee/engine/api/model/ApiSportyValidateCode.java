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
        public String cve;
        public String titulo;
        public String fecha_inicio;
        public String fecha_fin;
    }

    @Data
    public static class CanchaData {
        @PrimaryKey
        @NonNull
        public String cve;
        @NonNull
        public String nombre;
        @NonNull
        public String deporte;
    }

    @Data
    public static class EstadoData {
        @PrimaryKey
        @NonNull
        public String cve;
        @NonNull
        public String title;
    }

}
