package executor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

public class Storage implements Problem {

	/**
	 * The number of storages.
	 */
	protected int storageNumber;

	/**
	 * The number of items.
	 */
	protected int itemNumber;

	/**
	 * Entry {@code profit[i][j]} is the profit from including item {@code j}
	 * in storage {@code i}.
	 */
	protected int[][] profit;

	/**
	 * Entry {@code area[i][j]} is the area incurred from including item
	 * {@code j} in storage {@code i}.
	 */
	protected int[][] area;
	
	/**
	 * The remaining date to use of foods
	 */
	protected int[][] expiration;

	/**
	 * Entry {@code capacity[i]} is the area capacity of storage {@code i}.
	 */
	protected int[] capacity;

	/**
	 * Constructs a multiobjective 0/1 storage problem instance loaded from
	 * the specified file.
	 * 
	 * @param file the file containing the storage problem instance
	 * @throws IOException if an I/O error occurred
	 */
	public Storage(File file) throws IOException {
		this(new FileReader(file));
	}
	
	/**
	 * Constructs a multiobjective 0/1 storage problem instance loaded from
	 * the specified input stream.
	 * 
	 * @param inputStream the input stream containing the storage problem
	 *        instance
	 * @throws IOException if an I/O error occurred
	 */
	public Storage(InputStream inputStream) throws IOException {
		this(new InputStreamReader(inputStream));
	}
	
	/**
	 * Constructs a multi-objective 0/1 storage problem instance loaded from
	 * the specified reader.
	 * 
	 * @param reader the reader containing the storage problem instance
	 * @throws IOException if an I/O error occurred
	 */
	public Storage(Reader reader) throws IOException {
		super();
		
		load(reader);
	}

	/**
	 * Loads the storage problem instance from the specified reader.
	 * 
	 * @param reader the file containing the storage problem instance
	 * @throws IOException if an I/O error occurred
	 */
	private void load(Reader reader) throws IOException {
		Pattern specificationLine = Pattern.compile("storage allocation problem specification \\((\\d+) storages, (\\d+) items\\)");
		Pattern capacityLine = Pattern.compile(" capacity: \\+(\\d+)");
		Pattern areaLine = Pattern.compile("  area: \\+(\\d+)");
		Pattern profitLine = Pattern.compile("  profit: \\+(\\d+)");
		Pattern expLine = Pattern.compile("  expiration: \\+(\\d+)");

		CommentedLineReader lineReader = null;
		String line = null;
		Matcher matcher = null;
		
		try {
			lineReader = new CommentedLineReader(reader);
			line = lineReader.readLine(); // the problem specification line
			matcher = specificationLine.matcher(line);
			
			if (matcher.matches()) {
				storageNumber = Integer.parseInt(matcher.group(1));
				itemNumber = Integer.parseInt(matcher.group(2));
			} else {
				throw new IOException("storage data file not properly formatted: invalid specification line");
			}
			
			capacity = new int[storageNumber];
			profit = new int[storageNumber][itemNumber];
			area = new int[storageNumber][itemNumber];
			expiration = new int[storageNumber][itemNumber];
	
			for (int i = 0; i < storageNumber; i++) {
				line = lineReader.readLine(); // line containing "="
				line = lineReader.readLine(); // line containing "storage i:"
				line = lineReader.readLine(); // the storage capacity
				matcher = capacityLine.matcher(line);
				if (matcher.matches()) {
					capacity[i] = Integer.parseInt(matcher.group(1));
				} else {
					throw new IOException("Storage data file not properly formatted: invalid capacity line");
				}
	
				for (int j = 0; j < itemNumber; j++) {
					line = lineReader.readLine(); // line containing "item j:"
					line = lineReader.readLine(); // the item area
					matcher = areaLine.matcher(line);
					
					if (matcher.matches()) {
						area[i][j] = Integer.parseInt(matcher.group(1));
					} else {
						throw new IOException("Storage data file not properly formatted: invalid area line");
					}
	
					line = lineReader.readLine(); // the item profit
					matcher = profitLine.matcher(line);
					
					if (matcher.matches()) {
						profit[i][j] = Integer.parseInt(matcher.group(1));
					} else {
						throw new IOException("Storage data file not properly formatted: invalid profit line");
					}
					
					line = lineReader.readLine(); // the item expiration
					matcher = expLine.matcher(line);
					
					if (matcher.matches()) {
						expiration[i][j] = Integer.parseInt(matcher.group(1));
					} else {
						throw new IOException("Storagedata file not properly formatted: invalid expiration line");
					}
				}
			}
		} finally {
			if (lineReader != null) {
				lineReader.close();
			}
		}
	}

	@Override
	public void evaluate(Solution solution) {
		boolean[] d = EncodingUtils.getBinary(solution.getVariable(0));
		double[] pro = new double[storageNumber];
		double[] are = new double[storageNumber];
		double[] exp = new double[storageNumber];

		// calculate the profits, expiration and weights for the storage
		for (int i = 0; i < itemNumber; i++) {
			if (d[i]) {
				for (int j = 0; j < storageNumber; j++) {
					pro[j] += profit[j][i];
					are[j] += area[j][i];
					exp[j] += expiration[j][i];
				}
			}
		}

		// check if any weights exceed the capacities
		for (int j = 0; j < storageNumber; j++) {
			if (are[j] <= capacity[j]) {
				are[j] = 0.0;
			} else {
				are[j] = are[j] - capacity[j];
			}
		}

		// negate the objectives since Storage is maximization
		solution.setObjectives(Vector.negate(pro));
		solution.setConstraints(are);
	}

	@Override
	public String getName() {
		return "Storage";
	}

	@Override
	public int getNumberOfConstraints() {
		return storageNumber;
	}

	@Override
	public int getNumberOfObjectives() {
		return storageNumber;
	}

	@Override
	public int getNumberOfVariables() {
		return 1;
	}

	@Override
	public Solution newSolution() {
		Solution solution = new Solution(1, storageNumber, storageNumber);
		solution.setVariable(0, EncodingUtils.newBinary(itemNumber));
		return solution;
	}

	@Override
	public void close() {
		//do nothing
	}

}
