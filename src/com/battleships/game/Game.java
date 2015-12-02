package com.battleships.game;

//import com.sun.xml.internal.xsom.impl.scd.Iterators;

import com.battleships.ai.AIController;
import com.battleships.utils.Coords;
import com.battleships.utils.Direction;
import com.battleships.utils.Effect;
import com.battleships.utils.Player;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 18.10.15.
 */
public class Game {

    private int pointsLeft;
    private int pointsRight;

    private Player currentPlayer;

    private AIController AI;

    //Ships count: 4x1, 3x2, 3x2, 1x4
    private int SINGLE_MAST_SHIPS = 4;
    private int DUOBLE_MAST_SHIPS = 3;
    private int TRIPLE_MAST_SHIPS = 2;
    private int QUAD_MAST_SHIPS = 1;

    private ArrayList<Ship> boardLeft;
    private ArrayList<Ship> boardRight;

    //no comment...
    private boolean[][] lastSankShipLeft;
    private boolean[][] lastSankShipRight;

    public Game() {
        pointsLeft = 0;
        pointsRight = 0;

        boardLeft = new ArrayList<>();
        boardRight = new ArrayList<>();

        AI = new AIController();
    }

    /**
     * @param board     - 0 left, 1 right
     * @param length
     * @param row
     * @param col
     * @param direction - 0 left, 1 up, 2 right, 3 down
     * @return
     */
    public Boolean isPlacementPossible(Boolean board, Integer length, Integer row, Integer col, Direction direction) {
        Ship ship = new Ship(length);
        if (!ship.setShip(length, row, col, direction)) {
            return false;
        }

        if (board) {
            for (Ship s : boardRight) {
                if (s.isCollision(ship)) {
                    return false;
                }
            }
            return true;
        } else {
            for (Ship s : boardLeft) {
                if (s.isCollision(ship)) {
                    return false;
                }
            }
            return true;
        }

    }

    public boolean setNewShipInGame(
            Boolean board,
            Integer length,
            Integer row,
            Integer col,
            Direction direction
    ) {
        if (isPlacementPossible(board, length, row, col, direction)) {
            Ship ship = new Ship(length);
            ship.setShip(length, row, col, direction);
            if (board)
                boardRight.add(ship);
            else
                boardLeft.add(ship);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param board     - 0 left, 1 right
     * @param mastCount
     * @return
     */
    public Integer getRemainingShipsCount(Boolean board, Integer mastCount) {

        int defaultShipCount = 0;

        switch (mastCount) {
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

        if (board) {
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
    public Boolean isPlaceAndSurrFree(Boolean board, Integer row, Integer col) {
        if (board) {
            for (Ship s : boardLeft) {
                if (!s.isPlaceAndSurrFree(row, col)) {
                    return false;
                }
            }
            return true;
        } else {
            for (Ship s : boardRight) {
                if (!s.isPlaceAndSurrFree(row, col)) {
                    return false;
                }
            }
            return true;
        }
    }

    public void start() {
        pointsLeft = 0;
        pointsRight = 0;

        boardRight = AI.placeShipsFromFactors();

        currentPlayer = Player.HUMAN;
    }

    /**
     * @param board - 0 left, 1 right
     */
    public void eraseBoard(Boolean board) {
        if (board) {
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
    public int makeTurn(Boolean board, Integer row, Integer col) {
        if (board) {
            for (Ship ship : boardRight) {
                if (ship.isHit(row, col)) {
                    pointsLeft++;
                    if (currentPlayer == Player.HUMAN) currentPlayer = Player.AI;
                    else currentPlayer = currentPlayer.HUMAN;
                    if (ship.isSank()) {
                        lastSankShipRight = ship.getShipOriginal();
                        return 2;
                    } else {
                        return 1;
                    }
                }
            }
        } else {
            for (Ship ship : boardLeft) {
                if (ship.isHit(row, col)) {
                    pointsRight++;
                    if (currentPlayer == Player.HUMAN) currentPlayer = Player.AI;
                    else currentPlayer = currentPlayer.HUMAN;
                    if (ship.isSank()) {
                        lastSankShipLeft = ship.getShipOriginal();
                        return 2;
                    } else {
                        return 1;
                    }
                }
            }
        }
        if (currentPlayer == Player.HUMAN) currentPlayer = Player.AI;
        else currentPlayer = currentPlayer.HUMAN;
        return 0;
    }

    public Effect getEffect(Coords shot) {
        int hit = makeTurn(currentPlayer == Player.HUMAN ? false : true, shot.x, shot.y);
        switch (hit) {
            case 2:
                return Effect.SANK;
            case 1:
                return Effect.HIT;
            default:
                return Effect.MISSED;
        }
    }

    public List<Coords> getAIMove(){
        List<Coords> moves = new ArrayList<>();
        Coords move = AI.makeRandomMove();
        while(getEffect(move)!=Effect.MISSED){
            moves.add(move);
        }
        return moves;
    }

    public List<Coords> getShipArray() {
        boolean[][] lastSankShip = currentPlayer == Player.AI ? lastSankShipLeft : lastSankShipRight;
        List<Coords> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (lastSankShip[i][j] == true) {
                    list.add(new Coords(i, j));
                }
            }
        }
        return list;
    }

    public Boolean isGameOver() {
        if (isGameOver(false)) {
            return true;
        }
        if (isGameOver(true)) {
            return true;
        }
        return false;
    }

    /**
     * @param board - 0 left, 1 right
     * @return
     */
    public Boolean isGameOver(Boolean board) {
        if (board) {
            for (Ship ship : boardRight) {
                if (!ship.isSank()) {
                    return false;
                }
            }
        } else {
            for (Ship ship : boardLeft) {
                if (!ship.isSank()) {
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

    public static void main(String[] args) {
        Game g = new Game();
        boolean[][] randomBoard = g.getRandomBoard();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (randomBoard[i][j] == true) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
    }

    public boolean[][] getRandomBoard() {

        boolean[][] randomBoard = new boolean[10][10];

//        while (true) {
//
//            int shipsSet = 0;
//
//            for (int i = 0; i < QUAD_MAST_SHIPS; i++) {
//                if (setNewShipInGame(false,
//                        4,
//                        new SecureRandom().nextInt(10),
//                        new SecureRandom().nextInt(10),
//                        Direction.values(new SecureRandom().nextInt(4)))) {
//                    shipsSet++;
//                }
//            }
//
//            for (int i = 0; i < TRIPLE_MAST_SHIPS; i++) {
//                if (setNewShipInGame(false,
//                        3,
//                        new SecureRandom().nextInt(10),
//                        new SecureRandom().nextInt(10),
//                        new SecureRandom().nextInt(4))) {
//                    shipsSet++;
//                }
//            }
//
//            for (int i = 0; i < DUOBLE_MAST_SHIPS; i++) {
//                if (setNewShipInGame(false,
//                        2,
//                        new SecureRandom().nextInt(10),
//                        new SecureRandom().nextInt(10),
//                        new SecureRandom().nextInt(4))) {
//                    shipsSet++;
//                }
//            }
//
//            for (int i = 0; i < SINGLE_MAST_SHIPS; i++) {
//                if (setNewShipInGame(false,
//                        1,
//                        new SecureRandom().nextInt(10),
//                        new SecureRandom().nextInt(10),
//                        new SecureRandom().nextInt(4))) {
//                    shipsSet++;
//                }
//            }
//
//            System.out.println("Ustawiono statkow: " + shipsSet);
//
//            if (shipsSet == (QUAD_MAST_SHIPS + TRIPLE_MAST_SHIPS + DUOBLE_MAST_SHIPS + SINGLE_MAST_SHIPS)) {
//                for (Ship s : boardLeft) {
//                    randomBoard = s.setOnRandomBoard(randomBoard);
//                }
//                break;
//            } else {
//                boardLeft.clear();
//            }
//        }
        return randomBoard;
    }

    public ArrayList<Ship> getBoardLeft() {
        return boardLeft;
    }

    public ArrayList<Ship> getBoardRight() {
        return boardRight;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }


}
