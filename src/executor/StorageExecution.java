package executor;

import java.io.IOException;
import java.util.List;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;

public class StorageExecution {
    public static void main(String[] args) throws IOException {
        GameTheoryProblem problem = new GameTheoryProblem("TrafficControl.xlsx", 17);
        // solve using NSGA-II
        NondominatedPopulation result = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(50000)
                .distributeOnAllCores()
                .run();

        System.out.println("\nGAME THEORY INSTANCE:\n" + problem);
        printDominantStrategy(problem, result);
        printEquilibriaStrategy(problem, result);
    }

    private static void printDominantStrategy(GameTheoryProblem problem, NondominatedPopulation result) {
        List<NormalPlayer> players = problem.getNormalPlayers();
        int outstandingPlayerIndex = problem.getDominantPlayerIndex();
        NormalPlayer outstandingPlayer = players.get(outstandingPlayerIndex);
        int bestRes = outstandingPlayer.getBestResponse();

        System.out.println("==================================\nMOST DOMINANT SOLUTION:");
        System.out.printf("Normal Player %d - Strategy %d\n", outstandingPlayerIndex + 1, bestRes + 1);
        System.out.printf("Payoff: %f\n", outstandingPlayer.getStrategyAt(bestRes).getPayoff());
        System.out.printf("Properties of best strategy: %s\n\n", outstandingPlayer.getStrategyAt(bestRes));
    }

    private static void printEquilibriaStrategy(GameTheoryProblem problem, NondominatedPopulation result) {
        List<NormalPlayer> players = problem.getNormalPlayers();
        int equiPlayerIndex = problem.getNashEquiPlayerIndex();

        NormalPlayer player = players.get(equiPlayerIndex);
        int bestRes = player.getBestResponse();

        System.out.printf("==================================\nMOST EQUILIBRIUM PLAYER:\n");
        System.out.printf("Normal Player %d - Strategy %d\n", equiPlayerIndex + 1, bestRes + 1);
        System.out.printf("Payoff: %f\n", player.getStrategyAt(bestRes).getPayoff());
        System.out.printf("Properties of best strategy: %s\n\n", player.getStrategyAt(bestRes));
    }
}
