package com.battleships.placement;

import com.battleships.game.Game;
import com.battleships.utils.Direction;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Adam on 2015-11-12.
 */
public class PlacementArrayGenerator {
    public static void generate(String filePath, int iterationCount) {
        float[][] values = new float[10][10];
        Game game = new Game();

        values = fillValues(game, iterationCount);
        save(values, filePath);


    }

    public static float[][]getRandomBoard(int iterations){
        Map<Integer,Integer> ships = new HashMap<>();
        float[][] result = new float[10][10];
        for(int z=0;z<iterations;z++) {
            float[][] placement = new float[10][10];

            ships.put(1, 4);
            ships.put(2, 3);
            ships.put(3, 2);
            ships.put(4, 1);
            Random random = new Random();
            for (int j = 4; j > 0; j--) {
                for (int i = 0; i < ships.get(j); i++) {
                    Direction direction = Direction.values()[random.nextInt(4)];
                    int x = random.nextInt(10);
                    int y = random.nextInt(10);
                    if (!isPlacementPossible(placement, x, y, j, direction)) {
                   //     return getRandomBoard(iterationsLeft);
                    }
                    placeShip(placement, x, y, j, direction);
                }
            }
            result = sum(result,placement);
        }
        return result;
    }

    private static float[][] sum(float[][] result, float[][] placement) {
        float[][] result1 = new float[10][10];
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                result1[i][j] = result[i][j] + placement[i][j];
            }
        }
        return result1;
    }


    public static void print(float[][] tmp){
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                System.out.print(tmp[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static void placeShip(float[][] placement, int x, int y, Integer mast, Direction dir) {
        for(int i=0;i<mast;i++){
            switch(dir){
                case DOWN:
                    placement[x][y+i] = 1.0f;
                    break;
                case LEFT:
                    placement[x-i][y] = 1.0f;
                    break;
                case RIGHT:
                    placement[x+i][y] = 1.0f;
                    break;
                case UP:
                    placement[x][y-i] = 1.0f;
                    break;
            }
        }
    }


    private static  boolean isPlacementPossible(float[][] placement, int x, int y,int mast, Direction dir) {
        try {
            for (int i = 0; i < mast; i++) {
                switch (dir) {
                    case DOWN:
                        for(int j=-1;j<=1;j++)
                            for(int k=-1;k<=1;k++)
                                if(placement[x+j][k+y+i] != 0.0f)
                                    return false;

                        if (placement[x][y + i] != 0.0f)
                            return false;

                        break;
                    case LEFT:
                        for(int j=-1;j<=1;j++)
                            for(int k=-1;k<=1;k++)
                                if(placement[x-i+j][k+y] != 0.0f)
                                    return false;
                        if (placement[x - i][y] != 0.0f)
                            return false;
                        break;
                    case RIGHT:
                        for(int j=-1;j<=1;j++)
                            for(int k=-1;k<=1;k++)
                                if(placement[x+i+j][k+y] != 0.0f)
                                    return false;
                        if (placement[x + i][y] != 0.0f)
                            return false;
                        break;
                    case UP:
                        for(int j=-1;j<=1;j++)
                            for(int k=-1;k<=1;k++)
                                if(placement[x+j][k+y-i] != 0.0f)
                                    return false;
                        if (placement[x][y - i] != 0.0f)
                            return false;
                        break;
                }
            }
            return true;
        } catch(ArrayIndexOutOfBoundsException e){
            return false;
        }
    }


    public static float[][] getArray(String filePath) {
        float[][] values = new float[10][10];
        int row = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                for (int i = 0; i < 10; i++) {
                    values[row][i] = Float.parseFloat(split[i]);
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }


    public static void save(float[][] values, String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            for (int i = 0; i < 10; i++) {
                String line = "";
                for (int j = 0; j < 10; j++) {
                    line += values[i][j] + " ";

                }
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private static String getRowFromValues(float[] values, int i) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 10; j++) {
            sb.append(values[i + 10 * j] + " ");
        }
        return sb.toString();
    }

    private static float[][] fillValues(Game game, int iterationCount) {
        float[][] finalValues = new float[10][10];

        for (int iter = 0; iter < iterationCount; iter++) {
            System.out.println("Current iter : " +  iter );
            boolean[][] boolShips = game.getRandomBoard();
            float[][] values = new float[10][10];
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    values[i][j] = boolShips[i][j] == true ? 1 : 0;
                }
            }
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    finalValues[i][j]+=values[i][j];
                }
            }
        }
        return finalValues;
    }

    public static void print(boolean[][] tmp) {
    }
}

