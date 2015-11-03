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

    public void setShip(Integer row, Integer col){
        ship[row][col] = true;
        shipOriginal[row][col] = true;
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
}
