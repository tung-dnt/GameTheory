package gametheory;

public class Conflict {
    private int leftPlayer ;
    private int rightPlayer ;
    private int leftPlayerStrategy;
    private int rightPlayerStrategy;

    public Conflict(String conflict)  {
        String[] conflictSet= conflict.split(",");
        if(conflictSet.length != 4) {
        	System.err.print("Invalid Conflict input data format, it should be: 'left player, left player strategy, right player, right player strategy'");
        	return;
        }
        leftPlayer = Integer.parseInt(conflictSet[0])-1;
        leftPlayerStrategy = Integer.parseInt(conflictSet[1])-1;
        rightPlayer = Integer.parseInt(conflictSet[2])-1;
        rightPlayerStrategy = Integer.parseInt(conflictSet[3])-1;
    }
    public int getLeftPlayer() {
    	return leftPlayer;
    }

    public int getRightPlayer() {
    	return rightPlayer;
    }
    public int getLeftPlayerStrategy() {
    	return leftPlayerStrategy;
    }
    public int getRightPlayerStrategy() {
    	return rightPlayerStrategy;
    }
    public int[] getPlayers() {
    	int [] players = {leftPlayer, rightPlayer};
    	return players;
    }
    public int[] getStrategies() {
    	int [] strategies = {leftPlayerStrategy, rightPlayerStrategy};
    	return strategies;
    }
    public String toString(){
        return String.format("%s,%s,%s,%s",leftPlayer,leftPlayerStrategy,rightPlayer,rightPlayerStrategy);
    }
}