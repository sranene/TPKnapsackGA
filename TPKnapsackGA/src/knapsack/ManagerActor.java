package knapsack;

import java.util.Arrays;
import java.util.Random;

import library.Actor;
import library.Address;
import library.Message;
import library.SystemKillMessage;

public class ManagerActor extends Actor {

	private static Address customer;
	private static int GENERATION;
	private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors(); // 12
	
	private Individual[] population;
	
	private MFActor[] mfActors = new MFActor[NUM_THREADS];
	private CMActor[] cmActors = new CMActor[NUM_THREADS];
	
	private int nextFreeEmployee = 0;
	
	private int messagesMFReceived = 0;
	private int messagesCMReceived = 0;
	

	public ManagerActor() {
		
	}
	
	private void createNewMFActors() {
		for (int i=0; i<NUM_THREADS; i++) {
			this.mfActors[i] = new MFActor(this.getAddress());
		}
	}

	private void createNewCMActors() {
		for (int i=0; i<NUM_THREADS; i++) {
			this.cmActors[i] = new CMActor(this.getAddress());
		}
	}
	
	@Override
	protected void handleMessage(Message m) {
		if (m instanceof SystemKillMessage)  {
			for (MFActor e : mfActors) {
				e.getAddress().sendMessage(m);
			}
			for (CMActor e : cmActors) {
				e.getAddress().sendMessage(m);
			}
		} else if (m instanceof WorkOnPopulationMessage) {
			WorkOnPopulationMessage m2 = (WorkOnPopulationMessage) m;
			//System.out.println("ManagerActor received Costumer Message with gen: " + m2.getNumberGeneration());
			createNewMFActors();
			customer = m2.getReplyTo();
			GENERATION = m2.getNumberGeneration();
			
			population = m2.getPopulation().clone();

	        int chunkSize = population.length / mfActors.length;
	        
	        for (int i = 0; i < mfActors.length; i++) {
	            int start = i * chunkSize;
	            int end = (i == cmActors.length - 1) ? population.length - 1 : ((i + 1) * chunkSize) - 1;

	            Individual[] chunk = Arrays.copyOfRange(population, start, end + 1);
	            
	            mfActors[i].getAddress().sendMessage(new CalculateMessage(chunk, this.getAddress(), start, end));
	        
	        }
	        
		} else if (m instanceof ResponseMFMessage) {
			ResponseMFMessage m2 = (ResponseMFMessage) m;
			//System.out.println("ManagerActor received MFR " + m2.getPartOfPopulation());
			
			m2.getReplyTo().sendMessage(new SystemKillMessage());
			
			messagesMFReceived++;
			Individual[] partOfPopulationClone = m2.getPartOfPopulation().clone();
        	int startPos = m2.getStartPos();
        	int endPos = m2.getEndPos();
			
			
			for (int i = startPos, j = 0; i < endPos; j++, i++) {
				population[i] =  partOfPopulationClone[j];
			}

			if (messagesMFReceived == mfActors.length) {
				messagesMFReceived = 0;
				
				createNewCMActors();
				
				// Print the best individual so far.
				Individual best = bestOfPopulation();
				System.out.println("Best at generation " + GENERATION + " is " + best + " with " + best.fitness);
				
				Individual[] newPopulation = population;
				newPopulation[0] = best;
				
		        int chunkSize = newPopulation.length / cmActors.length;

		        for (int i = 0; i < cmActors.length; i++) {
		            int start = (i == 0) ? (i * chunkSize) + 1 : (i * chunkSize);
		            int end = (i == cmActors.length - 1) ? newPopulation.length - 1 : ((i + 1) * chunkSize) - 1;
		            
		            Individual[] chunk = Arrays.copyOfRange(newPopulation, start, end + 1);
		            
		            cmActors[i].getAddress().sendMessage(new CalculateMessage(chunk, this.getAddress(), start, end));
		        }
				
			}
			
		} else if (m instanceof ResponseCMMessage) {
			ResponseCMMessage m2 = (ResponseCMMessage) m;
			//System.out.println("ManagerActor received CMR " + m2.getPartOfPopulation());
			
			m2.getReplyTo().sendMessage(new SystemKillMessage());
			
			messagesCMReceived++;
			Individual[] partOfPopulationClone = m2.getPartOfPopulation().clone();
        	int startPos = m2.getStartPos();
        	int endPos = m2.getEndPos();
        	
			for (int i = startPos, j = 0; i < endPos; j++, i++) {
				population[i] =  partOfPopulationClone[j];
			}
			
			if (messagesCMReceived == cmActors.length) {
				messagesCMReceived = 0;
				Individual[] finalPopulation = population.clone();

				ManagerActor.customer.sendMessage(new ResponseMessage(finalPopulation));
			}
			
		}

	}
	
	@Override
	protected boolean handleException(Exception e) {
		System.out.println("ManagerActor is taking care of " + e);
		/*if (e instanceof MessageNotProcessedException) {
			MessageNotProcessedException e2 = (MessageNotProcessedException) e;
			for (int i=0; i<population.length/NUM_THREADS; i++) {
				if (!employees[i].isAlive()) {
					employees[i] = new Employee(this.getAddress());
				}
			}
			employees[0].getAddress().sendMessage(e2.getMessageLost());
		}*/
		// Restart the dead actor
		return true;
	}
	
	private Individual bestOfPopulation() {
		/*
		 * Returns the best individual of the population.
		 */
		Individual best = population[0];
		for (Individual other : population) {
			if (other.fitness > best.fitness) {
				best = other;
			}
		}
		return best;
	}
	
}