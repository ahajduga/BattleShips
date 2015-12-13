package com.battleships.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Adam on 2015-12-09.
 */
public class Gene {

    public static final int WAVE_COUNT = 10;

    private List<Wave> harmonics;


    public Gene(List<Wave> harmonics) {
        this.harmonics = harmonics;
    }

    public double[] getDist() {
        double[] totalDist = new double[100];
        for (Wave w : harmonics) {
            totalDist = addDist(totalDist, w.getDist());

        }
        return totalDist;
    }

    private double[] addDist(double[] first, double[] second) {
        double[] resultDist = new double[100];
        for (int i = 0; i < 100; i++) {
            resultDist[i] = first[i] + second[i];
        }
        return resultDist;
    }

    public double getFitness(double[] target) {
        double[] currentDist = getDist();
        double result = 0;
        double currentMax = getMax(currentDist);
        double targetMax = getMax(target);
        double[] target2 = Arrays.copyOf(target, 100);
        for (int i = 0; i < 100; i++) {
            currentDist[i] /= currentMax;
            target2[i] /= targetMax;
        }
        for (int i = 0; i < 100; i++) {
            result -= Math.pow(currentDist[i] - target2[i], 2.0);
        }
        return result;
    }

    private double getMax(double[] doubles) {
        double result = 0;
        for (int i = 0; i < 100; i++) {
            if (doubles[i] > result)
                result = doubles[i];
        }
        return result;

    }

    public List<Wave> getHarmonics() {
        return harmonics;
    }

    public static Gene cross(Gene father, Gene mother, boolean mutate) {
        double[] perfectChild = new double[100];
        double[] fatherDist = father.getDist();
        double[] motherDist = mother.getDist();
        double[] imag = new double[100];
        for (int i = 0; i < 100; i++) {
            perfectChild[i] = (fatherDist[i] - motherDist[i]) / 2;
        }


        DFT(true, perfectChild, imag);
        int[] sines = selectTop(perfectChild);

        int[] cosines = selectTop(perfectChild);

        List<Wave> harmonics = new ArrayList<>();
        for (int i = 0; i < WAVE_COUNT; i++) {
            //TODO zamienic te 1.0, 1 na sensowne
            int maxAmp = 10000;
            double rate = 0.1;
            Random random = new Random();
            double alpha = mutate  && (random.nextDouble() <= rate) ? maxAmp - 2*random.nextDouble()*maxAmp : imag[sines[i]];
            double beta = mutate && (random.nextDouble() <= rate)? maxAmp - 2*random.nextDouble()*maxAmp : perfectChild[cosines[i]];
            int omegaA = mutate && (random.nextDouble() <= rate)? (int)random.nextDouble()*50 : sines[i];
            int omegaB = mutate && (random.nextDouble() <= rate)? (int)random.nextDouble()*50 : cosines[i];

            harmonics.add(new Wave(alpha, beta, omegaA, omegaB));
        }


        return new Gene(harmonics);
    }

    private static int[] selectTop(double[] values) {
        int[] top = new int[WAVE_COUNT];
        double[] copy = new double[100];
        for (int i = 0; i < 100; i++)
            copy[i] = values[i];
        double bestValue = 0;
        int bestValueIndex = 0;
        for (int i = 0; i < WAVE_COUNT; i++) {
            for (int j = 0; j < 100; j++) {
                if (Math.abs(copy[j]) > bestValue) {
                    bestValue = Math.abs(copy[j]);
                    bestValueIndex = j;
                }
            }
            top[i] = bestValueIndex;
            copy[bestValueIndex] = 0;
            bestValue = 0;
            bestValueIndex = 0;
        }

        return top;
    }

    private static void DFT(boolean isForward, double[] real, double[] imag) {
        double arg;
        double cosarg, sinarg;
        double[] x2 = new double[100];
        double[] y2 = new double[100];
        for (int i = 0; i < 100; i++) {
            arg = -2.0 * Math.PI * (double) i / (double) 100;
            if (isForward)
                arg = -arg;
            for (int k = 0; k < 100; k++) {
                sinarg = Math.sin(k * arg);
                cosarg = Math.cos(k * arg);
                x2[i] += (real[k] * cosarg - imag[k] * sinarg);
                y2[i] += (real[k] * sinarg + imag[k] * cosarg);
            }
        }

        if (isForward) {
            for (int i = 0; i < 100; i++) {
                real[i] = x2[i] / (double) 100;
                imag[i] = y2[i] / (double) 100;
            }
        } else {
            for (int i = 0; i < 100; i++) {
                real[i] = x2[i];
                imag[i] = y2[i];
            }
        }

    }
}
