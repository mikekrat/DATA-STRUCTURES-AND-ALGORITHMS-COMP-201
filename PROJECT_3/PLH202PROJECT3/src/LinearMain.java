import java.io.*; 
import java.util.*; 

/**
 * This class toggles the main method and a menu of choices which runs 
 * the asked experiments (choice 1) or it terminates (choice 0) 
 * @author Mike
 *
 */
public class LinearMain {
	
	/**
	 * it is the number of bytes that are used to store a number on a random access file 
	 */
	static final int defaultSize=4;
	/**
	 * This is the initial number of buckets on the LinearHashings
	 */
	static final int initPages = 100;
	/**
	 * This is the size of every bucket on the LinearHashings
	 */
	static final int bucketSize = 10;
	/**
	 * it counts where the cursor is on a RAFile for hashtable05's insertions
	 */
	static int countFileLength05=0;
	/**
	 * it counts where the cursor is on a RAFile for hashtable08's insertions
	 */
	static int countFileLength08=0;
	/**
	 * it points to the right index of the array lists used for searches and deletions
	 */
	static int j=0;
	/**
	 * it is the number of insertions done on the experiments 
	 */
	static final int insertionsSize = 100;
	/**
	 * it is the number of searches and deletions done on the experiments 
	 */
	static final int searchesDeletionsSize = 50;
	
	public static void main(String args[]) throws IOException{
		
		String fileName = "";
		if( args.length > 0 ){
			fileName = args[0];
		}
		StandardInputRead reader = new StandardInputRead(); 
		LinearHashing hashTable08 = new LinearHashing(bucketSize, initPages,(float)0.8);
		LinearHashing hashTable05 = new LinearHashing(bucketSize, initPages,(float)0.5);
		BSTDynamic BSTD = new BSTDynamic();
		RandomAccessFile file = new RandomAccessFile(fileName,"rw");
		ArrayList<Integer> insertedNumbers08 = new ArrayList<>();
		ArrayList<Integer> insertedNumbers05 = new ArrayList<>();
		ArrayList<Integer> insertedNumbersBST = new ArrayList<>();
		Random r = new Random();
		printMenu1();
		while(true) {
			switch (reader.readInt("CMD> ")) {
			case 0:
				System.out.println("Process Terminated");
				file.close();
				return;
			case 1:
				double [][] experimentResults = new double[9][insertionsSize];
				int counter = 0;
				System.out.print("Input size (Í)| LH u>50% avg comparisons per insertion |"
								+  "   LH u>50% avg comparisons per search    |"
								+  "   LH u>50% avg comparisons per deletion  |"
								+  "  LH u>80% avg comparisons per insertion  |"
								+  "   LH u>80% avg comparisons per search    |"
								+  "   LH u>80% avg comparisons per deletion  |"
								+  "    BST avg comparisons per insertion     |"
								+  "     BST avg comparisons per search	    |"
								+  "    BST avg comparisons per deletion      |");
				for(int countExperiments = insertionsSize; countExperiments <= insertionsSize * insertionsSize; countExperiments += insertionsSize) {
					System.out.printf("\n%13d:", countExperiments);
					readNumbersHT(file, hashTable08, insertionsSize, insertedNumbers08, experimentResults, counter);
					readNumbersHT(file, hashTable05, insertionsSize, insertedNumbers05, experimentResults, counter);
					insertedNumbersBST = insertedNumbers08;
					readNumbersBST(BSTD,insertionsSize,insertedNumbersBST, experimentResults, counter);
					searchNumbersHT(hashTable08, searchesDeletionsSize, insertedNumbers08, r, experimentResults, counter);
					searchNumbersHT(hashTable05, searchesDeletionsSize, insertedNumbers05, r, experimentResults, counter);
					searchNumbersBST(BSTD, searchesDeletionsSize, insertedNumbersBST, r, experimentResults, counter);
					deleteNumbersHT(hashTable08, searchesDeletionsSize, insertedNumbers08, r, experimentResults, counter);
					deleteNumbersHT(hashTable05, searchesDeletionsSize, insertedNumbers05, r, experimentResults, counter);
					deleteNumbersBST(BSTD, searchesDeletionsSize, insertedNumbersBST, r, experimentResults, counter);
					j -= searchesDeletionsSize;
					for (int i = 0; i < experimentResults.length; i++) {
						System.out.printf("%40.2f | ",experimentResults[i][counter]);
					}
					counter++;
				}
				
				
				hashTable08 = new LinearHashing(bucketSize, initPages,(float)0.8);
				hashTable05 = new LinearHashing(bucketSize, initPages,(float)0.5);
				BSTD = new BSTDynamic();
				j = 0;
				countFileLength05 = 0;
				countFileLength08 = 0;
				break;
			}
			printMenu1();
		}
	}
	
	/**
	 * This method reads an asked number of keys from a RAFile and inserts it into
	 * a LinearHashing and counts the average number of comparisons done and stores
	 * it into a double double array. It also inserts the keys into an array list used 
	 * for other methods
	 * @param file is the RAFile instance
	 * @param hashTable is the LinearHashing instance
	 * @param keysNum is the asked number of keys
	 * @param insertedNumbers is the array list instance 
	 * @param experimentResults is the double double array instance
	 * @param counter
	 * @throws IOException
	 */
	public static void readNumbersHT(RandomAccessFile file, LinearHashing hashTable,int keysNum,
			ArrayList<Integer> insertedNumbers,double[][] experimentResults, int counter) throws IOException {
		int countFileLength=0;
		if(hashTable.getMaxThreshold() == 0.5)
			countFileLength = countFileLength05;
		else 
			countFileLength = countFileLength08;
		file.seek(countFileLength);
		int currentNum,totalComps=0;
		for(int i=0; (i < keysNum) && (countFileLength < file.length()); i++) {
			currentNum = file.readInt(); 
			hashTable.insertKey(currentNum);
			totalComps += hashTable.getComparisonsNum();
			insertedNumbers.add(currentNum);
			countFileLength += defaultSize;
		}
		double result = (double)totalComps / keysNum;
		if(hashTable.getMaxThreshold() == 0.5) {
			countFileLength05 = countFileLength;
			experimentResults[0][counter] = result;
		}
		else {
			countFileLength08 = countFileLength;
			experimentResults[3][counter] = result;
		}
	}
	/**
	 * This method reads an asked number of keys from an array list and inserts it into
	 * a BSTDynamic and counts the average number of comparisons done and stores
	 * it into a double double array
	 * @param BSTD is the BSTDynamic instance
	 * @param keysNum is the asked number of keys
	 * @param insertedNumbers is the array list instance
	 * @param experimentResults is the double double array instance
	 * @param counter
	 */
	public static void readNumbersBST(BSTDynamic BSTD,int keysNum, ArrayList<Integer> insertedNumbers,
			double[][] experimentResults, int counter) {
		int totalComps=0,counter1=0;
		while( (j < insertedNumbers.size()) && (counter1 < keysNum)) {
			totalComps += BSTD.insert(insertedNumbers.get(j));
			j++;
			counter1++;
		}
		double result = (double)totalComps / keysNum; 
		experimentResults[6][counter] = result;
	}
	
	/**
	 * This method searches an asked number of keys randomly from a LinearHashing and 
	 * counts the average number of comparisons done and stores it into a double double array
	 * @param hashTable is the LinearHashing instance
	 * @param keysNum is the asked number of keys
	 * @param insertedNumbers
	 * @param r is a Random instance
	 * @param experimentResults is the double double array instance
	 * @param counter
	 */
	public static void searchNumbersHT(LinearHashing hashTable,int keysNum, ArrayList<Integer> insertedNumbers,
			Random r, double[][] experimentResults, int counter) {
		int searchKey, totalComps=0;
		for (int i = 0; i < keysNum && i<insertedNumbers.size(); i++){
		   searchKey = insertedNumbers.get(r.nextInt(insertedNumbers.size()));
		   hashTable.searchKey(searchKey);
		   totalComps += hashTable.getComparisonsNum();
		}
		double result = (double)totalComps / keysNum; 
		if(hashTable.getMaxThreshold() == 0.5) {
			experimentResults[1][counter] = result;
		}
		else {
			experimentResults[4][counter] = result;
		}
	}
	
	/**
	 * This method searches an asked number of keys randomly from a BSTDynamic and 
	 * counts the average number of comparisons done and stores it into a double double array
	 * @param BSTD is the BSTDynamic instance
	 * @param keysNum is the asked number of keys
	 * @param insertedNumbers
	 * @param r is a Random instance
	 * @param experimentResults is the double double array instance
	 * @param counter
	 */
	public static void searchNumbersBST(BSTDynamic BSTD,int keysNum, ArrayList<Integer> insertedNumbers,
			Random r, double[][] experimentResults, int counter) {
		int searchKey, totalComps=0;
		for (int i = 0; i < keysNum && i<insertedNumbers.size(); i++){
		   searchKey = insertedNumbers.get(r.nextInt(insertedNumbers.size()));
		   totalComps += BSTD.search(searchKey);
		}
		double result = (double)totalComps / keysNum; 
		experimentResults[7][counter] = result;
		
	}
	
	/**
	 * This method deletes an asked number of keys randomly from a LinearHashing and 
	 * counts the average number of comparisons done and stores it into a double double array. 
	 * It also deletes those keys from the array list used for searches and deletions
	 * @param hashTable is the LinearHashing instance
	 * @param keysNum is the asked number of keys
	 * @param insertedNumbers is the array list instance
	 * @param r is a Random instance
	 * @param experimentResults is the double double array instance
	 * @param counter
	 */
	public static void deleteNumbersHT(LinearHashing hashTable,int keysNum, ArrayList<Integer> insertedNumbers,
			Random r, double[][] experimentResults, int counter) {
		int deleteKey, totalComps=0;
		for(int i = 0; i < keysNum && i<insertedNumbers.size(); i++){
			int deleteKeyIndex = r.nextInt(insertedNumbers.size());
			deleteKey = insertedNumbers.get(deleteKeyIndex);
		    hashTable.deleteKey(deleteKey);
		    totalComps += hashTable.getComparisonsNum();
		    insertedNumbers.remove(deleteKeyIndex);
		}
		double result = (double)totalComps / keysNum; 
		if(hashTable.getMaxThreshold() == 0.5) {
			experimentResults[2][counter] = result;
		}
		else {
			experimentResults[5][counter] = result;
		}
	}
	
	/**
	 * This method deletes an asked number of keys randomly from a BSTDynamic and 
	 * counts the average number of comparisons done and stores it into a double double array. 
	 * It also deletes those keys from the array list used for searches and deletions
	 * @param BSTD is the BSTDynamic instance
	 * @param keysNum is the asked number of keys
	 * @param insertedNumbers is the array list instance
	 * @param r is a Random instance
	 * @param experimentResults is the double double array instance
	 * @param counter
	 */
	public static void deleteNumbersBST(BSTDynamic BSTD,int keysNum, ArrayList<Integer> insertedNumbers,
			Random r, double[][] experimentResults, int counter) {
		int deleteKey, totalComps=0;
		for(int i = 0; i < keysNum && i<insertedNumbers.size(); i++){
			int deleteKeyIndex = r.nextInt(insertedNumbers.size());
			deleteKey = insertedNumbers.get(deleteKeyIndex);
		    totalComps +=  BSTD.deleteKey(deleteKey);
		    insertedNumbers.remove(deleteKeyIndex);
		}
		double result = (double)totalComps / keysNum; 
		experimentResults[8][counter] = result;
	}

	/**
	 * It prints the menu of choices
	 */
	public static void printMenu1() {
		System.out.println("\n=================================");
		System.out.println("0.Terminate");
		System.out.println("1.Run asked experiments");
		System.out.println("=================================");
	}
}

