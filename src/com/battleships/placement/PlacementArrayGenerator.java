package com.battleships.placement;

import com.battleships.game.Game;

import java.io.*;

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

}

