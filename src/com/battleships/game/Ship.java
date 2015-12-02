package com.battleships.game;

import java.util.Arrays;

import com.battleships.utils.Direction;

/**
 * Created by alex on 18.10.15.
 */

public class Ship {

    private final int BOARD_SIZE = 10;

    private int mastCount;

    private boolean[][] shipOriginal;
    private boolean[][] ship;

    public Ship(int mastCount){
        this.mastCount = mastCount;
        shipOriginal = new boolean[BOARD_SIZE][BOARD_SIZE];
        ship =         new boolean[BOARD_SIZE][BOARD_SIZE];
    }

    public Ship(boolean[][] ship){
        this.ship = ship;
        this.shipOriginal = ship;
    }

    public boolean setShip(Integer length, Integer row, Integer col, Direction direction){

        switch (direction){
            case LEFT:
                for (int i=0; i<length; i++){
                    if(col-i<0){
                        return false;
                    }
                    ship[row][col-i] = true;
                    shipOriginal[row][col-i] = true;
                }
                break;
            case UP:
                for (int i=0; i<length; i++){
                    if(row-i<0){
                        return false;
                    }
                    ship[row-i][col] = true;
                    shipOriginal[row-i][col] = true;
                }
                break;
            case RIGHT:
                for (int i=0; i<length; i++){
                    if(col+i>=BOARD_SIZE){
                        return false;
                    }
                    ship[row][col+i] = true;
                    shipOriginal[row][col+i] = true;
                }
                break;
            case DOWN:
                for (int i=0; i<length; i++){
                    if(row+i>=BOARD_SIZE){
                        return false;
                    }
                    ship[row+i][col] = true;
                    shipOriginal[row+i][col] = true;
                }
                break;
        }
        return true;
    }

    public Boolean isHit(Integer col, Integer row){
        if(ship[row][col]){
            ship[row][col] = false;
            return true;
        } else {
            return false;
        }
    }

    public Boolean isSank(){
        if(!Arrays.asList(ship).contains(true)){
            return true;
        } else {
            return false;
        }
    }

    public Boolean isCollision(Ship ship){
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (ship.shipOriginal[i][j] == true)
                    if(this.shipOriginal[i][j] == true
                            || (i + 1 < BOARD_SIZE && this.shipOriginal[i + 1][j] == true)
                            || (j + 1 < BOARD_SIZE && this.shipOriginal[i][j + 1] == true)
                            || (i + 1 < BOARD_SIZE && j + 1 < BOARD_SIZE && this.shipOriginal[i + 1][j + 1] == true)
                            || (i - 1 >= 0         && this.shipOriginal[i - 1][j] == true)
                            || (j - 1 >= 0         && this.shipOriginal[i][j - 1] == true)
                            || (i - 1 >= 0 && j - 1 >= 0         && this.shipOriginal[i - 1][j - 1] == true)
                            || (j - 1 >= 0 && i + 1 < BOARD_SIZE && this.shipOriginal[i + 1][j - 1] == true)
                            || (i - 1 >= 0 && j + 1 < BOARD_SIZE && this.shipOriginal[i - 1][j + 1] == true)) {
                        return true;
                    }
            }
        }
        return false;
    }

    public Boolean isPlaceAndSurrFree(Integer row, Integer col){
        if(this.ship[row][col] == true
                || (row + 1 < BOARD_SIZE && this.ship[row + 1][col] == true)
                || (col + 1 < BOARD_SIZE && this.ship[row][col + 1] == true)
                || (row + 1 < BOARD_SIZE && col + 1 < BOARD_SIZE && this.ship[row + 1][col + 1] == true)
                || (row - 1 >= 0         && this.ship[row - 1][col] == true)
                || (col - 1 >= 0         && this.ship[row][col - 1] == true)
                || (row - 1 >= 0 && col - 1 >= 0         && this.ship[row - 1][col - 1] == true)
                || (col - 1 >= 0 && row + 1 < BOARD_SIZE && this.ship[row + 1][col - 1] == true)
                || (row - 1 >= 0 && col + 1 < BOARD_SIZE && this.ship[row - 1][col + 1] == true)) {
            return false;
        } else {
            return true;
        }
    }

    public int getMastCount() {
        return mastCount;
    }

    public boolean[][] setOnRandomBoard(boolean[][] board){
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(this.shipOriginal[i][j] == true){
                    board[i][j] = true;
                }
            }
        }
        return board;
    }

    public boolean[][] getShipOriginal() {
        return shipOriginal;
    }

    public boolean[][] getShip() {
        return ship;
    }
}
