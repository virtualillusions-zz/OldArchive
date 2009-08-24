package Virtualproject2D;

import java.util.*;
public class ActorManager {
	LinkedList actorList;
	public ActorManager()
	{
		//Make a linked lsit to hold all the actors.
		actorList = new LinkedList();
	}
	public void createEntity(String s, int number)
	{
		for(int i=0;i<number;i++)
		{
			actorList.add(entityFactory(s));
		}
	}
	Object entityFactory(String name)
	{
		Class temp;
		Object targetClass = null;
		try
		{
			//Find class in question
			temp = Class.forName(name);
			//Make an instance of the desired class
			targetClass = temp.newInstance();
		}
		catch(Exception e)
		{
			System.out.println("Cannot create instance of "+name);
		}
		System.out.println("Created the "+name+"!");
		return targetClass;
	}
	public void clearEntities()
	{
		for(int i=0; i<actorList.size();i++)
		{
			actorList.remove(i);
		}
		System.out.println("The entity list has been cleared");
	}

}
