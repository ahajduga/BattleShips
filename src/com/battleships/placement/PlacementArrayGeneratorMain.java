package com.battleships.placement;

import com.battleships.ga.GAHandler;
import com.battleships.ga.Gene;
import com.battleships.ga.GenePool;
import com.battleships.ga.Wave;
import com.battleships.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2015-11-12.
 */
public class PlacementArrayGeneratorMain {


    public static void main(String[] args) {
        Game game = new Game();
        GAHandler handler = new GAHandler("after1000advanceswithmutation.txt",game.target, game);
        handler.advance(2);
        double[] dist = handler.getBest().getDist();

        double[] target = game.target;
     //   handler.getPool().savePool("after1000advanceswithmutation.txt");
        GenePool.generateRandomGenePool("testPool.txt");
    }
}
    //    GenePool.generateRandomGenePool("testPool.txt");

//        Wave wave = new Wave(2110, 30, 50, 60);
//        Wave wave2 = new Wave(20, 20, 20, 20);
//        double[] target = new double[100];
//        for (int i = 0; i < 100; i++) {
//            target[i] = 1;
//        }
//        double[] dist1 = wave.getDist();
//        double[] dist2 = wave2.getDist();
//        for (int i = 0; i < dist1.length; i++) {
//            System.out.print(dist1[i] + " ");
//        }
//        System.out.println();
//        for (int i = 0; i < dist2.length; i++) {
//            System.out.print(dist2[i] + " ");
//        }
//        System.out.println();
//        List<Wave> waves = new ArrayList<>();
//        waves.add(wave);
//        waves.add(wave2);
//        Gene gene = new Gene(waves);
//        double[] x = gene.getDist();
//        for (int i = 0; i < x.length; i++) {
//            System.out.print(x[i] + " ");
//        }
//        System.out.println();
//        System.out.println("Total fitness score = " + gene.getFitness(target));
//    }
//}
//
//    private static final String FILE_PATH = "mc.txt";
//    private static final int ITERATION_COUNT = 10;
//    public static void main(String[] args) {
//  //      float[][] tmp = PlacementArrayGenerator.getRandomBoard(ITERATION_COUNT);
//        PlacementArrayGenerator.generate("test.txt",10000);
//   //     PlacementArrayGenerator.save(tmp,"genetic.txt");
//   //     PlacementArrayGenerator.print(tmp);
//        int x = 5;
//    PlacementArrayGenerator.generate("test.txt",2);
//        float[][] get = PlacementArrayGenerator.getArray(FILE_PATH);
//        WeightHandler handler = new WeightHandler(get);
//        System.out.println("Suma elementow = " +  handler.getSum(get));
//        handler.shoot(2,2);
//        handler.shoot(2,2);
//        handler.shoot(2,2);
//        handler.shoot(2,2);
//        handler.shoot(2,2);
//        handler.shoot(2,2);
//        handler.shoot(2,2);
//        handler.shoot(2,2);
//        handler.shoot(2,2);
//        handler.shoot(2,2);        handler.shoot(2,2);
//        handler.shoot(2,2);
//        handler.shoot(2,2);
//        handler.shoot(2,2);
//        handler.shoot(2,2);
//
//        System.out.println("Suma elementow = " +  handler.getSum(get));

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

