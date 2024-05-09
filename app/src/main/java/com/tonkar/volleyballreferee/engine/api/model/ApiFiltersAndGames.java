package com.tonkar.volleyballreferee.engine.api.model;

import lombok.Data;

@Data
// Sporty, response when fetching games and filters in main activity
public class ApiFiltersAndGames {

    private ApiFilters filters;

    private ApiGame[] games;

    private static class ApiFilters {
        private String[] categories;
        private String[] locations;
        private String[] teams;
    }

    private static class ApiGame {
        private String id;
        private String category;
        private String location;
        private String team1;
        private String team2;
        private String date;
        private String time;
        private String court;
        private String referee;
        private String score;
        private String status;
    }

}