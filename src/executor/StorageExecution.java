package executor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;

public class StorageExecution {
    public static void main(String[] args) throws IOException {
        String inputFile = "OilManagement.xlsx";
        File file = new File(System.getProperty("user.dir") + "/input/" + inputFile);

        GameTheoryProblem problem = new GameTheoryProblem(file.getPath(), 2);
        // solve using NSGA-II
        NondominatedPopulation results = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(50000)
                .distributeOnAllCores()
                .run();

//        System.out.println("\nGAME THEORY INSTANCE:\n" + problem);
//        printDominantStrategy(problem, results);
        printEquilibriaStrategy(problem, results);
    }
    //CLOUD ALLOCATION

    //

    //TRAFFIC CONTROL
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

    private static void printEquilibriaStrategy(GameTheoryProblem problem, NondominatedPopulation result) {
        List<NormalPlayer> players = problem.getNormalPlayers();
        int equiPlayerIndex = problem.getBestResponse();

        NormalPlayer player = players.get(equiPlayerIndex);
        int bestRes = player.getDominantStrategyIndex();

        System.out.print("==================================\nBEST RESPONSE:\n");
        System.out.printf("Normal Player %d - Strategy %d\n", equiPlayerIndex + 1, bestRes + 1);
        System.out.printf("Best Response Payoff: %.2f\n", player.getStrategyAt(bestRes).getPayoff());
        System.out.printf("Properties of best response: %s\n\n", player.getStrategyAt(bestRes));
    }
}
