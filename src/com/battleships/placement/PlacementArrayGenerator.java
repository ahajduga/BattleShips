package com.battleships.placement;

import com.battleships.game.Game;

import java.io.*;

/**
 * Created by Adam on 2015-11-12.
 */
public class PlacementArrayGenerator {
    public static void generate(String filePath, int iterationCount) {
        int[] values = new int[10 * 10];
        Game game = new Game();


        values = fillValues(game, iterationCount);
        save(values, filePath);


    }

    public static int[] getArray(String filePath){
        int[] values = new int[10*10];
        int row = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line = "";
            while((line = br.readLine()) != null){
                String[] split = line.split(" ");
                for(int i=0;i<10;i++){
                    values[10*row + i] = Integer.parseInt(split[i]);
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


    private static void save(int[] values, String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (int i = 0; i < 10; i++) {
                writer.println(getRowFromValues(values, i) + " ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private static String getRowFromValues(int[] values, int i) {
        StringBuilder sb = new StringBuilder();
        for(int j=0;j<10;i++){
            sb.append(values[i*10+j]);
        }
        return sb.toString();
    }

    private static int[] fillValues(Game game, int iterationCount) {
        int[] values = new int[10 * 10];
        for (int i = 0; i < iterationCount; i++) {
            //      int[] settledShips = game.getRandomPlacement();
            int[] settledShips = new int[10 * 10];
            for (int j = 0; j < 10 * 10; j++) {
                values[j] += settledShips[j];
            }
        }
        return values;
    }
}
