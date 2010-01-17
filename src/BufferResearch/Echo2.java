package BufferResearch;

import java.io.*;
/**
 * The Following program reads characters from the keyboard into the 
 * String called inData. Then the characters stored in that String are 
 * sent to the monitor
 * @author Kyle Williams
 *
 */
public class Echo2 {
	public static void main(String[] args)throws IOException{
		InputStreamReader inStream = new InputStreamReader(System.in);
		BufferedReader stdin= new BufferedReader(inStream);
		
		String inData;
		do{
		System.out.println("Enter the data: ");
		inData=stdin.readLine();
		
		System.out.println("You entered: " + inData);
		}
		while(!inData.equals("exit"));
	}
}
