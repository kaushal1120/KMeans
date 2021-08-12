package ai.unsupervisedlearning.clustering;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Implementation of the KMeans with Random Restart algorithm.
 * @author kps9907
 *
 */
public class KMeans {
	/**
	 * Checks if all clusters in the given cluster list have atleast the minimum number of elements specified
	 * @param clustering List of clusters
	 * @param minCount minimum number of elements that must be in all clusters.
	 * @return true if all clusters have the minimum number of elements. false otherwise.
	 */
	public boolean clusterMinCountCheck(List<Set<String>> clustering, int minCount) {
		for(int i = 0; i < clustering.size(); i++) {
			if(clustering.get(i).size() < minCount)
				return false;
		}
		return true;
	}

	/**
	 * Returns the squared distance between 2 points. In the context of KMeans,
	 * it returns the squared distance between a datapoint and the center of a cluster.
	 * @param dataPoint
	 * @param center
	 * @return the squared distance between dataPoint and center.
	 */
	public double getCost(List<Double> dataPoint, List<Double> center) {
		double cost = 0.0;
		for(int i = 0; i < center.size(); i++) {
			cost += Math.pow(dataPoint.get(i) - center.get(i), 2);
		}
		return cost;
	}

	/**
	 * Checks if the two clusters are same. In the context of KMeans, this function checks, if the datapoints
	 * were assigned to the same clusters in two consecutive iterations.
	 * @param oldClustering
	 * @param newClustering
	 * @return true if the two clusterings are the same. else returns false.
	 */
	public boolean checkReassignment(List<Set<String>> oldClustering, List<Set<String>> newClustering) {
		for(int i = 0; i < oldClustering.size(); i++) {
			if(!oldClustering.get(i).equals(newClustering.get(i)))
				return false;
		}
		return true;
	}

	/**
	 * Checks if the datapoints were assigned to fewer clusters than specified. The size of the clustering object
	 * is k, the number of clusters specified and if any cluster is empty then it is starved.
	 * @param clustering
	 * @return return true if starvation occurs. Else false.
	 */
	public boolean checkStarvation(List<Set<String>> clustering) {
		for(int i = 0; i < clustering.size(); i++) {
			if(clustering.get(i).size() == 0)
				return true;
		}
		return false;
	}

	/**
	 * KMeans algorithm.
	 * @param k number of required clusters.
	 * @param data datapoints that need to be clustered.
	 * @param verbose output mode (verbose/compact)
	 * @param restartNo (number of kMean runs until this point)
	 * @param d (dimension of data)
	 * @param clustering (clustering that kMeans algorithm converges to in this run)
	 * @param centers (centers of the clusters that kMeans algorithm converges to in this run)
	 * @return
	 */
	public int kMeans(int k, Map<String, List<Double>> data, boolean verbose, int restartNo, int d, List<Set<String>> clustering, List<List<Double>> centers) {
		if(verbose) System.out.println("Random Restart " + restartNo);
		Random random = new Random();
		do {
			for(int i=0; i < clustering.size(); i++) clustering.get(i).clear();
			for(String key : data.keySet()) clustering.get(random.nextInt(k)).add(key);
		} while(!clusterMinCountCheck(clustering, 2));

		int numIterations = 0;
		boolean reassignmentCheck = false;

		do {
			numIterations++;
			List<Set<String>> newClustering = new ArrayList<Set<String>>();
			for(int i = 0;i < k;i++) newClustering.add(new HashSet<String>());

			//Sets center of a cluster to the mean of all datapoints in the cluster.
			for(int i = 0; i < k; i++) {
				for(int j = 0; j < d; j++) 	centers.get(i).set(j,0.0);
				for(String key : clustering.get(i))
					for(int j = 0; j < centers.get(i).size(); j++)
						centers.get(i).set(j, centers.get(i).get(j) + data.get(key).get(j));
				for(int j = 0; j < centers.get(i).size(); j++) centers.get(i).set(j, centers.get(i).get(j)/clustering.get(i).size());
			}
	
			if(verbose) { System.out.println(); verboseReport(clustering, centers, data); }

			//Reassign datapoints to new clusters.
			for(String key : data.keySet()) {
				double minCost = Double.MAX_VALUE;
				int minCostCluster = 0;
				for(int i = 0; i < centers.size(); i++) {
					double cost = getCost(data.get(key), centers.get(i));
					if(cost < minCost) {
						minCost = cost;
						minCostCluster = i;
					}
				}
				newClustering.get(minCostCluster).add(key);
			}
			//Check if there has been any change in the cluster assignment frm ther previous iterations.
			reassignmentCheck = checkReassignment(clustering, newClustering);
			for(int i = 0; i < clustering.size(); i++) clustering.set(i, newClustering.get(i));
		} while(!reassignmentCheck);

		if(verbose) {
			System.out.println("\nKMeans terminates with final clustering");
			verboseReport(clustering, centers, data);
			System.out.println(numIterations + " iteration\n");
		}
		return numIterations;
	}

	/**
	 * Reports the progress of the KMeans algorithm whenever invoked.
	 * @param clustering
	 * @param centers
	 * @param data
	 */
	public void verboseReport(List<Set<String>> clustering, List<List<Double>> centers, Map<String, List<Double>> data) {
		double cost = 0.0;
		int clusterNo = 1;
		for(int i = 0; i < clustering.size(); i++) {
			if(clustering.get(i).size() != 0) {
				System.out.print("Cluster " + clusterNo++ + ": ");
				for(String key : clustering.get(i)) {
					System.out.print(key + " ");
					cost += getCost(data.get(key), centers.get(i));
				}
				System.out.print("Center=[");
				for(int j = 0; j < centers.get(i).size(); j++) {
					if(j < centers.get(i).size() - 1)
						System.out.print(centers.get(i).get(j) + ",");
					else
						System.out.println(centers.get(i).get(j) + "]");
				}
			}
		}
		System.out.println("Cost="+cost);
	}

	/**
	 * Runs KMeans with random restart.
	 * @param k no. of clusters
	 * @param data data on which to run KMeans
	 * @param verbose mode of output required
	 * @param r no. of random restarts
	 * @param d dimension of the datapoints
	 */
	public void kMeansWithRandomRestart(int k, Map<String, List<Double>> data, boolean verbose, int r, int d) {
		List<Set<String>> bestClustering = new ArrayList<Set<String>>(); //Stores the best clustering of all the runs of the kMeans algorithm.
		List<List<Double>> bestCenters = new ArrayList<List<Double>>();
		double minCost = Double.MAX_VALUE;
		double randomRestartCosts[] = new double[r];
		int totalIterations = 0;
		int numberOfStarvations = 0;
		int noOfCloseSolutions = 0;
		
		for(int i = 1; i <= r; i++) {
			//Initializes the clustering and cluster centers for every random restart of the kMeans algorithm.
			List<Set<String>> clustering = new ArrayList<Set<String>>();
			List<List<Double>> centers = new ArrayList<List<Double>>();
			for(int j = 0;j < k;j++) {
				clustering.add(new HashSet<String>());
				centers.add(new ArrayList<Double>());
				for(int l = 0; l < d; l++)
					centers.get(j).add(0.0);
			}
			double cost = 0.0;

			int numberOfIterations = kMeans(k,data,verbose,i,d,clustering,centers);
			totalIterations += numberOfIterations;

			//Calculate cost of current restart of kMeans.
			for(int j = 0; j < clustering.size(); j++)
				for(String key : clustering.get(j))
					cost += getCost(data.get(key), centers.get(j));
			randomRestartCosts[i-1] = cost;

			if(checkStarvation(clustering)) 
				numberOfStarvations++;

			if(cost < minCost) {
				minCost = cost;
				bestClustering = clustering;
				bestCenters = centers;
			}		
		}

		System.out.println("Best clustering found:\nKMeans terminates with final clustering");
		verboseReport(bestClustering, bestCenters, data);
		System.out.println("Average number of iterations: " + (double) totalIterations/r + ".\n" + "Starvation occurred " + numberOfStarvations + " times.");
		for(int i = 0;i < r; i++) if(randomRestartCosts[i] < 2.0*minCost) noOfCloseSolutions++;
		System.out.println("A solution within a factor of 2 was found in " + (double) noOfCloseSolutions/r + " of the random restarts.");
	}
}
