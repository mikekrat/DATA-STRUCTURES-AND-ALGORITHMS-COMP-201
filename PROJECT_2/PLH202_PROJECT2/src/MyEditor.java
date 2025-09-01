import java.io.*;
import java.util.*;

/**
 * This class toggles the main method and a menu of choices which runs 
 * the menu of BSTArray or BSTDynamic (choices 1 and 2) or of a sorted array of 
 * both trees' numbers (choice 3) or it terminates (choice 0)  
 * @author Mike
 *
 */
public class MyEditor {
	
	/**
	 * it is the number of bytes that are used to store a number on a random access file 
	 */
	static final int intSize = 4;
	
	/**
	 * it is BST's size
	 */
	static int BSTLength = 0;
	
	/**
	 * It counts the number of comparisons done in some specific methods
	 */
	static int countComparisons = 0;
	
	/**
	 * It counts the runtime in ns before a method is called
	 */
	static long start = 0;
	
	/**
	 * It counts the runtime in ns after a method is called
	 */
	static long end = 0;
	
	/**
	 * It counts the runtime in ns before the insert method is called
	 */
	static long startIns = 0;
	
	/**
	 * It counts the runtime in ns after the insert method is called
	 */
	static long endIns = 0;
	
	/**
	 * It contains the total runtime taken for insertions on BSTArray
	 */
	static long insRuntimeBSTArray = 0;
	
	/**
	 * It contains the total runtime taken for insertions on BSTDynamic
	 */
	static long insRuntimeBSTDynamic = 0;
	
	public static void main( String[] args ) {
		StandardInputRead reader = new StandardInputRead();
		String fileName = "";
		if( args.length > 0 ){
			fileName = args[0];
		}
		BSTArray BSTA = new BSTArray();
		BSTDynamic BSTD = new BSTDynamic();
		System.out.println( "filename is: " + fileName );
		int SUM = readNumbers( fileName, BSTA );
		int SUM2 = readNumbers( fileName, BSTD );
		ArrayList<Integer> sortedTree = new ArrayList<>();
		BSTA.sortTreeToAL( BSTA.getRoot(), sortedTree );
		while(true){
			printMenu1();
			switch( reader.readInt("CMD> ") ){
			case 0:
				System.out.println( "Process terminated" );
				return;
			case 1:
				menu( BSTA, reader, SUM );	
				break;
			case 2:
				menu( BSTD, reader, SUM2 );
				break;
			case 3:
				menu2( reader, sortedTree );
				break;
			default:
				System.out.println( "Bad command..." );
				break;
			}
		}
	}
	
	/**
	 * This method toggles a menu for BSTrees which calculates the average 
	 * insert comparisons number and its runtime (choice 1), 
	 * calculates the average comparisons number for 100 random searches and its runtime (choice 2), 
	 * calculates the average comparisons number for 100 random range searches (choice 3),
	 * prints the tree (choice 4), inserts a number on the tree given from the user (choice 5),
	 * searches a number from the tree given from the user (choice 6),
	 * searches a given from the user range (choice 7),
	 * prints the tree sorted (in order) (choice 8) and returns to the first menu (choice 0)
	 * @param BST
	 * @param reader
	 * @param SUM
	 * @param root
	 */
	public static void menu( BSTree BST, StandardInputRead reader, int SUM ){
			
		printMenu();
		while( true ){
			switch( reader.readInt( "CMD> " ) ){
			case 0:
				return;
			case 1:
				printInsertAverage( BST, SUM );
				break;
			case 2:
				calcSearchAverage( BST );
				System.out.println( "Search runtime: " + ( end - start ) );
				end = 0; start = 0;
				break;
			
			case 3:
				calcRangeComps( BST, BST.getRootKey(), reader );
				break;
			case 4:
				BST.print();
				break;
			case 5:
				BST.inOrder( BST.getRootKey() );
				break;
			default:
				System.out.println( "Bad command..." );
				break;
			}
		}
	}
	
	
	/**
	 * This method toggles a menu for the sorted array of both trees' numbers which 
	 * calculates the average comparisons number for 100 random searches and its runtime (choice 1),
	 * calculates the average comparisons number for 100 random range searches (choice 2),
	 * searches a number from the array given from the user (choice 3) 
	 * and returns to the first menu (choice 0)
	 * 
	 * @param reader
	 * @param sortedTree
	 */
	public static void menu2( StandardInputRead reader, ArrayList<Integer> sortedTree ){
		printMenu2();
		while(true){
			switch(reader.readInt("CMD> ")){
			case 0:
				return;
			case 1:
				calcAverageCompAL( sortedTree );
				System.out.println( "Search runtime: " + ( end - start ) );
				break;
			case 2:
				calcSTRange( sortedTree, reader );		
				break;
			default:
				System.out.println( "Bad command" );
				break;
			}
		}
	}
	
	/**
	 * This method calculates the average comparisons number for 100 random searches from 
	 * the tree in the given range 
	 * @param BST
	 * @param root
	 * @param reader
	 */
	public static void calcRangeComps( BSTree BST, int root, StandardInputRead reader ){
		long totalComparisons=0;
		int low = 0,i;
		int K=0;
		
		K = giveRightInput( K, reader );
		
		for( i=0; i<100; i++ ){
			low = getRandomNumberInRange( -109663890, 1996638999 );
			totalComparisons += BST.range( root , low , low + K );
		}
		
		System.out.println( "Range comparisons: " + totalComparisons / i );
		
	} 
	
	/**
	 * This method calculates the average comparisons number for 100 random searches from 
	 * the sorted array in the given range
	 * @param sortedTree
	 * @param reader
	 */
	public static void calcSTRange( ArrayList<Integer> sortedTree, StandardInputRead reader ){
		int low,K=0;
		K = giveRightInput( K, reader );
		int totalComparisons = 0,i;
		for( i=0; i<100; i++ ){
			low = getRandomNumberInRange( -109663890, 1996638999 );	
			int lowIndex = binSearch( sortedTree, low );
			totalComparisons += countComparisons;
			int highIndex = binSearch( sortedTree, low + K );
			totalComparisons += countComparisons;
			for( int j=lowIndex; j<highIndex; j++ ){
				totalComparisons ++;
				//System.out.println( sortedTree.get(j) );
				if( j == highIndex - 1 & ++totalComparisons>=0){
					//System.out.print( "\n" );
				}
			}	
		}
		System.out.println( "Range comparisons average num: " + totalComparisons / i );
	} 
	
	/**
	 * This method forces the user to give one of the suggested ranges (100 or 1000)
	 * @param key
	 * @param reader
	 * @return the given from the user range
	 */
	public static int giveRightInput( int key, StandardInputRead reader ){
		key = reader.readInt( "Give me range (100 or 1000): " );
		while(( key != 100 && key != 1000 )){
			System.out.println( "Error input..." );
			key = reader.readInt( "Give me range (100 or 1000): " );
		}
		return key;
	}
	
	/**
	 * This method calculates the average comparisons number for 100 random searches from 
	 * the sorted array
	 * @param al
	 */
	public static void calcAverageCompAL( ArrayList<Integer> al ){
		start = System.nanoTime();
		int sum = 0,i=0;
		while( i < 100 ){
			binSearch( al, getRandomNumberInRange( -109663890, 1996638999 ));
			sum += countComparisons;
			i++;
		}
		System.out.println( "Sorted tree search comparisons average: " + sum  /  i );
		end = System.nanoTime();
	}
	
	/**
	 * This method searches a given number in a sorted array list binary 
	 * @param al
	 * @param key
	 * @version https://www.youtube.com/watch?v=hKI93hOfeIk
	 * @return The index of the number in the array list (if found) 
	 * or the closest number's index (if it was not found) 
	 */
	public static int binSearch( ArrayList<Integer> al, int key ){
		int low = 0;
		int high = al.size()-1;
		countComparisons = 0;
		int middle=-1;
		
		while( low <= high & ++countComparisons>=0){
			middle = ( low + high ) / 2;
			countComparisons++;
			if( key < al.get( middle ) & ++countComparisons>=0 ){
				countComparisons++;
				high = middle - 1;
			}
			else if( key > al.get( middle ) & ++countComparisons>=0 ){
				countComparisons++;
				low = middle + 1;
			}
			else{
				//System.out.println( key + " was found at: " + middle );
				return middle;
			}
		}
		return middle;
	}
	
	/**
	 * This method reads all the numbers from a file and stores them to an
	 * array list of integers
	 * @param name is file's name
	 * @param BST
	 * @return whatever calcCompSum returns
	 */
	public static int readNumbers( String name, BSTree BST ){
		int counter=0;
		ArrayList<Integer> al = new ArrayList<>();
		BSTLength = 0;
		try {
			RandomAccessFile file = new RandomAccessFile( name , "rw" );
			file.seek( counter );
			while( counter < file.length() ){
				al.add( file.readInt() );
				counter += intSize;
				BSTLength++;
			}		
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return calcCompSum( al, BST );
	    
	}
	/**
	 * This method inserts to a BSTree all the numbers from an array list of integers
	 * @param al
	 * @param BST
	 * @return the total comparisons number done to insert the numbers to the BSTree 
	 */
	public static int calcCompSum( ArrayList<Integer> al, BSTree BST ){
		int compSum=0;
		if(BST instanceof BSTArray){
			((BSTArray) BST).setLength(al.size());
		}startIns = System.nanoTime();
		for( int i=0; i < al.size(); i++ ){
			compSum += BST.insert( al.get(i) );
		}
		endIns = System.nanoTime();
		if(BST instanceof BSTArray){
			insRuntimeBSTArray = endIns - startIns;
		}else if(BST instanceof BSTDynamic){
			insRuntimeBSTDynamic = endIns - startIns;
		}
		return compSum ;
	}
	
	/**
	 * This method prints the insert average comparisons and its runtime
	 * @param BST
	 * @param sum
	 */
	public static void printInsertAverage( BSTree BST, int sum ){
		
		if(BST instanceof BSTArray){
			System.out.println( "BST array insert comparisons average: "+ sum / BSTLength );
		}else if(BST instanceof BSTDynamic){
			System.out.println( "BST dynamic insert comparisons average: "+ sum / BSTLength );
		}
		if(BST instanceof BSTArray){
			System.out.println( "Insert runtime BSTARRAY: " + insRuntimeBSTArray );
		}else if(BST instanceof BSTDynamic){
			System.out.println( "Insert runtime BSTDYNAMIC: " + insRuntimeBSTDynamic );
		}
	}
	
	/**
	 * This method calculates the average comparisons number 
	 * for 100 random searches in a BSTree and its runtime
	 * @param BST
	 */
	public static void calcSearchAverage( BSTree BST ){
		int sum=0,i=0;
		start = System.nanoTime();
		while( i < 100 ){
			sum+=BST.search(getRandomNumberInRange( -19966389, 1996638999 ));
			i++;
		}
		end = System.nanoTime();
		if(BST instanceof BSTArray)
			System.out.println( "BST array search average: " + sum / i );
		else if(BST instanceof BSTDynamic)
			System.out.println( "BST dynamic search average: " + sum / i );
		
	}
	
	/**
	 * This method generates a random integer in the given range (max - min)
	 * @param min
	 * @param max
	 * @version https://mkyong.com/java/java-generate-random-integers-in-a-range/
	 * @return that random number
	 */
	public static int getRandomNumberInRange( int min, int max ) {

		if ( min >= max ) {
			throw new IllegalArgumentException( "max must be greater than min" );
		}

		Random r = new Random();
		return r.nextInt( (max - min) + 1 ) + min;
	}
	
	/**
	 * This method prints the menu for BSTrees
	 */
	public static void printMenu(){
		System.out.println( "=============" );
		System.out.println( "0.Return" );
		System.out.println( "1.Insert comparisons average & runtime" );
		System.out.println( "2.Search comparisons average & runtime" );
		System.out.println( "3.Range comparisons average & runtime" );
		System.out.println( "4.Print the tree" );
		System.out.println( "5.In order" );
		System.out.println( "=============" );
	}
	
	/**
	 * This method prints the menu toggled from main method
	 */
	public static void printMenu1(){
		System.out.println( "=============" );
		System.out.println( "0.Terminate" );
		System.out.println( "1.Run BST array" );
		System.out.println( "2.Run BST dynamic" );
		System.out.println( "3.Run sorted array list" );
		System.out.println( "=============" );
		
	}
	
	/**
	 * This method prints sorted array's menu
	 */
	public static void printMenu2(){
		System.out.println( "=============" );
		System.out.println( "0.Return" );
		System.out.println( "1.Search comparisons average & runtime" );
		System.out.println( "2.Range comparisons average" );
		System.out.println( "=============" );
	} 
	
}
