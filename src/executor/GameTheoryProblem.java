package executor;

//DOCUMENT: GameTheoryProblem.txt

import java.io.IOException;
import java.util.*;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

public class GameTheoryProblem implements Problem {
    private SpecialPlayer specialPlayer;
    private List<NormalPlayer> normalPlayers;
    private List<Conflict> conflictSet;
    //Store average pure payoff diff in descending order
    private List<Double> playerAvgDiffs;

    public GameTheoryProblem(String path, int startRow) throws IOException {
        super();
        if (Objects.equals(path, "")) {
            System.err.println("INVALID INPUT PATH FOUND: Unable to generate Game Theory Problem");
            System.exit(-1);
        }
        // Load user input data from .xlsx file
        load(path, startRow);
        eliminateConflictStrategies();
    }

    private void load(String path, int startRow) throws IOException {
        startRow--; //Since row index start from 0 --> index = startRow-1
        InputDataDriver driver = new InputDataDriver(path);
        // WARNING: Must not modify these variables
        boolean isSpecialPlayerExist = driver.getValueOfCoordinate(startRow, 0) != 0;
        int NORMAL_PLAYER_START_ROW = isSpecialPlayerExist ? startRow + 3 : startRow + 1;
        int numberOfNP = driver.getValueOfCoordinate(NORMAL_PLAYER_START_ROW, 0).intValue();
        int CONFLICT_SET_START_ROW = isSpecialPlayerExist ? startRow + 5 + numberOfNP : startRow + 3 + numberOfNP;

        if (isSpecialPlayerExist) {
            specialPlayer = driver.loadSpecialPlayerFromFile(startRow);
            specialPlayer.displayInf();
        }

        List<Double> normalPlayerWeights = driver.loadNormalPlayerWeights(NORMAL_PLAYER_START_ROW);
        normalPlayers = driver.loadNormalPlayersFromFile(NORMAL_PLAYER_START_ROW, normalPlayerWeights);
        conflictSet = driver.loadConflictSetFromFile(CONFLICT_SET_START_ROW);
    }

    private void eliminateConflictStrategies() {
        if (conflictSet != null) {
            for (Conflict conflict : conflictSet) {
                NormalPlayer evaluatingLeftPlayer = normalPlayers.get(conflict.getLeftPlayer());
                NormalPlayer evaluatingRightPlayer = normalPlayers.get(conflict.getRightPlayer());
                int leftConflictStrat = conflict.getLeftPlayerStrategy();
                int rightConflictStrat = conflict.getRightPlayerStrategy();

                // IF STRATEGY BELONG TO SPECIAL PLAYER -> DON'T REMOVE
                // Set conflict strategy of right player to null
                if (evaluatingLeftPlayer.getStrategyAt(leftConflictStrat) != null && conflict.getLeftPlayer() > -1) {
                    evaluatingLeftPlayer.removeStrategiesAt(leftConflictStrat);
                }
                // Set conflict strategy of right player to null
                if (evaluatingRightPlayer.getStrategyAt(rightConflictStrat) != null && conflict.getRightPlayer() > -1) {
                    evaluatingRightPlayer.removeStrategiesAt(rightConflictStrat);
                }
            }
            //Completely remove all inappropriate strategies from Evaluating Strategies
            for (NormalPlayer player : normalPlayers)
                player.removeAllNull();
        }
    }

    private double computeNashEquilibrium() {
        double nash;
        List<Double> playerAvgDiffs = new ArrayList<>();
        //Set payoff for every normal player
        for (NormalPlayer player : normalPlayers) {
            double playerAvgDiff = 0;
            for (NormalPlayer opponent : normalPlayers) {
                playerAvgDiff += Math.abs(player.getPurePayoff() - opponent.getPurePayoff());
            }
            playerAvgDiff /= normalPlayers.size();//529
            playerAvgDiffs.add(playerAvgDiff);

        }
        nash = Collections.min(playerAvgDiffs);

        if (specialPlayer != null) {
            nash = Math.abs(nash - specialPlayer.getPayoff());
        }
        this.playerAvgDiffs = playerAvgDiffs;

        return nash;
    }

    public List<NormalPlayer> getNormalPlayers() {
        return normalPlayers;
    }

    public int getDominantPlayerIndex() {
        int bestResponse = 0;
        List<Double> payoffs = new ArrayList<>();

        for (NormalPlayer p : normalPlayers) {
            payoffs.add(p.getPurePayoff());
        }

        double max = normalPlayers.get(0).getPurePayoff();

        for (int i = 0; i < payoffs.size(); i++) {
            if (payoffs.get(i) > max) {
                bestResponse = i;
            }
            max = normalPlayers.get(i).getPurePayoff();
        }
        return bestResponse;
    }

    public int getNashEquiPlayerIndex() {
        computeNashEquilibrium();
        return playerAvgDiffs.indexOf(Collections.min(playerAvgDiffs));
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "Game Theory Problem HEHE";
    }

    @Override
    public int getNumberOfVariables() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public int getNumberOfObjectives() {
        // TODO Auto-generated method stub
        return normalPlayers.size();
    }

    @Override
    public int getNumberOfConstraints() {
        // TODO Auto-generated method stub
        return conflictSet.size();
    }

    @Override
    public void evaluate(Solution solution) {
        boolean[] areObjectivesExist = EncodingUtils.getBinary(solution.getVariable(0));
        double[] NashEquilibrium = {computeNashEquilibrium()};
        double[] payoffs = new double[solution.getNumberOfObjectives()];

        for (int i = 0; i < normalPlayers.size(); ++i) {
            if (areObjectivesExist[i]) {
                payoffs[i] = normalPlayers.get(i).getBestResponse();
            }
        }
        solution.setObjectives(payoffs);
        solution.setConstraints(NashEquilibrium);
    }

    // SOLUTION = VARIABLE -> OBJECTIVE || CONSTRAINT
    @Override
    public Solution newSolution() {
        //Number of strategies each player
        int numbeOfNP = normalPlayers.size();
        Solution solution = new Solution(1, numbeOfNP, 1);
        solution.setVariable(0, EncodingUtils.newBinary(numbeOfNP));
        return solution;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }
}
