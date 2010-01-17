package Example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class findInWord {
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String bool ;
		System.out.println("Enter a word to determine if it contains  'ale': ");
		String word = " "+stdin.readLine()+" "; stdin.close();
		bool = word.contains("ale")?  "does" :  "does't";
		System.out.println("Your entered word"+word+bool+" have 'ale' in it");
		
		
	}
}
