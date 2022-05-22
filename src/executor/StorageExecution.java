package executor;
/* *
 *Starts the command line program for solving strategic games.
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
    private static void printResult(GameTheoryProblem object, NondominatedPopulation result) {
        System.out.println("\nBEST RESPONSES\n");

        for (int i = 0; i < result.size(); i++) {
            List<NormalPlayer> players = object.getNormalPlayers();
            Solution solution = result.get(i);
            double[] objectives = solution.getObjectives();

            for (int j = 0; j < objectives.length; ++j) {
                // Get Best response of current player
                NormalPlayer currentPlayer = players.get(j);
                Strategy strategy = currentPlayer.getStrategyAt(j);
                if (strategy == null) continue;
                System.out.printf("Player %d - Strategy %d\n", i + 1, j + 1);
                System.out.printf("%s\nPayoff: %f", strategy.toString(), strategy.getPayoff());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        GameTheoryProblem object = new GameTheoryProblem("TrafficControl.xlsx", 17);
        // solve using NSGA-II
        NondominatedPopulation result = new Executor()
                .withProblem(object)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(50000)
                .distributeOnAllCores().run();
        // print the results
        printResult(object, result);
    }
}
