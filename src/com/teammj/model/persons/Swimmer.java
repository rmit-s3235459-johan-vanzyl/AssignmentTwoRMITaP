package com.teammj.model.persons;

import com.teammj.controller.Utilities;
import com.teammj.model.DATA;
import com.teammj.model.games.Game;
import com.teammj.model.persons.base.Athlete;
import org.w3c.dom.Element;

import java.util.UUID;

/**
 * Swimmer subclass
 * @author Johan van Zyl
 * @author Michael Guida
 * @see Athlete
 */
public final class Swimmer extends Athlete {
    public Swimmer(String name, int age, DATA.STATE fromState, Element element, UUID... uuids) {
        super(name, age, fromState, element, DATA.PERSON_TYPE.Swimmer, uuids);
    }

    /**
     * Compete in game method
     * @param game - game competing in
     * @return - time Swimmer spent swimming in game
     */
    @Override
    public int compete(Game game) {
        getGamesAttended().add(game);
        return 100 + Utilities.random.nextInt(100);
    }

}
