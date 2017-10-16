package com.teammj.model.persons;

import com.teammj.model.DATA;
import com.teammj.model.persons.base.Person;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.UUID;

final public class Competitor {
    private String name;
    private UUID uuid;
    private BooleanProperty predictedWinner = new SimpleBooleanProperty();
    private DATA.PERSON_TYPE personType;
    private Person person;

    public Competitor(String name, UUID uuid, boolean predictedWinner, DATA.PERSON_TYPE personType, Person person) {
        this.name = name;
        this.uuid = uuid;
        this.predictedWinner.set(predictedWinner);
        this.personType = personType;
        this.person = person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isPredictedWinner() {
        return predictedWinner.get();
    }

    public BooleanProperty predictedWinnerProperty() {
        return predictedWinner;
    }

    public void setPredictedWinner(boolean predictedWinner) {
        this.predictedWinner.set(predictedWinner);
    }

    public DATA.PERSON_TYPE getPersonType() {
        return personType;
    }

    public Person getPerson() {
        return person;
    }
}
