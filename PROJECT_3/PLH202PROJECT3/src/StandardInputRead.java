import java.io.*;
import java.text.*;
import java.util.*;

/**
 * It reads input from the standard input hiding from the user the usage 
 * of java.io package classes
 * @version ���� � ����� ��� ������ ��� 2� ������� ��� ���� �������������� ������� ��� ������ ��������� ��������������� (��� 102)
 * @author Giorgos Anestis
 */

public class StandardInputRead {
	/** The error that is return when reading positive integers from stdin*/
	public final static int POS_ERROR = -1;
	
	/** The error that is return when reading negative integers from stdin*/
	public final static int NEG_ERROR = 1;
	
	/** The basic reader*/
	BufferedReader in;
	
	/**
	 * Class constructor
	 */
	public StandardInputRead() {
		super();
		in = new BufferedReader(new InputStreamReader(System.in));		
	}

	/**
	 * It reads a string from standard input and returns it as value. 
	 * In case of an error it returns null
	 * 
	 * @param message The message that is appeared to the user asking for input
	 */
	public String readString(String message) {
		
		System.out.print(message);
		try {
			return in.readLine();
		}
		catch (IOException e) {
			return null;
		}			
	}
	
	/**
	 * It reads an positive integer, zero included, from standard input
	 * and returns it as value. In case of an error it returns -1
	 * 
	 * @param message The message that is appeared to the user asking for input
	 */
	public int readPositiveInt(String message) {
		
		String str;
		int num;
		
		System.out.print(message);			
		try {
			str = in.readLine();
			num = Integer.parseInt(str);
			if (num < 0 ){
				return POS_ERROR;
			}
			else {
				return num;
			}			
		}
		catch (IOException e) {			
			return POS_ERROR;
		}
		catch (NumberFormatException e1) {			
			return POS_ERROR;
		}
	}
	
	/**
	 * It reads an negative integer from standard input and returns it as value. 
	 * In case of an error it returns 1
	 * 
	 * @param message The message that is appeared to the user asking for input
	 */
	public int readNegativeInt(String message) {
		
		String str;
		int num;
		
		System.out.print(message);			
		try {
			str = in.readLine();
			num = Integer.parseInt(str);
			if (num >= 0 ){
				return NEG_ERROR;
			}
			else {
				return num;
			}			
		}
		catch (IOException e) {			
			return NEG_ERROR;
		}
		catch (NumberFormatException e1) {			
			return NEG_ERROR;
		}
	}
	
	/**
	 * It reads an integer from standard input and returns it as value. 
	 * In case of an error it returns 1
	 * 
	 * @param message The message that is appeared to the user asking for input
	 */
	
	public int readInt(String message){
		String str;
		int num;
		
		System.out.print(message);			
		try {
			str = in.readLine();
			num = Integer.parseInt(str);
			return num;		
		}
		catch (IOException e) {			
			return NEG_ERROR;
		}
		catch (NumberFormatException e1) {			
			return NEG_ERROR;
		}
	}

	/**
	 * It reads an positive float, zero included, from standard input
	 * and returns it as value. In case of an error it returns -1.0
	 * 
	 * @param message The message that is appeared to the user asking for input
	 */
	public float readPositiveFloat(String message) {
		
		String str;
		float num;
		
		System.out.print(message);			
		try {
			str = in.readLine();
			num = Float.parseFloat(str);
			if (num < 0 ){
				return POS_ERROR;
			}
			else {
				return num;
			}			
		}
		catch (IOException e) {			
			return POS_ERROR;
		}
		catch (NumberFormatException e1) {			
			return POS_ERROR;
		}
	}
	
	/**
	 * It reads an negative float from standard input and returns it as value. 
	 * In case of an error it returns 1
	 * 
	 * @param message The message that is appeared to the user asking for input
	 */
	public float readNegativeFloat(String message) {
		
		String str;
		float num;
		
		System.out.print(message);			
		try {
			str = in.readLine();
			num = Float.parseFloat(str);
			if (num >= 0 ){
				return NEG_ERROR;
			}
			else {
				return num;
			}			
		}
		catch (IOException e) {			
			return NEG_ERROR;
		}
		catch (NumberFormatException e1) {			
			return NEG_ERROR;
		}
	}
		
	/**
	 * It reads a date in the form dd/mm/yyyy from standard input and 
	 * returns it as value.In case of an error it returns null
	 * 
	 * @param message The message that is appeared to the user asking for input
	 */
	public Date readDate(String message) {
		
		String str;
		
		System.out.print(message);
		
		try {
			str = in.readLine();
			Locale l = new Locale("el", "GR");
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, l);
			return df.parse(str);
		}
		catch (IOException e) {
			return null;
		}
		catch (ParseException e1) {
			return null;
		}
	}
	
	/**
	 * It reads an time in the form h:mm AM or PM from standard input and 
	 * returns it as value.In case of an error it returns null
	 * Example of valid times: 8:30 AM, 2:00 PM, etc
	 * 
	 * @param message The message that is appeared to the user asking for input
	 */
	public Date readTime(String message) {
		
		String str;
		
		System.out.print(message);
		
		try {
			str = in.readLine();		
			DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
			
			return df.parse(str);
		}
		catch (IOException e) {
			return null;
		}
		catch (ParseException e1) {
			return null;
		}
	}
		
}