package executor;

import java.io.IOException;
import java.util.List;

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
		load(path, startRow);
		eliminateConflictStrategies();
	}

	private void load(String path, int startRow) throws IOException {
		startRow--;
		InputDataDriver driver = new InputDataDriver(path);
		boolean isSpecialPlayerExist = driver.getValueOfCoordinate(startRow, 0) != 0;
		int NORMAL_PLAYER_START_ROW = isSpecialPlayerExist ? startRow + 3 : startRow + 1;
		int numberOfNP = driver.getValueOfCoordinate(NORMAL_PLAYER_START_ROW, 0).intValue();
		int CONFLICT_SET_START_ROW = isSpecialPlayerExist ? startRow + 5 + numberOfNP : startRow+ 3 + numberOfNP;

		if(isSpecialPlayerExist) {
			specialPlayer=driver.loadSpecialPlayerFromFile(startRow);
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

				if (evaluatingLeftPlayer.getStrategyAt(leftConflictStrat) != null) {
					evaluatingLeftPlayer.removeStrategiesAt(leftConflictStrat);
				}
				if (evaluatingRightPlayer.getStrategyAt(rightConflictStrat) != null) {
					evaluatingRightPlayer.removeStrategiesAt(rightConflictStrat);
				}
			}
			for (NormalPlayer player : normalPlayers)
				player.removeAllNull();
		}
	}

	public List<NormalPlayer> getNormalPlayers() {
		return normalPlayers;
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
		double[] objectives = new double[normalPlayers.size()];
		for (int i = 0; i < objectives.length; ++i) {
			objectives[i] = normalPlayers.get(i).getBestResponse();
		}
		solution.setObjectives((objectives));
	}
	@Override
	public Solution newSolution() {
		Solution solution = new Solution(1, normalPlayers.size());
		solution.setVariable(0, EncodingUtils.newBinary(normalPlayerWeights.size()));
		return solution;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

}
