package knapsack;

import library.Address;
import library.Message;

public class ResponseMFMessage extends Message {
	private Individual[] population;
	private Address replyTo;
	private int startPos;
	private int endPos;
	
	public ResponseMFMessage(Individual[] population, Address address, int startPos, int endPos) {
		this.population = population;
		replyTo = address;
		this.startPos = startPos;
		this.endPos = endPos;
	}

	public Individual[] getPartOfPopulation() {
		return population;
	}

	public Address getReplyTo() {
		return replyTo;
	}

	public int getStartPos() {
		return startPos;
	}

	public int getEndPos() {
		return endPos;
	}
	

}
