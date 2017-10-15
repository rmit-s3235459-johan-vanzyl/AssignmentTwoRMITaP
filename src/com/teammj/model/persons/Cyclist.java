package com.teammj.model.persons;

import com.teammj.controller.Utilities;
import com.teammj.model.DATA;
import com.teammj.model.games.Game;
import com.teammj.model.persons.base.Athlete;
import org.w3c.dom.Element;

import java.util.UUID;

/**
 * Cyclist subclass
 * @author Johan van Zyl
 * @author Michael Guida
 * @see Athlete
 */
public final class Cyclist extends Athlete {

    public Cyclist(String name, int age, DATA.STATE fromState, Element element,  UUID...uuids) {
        super(name, age, fromState, element, DATA.PERSON_TYPE.Cyclist, uuids);
        System.out.printf("Hello, my name is %s and I am %d years old. " +
                "I am also a Cyclist.\n", name, age);
    }

    /**
     * Compete in game method
     * @param game - game competing in
     * @return - time Cyclist spent cycling in game
     */
    @Override
    public int compete(Game game) {
        getGamesAttended().add(game);
        return 500 + Utilities.random.nextInt(300);
    }
}
