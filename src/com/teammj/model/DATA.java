package com.teammj.model;

import com.teammj.controller.Utilities;

import java.util.*;

/**
 * ProgramData interface for console text, generating a random time states,
 * athlete types and names
 * and Referee names.
 * @author Johan van Zyl
 * @author Michael Guida
 */
public interface DATA {
    String POSTIVE_INTEGER_ONE_TO_NINE = "^([1-9][0-9]+|[1-9])$"; //REGEX
    String FULL_NAME = "^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}"; //REGEX

    String menuText = "Ozlympic Games\n" +
            "===================================\n" +
            "1.\tCreate Game\n" +
            "2.\tSelect Game {GAMEAVAIL}\n" +
            "3.\tAdd Athlete to Game {GAMEAVAIL}\n" +
            "4.\tAdd Referee to Game {GAMEAVAIL}\n" +
            "5.\tPredict the winner of the game {GAMEAVAIL}\n" +
            "6.\tStart the game {GAMEAVAIL}\n" +
            "7.\tDisplay the final results of all games {GAMEAVAIL}\n" +
            "8.\tDisplay the points of all persons\n" +
            "9.\tExit\n" +
            "\n" +
            "\n" +
            "Enter an option: _\n";

    String gameMenuText = "Ozlympic Games\n" +
            "===================================\n" +
            "1.\tSwimming\n" +
            "2.\tCycling\n" +
            "3.\tRunning\n" +
            "4.\tBack\n" +
            "\n" +
            "\n" +
            "Enter an option: _\n";

    String optionsMenu = "Ozlympic Games\n" +
            "===================================\n" +
            "{ENTRIES}" +
            "\n" +
            "\n" +
            "Enter an option: _\n";


    enum PERSON_TYPE {
        Swimmer, Cyclist, Sprinter, SuperAthlete, Referee
    }

    enum ATHLETE_TYPE {
        swimmer, cyclist, sprinter, superAthlete;
        private static final List<ATHLETE_TYPE> ATHLETE_TYPE_LIST = Collections.unmodifiableList(Arrays.asList(values()));

        public static ATHLETE_TYPE randomAthlete() {
            return ATHLETE_TYPE_LIST.get(Utilities.random.nextInt(ATHLETE_TYPE_LIST.size()));
        }
    }

    enum STATE {
        NSW, QLD, SA, TAS, VIC, WA;
        private static final List<STATE> STATE_LIST = Collections.unmodifiableList(Arrays.asList(values()));

        public static STATE randomState() {

            return STATE_LIST.get(Utilities.random.nextInt(STATE_LIST.size()));
        }

    }

    enum GAMETYPE {Swimming, Sprinting, Cycling}

    String[] athleteNames = {"Freddy Yeager", "Cathy Blackford", "Melisa Oros", "Elana Brannon", "Beryl Mojica",
            "Marquita Helms", "Kyle Cremer", "Jade Strachan", "Faviola Carmon", "Carey Lieberman",
            "Ramonita Cali", "Kiera Points", "Adrian Riffel", "Lonnie Chu", "Shaunte Orlandi", "Mallie Kell",
            "Samatha Varley", "Yong Gourley", "Assunta Foshee", "Susana Cox", "Daniell Pokorny",
            "Michaela Keniston", "Shizue Langevin", "Yasuko Rothfuss", "Loriann Points", "Beckie Carlsen", "Sallie Tullis",
            "Tawanda Schexnayder", "Serafina Colgan", "Broderick Nicolson"};
    String[] officialNames = {"Vina Shelman", "Starr Lade", "Eileen Grillo", "Maryjane Casady", "Hilaria Vigil",
            "John Masters", "Myra Stepney", "Beryl Victorino", "Leena Kaup", "Stepanie Charette", };

    String PERSONS = "persons";
    String ATHLETE = "athlete";
    String ATHLETES = "persons";
    String OFFICIALS = "officials";
    String REFEREES = "referees";
    String REFEREE = "referee";

    String UUID = "UUID";
    String NAME = "name";
    String AGE = "age";
    String STATE_S = "state";
    String GAME = "game";
    String GAMES = "games";
    String POINTS = "points";
    String PERSON = "person";
    String TIME = "time";

    String UNIQUEID = "uniqueID";
    String BEENRAN = "beenRan";
    String CYCLING = "cycling";
    String SPINTING = "sprinting";
    String SWIMMING = "swimming";

    String SWIMMERS = "swimmers";
    String SPRINTERS = "sprinters";
    String CYCLISTS = "cyclists";
    String SUPERATHLETES = "superAthletes";

    String OZLYMPICS = "ozlympics";


}