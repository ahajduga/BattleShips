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
     * @param length
     * @param row
     * @param col
     * @param direction - 0 left, 1 up, 2 right, 3 down
     * @return
     */
    public Boolean setNewShipInGame(
            Boolean board,
            Integer length,
            Integer row,
            Integer col,
            Integer direction
    ){
        Ship ship = new Ship();
        ship.setShip(board, length, row, col, direction);

        if(board){
            for(Ship s: boardLeft){
                if(s.isCollision(ship)){
                    return false;
                }
            }
            boardLeft.add(ship);
        } else {
            for(Ship s: boardRight){
                if(s.isCollision(ship)){
                    return false;
                }
            }
            boardRight.add(ship);
        }
        return true;
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
