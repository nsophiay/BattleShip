import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Arrays;

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

public class GameDriver { 

	private static String winner;

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in); // Declare and initialize scanner object
		Position[][] grid = new Position[8][8]; // 2D array of Position objects to represent the gameboard
		boolean doubleTurnH = false, doubleTurnC = false; // Two booleans to keep track of lost turns
		char a ;
		int x, y; // Integers to be used as the indexes for the array
		String coordinates;
		boolean invalid; // Boolean for invalid input

		placeItems(grid); // Places items for the human player
		placeComputerItems(grid); // Randomly places items for the computer

		// While the game isn't won, keep playing
		while(!gameWon(grid)){

			// HUMAN'S TURN

			// Check if coordinates entered are valid. If not, ask again
			do{
				System.out.print("Enter the position of your rocket: ");

				coordinates = input.nextLine();
				a = coordinates.charAt(0); // Store the letter entered in a char variable
				x = (coordinates.charAt(1) - '0')-1; // Store the number entered as an int
				y = convert(a);	// Convert the letter to an int so that it can be used as an index
				// The letter must be between A/a and H/h and the number must be between 0 and 7
				invalid = a <= 64 || (a >= 73 && a <= 96) || a >= 105 || x > 7 || y > 7 || x < 0 || y < 0;

				if(invalid)
					System.out.println("Position outside of range; try again");

			}while(invalid);

			// Series of if statements that checks what the rocket hit and prints the result
			if(grid[x][y].isCalled() && grid[x][y].getType() == "ship" && !grid[x][y].isHit()){
				grid[x][y].setHit(true);
				System.out.println("ship hit");
			}
			else if(grid[x][y].isCalled() && grid[x][y].getType() == "grenade" && !grid[x][y].isHit()){
				grid[x][y].setHit(true);
				System.out.println("grenade hit; lose a turn");
				doubleTurnC = true; // If the human hits a grenade, the computer will play twice
			}
			else if(grid[x][y].isHit())
				System.out.println("already called");
			else{
				grid[x][y].setHit(true);
				System.out.println("nothing hit");
			}
			printGrid(grid);

			// If the computer hit a grenade, the human gets to go again
			if(doubleTurnH){
				do{
					System.out.print("Enter the position of your rocket: ");

					coordinates = input.nextLine();
					a = coordinates.charAt(0);
					x = (coordinates.charAt(1) - '0')-1;
					y = convert(a);

					if(invalid)
						System.out.println("Position outside of range; try again");

				}while(invalid);
				if(grid[x][y].isCalled() && grid[x][y].getType() == "ship"){
					grid[x][y].setHit(true);
					System.out.println("ship hit");
				}
				else if(grid[x][y].isCalled() && grid[x][y].getType() == "grenade"){
					grid[x][y].setHit(true);
					System.out.println("grenade hit; lose a turn");
					doubleTurnC = true;
				}
				else if(grid[x][y].isHit())
					System.out.println("already called");
				else{
					grid[x][y].setHit(true);
					System.out.println("nothing hit");
				}
				doubleTurnH = false; // Reset turn tracker
				printGrid(grid);
			}

			// COMPUTER'S TURN

			if(!gameWon(grid)){ // Ensures that the game immediately stops if the human wins first


				a = (char)ThreadLocalRandom.current().nextInt(65, 72+1); // Pick a random character from A-H or a-h
				x = ThreadLocalRandom.current().nextInt(0, 7+1); // Pick a random int from 0 to 7
				y = convert(a);

				System.out.println("The position of my rocket: " + a + (x+1));

				if(grid[x][y].isCalled() && grid[x][y].getType() == "ship"){
					grid[x][y].setHit(true);
					System.out.println("ship hit");
				}
				else if(grid[x][y].isCalled() && grid[x][y].getType() == "grenade"){
					grid[x][y].setHit(true);
					System.out.println("grenade hit; lose a turn");
					doubleTurnH = true; // If the computer hits a grenade, the human will play twice
				}
				else if(grid[x][y].isHit())
					System.out.println("already called");
				else{
					grid[x][y].setHit(true);
					System.out.println("nothing hit");
				}
				printGrid(grid);

				if(doubleTurnC){
					a = (char)ThreadLocalRandom.current().nextInt(65, 72+1);
					x = ThreadLocalRandom.current().nextInt(0, 7+1);
					y = convert(a);
					System.out.println("The position of my rocket: " + a + (x+1));
					if(grid[x][y].isCalled() && grid[x][y].getType() == "ship"){
						grid[x][y].setHit(true);
						System.out.println("ship hit");
					}
					else if(grid[x][y].isCalled() && grid[x][y].getType() == "grenade"){
						grid[x][y].setHit(true);
						System.out.println("grenade hit; lose a turn");
						doubleTurnH = true;
					}
					else if(grid[x][y].isHit())
						System.out.println("already called");
					else{
						grid[x][y].setHit(true);
						System.out.println("nothing hit");
					}
					doubleTurnC = false;
					printGrid(grid);
				}
			}

		}

		// Declare the winner and show the position of all ships and grenades
		System.out.println("\nGame over! The winner is: " + winner + "\nThe complete grid looks like this:"); 
		printFinalGrid(grid);

		input.close();


	}

	/**
	 * Method to place the human's items
	 * @param grid - 2D array of position objects representing the gameboard
	 */

	public static void placeItems(Position[][] grid){

		Scanner input = new Scanner(System.in);
		String coordinates = "";
		boolean invalid, taken = false;
		char a = 0;
		int x = 0, y = 0;

		// Fill array with default Position objects
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid.length; j++)
				grid[i][j] = new Position();
		}


		System.out.println("Let's play!\n");

		// Place ships and validate their positions
		for(int shipCount = 0; shipCount < 6; shipCount++){

			do{
				taken = false;
				System.out.print("Enter the coordinates of your ship #" + (shipCount+1) + ": ");
				coordinates = input.nextLine();
				a = coordinates.charAt(0);
				x = (coordinates.charAt(1) - '0')-1;
				y = convert(a);
				invalid = a <= 64 || (a >= 73 && a <= 96) || a >= 105 || x > 7 || y > 7 || x < 0 || y < 0;

				if(x < 8 && y < 8){
					if(invalid){
						System.out.println("Sorry, coordinates outside range, try again");
					}
					else if(grid[x][y].isCalled() == true){
						System.out.println("Sorry, coordinates are already taken, try again");
						taken = true;
					}
					else{
						grid[x][y].setCalled(true);
						grid[x][y].setOwner("human");
						grid[x][y].setType("ship");
					}
				}
				else
					System.out.println("Sorry, coordinates outside range, try again");

			}
			while(invalid || taken);

		}

		System.out.println();

		// Place grenades and validate their positions
		for(int grenadeCount = 0; grenadeCount < 4; grenadeCount++){

			do{
				taken = false;
				System.out.print("Enter the coordinates of your grenade #" + (grenadeCount+1) + ": ");
				coordinates = input.nextLine();
				a = coordinates.charAt(0);
				x = (coordinates.charAt(1) - '0')-1;
				y = convert(a);
				invalid = a <= 64 || (a >= 73 && a <= 96) || a >= 105 || x > 7 || y > 7 || x < 0 || y < 0;

				if(x < 8 && y < 8){
					if(invalid){
						System.out.println("Sorry, coordinates outside range, try again");
					}
					else if(grid[x][y].isCalled() == true){
						System.out.println("Sorry, coordinates are already taken, try again");
						taken = true;
					}
					else{
						grid[x][y].setCalled(true);
						grid[x][y].setOwner("human");
						grid[x][y].setType("grenade");
					}
				}
				else
					System.out.println("Sorry, coordinates outside range, try again");

			}
			while(invalid || taken);

		}

		System.out.println();

	}

	/**
	 * Method to randomly place the computer's items
	 * @param grid - 2D array of position objects representing the gameboard
	 */
	public static void placeComputerItems(Position[][] grid){

		boolean taken = false;
		int x, y;

		// Place ships and validate that the position is available
		for(int i = 0; i < 6; i++){
			do{
				taken = false;
				x = ThreadLocalRandom.current().nextInt(0, 7+1);
				y = ThreadLocalRandom.current().nextInt(0, 7+1);
				if(!grid[x][y].isCalled()){
					grid[x][y].setCalled(true);
					grid[x][y].setOwner("computer");
					grid[x][y].setType("ship");
				}
				else
					taken = true;
			}while(taken);
		}

		// Place grenades and validate that the position is available
		for(int i = 0; i < 4; i++){
			do{
				taken = false;
				x = ThreadLocalRandom.current().nextInt(0, 7+1);
				y = ThreadLocalRandom.current().nextInt(0, 7+1);
				if(!grid[x][y].isCalled()){
					grid[x][y].setCalled(true);
					grid[x][y].setOwner("computer");
					grid[x][y].setType("grenade");
				}
				else
					taken = true;
			}while(taken);
		}

		System.out.println("The computer placed its ships and grenades at random.");

	}

	/** Method to determine when the game is won
	 * @param grid - 2D array of position objects representing the gameboard
	 * @return boolean - whether or not the game has been won
	 */
	public static boolean gameWon(Position[][] grid){

		boolean wonC = true;
		boolean wonH = true;

		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid.length; j++){
				// If at least one human ship hasn't been hit yet, the computer has not won
				if(grid[i][j].getOwner() == "human" && grid[i][j].getType() == "ship" && !grid[i][j].isHit()){
					wonC = false;
				}
				// If at least one computer ship hasn't been hit yet, the human has not won
				if(grid[i][j].getOwner() == "computer" && grid[i][j].getType() == "ship" && !grid[i][j].isHit()){
					wonH = false;
				}

			}
		}

		if(wonH)
			winner = "you";
		else if(wonC)
			winner = "computer";

		return wonC || wonH;


	}


	/**
	 * Method to convert a character into a number that can be used as an index
	 * @param a - the char that needs to be converted
	 * @return int - the index
	 */
	public static int convert(char a){
		switch(a){
		case 'a': case 'A': return 0; 
		case 'b': case 'B': return 1;
		case 'c': case 'C': return 2;
		case 'd': case 'D': return 3;
		case 'e': case 'E': return 4;
		case 'f': case 'F': return 5;
		case 'g': case 'G': return 6;
		case 'h': case 'H': return 7;
		default: return 0;
		}

	}

	/**
	 * Method to print the grid
	 * @param grid - 2D array of position objects representing the gameboard
	 */
	public static void printGrid(Position[][] grid){

		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid.length; j++){
				if(grid[i][j].isHit() && !grid[i][j].isCalled())
					System.out.print(" * ");
				else if(grid[i][j].isHit() && grid[i][j].getType() == "ship" && grid[i][j].getOwner() == "human")
					System.out.print(" s ");
				else if(grid[i][j].isHit() && grid[i][j].getType() == "grenade" && grid[i][j].getOwner() == "human")
					System.out.print(" g ");
				else if(grid[i][j].isHit() && grid[i][j].getType() == "grenade" && grid[i][j].getOwner() == "computer")
					System.out.print(" G ");
				else if(grid[i][j].isHit() && grid[i][j].getType() == "ship" && grid[i][j].getOwner() == "computer")
					System.out.print(" S ");
				else
					System.out.print(" _ ");
			}
			System.out.println();

		}

	}

	/**
	 * Method to print the grid, showing all ships and grenades
	 * @param grid - 2D array of position objects representing the gameboard
	 */
	public static void printFinalGrid(Position[][] grid){

		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid.length; j++){
				if(grid[i][j].getType() == "ship" && grid[i][j].getOwner() == "human")
					System.out.print(" s ");
				else if(grid[i][j].getType() == "grenade" && grid[i][j].getOwner() == "human")
					System.out.print(" g ");
				else if(grid[i][j].getType() == "grenade" && grid[i][j].getOwner() == "computer")
					System.out.print(" G ");
				else if(grid[i][j].getType() == "ship" && grid[i][j].getOwner() == "computer")
					System.out.print(" S ");
				else
					System.out.print(" _ ");
			}
			System.out.println();

		}



	}

}
