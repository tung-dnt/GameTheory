package executor;

import java.io.IOException;
import org.moeaframework.util.Vector;
import java.util.ArrayList;
import java.util.List;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

public class GameTheoryProblem implements Problem {
	private SpecialPlayer specialPlayer;
	private List<NormalPlayer> normalPlayers;
	private List<Conflict> conflictSet;
	private List<Double> normalPlayerWeights = new ArrayList<>();

	public GameTheoryProblem(String path, int startRow) throws IOException {
		super();
		load(path, startRow);
		eliminateConflictStrategies();
	}

	private void load(String path, int startRow) throws IOException {
		InputDataDriver driver = new InputDataDriver(path);
//    	SPECIAL PLAYER HANDLER        
		if (driver.getValueOfCoordinate(startRow, 0) != 0) {
			SpecialPlayer specialPlayer = new SpecialPlayer();

			int numberOfProperties = driver.getValueOfCoordinate(startRow, 1).intValue(); // row = 0;
			startRow++;
			specialPlayer.setNumberOfProperties(numberOfProperties);
			for (int i = 0; i < specialPlayer.getNumberOfProperties(); i++) {
				double property = driver.getValueOfCoordinate(startRow, i); // row = 1
				double weight = driver.getValueOfCoordinate(startRow + 1, i); // row = 2
				specialPlayer.addProperty(property);
				specialPlayer.addWeight(weight);
			}
			specialPlayer.setPayoff();
			setSpecialPlayer(specialPlayer);
		} else {
			startRow += 1;
		}
		startRow += 2;// important

//      NORMAL PLAYER HANDLER
		List<NormalPlayer> normalPlayerList;
		int numberOfNormalPlayer = driver.getValueOfCoordinate(startRow, 0).intValue();
		int numberOfProperties = driver.getValueOfCoordinate(startRow, 1).intValue();

		// get weight of each strategy
		startRow++; // Important

		for (int i = 0; i < numberOfProperties; i++) {
			addWeight(driver.getValueOfCoordinate(startRow, i));
		}
		// normal players
		startRow++; // Important

		normalPlayerList = new ArrayList<>(numberOfNormalPlayer);
		for (int i = 0; i < numberOfNormalPlayer; i++) {
			normalPlayerList.add(driver.setNormalPlayer(startRow, numberOfProperties, normalPlayerWeights));
			startRow++;
		}

		setNormalPlayers(normalPlayerList);

		conflictSet = driver.getConflictSet(startRow++);

		InputDataDriver.displayNormalPlayerList(normalPlayerList);
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

	private void addWeight(double property) {
		normalPlayerWeights.add(property);
	}

	private void setSpecialPlayer(SpecialPlayer spPlayer) {
		this.specialPlayer = spPlayer;
	}

	private void setNormalPlayers(List<NormalPlayer> normalPlayers) {
		this.normalPlayers = normalPlayers;
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
