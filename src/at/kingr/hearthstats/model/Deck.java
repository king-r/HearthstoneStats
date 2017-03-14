package at.kingr.hearthstats.model;

/**
 * Created by roli on 10.03.17.
 */
public class Deck {
    private String name;

    private DeckConstants.DeckClass deckClass;


    public Deck(String name, DeckConstants.DeckClass deckClass) {
        this.name = name;
        this.deckClass = deckClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeckConstants.DeckClass getDeckClass() {
        return deckClass;
    }

    public void setDeckClass(DeckConstants.DeckClass deckClass) {
        this.deckClass = deckClass;
    }
}
