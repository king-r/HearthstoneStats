package at.kingr.hearthstats.service;

import at.kingr.hearthstats.model.Deck;
import at.kingr.hearthstats.model.DeckConstants;
import at.kingr.hearthstats.model.UserDeck;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roli on 10.03.17.
 */
public class DataStoreService {
    public static DataStoreService INSTANCE;

    public static String JSON_FILENAME = "data.json";

    public static String METADECKSNAMES_JSON = "metadecknames";
    public static String METADECKS_JSON = "metadecks";
    public static String USERDECKS_JSON = "userdecks";
    public static String USERDECKNAMES_JSON = "userdecknames";
    public static String DECKS_NAME_JSON = "name";
    public static String DECKS_WINS_JSON = "wins";
    public static String DECKS_DEFEATS_JSON = "defeats";
    public static String DECKS_CLASS_JSON = "class";
    public static String DECKS_WINRATES_JSON = "winrates";


    public static DataStoreService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataStoreService();
        }
        return INSTANCE;
    }

    private DataStoreService() {

    }

    // loads all data from json into datapool
    public void loadData() {
        JSONParser parser = new JSONParser();
        try {
            FileReader fileReader = new FileReader(JSON_FILENAME);
            JSONObject object = (JSONObject) parser.parse(fileReader);
            // load meta decks
            JSONArray metaDeckNames = (JSONArray) object.get(METADECKSNAMES_JSON);
            JSONObject metaDecksObject = (JSONObject) object.get(METADECKS_JSON);
            DataPool.getInstance().addAllMetaDecks(extractDeckData(metaDeckNames, metaDecksObject));
            // load user decks
            JSONArray userDeckNames = (JSONArray) object.get(USERDECKNAMES_JSON);
            JSONObject userDecksObject = (JSONObject) object.get(USERDECKS_JSON);
            DataPool.getInstance().addAllUserDecks(extractDeckData(userDeckNames, userDecksObject));
        } catch (IOException | ParseException ex) {
            System.out.print(ex.toString());
        }
    }

    private List<Deck> extractDeckData(List<Object> deckNames, JSONObject decksObject) {
        List<Deck> decks = new ArrayList<>();
        if (deckNames != null) {
            for (Object deckName : deckNames) {
                String deck = (String) deckName;
                JSONObject deckObject = (JSONObject) decksObject.get(deck);
                UserDeck newDeck = new UserDeck(DeckConstants.DeckClass.valueOf((String) deckObject.get(DECKS_CLASS_JSON)), deck);
                decks.add(newDeck);
            }
        }
        return decks;
    }


    // stores all data contained by the datapool
    public void storeData() {
        JSONObject object = new JSONObject();

        // add meta deck names
        object = fillObjectWithDeckNames(object, METADECKSNAMES_JSON, DataPool.getInstance().getMetaDecks());
        // add meta deck data
        object = fillObjectWithDeckData(object, METADECKS_JSON, DataPool.getInstance().getMetaDecks());
        // add user deck names
        object = fillObjectWithDeckNames(object, USERDECKNAMES_JSON, DataPool.getInstance().getUserDecks());
        // add user deck data
        object = fillObjectWithDeckData(object, USERDECKS_JSON, DataPool.getInstance().getUserDecks());

        try {
            FileWriter fileWriter = new FileWriter(JSON_FILENAME);
            fileWriter.write(object.toString());
            fileWriter.close();
            System.out.print(object.toString());
        } catch (IOException ex) {
            System.out.print(ex.toString());
        }
    }

    private JSONObject fillObjectWithDeckNames(JSONObject object, String jsonDeckName, List<Deck> deckList) {
        for (Deck deck : deckList) {
            JSONArray deckArray = new JSONArray();
            deckArray.add(deck.getName());
            object.put(jsonDeckName, deckArray);
        }
        return object;
    }


    private JSONObject fillObjectWithDeckData(JSONObject object, String jsonDeckName, List<Deck> deckList) {
        JSONObject metadecks = new JSONObject();
        for (Deck deck : deckList) {
            JSONObject deckObject = new JSONObject();
            deckObject.put(DECKS_NAME_JSON, deck.getName());
            deckObject.put(DECKS_CLASS_JSON, deck.getDeckClass().toString());
            if(jsonDeckName.equals(USERDECKS_JSON)) {
               deckObject.put(DECKS_WINRATES_JSON, createWinRatesObject(deck));
            }
            metadecks.put(deck.getName(), deckObject);
        }
        object.put(jsonDeckName, metadecks);
        return object;
    }

    private JSONObject createWinRatesObject(Deck deck) {
        UserDeck userDeck = (UserDeck) deck;
        JSONObject object = new JSONObject();
        for(Deck metaDeck : DataPool.getInstance().getMetaDecks()) {
            JSONObject statsObject = new JSONObject();
            statsObject.put(DECKS_WINS_JSON, userDeck.getWinMap().get(metaDeck.getName()));
            statsObject.put(DECKS_DEFEATS_JSON, userDeck.getDefeatsMap().get(metaDeck.getName()));
            object.put(metaDeck.getName(), statsObject);
        }
        return object;
    }
}
