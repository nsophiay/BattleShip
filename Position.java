//----------------------------------
// Assignment 1
// Written by: Saffia Niro, #40054733
// For COMP 249 Section E - Fall 2018
// September 24, 2018
//----------------------------------

/**
 * 
 * @author Saffia Niro
 *
 */

public class Position {
	
	private String type;
	private String owner;
	private boolean called;
	private boolean hit;

	/**
	 * Default constructor
	 */
	
	public Position(){
		type = "none";
		owner = "none";
		called = false;
	}
	
	/**
	 * Constructor with three parameters
	 * @param type - The type of position (ship or grenade)
	 * @param owner - The owner (computer or human)
	 * @param called - Whether or not the position has already been called
	 */
	public Position(String type, String owner, boolean called){
		
		this.type = type;
		this.owner = owner;
		this.called = called;
		
	}

	/**
	 * Accessor for the type
	 * @return String - the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Mutator for the type
	 * @param type - the type
	 */

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Accessor for the owner
	 * @return String - the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Mutator for the owner
	 * @param owner - The owner (computer or human)
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * Accessor for the called boolean
	 * @return boolean - whether or not position has been called already
	 */
	public boolean isCalled() {
		return called;
	}

	/**
	 * Mutator for the called boolean
	 * @param called - whether or not position has been called already
	 */
	public void setCalled(boolean called) {
		this.called = called;
	}
	
	/**
	 * Accessor for the hit boolean
	 * @return boolean - whether or not the position has been fired upon yet
	 */
	public boolean isHit() {
		return hit;
	}

	/**
	 * Mutator for the hit boolean
	 * @param hit - whether or not the position has been fired upon yet
	 */
	public void setHit(boolean hit) {
		this.hit = hit;
	}
	
}
