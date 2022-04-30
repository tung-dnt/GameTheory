package gametheory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.util.Vector;
import org.moeaframework.util.io.CommentedLineReader;

public class GameTheoryObject implements Problem {
	private SpecialPlayer specialPlayer;
	private List<NormalPlayer> normalPlayers;
	private List<Conflict> conflictSet;
	private List<Integer> normalPlayerWeights= new ArrayList<>();
	
	public GameTheoryObject(String path, int startRow) throws IOException {
		super();
		load(path, startRow);
		eliminateConflictStrategies();
		printConflictInformation(); 	
	} 
	
	private void load(String path, int startRow) throws IOException {
//    	SPECIAL PLAYER HANDLER
        SpecialPlayer specialPlayer = new SpecialPlayer();
        InputDataDriver driver = new InputDataDriver(path);
        
        if(driver.getValueOfCoordinate(startRow,0) != 0){
            int numberOfProperties =  driver.getValueOfCoordinate(startRow,1); // row = 0;
            startRow++;
            specialPlayer.setNumberOfProperties(numberOfProperties);
            for (int i = 0; i < specialPlayer.getNumberOfProperties(); i++) {
                int  property = driver.getValueOfCoordinate(startRow,i); //row = 1
                int weight  = driver.getValueOfCoordinate(startRow + 1,i); //row = 2
                specialPlayer.addProperty(property);
                specialPlayer.addWeight(weight);
            }
        }
        setSpecialPlayer(specialPlayer);

        startRow += 2;//important

//      NORMAL PLAYER HANDLER
        List<NormalPlayer> normalPlayerList;
        int numberOfNormalPlayer = driver.getValueOfCoordinate(startRow, 0);
        int numberOfProperties = driver.getValueOfCoordinate(startRow, 1);
        
        //get weight of each strategy
        startRow ++; //Important
        
        for (int i = 0; i < numberOfProperties ; i++) {
            addWeight(driver.getValueOfCoordinate(startRow, i));
        }
        // normal players
        startRow ++; //Important
       
        normalPlayerList = new ArrayList<>(numberOfNormalPlayer);
        for (int i = 0; i <numberOfNormalPlayer; i++) {
            normalPlayerList.add(driver.setNormalPlayer(startRow, numberOfProperties));
            startRow++;
        }
        setNormalPlayers(normalPlayerList);
        conflictSet = driver.getConflictSet(startRow++);
        InputDataDriver.displayNormalPlayerList(normalPlayerList);
	}
	
	private void eliminateConflictStrategies() {
		for(int i = 0; i < conflictSet.size(); ++i) {
			
			NormalPlayer evaluatingLeftPlayer = normalPlayers.get(conflictSet.get(i).getLeftPlayer());
			NormalPlayer evaluatingRightPlayer = normalPlayers.get(conflictSet.get(i).getRightPlayer());
			int leftConflictStrat = conflictSet.get(i).getLeftPlayerStrategy();
			int rightConflictStrat = conflictSet.get(i).getRightPlayerStrategy();
			
			if(evaluatingLeftPlayer.getStrategyAt(leftConflictStrat) != null) {
				evaluatingLeftPlayer.removeStrategiesAt(leftConflictStrat);
			}
			if(evaluatingRightPlayer.getStrategyAt(rightConflictStrat) != null) {
				evaluatingRightPlayer.removeStrategiesAt(rightConflictStrat);
			}
		}
		for(NormalPlayer player: normalPlayers) player.removeAllNull();
	}
	
    public List<Integer> getWeights() {
        return normalPlayerWeights;
    }

    public int getWeightyAt(int index){
        return normalPlayerWeights.get(index);
    }

    public void addWeight(int property){
    	normalPlayerWeights.add(property);
    }
	
	public SpecialPlayer getSpecialPlayer() {
		return specialPlayer;
	}
	public void setSpecialPlayer(SpecialPlayer spPlayer) {
		this.specialPlayer = spPlayer;
	}
	
	public List<NormalPlayer> getNormalPlayers() {
		return normalPlayers;
	}
	public void setNormalPlayers(List<NormalPlayer> normalPlayers) {
		this.normalPlayers = normalPlayers;
	}
	
	public List<Conflict> getConflictSet(){
		return conflictSet;
	}
	public void setConflictSet(List<Conflict> conflictSet) {
		this.conflictSet = conflictSet;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Game Theory Object HEHE";
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
		// TODO Auto-generated method stub
		// negate the objectives since storage is maximization
		//solution.setObjectives(Vector.negate());
		//solution.setConstraints(g);
	}
	@Override
	public Solution newSolution() {
		Solution solution = new Solution(1, normalPlayers.size());
		solution.setVariable(0, EncodingUtils.newBinary(normalPlayers.size()));
		return solution;
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
	}
	public void printConflictInformation() {
		int playerCount = 1;
		System.out.println();
		System.out.println("CONFLICT SET: ");
		for(int i=0; i < conflictSet.size(); ++i) {
			System.out.println(conflictSet.get(i).toString());
		}
		System.out.println();
		System.out.println("NON-CONFLICT STRATEGIES:");
		for(NormalPlayer player: normalPlayers) {
			System.out.println("Player "+playerCount+": ");
			player.display();
			++playerCount;
		}
		System.out.println();
	} 
}
