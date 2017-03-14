package at.kingr.hearthstats.controller;

import at.kingr.hearthstats.model.Deck;
import at.kingr.hearthstats.model.DeckConstants;
import at.kingr.hearthstats.model.UserDeck;
import at.kingr.hearthstats.service.DataPool;
import at.kingr.hearthstats.service.DataService;
import at.kingr.hearthstats.service.DataStoreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static at.kingr.hearthstats.controller.FormElementGenerator.generateCustomGridButton;
import static at.kingr.hearthstats.controller.FormElementGenerator.generateCustomGridGrid;
import static at.kingr.hearthstats.controller.FormElementGenerator.generateCustomGridLabel;


public class PageController {

    @FXML
    public Button addNewMetaDeck;

    @FXML
    public TextField newMetaDeckName;

    @FXML
    public Button addNewUserDeck;

    @FXML
    public ComboBox<String> userDecksComboBox;

    @FXML
    private TextField newUserDeckName;

    @FXML
    private ComboBox<String> newUserDeckClassComboBox;

    @FXML
    public GridPane gridPane;

    @FXML
    public ComboBox<String> metaDeckClassComboBox;

    @FXML
    public Button deleteCurrentUserDeck;

    @FXML
    public Label overallWinRate;

    private UserDeck displayedDeck;

    public PageController() {
    }

    @FXML
    public void initialize() {
        DataStoreService.getInstance().loadData();

        // init class dropdowns
        ObservableList<String> classes = FXCollections.observableArrayList();
        for (DeckConstants.DeckClass deckClass : DeckConstants.DeckClass.values()) {
            classes.add(deckClass.toString());
        }
        metaDeckClassComboBox.setItems(classes);
        newUserDeckClassComboBox.setItems(classes);
        if (!classes.isEmpty()) {
            metaDeckClassComboBox.getSelectionModel().select(0);
            newUserDeckClassComboBox.getSelectionModel().select(0);
        }

        updateUserDeckComboBox();
        updateFrame();
    }

    public void updateFrame() {
        clearGrid();
        if (displayedDeck != null) {
            loadGridContent();
        } else {
            displayNoDeckSelected();
        }
        updateOverallWinRate();
    }

    public void updateOverallWinRate() {
        if (displayedDeck != null) {
            overallWinRate.setText("Winrate: " + String.format("%1$,.4f", displayedDeck.computeOverallWinRate()) + "%");
        } else {
            overallWinRate.setText("Winrate:");
        }
    }

    public void displayNoDeckSelected() {
        Label noDeckSelected = generateCustomGridLabel("No deck selected!");
        gridPane.add(noDeckSelected, 0, 1);
        gridPane.setRowSpan(noDeckSelected, 2);
    }

    public void clearGrid() {
        List<Node> nodesToDelete = new ArrayList<>();
        for (int counter = 0; counter < gridPane.getChildren().size(); counter++) {
            Node gridNode = gridPane.getChildren().get(counter);
            Pattern pattern = Pattern.compile("header_[0-9]");
            if (gridNode.getId() == null || !pattern.matcher(gridNode.getId()).matches()) {
                nodesToDelete.add(gridNode);
            }
        }
        gridPane.getChildren().removeAll(nodesToDelete);
    }

    private void loadGridContent() {
        List<Deck> metaDecks = DataPool.getInstance().getMetaDecks();
        for (int counter = 0; counter < metaDecks.size(); counter++) {
            Deck metaDeck = metaDecks.get(counter);
            addDeckToView(metaDeck, counter + 1);
        }
    }

    private void addDeckToView(Deck deck, int rowNumber) {
        // deck name
        gridPane.add(generateCustomGridLabel(deck.getName()), 0, rowNumber);

        // deck wins
        GridPane winsGrid = generateCustomGridGrid();
        Label deckName = generateCustomGridLabel(String.valueOf(displayedDeck.getWinMap().get(deck.getName())));
        winsGrid.add(deckName, 0, 0);
        Button incWins = generateCustomGridButton("+");
        incWins.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayedDeck.incWins(deck.getName());
                updateFrame();
            }
        });
        Button decWins = generateCustomGridButton("-");
        decWins.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayedDeck.decWins(deck.getName());
                updateFrame();
            }
        });
        winsGrid.add(incWins, 1, 0);
        winsGrid.add(decWins, 2, 0);
        gridPane.add(winsGrid, 1, rowNumber);

        // deck defeats
        GridPane defeatsGrid = generateCustomGridGrid();
        Label defeatsLabel = generateCustomGridLabel(String.valueOf(displayedDeck.getDefeatsMap().get(deck.getName())));
        defeatsGrid.add(defeatsLabel, 0, 0);
        Button incDefeats = generateCustomGridButton("+");
        incDefeats.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayedDeck.incDefeats(deck.getName());
                updateFrame();
            }
        });
        Button decDefeats = generateCustomGridButton("-");
        decDefeats.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayedDeck.decDefeats(deck.getName());
                updateFrame();
            }
        });
        defeatsGrid.add(incDefeats, 1, 0);
        defeatsGrid.add(decDefeats, 2, 0);
        gridPane.add(defeatsGrid, 2, rowNumber);

        // deck winrate
        double wins = displayedDeck.getWinMap().get(deck.getName());
        double defeats = displayedDeck.getDefeatsMap().get(deck.getName());
        double winRate = wins / (wins + defeats);
        if (Double.isNaN(winRate)) {
            winRate = 0.0;
        }
        gridPane.add(generateCustomGridLabel(String.format("%1$,.4f", winRate) + "%"), 3, rowNumber);

        // deck class
        gridPane.add(generateCustomGridLabel(deck.getDeckClass().toString()), 4, rowNumber);

        // delete meta deck button
        Button deleteButton = generateCustomGridButton("Delete");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DataPool.getInstance().removeMetaDeck(deck.getName());
                updateFrame();
            }
        });
        gridPane.add(deleteButton, 5, rowNumber);

        if(gridPane.getRowConstraints().size() < rowNumber) {
            gridPane.getRowConstraints().add(new RowConstraints());
        }
        gridPane.getRowConstraints().get(rowNumber - 1).setMinHeight(40);
    }


    public void updateUserDeckComboBox() {
        ObservableList<String> userDecks = FXCollections.observableArrayList();
        for (Deck userDeck : DataPool.getInstance().getUserDecks()) {
            userDecks.add(userDeck.getName());
        }
        userDecksComboBox.setItems(userDecks);
        if (!DataPool.getInstance().getUserDecks().isEmpty()) {
            if (userDecksComboBox.getSelectionModel().getSelectedIndex() == -1) {
                userDecksComboBox.getSelectionModel().select(0);
            }
        }
        displayedDeck = (UserDeck) DataPool.getInstance().getUserDeck(userDecksComboBox.getValue());
    }

    public void addNewMetaDeck(ActionEvent event) {
        DataService.addNewMetaDeck(newMetaDeckName.getText(), DeckConstants.DeckClass.valueOf(metaDeckClassComboBox.getValue()));
        updateFrame();
    }

    public void addNewUserDeck(ActionEvent event) {
        String newUserDeckName = this.newUserDeckName.getText();
        String newUserDeckClass = this.newUserDeckClassComboBox.getValue();
        DataService.addNewUserDeck(DeckConstants.DeckClass.valueOf(newUserDeckClass), newUserDeckName);
        updateUserDeckComboBox();
    }

    public void selectUserDeck(ActionEvent event) {
        String userDeckName = userDecksComboBox.getValue();
        if (userDeckName != null) {
            displayedDeck = (UserDeck) DataPool.getInstance().getUserDeck(userDeckName);
        }
        updateFrame();
    }

    public void deleteCurrentUserDeck(ActionEvent event) {
        if (displayedDeck == null) {
            return;
        }
        DataPool.getInstance().removeUserDeck(displayedDeck.getName());
        displayedDeck = null;
        updateUserDeckComboBox();
        updateFrame();
        DataStoreService.getInstance().storeData();
    }
}
