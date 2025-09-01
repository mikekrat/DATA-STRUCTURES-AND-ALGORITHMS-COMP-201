
/**
 * This class is used for taking every word and every word's line from a file  
 * @author Mike
 *
 */

public class Information {
	private String word;
	private int wordLine;
	
	/**
	 * This is an empty constructor for information
	 */
	public Information(){
		
	}
	
	/**
	 * This is the complete constructor for information
	 * @param word is Information's word
	 * @param wordLine is word's (@param word) line
	 */
	public Information(String word, int wordLine) {
		this.word = word;
		this.wordLine = wordLine;
	}

	/**
	 * This method is a getter for word
	 * @return word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * This method is a setter for word
	 * 
	 */
	
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * This method is a getter for word's line
	 * @return word
	 */

	public int getWordLine() {
		return wordLine;
	}

	/**
	 * This method is a setter for word's line
	 * 
	 */

	public void setWordLine(int wordLine) {
		this.wordLine = wordLine;
	}

}
