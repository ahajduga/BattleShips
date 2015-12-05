package com.battleships.placement;

import com.battleships.game.Game;
import com.battleships.utils.WeightHandler;

/**
 * Created by Adam on 2015-11-12.
 */
public class PlacementArrayGeneratorMain {


    private static final String FILE_PATH = "mc.txt";
    private static final int ITERATION_COUNT = 1000;
    public static void main(String[] args) {
        float[][] get = PlacementArrayGenerator.getArray(FILE_PATH);
        WeightHandler handler = new WeightHandler(get);
        System.out.println("Suma elementow = " +  handler.getSum(get));
        handler.shoot(2,2);
        handler.shoot(2,2);
        handler.shoot(2,2);
        handler.shoot(2,2);
        handler.shoot(2,2);
        handler.shoot(2,2);
        handler.shoot(2,2);
        handler.shoot(2,2);
        handler.shoot(2,2);
        handler.shoot(2,2);        handler.shoot(2,2);
        handler.shoot(2,2);
        handler.shoot(2,2);
        handler.shoot(2,2);
        handler.shoot(2,2);

        System.out.println("Suma elementow = " +  handler.getSum(get));

//      //  boolean[][] placement = new Game().placeShipsFromFactors(get);
//        int[][] placementints = new int[10][10];
//        for(int i=0;i<10;i++){
//            for(int j=0;j<10;j++){
//                placementints[i][j] = placement[i][j] == true ? 1 : 0;
//
//            }
//        }
//        PlacementArrayGenerator.save(placementints,"result.txt");

       // PlacementArrayGenerator.generate(FILE_PATH,ITERATION_COUNT);
    }
}
