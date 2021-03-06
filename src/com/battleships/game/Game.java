package com.battleships.game;

import com.battleships.ai.AIController;
import com.battleships.ga.GAHandler;
import com.battleships.placement.PlacementArrayGenerator;
import com.battleships.utils.*;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by alex on 18.10.15.
 */
public class Game {

    private int pointsLeft;
    private int pointsRight;

    private Player currentPlayer;

    private AIController AI;
    private GAHandler gaHandler;
    private WeightHandler weightHandler;

    //Ships count: 4x1, 3x2, 3x2, 1x4
    private int SINGLE_MAST_SHIPS = 4;
    private int DOUBLE_MAST_SHIPS = 3;
    private int TRIPLE_MAST_SHIPS = 2;
    private int QUAD_MAST_SHIPS = 1;

    private ArrayList<Ship> boardLeft;
    private ArrayList<Ship> boardRight;
    private ArrayList<Ship> boardLeftSank;
    private ArrayList<Ship> boardRightSank;

    private boolean[][] lastSankShipLeft;
    private boolean[][] lastSankShipRight;

    private double target[];
    private float[][] factors;

    public Game() {
        pointsLeft = 0;
        pointsRight = 0;

        boardLeft = new ArrayList<>();
        boardRight = new ArrayList<>();
        boardLeftSank = new ArrayList<>();
        boardRightSank = new ArrayList<>();

        target = readExpBoardFromFile("target.txt");
        gaHandler = new GAHandler("gp.txt", target, this);
        factors = PlacementArrayGenerator.getArray("mc.txt");
        AI = new AIController(factors);
        weightHandler = new WeightHandler(factors);
    }

    public Boolean isPlacementPossible(
            Board board,
            Integer length,
            Integer row,
            Integer col,
            Direction direction
    ) {
        Ship ship = new Ship(length);
        if (!ship.setShip(length, row, col, direction)) {
            return false;
        }

        if (board==Board.RIGHT) {
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

    public Boolean isThereShipPossible(
            Board board,
            Integer length,
            Integer row,
            Integer col,
            Direction direction
    ) {
        Ship ship = new Ship(length);
        if (!ship.setShip(length, row, col, direction)) {
            return false;
        }

        if (board==Board.RIGHT) {
            for (Ship s : boardRightSank) {
                if (s.isCollision(ship)) {
                    return false;
                }
            }
            return true;
        } else {
            for (Ship s : boardLeftSank) {
                if (s.isCollision(ship)) {
                    return false;
                }
            }
            return true;
        }

    }

    public boolean setNewShipInGame(
            Board board,
            Integer length,
            Integer row,
            Integer col,
            Direction direction
    ) {
        if (isPlacementPossible(board, length, row, col, direction)) {
            Ship ship = new Ship(length);
            ship.setShip(length, row, col, direction);
            if (board==Board.RIGHT)
                boardRight.add(ship);
            else
                boardLeft.add(ship);
            return true;
        } else {
            return false;
        }
    }

    public Integer getRemainingShipsCount(Board board, Integer mastCount) {

        int defaultShipCount = 0;

        switch (mastCount) {
            case 1:
                defaultShipCount = SINGLE_MAST_SHIPS;
                break;
            case 2:
                defaultShipCount = DOUBLE_MAST_SHIPS;
                break;
            case 3:
                defaultShipCount = TRIPLE_MAST_SHIPS;
                break;
            case 4:
                defaultShipCount = QUAD_MAST_SHIPS;
                break;
        }

        if (board==Board.RIGHT) {
            return defaultShipCount - (int) boardRight.stream()
                    .filter((ship) -> ship.getMastCount() == mastCount).count();
        } else {
            return defaultShipCount - (int) boardLeft.stream()
                    .filter((ship) -> ship.getMastCount() == mastCount).count();
        }
    }

    public Integer getShipsCount(Board board, Integer mastCount) {

        int ships = 0;

        if (board==Board.RIGHT) {

            for(Ship ship : boardRight){
                if(ship.getMastCount()==mastCount && !ship.isSank()) ships++;
            }
        } else {
            for(Ship ship : boardLeft){
                if(ship.getMastCount()==mastCount && !ship.isSank()) ships++;
            }
        }

        return ships;
    }

    public Boolean isPlaceAndSurrFree(Board board, Integer row, Integer col) {
        if (board==Board.LEFT) {
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
        printBoard(Board.RIGHT);
        printBoard(Board.LEFT);

        currentPlayer = Player.HUMAN;

        saveExpBoardToFile("target.txt");
    }

    public void eraseBoard(Board board) {
        if (board==Board.LEFT) {
            boardLeft.clear();
        } else {
            boardRight.clear();
        }
    }

    public boolean isPossibleShoot(Board board, Coords coords){

        if(getShipsCount(board, 1)>0){
            return isThereShipPossible(board, 1, coords.x, coords.y, Direction.DOWN);
        } else if(getShipsCount(board, 2)>0){
            return (isThereShipPossible(board, 2, coords.x, coords.y, Direction.DOWN)
                    || isThereShipPossible(board, 2, coords.x, coords.y, Direction.UP)
                    || isThereShipPossible(board, 2, coords.x, coords.y, Direction.LEFT)
                    || isThereShipPossible(board, 2, coords.x, coords.y, Direction.RIGHT));
        } else if(getShipsCount(board, 3)>0){
            return (isThereShipPossible(board, 3, coords.x, coords.y, Direction.DOWN)
                    || isThereShipPossible(board, 3, coords.x, coords.y, Direction.UP)
                    || isThereShipPossible(board, 3, coords.x, coords.y, Direction.LEFT)
                    || isThereShipPossible(board, 3, coords.x, coords.y, Direction.RIGHT));
        } else if(getShipsCount(board, 4)>0){
            return (isThereShipPossible(board, 4, coords.x, coords.y, Direction.DOWN)
                    || isThereShipPossible(board, 4, coords.x, coords.y, Direction.UP)
                    || isThereShipPossible(board, 4, coords.x, coords.y, Direction.LEFT)
                    || isThereShipPossible(board, 4, coords.x, coords.y, Direction.RIGHT));
        } else {
            return false;
        }
    }

    public int makeTurn(Board board, Integer row, Integer col) {
        if (board==Board.RIGHT) {
            for (Ship ship : boardRight) {
                if (ship.isHit(row, col)) {
                    pointsLeft++;
                    if (ship.isSank()) {
                        boardRightSank.add(ship);
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
                    if (ship.isSank()) {
                        boardLeftSank.add(ship);
                        lastSankShipLeft = ship.getShipOriginal();
                        return 2;
                    } else {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    public Effect getEffect(Coords shot) {
        int hit = makeTurn(currentPlayer == Player.HUMAN ? Board.RIGHT : Board.LEFT, shot.x, shot.y);

        if(currentPlayer == Player.HUMAN){
            weightHandler.shoot(shot.x, shot.y);
            PlacementArrayGenerator.save(factors, "mc.txt");
        }

        switch (hit) {
            case 2:
                return Effect.SANK;
            case 1:
                return Effect.HIT;
            default:
                if (currentPlayer == Player.HUMAN) currentPlayer = Player.AI;
                else currentPlayer = currentPlayer.HUMAN;
                return Effect.MISSED;
        }
    }

    public List<Coords> getAIMove(){
        List<Coords> moves = new ArrayList<>();

        while(true){
            Coords move = gaHandler.getShoot(Board.LEFT);
            Effect eff = getEffect(move);
            if(eff==Effect.MISSED){
                moves.add(move);
                gaHandler.setHistory(move, 1);
                break;

            } else if(eff==Effect.HIT){
                moves.add(move);
                gaHandler.setHistory(move, 2);
                if(!gaHandler.isSinking()) gaHandler.setIsSinking(true);
                gaHandler.addToSinkingHit(move);

            } else {
                moves.add(move);
                gaHandler.setHistory(move, 3);
                gaHandler.setIsSinking(false);
            }
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
        if (isGameOver(Board.LEFT)) {
            return true;
        }
        if (isGameOver(Board.RIGHT)) {
            return true;
        }
        return false;
    }

    public Boolean isGameOver(Board board) {
        if (board == Board.RIGHT) {
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
//        boolean[][] randomBoard = g.getRandomBoard();
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                if (randomBoard[i][j] == true) {
//                    System.out.print("#");
//                } else {
//                    System.out.print(".");
//                }
//            }
//            System.out.println("");
//        }

        g.start();

        g.printBoard(Board.RIGHT);
    }

    public boolean[][] getRandomBoard() {

        boolean[][] randomBoard = new boolean[10][10];

        while (true) {

            int shipsSet = 0;

            for (int i = 0; i < QUAD_MAST_SHIPS; i++) {
                if (setNewShipInGame(Board.RIGHT,
                        4,
                        new SecureRandom().nextInt(10),
                        new SecureRandom().nextInt(10),
                        Direction.values()[new SecureRandom().nextInt(4)])) {
                    shipsSet++;
                }
            }

            for (int i = 0; i < TRIPLE_MAST_SHIPS; i++) {
                if (setNewShipInGame(Board.RIGHT,
                        3,
                        new SecureRandom().nextInt(10),
                        new SecureRandom().nextInt(10),
                        Direction.values()[new SecureRandom().nextInt(4)])) {
                    shipsSet++;
                }
            }

            for (int i = 0; i < DOUBLE_MAST_SHIPS; i++) {
                if (setNewShipInGame(Board.RIGHT,
                        2,
                        new SecureRandom().nextInt(10),
                        new SecureRandom().nextInt(10),
                        Direction.values()[new SecureRandom().nextInt(4)])) {
                    shipsSet++;
                }
            }

            for (int i = 0; i < SINGLE_MAST_SHIPS; i++) {
                if (setNewShipInGame(Board.RIGHT,
                        1,
                        new SecureRandom().nextInt(10),
                        new SecureRandom().nextInt(10),
                        Direction.values()[new SecureRandom().nextInt(4)])) {
                    shipsSet++;
                }
            }

            System.out.println("Ustawiono statkow: " + shipsSet);

            if (shipsSet == (QUAD_MAST_SHIPS + TRIPLE_MAST_SHIPS + DOUBLE_MAST_SHIPS + SINGLE_MAST_SHIPS)) {
                for (Ship s : boardLeft) {
                    randomBoard = s.setOnRandomBoard(randomBoard);
                }
                break;
            } else {
                boardLeft.clear();
            }
        }
        return randomBoard;
    }

    public int[][] printBoard(Board board){

        ArrayList<Ship> boardTmp = board==Board.LEFT ? boardLeft : boardRight;
        int[][] boardPrint = new int[10][10];

        for(int i=1; i<=boardTmp.size(); i++){

            boolean[][] ship = boardTmp.get(i-1).getShipOriginal();

            for(int j=0; j<10; j++){
                for(int k=0; k<10; k++){
                    if(ship[j][k]){
                        boardPrint[j][k]=i;
                    }
                }
            }
        }

        for(int j=0; j<10; j++){
            for(int k=0; k<10; k++){
                if(boardPrint[j][k]==0){
                    System.out.print(".");
                } else {
                    System.out.print(boardPrint[j][k]);
                }
            }
            System.out.println();
        }

        return boardPrint;
    }

    public double[] readExpBoardFromFile(String fileName){

        double[] target = new double[100];

        int pos = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                for (int i = 0; i < 10; i++) {
                    target[pos] = Double.parseDouble(split[i]);
                    pos++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return target;
    }

    public void saveExpBoardToFile(String fileName){

        int gamesCount = readGamesCount();

        for(Ship ship : boardLeft){
            for (int i = 0; i < 100; i+=10) {
                for (int j = 0; j < 10; j++) {
                    target[i+j] = gamesCount*target[i+j] + (ship.getShipOriginal()[i/10][j] ? 10 : 0);
                }
            }
        }

        gamesCount++;

        try (PrintWriter writer = new PrintWriter(fileName)) {

            for (int i = 0; i < 100; i+=10) {
                String line = "";
                for (int j = 0; j < 10; j++) {
                    line += target[i+j]/gamesCount + " ";
                }
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        saveGamesCount(gamesCount);
    }

    public int readGamesCount(){

        int gamesCount = 0;
        try {

            Scanner scanner = new Scanner(new File("games_count.txt"));
            gamesCount = scanner.nextInt();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return gamesCount;
    }

    public void saveGamesCount(int gamesCount){

        try{

            File file = new File("games_count.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("" + gamesCount);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
