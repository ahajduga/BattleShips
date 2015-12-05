package com.battleships.utils;

/**
 * Created by Adam on 2015-12-04.
 */
public class WeightHandler {

    private static final float INITIAL_WEIGHT = 10.0f;
    private static final float STEP = 0.5f;


    private float currentWeight = INITIAL_WEIGHT;
    private float[][] factors;
    private final float totalValue;
    public WeightHandler(float[][] factors) {
        this.factors = factors;
        totalValue = getSum(factors);
    }

    public float getSum(float[][] factors){
        float sum=0.0f;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
            sum+= factors[i][j];
            }
        }
        return sum;
    }

    public void shoot(int x, int y) {
        int values = 0;
        float subtractionValue = getSubtractionValue(values, x, y);
     //   float sumSubtracted = 0.0f;
        correctValues(subtractionValue, x, y);
        currentWeight-=STEP;
    }

    private void correctValues(float subtractionValue, int x, int y){
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if(i >= x-1 && i <= x+1 && j >= y-1 && j <= y-1 ) continue;
                factors[x][y] += subtractionValue;
            }
        }
    }
    private float getSubtractionValue(int values, int x, int y) {
        float sumSubtracted = 0.0f;
        float valuesCount = 10;
        sumSubtracted += currentWeight/2;
        factors[x][y] -= currentWeight/2;
        for(int i=x-1;i<=x+1;i++){
            if(i<0 || i >= 10){
                valuesCount--;
                continue;
            }
            for(int j=x-1;j<=x+1;j++){
                if(j<0 || j >= 10){
                    valuesCount--;
                    continue;
                }
                sumSubtracted += currentWeight/2;
                factors[i][j] -= currentWeight/2;
            }
        }
        return sumSubtracted/(100-valuesCount);
    }


}
