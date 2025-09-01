
/**
 * This interface has the basic methods for a binary search tree 
 * @author Mike
 *
 */
public interface BinarySearchTree {
	 /**
	  * This method inserts keys on the tree
	  * @param key
	  * @return The number of comparisons done
	  */
	 int insert(int key);
	 
	 /**
	  * This method searches keys from the tree
	  * @param key
	  * @return The number of comparisons done
	  */
	 int search(int key);
	 
	 /**
	  * This method prints tree's nodes sorted
	  * @param root is tree's root
	  */
	 void inOrder(int root);
	 
	 /**
	  * This method prints tree's nodes in the given range
	  * @param subRoot is each subtree's root
	  * @param low is the number where printing starts 
	  * @param high is the number where printing ends 
	  * @return The number of comparisons done
	  */
	 int range(int subRoot, int low,int high);
}
