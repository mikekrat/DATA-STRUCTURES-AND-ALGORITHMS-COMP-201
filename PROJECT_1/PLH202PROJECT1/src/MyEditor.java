
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class toggles the main method
 * @author Mike Kratimenos
 */

public class MyEditor {
	
	/**it is used to store the number of random access file's pages */
	static int pagesNum = 0;
	
	/**it is used to count how many information are on random access file's last page */
	static int flag = 0;
	
	/**it is the max number of bytes that can be filled on every random access file's page */
	static final int bufferSize=128;
	
	/**it is the max number of bytes that can be filled on every random access file's word */
	static final int maxWordSize=20;
	
	/**it is the max number of bytes that can be filled on every random access file's number */
	static final int intSize = 7;
	
	/**it is the number of the last bytes on every random access file's page */
	static final int endOfPage = 20;
	
	/**it is the number of information allowed on every random access file's page */
	static final int result = (int)(bufferSize/(maxWordSize+intSize));
	
	/**it is every random access file's ending  */
	static final String end = ".ndx";
	
	/**it is the max number of characters allowed on every text file's line*/
	static final int maxLineLength = 80;
	
	/**This method is an empty constructor for MyEditor class */
	public MyEditor(){
		
	}
	public static void main(String[] args) {
		String filename = "";
		if(args.length>0){
			filename = args[0];
		}
		System.out.println("filename is: " + filename);
		menu(filename);
	}
	
	/**
	 * This method creates an instance of double linked list and passes data from a text file to that list and
	 * toggles the menu which was asked to be implemented and every option in that menu is selected by the user. 
	 * If the user selects:
	 * 	^ : Go to double linked list's first line,
	 *	$ : Go to double linked list's last line,
	 *	- : Go up one line in the double linked list,
	 *	+ : Go down one line in the double linked list,
	 *	a : Add new line after double linked list's current line (the user is asked to type in the text for the new line),
	 *	t : Add new line before double linked list's current line (the user is asked to type in the text for the new line),
	 *	d : Delete double linked list's current line,
	 *	l : Print all double linked list's lines,
	 *	n : Toggle whether line numbers are displayed when printing double linked list's all lines (l command),
	 *	p : Print double linked list's current line,
	 *	q : Exit without saving double linked list to the text file,
	 *	w : save double linked list to the text file,
	 *	x : Exit with saving double linked list to the text file,
	 *	= : Print double linked list's current line number,
	 *	# : Print double linked list's number of lines and characters,
	 *	c : Create a random access file(RAF) with the other text file's words and words' lines in the file sorted alphabetically 
	 *  and print RAF's number of pages, 
	 *	v : Print RAF's data (word, word's line number),
	 *	s : Search a word given by the user serially from the RAF 
	 * and print in which lines was it found (if found) and final disk accesses,
	 *	b : Search a word given by the user binary from the RAF
	 * and print in which lines was it found (if found) and final disk accesses
	 * @version http://tutorials.jenkov.com/java-io/randomaccessfile.html
	 * @param filename is text file's name 
	 * @throws FileNotFoundException 
	 * @throws IOException
	 */
	public static void menu(String filename){
		
		
		
		DoubleLinkedList<String> dll = new DoubleLinkedList<String>();
		passTxtFileToDLL(filename,dll);
		StandardInputRead reader = new StandardInputRead();	
		
		DoubleLinkedList<String>.Node temp = dll.getHead();
		ArrayList<Information> al = new ArrayList<>();
		String FileName = null;
		
		printMenu();
		boolean tf=true;
		String s = reader.readString("CMD> ");
		while(!s.equals(null)){
			
			switch(s){
				case "^":
					temp = dll.getHead();
					System.out.println("OK\n");
					break;
				case "$":
					temp = dll.getTail();
					System.out.println("OK\n");
					break;
				case "-":
					if(temp.prev!=null){
						temp = temp.prev;
						System.out.println("OK\n");
					}else
						System.out.println("You are in the first line already...\n");
					break;
				case "+":
					if(temp.next!=null){
						temp = temp.next;
						System.out.println("OK\n");
					}else
						System.out.println("You are in the last line already...\n");
					
					break;
				case "a":
					dll.addAfterCurrentNode(temp, reader.readString("Type text for new line: "));
					break;
				case "t":
					dll.addBeforeCurrentNode(temp, reader.readString("Type text for new line: "));
					break;
				case "d":
					dll.deleteNode(temp);
					if(temp.next!=null){
						temp = temp.next;
					}else if(temp.next==null&& temp.prev!=null){
						temp = temp.prev;					
					}
					else if(temp.next==null&& temp.prev==null){
						temp = null;					
					}
					break;
				case "l":
					dll.print(tf);
					break;
				case "n":
					if(tf){
						tf=false;
						System.out.println("tf set false");
					}
					else{
						tf=true;
						System.out.println("tf set true");
					}						
					break;
				case "p":
					if(temp!=null){
						System.out.println("Current line is:\n"+ temp.element);
					}else{
						System.out.println("The list is empty...");
					}
					
					break;
				case "q":
					System.out.println("process terminated without save");
					return;
				case "w":
					saveListToTxtFile(filename, dll.getHead());
					break;				
				case "x":
					saveListToTxtFile(filename, dll.getHead());
					System.out.println("process terminated with save");
					return;
				case "=":
					int flag = 1;
					DoubleLinkedList<String>.Node tempHead = dll.getHead();
					while(tempHead!=temp){
						tempHead = tempHead.next;
						flag++;
					}
					System.out.println("Current line's number is: "+flag);
					break;
				case "#":
					calcLinesAndChars(dll);					
					break;
				case "c":
					dll.passToArrayList(al);					
					sortArrayList(al);	
					FileName = reader.readString("Give me RAFile's name: ");
					RandomAccessFile file;
					try {
						file = new RandomAccessFile( FileName + end, "rw");
						pagesNum = createRAFile(al, file);
						al = new ArrayList<>();
						file.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "v":
					RandomAccessFile File;
					try {
						File = new RandomAccessFile( FileName + end, "rw");
						printRAFile(File, 0);
						File.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "s":
					RandomAccessFile FILE;
					try {
						FILE = new RandomAccessFile( FileName + end, "rw");
						searchWordSerial(FILE,reader.readString("Type word for search: "));												
						FILE.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "b":
					RandomAccessFile file1;					
					try {
						file1 = new RandomAccessFile( FileName + end, "rw");
						searchWordBin(file1,reader.readString("Type word for search: "),pagesNum);					
						file1.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}							
					break;	
				default:
					System.out.println("Bad command");
			}
			s = reader.readString("CMD> ");
		}
	}
	/**
	 * This method searches binary a string (parameter test) in a
	 * random access file (parameter File) and prints in which lines 
	 * was it found (if it was found) or it prints that it was not found (if it was not found)
	 * @version https://www.youtube.com/watch?v=1ruyrpTKGZk
	 * @param File
	 * @param test
	 * @param pagesNum which is random access file's pages number
	 * @throws IOException
	 */
	
	public static void searchWordBin(RandomAccessFile File, String test, int pagesNum){
		int finalDiskAccesses = 0;
		ArrayList<String> listOfLines = new ArrayList<>();
		int counter = 0;
		int pages=1;
		int pos=0;
		int last=0;
		int first = 0;
		boolean foundAll=false;
		last = pagesNum*bufferSize;
		byte[] bytesToRead;
		String page;
		int middle1,middle2;
		int middle  = (int)(first + last) / 2;
		while(first < last){			
			try {
				while(middle%bufferSize!=0){
					middle--;
				}
				File.seek(middle);			
				bytesToRead = new byte[bufferSize];
				File.read(bytesToRead);		
				page = new String(bytesToRead);
				for(int i=0; i<result; i++){
					String word = page.substring(pos, pos+maxWordSize-1);
					word = removeSpaces(word);
					String sNum = page.substring(pos+maxWordSize,pos+maxWordSize+intSize);
					sNum = removeSpaces(sNum);				
					if(word.compareTo(test)<0){
						first = middle + bufferSize;
					}else if(word.compareTo(test)>0){
						last = middle;
					}else{
						listOfLines.add(sNum);
						finalDiskAccesses=pages;
						counter++;
						if(counter==result){
							middle1=middle+bufferSize;
							while(counter==result && middle1<File.length()){
								pos=0;
								counter=0;
								File.seek(middle1);			
								bytesToRead = new byte[bufferSize];
								File.read(bytesToRead);		
								page = new String(bytesToRead);
								for(int j=0; j<result; j++){
									String word1 = page.substring(pos, pos+maxWordSize-1);
									word1 = removeSpaces(word1);
									String sNum1 = page.substring(pos+maxWordSize,pos+maxWordSize+intSize);
									sNum1 = removeSpaces(sNum1);
									if(word1.compareTo(test)==0){
										listOfLines.add(sNum1);
										finalDiskAccesses=pages;
										counter++;
									}
									pos+=intSize + maxWordSize;	
								}
								pages++;
								middle1+=bufferSize;
							}
							middle2=middle-bufferSize;
							counter=result;
							while(counter==result && middle2>0){
								pos=0;
								counter=0;
								File.seek(middle2);			
								bytesToRead = new byte[bufferSize];
								File.read(bytesToRead);		
								page = new String(bytesToRead);
								for(int j=0; j<result; j++){
									String word2 = page.substring(pos, pos+maxWordSize-1);
									word2 = removeSpaces(word2);
									String sNum2 = page.substring(pos+maxWordSize,pos+maxWordSize+intSize);
									sNum2 = removeSpaces(sNum2);
									if(word2.compareTo(test)==0){
										listOfLines.add(sNum2);
										finalDiskAccesses=pages;
										counter++;
									}
									pos+=intSize + maxWordSize;	
								}
								pages++;
								middle2-=bufferSize;
							} 
							foundAll=true;
						}
					}					
					pos+=intSize + maxWordSize;	
				}				
				if(foundAll){
					break;
				}
				pos=0;
				counter=0;
				pages++;
				middle  = (int)(first + last) / 2;
				
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!listOfLines.isEmpty()){
			System.out.print("<"+test+">"+" is on lines: ");
			for(int i=0; i<listOfLines.size(); i++){
				System.out.print(listOfLines.get(i));
				if(i<listOfLines.size()-1){
					System.out.print(",");
				}
				
			}
			System.out.println("\ndisk accesses: "+finalDiskAccesses);
		}else{
			System.out.println("\n<"+test+"> was not found...");
			System.out.println("disk accesses: "+ pages);
		}
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * This method searches serially a string (parameter test) in a
	 * random access file (parameter File) and prints in which lines 
	 * was it found (if it was found) or it prints that it was not found (if it was not found)
	 * @param File
	 * @param test
	 * @throws IOException
	 */
	
	public static void searchWordSerial(RandomAccessFile File, String test){
		int finalDiskAccesses = 0;
		ArrayList<String> listOfLines = new ArrayList<>();
		
		boolean b = false;
		int pos=0;
		int pages=0;
		int counter1=0;
		try {
			File.seek(pos);
			while(true){		
				if(counter1 >=File.length()){
					break;
				}
				
				byte[] bytesToRead = new byte[bufferSize];
				File.read(bytesToRead);		
				String page = new String(bytesToRead);
				pages++;
				for(int i=0; i<result; i++){
					String word = page.substring(pos, pos+maxWordSize-1);
					word = removeSpaces(word);
					String sNum = page.substring(pos+maxWordSize,pos+maxWordSize+intSize);
					sNum = removeSpaces(sNum);				
					if(word.equals(test)){
						listOfLines.add(sNum);	
						finalDiskAccesses=pages;
					}else if(word.compareTo(test)>0){
						b=true;
						break;
					}
					pos += maxWordSize + intSize;
				}
				if(b){break;}			
				counter1+=pos+endOfPage;
				pos=0;
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!listOfLines.isEmpty()){
			System.out.print("<"+test+">"+" is on lines: ");
			for(int i=0; i<listOfLines.size(); i++){
				System.out.print(listOfLines.get(i));
				if(i<listOfLines.size()-1){
					System.out.print(",");
				}
				
			}
			System.out.println("\ndisk accesses: "+finalDiskAccesses);
		}else{
			System.out.println("\n<"+test+"> was not found...");
			System.out.println("disk accesses: "+ pages);
		}
	}
	/**
	 * This method prints random access file's (parameter File) elements
	 * @param File
	 * @param pos is the position from where the file is being read 
	 */
	public static void printRAFile(RandomAccessFile File, int pos){
		
			int counter1=0;
			int counter = 0;
			try {
				File.seek(pos);
				while(true){		
					if(counter1 >=File.length()){
						break;
					}
					
					byte[] bytesToRead = new byte[bufferSize];
					File.read(bytesToRead);		
					String page = new String(bytesToRead);
					for(int i=0; i<result; i++){
						String word = page.substring(pos, pos+maxWordSize-1);
						word = removeSpaces(word);
						String sNum = page.substring(pos+maxWordSize,pos+maxWordSize+intSize);
						sNum = removeSpaces(sNum);	
						if(counter<File.length()-(result-flag+1)*(maxWordSize+intSize)){
							System.out.println("word: "+word+" line: "+sNum);
						}
						pos += maxWordSize + intSize;
						counter += maxWordSize + intSize;
					}
					counter1+=pos+endOfPage;
					counter += endOfPage;
					pos=0;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	} 
	/**
	 * This method removes spaces from given string (parameter word)
	 * @param word
	 * @return the string without spaces
	 */
	public static String removeSpaces(String word){
		for(int i=0; i<word.length();){
			char ch = word.charAt(i);
			if(ch == ' '){
				String before = word.substring(0, i);
				String after = word.substring(i+1);
				word = before + after;
			}else if(ch ==0){
				String before = word.substring(0, i);
				String after = word.substring(i+1);
				word = before + after;
			}
			else{
				i++;
			}
		}
		return word;
	}
	/**
	 * This method passes a text file's lines to a double linked list (parameter dll)
	 * @param name is text file's name
	 * @param dll
	 * @throws IOException
	 * @version https://stackoverflow.com/questions/5343689/java-reading-a-file-into-an-arraylist 6th answer
	 */
	public static void passTxtFileToDLL(String name, DoubleLinkedList<String> dll){
	    try (BufferedReader br = new BufferedReader(new FileReader(name)))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                dll.addLast(sCurrentLine);
            }
            br.close();
           
        } catch (IOException e) {
            e.printStackTrace();
        } 
	    
	    DoubleLinkedList<String>.Node temp = dll.getHead();
	    
	    while(temp!=null){
	    	if(temp.element.codePointCount(0, temp.element.length())>maxLineLength){
	    		temp.element = temp.element.substring(0, maxLineLength);
	    	}
	    	temp = temp.next;
	    }
	    
	}
	/**
	 * This method passes a double linked list's elements to a text file
	 * @param filename is text file's name
	 * @param tmpHead is double linked list's head
	 * @throws IOException
	 * @version http://www.java2s.com/Tutorials/Java/IO/Text_File/Save_to_a_text_File_with_PrintStream_in_Java.htm
	 */
	public static void saveListToTxtFile(String filename, DoubleLinkedList<String>.Node tmpHead){
		try (PrintStream out = new PrintStream(filename)){
			if(tmpHead.next==null && tmpHead!=null){
			    out.println(tmpHead.element);			    
			}else if(tmpHead==null){
				System.out.println("The list is empty...");
			}
			while(tmpHead!=null){
			    out.println(tmpHead.element);
			    tmpHead = tmpHead.next;
			}
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * This method passes the array list (parameter al) to a random access file (file)
	 * @param al
	 * @param file 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return the number of pages in the random access file or 0 if the file was not found
	 * @version https://www.geeksforgeeks.org/bytebuffer-clear-methods-in-java-with-examples/ ,
	 * https://stackoverflow.com/questions/44455987/resetting-byte-buffer-to-zeros 2nd answer	
	 */
	
	public static int createRAFile(ArrayList<Information> al,RandomAccessFile file){
				
			try {
				java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(bufferSize);
				int counter = 0;
				int counter1 = 0;
				int counter2 = 0;
				int pos=0;
				boolean lastLine = false;
				
				for(int j=result; j<al.size(); j+=result){
					for(int i=0; i<result; i++){
						if((counter2+result)<=al.size()){
							bb.put(al.get(i+counter2).getWord().getBytes(java.nio.charset.StandardCharsets.US_ASCII));
							counter=al.get(i+counter2).getWord().codePointCount(0, al.get(i+counter2).getWord().length());
							while(counter<maxWordSize){
								bb.put(" ".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
								counter++;
							}
							String sWordLine = String.valueOf(al.get(i+counter2).getWordLine());
							int c = sWordLine.codePointCount(0, sWordLine.length());
							while(c<intSize){
								bb.put(" ".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
								c++;
							}
							bb.put(sWordLine.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
						}else{
							break;
						}
					}
					for(int i =1; i<endOfPage; i++){
						bb.put(" ".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
					}
					bb.put("\n".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
					file.write(bb.array());
					Arrays.fill(bb.array(), (byte)0);
					bb.clear();
					counter2+=result;	
					counter1++;
				}
				Arrays.fill(bb.array(), (byte)0);
				bb.clear();
				while(counter2<al.size()){					
					bb.put(al.get(counter2).getWord().getBytes(java.nio.charset.StandardCharsets.US_ASCII));
					counter=al.get(counter2).getWord().codePointCount(0, al.get(counter2).getWord().length());
					while(counter<maxWordSize){
						bb.put(" ".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
						counter++;
					}
					String sWordLine = String.valueOf(al.get(counter2).getWordLine());
					int c = sWordLine.codePointCount(0, sWordLine.length());
					while(c<intSize){
						bb.put(" ".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
						c++;
					}
					bb.put(sWordLine.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
					counter2++;
					flag++;
					pos+= intSize+maxWordSize;
					lastLine=true;
				}
				while(pos<bufferSize && lastLine){
					bb.put(" ".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
					pos++;
				}
				file.seek((counter1*bufferSize));
				file.write(bb.array());
				
				Arrays.fill(bb.array(), (byte)0);
				bb.clear();			
				if(counter2%counter1!=0){
					counter1++;
				}
				System.out.println("OK. Data pages of size "+bufferSize+" bytes: "+counter1);
				return counter1;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 0;
			
	}
	/**
	 * This method is used to sort alphabetically an array list of information (parameter al)
	 * @param al
	 * @version https://www.youtube.com/watch?v=wzWFQTLn8hI
	 */
	
	public static void sortArrayList(ArrayList<Information> al){
		Collections.sort(al, new Comparator<Information>(){
			
			public int compare(Information i1,Information i2)
			{	
				return String.valueOf(i1.getWord()).compareTo(i2.getWord());		
			}
			
		});
		String str = "";
		
		for(int i=0; i<al.size(); i++){
			if(al.get(i).getWord().codePointCount(0, al.get(i).getWord().length())>maxWordSize){
				for(int j=0; j<20; j++){
					str+=al.get(i).getWord().charAt(j);
				}
				al.get(i).setWord(str);
				str = "";
			}
			
		}
	}
	/**
	 * This method calculates the number of lines and characters
	 * of a file which it was passed in a double linked list (parameter dll) 
	 * @param dll
	 * @return the number of lines (linesNum) or 0 if the list is empty
	 */
	public static int calcLinesAndChars(DoubleLinkedList<String> dll){
		int linesNum = 0;
		
		if(dll.getSize()==0){
			System.out.println("the list is empty");
			return 0;
		}else if(dll.getSize()==1){
			linesNum++;
		}
		DoubleLinkedList<String>.Node tempHead1 = dll.getHead();
		
		while(tempHead1!=null){
			tempHead1 = tempHead1.next;
			linesNum++;
		}
		
		int length = 0;
		tempHead1 = dll.getHead();
		if(tempHead1.next==null && tempHead1!=null){
			for(int i=0; i<tempHead1.element.length(); i++){
				if(tempHead1.element.charAt(i) != ' ') {   
		                length++;  
				}
			}
		}else if(tempHead1==null){
			System.out.println("the list is empty...");
			return 0;
		}
			
		while(tempHead1!=null){
			for(int i=0; i<tempHead1.element.length(); i++){
				if(tempHead1.element.charAt(i) != ' ') {   
		                length++;  
				}
			}
			tempHead1 = tempHead1.next;
		}
		System.out.println(linesNum+" lines, "+length+" characters");
		return linesNum;
	}
	
	/**
	 * This method prints the menu
	 */
	public static void printMenu(){
		System.out.println("=============================================================================================");
		System.out.println("^ : Go to double linked list's first line");
		System.out.println("$ : Go to double linked list's last line");
		System.out.println("- : Go up one line in the double linked list");
		System.out.println("+ : Go down one line in the double linked list");
		System.out.println("a : Add new line after double linked list's current line (the user is asked to type in the text for the new line)");
		System.out.println("t : Add new line before double linked list's current line (the user is asked to type in the text for the new line)");
		System.out.println("d : Delete double linked list's current line");
		System.out.println("l : Print all double linked list's lines");
		System.out.println("n : Toggle whether line numbers are displayed when printing double linked list's all lines (l command)");
		System.out.println("p : Print double linked list's current line");
		System.out.println("q : Exit without saving double linked list to the text file");
		System.out.println("w : save double linked list to the text file");
		System.out.println("x : Exit with saving double linked list to the text file");
		System.out.println("= : Print double linked list's current line number");
		System.out.println("# : Print double linked list's number of lines and characters");
		System.out.println("c : Create a random access file(RAF) with the other text file's words and words' lines in the file sorted alphabetically and print RAF's number of pages,"); 
		System.out.println("v : Print RAF's data (word, word's line number)");
		System.out.println("s : Search a word given by the user serially from the RAF and print in which lines was it found (if found) and final disk accesses");
		System.out.println("b : Search a word given by the user binary from the RAF and print in which lines was it found (if found) and final disk accesses");
		System.out.println("=============================================================================================");
	}
	
}
