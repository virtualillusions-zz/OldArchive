package serializationing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;

public class TestSerializing {


	public static void main(String args[]){
		Sample sample = new Sample();
		try{
			sample.test();
		}catch(Exception e){
				e.printStackTrace();
				}
		}
	
	static class Sample{
		Actor[] actors;
		
		public void initialize(){
			actors = new Actor[3];
			
			//Initialize actors
			for(int i=0; i<actors.length; i++){
				actors[i] = new Actor();
				actors[i].name = "Actor "+i;
				actors[i].id = i;
				float x = (float) Math.random(), y =(float) Math.random(), z =(float) Math.random();
				actors[i].position = new Point(x,y,z);
			}			
		}
		
		public void test() throws Exception{
			initialize();
			printActors();
			
			System.out.println("----writing");
			FileOutputStream fos = new FileOutputStream("actors");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(actors);
			oos.close();// flush, clear, close
			
			actors = null;
			
			System.out.println("----reading");
			FileInputStream fis = new FileInputStream("actors");
			ObjectInputStream ois = new ObjectInputStream(fis);
			actors = ((Actor[])ois.readObject());

			printActors();
		}
		
		private void printActors(){
			for(Actor a: actors)
			System.out.println( "Actor Name: "+a.name+"\n Actor ID: "+a.id);
		}
	}
	
	static class Actor implements Serializable{
		int id;
		String name;
		Point position;
		
		//Alternative approach can be used instead of sending everything out only send selected
		//Creating instances of ObjectStreamField and storing them in the private static member named 
		//serialPersistentFields is safer than assuming nontransient members should be serialized. This 
		//is in part because when a member is added to the class, it is not automatically considered
		//serializable. It also makes the job of the writeObject metod easier because it can forward 
		//these instances to the class descriptor of the Actor class
		private static final ObjectStreamField[] serialPersistentFields = {
			new ObjectStreamField("id",int.class),
			new ObjectStreamField("name",String.class),
			new ObjectStreamField("position",Point.class)
		};
	}

	static class Point implements Serializable{
		float x;
		float y;
		float z;
		
		Point(float x, float y, float z){
			this.x=x;
			this.y=y;
			this.z=z;
		}
	}
	
}
