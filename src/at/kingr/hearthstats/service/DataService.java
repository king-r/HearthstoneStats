package at.kingr.hearthstats.service;

import at.kingr.hearthstats.model.DeckConstants;

/**
 * Created by roli on 12.03.17.
 */
public class DataService {

    public static void addNewUserDeck(DeckConstants.DeckClass deckClass, String deckName) {
        DataPool.getInstance().addUserDeck(deckClass, deckName);
        DataStoreService.getInstance().storeData();
    }

    public static void addNewMetaDeck(String deckName, DeckConstants.DeckClass deckClass) {
        DataPool.getInstance().addMetaDeck(deckName, deckClass);
        DataStoreService.getInstance().storeData();
    }
}
