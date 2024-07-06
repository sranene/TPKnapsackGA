package knapsack;

import java.util.Random;

import library.Actor;
import library.Address;
import library.Message;

public class CMActor extends Actor  {
	
	private static final double PROB_MUTATION = 0.5;
	private static final int TOURNAMENT_SIZE = 3;
	private static final int POP_SIZE = 100000;
	
	private Random r = new Random();

	public CMActor(Address address) {
		super(address);
	}

	@Override
	protected void handleMessage(Message m) {
		//System.out.println("CMActor " + this + " received " + m);
		
		if (m instanceof CalculateMessage) {
			CalculateMessage m2 = ((CalculateMessage) m);
			
        	int startPos = m2.getStartPos();
        	int endPos = m2.getEndPos();
			Individual[] partOfPopulation = m2.getPartOfPopulation().clone();
			Individual[] partOfPopulationCalculated = new Individual[partOfPopulation.length];
			
			for(int i = 0; i < partOfPopulation.length; i++) {
				Individual parent1 = tournament(partOfPopulation, TOURNAMENT_SIZE, r);
				Individual parent2 = tournament(partOfPopulation, TOURNAMENT_SIZE, r);
				partOfPopulationCalculated[i] = crossover(parent1, parent2, r);
				if (r.nextDouble() < PROB_MUTATION) {
					partOfPopulationCalculated[i] = mutate(r, partOfPopulationCalculated[i]);
				}
            }
			//System.out.println("CMActor is sending CMR to ManagerActor " + partOfPopulationCalculated);
			m2.getReplyTo().sendMessage(new ResponseCMMessage(partOfPopulationCalculated, this.getAddress(), startPos, endPos));
			
		}
		
	}
	
	private Individual tournament(Individual[] population, int tournamentSize, Random r) {
		/*
		 * In each tournament, we select tournamentSize individuals at random, and we
		 * keep the best of those.
		 */
		Individual best = population[r.nextInt(population.length)]; /////////////////
		for (int i = 0; i < tournamentSize; i++) {
			Individual other = population[r.nextInt(population.length)]; ///////////////////
			if (other.fitness > best.fitness) {
				best = other;
			}
		}
		return best;
	}
	

	/*
	 * Generates a random point in the genotype (selected Items)
	 * Until that point, uses genes from dad (current)
	 * After that point, uses genes from mom (mate)
	 */
	public Individual crossover(Individual parent1, Individual parent2, Random r) {
		Individual child = new Individual();
		int crossoverPoint = r.nextInt(parent1.GENE_SIZE);
		for (int i = 0; i < parent1.GENE_SIZE; i++) {
			if (i < crossoverPoint) {
				child.selectedItems[i] = parent1.selectedItems[i];
			} else {
				child.selectedItems[i] = parent2.selectedItems[i];
			}
		}
		return child;
	}

	public Individual mutate(Random r, Individual parent) {
		int mutationPoint = r.nextInt(parent.GENE_SIZE);
		parent.selectedItems[mutationPoint] = !parent.selectedItems[mutationPoint];
		return parent;
	}
	

}
