package com.battleships.ga;

import com.battleships.game.Game;
import com.battleships.utils.Coords;

import java.util.Random;

/**
 * Created by Adam on 2015-12-09.
 */
public class GAHandler {


    private GenePool pool;

    public GAHandler(String fileName, double[] target) {
        pool = new GenePool(fileName,target);
    }

    public void advance(){
        pool.advance();
    }

    public void advance(int advances){
        for(int i=0;i<advances;i++)
            advance();
    }

    public Coords getShoot(boolean[][] board){

        double[] dist = pool.getBest().getDist();
        for(int i = 0; i < 100; i++)
        {
            if(!Game.isPossibleShoot(board, new Coords(i % 10, i / 10))) dist[i] = 0;
        }
        double total = 0;
        for(int i=0;i<100;i++)
            total+= dist[i];

        for(int i=0;i<100;i++)
            dist[i] /= total;

        for(int i=1;i<100;i++)
            dist[i] += dist[i-1];
        Random random = new Random();
        double rand = random.nextDouble();
        int x=0,y=0;
        for(int i = 0; i < 99; i++) //was /*dist[i] < rand &&*/ <- i think that's wrong
        {
            if(dist[i] > rand)
            {
                x = i % 10;
                y = i / 10;
                break;
            }
        }
    return new Coords(x,y);
    }

}
