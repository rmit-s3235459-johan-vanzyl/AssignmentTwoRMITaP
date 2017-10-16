package com.teammj.model.persons;

/**
 * For displaying athlete times in a TableView class
 */
final public class AthleteMap {
    private String athleteName;
    private Integer athleteTime;
    private Integer athletePoints;

    public AthleteMap(String athleteName, Integer athleteTime, Integer athletePoints) {
        this.athleteName = athleteName;
        this.athleteTime = athleteTime;
        this.athletePoints = athletePoints;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public Integer getAthleteTime() {
        return athleteTime;
    }

    public Integer getAthletePoints() {
        return athletePoints;
    }
}
