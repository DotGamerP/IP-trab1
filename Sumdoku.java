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

        
    }

    private static int askAndGetGridSize(Scanner sc){
        
        // We'll ask the grid size
        System.out.print("Tamanho da grelha? ");
        // We return the input from the user
        return sc.nextInt(); 
    }

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

    public static boolean isValidForPuzzle(SumdokuGrid grid){

        int gridSize = grid.size();
        int rowRepeated = 0;

        if(grid == null || gridSize <= 0)
            return false;

        
        // Para verificar se o número de cada posição está repetida em cada linha
        for(int r = 1; r <= gridSize; r++) {

            for(int c = 1; c < gridSize; c++) {

                int value = grid.value(r, c);

                if(grid.value(r, c) < 1 || grid.value(r, c) > gridSize)
                    return false;

                if(value == grid.value(r, c + 1))
                    return false;
            }
        }

        // Para verificar se o número de cada posição está repetida em cada coluna
        for(int c = 1; c <= gridSize; c++) {

            for(int r = 1; r < gridSize; r++) {

                int value = grid.value(r, c);

                if(value == grid.value(r + 1, c))
                    return false;

            }
        }
        
        // Caso nenhuma destas condições sejam acionadas então o puzzle é válido
        return true;
    }

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

    public static boolean definesPuzzle(SumdokuGrid grid, GridGroups groups){
        
        int size = grid.size();
        int gridSize = groups.gridSize();
        boolean validPuzzle = gridSize != size || groups.numberOfGroups() < 2;

        if(validPuzzle)
            return false;

        else
            return true;
    }

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

    public static SumdokuGrid readGrid(int size, Scanner leitor){

        int valueOfSquare = 0;
        int square = 0;
        System.out.print("Leitura do puzzle.\nTamanho da grelha? ");
        size = leitor.nextInt();
        SumdokuGrid finalSumdokuGrid = new SumdokuGrid(size);
        int numOfSquares = size * size;

        for(int row = 1; row <= size; row++) {

            for(int col = 1; col <= size; col++) {

            square++;
            System.out.println("Casa " + square + ": ");
            valueOfSquare = leitor.nextInt();
            finalSumdokuGrid.addSquareToGroup(rowOfSquare(square, size), columnOfSquare(square, size), g);

            }
        }

        return finalSumdokuGrid;
    }

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

    private static int askAndGetSizeOfGroup(Scanner sc, int g){

        // We'll print the text for the user
        System.out.print("Tamanho do grupo " + g + "? ");
        // We store the group size through the Scanner "sc"
        int groupSize = sc.nextInt();

        return groupSize; // We finally return the size of the group "g"
    }

    private static int askAndGetSquare(Scanner sc, int numOfSquares){

        // We'll print the text for the user
        System.out.println("Casa? ");
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
            |-> But it's prohibited in this project, so we will mantain the code as it is right now                                                          |
            -------------------------------------------------------------------------------------------------------------------------------------------------*/

            return grid; // We finally return the grid

        } else {
            return null; // We end up returning null

        }
    }

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
            |-> But it's prohibited in this project, so we will mantain the code as it is right now                                                          |
            -------------------------------------------------------------------------------------------------------------------------------------------------*/

            return group; // We finally return the group

        } else {
            return null; // We end up returning null

        }
    }

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

    public static void play(SumdokuGrid grid, GridGroups groups, int maxAttempts, Scanner sc){
        //
    }
}
