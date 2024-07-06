package knapsack;

import library.Actor;
import library.Address;
import library.Message;

public class MFActor extends Actor  {
	

	public MFActor(Address address) {
		super(address);
	}

	@Override
	protected void handleMessage(Message m) {
		//System.out.println("MFActor " + this + " received " + m);
		
		if (m instanceof CalculateMessage) {

			CalculateMessage m2 = ((CalculateMessage) m);
			
        	int startPos = m2.getStartPos();
        	int endPos = m2.getEndPos();
			Individual[] partOfPopulation = m2.getPartOfPopulation().clone();
			Individual[] partOfPopulationCalculated = new Individual[partOfPopulation.length];
			
			for(int i = 0; i < partOfPopulation.length; i++) {
				partOfPopulationCalculated[i] = measureFitness(partOfPopulation[i]);
            }
			
			//System.out.println("MFActor is sending MFR to ManagerActor " + partOfPopulationCalculated);
			m2.getReplyTo().sendMessage(new ResponseMFMessage(partOfPopulationCalculated, this.getAddress(), startPos, endPos));
			
		}
		
	}
	
	public Individual measureFitness(Individual individual) {
		int totalWeight = 0;
		int totalValue = 0;
		for (int i = 0; i < individual.GENE_SIZE; i++) {
			if (individual.selectedItems[i]) {
				totalValue += individual.VALUES[i];
				totalWeight += individual.WEIGHTS[i];
			}
		}
		if (totalWeight > individual.WEIGHT_LIMIT) {
			individual.fitness = -(totalWeight - individual.WEIGHT_LIMIT);
			return individual;
		} else {
			individual.fitness = totalValue;
			return individual;
		}
	}

}
