import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Genetic_Algorithm ga = new Genetic_Algorithm(10);
    }
}

class Genetic_Algorithm
{
    private final int CANDIDATES_NUMBER = 6;
    private final int BINARY_DIGITS     = 10;

    private int generation;
    private int populationSize;
    private float elitismValue;
    private float crossoverRate;
    private float mutationRate;
    private int[][] candidates;
    private int[] fitness;
    private float[] proportionateFitness;
    private int totalFitness;

    public int i,j;

    public Genetic_Algorithm(int populationSize)
    {
        generation = 0;
        this.populationSize = populationSize;
        this.elitismValue = 0.1f;  // "f" para indicar que el número es float
        this.crossoverRate = 0.7f;
        this.mutationRate = 0.2f;
        totalFitness = 0;
        fitness = new int[CANDIDATES_NUMBER];
        proportionateFitness = new float[CANDIDATES_NUMBER];

        candidates = produceInitialRandomPopulation();
        calculeFitness();

        for(i = 0; i < 6; i++)
        {
            for(j = 0; j < 10; j++)
            {
                System.out.print(candidates[i][j]);
            }
            System.out.print("   ");
            System.out.print(fitness[i]);
            System.out.print("   ");
            //System.out.print(Math.ceil(((float)fitness[i] / totalFitness) * 100));
            System.out.print(proportionateFitness[i]);
            System.out.print("   ");
            System.out.print(proportionateFitness[i] * 100);
            System.out.println("");
            System.out.println("");
        }

        rouletteWheel();

        for(i = 0; i < 6; i++)
        {
            for(j = 0; j < 10; j++)
            {
                System.out.print(candidates[i][j]);
            }
            System.out.println("");
            System.out.println("");
        }
    }

    public void calculeFitness()
    {
        for(i = 0; i < CANDIDATES_NUMBER; i++)
        {
            for(j = 0; j < BINARY_DIGITS; j++)
            {
                fitness[i]+=candidates[i][j];
            }
            totalFitness += fitness[i];
        }
        for (i = 0; i < fitness.length; i++)
        {
            proportionateFitness[i] = (float)fitness[i] / totalFitness;
        }
    }

    public void rouletteWheel()
    {
        float[] roulette;
        double rouletteNumber;
        int i=0;
        int j;
        int[] auxArray;

        roulette = new float[CANDIDATES_NUMBER];
        auxArray = new int[BINARY_DIGITS];

        roulette[i] = proportionateFitness[i];

        for(; i<CANDIDATES_NUMBER-1; i++)
        {
            roulette[i+1] = roulette[i] + proportionateFitness[i+1];
        }

        for(i=0; i<CANDIDATES_NUMBER;i++)
        {
            if((float)(rouletteNumber = Math.random()) <= crossoverRate)  //Se determina si se cruza
            {
                rouletteNumber = Math.random();
                for(j=0;j<proportionateFitness.length;j++)
                {
                    if(proportionateFitness[j]>= (float)rouletteNumber)
                    {
                        crossover(this.candidates[i],this.candidates[j]);
                    }
                }
            }
        }

    }

    public void crossover(int[] a,int[] b)
    {
        int[] c = new int[BINARY_DIGITS];
        int i,j;

        for(i=0; i<(int)Math.ceil(BINARY_DIGITS/2); i++)
        {
            c[i] = a[i];
        }
        for(; i<BINARY_DIGITS; i++)
        {
            c[i] = b[i];
        }

        for(i=0; i<(int)Math.ceil(BINARY_DIGITS/2); i++)
        {
            b[i] = c[i];
        }
        for(; i<BINARY_DIGITS; i++)
        {
            a[i] = c[i];
        }
    }

    private int[][] produceInitialRandomPopulation()
    {
        int[][] binaryNumbers;
        int i,j;
        double aux;

        binaryNumbers = new int[CANDIDATES_NUMBER][BINARY_DIGITS];

        for(i=0;i < CANDIDATES_NUMBER; i++)
        {
            for(j=0; j < BINARY_DIGITS; j++)
            {
                aux = Math.random();
                if(aux <= 0.5)
                {
                    aux = 0;
                }
                else
                {
                    aux = 1;
                }
                binaryNumbers[i][j] = (int)aux;
            }
        }

        return binaryNumbers;
    }
}
