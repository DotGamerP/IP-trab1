import java.util.Arrays;

/**
 * The {@code SumdokuTest} tests some functions and other procedures of  
 * {@code Sumdoku} class, according to IP first coding project. 
 * 
 * 		Compile: javac -cp  SumdokuLib.jar   Sumdoku.java SumdokuTest.java
 * 		Execute: java  -cp  SumdokuLib.jar:. SumdokuTest
 * 
 * @author malopes IP2425@LEI-FCUL 
 * @version 1
 */

public class SumdokuTest {

	public static void main(String[] args) {
		System.out.println ("Testing Somadoku.java \n");

		testRowOfSquare();
		testColumnOfSquare();
		System.out.println ();

		testGridIsValidForPuzzle ();
		testGroupsIsValidForPuzzle ();
		testDefinesPuzzle();
		System.out.println ();
		
		testCluesToString();
		testPuzzleSolved();
	}

	private static void testRowOfSquare () {
		System.out.println ("Testing rowOfSquare:");
		boolean error = false;

		int size = 3;
		int square = 4;

		int expected = 2;
		int obtained = Sumdoku.rowOfSquare (square, size);
		error = checkEqual(expected, obtained, Integer.toString(square), Integer.toString(size)) || error;

		size = 4;
		expected = 1;

		obtained = Sumdoku.rowOfSquare (square, size);
		error = checkEqual(expected, obtained, Integer.toString(square), Integer.toString(size)) || error;

		size = 3;
		square = 8;
		expected = 3;

		obtained = Sumdoku.rowOfSquare (square, size);
		error = checkEqual(expected, obtained, Integer.toString(square), Integer.toString(size)) || error;

		System.out.println (error ? "FAIL" : "PASS");	
	}	

	private static void testColumnOfSquare () {
		System.out.println ("Testing columnOfSquare:");
		boolean error = false;

		int size = 4;
		int square = 3;

		int expected = 3;
		int obtained = Sumdoku.columnOfSquare (square, size);
		error = checkEqual(expected, obtained, Integer.toString(square), Integer.toString(size)) || error;

		size = 3;
		square = 5;
		expected = 2;
		obtained = Sumdoku.columnOfSquare (square, size);
		error = checkEqual(expected, obtained, Integer.toString(square), Integer.toString(size)) || error;

		size = 3;
		square = 9;
		expected = 3;

		obtained = Sumdoku.columnOfSquare (square, size);
		error = checkEqual(expected, obtained, Integer.toString(square), Integer.toString(size)) || error;

		System.out.println (error ? "FAIL" : "PASS");	
	}	

	private static void testGridIsValidForPuzzle () {
		System.out.println ("Testing isValidForPuzzle(Grid):");
		boolean error = false;

		SumdokuGrid grid = grid3();
		boolean obtained = Sumdoku.isValidForPuzzle(grid);
		error = checkEqual(true, obtained, grid.toString()) || error;

		grid.fill(1, 3, 3); // 3 twice in the first line
		obtained = Sumdoku.isValidForPuzzle(grid);
		error = checkEqual(false, obtained, grid.toString()) || error;

		grid = grid3();
		grid.fill(3, 3, 3); // 3 twice in the last column
		obtained = Sumdoku.isValidForPuzzle(grid);
		error = checkEqual(false, obtained, grid.toString()) || error;

		grid = new SumdokuGrid(4);
		grid.fill(1, 1, 1); // only one square is filled
		obtained = Sumdoku.isValidForPuzzle(grid);
		error = checkEqual(false, obtained) || error;

		grid = grid5();
		obtained = Sumdoku.isValidForPuzzle(grid);
		error = checkEqual(true, obtained, grid.toString()) || error;

		System.out.println (error ? "FAIL" : "PASS");
	}

	private static void testGroupsIsValidForPuzzle () {
		System.out.println ("Testing isValidForPuzzle(GridGroups):");
		boolean error = false;


		GridGroups groups = groups3();
		boolean obtained = Sumdoku.isValidForPuzzle(groups);
		error = checkEqual(true, obtained, groups.toString()) || error;

		groups = new GridGroups(2, 1); //grid size is invalid
		groups.addSquareToGroup(1, 1, 1);
		groups.addSquareToGroup(1, 2, 1);
		groups.addSquareToGroup(2, 1, 1);
		groups.addSquareToGroup(2, 2, 1);
		obtained = Sumdoku.isValidForPuzzle(groups);
		error = checkEqual(false, obtained, groups.toString()) || error;

		groups = new GridGroups(3, 3); //an empty group
		groups.addSquareToGroup(1, 1, 1);
		groups.addSquareToGroup(1, 2, 1);
		groups.addSquareToGroup(2, 1, 1);
		groups.addSquareToGroup(2, 2, 2);
		groups.addSquareToGroup(1, 3, 2);
		groups.addSquareToGroup(2, 3, 2);
		groups.addSquareToGroup(3, 1, 2);
		groups.addSquareToGroup(3, 2, 2);
		groups.addSquareToGroup(3, 3, 2);
		obtained = Sumdoku.isValidForPuzzle(groups);
		error = checkEqual(false, obtained, groups.toString()) || error;

		groups = new GridGroups(3, 3); //not every square is in a goup
		groups.addSquareToGroup(1, 1, 1);
		groups.addSquareToGroup(1, 2, 1);
		groups.addSquareToGroup(2, 1, 1);
		groups.addSquareToGroup(2, 2, 2);
		groups.addSquareToGroup(1, 3, 2);
		groups.addSquareToGroup(2, 3, 2);
		groups.addSquareToGroup(3, 2, 3);
		groups.addSquareToGroup(3, 3, 3);
		obtained = Sumdoku.isValidForPuzzle(groups);
		error = checkEqual(false, obtained, groups.toString()) || error;

		groups = groups3();
		obtained = Sumdoku.isValidForPuzzle(groups);
		error = checkEqual(true, obtained, groups.toString()) || error;

		System.out.println (error ? "FAIL" : "PASS");
	}

	private static void testDefinesPuzzle () {
		System.out.println ("Testing definesPuzzle(Grid):");
		boolean error = false;

		GridGroups groups = groups3();
		SumdokuGrid grid = grid3();

		boolean obtained = Sumdoku.definesPuzzle(grid, groups);
		error = checkEqual(true, obtained, grid.toString(),  groups.toString()) || error;

		grid = grid5();	//not agree in size
		obtained = Sumdoku.definesPuzzle(grid, groups);
		error = checkEqual(false, obtained, grid.toString(), groups.toString()) || error;


		grid = grid3();
		groups =  new GridGroups(3, 1); //not a single solution
		groups.addSquareToGroup(1, 1, 1);
		groups.addSquareToGroup(1, 2, 1);
		groups.addSquareToGroup(1, 3, 1);
		groups.addSquareToGroup(2, 1, 1);
		groups.addSquareToGroup(2, 2, 1);
		groups.addSquareToGroup(2, 3, 1);
		groups.addSquareToGroup(3, 1, 1);
		groups.addSquareToGroup(3, 2, 1);
		groups.addSquareToGroup(3, 3, 1);

		obtained = Sumdoku.definesPuzzle(grid, groups);
		error = checkEqual(false, obtained, grid.toString(), groups.toString()) || error;
		System.out.println (error ? "FAIL" : "PASS");		
	}
	
	
	private static void testCluesToString () {
		System.out.println ("Testing cluesToString:");
		boolean error = false;

		GridGroups groups = groups3();
		SumdokuGrid grid = grid3();

		String obtained = Sumdoku.cluesToString(grid, groups);
		String expected = "Soma das casas: G1 = 5 G2 = 2 G3 = 5 G4 = 5 G5 = 1 \n";
		error = !obtained.equals(expected) || error;
		if (error) {
			System.out.println (">>> expected:" + expected + "<<<");
			System.out.println (">>> obtained:" + obtained + "<<<");
		}
		
		grid = grid5();	
		groups = groups5();
		obtained = Sumdoku.cluesToString(grid, groups);
		expected = "Soma das casas: G1 = 14 G2 = 3 G3 = 5 G4 = 8 G5 = 5 "+
						"G6 = 3 G7 = 9 G8 = 6 G9 = 10 G10 = 5 G11 = 7 \n";
		error =  !obtained.equals(expected) || error;
		if (error) {
			System.out.println (">>> expected:" + expected);
			System.out.println (">>> obtained:" + obtained);
		}
		System.out.println (error ? "FAIL" : "PASS");		
	}
	
	private static void testPuzzleSolved () {
		System.out.println ("Testing puzzleSolved:");
		boolean error = false;

		SumdokuGrid grid = grid3();
		SumdokuGrid gridPlayed = new SumdokuGrid(3);
		gridPlayed.fill(1, 1, 3);
		gridPlayed.fill(1, 2, 1);
		gridPlayed.fill(1, 3, 2);
		gridPlayed.fill(2, 1, 1); //unfilled squares
		
		boolean obtained = Sumdoku.puzzleSolved(gridPlayed, grid);
		error = checkEqual(false, obtained, gridPlayed.toString(), grid.toString()) || error;
		
		gridPlayed.fill(2, 2, 2);
		gridPlayed.fill(2, 3, 3);
		gridPlayed.fill(3, 1, 2);
		gridPlayed.fill(3, 2, 1); //squares filled with wrong numbers
		gridPlayed.fill(3, 3, 3); //idem
		
		obtained = Sumdoku.puzzleSolved(gridPlayed, grid);
		error = checkEqual(false, obtained, gridPlayed.toString(), grid.toString()) || error;

		gridPlayed = grid3();
		obtained = Sumdoku.puzzleSolved(gridPlayed, grid);
		error = checkEqual(true, obtained, gridPlayed.toString(), grid.toString()) || error;
		
		System.out.println (error ? "FAIL" : "PASS");		
	}

	private static boolean checkEqual(int expected, int obtained, String... args) {
		boolean error = false;
		if (obtained != expected) {
			System.out.println(Arrays.toString(args));
			System.out.printf (">>> expected: %d obtained: %d %n", expected, obtained);
			error = true;
		}
		return error;
	}

	private static boolean checkEqual(boolean expected, boolean obtained, String... args) {
		boolean error = false;
		if (expected != obtained) {
			System.out.println(Arrays.toString(args));
			System.out.printf (">>> expected: %b obtained: %b %n", expected, obtained);

			error = true;
		}
		return error;
	}

	static SumdokuGrid  grid3() {
		SumdokuGrid grid = new SumdokuGrid(3);
		grid.fill(1, 1, 3);
		grid.fill(1, 2, 1);
		grid.fill(1, 3, 2);
		grid.fill(2, 1, 1);
		grid.fill(2, 2, 2);
		grid.fill(2, 3, 3);
		grid.fill(3, 1, 2);
		grid.fill(3, 2, 3);
		grid.fill(3, 3, 1);
		return grid;
	}

	static GridGroups  groups3() {
		GridGroups groups = new GridGroups(3, 5);

		groups.addSquareToGroup(1, 1, 1);
		groups.addSquareToGroup(1, 2, 1);
		groups.addSquareToGroup(2, 1, 1);
		groups.addSquareToGroup(2, 2, 2);
		groups.addSquareToGroup(1, 3, 3);
		groups.addSquareToGroup(2, 3, 3);
		groups.addSquareToGroup(3, 1, 4);
		groups.addSquareToGroup(3, 2, 4);
		groups.addSquareToGroup(3, 3, 5);

		return groups;
	}

	static SumdokuGrid  grid5() {
		SumdokuGrid grid = new SumdokuGrid(5);
		grid.fill(1, 1, 2);
		grid.fill(1, 2, 5);
		grid.fill(1, 3, 3);
		grid.fill(1, 4, 1);
		grid.fill(1, 5, 4);
		grid.fill(2, 1, 5);
		grid.fill(2, 2, 3);
		grid.fill(2, 3, 4);
		grid.fill(2, 4, 2);
		grid.fill(2, 5, 1);
		grid.fill(3, 1, 1);
		grid.fill(3, 2, 2);
		grid.fill(3, 3, 5);
		grid.fill(3, 4, 4);
		grid.fill(3, 5, 3);
		grid.fill(4, 1, 4);
		grid.fill(4, 2, 1);
		grid.fill(4, 3, 2);
		grid.fill(4, 4, 3);
		grid.fill(4, 5, 5);
		grid.fill(5, 1, 3);
		grid.fill(5, 2, 4);
		grid.fill(5, 3, 1);
		grid.fill(5, 4, 5);
		grid.fill(5, 5, 2);
		return grid;
	}

	static GridGroups  groups5() {
		GridGroups groups = new GridGroups(5, 11);

		groups.addSquareToGroup(1, 1, 1);
		groups.addSquareToGroup(1, 2, 1);
		groups.addSquareToGroup(1, 3, 1);
		groups.addSquareToGroup(1, 4, 2);
		groups.addSquareToGroup(1, 5, 3);
		groups.addSquareToGroup(2, 1, 4);
		groups.addSquareToGroup(2, 2, 4);
		groups.addSquareToGroup(2, 3, 1);
		groups.addSquareToGroup(2, 4, 2);
		groups.addSquareToGroup(2, 5, 3);
		groups.addSquareToGroup(3, 1, 5);
		groups.addSquareToGroup(3, 2, 6);
		groups.addSquareToGroup(3, 3, 7);
		groups.addSquareToGroup(3, 4, 7);
		groups.addSquareToGroup(3, 5, 8);
		groups.addSquareToGroup(4, 1, 5);
		groups.addSquareToGroup(4, 2, 6);
		groups.addSquareToGroup(4, 3, 9);
		groups.addSquareToGroup(4, 4, 9);
		groups.addSquareToGroup(4, 5, 9);
		groups.addSquareToGroup(5, 1, 8);
		groups.addSquareToGroup(5, 2, 10);
		groups.addSquareToGroup(5, 3, 10);
		groups.addSquareToGroup(5, 4, 11);
		groups.addSquareToGroup(5, 5, 11);

		return groups;
	}


}
