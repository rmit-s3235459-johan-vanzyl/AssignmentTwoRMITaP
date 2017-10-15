package com.teammj.model.persons.base;

import com.teammj.controller.DocumentHandler;
import com.teammj.model.DATA;
import com.teammj.model.games.Game;
import com.teammj.model.persons.Cyclist;
import com.teammj.model.persons.Sprinter;
import com.teammj.model.persons.SuperAthlete;
import com.teammj.model.persons.Swimmer;
import org.w3c.dom.Element;

import java.util.UUID;

/**
 * Athlete super class for inheritance.
 * See abstract methods
 * @author Johan van Zyl
 * @author Michael Guida
 * @see Cyclist
 * @see Sprinter
 * @see Swimmer
 * @see SuperAthlete
 */
public abstract class Athlete extends Person implements Comparable<Athlete> {

    private int points;

    protected Athlete(String name, int age, DATA.STATE fromState, Element element, UUID... uuids) {
        super(name, age, fromState, element, uuids);
        DocumentHandler.setAttr(DATA.POINTS, "0", element);
        points = 0;
    }

    /**
     * Compete in game method
     * @param game - game competing in
     * @return - time athlete spent in game
     */
    public abstract int compete(Game game);

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
        DocumentHandler.setAttr(DATA.POINTS, Integer.toString(this.points), element);
    }

    public void setPoints(int points) {
        this.points = points;
        DocumentHandler.setAttr(DATA.POINTS, Integer.toString(points), element);
    }

    /**
     * Just to be able to compare 2 Athlete's score
     * @param comparedAthlete - The other Athlete
     * @return - The athlete with more points (as int) or alphabetically if equal
     */
    @Override
    public int compareTo(Athlete comparedAthlete) {
        int compare = comparedAthlete.getPoints() - this.points;
        if(compare == 0) compare = this.getName().compareTo(comparedAthlete.getName());
        return compare;
    }

}
