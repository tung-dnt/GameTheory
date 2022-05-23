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
        System.out.println("\nNORMAL PLAYERS BEST RESPONSES");
        List<NormalPlayer> players = object.getNormalPlayers();
        int outstandingPlayerIndex = object.getDominantPlayerIndex();
        NormalPlayer outstandingPlayer = players.get(outstandingPlayerIndex);
        int bestRes = outstandingPlayer.getBestResponse();
        Solution solution = result.get(0);
        double[] objectives = solution.getObjectives();

        for (int j = 0; j < objectives.length; ++j) {
            // Get Best response of current player
            NormalPlayer currentPlayer = players.get(j);
            double payoff = currentPlayer.getStrategyAt((int) objectives[j]).getPayoff();
            String strategy = currentPlayer.getStrategyAt((int) objectives[j]).toString();

            System.out.printf("Normal Player %d - Strategy %d\n", j + 1, (int) objectives[j] + 1);
            System.out.printf("Payoff: %f\n", payoff);
            System.out.printf("Properties of best strategy: %s\n\n", strategy);
        }

        System.out.println("==================================\nBest Solution:");
        System.out.printf("Normal Player %d - Strategy %d\n", outstandingPlayerIndex + 1, bestRes + 1);
        System.out.printf("Payoff: %f\n", outstandingPlayer.getStrategyAt(bestRes).getPayoff());
        System.out.printf("Properties of best strategy: %s\n\n", outstandingPlayer.getStrategyAt(bestRes));
    }

    public static void main(String[] args) throws IOException {
        GameTheoryProblem object = new GameTheoryProblem("TrafficControl.xlsx", 17);
        // solve using NSGA-II
        NondominatedPopulation result = new Executor()
                .withProblem(object)
                .withAlgorithm("GA")
                .withMaxEvaluations(50000)
                .distributeOnAllCores().run();
        printResult(object, result);
    }
}
