package messingWithEnums;

public class MessingAround {
		
	enum Direction {LEFT, RIGHT, UP, DOWN};
	
	public static void main(String[] args){
		int Count = 0; 
		for (Direction action : Direction.values()){//In for action is a new variable created
			Count++;   
			Count+=Math.random()*50+1;
			System.out.println(action.ordinal()+1);
			System.out.println("Count "+Count);  
			System.out.println(action); 
	         }
		
		
		
		double[] ar = {1.2, 3.0, 0.8};
		int sum = 0;
		for (double d : ar) {  // d gets successively each value in ar.
		    sum += d;
		    System.out.println(d);
		}
	}
	
}
