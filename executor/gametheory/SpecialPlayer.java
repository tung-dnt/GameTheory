package gametheory;

import java.util.ArrayList;
import java.util.List;

/**
 * @object A typical SpecialPlayer is
 *
 * @attributes numberOfProperties int properties List<Integer> weights
 *             List<Integer>
 */
public class SpecialPlayer {
	private int numberOfProperties;
	private List<Double> properties = new ArrayList<>();
	private List<Double> weights = new ArrayList<>();
	private double payoff;

	public SpecialPlayer() {
	}

	/**
	 *
	 * @return properties size
	 */
	public int getNumberOfProperties() {
		return numberOfProperties;
	}

	/**
	 * @modifies properties
	 * @effects add more property into properties
	 */
	public void addProperty(double property) {
		properties.add(property);
	}

	/**
	 * @modifies weight
	 * @effects add more weight into weights
	 */
	public void addWeight(double weight) {
		weights.add(weight);
	}

	/**
	 * @modifies properties
	 * @effects set number of Properties
	 */
	public void setNumberOfProperties(int numberOfProperties) {
		this.numberOfProperties = numberOfProperties;
	}

	public double getPayoff() {
		return payoff;
	}

	public void setPayoff() {
		double newPayoff = 0;
		for (int i = 0; i < properties.size(); ++i) {
			newPayoff += properties.get(i) * weights.get(i);
		}
		this.payoff = newPayoff;
	}

	public void displayInf() {
		System.out.println("Properties : ");
		properties.forEach(x -> System.out.print(x + "\t"));
		System.out.println("\n" + "Weight");
		weights.forEach(x -> System.out.print(x + "\t"));
	}
}