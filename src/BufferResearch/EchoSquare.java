package BufferResearch;
 
import java.io.*;

/**
 * Reads a number, square it, then print the results on the screen
 * @author Kyle Williams
 */
public class EchoSquare {
	public static void main(String[] args) throws IOException{
		InputStreamReader inStream = new InputStreamReader(System.in);
		BufferedReader stdin = new BufferedReader(inStream);
		
		String inData;
		int num, square;//declare two int variables
		
		System.out.println("Enter and integer: ");
		inData = stdin.readLine();
		
		num = Integer.parseInt(inData);//convert inData to int
		square = num*num;//compute the square
		
		System.out.println("The Square of "+inData+" is "+square);
	}
}
