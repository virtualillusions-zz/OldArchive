package com.tps1.character;


public class Charactertype extends playerGameState { 	  	  
    // this string finds the model and loads it
    private String strType=null;
    //the one and only instance of the playerGameState
    private static Charactertype instance = null;


    public static Charactertype get(String theName){
    	 if (instance == null) {
	        	instance = new Charactertype(theName);
	        }
	        return instance;
    }
    
    /**
     * 
     * @param type is it Human or Computer
     * @return instance of this field
     */
    public static Charactertype create(String type){
   	 if (instance == null) {
	        	instance = new Charactertype(type);
	        }
	        return instance;
   }
    // the constructor is private, it can only be called inside this class
    public Charactertype(String theName){
    	  super(theName);
    	  strType = theName;
        //initializes movement controller
    } 
   
    // return the character name type
    public String getCharactertype() {
      return strType;
    }
}