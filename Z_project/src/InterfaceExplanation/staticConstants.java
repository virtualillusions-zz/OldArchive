package InterfaceExplanation;
	/**
	 * An interface in the Java programming language is an abstract type that is used to specify an 
	 * interface (in the generic sense of the term) that classes must implement. Interfaces are declared 
	 * using the interface keyword, and may only contain method signatures and constant declarations 
	 * (variable declarations which are declared to be both static and final). An interface may never 
	 * contain method definitions. As interfaces are implicitly abstract, they cannot be directly 
	 * instantiated. Object references in Java may be specified to be of an interface type; in which 
	 * case they must either be null, or be bound to an object which implements the interface. The 
	 * keyword implements is used to declare that a given class implements an interface. A class which 
	 * implements an interface must either implement all methods in the interface, or be an abstract
	 *  class. One benefit of using interfaces is that they simulate multiple inheritance. 
	 *  All classes in Java (other than java.lang.Object, the root class of the Java type system) must 
	 *  have exactly one base class; multiple inheritance of classes is not allowed. 
	 *  However, a Java class/interface may implement/extend any number of interfaces.
	 */
	public interface staticConstants
	{
	   public static final String USER_ID  =   "userId";
	   public static final String PASSWORD =   "pword";
	  /** Java's support of interfaces provides a mechanism by which we can get the equivalent of 
	   * callbacks. The trick is to define a simple interface that declares the method we wish to be 
	   * invoked. For example, suppose we want to be notified when an event happens. 
	   * We can define an interface:*/
	    public void interestingEvent();

	}
	
	



