package com.battleships.game;

import java.util.Arrays;

/**
 * Created by alex on 18.10.15.
 */

public class Ship {

    private final int BOARD_SIZE = 10;

    private int mastCount;

    private Boolean[][] shipOriginal;
    private Boolean[][] ship;

    public Ship(int mastCount){
        this.mastCount = mastCount;
        shipOriginal = new Boolean[BOARD_SIZE][BOARD_SIZE];
        ship =         new Boolean[BOARD_SIZE][BOARD_SIZE];
        Arrays.fill(shipOriginal, false);
        Arrays.fill(ship,         false);
    }

    public Ship(Boolean[][] ship){
        this.ship = ship;
        this.shipOriginal = ship;
    }

    public void setShip(Boolean board, Integer length, Integer row, Integer col, Integer direction){

        switch (direction){
            case 0:
                for (int i=0; i<length; i++){
                    if(col-1<0){
                        return;
                    }
                    ship[row][col-i] = true;
                    shipOriginal[row][col-i] = true;
                }
                break;
            case 1:
                for (int i=0; i<length; i++){
                    if(row-1<0){
                        return;
                    }
                    ship[row-1][col] = true;
                    shipOriginal[row-1][col] = true;
                }
                break;
            case 2:
                for (int i=0; i<length; i++){
                    if(col+1>BOARD_SIZE){
                        return;
                    }
                    ship[row][col+i] = true;
                    shipOriginal[row][col+i] = true;
                }
                break;
            case 3:
                for (int i=0; i<length; i++){
                    if(row+1>BOARD_SIZE){
                        return;
                    }
                    ship[row+1][col] = true;
                    shipOriginal[row+1][col] = true;
                }
                break;
        }
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
        for(int i=0; i<BOARD_SIZE; i++){
            for(int j=0; j<BOARD_SIZE; j++){
                if(ship.shipOriginal[i][j] == true &&
                        (  this.shipOriginal[i][i]      == true
                        || (i+1<BOARD_SIZE && this.shipOriginal[i+1][i]    == true)
                        || (i+1<BOARD_SIZE && this.shipOriginal[i][i+1]    == true)
                        || (i+1<BOARD_SIZE && this.shipOriginal[i+1][i+1]  == true)
                        || (i+1<BOARD_SIZE && this.shipOriginal[i-1][i]    == true)
                        || (i-1>=0         && this.shipOriginal[i][i-1]    == true)
                        || (i-1>=0         && this.shipOriginal[i-1][i-1]  == true)
                        || (i-1>=0         && this.shipOriginal[i+1][i-1]  == true)
                        || (i-1>=0         && this.shipOriginal[i-1][i+1]  == true))){
                    return false;
                }
            }
        }
        return true;
    }

    public int getMastCount() {
        return mastCount;
    }
}
