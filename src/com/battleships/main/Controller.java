package com.battleships.main;

import com.battleships.game.Game;
import com.battleships.utils.*;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;


public class Controller implements Initializable {
    private static final Color HIGHLIGHT_COLOR = Color.color(255d / 255d, 170d / 255d, 172d / 255d);
    private static final Color FILL_COLOR = Color.RED;
    private static final Color INIT_FIELD_COLOR = Color.BLACK;
    private static final Color MISSED_COLOR = Color.LIGHTGRAY;
    private static final Color PLAYER_HIT_COLOR = HIGHLIGHT_COLOR;
    private static final Color AI_HIT_COLOR = Color.PURPLE;
    private static final Color PLAYER_SANK_COLOR = FILL_COLOR;
    private static final int CELL_SIZE = 25;
    @FXML
    private GridPane boardLeft;
    @FXML
    private GridPane boardRight;
    @FXML
    private Label mast1Field;
    @FXML
    private Label mast2Field;
    @FXML
    private Label mast3Field;
    @FXML
    private Label mast4Field;
    @FXML
    private Button startButton;

    private Game gameInstance;
    private Label currentLabel;
    private Field currentField;
    private Map<Label, Integer> shipMap = new LinkedHashMap<>();
    private List<Field> shadows = new ArrayList<>();
    private Direction currDir;
    private State currentState = State.PLACING;
    private Field currentShot = null;

    private void initShipMap() {
        shipMap.put(mast1Field, 1);
        shipMap.put(mast2Field, 2);
        shipMap.put(mast3Field, 3);
        shipMap.put(mast4Field, 4);
    }

    private void initBoards() {
        for (int i = 0; i < boardLeft.getRowConstraints().size(); i++) {
            for (int j = 0; j < boardLeft.getColumnConstraints().size(); j++) {

                Field field = new Field(CELL_SIZE - 1, INIT_FIELD_COLOR, new Coords(i, j));
                field.setOnMouseClicked(event -> {
                    if (currentState == State.SHOOTING) return;
                    Field field1 = (Field) event.getSource();
                    handleOwnBoardClick(field1);
                });
                Field field2 = new Field(CELL_SIZE - 1, INIT_FIELD_COLOR, new Coords(i, j));
                field2.setOnMouseClicked(event -> {
                    if (currentState == State.PLACING) return;
                    Field field1 = (Field) event.getSource();
                    handleEnemyBoardClick(field1);
                });
                boardLeft.add(field, i, j);
                boardRight.add(field2, i, j);
            }
        }
    }

    private void initFields() {
        int mast1Left = gameInstance.getRemainingShipsCount(false, 1);
        int mast2Left = gameInstance.getRemainingShipsCount(false, 2);
        int mast3Left = gameInstance.getRemainingShipsCount(false, 3);
        int mast4Left = gameInstance.getRemainingShipsCount(false, 4);
        mast1Field.setText("1m, Left: " + mast1Left);
        mast2Field.setText("2m, Left: " + mast2Left);
        mast3Field.setText("3m, Left: " + mast3Left);
        mast4Field.setText("4m, Left: " + mast4Left);
    }

    private void updateFields() {
        int mast1Left = gameInstance.getRemainingShipsCount(false, 1);
        int mast2Left = gameInstance.getRemainingShipsCount(false, 2);
        int mast3Left = gameInstance.getRemainingShipsCount(false, 3);
        int mast4Left = gameInstance.getRemainingShipsCount(false, 4);

        mast1Field.setText("1m, Left: " + mast1Left);
        if (mast1Left == 0) mast1Field.setDisable(true);
        mast2Field.setText("2m, Left: " + mast2Left);
        if (mast2Left == 0) mast2Field.setDisable(true);
        mast3Field.setText("3m, Left: " + mast3Left);
        if (mast3Left == 0) mast3Field.setDisable(true);
        mast4Field.setText("4m, Left: " + mast4Left);
        if (mast4Left == 0) mast4Field.setDisable(true);
        if (mast1Left + mast2Left + mast3Left + mast4Left != 0) { //TODO ==
            startButton.setDisable(false);
            startButton.setOnMouseClicked(event -> {
                currentState = State.SHOOTING;
                mast1Field.setDisable(true);
                mast2Field.setDisable(true);
                mast3Field.setDisable(true);
                mast4Field.setDisable(true);
                System.out.println("started");
                gameInstance.start();

            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameInstance = new Game();
        initBoards();
        initShipMap();
        initFields();
    }

    private void beginPlacing(Field field) {

        currentField.setFill(HIGHLIGHT_COLOR);
        boardLeft.setOnMouseMoved(event -> {
            clearShadows();
            currDir = getDirection(event);

            updateFields();
            if (areBoundsValid(shipMap.get(currentLabel), field, currDir)) {
                if (gameInstance.isPlacementPossible(true, shipMap.get(currentLabel), currentField.getCoords().y, currentField.getCoords().x, currDir)) {
                    drawShadows();
                } else shadows.clear();
            }


        });
    }

    private void fillShadows() {
        for (Field f : shadows) {
            f.setFill(FILL_COLOR);
            f.setColor(FILL_COLOR);
        }
        shadows.clear();
    }

    private void clearShadows() {
        for (Field f : shadows) {
            f.setFill(INIT_FIELD_COLOR);
            f.setColor(INIT_FIELD_COLOR);
        }
        shadows.clear();
    }

    private void drawShadows() {
        for (Field f : shadows) {
            f.setFill(HIGHLIGHT_COLOR);
            f.setColor(HIGHLIGHT_COLOR);
        }
    }

    private boolean areBoundsValid(int mast, Field field, Direction currDir) {

        int x = field.getCoords().x;
        int y = field.getCoords().y;
        for (int i = 1; i < mast; i++) {
            if (currDir == Direction.UP) {
                if (y - i < 0) return false;
                shadows.add(getField(x, y - i));
            } else if (currDir == Direction.DOWN) {
                if (y + i > 9) return false;
                shadows.add(getField(x, y + i));
            } else if (currDir == Direction.LEFT) {
                if (x - i < 0) return false;
                shadows.add(getField(x - i, y));
            } else if (currDir == Direction.RIGHT) {
                if (x + i > 9) return false;
                shadows.add(getField(x + i, y));
            }
        }
        return true;


    }

    private Field getField(int y, int x) {
        Node result = null;
        ObservableList<Node> childrens = boardLeft.getChildren();
        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == null || GridPane.getColumnIndex(node) == null) continue;
            if (boardLeft.getRowIndex(node) == x && boardLeft.getColumnIndex(node) == y) {
                result = node;
                break;
            }
        }
        return (Field) result;
    }


    private Direction getDirection(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        double fieldX = currentField.getCoords().x * CELL_SIZE + CELL_SIZE / 2;
        double fieldY = currentField.getCoords().y * CELL_SIZE + CELL_SIZE / 2;
        if (Math.abs(mouseX - fieldX) > Math.abs(mouseY - fieldY)) {
            return mouseX - fieldX > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            return mouseY - fieldY > 0 ? Direction.DOWN : Direction.UP;
        }
    }

    private void clearSelection() {
        updateFields();
        boardLeft.setOnMouseMoved(null);
        currentField.setFill(Color.RED);
        currentField = null;
        currentLabel.setStyle("-fx-background-color:white;");
        currentLabel = null;
    }

    private void handleEnemyBoardClick(Field field) {
     //   if (gameInstance.getCurrentPlayer() != Player.HUMAN) return;
        if (field.getColor() != INIT_FIELD_COLOR) return;
        if (gameInstance.getEffect(field.getCoords()) == Effect.MISSED) {
            System.out.println("missed");
            field.setColor(MISSED_COLOR);
            List<Coords> coords = gameInstance.getAIMove();
            for(Coords c : coords){
                if(getField(c.y,c.x).getColor()==FILL_COLOR)
                    getField(c.y,c.x).setColor(AI_HIT_COLOR);
                else getField(c.y,c.x).setColor(MISSED_COLOR);
            }
        } else if (gameInstance.getEffect(field.getCoords()) == Effect.HIT) {
            System.out.println("hit");
            field.setColor(HIGHLIGHT_COLOR);
        } else {
            System.out.println("sink");
            sink(gameInstance.getShipArray());
        }

    }


    private void sink(List<Coords> coords) {
        for (Coords c : coords) {
            getField(c.y, c.x).setFill(FILL_COLOR);
        }
    }

    private void handleOwnBoardClick(Field field) {
        if (currentLabel == null)
            return;
        if (currentField == null) {

            if (gameInstance.isPlaceAndSurrFree(true, field.getCoords().y, field.getCoords().x)) {
                currentField = field;
                if (shipMap.get(currentLabel) == 1) {
                    gameInstance.setNewShipInGame(true, 1, currentField.getCoords().y, currentField.getCoords().x, Direction.DOWN);
                    clearSelection();

                } else
                    beginPlacing(field);
            }
        } else {
            if (shadows.size() != 0) {
                currentField.setFill(Color.RED);
                gameInstance.setNewShipInGame(true, shipMap.get(currentLabel), currentField.getCoords().y, currentField.getCoords().x, currDir);
                clearSelection();
                fillShadows();

            }
        }


    }


    public void shipSelected(Event event) {
        clearShadows();
        if (currentField != null) currentField.setFill(INIT_FIELD_COLOR);
        if (currentLabel != null)
            currentLabel.setStyle("-fx-background-color:white;");
        currentLabel = (Label) event.getSource();

        currentLabel.setStyle("-fx-background-color:yellow;");
        currentField = null;
        boardLeft.setOnMouseMoved(null);
    }


}
