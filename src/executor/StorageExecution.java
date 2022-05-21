package executor;
/**
 * Starts the command line program for solving strategic games.
 *
 * @param args
 *            The first argument is the game to solve, as path to a
 *            json-file. The second argument is optional and specifies a
 *            specific support sets to solve the game. If not given, the
 *            game is solved for all possible support set combinations.<br/>
 *            <br/>
 *            An example call would be:
 *            <tt>java SolveGame matching-pennies.json
 *            "[H,T][T]"</tt>
 */

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
	private static void printResult(GameTheoryProblem object,NondominatedPopulation result){
		for (int i = 0; i < result.size(); i++) {
			List<NormalPlayer> players = object.getNormalPlayers();

			Solution solution = result.get(i);
			double[] objectives = solution.getObjectives();

			for (int j = 0; j < objectives.length; ++j) {
				// Get Best response of current player
				NormalPlayer currentPlayer = players.get(j);
				double payoff = currentPlayer.getStrategyAt((int) objectives[j]).getPayoff();
				String strategy = currentPlayer.getStrategyAt((int) objectives[j]).toString();
				System.out.printf("Normal Player %d - Strategy %d\n", j + 1, (int) objectives[j] + 1) ;
				System.out.printf("Profit: %d\n", (int) payoff);
				System.out.printf("Properties of best strategy: %s\n\n", strategy);
			}
		}
	}
	public static void main(String[] args) throws IOException {

		GameTheoryProblem object = new GameTheoryProblem("newData.xlsx", 17);

		if (object == null) {
			System.err.println("Unable to generate Game Theory Problem");
			System.exit(-1);
		}
		// solve using NSGA-II
		NondominatedPopulation result = new Executor().withProblem(object).withAlgorithm("NSGAII")
				.withMaxEvaluations(50000).distributeOnAllCores().run();

		System.out.println("\nNORMAL PLAYERS BEST RESPONSES\n");
		// print the results
		printResult(object, result);
	}

}
