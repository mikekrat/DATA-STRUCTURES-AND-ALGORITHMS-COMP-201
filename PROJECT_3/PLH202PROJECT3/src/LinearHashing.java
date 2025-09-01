
/**
 * This class is a linear implementation for a hash table
 * @author Mike
 *
 */
class LinearHashing {

	/**
	 * pointer to the hash buckets
	 */
	private HashBucket[] hashBuckets;

	/**
	 * max load factor threshold
	 */
	private float maxThreshold;
	
	/**
	 * min load factor threshold
	 */
	private float minThreshold;

	/**
	 * max number of keys in each bucket
	 */
	private int bucketSize;	
	
	/**
	 * number of keys currently stored in the LinearHashing
	 */
	private int keysNum;
	
	/**
	 * total space the hash table has for keys
	 */
	private int keySpace;
	
	/**
	 * pointer to the next bucket to be split
	 */
	private int nextSplit;
	
	/**
	 * current number of buckets
	 */
	private int bucketsNum;
	
	/**
	 * the n used for the hash function
	 */
	private int n;
	
	/**
	 * minimum number of buckets this LinearHashing can have
	 */
	private int minBuckets;
	
	/**
	 * This is the counter of comparisons done in a method
	 */
	private int comparisonsNum;

	/**
	 * This method is a Constructor for this class
	 * @param itsBucketSize
	 * @param initPages
	 * @param maxTH
	 */
	public LinearHashing(int itsBucketSize, int initPages, float maxTH) { 	

		int i;
		comparisonsNum = 0;
		bucketSize = itsBucketSize;
		keysNum = 0;
		nextSplit = 0;
		bucketsNum = initPages;
		n = initPages;
		minBuckets = initPages;
		keySpace = bucketsNum*bucketSize;
		maxThreshold = maxTH;
		minThreshold = (float)0.5;

		if ((bucketSize == 0) || (bucketsNum == 0) & ++comparisonsNum>0) {
		  System.out.println("error: space for the table cannot be 0");
		  System.exit(1);
		}
		hashBuckets = new HashBucket[bucketsNum];
		for (i = 0; i < bucketsNum; i++) {
		   hashBuckets[i] = new HashBucket(bucketSize);
		}
	}
	
	/**
	 * This method returns a hash based on the key
	 * @param key
	 * @return a hash based on the key
	 */
	private int hashFunction(int key){
		
		this.setComparisonsNum(0);
		int retval;

		retval = key%this.n;
		++comparisonsNum;
		if (retval < 0 & ++comparisonsNum>0)
			retval *= -1;
			++comparisonsNum;
		if (retval >= nextSplit & ++comparisonsNum>0){
		  //System.out.println( "Retval = " + retval);
		  return retval;
		}
		else {
			 retval = key%(2*this.n);
			 ++comparisonsNum;
			 if (retval < 0 & ++comparisonsNum>0)
				 retval *= -1;
			 	 ++comparisonsNum;
			 //System.out.println( "Retval = " + retval);
		         return retval;
		}
	}

	/**
	 * This method returns the current load factor of the hash table.
	 * @return the current load factor of the hash table.
	 */
	private float loadFactor() {

		return ((float)this.keysNum)/((float)this.keySpace);
	}

	/**
	 * This method splits the bucket pointed by nextSplit.
	 */
	private void bucketSplit() {

		int i;
		HashBucket[] newHashBuckets;

		newHashBuckets= new HashBucket[bucketsNum+1];
		comparisonsNum += 2;
		for (i = 0; (i < this.bucketsNum & ++comparisonsNum>0); i++){
		   newHashBuckets[i] = this.hashBuckets[i];
		   ++comparisonsNum;
		}

		hashBuckets = newHashBuckets;
		hashBuckets[this.bucketsNum] = new HashBucket(this.bucketSize);
		this.keySpace += this.bucketSize;
		this.hashBuckets[this.nextSplit].splitBucket(this, 2*this.n, this.nextSplit, hashBuckets[this.bucketsNum]);
		this.bucketsNum++;
		comparisonsNum += 4;
		if (this.bucketsNum == 2*this.n & ++comparisonsNum>0) {
		  this.n = 2*this.n;
		  this.nextSplit = 0;
		  comparisonsNum += 2;
		}
		else {
		    this.nextSplit++;
		    ++comparisonsNum;
		}
	}

	/**
	 * This method merges the last bucket that was split
	 */
	private void bucketMerge() { 	
		
		int i;
		HashBucket[] newHashBuckets;
		newHashBuckets= new HashBucket[bucketsNum-1];
		++comparisonsNum;
		for (i = 0; (i < this.bucketsNum-1 & ++comparisonsNum>0); i++) {
		   newHashBuckets[i] = this.hashBuckets[i];
		   ++comparisonsNum;
		}
		if (this.nextSplit == 0 & ++comparisonsNum>0) {
		  this.n = (this.bucketsNum)/2;
		  this.nextSplit = this.n-1;
		  comparisonsNum += 2;
		}
		else {
		  this.nextSplit--;
		  ++comparisonsNum;
		}
		this.bucketsNum--;
		this.keySpace -= this.bucketSize;
		this.hashBuckets[this.nextSplit].mergeBucket(this, hashBuckets[this.bucketsNum]);
		hashBuckets = newHashBuckets;
		comparisonsNum += 3;
	}
	// getters setters 
	public int getBucketSize() {return bucketSize;}
	public int getKeysNum() {return keysNum;}
	public int getKeySpace() {return keySpace;}
	public int getComparisonsNum() {return comparisonsNum;}
	public float getMaxThreshold() {return maxThreshold;}
	public void setMaxThreshold(float maxThreshold) {this.maxThreshold = maxThreshold;}
	public void setBucketSize(int size) {bucketSize = size;}
	public void setKeysNum(int num) {keysNum = num;}
	public void setKeySpace(int space) {keySpace = space;}
	public void setComparisonsNum(int comparisonsNum) {this.comparisonsNum = comparisonsNum;}
	
	/**
	 * This method inserts a new key.
	 * @param key
	 */
	public void insertKey(int key) {

		//System.out.println( "hashBuckets[" + this.hashFunction(key) + "] =  " + key);
		this.hashBuckets[this.hashFunction(key)].insertKey(key, this,false);
		if (this.loadFactor() > maxThreshold & ++comparisonsNum>0){
		  //System.out.println("loadFactor = " + this.loadFactor() );
		  this.bucketSplit();
		  //System.out.println("BucketSplit++++++");
		}
	}

	/**
	 * This method deletes a key.
	 * @param key
	 */
	public void deleteKey(int key) {

		this.hashBuckets[this.hashFunction(key)].deleteKey(key, this);
		if (this.loadFactor() > maxThreshold & ++comparisonsNum>0){
		  this.bucketSplit();
		}
		else if ((this.loadFactor() < minThreshold & ++comparisonsNum>0) && 
				(this.bucketsNum > this.minBuckets & ++comparisonsNum>0)){
			 this.bucketMerge();
		}
	}

	/**
	 * This method searches for a key.
	 * @param key
	 * @return
	 */
	public boolean searchKey(int key) {
		return this.hashBuckets[this.hashFunction(key)].searchKey(key, this);
	}

	/**
	 * This method prints the LinearHashing
	 */
	public void printHash() {
		for (int i = 0; i < this.bucketsNum; i++) {
		   System.out.println("Bucket[" + i + "]");
		   this.hashBuckets[i].printBucket(this.bucketSize);
		}
	}


} // LinearHashing class
