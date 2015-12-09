package com.battleships.ga;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2015-12-09.
 */
public class Wave {

    private static final double STEP = 100.0 / (2.0 * Math.PI);
    private double alpha, beta;
    int omegaA, omegaB;

    public Wave(double alpha, double beta, int omegaA, int omegaB) {
        this.alpha = alpha;
        this.beta = beta;
        this.omegaA = omegaA;
        this.omegaB = omegaB;
    }

    public double[] getDist() {
        double[] totalDist = new double[100];
        for (int i = 0; i < 100; i++)
            totalDist[i] = alpha * Math.sin(omegaA * (i / STEP)) + beta * Math.cos(omegaB * (i / STEP));

        return totalDist;
    }
}
