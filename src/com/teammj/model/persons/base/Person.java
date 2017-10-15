package com.teammj.model.persons.base;

import com.teammj.controller.DocumentHandler;
import com.teammj.model.DATA;
import com.teammj.model.games.Game;
import org.w3c.dom.Element;
import java.util.ArrayDeque;
import java.util.UUID;

/**
 * Person super class for inheritance.
 * See abstract methods
 *
 * @author Johan van Zyl
 * @author Michael Guida
 * @see Official
 * @see Athlete
 */


public abstract class Person {
    private UUID UniqueID;
    private String name;
    private Integer age;
    private DATA.STATE fromState;
    private ArrayDeque<Game> gamesAttended = new ArrayDeque<>();
    private DATA.PERSON_TYPE personType;
    protected Element element;

    Person(String name, int age, DATA.STATE fromState, Element element, DATA.PERSON_TYPE personType, UUID ...uuid) {
        this.name = name;
        this.age = age;
        this.fromState = fromState;
        this.element = element;
        this.personType = personType;

        if(uuid.length > 0) {
            this.UniqueID = uuid[0];
        } else {
            UniqueID = UUID.randomUUID();
        }

        DocumentHandler.setAttr(DATA.UUID, UniqueID.toString(), element);
        DocumentHandler.setAttr(DATA.NAME, name, element);
        DocumentHandler.setAttr(DATA.AGE, Integer.toString(age), element);
        DocumentHandler.setAttr(DATA.STATE_S, fromState.toString(), element);
    }

    public String getName() {
        return name;
    }

    public UUID getUniqueID() {
        return UniqueID;
    }

    protected ArrayDeque<Game> getGamesAttended() {
        return gamesAttended;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public DATA.PERSON_TYPE getPersonType() {
        return personType;
    }

    public DATA.STATE getFromState() {
        return fromState;
    }

    public int getAge() {
        return age;
    }
}
