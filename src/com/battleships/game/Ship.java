package com.battleships.game;

import java.util.Arrays;

/**
 * Created by alex on 18.10.15.
 */
public class Ship {

    private Boolean[][] shipOriginal;
    private Boolean[][] ship;

    public Ship(){
        shipOriginal = new Boolean[10][10];
        ship =         new Boolean[10][10];
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
                    ship[row][col-i] = true;
                    shipOriginal[row][col-i] = true;
                }
                break;
            case 1:
                for (int i=0; i<length; i++){
                    ship[row+1][col] = true;
                    shipOriginal[row+1][col] = true;
                }
                break;
            case 2:
                for (int i=0; i<length; i++){
                    ship[row][col+i] = true;
                    shipOriginal[row][col+i] = true;
                }
                break;
            case 3:
                for (int i=0; i<length; i++){
                    ship[row-1][col] = true;
                    shipOriginal[row-1][col] = true;
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
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(ship.shipOriginal[i][j] == true &&
                        (  this.shipOriginal[i][i]      == true
                        || this.shipOriginal[i+1][i]    == true
                        || this.shipOriginal[i][i+1]    == true
                        || this.shipOriginal[i+1][i+1]  == true
                        || this.shipOriginal[i-1][i]    == true
                        || this.shipOriginal[i][i-1]    == true
                        || this.shipOriginal[i-1][i-1]  == true
                        || this.shipOriginal[i+1][i-1]  == true
                        || this.shipOriginal[i-1][i+1]  == true)){
                    return false;
                }
            }
        }
        return true;
    }
}
