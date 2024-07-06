package knapsack;

import library.Message;

public class ResponseMessage extends Message {
	private Individual[] population;
	
	public ResponseMessage(Individual[] population) {
		this.population = population;
	}

	public Individual[] getPopulation() {
		return population;
	}
}
