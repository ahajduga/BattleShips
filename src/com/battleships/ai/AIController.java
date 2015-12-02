package com.battleships.ai;

import com.battleships.game.Ship;
import com.battleships.placement.PlacementArrayGenerator;
import com.battleships.utils.Coords;
import com.battleships.utils.Direction;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by alex on 02.12.15.
 */
public class AIController {
int x=0, y = 0;
    private int[][] factors;
    private boolean[][] alreadyHit;
    private ArrayList<Ship> AIShips;

    public AIController(){

        factors = new int[10][10];
        alreadyHit = new boolean[10][10];
        factors = PlacementArrayGenerator.getArray("mc.txt");
    }

    public ArrayList<Ship> placeShipsFromFactors() {
        boolean[][] result = new boolean[10][10];
        AIShips = new ArrayList<>();
        //place4mast
        int bestMatchedValue = 999999999;
        Direction matchedDirection;
        Coords placement;
        findPlace(4, 1, result);
        findPlace(3, 2, result);
        findPlace(2, 3, result);
        findPlace(1, 4, result);

//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                if (result[i][j] == true) {
//                    System.out.print("#");
//                } else {
//                    System.out.print(".");
//                }
//            }
//            System.out.println("");
//        }
        return AIShips;
    }

    private void findPlace(int mast, int count, boolean[][] result) {
        for(int i=0;i<count;i++) {
            Direction bestDirection = null;
            int bestX = 0;
            int bestY = 0;
            int bestMatchedValue = Integer.MAX_VALUE;
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {

                    if (isPlacementPossible(mast, x, y, Direction.DOWN)) {
//                    if (canBePlaced(x, y, mast, result, Direction.DOWN)) {
                        int currentValue = findValue(mast, x, y, Direction.DOWN);
                        if (currentValue < bestMatchedValue) {
                            bestMatchedValue = currentValue;
                            bestDirection = Direction.DOWN;
                            bestX = x;
                            bestY = y;
                        }
                    }
                    if (isPlacementPossible(mast, x, y, Direction.UP)) {
//                    if (canBePlaced(x, y, mast, result, Direction.UP)) {
                        int currentValue = findValue(mast, x, y, Direction.UP);
                        if (currentValue < bestMatchedValue) {
                            bestMatchedValue = currentValue;
                            bestDirection = Direction.UP;
                            bestX = x;
                            bestY = y;
                        }
                    }
                    if (isPlacementPossible(mast, x, y, Direction.LEFT)) {
//                    if (canBePlaced(x, y, mast, result, Direction.LEFT)) {
                        int currentValue = findValue(mast, x, y, Direction.LEFT);
                        if (currentValue < bestMatchedValue) {
                            bestMatchedValue = currentValue;
                            bestDirection = Direction.LEFT;
                            bestX = x;
                            bestY = y;
                        }
                    }
                    if (isPlacementPossible(mast, x, y, Direction.RIGHT)) {
//                    if (canBePlaced(x, y, mast, result, Direction.RIGHT)) {
                        int currentValue = findValue(mast, x, y, Direction.RIGHT);
                        if (currentValue < bestMatchedValue) {
                            bestMatchedValue = currentValue;
                            bestDirection = Direction.RIGHT;
                            bestX = x;
                            bestY = y;
                        }
                    }
                }
            }
            place(result, mast, bestX, bestY, bestDirection);
        }
    }

    private void place(boolean[][] result, int mast, int x, int y, Direction dir){
        for(int i=0;i<mast;i++){
            switch(dir){
                case RIGHT:
                    result[x][y+i] = true;
                    break;
                case LEFT:
                    result[x][y-i] = true;
                    break;
                case UP:
                    result[x-i][y] = true;
                    break;
                case DOWN:
                    result[x+i][y] = true;
                    break;
            }
        }
        Ship ship = new Ship(mast);
        ship.setShip(mast, x, y, dir);
        AIShips.add(ship);
    }
    private int findValue(int mast, int x, int y, Direction dir) {
        int sum = 0;
        for(int i=0;i<mast;i++){
            switch(dir){
                case RIGHT:
                    sum+= factors[x][y+i];
                    break;
                case LEFT:
                    sum+= factors[x][y-i];
                    break;
                case UP:
                    sum+= factors[x-i][y];
                    break;
                case DOWN:
                    sum+= factors[x+i][y];
                    break;
            }
        }
        return sum;
    }

    private boolean canBePlaced(int x, int y, int mast, boolean[][] result, Direction dir) {

        for (int i = 0; i < mast; i++) {
            switch (dir) {
                case DOWN:
                    if(y+i >=10) return false;
                    if (result[x][y + i]) return false;
                    break;
                case UP:
                    if(y-i <0) return false;
                    if (result[x][y - i]) return false;
                    break;
                case LEFT:
                    if(x-i <0) return false;
                    if (result[x - i][y]) return false;
                    break;
                case RIGHT:
                    if(x+i >=10) return false;
                    if (result[x + i][y]) return false;
                    break;
            }
        }
        return true;
    }

    public Boolean isPlacementPossible(Integer length, Integer row, Integer col, Direction direction) {
        Ship ship = new Ship(length);
        if (!ship.setShip(length, row, col, direction)) {
            return false;
        }

        for (Ship s : AIShips) {
            if (s.isCollision(ship)) {
                return false;
            }
        }
        return true;
    }

    public Coords makeRandomMove(){


        if(y>= 10){
            x++;
            y=0;
        }
        return new Coords(x,y++);
//        Coords x = new Coords(new SecureRandom().nextInt(10), new SecureRandom().nextInt(10));
//        if(alreadyHit[x.y][x.x] == true)
//            return null;
//        alreadyHit[x.y][x.x] = true;
//        return x;
    }
}
