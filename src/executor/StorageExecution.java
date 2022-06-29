package executor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

public class StorageExecution {
    public static void main(String[] args) throws IOException {
        String inputFile = "OilManagement.xlsx";
        File file = new File(System.getProperty("user.dir") + "/input/" + inputFile);

        GameTheoryProblem problem = new GameTheoryProblem(file.getPath(), 16);
        // solve using NSGA-II
        NondominatedPopulation results = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(50000)
                .distributeOnAllCores()
                .run();
        printEquilibriaStrategy(problem, results);
    }
    private static void printEquilibriaStrategy(GameTheoryProblem problem, NondominatedPopulation result) {
        List<NormalPlayer> players = problem.getNormalPlayers();
        Solution solution = result.get(0);
        double nashEquilibrium = solution.getConstraint(0);

        int equiPlayerIndex = problem.getBestResponse();

        NormalPlayer player = players.get(equiPlayerIndex);
        int bestRes = player.getDominantStrategyIndex();

        System.out.print("==================================\nBEST RESPONSE:\n");
        System.out.printf("Normal Player %d - Strategy %d\n", equiPlayerIndex + 1, bestRes + 1);
        System.out.printf("Best Response Payoff: %.2f\n", player.getStrategyAt(bestRes).getPayoff());
        System.out.printf("NASH EQUILIBRIUM: %.2f", nashEquilibrium);
    }

    private static void printDominantStrategy(GameTheoryProblem problem, NondominatedPopulation result) {
        List<NormalPlayer> players = problem.getNormalPlayers();
        int outstandingPlayerIndex = problem.getDominantPlayerIndex();
        NormalPlayer outstandingPlayer = players.get(outstandingPlayerIndex);
        int bestRes = outstandingPlayer.getDominantStrategyIndex();

        System.out.println("==================================\nMOST DOMINANT SOLUTION:");
        System.out.printf("Normal Player %d - Strategy %d\n", outstandingPlayerIndex + 1, bestRes + 1);
        System.out.printf("Payoff: %.2f\n", outstandingPlayer.getStrategyAt(bestRes).getPayoff());
        System.out.printf("Properties of dominant strategy: %s\n\n", outstandingPlayer.getStrategyAt(bestRes));
    }

    private static void printRemaining(GameTheoryProblem problem) {
        List<NormalPlayer> players = problem.getNormalPlayers();
        int[] bestResponses = problem.getRemainAlliances();
        int equiPlayerIndex = problem.getBestResponse();

        for (int i = 0; i < bestResponses.length; ++i) {
            if (i == equiPlayerIndex) continue;
            System.out.printf("Normal Player %d - Strategy %d\n", i + 1, bestResponses[i] + 1);
            System.out.printf("Best Response Payoff: %.2f\n", players.get(i).getStrategyAt(bestResponses[i]).getPayoff());
            System.out.printf("Properties of best response: %s\n\n", players.get(i).getStrategyAt(bestResponses[i]));
        }
    }
}
