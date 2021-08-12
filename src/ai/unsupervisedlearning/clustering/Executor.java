package ai.unsupervisedlearning.clustering;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Reads the input and data from file and executes the KMeans algorithm on the given input and data.
 * @author kps9907
 *
 */
public class Executor {

	/**
	 * Reads the input and data from file. Creates an object of KMeans class.
	 * Invokes the KMeans algorithm on the given input and data.
	 * @param args
	 */
	public static void main(String[] args) {
		int k = 0;
		int r = 0;
		boolean verbose = false;
		Map<String, List<Double>> data = new LinkedHashMap<String, List<Double>>();
		int dimension = 0; //Represents the dimensionality of the data.
		File inputFile = new File(System.getProperty("user.dir") + "/src/input_files/kmeans_input.txt");
		File dataFile = new File(System.getProperty("user.dir") + "/src/input_files/data.csv");
		Scanner inputScanner;

		//Reads the input parameters k,r,verbose from the input file.
		try {
			inputScanner = new Scanner(inputFile);
			k = inputScanner.nextInt();
			r = inputScanner.nextInt();
			verbose = inputScanner.nextInt() == 0 ? false : true;
			inputScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		
		Scanner dataScanner;
		//Reads data from the csv file and stores it in a map.
		try {
			dataScanner = new Scanner(dataFile);
			dataScanner.useDelimiter(",");
			while(dataScanner.hasNextLine()) {
				List<String> x = Arrays.asList(dataScanner.nextLine().split(","));
				List<Double> coordinates = new ArrayList<Double>();;
				for(int i = 1; i < x.size(); i++)
					coordinates.add(Double.valueOf(x.get(i)));
				dimension = coordinates.size();
				data.put(x.get(0), coordinates);
			}
			dataScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//Invoke kMeans with random restart.
		KMeans kMeans = new KMeans();
		kMeans.kMeansWithRandomRestart(k, data, verbose, r, dimension);
	}
}
