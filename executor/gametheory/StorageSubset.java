package gametheory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.util.Vector;

public class StorageSubset extends Storage {

	/**
	 * Constructs a multiobjective 0/1 storage problem instance loaded from
	 * the specified file.
	 * 
	 * @param file the file containing the storage problem instance
	 * @throws IOException if an I/O error occurred
	 */
	public StorageSubset(File file) throws IOException {
		super(file);
	}
	
	/**
	 * Constructs a multiobjective 0/1 storage problem instance loaded from
	 * the specified input stream.
	 * 
	 * @param inputStream the input stream containing the storage problem
	 *        instance
	 * @throws IOException if an I/O error occurred
	 */
	public StorageSubset(InputStream inputStream) throws IOException {
		super(inputStream);
	}
	
	/**
	 * Constructs a multiobjective 0/1 storage problem instance loaded from
	 * the specified reader.
	 * 
	 * @param reader the reader containing the storage problem instance
	 * @throws IOException if an I/O error occurred
	 */
	public StorageSubset(Reader reader) throws IOException {
		super(reader);
	}

	@Override
	public void evaluate(Solution solution) {
		int[] items = EncodingUtils.getSubset(solution.getVariable(0));
		double[] f = new double[storageNumber];
		double[] g = new double[storageNumber];
		double[] k = new double[storageNumber];

		// calculate the profits and area for the storage
		for (int i = 0; i < items.length; i++) {
			for (int j = 0; j < storageNumber; j++) {
				f[j] += profit[j][items[i]];
				g[j] += area[j][items[i]];
				k[j] += expiration[j][items[i]];
			}
		}

		// check if any weights exceed the capacities
		for (int j = 0; j < storageNumber; j++) {
			if (g[j] <= capacity[j]) {
				g[j] = 0.0;
			} else {
				g[j] = g[j] - capacity[j];
			}
		}

		// negate the objectives since storage is maximization
		solution.setObjectives(Vector.negate(f));
		solution.setConstraints(g);
	}

	@Override
	public String getName() {
		return "KnapsackSubset";
	}

	@Override
	public Solution newSolution() {
		Solution solution = new Solution(1, storageNumber, storageNumber);
		solution.setVariable(0, EncodingUtils.newSubset(0, itemNumber, itemNumber));
		return solution;
	}

}
