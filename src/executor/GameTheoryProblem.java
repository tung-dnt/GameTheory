package executor;

//DOCUMENT: GameTheoryProblem.txt

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

public class GameTheoryProblem implements Problem {
    private SpecialPlayer specialPlayer;
    private List<NormalPlayer> normalPlayers;
    private List<Conflict> conflictSet;
    private List<Double> normalPlayerWeights;

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

        normalPlayerWeights = driver.loadNormalPlayerWeights(NORMAL_PLAYER_START_ROW);
        normalPlayers = driver.loadNormalPlayersFromFile(NORMAL_PLAYER_START_ROW, normalPlayerWeights);
        conflictSet = driver.loadConflictSetFromFile(CONFLICT_SET_START_ROW);

        InputDataDriver.displayNormalPlayerList(normalPlayers);
    }

    private void eliminateConflictStrategies() {
        if (conflictSet != null) {
            for (int i = 0; i < conflictSet.size(); ++i) {
                NormalPlayer evaluatingLeftPlayer = normalPlayers.get(conflictSet.get(i).getLeftPlayer());
                NormalPlayer evaluatingRightPlayer = normalPlayers.get(conflictSet.get(i).getRightPlayer());
                int leftConflictStrat = conflictSet.get(i).getLeftPlayerStrategy();
                int rightConflictStrat = conflictSet.get(i).getRightPlayerStrategy();

                // IF STRATEGY BELONG TO SPECIAL PLAYER -> DON'T REMOVE
                // Set conflict strategy of right player to null
                if (evaluatingLeftPlayer.getStrategyAt(leftConflictStrat) != null && conflictSet.get(i).getLeftPlayer() > -1) {
                    evaluatingLeftPlayer.removeStrategiesAt(leftConflictStrat);
                }
                // Set conflict strategy of right player to null
                if (evaluatingRightPlayer.getStrategyAt(rightConflictStrat) != null && conflictSet.get(i).getRightPlayer() > -1) {
                    evaluatingRightPlayer.removeStrategiesAt(rightConflictStrat);
                }
            }
            //Completely remove all inappropriate strategies from Evaluating Strategies
            for (NormalPlayer player : normalPlayers)
                player.removeAllNull();
        }
    }

    // Compute intersection point between normal player vector && special player
    private double[] computeNashEquilibrium() {
        int numbeOfObjectives = normalPlayers.get(0).getStrategies().size();
        double[] nash = new double[numbeOfObjectives];

        for (int i = 0; i < numbeOfObjectives; ++i) {
            if (specialPlayer == null) {
                nash[i] = 0;
            } else {
                nash[i] = normalPlayers.get(i).getBestResponse() - specialPlayer.getPayoff();
            }
        }
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
        }

        return bestResponse;
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
        double[] NashEquilibrium = computeNashEquilibrium();
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
