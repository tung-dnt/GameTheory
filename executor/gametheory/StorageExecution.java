package gametheory;

import java.io.IOException;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.util.Vector;

public class StorageExecution {

	/**
	 * Starts the example running the storage problem.
	 * 
	 * @param args the command line arguments
	 * @throws IOException if an I/O error occurred
	 */
	public static void main(String[] args) throws IOException {
		
		GameTheoryObject object = new GameTheoryObject("newData.xlsx", 16);
		
		if (object == null) {
			System.err.println("Unable to generate GameTheoryObject");
			System.exit(-1);
		}
		// solve using NSGA-II
		NondominatedPopulation result = new Executor()
				.withProblem(object)
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
