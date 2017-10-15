package com.teammj.model.games;

import com.teammj.model.DATA;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Person;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Map;

/**
 * Game interface for implementation.
 * See abstract methods
 * @author Johan van Zyl
 * @author Michael Guida
 * @see CyclingGame
 * @see SprintGame
 * @see SwimmingGame
 */
public interface Game  {
    ArrayList<Person> getParticipants();
    boolean haveIbeenRan();
    void setHaveIbeenRan(boolean bool, boolean loading);
    Map<Athlete, Integer> getAthleteTimes();
    void setAthleteTimes(Map<Athlete, Integer> athleteTimes);
    String getUniqueID();
    void setElement(Element element);
    void setParticipants(ArrayList<Person> participants);
    void setUniqueID(String uniqueID, boolean loading);
    void addParticipant(Person participant);
    public DATA.GAMETYPE getGametype();
    public Integer getCount();
}
