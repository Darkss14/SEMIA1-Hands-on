package examples.behaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class GenericAgent extends Agent {

    private final int CANDIDATES_NUMBER = 6;
    private final int BINARY_DIGITS     = 10;

    private int generation;
    private float elitismValue;
    private float crossoverRate;
    private float mutationRate;

    private int[][] candidates;
    private int[][] oldGen;
    private int[] candidateSolution;

    private int[] fitness;
    private float[] proportionateFitness;
    private int totalFitness;
    private int[] oldFitness;
    private int[] newFitness;

    private boolean criterionSatisfied;

    public int i,j;

  protected void setup() {

    System.out.println("Agent "+getLocalName()+" started.");
    this.generation = 0;
    this.elitismValue = 0.1f;  // "f" para indicar que el número es float
    this.crossoverRate = 0.9f;
    this.mutationRate = 0.2f;
    this.oldGen = new int[CANDIDATES_NUMBER][BINARY_DIGITS];
    this.criterionSatisfied = false;

    candidates = produceInitialRandomPopulation();
    generation++;

    addBehaviour(new MyGenericBehaviour());
  }

  public void printData()
  {
      System.out.println("            Generation " + generation);
      System.out.println("");
      for(i = 0; i < CANDIDATES_NUMBER; i++)
      {
          System.out.print("Chain:       ");
          System.out.print("Fitness:  ");
          System.out.print("Fitness Prop.:   ");
          System.out.println("");
          for(j = 0; j < BINARY_DIGITS; j++)
          {
              System.out.print(candidates[i][j]);
          }
          System.out.print("      ");
          System.out.print(fitness[i]);
          System.out.print("         ");

          System.out.print(Math.round((proportionateFitness[i])*100.0)/100.0);
          System.out.println("");
          System.out.println("");
      }
      System.out.print("    Total Population Fitness: " + totalFitness);
      System.out.println("");
      System.out.println("");
      System.out.println("----------------------------------------");
  }

  public void evaluatePopulation()
  {
      this.totalFitness = 0;
      this.fitness = new int[CANDIDATES_NUMBER];
      this.proportionateFitness = new float[CANDIDATES_NUMBER];
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

  public void survivalOfTheFittest(int[][] oldGens, int[][] newGens)
  {
      int i,j;

      for(i=0;i<CANDIDATES_NUMBER;i++)
      {
          if(this.oldFitness[i] > this.newFitness[i])
          {
              for(j=0;j<BINARY_DIGITS;j++)
              {
                  newGens[i][j] = oldGens[i][j];
              }
          }
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
          rouletteNumber = Math.random();

          if((float)rouletteNumber <= this.crossoverRate)  //Se determina si se cruza
          {
              rouletteNumber = Math.random();
              for(j=0;j<proportionateFitness.length;j++)
              {
                  if((float)rouletteNumber <= roulette[j])
                  {
                      crossover(this.candidates[i],this.candidates[j]);
                      break;
                  }
              }
          }
      }
  }

  public void crossover(int[] a, int[] b)
  {
      int[] ca = new int[BINARY_DIGITS];
      int[] cb = new int[BINARY_DIGITS];
      int i;

      for(i=0;i<BINARY_DIGITS;i++)
      {
          ca[i] = a[i];
          cb[i] = b[i];
      }

      for(i=0;i<(int)Math.ceil(BINARY_DIGITS/2);i++)
      {
          a[i] = cb[i];
      }
      for(i=0;i<(int)Math.ceil(BINARY_DIGITS/2);i++)
      {
          b[i] = ca[i];
      }
  }

  public void mutation()
  {
      int i,j;
      double randomNumber;

      for(i=0; i<CANDIDATES_NUMBER;i++)
      {
          for(j=0;j<BINARY_DIGITS;j++)
          {
              randomNumber = Math.random();
              if((float)randomNumber <= this.mutationRate)
              {
                  if(candidates[i][j] == 1)
                  {
                      this.candidates[i][j] = 0;
                  }
                  else
                  {
                      this.candidates[i][j] = 1;
                  }
              }
          }
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

  private class MyGenericBehaviour extends Behaviour {


    public void action() {
        evaluatePopulation();


        System.out.println("            Generation " + generation++);
        System.out.println("");
        for(i = 0; i < CANDIDATES_NUMBER; i++)
        {
            System.out.print("Chain:       ");
            System.out.print("Fitness:  ");
            System.out.print("Fitness Prop.:   ");
            System.out.println("");
            for(j = 0; j < BINARY_DIGITS; j++)
            {
                System.out.print(candidates[i][j]);
            }
            System.out.print("      ");
            System.out.print(fitness[i]);
            System.out.print("         ");

            System.out.print(Math.round((proportionateFitness[i])*100.0)/100.0);
            System.out.println("");
            System.out.println("");
        }
        System.out.print("    Total Population Fitness: " + totalFitness);
        System.out.println("");
        System.out.println("");
        System.out.println("----------------------------------------");


        for(i=0;i<CANDIDATES_NUMBER;i++)
        {
            for(j = 0; j < BINARY_DIGITS; j++)
            {
                oldGen[i][j] = candidates[i][j];
            }
        }
        oldFitness = fitness;

        rouletteWheel();
        mutation();
        evaluatePopulation();
        newFitness = fitness;
        survivalOfTheFittest(oldGen,candidates);
    }

    public boolean done() {
        int i;
        for(i=0;i<CANDIDATES_NUMBER;i++)
        {
            if(fitness[i] == BINARY_DIGITS)
            {
                printData(); // Mexicanada de la que no estoy orgulloso :c
                criterionSatisfied = true;
                candidateSolution = candidates[i];
                break;
            }
        }
        return criterionSatisfied;
    }

    public int onEnd() {
        System.out.print("Solution: ");
        for(j = 0; j < BINARY_DIGITS; j++)
        {
            System.out.print(candidateSolution[j]);
        }
        System.out.println("");

        myAgent.doDelete();
        return super.onEnd();
    }

  }    // END of inner class ...Behaviour
}
