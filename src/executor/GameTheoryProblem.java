package executor;

/**
 * @General_Formula: STEPS TO FIND OUT THE BEST RESPONSE/NASH EQUILIBRIUM
 * 1)Set up the game with `load()` method
 * 2)Define each player's set of strategies with `loadNormalPlayersFromFile()`
 * 3)Find and eliminate conflict strategies with `eliminateDominantStrategies()`
 * 4)Find the pure strategy Nash Equilibrium of the game
 * 5)Find the mixed strategy Nash Equilibrium of the game
 **/
//--------------------------------------------------------------------------
/* *
 * WARNING: DO NOT CHANGE ORDER OF GameTheoryProblem CONSTRUCTOR
 * PRECAUTION: THIS WOULD ONLY SOLVE PROBLEM FOR COORDINATING PROBLEMS

 *  We will try to solve to equations of type : ( find p and q )
        a*p=b
        c*q=d
        ( How we got this a,b,c and d coefficient ? )
        for example :
                      q                  1-q
                      C                   D
             ---------------------------------------------
    p      A        (3,-3)       |        (-2,2)
             ---------------------------------------------
    1-p    B        (-1,1)       |         (0,0)
             ---------------------------------------------
        If we apply the definition of mixed strategy Nash Equilibrium we will get :
        For Player 1 :
        -3p + 1(1-p) = 2p + 0(1-p)
        For Player 2 :
        3q + (-2)(1-q) = -1q + 0(1-q)
        So our goal is to solve this linear system !
        Let's make it like the format in the start of this example :
        (-3-1-2+0)p = -1 + 0
        (3+2+1+0)q  = +2 + 0
        Cool we have now our a,b,c and d coefficient ! let's do our thing :
        p = 1/6
        q = 1/3
*/

import java.io.IOException;
import java.util.*;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

public class GameTheoryProblem implements Problem {
    private SpecialPlayer specialPlayer;
    private List<NormalPlayer> normalPlayers;
    private List<Conflict> conflictSet;
    //Store average pure payoff differences
    private List<Double> playerAvgDiffs;
    int[] bestResponses = new int[4];

    public GameTheoryProblem(String path, int startRow) throws IOException {
        super();
        if (Objects.equals(path, "")) {
            System.err.println("INVALID INPUT PATH FOUND: Unable to generate Game Theory Problem");
            System.exit(-1);
        }
        // Load user input data from .xlsx file
        load(path, startRow);
        eliminateConflictStrategies();
        computeNashEquilibrium();
    }

    /**
     * @usage To set gametheory instance data from .xlsx file
     * @modifies specialPlayer, normalPlayers, conflictSet
     * @params String path: .xlsx pathname,
     * ________int startRow: start row in xlsx file - where real data inserted
     */
    private void load(String path, int startRow) throws IOException {
        startRow--; //Since row index start from 0 --> index = startRow-1
        InputDataDriver driver = new InputDataDriver(path);
        // WARNING: Must not modify these variables
        boolean isSpecialPlayerExist = driver.getValueOfCoordinate(startRow, 0) != 0;
        // SET UP DATA LOCATION
        int NORMAL_PLAYER_START_ROW = isSpecialPlayerExist ? startRow + 3 : startRow + 1;
        int numberOfNP = driver.getValueOfCoordinate(NORMAL_PLAYER_START_ROW, 0).intValue();
        int CONFLICT_SET_START_ROW = isSpecialPlayerExist ? startRow + 5 + numberOfNP : startRow + 3 + numberOfNP;
        // Load Special Player
        if (isSpecialPlayerExist) {
            specialPlayer = driver.loadSpecialPlayerFromFile(startRow);
        }
        List<Double> normalPlayerWeights = driver.loadNormalPlayerWeights(NORMAL_PLAYER_START_ROW);
        normalPlayers = driver.loadNormalPlayersFromFile(NORMAL_PLAYER_START_ROW, normalPlayerWeights);
        conflictSet = driver.loadConflictSetFromFile(CONFLICT_SET_START_ROW);
    }

    /**
     * @usage To remove all conflict strategies of conflict set declared in .xlsx file
     * @modifies normalPlayers
     * @algorithm Loop through conflict set
     * -----------> set all strategies matching with strategies in conflict set to null
     * -----------> remove all null strategies in normalPlayers
     * Conflict set format: Left Player, Left Player Strategy, Right Player, Right Player Strategy
     */
    private void eliminateConflictStrategies() {
        if (conflictSet == null) return;

        for (Conflict conflict : conflictSet) {
            NormalPlayer evaluatingLeftPlayer = normalPlayers.get(conflict.getLeftPlayer());
            NormalPlayer evaluatingRightPlayer = normalPlayers.get(conflict.getRightPlayer());
            int leftConflictStrat = conflict.getLeftPlayerStrategy();
            int rightConflictStrat = conflict.getRightPlayerStrategy();

            // IF STRATEGY BELONG TO SPECIAL PLAYER -> DON'T REMOVE
            // Set conflict strategy of right player to null
            if (evaluatingLeftPlayer.getStrategyAt(leftConflictStrat) != null && conflict.getLeftPlayer() > -1)
                evaluatingLeftPlayer.removeStrategiesAt(leftConflictStrat);

            // Set conflict strategy of right player to null
            if (evaluatingRightPlayer.getStrategyAt(rightConflictStrat) != null && conflict.getRightPlayer() > -1)
                evaluatingRightPlayer.removeStrategiesAt(rightConflictStrat);

        }
        //Completely remove all inappropriate strategies from Evaluating Strategies
        for (NormalPlayer player : normalPlayers)
            player.removeAllNull();
    }

    private List<Double> buildPayoffGlopses(List<NormalPlayer> players) {
        List<Double> playerAvgDiffs = new ArrayList<>();
        for (NormalPlayer player : players) {
            double playerAvgDiff = 0;
            for (NormalPlayer opponent : players) {
                playerAvgDiff += Math.abs(player.getPurePayoff() - opponent.getPurePayoff());
            }
            playerAvgDiff /= normalPlayers.size();
            playerAvgDiffs.add(playerAvgDiff);
        }
        return playerAvgDiffs;
    }

    /**
     * @usage To get the smallest average pure payoff difference among players
     * @algorithm 1) Loop through normalPlayers and calculate player average differences
     * ______________with formula |playerPayoff - opponentPayoff| / normalPlayers.size()
     * ___________2) Save result in a list
     * ___________3) Assign result list to playerAvgDiffs property
     */
    private double computeNashEquilibrium() {
        double nash;
        List<Double> playerAvgDiffs = buildPayoffGlopses(normalPlayers);
        nash = Collections.min(playerAvgDiffs);

        if (specialPlayer != null) nash = Math.abs(nash - specialPlayer.getPayoff());
        this.playerAvgDiffs = playerAvgDiffs;

        return nash;
    }

    public List<NormalPlayer> getNormalPlayers() {
        return normalPlayers;
    }

    /**
     * @usage To get player index with the highest pure payoff
     * @algorithm 1) Save all pure payoff values to a list
     * ___________2) Get max value in payoffs list
     * ___________3) Return max value index in list (Since pure payoff index == player index)
     */
    public int getDominantPlayerIndex() {
        List<Double> payoffs = new ArrayList<>();
        normalPlayers.forEach(player -> payoffs.add(player.getPurePayoff()));
        double max = Collections.max(payoffs);
        return payoffs.indexOf(max);
    }

    /**
     * @usage Get user with best response strategy
     * ----> The lower payoff average difference, the more equilibrium strategy is
     */
    public int getBestResponse() {
        return playerAvgDiffs.indexOf(Collections.min(playerAvgDiffs));
    }

    public int[] getRemainAlliances() {
        int[] bestResponse = new int[normalPlayers.size()];
        Arrays.fill(bestResponse, 2);
        int bestPlayerIndex = getBestResponse();
        int bestStrategyIndex = normalPlayers.get(bestPlayerIndex).getDominantStrategyIndex();
        bestResponse[bestPlayerIndex] = bestStrategyIndex;

        if (bestStrategyIndex == normalPlayers.size() - 1) {
            for (double p : bestResponse) p = bestStrategyIndex;
        } else {
            for (int i = 0; i < normalPlayers.size(); ++i) {
                int upperBound = normalPlayers.size() - i;
                if (bestStrategyIndex == i) {
                    bestResponses[i] = playerAvgDiffs.indexOf(Collections.min(playerAvgDiffs)) / upperBound;
                } else bestResponses[i] = 2;
            }
        }
        return bestResponse;
    }

    /**
     * @usage To set up evaluator for MOEA model every single time Evaluation run
     */
    @Override
    public void evaluate(Solution solution) {
        boolean[] areObjectivesExist = EncodingUtils.getBinary(solution.getVariable(0));
        double[] NashEquilibrium = {computeNashEquilibrium()};
        double[] payoffs = new double[solution.getNumberOfObjectives()];

        for (int i = 0; i < normalPlayers.size(); ++i) {
            if (areObjectivesExist[i]) {
                payoffs[i] = normalPlayers.get(i).getDominantStrategyIndex();
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

    public String toString() {
        StringBuilder gameString = new StringBuilder();
        for (NormalPlayer normalPlayer : normalPlayers) {
            gameString.append("Normal player: ").append(normalPlayers.indexOf(normalPlayer) + 1).append(normalPlayer);
            gameString.append("\n----------------\n");
        }
        return gameString.toString();
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
    public void close() {
        // TODO Auto-generated method stub
    }
}
