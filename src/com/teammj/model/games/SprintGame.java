package com.teammj.model.games;

import com.teammj.controller.DocumentHandler;
import com.teammj.model.DATA;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Person;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Map;

/**
 * SprintGame subclass
 *
 * @author Johan van Zyl
 * @author Michael Guida
 * @see Game
 */
public final class SprintGame implements Game {
    private ArrayList<Person> participants;
    private Map<Athlete, Integer> athleteTimes;
    private String uniqueID;
    private static Integer count = 1;
    private boolean haveIbeenRan;
    private Element element;
    private static final DATA.GAMETYPE gametype = DATA.GAMETYPE.Sprinting;

    public SprintGame(Element element) {
        this.element = element;
        participants = new ArrayList<>();
        if (count < 10) {
            setUniqueID("R0".concat(Integer.toString(count++)), false);
        } else {
            setUniqueID("R".concat(Integer.toString(count++)), false);
        }
    }

    public ArrayList<Person> getParticipants() {
        return participants;
    }

    public boolean haveIbeenRan() {
        return haveIbeenRan;
    }

    public void setHaveIbeenRan(boolean bool, boolean loading) {
        this.haveIbeenRan = bool;
        if(!loading) DocumentHandler.setAttr(DATA.BEENRAN, Boolean.toString(bool), element);
    }

    public Map<Athlete, Integer> getAthleteTimes() {
        return athleteTimes;
    }

    public void setAthleteTimes(Map<Athlete, Integer> athleteTimes) {
        this.athleteTimes = athleteTimes;
        DocumentHandler.updateAthleteMap(athleteTimes, element);
    }

    public String getUniqueID() {
        return uniqueID;
    }

    @Override
    public void setElement(Element element) {
        this.element = element;
    }

    @Override
    public void setParticipants(ArrayList<Person> participants) {
        this.participants = participants;
    }

    @Override
    public void setUniqueID(String uniqueID, boolean loading) {
        this.uniqueID = uniqueID;
        if(!loading) DocumentHandler.setAttr(DATA.UNIQUEID, uniqueID, element);
    }

    @Override
    public void addParticipant(Person participant) {
        this.participants.add(participant);
        Element person = this.element.getOwnerDocument().createElement(DATA.PERSON);
        DocumentHandler.setAttr(DATA.UUID, participant.getUniqueID().toString(), person);
        element.appendChild(person);
    }

    public DATA.GAMETYPE getGametype() {
        return gametype;
    }

    @Override
    public Integer getCount() {
        return count - 2;
    }
}