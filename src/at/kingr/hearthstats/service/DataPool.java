package at.kingr.hearthstats.service;

import at.kingr.hearthstats.model.Deck;
import at.kingr.hearthstats.model.DeckConstants;
import at.kingr.hearthstats.model.UserDeck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by roli on 10.03.17.
 */
public class DataPool {
    private static DataPool INSTANCE;

    private List<Deck> metaDecks;

    private List<Deck> userDecks;

    private DataPool() {
        metaDecks = new ArrayList<>();
        userDecks = new ArrayList<>();
    }

    public Deck addMetaDeck(String name, DeckConstants.DeckClass deckClass) {
        Deck deck = new Deck(name, deckClass);
        metaDecks.add(deck);
        userDecks.stream().forEach(userDeck -> ((UserDeck)userDeck).addMapCounter(deck.getName()));
        return deck;
    }

    public Deck addMetaDeck(Deck deck) {
        metaDecks.add(deck);
        userDecks.stream().forEach(userDeck -> ((UserDeck)userDeck).addMapCounter(deck.getName()));
        return deck;
    }

    public void addAllMetaDecks(List<Deck> decks) {
        metaDecks.addAll(decks);
        for(Deck metadeck : decks) {
            userDecks.stream().forEach(userDeck -> ((UserDeck)userDeck).addMapCounter(metadeck.getName()));
        }
    }

    public List<Deck> getMetaDecks() {
        return metaDecks;
    }

    public void removeMetaDeck(String name) {
        for (int i = 0; i < metaDecks.size(); i++) {
            if (metaDecks.get(i).getName().equals(name)) {
                metaDecks.remove(i);
                return;
            }
        }
    }

    public void addUserDeck(DeckConstants.DeckClass deckClass, String userDeckName) {
        UserDeck deck = new UserDeck(deckClass, userDeckName);
        userDecks.add(deck);
    }

    public void addUserDeck(UserDeck userDeck) {
        userDecks.add(userDeck);
    }

    public void addAllUserDecks(List<Deck> decks) {
        userDecks.addAll(decks);
    }

    public List<Deck> getUserDecks() {
        return userDecks;
    }

    public Deck getUserDeck(String name) {
        List<Deck> selection = userDecks.stream()
                .filter(userdeck -> userdeck.getName().equals(name))
                .collect(Collectors.toList());
        if (selection != null && !selection.isEmpty()) {
            return selection.get(0);
        }
        return null;
    }

    public void removeUserDeck(String name) {
        for (int i = 0; i < userDecks.size(); i++) {
            if (userDecks.get(i).getName().equals(name)) {
                userDecks.remove(i);
                return;
            }
        }
    }

    public static DataPool getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataPool();
        }
        return INSTANCE;
    }

}
