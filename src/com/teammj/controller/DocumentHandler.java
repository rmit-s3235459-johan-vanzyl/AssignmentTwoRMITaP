package com.teammj.controller;

import com.sun.istack.internal.Nullable;
import com.teammj.Ozlympic;
import com.teammj.model.DATA;
import com.teammj.model.games.CyclingGame;
import com.teammj.model.games.Game;
import com.teammj.model.games.SprintGame;
import com.teammj.model.games.SwimmingGame;
import com.teammj.model.persons.*;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Official;
import com.teammj.model.persons.base.Person;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.*;

public final class DocumentHandler {

    public static void setAttr(final String type, final String value, final Element source) {
        Attr attr = source.getAttributeNode(type);
        if (attr == null) {
            attr = source.getOwnerDocument().createAttribute(type);
            source.setAttributeNode(attr);
        }
        attr.setValue(value);
    }

    static Document generateSaveFile(@Nullable final Window window,
                                     final ObservableList<Athlete> athletes,
                                     final ObservableList<Official> officials,
                                     boolean addPersons,
                                     final ObservableList<Game>... games) {
        File outputFile;
        Document document = null;
        if (window != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Generate save file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", "*.xml"));

            outputFile = fileChooser.showSaveDialog(window);
            if (outputFile == null) return null;
        } else {
            outputFile = new File("AutoSave.xml");
            if (outputFile.exists()) {
                if (games.length > 0) {
                    return loadFromSavedFile(
                            null,
                            athletes,
                            officials,
                            games[0],
                            outputFile
                    );
                }
                return null;
            }
        }


        try {
            document = ((DocumentBuilderFactory.newInstance()).newDocumentBuilder()).newDocument();
            Element root = document.createElement(DATA.OZLYMPICS);
            document.appendChild(root);

            Element rootPersons = document.createElement(DATA.PERSONS);
            root.appendChild(rootPersons);

            Element rootAthletes = document.createElement(DATA.ATHLETES);
            rootPersons.appendChild(rootAthletes);

            Element rootSwimmers = document.createElement(DATA.SWIMMERS);
            rootAthletes.appendChild(rootSwimmers);

            Element rootSprinters = document.createElement(DATA.SPRINTERS);
            rootAthletes.appendChild(rootSprinters);

            Element rootCyclists = document.createElement(DATA.CYCLISTS);
            rootAthletes.appendChild(rootCyclists);

            Element rootSuperAthletes = document.createElement(DATA.SUPERATHLETES);
            rootAthletes.appendChild(rootSuperAthletes);

            if (addPersons) {
                for (String athleteName : DATA.athleteNames) {
                    DATA.ATHLETE_TYPE athleteType = DATA.ATHLETE_TYPE.randomAthlete();
                    Element element;
                    switch (athleteType) {
                        case swimmer:
                            element = document.createElement(DATA.ATHLETE_TYPE.swimmer.toString());
                            rootSwimmers.appendChild(element);
                            athletes.add(
                                    new Swimmer(athleteName, 18 + Utilities.random.nextInt(30), DATA.STATE.randomState(), element)
                            );
                            break;
                        case cyclist:
                            element = document.createElement(DATA.ATHLETE_TYPE.cyclist.toString());
                            rootCyclists.appendChild(element);
                            athletes.add(
                                    new Cyclist(athleteName, 18 + Utilities.random.nextInt(30), DATA.STATE.randomState(), element)
                            );
                            break;
                        case sprinter:
                            element = document.createElement(DATA.ATHLETE_TYPE.sprinter.toString());
                            rootSprinters.appendChild(element);
                            athletes.add(
                                    new Sprinter(athleteName, 18 + Utilities.random.nextInt(30), DATA.STATE.randomState(), element)
                            );
                            break;
                        case superAthlete:
                            element = document.createElement(DATA.ATHLETE_TYPE.superAthlete.toString());
                            rootSuperAthletes.appendChild(element);
                            athletes.add(
                                    new SuperAthlete(athleteName, 18 + Utilities.random.nextInt(30), DATA.STATE.randomState(), element)
                            );
                            break;
                    }
                }
            }


            Element rootOfficials = document.createElement(DATA.OFFICIALS);
            rootPersons.appendChild(rootOfficials);

            Element rootReferees = document.createElement(DATA.REFEREES);
            rootOfficials.appendChild(rootReferees);

            if (addPersons) {
                for (String refereeName : DATA.officialNames) {
                    Element referee = document.createElement(DATA.REFEREE);
                    rootReferees.appendChild(referee);
                    officials.add(
                            new Referee(refereeName, 18 + Utilities.random.nextInt(30), DATA.STATE.randomState(), referee)
                    );
                }
            }


            Element gamesRoot = document.createElement(DATA.GAMES);
            root.appendChild(gamesRoot);

            gamesRoot.appendChild(document.createElement(DATA.SWIMMING));
            gamesRoot.appendChild(document.createElement(DATA.SPINTING));
            gamesRoot.appendChild(document.createElement(DATA.CYCLING));

            //Write to file
            DOMSource source = new DOMSource(document);
            StreamResult streamResult = new StreamResult(outputFile);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, streamResult);


        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

        return document;
    }

    public static void saveGame(Document document, @Nullable final Window window, File... files) {
        File file;
        if (files.length > 0) {
            file = files[0];
        } else if (window != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose an save file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", "*.xml"));

            file = fileChooser.showSaveDialog(window);
            if (file == null) return;
        } else {
            return;
        }

        //Write to file
        DOMSource source = new DOMSource(document);
        StreamResult streamResult = new StreamResult(file);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2);

        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static Document loadFromSavedFile(@Nullable final Window window,
                                             final ObservableList<Athlete> athletes,
                                             final ObservableList<Official> officials,
                                             final ObservableList<Game> games, File... files) {
        File fileToLoad;
        Document document = null;
        if (files.length > 0) {
            fileToLoad = files[0];
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Ozlympic XML file to load");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", "*.xml"));

            File loadDirectory = new File(System.getProperty("user.dir") + "\\out\\production\\AssignmentTwoRMITaP");
            if (loadDirectory.exists()) {
                fileChooser.setInitialDirectory(loadDirectory);
            }

            fileToLoad = fileChooser.showOpenDialog(window);
            if (fileToLoad == null) return null;
        }

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(fileToLoad);
            document.getDocumentElement().normalize();

            // -- First, gather all persons

            //Swimmers
            NodeList nodeList = document.getElementsByTagName(DATA.ATHLETE_TYPE.swimmer.toString());
            athletes.addAll(parseAthletesFromNode(nodeList, DATA.ATHLETE_TYPE.swimmer));

            //Sprinters
            nodeList = document.getElementsByTagName(DATA.ATHLETE_TYPE.sprinter.toString());
            athletes.addAll(parseAthletesFromNode(nodeList, DATA.ATHLETE_TYPE.sprinter));

            //Cyclists
            nodeList = document.getElementsByTagName(DATA.ATHLETE_TYPE.cyclist.toString());
            athletes.addAll(parseAthletesFromNode(nodeList, DATA.ATHLETE_TYPE.cyclist));

            //SuperAthletes
            nodeList = document.getElementsByTagName(DATA.ATHLETE_TYPE.superAthlete.toString());
            athletes.addAll(parseAthletesFromNode(nodeList, DATA.ATHLETE_TYPE.superAthlete));

            //Referees
            nodeList = document.getElementsByTagName(DATA.REFEREE);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element root = (Element) nodeList.item(i);
                UUID uuid = UUID.fromString(root.getAttribute(DATA.UUID));
                String name = root.getAttribute(DATA.NAME);
                int age = Integer.parseInt(root.getAttribute(DATA.AGE));
                DATA.STATE state = parseState(root.getAttribute(DATA.STATE_S));

                Referee referee = new Referee(name, age, state, root, uuid);
                officials.add(referee);
            }

            // -- Second, load all games
            games.addAll(parseGames(document, athletes, officials));

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        return document;
    }

    private static DATA.STATE parseState(String state_s) {
        DATA.STATE state = null;
        switch (state_s) {
            case "NSW":
                state = DATA.STATE.NSW;
                break;
            case "QLD":
                state = DATA.STATE.QLD;
                break;
            case "SA":
                state = DATA.STATE.SA;
                break;
            case "TAS":
                state = DATA.STATE.TAS;
                break;
            case "VIC":
                state = DATA.STATE.VIC;
                break;
            case "WA":
                state = DATA.STATE.WA;
                break;

        }
        return state;
    }

    private static Map<Athlete, Integer> parseAthleteTimes(NodeList nodeList, ObservableList<Athlete> athletes) {
        Map<Athlete, Integer> athleteTimes = new HashMap<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            int time = Integer.parseInt(element.getAttribute(DATA.TIME));
            UUID uuid = UUID.fromString(element.getAttribute(DATA.UUID));

            Athlete athlete = athletes.stream()
                    .filter(a -> a.getUniqueID().equals(uuid))
                    .findAny()
                    .orElse(null);

            if (athlete != null) athleteTimes.put(athlete, time);
        }

        return athleteTimes;
    }

    private static ArrayList<Person> parsePersonsAttended(NodeList nodeList,
                                                          ObservableList<Athlete> athletes,
                                                          ObservableList<Official> officials) {
        ArrayList<Person> persons = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element root = (Element) nodeList.item(i);
            UUID uuid = UUID.fromString(root.getAttribute(DATA.UUID));
            Person person = athletes.stream()
                    .filter(athlete -> athlete.getUniqueID().equals(uuid))
                    .findAny()
                    .orElse(null);
            if (person != null) {
                persons.add(person);
            } else {
                person = officials.stream()
                        .filter(official -> official.getUniqueID().equals(uuid))
                        .findAny()
                        .orElse(null);
                if (person != null) persons.add(person);
            }
        }

        return persons;
    }

    private static Game parseGame(Element element,
                                  DATA.GAMETYPE gametype,
                                  ObservableList<Athlete> athletes,
                                  ObservableList<Official> officials) {
        Game game = null;
        switch (gametype) {
            case Cycling:
                game = new CyclingGame(element);
                break;
            case Swimming:
                game = new SwimmingGame(element);
                break;
            case Sprinting:
                game = new SprintGame(element);
                break;
        }

        // set UniqueID
        game.setUniqueID(element.getAttribute(DATA.UNIQUEID), true);

        // set been ran
        game.setHaveIbeenRan(element.getAttribute(DATA.BEENRAN).equals("true"), true);

        // get participants
        game.setParticipants(parsePersonsAttended(element.getElementsByTagName(DATA.PERSON), athletes, officials));

        // get athlete times
        game.setAthleteTimes(parseAthleteTimes(element.getElementsByTagName(DATA.ATHLETE), athletes));

        return game;
    }

    private static ArrayList<Game> parseGames(Document document,
                                              ObservableList<Athlete> athletes,
                                              ObservableList<Official> officials) {

        ArrayList<Game> games = new ArrayList<>();
        // Swimming Games
        Element root = (Element) document.getElementsByTagName(DATA.SWIMMING).item(0);
        NodeList nodeList = root.getElementsByTagName(DATA.GAME);
        for (int i = 0; i < nodeList.getLength(); i++) {
            games.add(parseGame((Element) nodeList.item(i), DATA.GAMETYPE.Swimming, athletes, officials));
        }
        // Running Games
        root = (Element) document.getElementsByTagName(DATA.SPINTING).item(0);
        nodeList = root.getElementsByTagName(DATA.GAME);
        for (int i = 0; i < nodeList.getLength(); i++) {
            games.add(parseGame((Element) nodeList.item(i), DATA.GAMETYPE.Sprinting, athletes, officials));
        }
        // Cycling Games
        root = (Element) document.getElementsByTagName(DATA.CYCLING).item(0);
        nodeList = root.getElementsByTagName(DATA.GAME);
        for (int i = 0; i < nodeList.getLength(); i++) {
            games.add(parseGame((Element) nodeList.item(i), DATA.GAMETYPE.Cycling, athletes, officials));
        }

        return games;
    }

    private static ArrayList<Athlete> parseAthletesFromNode(NodeList nodeList, DATA.ATHLETE_TYPE athleteType) {
        ArrayList<Athlete> athletes = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Athlete athlete = null;
            Element root = (Element) nodeList.item(i);
            UUID uuid = UUID.fromString(root.getAttribute(DATA.UUID));
            String name = root.getAttribute(DATA.NAME);
            int age = Integer.parseInt(root.getAttribute(DATA.AGE));
            DATA.STATE state = parseState(root.getAttribute(DATA.STATE_S));
            int points = Integer.parseInt(root.getAttribute(DATA.POINTS));

            switch (athleteType) {
                case superAthlete:
                    athlete = new SuperAthlete(name, age, state, root, uuid);
                    break;
                case sprinter:
                    athlete = new Sprinter(name, age, state, root, uuid);
                    break;
                case cyclist:
                    athlete = new Cyclist(name, age, state, root, uuid);
                    break;
                case swimmer:
                    athlete = new Swimmer(name, age, state, root, uuid);
                    break;
            }

            athlete.setPoints(points);
            athletes.add(athlete);
        }

        return athletes;
    }

    public static void updateAthleteMap(Map<Athlete, Integer> athleteTimes, Element element) {
        athleteTimes.forEach((athlete, time) -> {
            NodeList nodeList = element.getElementsByTagName(DATA.ATHLETE);
            Attr uuid;
            boolean found = false;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element ath = (Element) nodeList.item(i);
                uuid = ath.getAttributeNode(DATA.UUID);
                if (uuid != null) {
                    found = true;
                    setAttr(DATA.TIME, time.toString(), ath);
                    break;
                }
            }

            if (!found) {
                Element elementA = element.getOwnerDocument().createElement(DATA.ATHLETE);
                setAttr(DATA.UUID, athlete.getUniqueID().toString(), elementA);
                setAttr(DATA.TIME, time.toString(), elementA);
                element.appendChild(elementA);
            }
        });
    }

    static Element addCyclist(final Document document) {
        try {
            Element root = (Element) document.getElementsByTagName(DATA.CYCLISTS).item(0);
            Element cyclist = document.createElement(DATA.ATHLETE_TYPE.cyclist.toString());
            root.appendChild(cyclist);
            return cyclist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    static Element addSprinter(final Document document) {
        try {
            Element root = (Element) document.getElementsByTagName(DATA.SPRINTERS).item(0);
            Element sprinter = document.createElement(DATA.ATHLETE_TYPE.sprinter.toString());
            root.appendChild(sprinter);
            return sprinter;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static Element addSwimmer(final Document document) {
        try {
            Element root = (Element) document.getElementsByTagName(DATA.SWIMMERS).item(0);
            Element swimmer = document.createElement(DATA.ATHLETE_TYPE.swimmer.toString());
            root.appendChild(swimmer);
            return swimmer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static Element addSuperAthlete(final Document document) {
        try {
            Element root = (Element) document.getElementsByTagName(DATA.SUPERATHLETES).item(0);
            Element superAthlete = document.createElement(DATA.ATHLETE_TYPE.superAthlete.toString());
            root.appendChild(superAthlete);
            return superAthlete;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static Element addReferee(final Document document) {
        try {
            Element root = (Element) document.getElementsByTagName(DATA.REFEREES).item(0);
            Element referee = document.createElement(DATA.REFEREE);
            root.appendChild(referee);
            return referee;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
