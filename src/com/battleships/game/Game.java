package com.battleships.game;

//import com.sun.xml.internal.xsom.impl.scd.Iterators;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by alex on 18.10.15.
 */
public class Game {

    private int pointsLeft;
    private int pointsRight;

    //Ships count: 4x1, 3x2, 3x2, 1x4
    private int SINGLE_MAST_SHIPS = 4;
    private int DUOBLE_MAST_SHIPS = 3;
    private int TRIPLE_MAST_SHIPS = 2;
    private int QUAD_MAST_SHIPS   = 1;

    private ArrayList<Ship> boardLeft;
    private ArrayList<Ship> boardRight;

    public Game(){
        pointsLeft  = 0;
        pointsRight = 0;

        boardLeft  = new ArrayList<>();
        boardRight = new ArrayList<>();
    }

    /**
     * @param board - 0 left, 1 right
     * @param length
     * @param row
     * @param col
     * @param direction - 0 left, 1 up, 2 right, 3 down
     * @return
     */
    public Boolean isPlacementPossible(Boolean board, Integer length, Integer row, Integer col, Integer direction){
        Ship ship = new Ship(length);System.out.println("row: " + row + " col: " + col);
        if(!ship.setShip(board, length, row, col, direction)){
            return false;
        }

        if(board){
            for(Ship s: boardLeft){
                if(s.isCollision(ship)){
                    return false;
                }
            }
            return true;
        } else {
            for(Ship s: boardRight){
                if(s.isCollision(ship)){
                    return false;
                }
            }
            return true;
        }

    }
    public void setNewShipInGame(
            Boolean board,
            Integer length,
            Integer row,
            Integer col,
            Integer direction
    ) {
        if (isPlacementPossible(board, length, row, col, direction)) {
            Ship ship = new Ship(length);
            ship.setShip(board, length, row, col, direction);
            if (board == true)
                boardLeft.add(ship);
            boardRight.add(ship);

        }

    }

    /**
     * @param board - 0 left, 1 right
     * @param mastCount
     * @return
     */
    public Integer getRemainingShipsCount(Boolean board, Integer mastCount){

        int defaultShipCount = 0;

        switch (mastCount){
            case 1:
                defaultShipCount = SINGLE_MAST_SHIPS;
                break;
            case 2:
                defaultShipCount = DUOBLE_MAST_SHIPS;
                break;
            case 3:
                defaultShipCount = TRIPLE_MAST_SHIPS;
                break;
            case 4:
                defaultShipCount = QUAD_MAST_SHIPS;
                break;
        }

        if(board){
            return defaultShipCount - (int) boardLeft.stream()
                    .filter((ship) -> ship.getMastCount() == mastCount).count();
        } else {
            return defaultShipCount - (int) boardRight.stream()
                    .filter((ship) -> ship.getMastCount() == mastCount).count();
        }
    }

    /**
     * @param board - 0 left, 1 right
     * @param row
     * @param col
     * @return
     */
    public Boolean isPlaceAndSurrFree(Boolean board, Integer row, Integer col){
        if(board){
            for(Ship s: boardLeft){
                if(!s.isPlaceAndSurrFree(row, col)){
                    return false;
                }
            }
            return true;
        } else {
            for(Ship s: boardRight){
                if(!s.isPlaceAndSurrFree(row, col)){
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * @param board - 0 left, 1 right
     */
    public void eraseBoard(Boolean board){
        if(board){
            boardLeft.clear();
        } else {
            boardRight.clear();
        }
    }

    /**
     * @param board - 0 left, 1 right
     * @param row
     * @param col
     * @return
     */
    public Boolean makeTurn(Boolean board, Integer row, Integer col){
        if(board){
            for(Ship ship : boardRight){
                if(ship.isHit(row, col)){
                    pointsLeft++;
                    return true;
                }
            }
        } else {
            for(Ship ship : boardLeft){
                if(ship.isHit(row, col)){
                    pointsRight++;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param board - 0 left, 1 right
     * @return
     */
    public Boolean isGameOver(Boolean board){
        if(board){
            for(Ship ship : boardRight){
                if(!ship.isSank()){
                    return false;
                }
            }
        } else {
            for(Ship ship : boardLeft){
                if(!ship.isSank()){
                    return false;
                }
            }
        }
        return true;
    }

    public int getPointsLeft() {
        return pointsLeft;
    }

    public int getPointsRight() {
        return pointsRight;
    }
}
