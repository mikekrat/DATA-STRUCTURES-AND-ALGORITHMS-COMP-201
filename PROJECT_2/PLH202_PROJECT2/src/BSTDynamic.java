
/**
 * This class is a binary search tree dynamic implementation 
 * @author Mike
 * @version https://www.geeksforgeeks.org/binary-search-tree-set-1-search-and-insertion/
 */
public class BSTDynamic extends BSTree{
	
	/**
	 *  This class contains left and right child of current node and key value
	 *  @author Mike
	 */
    public class Node { 
    	
    	/**
    	 * This is node's info
    	 */
        int key; 
        
        /**
         * This is node's left node
         */
        Node left;
        
        /**
         * This is node's right node
         */
        Node right; 
  
        /**
         * This method is Node's constructor
         * @param key
         */
        public Node(int key) { 
            this.key = key; 
            left = right = null; 
        } 
    } 
  
    /**
     * This is dynamic tree's root
     */
    private Node root; 
    
    /**
	 * This is the counter of comparisons done in a method
	 */
    private int comparisonCounter=0;
	
    /**
     * This method is dynamic tree's constructor
     */
    public BSTDynamic() {  
        root = null;  
    } 
  
    public int insert(int key) { 
       root = insertRec(root, key); 
       comparisonCounter++;
       int cc = comparisonCounter;
       comparisonCounter=0;
       return cc;
    } 
      
    /**
     * This method is a recursive implementation for insert
     * @param root
     * @param key
     * @return tree's root
     */
    public Node insertRec(Node root, int key) { 
  
        /* If the tree is empty, return a new node */
        if (root == null & ++comparisonCounter>=0) { 
        	root = new Node(key); 
        	comparisonCounter ++;
            return root; 
        } 
  
        /* Otherwise, recur down the tree */
        if (key < root.key & ++comparisonCounter>=0) {
        	root.left = insertRec(root.left, key);
        	comparisonCounter ++;
        }
        else if (key > root.key & ++comparisonCounter>=0){ 
        	 root.right = insertRec(root.right, key);
        	 comparisonCounter ++;
        }
  
        /* return the (unchanged) node pointer */
        return root; 
    } 
  
    public void inOrder(int root) {
    	Node tempRoot = findRoot(this.root,root);  
    	comparisonCounter=0;
    	inOrderRec(tempRoot);
    }
    	
    /**
     * This method is a recursive implementation for inOrder
     * @param root
     */
    public void inOrderRec(Node root){
        if (root != null) { 
            inOrderRec(root.left); 
            System.out.println(root.key); 
            inOrderRec(root.right); 
        } 
    } 

    /**
     * This method finds a key's node and returns it
     * @param root
     * @param key
     * @return key's node
     */
    public Node findRoot(Node root, int key){
    	comparisonCounter=0;
    	while(root != null & ++comparisonCounter>=0){
    		if(key > root.key & ++comparisonCounter>=0){
    			comparisonCounter ++;
    			root = root.right;
    		}else if(key < root.key & ++comparisonCounter>=0){
    			comparisonCounter ++;
    			root = root.left;
    		}else{
    			break;
    		}
    	}
    	return root;
    }
    
	public int search(int key) {
		int cc=0;
		Node tempRoot = findRoot(root,key);   
		Node finRoot = searchRec(tempRoot,key);
		comparisonCounter += 2;
		if(finRoot != null & ++comparisonCounter>=0){
			//System.out.println(finRoot.key+" was found!!");
	    	if(finRoot.left != null & ++comparisonCounter>=0){
	    		//System.out.println(key + "'s left: " + finRoot.left.key);
		    }
	    	if(finRoot.right != null & ++comparisonCounter>=0){
	    		//System.out.println(key + "'s right: " + finRoot.right.key);
		    }    	
	    }
		cc = comparisonCounter;
		comparisonCounter = 0;
		return cc;
	}
	
	/**
	 * This method is a recursive implementation for search
	 * @param root
	 * @param key
	 * @return
	 */
	public Node searchRec(Node root, int key) { 
	    // Base Cases: root is null or key is present at root 
	    if (root == null || root.key == key & ++comparisonCounter>=0) {
	    	return root; 
	    }
	  
	    // val is greater than root's key 
	    else if (root.key > key & ++comparisonCounter>=0) {
	    	return searchRec(root.left, key);
	    }
	  
	    // val is less than root's key 
	    else{
	    	return searchRec(root.right, key);
	    }
	} 
	public int range(int subRoot, int low, int high) {
		comparisonCounter=0;	
    	Node tempRoot = findRoot(root,subRoot); 
		comparisonCounter++;
    	rangeRec(tempRoot,low,high);
    	return comparisonCounter;
    }  
	
	/**
	 * This method is a recursive implementation for range
	 * @param root
	 * @param low
	 * @param high
	 */
	public void rangeRec(Node root, int low, int high){
		if(root == null & ++comparisonCounter>=0) {
			return;
		}
		if(root.key > low & ++comparisonCounter>=0){
			rangeRec(root.left,low,high);
		}
		if(root.key > low && root.key < high & ++comparisonCounter>=0){
			//System.out.println(root.key);
		}
		if(root.key < high & ++comparisonCounter>=0){
			rangeRec(root.right,low,high);
		}
	}

	public int getRootKey() {
		return root.key;
	}
	
	public int getComparisonCounter() {
		return comparisonCounter;
	}

	public void setComparisonCounter(int comparisonCounter) {
		this.comparisonCounter = comparisonCounter;
	}

	/**
	 * This method is a getter for tree's root
	 * @return tree's root
	 */
	public Node getRoot() {
		return root;
	}

	/**
	 * This method is a setter for tree's root
	 * @param root
	 */
	public void setRoot(Node root) {
		this.root = root;
	}
	
	
	public void print(){
		printRec(root);
	}
	
	/**
	 * This method is a recursive implementation for print
	 * @param root
	 * @version https://stackoverflow.com/questions/47510048/printing-java-binary-search-tree
	 */
	public void printRec(Node root) {
        if (root == null) {return;}
        System.out.println(root.key);
        if(root.left != null){System.out.println(root.key+"'s left: "+root.left.key);}
        if(root.right != null){System.out.println(root.key+"'s right: "+root.right.key);}
        printRec(root.left);
        printRec(root.right);
    }

	
}