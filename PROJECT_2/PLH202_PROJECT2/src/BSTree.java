
/**
 * This abstract class is a binary search tree
 * @author Mike
 *
 */

public abstract class BSTree implements BinarySearchTree{
	
	public abstract int insert( int key );
	
	public abstract int search( int key );
	
	public abstract void inOrder( int root );
	
	public abstract int range( int subRoot, int low, int high );

	/**
	 * This method gets comparisons counter
	 * @return comparisons counter
	 */
	public abstract int getComparisonCounter();
	
	/**
	 * This method sets comparisons counter into the given value
	 * @param comparisonCounter
	 */
	public abstract void setComparisonCounter( int comparisonCounter );
	
	/**
	 * This method gets root's key
	 * @return root's key
	 */
	public abstract int getRootKey();
	
	/**
	 * This method prints the tree
	 */
	public abstract void print() ;
}
