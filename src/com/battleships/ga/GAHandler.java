package com.battleships.ga;

import com.battleships.game.Game;
import com.battleships.utils.Board;
import com.battleships.utils.Coords;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Adam on 2015-12-09.
 * Edited by Olek on 2015-12-12.
 */
public class GAHandler {

    private GenePool pool;
    private Game game;
    private int[][] history;
    private boolean isSinking = false;
    private ArrayList<Coords> sinkingHit;

    public GAHandler(String fileName, double[] target, Game game) {
        pool = new GenePool(fileName,target);
        this.game = game;
        history = new int[10][10];
    }

    public Gene getBest(){
        return pool.getBest();
    }
    public void advance(){
        pool.advance();
    }

    public void advance(int advances){
        for(int i=0;i<advances;i++)
            advance();
    }

    public Coords getShoot(Board board){

        if(isSinking){
            return getSinkShoot();
        }

        double[] dist = pool.getBest().getDist();
        for(int i = 0; i < 100; i++)
        {
            if(!game.isPossibleShoot(board, new Coords(i % 10, i / 10))) dist[i] = 0;
            if(history[i%10][i/10] != 0) dist[i] = 0;
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
        int x=-1,y=-1;
        while(x==-1) {

            for (int i = 0; i < 99; i++) //was /*dist[i] < rand &&*/ <- i think that's wrong
            {
                if (dist[i] > rand) {
                    x = i % 10;
                    y = i / 10;
                    break;
                }
            }
            rand -=0.05;
        }
        System.out.println("x = " + x + ", y = " + y);
        return new Coords(x,y);
    }

    public Coords getSinkShoot(){

        if(sinkingHit.size()==1){

            Coords hit = sinkingHit.get(0);

            ArrayList<Coords> random = new ArrayList<>();

            if(hit.x<9 && history[hit.x+1][hit.y]==0){ //if wasn't earlier

                if(!(hit.x+2<10)){ //if on border
                    random.add(new Coords(hit.x+1, hit.y));

                } else if(history[hit.x+2][hit.y]!=3){ //if not sank near

                    if(!(hit.y+1<10)){ //if in corner

                        if(history[hit.x+2][hit.y-1]!=3){
                            random.add(new Coords(hit.x+1, hit.y));
                        }

                    } else if(!(hit.y>0)){ //if in corner

                        if(history[hit.x+2][hit.y+1]!=3){
                            random.add(new Coords(hit.x+1, hit.y));
                        }

                    } else { //not in any corner

                        if(history[hit.x+2][hit.y-1]!=3 && history[hit.x+2][hit.y+1]!=3){
                            random.add(new Coords(hit.x+1, hit.y));
                        }
                    }
                }
            }

            if(hit.x>0 && history[hit.x-1][hit.y]==0){ //if wasn't earlier

                if(!(hit.x-1>0)){ //if on border
                    random.add(new Coords(hit.x-1, hit.y));

                } else if(history[hit.x-2][hit.y]!=3){ //if not sank near

                    if(!(hit.y+1<10)){ //if in corner

                        if(history[hit.x-2][hit.y-1]!=3){
                            random.add(new Coords(hit.x-1, hit.y));
                        }

                    } else if(!(hit.y>0)){ //if in corner

                        if(history[hit.x-2][hit.y+1]!=3){
                            random.add(new Coords(hit.x-1, hit.y));
                        }

                    } else { //not in any corner

                        if(history[hit.x-2][hit.y-1]!=3 && history[hit.x-2][hit.y+1]!=3){
                            random.add(new Coords(hit.x-1, hit.y));
                        }
                    }
                }
            }

            if(hit.y<9 && history[hit.x][hit.y+1]==0){ //if wasn't earlier

                if(!(hit.y+2<10)){ //if on border
                    random.add(new Coords(hit.x, hit.y+1));

                } else if(history[hit.x][hit.y+2]!=3){ //if not sank near

                    if(!(hit.x+1<10)){ //if in corner

                        if(history[hit.x-1][hit.y+2]!=3){
                            random.add(new Coords(hit.x, hit.y+1));
                        }

                    } else if(!(hit.x>0)){ //if in corner

                        if(history[hit.x+1][hit.y+2]!=3){
                            random.add(new Coords(hit.x, hit.y+1));
                        }

                    } else { //not in any corner

                        if(history[hit.x+1][hit.y+2]!=3 && history[hit.x-1][hit.y+2]!=3){
                            random.add(new Coords(hit.x, hit.y+1));
                        }
                    }
                }
            }

            if(hit.y>0 && history[hit.x][hit.y-1]==0){ //if wasn't earlier

                if(!(hit.y-1>0)){ //if on border
                    random.add(new Coords(hit.x, hit.y-1));

                } else if(history[hit.x][hit.y-2]!=3){ //if not sank near

                    if(!(hit.x+1<10)){ //if in corner

                        if(history[hit.x-1][hit.y-2]!=3){
                            random.add(new Coords(hit.x, hit.y-1));
                        }

                    } else if(!(hit.x>0)){ //if in corner

                        if(history[hit.x+1][hit.y-2]!=3){
                            random.add(new Coords(hit.x, hit.y-1));
                        }

                    } else { //not in any corner

                        if(history[hit.x+1][hit.y-2]!=3 && history[hit.x-1][hit.y-2]!=3){
                            random.add(new Coords(hit.x, hit.y-1));
                        }
                    }
                }
            }

            for (Coords p : random){
                if(history[p.x][p.y]!=0){
                    random.remove(p);
                }
            }

            return random.get(new SecureRandom().nextInt(random.size()));

        } else {

            Coords h1 = sinkingHit.get(0);
            Coords h2 = sinkingHit.get(1);

            if(h1.x == h2.x){
                int max=0;
                for(Coords c : sinkingHit){
                    if(c.y > max) max = c.y;
                }
                if(max==9 || history[h1.x][max+1]!=0){
                    int min=10;
                    for(Coords c2 : sinkingHit){
                        if(c2.y < min) min = c2.y;
                    }
                    return new Coords(h1.x, min-1);
                } else {
                    return new Coords(h1.x, max+1);
                }

            } else {

                int max=0;
                for(Coords c : sinkingHit){
                    if(c.x > max) max = c.x;
                }
                if(max==9 || history[max+1][h1.y]!=0){
                    int min=10;
                    for(Coords c2 : sinkingHit){
                        if(c2.x < min) min = c2.x;
                    }
                    return new Coords(min-1, h1.y);
                } else {
                    return new Coords(max+1, h1.y);
                }
            }
        }
    }

    public boolean isSinking() {
        return isSinking;
    }

    public void setIsSinking(boolean isSinking) {
        sinkingHit = new ArrayList<>();
        this.isSinking = isSinking;
    }

    public void addToSinkingHit(Coords move){
        sinkingHit.add(move);
    }

    public void setHistory(Coords move, int value) {
        history[move.x][move.y]=value;
    }
}
