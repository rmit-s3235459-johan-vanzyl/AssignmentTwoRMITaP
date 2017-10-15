package com.teammj.model.persons;

import com.teammj.controller.Utilities;
import com.teammj.model.DATA;
import com.teammj.model.games.Game;
import com.teammj.model.persons.base.Athlete;
import org.w3c.dom.Element;

import java.util.UUID;

/**
 * Sprinter subclass
 * @author Johan van Zyl
 * @author Michael Guida
 * @see Athlete
 */
public final class Sprinter extends Athlete {
    public Sprinter(String name, int age, DATA.STATE fromState, Element element, UUID... uuids) {
        super(name, age, fromState, element, DATA.PERSON_TYPE.Sprinter, uuids);
        System.out.printf("Hello, my name is %s and I am %d years old. " +
                "I am also a Sprinter.\n", name, age);
    }

    /**
     * Compete in game method
     * @param game - game competing in
     * @return - time Sprinter spent sprinting in game
     */
    @Override
    public int compete(Game game) {
        getGamesAttended().add(game);
        return 10 + Utilities.random.nextInt(10);
    }
}
