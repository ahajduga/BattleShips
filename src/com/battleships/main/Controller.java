package com.battleships.main;

import com.battleships.game.Game;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;

/**
 * Created by Adam on 2015-10-16.
 */

enum Direction {
    LEFT,UP,RIGHT,DOWN
}

public class Controller implements Initializable {
    private final Color highlightColor = Color.color(255d/255d,170d/255d,172d/255d);
    @FXML
    private GridPane boardLeft;
    @FXML
    private GridPane boardRight;

    private Game gameInstance;
    private Label currentLabel;
    private Field currentField;
    private List<Rectangle> tempFields = new ArrayList<>();
    @FXML
    private Label mast1Field;
    @FXML
    private Label mast2Field;
    @FXML
    private Label mast3Field;
    @FXML
    private Label mast4Field;

    private Map<Label, Integer> shipMap = new LinkedHashMap<>();
    private List<Field> shadows = new ArrayList<>();
    private Direction currDir;

    private void initShipMap() {
        shipMap.put(mast1Field, 1);
        shipMap.put(mast2Field, 2);
        shipMap.put(mast3Field, 3);
        shipMap.put(mast4Field, 4);
    }

    private void initBoards() {
        for (int i = 0; i < boardLeft.getRowConstraints().size(); i++) {
            for (int j = 0; j < boardLeft.getColumnConstraints().size(); j++) {
                Field field = new Field(24, Color.GREY, i, j);
                field.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        Field field = (Field) event.getSource();
                        handleBoardClick(field);
                    }
                });
                Field field2 = new Field(24, Color.GREY, i, j);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameInstance = new Game();
        initBoards();
        initShipMap();
        initFields();
    }

    private void beginPlacing(Field field) {
        currentField = field;
        currentField.setFill(highlightColor);
        boardLeft.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(shadows.size() != 0){
                    currentField.setFill(Color.RED);
                    gameInstance.setNewShipInGame(true,shipMap.get(currentLabel),currentField.getxCell(),currentField.getyCell(),currDir.ordinal());
                    currentField = null;
                    currentLabel.setStyle("-fx-background-color:white;");
                    currentLabel = null;
                    boardLeft.setOnMouseClicked(null);
                    boardLeft.setOnMouseMoved(null);
                    fillShadows();
                }
            }
        });
        boardLeft.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearShadows();
                currDir = getDirection(event);

                    if(areBoundsValid(shipMap.get(currentLabel),field,currDir)){
                       if( gameInstance.isPlacementPossible(true,shipMap.get(currentLabel),currentField.getxCell(),currentField.getyCell(),currDir.ordinal())){
                           drawShadows();
                       }
                    }

               // System.out.println(gameInstance.isPlacementPossible(true,shipMap.get(currentLabel),currentField.getxCell(),currentField.getyCell(),currDir.ordinal()));


            }
        });
    }
    private void fillShadows(){
        for (Field f : shadows) {
            f.setFill(Color.RED);
        }
        shadows.clear();
    }
    private void clearShadows() {
        for (Field f : shadows) {
            f.setFill(Color.BLACK);
        }
        shadows.clear();
    }
    private void drawShadows(){
        for(Field f : shadows){
            f.setFill(highlightColor);
        }
    }
    private boolean areBoundsValid(int mast, Field field, Direction currDir){

        int x = currentField.getxCell();
        int y = currentField.getyCell();
        for(int i=1;i<mast;i++){
            if(currDir == Direction.UP){
                if(y-i < 0) return false;
                shadows.add(getField(x,y-i));
            }
            else if(currDir == Direction.DOWN){
                if(y+i > 9) return false;
                shadows.add(getField(x,y+i));
            }
            else if(currDir == Direction.LEFT){
                if(x-i < 0) return false;
                shadows.add(getField(x-i,y));
            }
            else if(currDir == Direction.RIGHT){
                if(x+i > 9) return false;
                shadows.add(getField(x+i,y));
            }
        }
        return true;


    }
    private Field getField(int y, int x){
        Node result= null;
        ObservableList<Node> childrens = boardLeft.getChildren();
        for(Node node: childrens) {
            if(GridPane.getRowIndex(node) == null || GridPane.getColumnIndex(node) == null) continue;
            if(boardLeft.getRowIndex(node) == x && boardLeft.getColumnIndex(node) == y) {
                result = node;
                break;
            }
        }
        return (Field)result;
    }


    private Direction getDirection(MouseEvent event){
        double mouseX = event.getX();
        double mouseY = event.getY();
        double fieldX = currentField.getxCell()*25+12;
        double fieldY = currentField.getyCell()*25+12;
//        System.out.println(mouseX + ", " + mouseY);
//        System.out.println(fieldX + ", " + fieldY);
        if(Math.abs(mouseX-fieldX) > Math.abs(mouseY-fieldY) ){
           return  mouseX-fieldX > 0 ? Direction.RIGHT : Direction.LEFT;
        }
        else{
            return mouseY-fieldY> 0 ? Direction.DOWN : Direction.UP;
        }
    }
    private void handleBoardClick(Field field) {
        if(currentLabel == null)
            return;
        if(currentField == null ){
            currentField = field;
            beginPlacing(field);
        }

            if (currentLabel != null) {
                currentField.setFill(Color.color(255d / 255d, 170d / 255d, 172d / 255d));
//                boardLeft.setOnMouseMoved(new EventHandler<MouseEvent>() {
//
//                    @Override
//                    public void handle(MouseEvent event) {
//                        tempFields.clear();
//                        if (event.getX() > currentField.getX() {
//                            if (event.getY() > currentField.getY()) {
//                     //           fillShip(shipMap.get(currentLabel), Direction.UP);
//                            }
//
//                        }
//                    }
//                });
            } else return;
        }


    private void fillShip(Integer integer, Direction up) {
        for (int i = 0; i < integer; i++) {
            //  getRect(boardLeft.get)
        }
    }


    public void shipSelected(Event event) {
        clearShadows();
        if(currentField != null)currentField.setFill(Color.BLACK);
        if (currentLabel != null)
            currentLabel.setStyle("-fx-background-color:white;");
        currentLabel = (Label) event.getSource();

        currentLabel.setStyle("-fx-background-color:yellow;");
        currentField = null;
        boardLeft.setOnMouseMoved(null);
    }

    public void cellSelected(Event event) {
        //    event.getSource().
    }
}
