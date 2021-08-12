KMeans Clustering:
1. The main function in java file Executor.java in package ai.unsupervisedlearning.clustering runs the KMeans with RandomRestart program.

2. The input for this program is defined in KMeans-master/src/input_files/kmeans_input.txt. Replace the contents of this input file to modify 
any or all of the following three parameters in their respective order: 
i) k : no. of clusters ii) r : no. of random restarts iii) output mode : 1-verbose/0-compact.

3. The data points on which the KMeans algorithm is run is defined in KMeans-master/src/input_files/data.csv. Replace the contents of this data 
file to run KMeans with Random Restart on any other dataset.

To run the above program:
1. Unzip KMeans-master.zip.
2. cd KMeans-master
3. Replace the contents of src/input_files/kmeans_input.txt to change the input parameters for the program.
4. Replace the contents of src/input_files/data.csv to change the dataset on which KMeans is run.

Commands to run the above program on Windows:

dir /s /B *.java > sources.txt (To populate a list of .java files in a single file sources.txt)
javac -d classes @sources.txt (To compile the java files populated in sources.txt)
java -cp classes ai.unsupervisedlearning.clustering.Executor (To run Executor.java)

Commands to run the above programs on Linux/Mac

find . -name "*.java" > sources.txt (To populate a list of .java files in a single file sources.txt)
mkdir classes
javac -d classes @sources.txt (To compile the java files populated in sources.txt)
java -cp classes ai.unsupervisedlearning.clustering.Executor (To run Executor.java)