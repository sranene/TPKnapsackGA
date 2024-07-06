package knapsack;

import java.util.Random;

/*
 * This file is not supposed to be changed. 
 */

public class Individual {

	// This is the definition of the problem
	public static final int GENE_SIZE = 1000; // Number of possible items
	public static int[] VALUES = new int[GENE_SIZE];
	public static int[] WEIGHTS = new int[GENE_SIZE];
	public static int WEIGHT_LIMIT = 300;

	static {
		// This code initializes the problem.
		Random r = new Random(1L);
		for (int i = 0; i < GENE_SIZE; i++) {
			VALUES[i] = r.nextInt(100);
			WEIGHTS[i] = r.nextInt(100);
		}
	}

	/*
	 * This array corresponds to whether the object at a given index
	 * is selected to be placed inside the knapsack.
	 * The goal is to find the items that maximize the total value with
	 * surpassing the weight limit.
	 */
	public boolean[] selectedItems = new boolean[GENE_SIZE];
	public int fitness;

	public static Individual createRandom(Random r) {
		Individual ind = new Individual();
		for (int i = 0; i < GENE_SIZE; i++) {
			ind.selectedItems[i] = r.nextBoolean();
		}
		return ind;
	}


}
