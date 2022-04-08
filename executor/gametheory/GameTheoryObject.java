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
	
	public GameTheoryObject(String path, int startRow) throws IOException {
		super();
		load(path, startRow);
	} 
	
	private void load(String path, int startRow) throws IOException {
//    	SPECIAL PLAYER HANDLER
        SpecialPlayer specialPlayer = new SpecialPlayer();

        if(InputDataDriver.getValueOfCoordinate(startRow,0, path) != 0){
            int numberOfProperties =  InputDataDriver.getValueOfCoordinate(startRow,1,path); // row = 0;
            startRow++;
            specialPlayer.setNumberOfProperties(numberOfProperties);
            for (int i = 0; i < specialPlayer.getNumberOfProperties(); i++) {
                int  property = InputDataDriver.getValueOfCoordinate(startRow,i, path); //row = 1
                int weight  = InputDataDriver.getValueOfCoordinate(startRow + 1,i,path); //row = 2
                specialPlayer.addProperty(property);
                specialPlayer.addWeight(weight);
            }
        }
        setSpecialPlayer(specialPlayer);

        startRow += 2;//important

//        NORMAL PLAYER HANDLER
        List<NormalPlayer> normalPlayerList;
        int numberOfNormalPlayer = InputDataDriver.getValueOfCoordinate(startRow, 0, path);
        int numberOfProperties = InputDataDriver.getValueOfCoordinate(startRow, 1, path);
        int[] normalPlayerWeights = new int[numberOfProperties];
        
        //get weight of each strategy
        startRow += 1; //Important
        
        for (int i = 0; i < numberOfProperties ; i++) {
            normalPlayerWeights[i] = InputDataDriver.getValueOfCoordinate(startRow, i, path);
        }
        // normal players
        startRow += 1; //Important
        normalPlayerList = new ArrayList<>(numberOfNormalPlayer);
        for (int i = 0; i <numberOfNormalPlayer; i++) {
            normalPlayerList.add(InputDataDriver.setNormalPlayer(startRow, numberOfProperties,path));
            startRow++;
        }
        setNormalPlayers(normalPlayerList);
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
//		double[] f = new double[storageNumber];
//		double[] g = new double[storageNumber];
//		double[] k = new double[storageNumber];

		// calculate the profits and area for the storage
//		for (int i = 0; i < items.length; i++) {
//			for (int j = 0; j < storageNumber; j++) {
//				f[j] += profit[j][items[i]];
//				g[j] += area[j][items[i]];
//				k[j] += expiration[j][items[i]];
//			}
//		}
//
//		// check if any weights exceed the capacities
//		for (int j = 0; j < storageNumber; j++) {
//			if (g[j] <= capacity[j]) {
//				g[j] = 0.0;
//			} else {
//				g[j] = g[j] - capacity[j];
//			}
//		}

		// negate the objectives since storage is maximization
//		solution.setObjectives(Vector.negate(f));
//		solution.setConstraints(g);
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
}
