
import java.util.*;

/**
 * This class is a binary search tree array implementation 
 * @author Mike
 *
 */
public class BSTArray extends BSTree {
	
	/**
	 * This is the int array where the tree is stored 
	 */
	private int[][] tree;
	
	/**
	 * This is the first available index on the array
	 */
	private int firstAvail;
	
	/**
	 * This is tree's root's index
	 */
	private int root;
	
	/**
	 * This is tree's length
	 */
	private int length = 1000000;
	
	/**
	 * This is the counter of comparisons done in a method
	 */
	private int comparisonCounter = 0;
		
	/**
	 * This method is a constructor for binary search tree array
	 */
	public BSTArray(){
		tree = new int[3][length];
		for( int j=0; j<tree.length; j++ ){
			for( int i=0; i<length; i++ ){
				tree[j][i] = -1;
			}
		}
		root = -1;		
		for( int i=0; i<length; i++ ){
			tree[2][i] = i+1;
			if( i==length-1 )
				tree[2][i] = -1;
		}
		firstAvail = 0;
	}
	
	public void print(){
		for(int i=0; i<length; i++){
			System.out.println(i+": "+tree[0][i]+" "+tree[1][i]+" "+tree[2][i]);
		}
	}
	
	/**
	 * This method gets the array of integers where the tree is stored
	 * @return the array of integers where the tree is stored
	 */
	public int[][] getTree() {
		return tree;
	}

	/**
	 * This method sets the array of integers where the tree is stored to the given value
	 * @param tree
	 */
	public void setTree( int[][] tree ) {
		this.tree = tree;
	}

	/**
	 * This method is a getter for firstAvail
	 * @return
	 */
	public int getFirstAvail() {
		return firstAvail;
	}

	/**
	 * This method is a setter for firstAvail
	 * @param firstAvail
	 */
	public void setFirstAvail( int firstAvail ) {
		this.firstAvail = firstAvail;
	}

	/**
	 * This method gets tree's root
	 * @return tree's root
	 */
	public int getRoot() {
		return root;
	}

	/**
	 * This method sets tree's root into the given value
	 * @param root
	 */
	public void setRoot( int root ) {
		this.root = root;
	}

	/**
	 * This method is a getter for tree's length
	 * @return tree's length
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * This method is a setter for tree's length
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	public int getComparisonCounter() {
		return comparisonCounter;
	}

	public void setComparisonCounter( int comparisonCounter ) {
		this.comparisonCounter = comparisonCounter;
	}

	public int insert( int key ){		
		int nextAvail=-1;
		int cc=0;
		if( firstAvail >= length  & ++comparisonCounter>=0){
			System.out.println( "The tree is full!!" );
			cc = comparisonCounter;
			comparisonCounter = 0;
			return cc;
		}else if(firstAvail != -1 & ++comparisonCounter>=0){	
			tree[0][firstAvail] = key;
			nextAvail = tree[2][firstAvail];
			tree[2][firstAvail] = -1;
			comparisonCounter += 3;
			if (root == -1 & ++comparisonCounter>=0) { 
				root = firstAvail; 
	            setFirstAvail( nextAvail );
	            comparisonCounter += 2;
	            cc = comparisonCounter;
				comparisonCounter = 0;
				return cc;
	        } 
		}		
		int current = root;
		comparisonCounter ++;
		while( true & ++comparisonCounter>=0){
			if( key > tree[0][current] & ++comparisonCounter>=0){
				if( tree[2][current] == -1 & ++comparisonCounter>=0 ){
					tree[2][current] = firstAvail;
					comparisonCounter ++;
					break;
				}
				current = tree[2][current];
				comparisonCounter ++;
			}
			else if( key < tree[0][current] & ++comparisonCounter>=0 ){
				comparisonCounter++;
				if( tree[1][current] == -1 & ++comparisonCounter>=0 ){
					tree[1][current] = firstAvail;
					comparisonCounter ++;
					break;
				}
				current = tree[1][current];
				comparisonCounter ++;
			}
		}
		if(firstAvail != -1 & ++comparisonCounter>=0){
			setFirstAvail( nextAvail );
			comparisonCounter ++;
		}
		cc = comparisonCounter;
		comparisonCounter = 0;
		return cc;
		
	}
	
	public int search( int key ){
		int current = root , cc = 0;
		comparisonCounter ++;
		while(current != -1 & ++comparisonCounter>=0){
			if(tree[0][current] == key & ++comparisonCounter>=0){
				//System.out.println(key+" was found at: "+current+" with left and right indexes: " + tree[1][current]+" "+ 
									//tree[2][current]);
				cc = comparisonCounter;
				comparisonCounter = 0;
				return cc;
			}
			if(tree[0][current] < key & ++comparisonCounter>=0){
				current = tree[2][current];
				comparisonCounter ++;
			}
			else if(tree[0][current]>key & ++comparisonCounter>=0){
				current=tree[1][current];
				comparisonCounter ++;
			}
		}
		cc = comparisonCounter;
		comparisonCounter = 0;
		return cc;

	}

	public void inOrder(int root) {
		if(root!=-1){
			inOrder(tree[1][root]);
			System.out.println(tree[0][root]);
			inOrder(tree[2][root]);
		}
	}
	/**
	 * This method sorts the tree into an array list  
	 * @param line is the node from which sorting starts
	 * @param al
	 */
	public void sortTreeToAL(int line, ArrayList<Integer> al){
		if(line!=-1){
			sortTreeToAL(tree[1][line],al);
			al.add(tree[0][line]);
			sortTreeToAL(tree[2][line],al);
		}
	}
	
	public int range(int subRoot, int low,int high){
		comparisonCounter=0;
		return rangeRec(subRoot,low,high);
	}
	
	/**
	 * This method is a recursive implementation of range
	 * @param subRoot
	 * @param low
	 * @param high
	 * @return The number of comparisons done
	 */
	public int rangeRec(int subRoot, int low,int high){
		if(subRoot==-1 & ++comparisonCounter>=0){
			return comparisonCounter;
		}
		
		if(low<tree[0][subRoot] & ++comparisonCounter>=0){
			rangeRec(tree[1][subRoot],low,high);
		}
		if(low<tree[0][subRoot] && tree[0][subRoot]<high & ++comparisonCounter>=0){
			//System.out.println(tree[0][subRoot]);	
		}
		if(tree[0][subRoot]<high & ++comparisonCounter>=0){
			rangeRec(tree[2][subRoot],low,high);
		}
		return comparisonCounter;
	}

	public int getRootKey() {
		return getRoot();
	}
	
	
	
}
