
/**
 * This class is used for LinearHashing's buckets
 * @author Mike
 *
 */
class HashBucket {

	/**
	 * number of keys currently stored in the HashBucket
	 */
	private int keysNumber;
	
	/**
	 * pointer to this HashBucket's keys
	 */
	private int[] keys;
	
	/**
	 * This HashBucket's overflow HashBucket
	 */
	private HashBucket overflowBucket;

	/**
	 * This method is a Constructor for this class
	 * @param bucketSize
	 */
	public HashBucket(int bucketSize) {

		keysNumber = 0;
		keys = new int[bucketSize];
		overflowBucket = null;
	}

	/**
	 * This method returns this bucket's number of keys 
	 * @return this bucket's number of keys 
	 */
	public int numKeys(){return keysNumber;}

	/**
	 * This method inserts a key to the node
	 * @param key
	 * @param lh
	 * @param mergeHappened
	 */
	public void insertKey(int key, LinearHashing lh,boolean mergeHappened) {

		int i;
		int bucketSize = lh.getBucketSize();
		int keysNum = lh.getKeysNum();
		int compNum = lh.getComparisonsNum() + 2;
		
		for (i = 0; (i < this.keysNumber & ++compNum>0) && (i < bucketSize & ++compNum>0); i++){
		   if (this.keys[i] == key & ++compNum>0){	//key already here. Ignore the new one
			   lh.setComparisonsNum(compNum);
			   return;
		   }
		}
		if (i < bucketSize & ++compNum>0 ){				// bucket not full write the new key
		  keys[i] = key;
		  this.keysNumber++;
		  compNum += 2;
		  if(!mergeHappened & ++compNum>0) {
			  keysNum++;
			  lh.setKeysNum(keysNum); 			// update linear hashing class.
			  compNum += 2;
			  //System.out.println("HashBucket.insertKey: KeysNum = " + keysNum );
		  }
		}
		else {
		    //System.out.println("Overflow.............");
		    if (this.overflowBucket != null & ++compNum>0){	// pass key to the overflow
		      this.overflowBucket.insertKey(key, lh,mergeHappened);
		    }
		    else {						// create a new overflow and write the new key
			this.overflowBucket = new HashBucket(bucketSize);
			++compNum;
			this.overflowBucket.insertKey(key, lh,mergeHappened);
		    }
		}
		lh.setComparisonsNum(compNum);
	}

	/**
	 * This method deletes a key from this bucket
	 * @param key
	 * @param lh
	 */
	public void deleteKey(int key, LinearHashing lh) { 

		int bucketSize = lh.getBucketSize();
		int keysNum = lh.getKeysNum();
		int compNum = lh.getComparisonsNum() + 2;
		
		for (int i = 0; (i < this.keysNumber & ++compNum>0) && (i < bucketSize & ++compNum>0); i++) {
		   if (this.keys[i] == key & ++compNum>0 ) {
		     if (this.overflowBucket == null & ++compNum>0) {		// no overflow
				 this.keys[i] = this.keys[this.keysNumber-1];
				 this.keysNumber--;
				 keysNum--;
				 lh.setKeysNum(keysNum);			// update linear hashing class.
				 compNum += 4;
		     }
		     else {	// bucket has an overflow so remove a key from there and bring it here
		    	 lh.setComparisonsNum(compNum);
			     this.keys[i] = this.overflowBucket.removeLastKey(lh);
			     compNum = lh.getComparisonsNum();
				 keysNum--;
				 lh.setKeysNum(keysNum);			// update linear hashing class.
				 compNum += 3;
				 if (this.overflowBucket.numKeys() == 0 & ++compNum>0) { // overflow empty free it
					 this.overflowBucket = null;
					 ++compNum;
				 }
		     }
		     lh.setComparisonsNum(compNum);
		     return;
		   }
		}
		if (this.overflowBucket != null & ++compNum>0) {			// look at the overflow for the key to be deleted if one exists
			 this.overflowBucket.deleteKey(key, lh);
			 if (this.overflowBucket.numKeys() == 0 & ++compNum>0) {	// overflow empty free it
				this.overflowBucket = null;
				++compNum;
			 }
	     }
		lh.setComparisonsNum(compNum);
	}

	/**
	 * This method removes this bucket's last key
	 * @param lh
	 * @return
	 */
	public int removeLastKey(LinearHashing lh) {
		
		int compNum = lh.getComparisonsNum();
		int retval;
		
		if (this.overflowBucket == null & ++compNum>0) {
		  if (this.keysNumber != 0 & ++compNum>0){
		    this.keysNumber--;
		    ++compNum;
		    lh.setComparisonsNum(compNum);
		    return this.keys[this.keysNumber];
		  }
		  lh.setComparisonsNum(compNum);
		  return 0;
		}
		else {
		  retval = this.overflowBucket.removeLastKey(lh);
		  ++compNum;
		  if (this.overflowBucket.numKeys() == 0 & ++compNum>0) {	// overflow empty free it
		    this.overflowBucket = null;
		    ++compNum;
		  }
		  lh.setComparisonsNum(compNum);
		  return retval;
		}
	}

	/**
	 * This method searches a key in this bucket
	 * @param key
	 * @param lh
	 * @return true if it succeeds to find it, false if it fails 
	 */
	public boolean searchKey(int key, LinearHashing lh) {

		int i;
		int bucketSize = lh.getBucketSize();
		int compNum = lh.getComparisonsNum() + 1;

		for (i = 0; (i < this.keysNumber & ++compNum>0) && (i < bucketSize & ++compNum>0); i++) {
		   if (this.keys[i] == key & ++compNum>0) {	//key found
			   lh.setComparisonsNum(compNum);
			   return true;
		   }
		}
		if (this.overflowBucket != null & ++compNum>0) {				//look at the overflow for the key if one exists
			lh.setComparisonsNum(compNum);
			return this.overflowBucket.searchKey(key,lh);
	    }
	    else {
			lh.setComparisonsNum(compNum);		      
	    	return false;
	    }
	}

	/**
	 * This method splits the current bucket
	 * @param lh
	 * @param n
	 * @param bucketPos
	 * @param newBucket
	 */
	public void splitBucket(LinearHashing lh, int n, int bucketPos, HashBucket newBucket) {

		boolean tf1 = false, tf2 = false;
		int i;
		int bucketSize = lh.getBucketSize();
		int keysNum = lh.getKeysNum(); 
		int compNum = lh.getComparisonsNum() + 2;
		int defaultCompNum = lh.getComparisonsNum();

		for (i = 0; (i < this.keysNumber & ++compNum>0) && (i < bucketSize & ++compNum>0);) {
		   if ((this.keys[i]%n) != bucketPos & ++compNum>0){	//key goes to new bucket
		     newBucket.insertKey(this.keys[i], lh,false);
		     tf1  = true;
		     this.keysNumber--;
		     keysNum = lh.getKeysNum();
		     keysNum--;
		     lh.setKeysNum(keysNum);		// update linear hashing class.
		     this.keys[i] = this.keys[this.keysNumber];
		     compNum += 6;
		   }
		   else {				// key stays here
		     i++;
		     ++compNum;
		   }
		}
		if(tf1 & ++compNum>0) {compNum += lh.getComparisonsNum() - defaultCompNum;}
		   
		if (this.overflowBucket != null & ++compNum>0) {	// split the overflow too if one exists
			this.overflowBucket.splitBucket(lh, n, bucketPos, newBucket);
			compNum += lh.getComparisonsNum() - defaultCompNum;
		}
		while (this.keysNumber != bucketSize & ++compNum>0) {
		     if (this.overflowBucket == null & ++compNum>0) {
			     lh.setComparisonsNum(compNum);
				 return;
		     }
		     if (this.overflowBucket.numKeys() != 0 & ++compNum>0) {
		    	 this.keys[this.keysNumber] = this.overflowBucket.removeLastKey(lh);
		    	 tf2 = true;
		    	 compNum += 2;
		 		 if (this.overflowBucket.numKeys() == 0 & ++compNum>0) {	// overflow empty free it
		    		 this.overflowBucket = null;
		    		 ++compNum;
		    	 }
		    	 this.keysNumber++;
		    	 ++compNum;
		     }
		     else {				// overflow empty free it
				 this.overflowBucket = null;
				 ++compNum;
		     }
	 	}
		if(tf2 & ++compNum>0) {compNum += lh.getComparisonsNum() - defaultCompNum;}
		lh.setComparisonsNum(compNum);
	}

	/**
	 * This method merges the current bucket
	 * @param lh
	 * @param oldBucket
	 */
	public void mergeBucket(LinearHashing lh, HashBucket oldBucket) {

		boolean tf = false;
		int defaultCompNum = lh.getComparisonsNum();
		int compNum = lh.getComparisonsNum() + 1;
		while (oldBucket.numKeys() != 0 & ++compNum>0) {
			 tf = true;
		     this.insertKey(oldBucket.removeLastKey(lh), lh,true);     
		}
	    if(tf & ++compNum>0) {compNum += lh.getComparisonsNum() - defaultCompNum;}
		lh.setComparisonsNum(compNum);
	}

	/**
	 * This method prints this bucket's keys
	 * @param bucketSize
	 */
    public void printBucket(int bucketSize) {

		int i;

		System.out.println("keysNum is: " + this.keysNumber);
		for (i = 0; (i < this.keysNumber) && (i < bucketSize); i++) {
		   System.out.println("key at: " + i + " is: " + this.keys[i]);
		}
		if (this.overflowBucket != null) {
		  System.out.println("printing overflow---");
		  this.overflowBucket.printBucket(bucketSize);
		}
	}

	
} 