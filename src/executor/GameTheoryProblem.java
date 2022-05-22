package executor;
/* *
 * STEPS TO FIND OUT THE BEST RESPONSE / NASH EQUILIBRIUM
 * 1) Set up the game with `load()` method
 * 2) Define each player's set of strategies with `loadNormalPlayersFromFile()`
 * 3) Find and eliminate dominant strategies with `eliminateDominantStrategies()`
 * 4) Find the pure strategy Nash Equilibria of the game
 * 5) Find the mixed strategy Nash Equilibrium of the game
 * */
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
 *
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.util.Vector;

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
