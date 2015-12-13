package com.battleships.ga;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Adam on 2015-12-09.
 */
public class GenePool {

    public static final int GENE_COUNT = 100;
    private static final double MIN_AMP = -10000.0d;
    private static final double MAX_AMP = 10000.0d;
    private static final int MIN_PULSE = 0;
    private static final int MAX_PULSE = 50;
    private Gene[] genes;
    private double[] target;

    public GenePool(String geneFile, double[] target) {

        this.target = target;
        genes = new Gene[GENE_COUNT];

        loadGenes(geneFile);

    }
    private void loadGenes(String geneFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(geneFile))) {
            for (int i = 0; i < GENE_COUNT; i++) {
                List<Wave> waves = new ArrayList<>();
                for (int j = 0; j < Gene.WAVE_COUNT; j++) {
                    String str = br.readLine();

                    String[] split = str.split(" ");
                    double alpha = Double.parseDouble(split[0]);
                    int omegaA = Integer.parseInt(split[1]);
                    double beta = Double.parseDouble(split[2]);
                    int omegaB = Integer.parseInt(split[3]);
                    waves.add(new Wave(alpha,beta,omegaA,omegaB));

                }
                genes[i] = new Gene(waves);
                br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePool(String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            for (int i = 0; i < GENE_COUNT; i++) {

                Gene currentGene = genes[i];
                List<Wave> waves = currentGene.getHarmonics();
                for (int j = 0; j < Gene.WAVE_COUNT; j++) {
                    Wave currentWave = waves.get(j);
                    double alpha = currentWave.getAlpha();
                    double beta = currentWave.getBeta();
                    int omegaA = currentWave.getOmegaA();
                    int omegaB = currentWave.getOmegaB();
                    String waveData = String.valueOf(alpha) + " " + omegaA + " " + beta + " " + omegaB;
                    writer.println(waveData);

                }
                writer.println();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void generateRandomGenePool(String filePath) {
        Random random = new Random();
        try (PrintWriter writer = new PrintWriter(filePath)) {

            for (int i = 0; i < GENE_COUNT; i++) {

                for (int j = 0; j < Gene.WAVE_COUNT; j++) {
                    double alpha = MIN_AMP + (MAX_AMP - MIN_AMP) * random.nextDouble();
                    double beta = MIN_AMP + (MAX_AMP - MIN_AMP) * random.nextDouble();
                    int omegaA = (int)(MIN_PULSE + (MAX_PULSE - MIN_PULSE) * random.nextDouble());
                    int omegaB = (int)(MIN_PULSE + (MAX_PULSE - MIN_PULSE) * random.nextDouble());
                    String waveData = String.valueOf(alpha) + " " + omegaA + " " + beta + " " + omegaB;
                    writer.println(waveData);

                }
                writer.println();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void advance() {



        //Gene nextGen[100];
        sortGenes();

        Gene[] newGenePool = new Gene[100];
        for(int i=0;i<10;i++)
            newGenePool[i] = genes[i];
        int nextIndex = 10;
        for(int i=0;i<10;i++)
            for(int j=i+1;j<10;j++)
                newGenePool[nextIndex++] = Gene.cross(newGenePool[i],newGenePool[j],false);
        for(int i=0;i<10;i++)
            for(int j=i+1;j<10;j++)
                newGenePool[nextIndex++] = Gene.cross(newGenePool[i],newGenePool[j],true);

        genes = newGenePool;
    }

    private void sortGenes(){
            for(int i = 0; i < 10; i++)
            {
                double bestFitness = -99999.0;
                int best = i;
                for(int j = i; j < 100; j++)
                {
                    double currentFitness = genes[j].getFitness(target);
                    if(currentFitness > bestFitness)
                    {
                        bestFitness = currentFitness;
                        best = j;
                    }
                }
                Gene temp = genes[i];
                genes[i] = genes[best];
                genes[best] = temp;
            }
    }

    public Gene getBest(){
        double bestFitness = -1;
        int best = 0;

        for(int i = 0; i < 100; i++)
        {
            if(genes[i].getFitness(target) > bestFitness)
            {
                bestFitness = genes[i].getFitness(target);
                best = i;
            }
        }

        return genes[best];
    }
}
