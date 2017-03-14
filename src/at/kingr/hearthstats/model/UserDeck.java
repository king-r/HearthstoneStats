package at.kingr.hearthstats.model;

import at.kingr.hearthstats.service.DataPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by roli on 10.03.17.
 */
public class UserDeck extends Deck {

    Map<String, Integer> winMap;
    Map<String, Integer> defeatsMap;

    public UserDeck(DeckConstants.DeckClass deckClass, String name) {
        super(name, deckClass);
        winMap = new HashMap<>();
        defeatsMap = new HashMap<>();
        DataPool.getInstance().getMetaDecks()
                .stream().forEach(metaDeck -> winMap.put(metaDeck.getName(), 0));
        DataPool.getInstance().getMetaDecks()
                .stream().forEach(metaDeck -> defeatsMap.put(metaDeck.getName(), 0));
    }

    public void incWins(String name) {
        winMap.put(name, winMap.get(name) + 1);
    }

    public void incDefeats(String name) {
        defeatsMap.put(name, defeatsMap.get(name) + 1);
    }

    public void decWins(String name) {
        winMap.put(name, winMap.get(name) - 1);
    }

    public void decDefeats(String name) {
        defeatsMap.put(name, defeatsMap.get(name) - 1);
    }

    public Map<String, Integer> getWinMap() {
        return winMap;
    }

    public Map<String, Integer> getDefeatsMap() {
        return defeatsMap;
    }

    public void addMapCounter(String deckname) {
        winMap.put(deckname, 0);
        defeatsMap.put(deckname, 0);
    }

}
