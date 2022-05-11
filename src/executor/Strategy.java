package executor;

import java.util.ArrayList;
import java.util.List;

public class Strategy {
	private List<Double> properties = new ArrayList<>();
	private double payoff;

	public List<Double> getProperties() {
		return properties;
	}

	public double getPayoff() {
		return payoff;
	}

	public void setPayoff(List<Double> weights) {
		double newPayoff = 0;
		for (int i = 0; i < properties.size(); ++i) {
			newPayoff += properties.get(i) * weights.get(i);
		}
		this.payoff = newPayoff;
	}

	public double getPropertyAt(int index) {
		return properties.get(index);
	}

	public void addProperty(double property) {
		properties.add(property);
	}

	@Override
	public String toString() {
		String props = "[ ";
		for (double prop : properties) {
			props += prop + ", ";
		}
		return props.substring(0, props.length() - 2) + " ]";
	}
}