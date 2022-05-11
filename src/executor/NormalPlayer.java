package executor;

import java.util.ArrayList;
import java.util.List;

/**
 * @object A typical Normal Player is
 *
 * @attributes strategies List<Strategy>
 */
public class NormalPlayer {
	private List<Strategy> strategies;

	public NormalPlayer(List<Strategy> strategies) {
		this.strategies = strategies;
	}

	public int getBestResponse() {
		int bestResponse = 0;
		
		List<Double> payoffs = new ArrayList<>();

		for (Strategy s : strategies) {
			payoffs.add(s.getPayoff());
		}

		double max = strategies.get(0).getPayoff();

		for (int i = 0; i < payoffs.size(); i++) {
			if(payoffs.get(i) > max) {
				bestResponse = i;
			}
		}

		return bestResponse;
	}

	public List<Strategy> getStrategiesList() {
		return strategies;
	}

	public Strategy getStrategyAt(int index) {
		return strategies.get(index);
	}

	public void removeStrategiesAt(int index) {
		strategies.set(index, null);
	}

	public void setStrategies(List<Strategy> strat) {
		strategies = strat;
	}
	
	public void removeAllNull() {
		for (int i = 0; i < strategies.size(); ++i) {
			if (strategies.get(i) == null)
				strategies.remove(i);
		}
	}

	public void display() {
		for (Strategy s : strategies) {
			if (s == null)
				continue;
			System.out.println("Strategy " + (strategies.indexOf(s) + 1) + ":\t");
			for (double p : s.getProperties()) {
				System.out.print(p + ", ");
			}
			System.out.println();
		}
	}
}