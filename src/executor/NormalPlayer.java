package executor;

import java.util.ArrayList;
import java.util.List;

public class NormalPlayer {
    private final List<Strategy> strategies;

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

    public int getDominantStrategyIndex() {
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
        StringBuilder NP = new StringBuilder();
        for (Strategy s : strategies) {
            if (s == null)
                continue;
            NP.append("\nStrategy ").append(strategies.indexOf(s) + 1).append(":\t");
            NP.append(s).append("\nPayoff: ").append(s.getPayoff());
        }
        return NP.toString();
    }
}
