package BufferResearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class switchResearch {

	public static String CASE(String DATA){
		int choose = 0;
		String comment=null;
		String forcomment= DATA;
		if (forcomment.equalsIgnoreCase("Hello"))   // The generated insult.
		{choose=0;} 
		else if(forcomment.equalsIgnoreCase("GoodBye"))   // The generated insult. 
		{choose=1;}
		else if(forcomment.equalsIgnoreCase("How Are You"))   // The generated insult.
		{choose=2;}
		else if(forcomment.equalsIgnoreCase("help"))   // The generated insult.
		{choose=3;}
		else choose=4;
		
		
		switch (choose) {
		    case 0:  comment = "Hi, How Are You";
		             break;
		    case 1:  comment = "Have a nice day";
		             break;
		    case 2:  comment = "I'm okay, a little bored i feel like i'm trapped";
		             break;
		    case 3:  comment = "You're quite incompetent, help yourself";
            	break;
		    default: comment = "Leave me alone";
		}
		return comment;
	}
	
	public static void main(String[] args) throws IOException{
		InputStreamReader inStream = new InputStreamReader(System.in);
		BufferedReader stdin = new BufferedReader(inStream);		
		String inData;
		int count=0;
		System.out.println("Enter and command: ");	
		do{
			if (count==5){System.out.println("Type exit to end program");count=0;}
			else{System.out.println("Please say either hello, goodbye, help or how are you: ");}
			inData = stdin.readLine();			
		
			System.out.println(CASE(inData));
			System.out.println("");
			count++;
			
			}while(!inData.equalsIgnoreCase("exit"));
		System.exit(0);
	}
}
