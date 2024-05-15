package com.tonkar.volleyballreferee.engine.api.model;

import lombok.Data;

@Data
public class ApiSportyPostRequestFilterGames {

    private String token;
    private String cancha;
    private String dia;
    private String estado;

    public ApiSportyPostRequestFilterGames(String token, String cancha, String dia, String estado) {
        this.token = token;
        this.cancha = cancha;
        this.dia = dia;
        this.estado = estado;
    }



}
