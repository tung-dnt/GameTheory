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

public class StorageExecution {
    public static void main(String[] args) throws IOException {
        GameTheoryProblem problem = new GameTheoryProblem("TrafficControl.xlsx", 17);
        // solve using NSGA-II
        NondominatedPopulation result = new Executor().withProblem(problem).withAlgorithm("NSGAII").withMaxEvaluations(50000).distributeOnAllCores().run();

//        printGameTheoryInstances(object);
        printDominantStrategy(problem, result);
        printEquilibriaStrategy(problem, result);
    }

    private static void printGameTheoryInstances(GameTheoryProblem problem) {
        List<NormalPlayer> players = problem.getNormalPlayers();
        InputDataDriver.displayNormalPlayerList(players);
    }

    private static void printDominantStrategy(GameTheoryProblem problem, NondominatedPopulation result) {
        List<NormalPlayer> players = problem.getNormalPlayers();
        int outstandingPlayerIndex = problem.getDominantPlayerIndex();
        NormalPlayer outstandingPlayer = players.get(outstandingPlayerIndex);
        int bestRes = outstandingPlayer.getBestResponse();

        System.out.println("==================================\nBest Solution:");
        System.out.printf("Normal Player %d - Strategy %d\n", outstandingPlayerIndex + 1, bestRes + 1);
        System.out.printf("Payoff: %f\n", outstandingPlayer.getStrategyAt(bestRes).getPayoff());
        System.out.printf("Properties of best strategy: %s\n\n", outstandingPlayer.getStrategyAt(bestRes));
    }

    private static void printEquilibriaStrategy(GameTheoryProblem problem, NondominatedPopulation result) {
        List<NormalPlayer> players = problem.getNormalPlayers();
        int equiPlayerIndex = problem.getNashEquiPlayerIndex();

        NormalPlayer player = players.get(equiPlayerIndex);
        int bestRes = player.getBestResponse();

        System.out.printf("==================================\nMOST EQUILIBRIUM PLAYER:\nSolution: %d\n", equiPlayerIndex + 1);
        System.out.printf("Normal Player %d - Strategy %d\n", equiPlayerIndex + 1, bestRes + 1);
        System.out.printf("Payoff: %f\n", player.getStrategyAt(bestRes).getPayoff());
        System.out.printf("Properties of best strategy: %s\n\n", player.getStrategyAt(bestRes));
    }


}
