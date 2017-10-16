package com.teammj.model.persons;

import com.teammj.model.DATA;
import com.teammj.model.games.Game;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Official;
import org.w3c.dom.Element;

import java.util.Map;
import java.util.UUID;

/**
 * Referee sub class
 * @author Johan van Zyl
 * @author Michael Guida
 * @see Official
 */

public final class Referee extends Official {
    public Referee(String name, Integer age, DATA.STATE fromState, Element element, UUID... uuid) {
        super(name, age, fromState, element, DATA.PERSON_TYPE.Referee, uuid);
        System.out.printf("Hello, my name is %s and I am %d years old." +
                " I am also a Referee.\n", name, age);
    }

    /**
     * Displays game results and gives
     * out points to top 3 placers
     */
    public void printAthleteTimes(Map<Athlete, Integer> times)  {
        System.out.println("\nThe athlete times are");
        int athletePosition = 1;
        Athlete winner;
        for (Map.Entry<Athlete, Integer> entry : times.entrySet()){
            System.out.println(entry.getValue() + " seconds for " + entry.getKey().getName());
            switch (athletePosition){
                case 1:
                    winner = entry.getKey();
                    winner.addPoints(5);
                    break;
                case 2:
                    entry.getKey().addPoints(2);
                    break;
                case 3:
                    entry.getKey().addPoints(1);
                    break;
            }

            athletePosition++;
        }
    }

    /**
     * Checks if there is the correct
     * number of players
     */
    public String evaluateGame(Game game) {
        String reason = null;
        int players = game.getParticipants().size() - 1;
        if (players < 4) {
            reason = "Not enough players! You need " + (4 - players) + " more players!";
        }
        if (players > 8) {
            reason = "Too many players! You need kick out " + (players - 8) + " players!";
        }
        game.setHaveIbeenRan(true, false);
        return reason;
    }

}
