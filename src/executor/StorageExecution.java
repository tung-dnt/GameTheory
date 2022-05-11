package executor;

import java.io.IOException;
import java.util.List;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

public class StorageExecution {

	/**
	 * Starts the example running the storage problem.
	 * 
	 * @param args the command line arguments
	 * @throws IOException if an I/O error occurred
	 */
	public static void main(String[] args) throws IOException {

		GameTheoryProblem object = new GameTheoryProblem("newData.xlsx", 16);

		if (object == null) {
			System.err.println("Unable to generate GameTheoryProblem");
			System.exit(-1);
		}
		// solve using NSGA-II
		NondominatedPopulation result = new Executor().withProblem(object).withAlgorithm("NSGAII")
				.withMaxEvaluations(50000).distributeOnAllCores().run();

		System.out.println("\nNORMAL PLAYERS BEST RESPONSES\n");
		// print the results
		for (int i = 0; i < result.size(); i++) {
			List<NormalPlayer> players = object.getNormalPlayers();

			Solution solution = result.get(i);
			double[] objectives = solution.getObjectives();

			for (int j = 0; j < objectives.length; ++j) {
				// Get Best response of current player
				NormalPlayer currentPlayer = players.get(j);
				double payoff = currentPlayer.getStrategyAt((int) objectives[j]).getPayoff();
				String strategy = currentPlayer.getStrategyAt((int) objectives[j]).toString();

				System.out.printf("Normal Player %d - Strategy %d\n", j + 1, (int) objectives[j]);
				System.out.printf("Profit: %d\n", (int) payoff);
				System.out.printf("Properties of best strategy: %s\n\n", strategy);
			}
		}
	}

}
