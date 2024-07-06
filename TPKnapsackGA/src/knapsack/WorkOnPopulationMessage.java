package knapsack;

import library.Address;
import library.Message;

public class WorkOnPopulationMessage extends Message {
	private Individual[] population;
	private int numberGeneration;
	private Address replyTo;
	
	public WorkOnPopulationMessage(Individual[] population, int numberGeneration, Address address) {
		this.population = population;
		this.numberGeneration = numberGeneration;
		replyTo = address;
	}

	public Individual[] getPopulation() {
		return population;
	}

	public Address getReplyTo() {
		return replyTo;
	}

	public int getNumberGeneration() {
		return numberGeneration;
	}
	

}
