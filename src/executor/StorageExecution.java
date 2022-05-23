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
        GameTheoryProblem object = new GameTheoryProblem("TrafficControl.xlsx", 17);
        // solve using NSGA-II
        NondominatedPopulation result = new Executor()
                .withProblem(object)
                .withAlgorithm("GA")
                .withMaxEvaluations(50000)
                .distributeOnAllCores().run();
//        printGametheoryInstances(object);
        printEquilibriaStrategy(object, result);
    }

    private static void printGametheoryInstances(GameTheoryProblem object) {
        List<NormalPlayer> players = object.getNormalPlayers();
        InputDataDriver.displayNormalPlayerList(players);
    }

    private static void printDominantStrategy(GameTheoryProblem object, NondominatedPopulation result) {
        List<NormalPlayer> players = object.getNormalPlayers();
        int outstandingPlayerIndex = object.getDominantPlayerIndex();
        NormalPlayer outstandingPlayer = players.get(outstandingPlayerIndex);
        int bestRes = outstandingPlayer.getBestResponse();

        System.out.println("==================================\nBest Solution:");
        System.out.printf("Normal Player %d - Strategy %d\n", outstandingPlayerIndex + 1, bestRes + 1);
        System.out.printf("Payoff: %f\n", outstandingPlayer.getStrategyAt(bestRes).getPayoff());
        System.out.printf("Properties of best strategy: %s\n\n", outstandingPlayer.getStrategyAt(bestRes));
    }

    private static void printEquilibriaStrategy(GameTheoryProblem object, NondominatedPopulation result) {
        List<NormalPlayer> players = object.getNormalPlayers();
        List<Integer> equiPlayerIndex = object.getNashEquiPlayerIndex();

        for (int i = 0; i < equiPlayerIndex.size(); ++i) {
            int playerIndex = equiPlayerIndex.get(i);
            NormalPlayer player = players.get(playerIndex);
            int bestRes = player.getBestResponse();
            System.out.printf("==================================\nSolution :%d\n", i+1);
            System.out.printf("Normal Player %d - Strategy %d\n", playerIndex + 1, bestRes + 1);
            System.out.printf("Payoff: %f\n", player.getStrategyAt(bestRes).getPayoff());
            System.out.printf("Properties of best strategy: %s\n\n", player.getStrategyAt(bestRes));
        }
    }

}
