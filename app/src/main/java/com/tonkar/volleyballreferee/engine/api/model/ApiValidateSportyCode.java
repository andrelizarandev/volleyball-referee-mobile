package com.tonkar.volleyballreferee.engine.api.model;

import lombok.Data;

// Sporty, payload when validating a sporty code
@Data
public class ApiValidateSportyCode {

    private EventData evento;
    private CanchaData[] canchas;

    private static class EventData {
        private String cve;
        private String titulo;
        private String fecha_inicio;
        private String fecha_fin;
    }

    private static class CanchaData {
        private String cve;
        private String nombre;
        private String deporte;
    }


}
