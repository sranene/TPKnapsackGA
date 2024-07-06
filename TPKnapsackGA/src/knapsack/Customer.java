package knapsack;

import java.util.Random;

import library.Actor;
import library.Address;
import library.Message;
import library.SystemKillMessage;

public class Customer extends Actor  {

    //private boolean executionCompleted = false;
	
	private static final int N_GENERATIONS = 500;
	private static final int POP_SIZE = 100000;
	private Individual[] population = new Individual[POP_SIZE];
	
	private Address serverAddress;
	private int messagesReceived = 0;
	
	private Random r = new Random();
	
	public Customer(Address address) {
		serverAddress = address;
		for (int i = 0; i < POP_SIZE; i++) {
			population[i] = Individual.createRandom(r);
		}
	}


	@Override
	protected void handleMessage(Message m) {
		if (m instanceof BootstrapMessage) {
			//System.out.println("Starting Customer");
			Individual[] populationTemp = population.clone();

			serverAddress.sendMessage(new WorkOnPopulationMessage(populationTemp, 0, this.getAddress()));
			
		} else if (m instanceof ResponseMessage) {
			ResponseMessage m2 = (ResponseMessage) m;
			//System.out.println("Customer received " + m2.getPopulation());
		    population = m2.getPopulation().clone();
			messagesReceived++;
			if (messagesReceived < N_GENERATIONS) {
				int gen = messagesReceived;
				Individual[] populationTemp = population.clone();
				serverAddress.sendMessage(new WorkOnPopulationMessage(populationTemp, gen, this.getAddress()));
			}
			if (messagesReceived == N_GENERATIONS) {
				serverAddress.sendMessage(new SystemKillMessage());
				this.getAddress().sendMessage(new SystemKillMessage());
				//executionCompleted = true;
			}
		}
	}

    /*public boolean isExecutionCompleted() {
        return executionCompleted;
    }*/
	

}
