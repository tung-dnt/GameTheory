package allocation.storage;

import java.io.IOException;
import java.io.InputStream;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.util.Vector;

/**
 * Example of binary optimization using the {@link StorageSubset} problem on the
 * {@code storage.100.2} instance.
 */
public class StorageExecution {

	/**
	 * Starts the example running the storage problem.
	 * 
	 * @param args the command line arguments
	 * @throws IOException if an I/O error occurred
	 */
	public static void main(String[] args) throws IOException {
		// open the file containing the storage problem instance
		InputStream input = StorageSubset.class.getResourceAsStream(
				"storage.20.2");
		
		if (input == null) {
			System.err.println("Unable to find the file storage.20.2");
			System.exit(-1);
		}
				
		// solve using NSGA-II
		NondominatedPopulation result = new Executor()
				.withProblemClass(StorageSubset.class, input)
				.withAlgorithm("NSGAII")
				.withMaxEvaluations(50000)
				.distributeOnAllCores()
				.run();

		// print the results
		for (int i = 0; i < result.size(); i++) {
			Solution solution = result.get(i);
			double[] objectives = solution.getObjectives();
					
			// negate objectives to return them to their maximized form
			objectives = Vector.negate(objectives);
					
			System.out.println("Solution " + (i+1) + ":");
			System.out.println("    Storage 1 Profit:  " + objectives[0]);
			System.out.println("    Storage 2 Profit:  " + objectives[1]);
			System.out.println("    Selected items: " + solution.getVariable(0));
		}
	}

}
