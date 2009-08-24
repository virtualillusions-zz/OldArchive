package serializationing;

import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

//Allows for total control of what gets written and read making it more sufficient giving more CPU and space
	// Is also a lot faster than Serializable
//The CPU advantage come from the fact that the system will not do much for you anymore
//The Space advantage coems from the fact that only descriptor of current class is written to the stream,
	//and the metadata of the super classes are excluded
//Unlike Serializable; Externalizable classes must have a public constructor without any parameters
       //(Easier to simple create an empty dummy class)
public class TestExternalizationing {


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
			
			oos.writeInt(actors.length);
			for(int i = 0 ; i < actors.length; i++){
				actors[i].writeExternal(oos);
			}			
			
			oos.flush();
			oos.close();// flush, clear, close
			
			actors = null;
			
			System.out.println("----reading");
			FileInputStream fis = new FileInputStream("actors");
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			actors = new Actor[ois.readInt()];
			for(int i = 0 ; i < actors.length; i++){
				actors[i] = new Actor();
				actors[i].readExternal(ois);
			}
			
			printActors();
		}
		
		private void printActors(){
			for(Actor a: actors)
			System.out.println( "Actor Name: "+a.name+"\n Actor ID: "+a.id);
		}
	}
	
	static class Actor implements Externalizable{
		int id;
		String name;
		Point position;
		
		//must be public
		public Actor(){}
		
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeInt(id);
			out.writeUTF(name);
			out.writeObject(position);
		}
		
		public void readExternal(ObjectInput in) throws IOException,
				ClassNotFoundException {
			id = in.readInt();
			name = in.readUTF();
			position = (Point)in.readObject();			
		}
		//It is alsp possible to modify above methods to  deal with Point Object directly
		/*
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeInt(id);
			out.writeUTF(name);
			position.writeExternal(out);
		}
		
		public void readExternal(ObjectInput in) throws IOException,
				ClassNotFoundException {
			id = in.readInt();
			name = in.readUTF();
			position = new Point();
			position.readExternal(in)			
		}
		 */		
	}

	static class Point implements Externalizable{
		float x;
		float y;
		float z;
		
		// must be public
		public Point(){}
		
		Point(float x, float y, float z){
			this.x=x;
			this.y=y;
			this.z=z;
		}
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeFloat(x);
			out.writeFloat(y);
			out.writeFloat(z);
		}
		
		public void readExternal(ObjectInput in) throws IOException,
				ClassNotFoundException {
			x = in.readFloat();
			y = in.readFloat();
			z = in.readFloat();
		}
		
	}
	
}
