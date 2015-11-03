package com.battleships.game;

import com.sun.xml.internal.xsom.impl.scd.Iterators;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by alex on 18.10.15.
 */
public class Game {

    private int pointsLeft;
    private int pointsRight;

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
     * @param row
     * @param col
     */
    public void setNewShipInGame(Boolean board, Integer row, Integer col){
        Ship ship = new Ship();
        ship.setShip(row, col);
        if(board){
            boardLeft.add(ship);
        } else {
            boardRight.add(ship);
        }
    }

    /**
     * @param board - 0 left, 1 right
     * @param ship - No.
     * @param row
     * @param col
     */
    public void setExistingShipInGame(Boolean board, Integer ship, Integer row, Integer col){
        if(board){
            boardLeft.get(ship).setShip(row, col);
        } else {
            boardRight.get(ship).setShip(row, col);
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
}
