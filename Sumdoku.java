import java.util.Scanner;

/**
 * The {@code Sumdoku} consists in the class which contains the full project.
 * 
 * This is the first project of 2024 in IP (Introdução à Programação) on FCUL (Faculdade de Ciências - Universidade de Lisboa)
 * 
 * 		IN LINUX:
 *      Compile: javac -cp SumdokuLib.jar Sumdoku.java 
 * 		Execute: java  -cp  SumdokuLib.jar:. Sumdoku
 * 
 * 		IN WINDOWS:
 * 		Compile: javac -cp SumdokuLib.jar Sumdoku.java
 * 		Execute: java -cp "SumdokuLib.jar;." Sumdoku
 * 
 * @author Pedro Reinaldo Mendes - nº63729
 * @author Tiago Costa - nº63718
 * @version 1.0
 */  
public class Sumdoku {

    /**
     * Entry point for the Sumdoku game. Handles both user-input 
     * puzzles and predefined puzzles based on the first argument.
     *
     * @param args command-line arguments: The first argument (if provided) specifies the grid size of the default puzzle the user's going to resolve in the game
     * @requires The first argument (if provided) must be a positive integer
     * @ensures Initializes and runs the Sumdoku puzzle game or displays an error for invalid input.
     */
    public static void main(String[] args){

        // We create the scanner variable that we'll be using and the gridSize that'll be either asked or directly from an argument
        Scanner sc = new Scanner(System.in);
        int gridSize;
        // Also, we create the puzzle's SumdokuGrid and GridGroups that will be used to play
        SumdokuGrid puzzleGrid;
        GridGroups puzzleGroups;

        // We must verify if the user defined the grid size in the arguments or not
        if (args.length == 0){

            // If there are no arguments, we'll indicate to the user that we'll be reading his new puzzle...
            System.out.println("Leitura do puzzle."); 
            // We ask and get the grid size
            gridSize = askAndGetGridSize(sc);
            // We read the grid and verify it
            puzzleGrid = verifyAndReadGrid(gridSize, sc);
            // We read the groups and verify them
            puzzleGroups = verifyAndReadGroups(puzzleGrid, sc);
            // We end the puzzle creation with a line break
            System.out.println("");

            // Once we have the value of "gridSize" and the grid/groups of the puzzle, because the user wrote it, we can play
            play(puzzleGrid, puzzleGroups, gridSize*gridSize, sc);

        } else {
            // We store the first argument as an integer in a variable
            int x = Integer.parseInt(args[0]);

            if (getBuiltInGrid(x) == null || getBuiltInGroups(x) == null){
                // If we don't have any default puzzle for the size, we'll tell the user that it's not valid and end the program
                System.out.println("Tamanho da grelha inválido ou não suportado.");

            } else {
                // If we have a valid int argument, we'll use our default puzzle...
                puzzleGrid = getBuiltInGrid(x);
                puzzleGroups = getBuiltInGroups(x);
                // Now, we can obtain the grid size from the puzzle grid
                gridSize = puzzleGrid.size();
                // Once we have the value of "gridSize" and the grid/groups default of the puzzle, because it was in the argument, we can play
                play(puzzleGrid, puzzleGroups, gridSize*gridSize, sc);
            }
        }

        // We end the program by closing our Scanner
        sc.close();
    }

    /**
     * Ask and get a grid size (we'll also verify that it's between 3 and 9)
     *
     * @param sc Scanner that will be used to obtain the user's response
     * @requires {@code sc != null}
     * @ensures {@code \result >= 3 && \result <= 9}
     * @return the verified grid size entered by the user
     */
    private static int askAndGetGridSize(Scanner sc){
        
        // We'll ask the grid size
        System.out.print("Tamanho da grelha? ");
        // We get the input of the grid size from the user
        int gridSize = sc.nextInt(); 

        // We'll verify the gridSize is between 3 and 9
        while (gridSize < 3 || gridSize > 9){
            // If the grid size is invalid, we'll warn the user
            System.out.println("Valor invalido. Tem de estar entre 3 e 9.");
            // And, this time, we'll simply get directly the new value from the user (as shown when an input is invalid in the "$java Sumdoku"'s project example)
            gridSize = sc.nextInt();
        }

        return gridSize; // We finally return the verified grid size
    }

    /**
     * Verify and read the user's grid
     *
     * @param gridSize The size of the grid we want to read
     * @param sc Scanner that will be used to obtain the user's response
     * @requires {@code gridSize > 2 && gridSize < 10 && sc != null}
     * @ensures {@code \result != null && isValidForPuzzle(\result)}
     * @return the verified SumdokuGrid entered by the user
     */
    private static SumdokuGrid verifyAndReadGrid(int gridSize, Scanner sc){

        // We read the grid
        SumdokuGrid puzzleGrid = readGrid(gridSize, sc);

        // We verify if it's a valid grid
        while (!isValidForPuzzle(puzzleGrid)) {
            // If it's not valid for a puzzle we must tell him and start reading the grid again
            System.out.println("A grelha inserida é inválida. Recomece.");
            puzzleGrid = readGrid(gridSize, sc);
        }
        return puzzleGrid; // We finally return the verified grid
    }

    /**
     * Verify and read the user's groups
     *
     * @param grid The SumdokuGrid object representing the puzzle grid
     * @param sc Scanner that will be used to obtain the user's response
     * @requires {@code grid != null && sc != null}
     * @ensures {@code \result != null && isValidForPuzzle(\result)}
     * @return the verified GridGroups entered by the user
     */
    private static GridGroups verifyAndReadGroups(SumdokuGrid grid, Scanner sc){

        // We read the groups
        GridGroups puzzleGroups = readGroups(grid, sc);
        // We verify if it's a valid grid
        while (!isValidForPuzzle(puzzleGroups)) {
            // If it's not valid for a puzzle we must tell him and start reading the groups again
            System.out.println("Os grupos inseridos são inválidos. Recomece.");
            puzzleGroups = readGroups(grid, sc);
        }
        return puzzleGroups; // We finally return the verified groups
    }

    /**
     * Calculate the row number of a given square in a grid
     *
     * @param square The index of the square (being 1 the first square)
     * @param gridSize The size of the grid (number of squares per row/column)
     * @requires {@code square > 0 && square <= gridSize*gridSize && gridSize > 2 && gridSize < 10}
     * @ensures {@code \result > 0 && \result <= gridSize}
     * @return the row number of the specified square
     */
    public static int rowOfSquare(int square, int gridSize){

        /*
        -> In each row, we have a "gridSize" number of squares
        -> That's why we'll be basically dividing the whole grid by the "gridSize" number in order to distinguish each row
        -> In order to get the row number we could, for example, divide the square number by the "gridSize" and add 1 (EXCEPT IF WE'RE DEALING WITH THE LAST SQUARE OF THE ROW)
        -> If we're dividing the number of the last square of a row by the "gridSize" we'll get a row number that's too high
        -> So, we'll be theoretically thinking that the first square is the number 0, in order for every calculation to be fine (square-1)
        */

        int rowNumber = ((square-1)/gridSize) + 1; // We calculate and store the row number
        return rowNumber; // We return the row number
    }

    /**
     * Calculate the column number of a given square in a grid
     *
     * @param square The index of the square (being 1 the first square)
     * @param gridSize The size of the grid (number of squares per row/column)
     * @requires {@code square > 0 && square <= gridSize * gridSize && gridSize > 2 && gridSize < 10}
     * @ensures {@code \result > 0 && \result <= gridSize}
     * @return the column number of the specified square
     */
    public static int columnOfSquare(int square, int gridSize){

        /*
        -> In the first row we'll have the squares from 1 to "gridSize" 
        -> That's why we'll subtract the "gridSize" to the square number in order to theoretically go down one row
        */
        while (square > gridSize){
            square -= gridSize;
        }

        int columnNumber = square; // Once the square number is less or equal than the "gridSize", we'll be getting exactly the column number
        return columnNumber; // We return the column number

    }

    /**
     * Validates whether the given Sumdoku grid follows the necessary rules for a valid puzzle.
     * 
     * @param grid The Sumdoku grid to be validated.
     * @return True if the grid is valid, false otherwise.
     */
    public static boolean isValidForPuzzle(SumdokuGrid grid) {
        // Check if the grid is null
        if (grid == null) {
            return false; // Return false if grid is null
        }
    
        // Get the size of the grid
        int gridSize = grid.size();
        // Calculate the expected sum for each row and column (1 + 2 + ... + n)
        int targetSum = gridSize * (gridSize + 1) / 2;
    
        // Loop through each row and column to check for validity
        for (int rowIndex = 1; rowIndex <= gridSize; rowIndex++) {
            int currentRowSum = 0;
            int currentColSum = 0;
    
            // Check each cell in the row and column
            for (int colIndex = 1; colIndex <= gridSize; colIndex++) {
                int rowValue = grid.value(rowIndex, colIndex); // Value in the current row
                int colValue = grid.value(colIndex, rowIndex); // Value in the current column
    
                // Check for duplicates in the row up to the current column
                for (int prevColIndex = 1; prevColIndex < colIndex; prevColIndex++) {
                    if (grid.value(rowIndex, prevColIndex) == rowValue) {
                        return false; // Duplicate found in the row, invalid grid
                    }
                }
    
                // Check for duplicates in the column up to the current row
                for (int prevRowIndex = 1; prevRowIndex < colIndex; prevRowIndex++) {
                    if (grid.value(prevRowIndex, rowIndex) == colValue) {
                        return false; // Duplicate found in the column, invalid grid
                    }
                }
    
                // Check if the value is outside the valid range (1 to grid size)
                if (rowValue < 1 || rowValue > gridSize || colValue < 1 || colValue > gridSize) {
                    return false; // Invalid value, outside the allowed range
                }
    
                // Update the row and column sums
                currentRowSum += rowValue;
                currentColSum += colValue;
            }
    
            // Check if the sum of the row and column match the expected sum
            if (currentRowSum != targetSum || currentColSum != targetSum) {
                return false; // Invalid grid if the sum doesn't match
            }
        }
    
        // If all checks pass, the grid is valid
        return true;
    }

    /**
     * Validate if the given GridGroups is valid for the puzzle.
     *
     * @param groups The GridGroups to validate
     * @return True if the groups are valid, false otherwise
     */
    public static boolean isValidForPuzzle(GridGroups groups){

        /*
        -> First we'll verify if "groups":
            · have a null value
            · doesn't have every square in a group
            · have empty groups
            · doesn't have a size (".gridSize()") greater than 2 and less than 10 
        -> If at least one of these conditions is true, the object won't be valid
        */

        if (groups == null || !isEverySquareInGroup(groups) || hasEmptyGroup(groups) || !(groups.gridSize() > 2 && groups.gridSize() < 10)){
            return false; // The object isn't valid (we'll return false)
        } else {			
            return true; // The object is valid (we'll return true)
        }
    }

    /**
     * Check if every square in the grid belongs to a group.
     *
     * This method iterates through all squares in the grid and checks if each square is assigned to a group.
     * 
     * @param groups The GridGroups object containing the grid.
     * @requires {@code groups != null}
     * @return True if every square is assigned to a group, false otherwise.
     */
    private static boolean isEverySquareInGroup(GridGroups groups){

        // We store the grid size in a variable in order to optimize the speed of the code
        int gridSize = groups.gridSize(); 

        // We'll go through every square of the grid
        for(int r = 1; r <= gridSize; r++){
            for(int c = 1; c <= gridSize; c++){
                
                // If the group of the square in the row "r" and column "c" is 0, it doesn't belong to a group
                if (groups.groupOfSquare(r, c) == 0)
                    return false; // If at least one square doesn't belong to a group, we'll return false
            }
        }

        return true; // If it never found a square without a group, we'll return true

    }

    /**
     * Check if there are any empty groups in a certain GridGroups
     *
     * This method iterates through all groups and checks if any group has no squares assigned to it.
     * 
     * @param groups The GridGroups object containing the groups
     * @requires {@code groups != null}
     * @return True if there is an empty group, false otherwise.
     */
    private static boolean hasEmptyGroup(GridGroups groups){

        // We store the grid size and the number of groups in a variable in order to optimize the speed of the code
        int gridSize = groups.gridSize(); 
        int numberOfGroups = groups.numberOfGroups();

        boolean gEmpty; // We define a variable that will track if a group (g) is empty or not
        
        // We'll go through every group number
        for(int g = 1; g <= numberOfGroups; g++){
            gEmpty = true; // We start stating that the group is empty
            // We'll go through every square while the group is empty ("gEmpty == true")
            for(int r = 1; r <= gridSize && gEmpty; r++){
                for(int c = 1; c <= gridSize && gEmpty; c++){

                    // We verify if the group of the square is the group we're working with at the moment
                    if (groups.groupOfSquare(r, c) == g)
                        gEmpty = false; // If the group of the square is the one we're working with, we'll state the group isn't empty anymore
                }
            }
            // We verify after going through the squares if the group we're working with is empty
            if (gEmpty)
                return true; // If we have an empty group, we return true
        }

        return false; // If it never found a group without squares, we'll return false

    }

    /**
     * Checks if the given Sumdoku puzzle defined by the grid and group 
     * configuration is a valid puzzle with exactly one solution.
     *
     * @param grid The Sumdoku grid to be validated.
     * @param groups The group configuration that defines the puzzle's rules.
     * @return true if the grid and group define a valid puzzle with exactly one solution, false otherwise.
     */
    public static boolean definesPuzzle(SumdokuGrid grid, GridGroups groups) {
        // Check if the grid size matches the group configuration size.
        if (grid.size() != groups.gridSize()) {
            return false; // Return false if sizes do not match.
        }

        // Create a SumdokuSolver to calculate the number of possible solutions.
        SumdokuSolver solutions = new SumdokuSolver(grid, groups);

        // Check how many solutions exist for the given puzzle, using a maximum of 2 solutions.
        int numSolutions = solutions.howManySolutions(2);

        // Return false if there is not exactly one solution.
        if (numSolutions != 1) {
            return false;
        }

        // Return true if the puzzle has exactly one solution.
        return true;
    }

    /**
     * Calculate the sum of the values of squares in each group and return the results as a formatted string.
     *
     * This method iterates through all the groups and squares in the grid, calculating the sum of the values
     * in each group and constructing a string that presents this information.
     * 
     * @param grid The SumdokuGrid containing the values of the squares.
     * @param groups The GridGroups object that defines the group assignments for the squares.
     * @requires {@code grid != null && groups != null && groups.gridSize() > 2 && groups.gridSize() < 10 && definesPuzzle(grid, groups)}
     * @ensures {@code \result != null}
     * @return A string containing the sum of the values for each group in the format: "Soma das casas: G1 = X G2 = Y ...".
     */
    public static String cluesToString(SumdokuGrid grid, GridGroups groups){

        // We store the grid size and the number of groups in a variable in order to optimize the speed of the code
        int gridSize = groups.gridSize(); 
        int numberOfGroups = groups.numberOfGroups();

        // We'll create a StringBuilder in order to update the result constantly and a sum variable to track the sum of each group
        StringBuilder result = new StringBuilder("Soma das casas:");
        int sum = 0;

        // We'll go through every possible group
        for(int g = 1; g <= numberOfGroups; g++){
            // We start setting the sum to 0
            sum = 0;
            // We'll go through every possible square
            for(int r = 1; r <= gridSize; r++){
                for(int c = 1; c <= gridSize; c++){

                    // We verify if the square is part of the group we're working with at the moment
                    if (groups.groupOfSquare(r, c) == g)
                        sum += grid.value(r, c); // If the square is part of the group we're working with, we add the value of that square to sum
                }
            }
            // After doing the sum of the values in the group, we add it to the StringBuilder result with the right format
            result.append(" G" + g + " = " + sum);
        }
        result.append(" \n"); // We finalize the StringBuilder result with a space (required by SumdokuTest.java) and a line break
        return result.toString(); // We return the StringBuilder result converted to String
    }
    
    /**
     * Read and construct a SumdokuGrid object based on user input, associating squares to their values.
     *
     * This method prompts the user to input values for each square in the grid and constructs a `SumdokuGrid` object 
     * by filling each square with the provided values. The grid size is defined by the user input.
     *
     * @param size The size of the grid (number of rows and columns in the grid).
     * @param sc The Scanner object used to receive user input.
     * @requires {@code size > 2 && size < 10 && leitor != null} 
     * @ensures {@code \result != null} 
     * @return A SumdokuGrid object filled with the values provided by the user for each square.
     */
    public static SumdokuGrid readGrid(int size, Scanner sc) {

        // Initialize the value of the square and the square counter
        int valueOfSquare;
        int square = 0;
    
        // Create a new SumdokuGrid with the specified size
        SumdokuGrid finalSumdokuGrid = new SumdokuGrid(size);
    
        // Calculate the total number of squares in the grid (size * size)
        int numOfSquares = size * size;
    
        // Loop through each row in the grid
        for (int row = 1; row <= size; row++) {
            // Loop through each column in the grid for the current row
            for (int col = 1; col <= size; col++) {
    
                // Increment the square counter for each grid position
                square++;
    
                // Prompt the user to enter a value for the current square
                System.out.print("Casa " + square + ": ");
                valueOfSquare = sc.nextInt();
    
                // Ensure that the entered value is within the valid range (1 to size)
                while (valueOfSquare < 1 || valueOfSquare > size) {
                    // If the value is invalid, notify the user and prompt for re-entry
                    System.out.println("Valor invalido. Tem de estar entre 1 e " + size + ".");
                    valueOfSquare = sc.nextInt();
                }
    
                // Fill the Sumdoku grid at the current row and column with the valid value
                finalSumdokuGrid.fill(row, col, valueOfSquare);
            }
        }
    
        // Return the filled Sumdoku grid
        return finalSumdokuGrid;
    }

    /**
     * Read and construct a GridGroups object based on user input, associating squares to groups in a grid.
     *
     * This method asks the user for the number of groups, the size of each group, and the squares that belong to each group. 
     * It constructs a GridGroups object with the specified group information and returns it.
     * 
     * @param grid The SumdokuGrid containing the values of the squares.
     * @param sc The Scanner object used to receive user input.
     * @requires {@code grid != null && sc != null && grid.size() > 2 && grid.size() < 10 && definesPuzzle(grid, groups)}
     * @ensures {@code \result != null} 
     * @return A GridGroups object containing the group assignments for each square in the grid.
     */
    public static GridGroups readGroups(SumdokuGrid grid, Scanner sc){

        // We store the grid size and number of squares in a variable in order to optimize the speed of the code
        int gridSize = grid.size(); 
        int numOfSquares = gridSize*gridSize;

        // We ask and get the total number of groups (the function already does the verification that the group is between 1 and "numOfSquares")
        int numOfGroups = askAndGetNumOfGroups(sc, numOfSquares);

        GridGroups finalGridGroups = new GridGroups(gridSize, numOfGroups); // Now we can create the GridGroups that will be returned at the end

        // We create a two variables that will keep track of each group's size and each square
        int groupSize; 
        int square;

        // We'll go through every group
        for(int g = 1; g <= numOfGroups; g++){
            // We'll ask and get the size of the group we're working with
            groupSize = askAndGetSizeOfGroup(sc, g);
            // Now, we'll simply ask which square belong to the group (we'll repeat it "groupSize" times)
            for(int i = 0; i < groupSize; i++){
                // We'll ask and get the square we want to put in the group we're working with (the function already does the verification that the square is between 1 and "numOfSquares")
                square = askAndGetSquare(sc, numOfSquares);
                // We'll add the square to the group we're working with
                finalGridGroups.addSquareToGroup(rowOfSquare(square, gridSize), columnOfSquare(square, gridSize), g);
            }
        }

        // After doing the groups, we're now able to send a GridGroups that MUST BE VERIFIED (because it might have empty groups, squares in no group...)
        return finalGridGroups;

    }

    /**
     * Asks the user for the number of groups and ensures it is within the valid range.
     *
     * This method prompts the user to input the number of groups, then verifies if the input is valid.
     * The valid number of groups must be between 1 and the total number of squares in the grid.
     *
     * @param sc The Scanner object used to get the user input.
     * @param numOfSquares The total number of squares in the grid, used to define the valid range for the number of groups.
     * @requires {@code sc != null && numOfSquares > 0}
     * @ensures {@code \result >= 1 && \result <= numOfSquares}
     * @return The valid number of groups provided by the user.
     */
    private static int askAndGetNumOfGroups(Scanner sc, int numOfSquares){
        
        // We'll print the text for the user
        System.out.println("Número de grupos?");
        // We store the number of groups through the Scanner "sc"
        int numOfGroups = sc.nextInt();

        // We must verify if the number of groups isn't between 1 and the number of squares (isn't valid)
        while (numOfGroups < 1 || numOfGroups > numOfSquares){
            // If the number of groups isn't valid, we warn the user and define the number again
            System.out.println("Valor invalido. Tem de estar entre 1 e " + numOfSquares + ".");
            // This time, we mustn't ask for the number of groups, instead we'll expect the user to directly put it (following the instructions of this project)
            numOfGroups = sc.nextInt();
        }
        return numOfGroups; // We finally return the verified number of groups
    }

    /**
     * Asks the user for the size of a specified group.
     *
     * This method prompts the user to input the size of a group and returns the value.
     *
     * @param sc The Scanner object used to get the user input.
     * @param g The group number for which the size is requested.
     * @requires {@code sc != null && g > 0}
     * @ensures {@code \result > 0}
     * @return The size of the specified group provided by the user.
     */
    private static int askAndGetSizeOfGroup(Scanner sc, int g){

        // We'll print the text for the user
        System.out.print("Tamanho do grupo " + g + "? ");
        // We store the group size through the Scanner "sc"
        int groupSize = sc.nextInt();

        return groupSize; // We finally return the size of the group "g"
    }

    /**
     * Asks the user for a square number and ensures it is within the valid range.
     *
     * This method prompts the user to input the number of a square, then verifies if the input is valid.
     * The valid square number must be between 1 and the total number of squares in the grid.
     *
     * @param sc The Scanner object used to get the user input.
     * @param numOfSquares The total number of squares in the grid, used to define the valid range for the square number.
     * @requires {@code sc != null && numOfSquares > 0}
     * @ensures {@code \result >= 1 && \result <= numOfSquares}
     * @return The valid square number provided by the user.
     */
    private static int askAndGetSquare(Scanner sc, int numOfSquares){

        // We'll print the text for the user
        System.out.print("Casa? ");
        // We store the group size through the Scanner "sc"
        int square = sc.nextInt();

        // We must verify if the square isn't between 1 and the number of squares (isn't valid)
        while(square < 1 || square > numOfSquares) {
            // If the square isn't valid, we warn the user and define the square again
            System.out.println("Valor invalido. Tem de estar entre 1 e " + numOfSquares + ".");
            // This time, we mustn't ask for the square, instead we'll expect the user to directly put it (following the instructions of this project)
            square = sc.nextInt();
        }

        return square; // We finally return the verified square
    }

    /**
     * Returns a built-in SumdokuGrid for a specified size.
     *
     * This method provides a pre-defined grid for a given size previously stored. 
     * For any other size, it returns null.
     *
     * @param size The size of the grid
     * @requires {@code size > 0}
     * @return A SumdokuGrid object with predefined values, or null if the size is not defined.
     */
    public static SumdokuGrid getBuiltInGrid(int size){

        // If we have a default grid created for a specific size, then we'll return it. Else, we'll return null as a representation of "not valid".
        if (size == 3) {
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

            /*--------------------------------------------------------------------NOTE------------------------------------------------------------------------
            |-> We could definitely use a StringBuilder as an imitation of an array and do this in a more legible way (storing the differents values in it)...|
            |-> Although this would mean a worse efficiency of the code (it would take longer time to execute)                                                |
            |-> We could only do this efficient with an array                                                                                                 |
            |-> But it's prohibited in this project, so we will mantain the code as it is right now                                                           |
            -------------------------------------------------------------------------------------------------------------------------------------------------*/

            return grid; // We finally return the grid

        } else {
            return null; // We end up returning null

        }
    }

    /**
     * Returns a built-in GridGroups for a specified size.
     *
     * This method provides a pre-defined groups grid for a given size previously stored. 
     * For any other size, it returns null.
     *
     * @param size The size of the grid
     * @requires {@code size > 0}
     * @return A SumdokuGrid object with predefined values, or null if the size is not defined.
     */
    public static GridGroups getBuiltInGroups(int size){
        // If we have default groups created for a specific size, then we'll return it. Else, we'll return null as a representation of "not valid".
        if (size == 3) {
            GridGroups group = new GridGroups(3, 5);

            group.addSquareToGroup(1, 1, 1);
            group.addSquareToGroup(1, 2, 1);
            group.addSquareToGroup(1, 3, 3);
            group.addSquareToGroup(2, 1, 1);
            group.addSquareToGroup(2, 2, 2);
            group.addSquareToGroup(2, 3, 3);
            group.addSquareToGroup(3, 1, 4);
            group.addSquareToGroup(3, 2, 4);
            group.addSquareToGroup(3, 3, 5);

            /*--------------------------------------------------------------------NOTE------------------------------------------------------------------------
            |-> We could definitely use a StringBuilder as an imitation of an array and do this in a more legible way (storing the differents values in it)...|
            |-> Although this would mean a worse efficiency of the code (it would take longer time to execute)                                                |
            |-> We could only do this efficient with an array                                                                                                 |
            |-> But it's prohibited in this project, so we will mantain the code as it is right now                                                           |
            -------------------------------------------------------------------------------------------------------------------------------------------------*/

            return group; // We finally return the group

        } else {
            return null; // We end up returning null

        }
    }

    /**
     * Checks if the puzzle has been solved correctly.
     *
     * This method compares each square's value in two SumdokuGrid objects: the played grid and the original grid. 
     * If all the values match between the two grids, it returns true, indicating the puzzle is solved. Otherwise, 
     * it returns false.
     *
     * @param playedGrid The grid representing the player's progress.
     * @param grid The original grid that defines the correct solution.
     * @requires {@code playedGrid != null && grid != null && definesPuzzle(playedGrid.size(), grid.size())}
     * @return true if the player's grid matches the solution, false otherwise.
     */
    public static boolean puzzleSolved(SumdokuGrid playedGrid, SumdokuGrid grid){

        // We store the grid size in a variable in order to optimize the speed of the code
        int gridSize = grid.size(); 
        
        // We'll go through every possible square
        for(int r = 1; r <= gridSize; r++){
            for(int c = 1; c <= gridSize; c++){

                // In each square, we'll check if the value in one SumdokuGrid is different than in the other one
                if(playedGrid.value(r, c) != grid.value(r, c))
                    return false; // If the value of one square differs between the two SumdokuGrid, we'll return false
            }
        }
        return true; // If we don't find any square with a different value in both SumdokuGrid, we'll return true
    }

    /**
     * Starts a new game of Sumdoku, allowing the user to play by filling in the grid.
     *
     * This method initializes the game, displaying the puzzle clues and instructions,
     * and allows the user to make a series of attempts to solve the puzzle.
     * The user is prompted to select squares and enter values, and the game tracks 
     * the number of attempts. Once the maximum number of attempts is reached, the 
     * puzzle is checked for correctness, and a message is displayed.
     *
     * @param grid The original SumdokuGrid containing the correct solution.
     * @param groups The GridGroups object containing the group assignments for each square.
     * @param maxAttempts The maximum number of attempts allowed to solve the puzzle.
     * @param sc The Scanner object used to receive user input.
     * @requires {@code grid != null && groups != null && sc != null && maxAttempts > 0}
     * @ensures {@code \result != null} 
     */
    public static void play(SumdokuGrid grid, GridGroups groups, int maxAttempts, Scanner sc) {

        // Store the size of the grid
        int gridSize = grid.size();

        // Declare variables to store the square and value to be filled
        int square;
        int value;

        // Create a new SumdokuGrid to track the user's progress
        SumdokuGrid playedGrid = new SumdokuGrid(gridSize);

        // Display welcome message and game instructions
        System.out.println("Bem vindo ao jogo Sumdoku!\nNeste jogo a grelha tem tamanho " + gridSize + " e tens estas pistas:");
        System.out.print(groups); // Print the grid groups
        System.out.print(cluesToString(grid, groups)); // Display the clues for the puzzle
        System.out.println("Tens " + maxAttempts + " tentativas para resolver o puzzle. Boa sorte!"); // Display the maximum attempts

        // Loop a "number of attempts" times
        for (int i = 0; i < maxAttempts; i++) {

            // Ask the user to select a square and enter a value
            square = askAndGetSelectedSquare(sc, gridSize);
            value = askAndGetValue(sc, gridSize);

            // Fill the selected square in the played grid with the chosen value
            playedGrid.fill(rowOfSquare(square, gridSize), columnOfSquare(square, gridSize), value);

            // Display the current state of the played grid after the move
            System.out.print(playedGrid);             
        }

        // After all attempts, check if the puzzle is solved
        if(puzzleSolved(playedGrid, grid)){
            System.out.println("Parabens, resolveste o puzzle!"); // Display success message
        } else {
            System.out.println("Tentativas esgotadas. Tenta outra vez!"); // Display failure message
        }

    }

    /**
     * Prompts the user to select a square to fill in the grid.
     *
     * This method asks the user for a valid square number to fill. The square must be between 
     * 1 and the total number of squares in the grid. The method ensures the input is valid.
     *
     * @param sc The Scanner object used to receive user input.
     * @param gridSize The size of the grid, used to validate the selected square number.
     * @requires {@code sc != null && gridSize > 2 && gridSize < 10}
     * @ensures {@code \result >= 1 && \result <= gridSize * gridSize}
     * @return The valid square number chosen by the user.
     */
    private static int askAndGetSelectedSquare(Scanner sc, int gridSize){
        
        // Declare a variable to store the square number that the user selects
        int square;

        // Prompt the user to enter the square they want to fill
        System.out.print("Casa a preencher? ");
        square = sc.nextInt(); // Store the user input as the square number

        // Validate if the selected square is within the valid range
        while(square < 1 || square > gridSize*gridSize){
            // If the square is invalid, print an error message and ask the user again (exactly as in the examples of the project)
            System.out.println("Valor invalido. Tem de estar entre 1 e " + gridSize*gridSize + ".");
            square = sc.nextInt(); // Get a new square number from the user
        }

        // Return the valid square number
        return square;
    }

    /**
     * Prompts the user to enter a value for a selected square in the grid.
     *
     * This method asks the user for a valid value to place in a selected square. The value must 
     * be between 1 and the size of the grid. The method ensures the input is valid.
     *
     * @param sc The Scanner object used to receive user input.
     * @param gridSize The size of the grid, used to validate the value.
     * @requires {@code sc != null && gridSize > 2 && gridSize < 10}
     * @ensures {@code \result >= 1 && \result <= gridSize}
     * @return The valid value to place in the selected square.
     */
    private static int askAndGetValue(Scanner sc, int gridSize){
        
        // Declare a variable to store the value that the user wants to place in the selected square
        int value;

        // Prompt the user to enter the value to be placed in the selected square
        System.out.print("Valor a colocar? ");
        value = sc.nextInt(); // Store the user input as the value to be placed

        // Validate if the entered value is within the valid range
        while(value < 1 || value > gridSize){
            // If the value is invalid, print an error message and ask the user again (exactly as in the examples of the project)
            System.out.println("Valor invalido. Tem de estar entre 1 e " + gridSize + ".");
            value = sc.nextInt(); // Get a new value from the user
        }

        // Return the valid value entered by the user
        return value;

    }

    /*--------------------------------------------------------------------NOTE------------------------------------------------------------------------
    |-> Many times in this code, we're using while loops instead of do-while loops...                                                                |
    |-> This is because in the lines of this code, a do-while loop would innecesarily demand the machine to do a verification process twice          |
    |-> We consider a do-while loop would be way more useful in another situations that didn't take place in this code                               |
    -------------------------------------------------------------------------------------------------------------------------------------------------*/

}