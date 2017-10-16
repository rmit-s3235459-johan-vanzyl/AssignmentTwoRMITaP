package com.teammj.model.persons;

import com.teammj.controller.Utilities;
import com.teammj.model.DATA;
import com.teammj.model.games.CyclingGame;
import com.teammj.model.games.Game;
import com.teammj.model.games.SprintGame;
import com.teammj.model.games.SwimmingGame;
import com.teammj.model.persons.base.Athlete;
import org.w3c.dom.Element;

import java.util.UUID;

/**
 * SuperAthlete subclass
 * @author Johan van Zyl
 * @author Michael Guida
 * @see Athlete
 * @see Cyclist
 * @see Sprinter
 * @see Swimmer
 */
public final class SuperAthlete extends Athlete {
    public SuperAthlete(String name, int age, DATA.STATE fromState, Element element, UUID... uuids) {
        super(name, age, fromState, element, DATA.PERSON_TYPE.SuperAthlete, uuids);
    }

    /**
     * Compete in game method
     * @param game - game competing in
     * @return - time athlete spent in game
     */
    public int compete(Game game) {
        int competingTime = 0;
        getGamesAttended().add(game);

        if (game instanceof SprintGame){
            competingTime = 10 + Utilities.random.nextInt(10);
        }
        if (game instanceof CyclingGame){
            competingTime = 500 + Utilities.random.nextInt(300);
        }
        if (game instanceof SwimmingGame){
            competingTime = 100 + Utilities.random.nextInt(100);
        }

        return competingTime;
    }
}
