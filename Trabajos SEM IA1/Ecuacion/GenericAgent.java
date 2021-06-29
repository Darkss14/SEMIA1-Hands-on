package examples.behaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class GenericAgent extends Agent {

    private final int CANDIDATES_NUMBER = 6;
    private final int MONOMIES_NUMBER   = 6;

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

    private int a,b,c,d,e,f;
    private int result;

    private boolean criterionSatisfied;
    private int expectedResult;

    public int i,j;

  protected void setup() {

    System.out.println("Agent "+getLocalName()+" started.");
    this.generation = 0;
    this.elitismValue = 0.1f;  // "f" para indicar que el número es float
    this.crossoverRate = 0.95f;
    this.mutationRate = 0.1f;
    this.oldGen = new int[CANDIDATES_NUMBER][MONOMIES_NUMBER];
    this.criterionSatisfied = false;
    this.expectedResult = 30;


    candidates = produceInitialRandomPopulation();
    generation++;

    addBehaviour(new MyGenericBehaviour());
  }

  public void printData()
  {
      System.out.println("                   Generation " + generation++);
      System.out.println("");
      for(i = 0; i < CANDIDATES_NUMBER; i++)
      {
          a = candidates[i][0];
          b = candidates[i][1];
          c = candidates[i][2];
          d = candidates[i][3];
          e = candidates[i][4];
          f = candidates[i][5];
          result = a +2*b -3*c +d +4*e +f;

          System.out.print("          Operation:           ");
          System.out.print("Fitness:  ");
          System.out.print("Fitness Prop.:   ");
          System.out.println("");
          System.out.printf(" %d +2*%d -3*%d +%d +4*%d +%d =%d", a,b,c,d,e,f,result);
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
      System.out.println("------------------------------------------------------");
  }

  public int binaryToInt(int[] binaryNumber)
  {
      int intNumber, i;
      int multiplier;

      for(i=0,intNumber=0,multiplier=1; i<MONOMIES_NUMBER; i++)
      {
          intNumber+=(binaryNumber[i] * multiplier);
          multiplier*=2;
      }
      return intNumber;
  }

  public int[] revertIntArray(int[] array)
  {
      int i,j;
      int[] reverseArray = new int[MONOMIES_NUMBER];

      for(i=0,j=MONOMIES_NUMBER-1;i<MONOMIES_NUMBER;i++,j--)
      {
          reverseArray[i] = array[j];
      }
      return reverseArray;
  }

  public void evaluatePopulation()
  {
      this.totalFitness = 0;
      this.fitness = new int[CANDIDATES_NUMBER];
      this.proportionateFitness = new float[CANDIDATES_NUMBER];
      for(i = 0; i < CANDIDATES_NUMBER; i++)
      {
          a = candidates[i][0];
          b = candidates[i][1];
          c = candidates[i][2];
          d = candidates[i][3];
          e = candidates[i][4];
          f = candidates[i][5];

          result = a + 2*b -3*c + d + 4*e + f;
          fitness[i] = 100 - Math.abs(result - this.expectedResult);
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
          // El número más cercano a 100 es el que se queda
          if(Math.abs(100 - this.oldFitness[i]) < Math.abs(100 - this.newFitness[i]))
          {
              for(j=0;j<MONOMIES_NUMBER;j++)
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
      auxArray = new int[MONOMIES_NUMBER];

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
      int[] ca = new int[MONOMIES_NUMBER];
      int[] cb = new int[MONOMIES_NUMBER];
      int i;

      for(i=0;i<MONOMIES_NUMBER;i++)
      {
          ca[i] = a[i];
          cb[i] = b[i];
      }

      for(i=0;i<(int)Math.ceil(MONOMIES_NUMBER/2);i++)
      {
          a[i] = cb[i];
      }
      for(i=0;i<(int)Math.ceil(MONOMIES_NUMBER/2);i++)
      {
          b[i] = ca[i];
      }
  }

  public void mutation()
  {
      int i,j;
      int max, min, range, newNumber;
      double randomNumber;

      min = 1;
      max = 9;
      range = max - min + 1;

      for(i=0; i<CANDIDATES_NUMBER;i++)
      {
          for(j=0;j<MONOMIES_NUMBER;j++)
          {
              randomNumber = Math.random();
              if((float)randomNumber <= this.mutationRate)
              {
                  do
                  {
                      newNumber = (int)(Math.random() * range) + min;
                  }
                  while (this.candidates[i][j] == newNumber);
                  // Si el nuevo número es igual al anterior, entonces se repite
                  this.candidates[i][j] = newNumber;
              }
          }
      }
  }

  private int[][] produceInitialRandomPopulation()
  {
      int[][] candidates_equations;
      int i,j;
      int max, min;
      int aux, range;

      candidates_equations = new int[CANDIDATES_NUMBER][MONOMIES_NUMBER];
      max = 30;
      min = 0;
      range = max - min + 1;

      for(i=0;i < CANDIDATES_NUMBER; i++)
      {
          for(j=0; j < MONOMIES_NUMBER; j++)
          {
              aux = (int)(Math.random() * range) + min;
              candidates_equations[i][j] = aux;
          }
      }

      return candidates_equations;
  }

  private class MyGenericBehaviour extends Behaviour {


    public void action() {
        evaluatePopulation();
        printData();
        for(i=0;i<CANDIDATES_NUMBER;i++)
        {
            for(j = 0; j < MONOMIES_NUMBER; j++)
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
            if(fitness[i] == 100)//El fitness de 100 representa que el resultado es el esperado
            {
                criterionSatisfied = true;
                candidateSolution = candidates[i];
                break;
            }
        }
        return criterionSatisfied;
    }

    public int onEnd() {
        printData();
        System.out.print("Solution: ");
        a = candidateSolution[0];
        b = candidateSolution[1];
        c = candidateSolution[2];
        d = candidateSolution[3];
        e = candidateSolution[4];
        f = candidateSolution[5];
        result = a +2*b -3*c +d +4*e +f;
        System.out.print("   Solution: ");
        System.out.printf(" %d +2*%d -3*%d +%d +4*%d +%d =%d", a,b,c,d,e,f,result);
        System.out.println("");
        myAgent.doDelete();
        return super.onEnd();
    }
  }    // END of inner class ...Behaviour
}
