package executor;

import java.util.ArrayList;
import java.util.List;

public class NormalPlayer {
    private List<Strategy> strategies;

    public NormalPlayer(List<Strategy> strategies) {
        this.strategies = strategies;
    }

    public Strategy getStrategyAt(int index) {
        return strategies.get(index);
    }

    public void removeStrategiesAt(int index) {
        strategies.set(index, null);
    }

    public void removeAllNull() {
        for (int i = 0; i < strategies.size(); ++i) {
            if (strategies.get(i) == null)
                strategies.remove(i);
        }
    }

    public int getBestResponse() {
        int bestResponse = 0;

        List<Double> payoffs = new ArrayList<>();

        for (Strategy s : strategies) {
            payoffs.add(s.getPayoff());
        }

        double max = strategies.get(0).getPayoff();

        for (int i = 0; i < payoffs.size(); i++) {
            if (payoffs.get(i) > max) {
                bestResponse = i;
            }
        }

        return bestResponse;
    }

    public double getPurePayoff() {
        double payoff = 0;
        for (Strategy strategy : strategies) {
            payoff += strategy.getPayoff();
        }
        return payoff;
    }

    public String toString() {
        String NP = "";
        for (Strategy s : strategies) {
            if (s == null)
                continue;
            NP += "\nStrategy " + (strategies.indexOf(s) + 1) + ":\t";
            NP += s + "\nPayoff: " + s.getPayoff();
        }
        return NP;
    }
}
