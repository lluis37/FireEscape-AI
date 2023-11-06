import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.print.attribute.Size2DSyntax;

import java.util.HashMap;

public class Ship {

    private static final int OPEN = 0;
    private static final int FIRE = -1;
    private static final int BOT = -8;
    private static final int BUTTON = -5;

    public static int[][] generateShip(int size) {
        int[][] ship = new int[size][size];

        // ArrayList to keep track of cells which have one open neighbor
        ArrayList<Integer> oneNeighbor = new ArrayList<Integer>();
        
        // Hashtable to keep track of all cells that have been removed from ArrayList
        Hashtable<Integer, Character> visited = new Hashtable<Integer, Character>();

        // Enumerate the cells of the ship to make identification of cells with one open neighbor
        // easier
        int designator = 1;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                ship[r][c] = designator;
                designator++;
            }
        }

        //Select random interior cell to open and add its neighbors to ArrayList
        int randomR = (int) (Math.random() * (size - 3) + 1);
        int randomC = (int) (Math.random() * (size - 3) + 1);
        ship[randomR][randomC] = OPEN;
        // top neighbor
        oneNeighbor.add(ship[randomR-1][randomC]);
        // bottom neighbor
        oneNeighbor.add(ship[randomR+1][randomC]);
        // left neighbor
        oneNeighbor.add(ship[randomR][randomC-1]);
        // right neighbor
        oneNeighbor.add(ship[randomR][randomC+1]);

        // Make sure that any open cell is marked as visited
        visited.put(0, 'v');

        // Iteratively open blocked cells in the board
        while ( !oneNeighbor.isEmpty() ) {
            //printArray(ship);
            //System.out.println();
            int randomCell = (int) (Math.random() * oneNeighbor.size());
            int cellToOpen = oneNeighbor.get(randomCell);
            oneNeighbor.remove(randomCell);
            visited.put(cellToOpen, 'v');

            // search for the cellToOpen and open it
            for (int r = 0; r < size; r++) {
                int breakOut = 0;
                for (int c = 0; c < size; c++) {
                    if (ship[r][c] == cellToOpen) {
                        ship[r][c] = OPEN;

                        // add/remove top cell of newly opened cell to/from ArrayList 
                        if (r > 0) {
                            if ( oneNeighbor.contains(ship[r-1][c]) ) {
                                oneNeighbor.remove(oneNeighbor.indexOf(ship[r-1][c]));
                                visited.put(ship[r-1][c], 'v');
                            } else if ( !visited.containsKey(ship[r-1][c]) ) {
                                oneNeighbor.add(ship[r-1][c]);
                            }
                        }
                        // add/remove bottom cell of newly opened cell to/from ArrayList
                        if (r < (size - 1)) {
                            if ( oneNeighbor.contains(ship[r+1][c]) ) {
                                oneNeighbor.remove(oneNeighbor.indexOf(ship[r+1][c]));
                                visited.put(ship[r+1][c], 'v');
                            } else if ( !visited.containsKey(ship[r+1][c]) ) {
                                oneNeighbor.add(ship[r+1][c]);
                            }
                        }
                        // add/remove left cell of newly opened cell to/from ArrayList
                        if (c > 0) {
                            if ( oneNeighbor.contains(ship[r][c-1]) ) {
                                oneNeighbor.remove(oneNeighbor.indexOf(ship[r][c-1]));
                                visited.put(ship[r][c-1], 'v');
                            } else if ( !visited.containsKey(ship[r][c-1]) ) {
                                oneNeighbor.add(ship[r][c-1]);
                            }
                        }
                        // add/remove right cell of newly opened cell to/from ArrayList
                        if (c < (size - 1)) {
                            if ( oneNeighbor.contains(ship[r][c+1]) ) {
                                oneNeighbor.remove(oneNeighbor.indexOf(ship[r][c+1]));
                                visited.put(ship[r][c+1], 'v');
                            } else if ( !visited.containsKey(ship[r][c+1]) ){
                                 oneNeighbor.add(ship[r][c+1]);
                            }
                        }

                        breakOut = 1;
                        break;
                    }
                }

                if (breakOut == 1) {
                    break;
                }
            }
        }

        // Get all dead ends currently present in the ship
        ArrayList<Integer> deadEnds = new ArrayList<Integer>();
        // elementCounter helps to keep track of where the deadEnds exist in the ship (Array)
        int elementCounter = 0;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                int numOpenNeighbors = 0;
                if (ship[r][c] == OPEN) {
                    // check to see if top neighbor is open
                    if (r > 0) {
                        if (ship[r-1][c] == OPEN) {
                            numOpenNeighbors++;
                        }
                    }

                    // check to see if bottom neighbor is open
                    if (r < (size - 1)) {
                        if (ship[r+1][c] == OPEN) {
                            numOpenNeighbors++;
                        }
                    }

                    // check to see if left neighbor is open
                    if (c > 0) {
                        if (ship[r][c-1] == OPEN) {
                            numOpenNeighbors++;
                        }
                    }

                    // check to see if right neighbor is open
                    if (c < (size - 1)) {
                        if (ship[r][c+1] == OPEN) {
                            numOpenNeighbors++;
                        }
                    }

                    if (numOpenNeighbors == 1) {
                        deadEnds.add(elementCounter);
                    }
                }

                elementCounter++;
            }
        }

        // Open half of the dead ends in the ship
        int halfDeadEnds = deadEnds.size()/2;
        int numberEndsOpened = 0;
        ArrayList<Integer> blockedNeighbors = new ArrayList<Integer>();
        while (numberEndsOpened < halfDeadEnds) {
            int randomDeadEnd = (int) (Math.random() * deadEnds.size());
            int deadEndToUnblock = deadEnds.get(randomDeadEnd);
            elementCounter = 0;
            for (int r = 0; r < size; r++) {
                boolean breakout = false;
                for (int c = 0; c < size; c++) {
                    if (elementCounter == deadEndToUnblock) {
                        // now add blocked neighbors of dead end to ArrayList

                        // check if top neighbor is blocked
                        if (r > 0) {
                            if (ship[r-1][c] != OPEN) {
                                blockedNeighbors.add(ship[r-1][c]);
                            }
                        }

                        // check if bottom neighbor is blocked
                        if (r < (size - 1)) {
                            if (ship[r+1][c] != OPEN) {
                                blockedNeighbors.add(ship[r+1][c]);
                            }
                        }

                        // check if left neighbor is blocked
                        if (c > 0) {
                            if (ship[r][c-1] != OPEN) {
                                blockedNeighbors.add(ship[r][c-1]);
                            }
                        }

                        // check if right neighbor is blocked
                        if (c < (size - 1)) {
                            if (ship[r][c+1] != OPEN) {
                                blockedNeighbors.add(ship[r][c+1]);
                            }
                        }

                        // randomly choose blocked neighbor to open
                        int randomNeighbor = (int) (Math.random() * blockedNeighbors.size());
                        int neighborToOpen = blockedNeighbors.get(randomNeighbor);

                        // now find the blocked neighbor and open it

                        // check if top neighbor is neighborToOpen
                        if (r > 0) {
                            if (ship[r-1][c] == neighborToOpen) {
                                ship[r-1][c] = OPEN;
                            }
                        }

                        // check if bottom neighbor is neighborToOpen
                        if (r < (size - 1)) {
                            if (ship[r+1][c] == neighborToOpen) {
                                ship[r+1][c] = OPEN;
                            }
                        }

                        // check if left neighbor is neighborToOpen
                        if (c > 0) {
                            if (ship[r][c-1] == neighborToOpen) {
                                ship[r][c-1] = OPEN;
                            }
                        }
                        
                        // check if right neighbor is neighborToOpen
                        if (c < (size - 1)) {
                            if (ship[r][c+1] == neighborToOpen) {
                                ship[r][c+1] = OPEN;
                            }
                        }
                        deadEnds.remove(randomDeadEnd);
                        numberEndsOpened++;
                        blockedNeighbors.clear();

                        // now find all remaining deadEnds and remove any deadEnds in ArrayList which no longer exist
                        elementCounter = 0;
                        for (int row = 0; row < size; row++) {
                            for (int col = 0; col < size; col++) {
                                if (deadEnds.contains(elementCounter)) {
                                    // check if dead end is still a dead end
                                    int numOpenNeighbors = 0;

                                    // check to see if top neighbor is open
                                    if (row > 0) {
                                        if (ship[row-1][col] == OPEN) {
                                            numOpenNeighbors++;
                                        }
                                     }

                                    // check to see if bottom neighbor is open
                                    if (row < (size - 1)) {
                                        if (ship[row+1][col] == OPEN) {
                                            numOpenNeighbors++;
                                        }
                                    }

                                    // check to see if left neighbor is open
                                    if (col > 0) {
                                        if (ship[row][col-1] == OPEN) {
                                            numOpenNeighbors++;
                                        }
                                    }

                                    // check to see if right neighbor is open
                                    if (col < (size - 1)) {
                                        if (ship[row][col+1] == OPEN) {
                                         numOpenNeighbors++;
                                        }
                                    }

                                    // if numOpenNeighbors != 1, the dead end is no longer a dead end
                                    // so remove from ArrayList
                                    if (numOpenNeighbors != 1) {
                                        deadEnds.remove(deadEnds.indexOf(elementCounter));
                                        numberEndsOpened++;
                                    }
                                }

                                elementCounter++;
                            }
                        }

                        breakout = true;
                        break;
                    }

                    elementCounter++;
                }

                if (breakout) {
                    break;
                }
            }
        }

        
        return ship;
    }

    // Method to place the intial fire
    public static int placeFire(int[][] ship, HashMap<Integer, Integer> cellNTF, ArrayList<Integer> openCells) {
        int size = ship.length;

        // Choose random cell for fire
        int randomCellIndex = (int) (Math.random() * openCells.size());
        int fireCellNum = openCells.get(randomCellIndex);
        openCells.remove(randomCellIndex);

        
        // Place fire cell
        int elementCounter = 1;
        for (int r = 0; r < size; r++) {
            boolean breakout = false;
            for (int c = 0; c < size; c++) {
                if (fireCellNum == elementCounter) {
                    ship[r][c] = FIRE;

                    // Add open neighbors to HashMap and ArrayList

                    // check to see if top neighbor is open or button cell
                    if (r > 0) {
                        if (ship[r-1][c] == OPEN || ship[r-1][c] == BUTTON) {
                            cellNTF.put(elementCounter-size, 1);
                        }
                    }

                    // check to see if bottom neighbor is open or button cell
                    if (r < (size - 1)) {
                        if (ship[r+1][c] == OPEN || ship[r+1][c] == BUTTON) {
                            cellNTF.put(elementCounter+size, 1);
                        }
                    }

                    // check to see if left neighbor is open or button cell
                    if (c > 0) {
                        if (ship[r][c-1] == OPEN || ship[r][c-1] == BUTTON) {
                            cellNTF.put(elementCounter-1, 1);
                        }
                    }

                    // check to see if right neighbor is open or button cell
                    if (c < (size - 1)) {
                        if (ship[r][c+1] == OPEN || ship[r][c+1] == BUTTON) {
                            cellNTF.put(elementCounter+1, 1);
                        }
                    }

                    breakout = true;
                    break;
                }

                elementCounter++;
            }
            
            if (breakout) {
                break;
            }
        }

        return fireCellNum;
    }

    // Method to spread the fire once
    public static void spreadFire(int[][] ship, HashMap<Integer, Integer> cellNTF, double q, ArrayList<Integer> cellsOnFire) {
        // HashMap to keep track of all newly created cells next to fire before placing
        // them in cellNextToFire HashMap
        HashMap<Integer, Integer> newlyAdded = new HashMap<Integer, Integer>();
        
        // Keep track of the number of cells next to fire which you have visited
        // so that you know when to break out of loop
        int countedCNTF = 0;
        int startingCountCNTF = cellNTF.size();
        int elementCounter = 1;
        int size = ship.length;
        if ( !cellNTF.isEmpty() ) {
            for (int r = 0; r < ship.length; r++) {
                boolean breakout = false;
                for (int c = 0; c < ship.length; c++) {
                    if ( cellNTF.containsKey(elementCounter) ) {
                        countedCNTF++;
                        double k = cellNTF.get(elementCounter);
                        double probability = 1 - Math.pow( (1 - q), k);
                        double rndmNum = Math.random();

                        // check to see if cell catches fire
                        if ( rndmNum < probability ) {
                            ship[r][c] = FIRE;
                            cellNTF.remove(elementCounter);
                            cellsOnFire.add(elementCounter);

                            // check if open neighbor cells are in cellNTF / possibly add them to newlyAdded

                            // check to see if top neighbor is in cellNTF / in newlyAdded / open
                            if (r > 0) {
                                if ( cellNTF.containsKey(elementCounter-size) ) {
                                    if ( newlyAdded.containsKey(elementCounter-size) ) {
                                        int oldK = newlyAdded.get(elementCounter-size);
                                        newlyAdded.replace(elementCounter-size, oldK+1);
                                    } else {
                                        int oldK = cellNTF.get(elementCounter-size);
                                        newlyAdded.put(elementCounter-size, oldK+1);
                                    }
                                } else if ( newlyAdded.containsKey(elementCounter-size) ) {
                                    int oldK = newlyAdded.get(elementCounter-size);
                                    newlyAdded.replace(elementCounter-size, oldK+1);
                                } else if (ship[r-1][c] == OPEN || ship[r-1][c] == BUTTON) {
                                    newlyAdded.put(elementCounter-size, 1);
                                }
                            }

                            // check to see if bottom neighbor is in cellNTF / in newlyAdded / open
                            if (r < (size - 1)) {
                                if ( cellNTF.containsKey(elementCounter+size) ) {
                                    if ( newlyAdded.containsKey(elementCounter+size) ) {
                                        int oldK = newlyAdded.get(elementCounter+size);
                                        newlyAdded.replace(elementCounter+size, oldK+1);
                                    } else {
                                        int oldK = cellNTF.get(elementCounter+size);
                                        newlyAdded.put(elementCounter+size, oldK+1);
                                    }
                                } else if ( newlyAdded.containsKey(elementCounter+size) ) {
                                    int oldK = newlyAdded.get(elementCounter+size);
                                    newlyAdded.replace(elementCounter+size, oldK+1);
                                } else if (ship[r+1][c] == OPEN || ship[r+1][c] == BUTTON) {
                                    newlyAdded.put(elementCounter+size, 1);
                                }
                            }

                            // check to see if left neighbor is in cellNTF / in newlyAdded / open
                            if (c > 0) {
                                if ( cellNTF.containsKey(elementCounter-1) ) {
                                    if ( newlyAdded.containsKey(elementCounter-1) ) {
                                        int oldK = newlyAdded.get(elementCounter-1);
                                        newlyAdded.replace(elementCounter-1, oldK+1);
                                    } else {
                                        int oldK = cellNTF.get(elementCounter-1);
                                        newlyAdded.put(elementCounter-1, oldK+1);
                                    }
                                } else if ( newlyAdded.containsKey(elementCounter-1) ) {
                                    int oldK = newlyAdded.get(elementCounter-1);
                                    newlyAdded.replace(elementCounter-1, oldK+1);
                                } else if (ship[r][c-1] == OPEN || ship[r][c-1] == BUTTON) {
                                    newlyAdded.put(elementCounter-1, 1);
                                }
                            }

                            // check to see if right neighbor is in cellNTF / in newlyAdded / open
                            if (c < (size - 1)) {
                                if ( cellNTF.containsKey(elementCounter+1) ) {
                                    if ( newlyAdded.containsKey(elementCounter+1) ) {
                                        int oldK = newlyAdded.get(elementCounter+1);
                                        newlyAdded.replace(elementCounter+1, oldK+1);
                                    } else {
                                        int oldK = cellNTF.get(elementCounter+1);
                                        newlyAdded.put(elementCounter+1, oldK+1);
                                    }
                                } else if ( newlyAdded.containsKey(elementCounter+1) ) {
                                    int oldK = newlyAdded.get(elementCounter+1);
                                    newlyAdded.replace(elementCounter+1, oldK+1);
                                } else if (ship[r][c+1] == OPEN || ship[r][c+1] == BUTTON) {
                                    newlyAdded.put(elementCounter+1, 1);
                                }
                            }
                        }

                        if (countedCNTF == startingCountCNTF) {
                            breakout = true;
                            break;
                        }
                    }

                    elementCounter++;
                }

                if (breakout) {
                    break;
                }
            }
        }

        // place all newlyAdded cells next to fire in cellNTF
        cellNTF.putAll(newlyAdded);
    }

    public static int placeButton(int[][] ship, ArrayList<Integer> openCells) {
        int size = ship.length;

        // Choose random cell for button
        int randomCellIndex = (int) (Math.random() * openCells.size());
        int buttonCellNum = openCells.get(randomCellIndex);
        openCells.remove(randomCellIndex);
        
        // Place button
        int elementCounter = 1;
        for (int r = 0; r < size; r++) {
            boolean breakout = false;
            for (int c = 0; c < size; c++) {
                if (elementCounter == buttonCellNum) {
                    ship[r][c] = BUTTON;
                    breakout = true;
                    break;
                }
                elementCounter++;
            }

            if (breakout) {
                break;
            }
        }

        return buttonCellNum;
    }

    public static int placeBot(int[][] ship, ArrayList<Integer> openCells) {
        // Choose random cell for bot
        int randomCellIndex = (int) (Math.random() * openCells.size());
        int botCellNum = openCells.get(randomCellIndex);
        openCells.remove(randomCellIndex);

        // Not actually placing bot, to make moving the bot around the ship a bit easier

        return botCellNum;
    }

    public static Stack<Integer> bot1Path(int[][] ship, int botStartLocation, int buttonLocation) {
        // Keep track of parents so that you can build path to button
        HashMap<Integer, Integer> parent = new HashMap<Integer, Integer>();
        int size = ship.length;
        Queue<Integer> fringe = new LinkedList<Integer>();
        ArrayList<Integer> closedSet = new ArrayList<Integer>();

        // Perform BFS starting from the botStartLocation
        parent.put(botStartLocation, botStartLocation);
        fringe.add(botStartLocation);
        closedSet.add(botStartLocation);
        while (fringe.peek() != null) {
            int currentState = fringe.remove();

            if (currentState == buttonLocation) {
                break;
            } else {
                // find where currentState is on ship and add its valid children to fringe
                int elementCounter = 1;
                for (int r = 0; r < size; r++) {
                    boolean breakout = false;
                    for (int c = 0; c < size; c++) {
                        if (elementCounter == currentState) {
                            // check to see if top neighbor is valid child
                            if (r > 0) {
                                if ( (ship[r-1][c] == OPEN || ship[r-1][c] == BUTTON) && !closedSet.contains(elementCounter-size) ) {
                                    fringe.add(elementCounter-size);
                                    parent.put(elementCounter-size, currentState);
                                    closedSet.add(elementCounter-size);
                                }
                            }

                            // check to see if bottom neighbor is valid child
                            if (r < (size - 1)) {
                                if ( (ship[r+1][c] == OPEN || ship[r+1][c] == BUTTON) && !closedSet.contains(elementCounter+size) ) {
                                    fringe.add(elementCounter+size);
                                    parent.put(elementCounter+size, currentState);
                                    closedSet.add(elementCounter+size);
                                }
                            }

                            // check to see if left neighbor is valid child
                            if (c > 0) {
                                if ( (ship[r][c-1] == OPEN || ship[r][c-1] == BUTTON) && !closedSet.contains(elementCounter-1) ) {
                                    fringe.add(elementCounter-1);
                                    parent.put(elementCounter-1, currentState);
                                    closedSet.add(elementCounter-1);
                                }
                            }

                            // check to see if right neighbor is valid child
                            if (c < (size - 1)) {
                                if ( (ship[r][c+1] == OPEN || ship[r][c+1] == BUTTON) && !closedSet.contains(elementCounter+1) ) {
                                    fringe.add(elementCounter+1);
                                    parent.put(elementCounter+1, currentState);
                                    closedSet.add(elementCounter+1);
                                }
                            }

                            breakout = true;
                            break;
                        }

                        elementCounter++;
                    }

                    if (breakout) {
                        break;
                    }
                }
            }
        }

        // Traverse parent HashMap and create path from botStartLocation to button
        Stack<Integer> bot1P = new Stack<Integer>();
        if (parent.containsKey(buttonLocation)) {
            int traversalStart = buttonLocation;
            while (traversalStart != botStartLocation) {
                bot1P.push(traversalStart);
                traversalStart = parent.get(traversalStart);
            }
        }

        return bot1P;
    }

    public static int bot2Move(int[][] ship, int bot2Location, int buttonLocation) {
        // Keep track of parents so that you can build path to button
        HashMap<Integer, Integer> parent = new HashMap<Integer, Integer>();
        int size = ship.length;
        Queue<Integer> fringe = new LinkedList<Integer>();
        ArrayList<Integer> closedSet = new ArrayList<Integer>();

        // Perform BFS starting from bot2Location
        parent.put(bot2Location, bot2Location);
        fringe.add(bot2Location);
        closedSet.add(bot2Location);
        while (fringe.peek() != null) {
            int currentState = fringe.remove();

            if (currentState == buttonLocation) {
                break;
            } else {
                // find where currentState is on ship and add its valid children to fringe
                int elementCounter = 1;
                for (int r = 0; r < size; r++) {
                    boolean breakout = false;
                    for (int c = 0; c < size; c++) {
                        if (elementCounter == currentState) {
                            // check to see if top neighbor is valid child
                            if (r > 0) {
                                if ( (ship[r-1][c] == OPEN || ship[r-1][c] == BUTTON) && !closedSet.contains(elementCounter-size) ) {
                                    fringe.add(elementCounter-size);
                                    parent.put(elementCounter-size, currentState);
                                    closedSet.add(elementCounter-size);
                                }
                            }

                            // check to see if bottom neighbor is valid child
                            if (r < (size - 1)) {
                                if ( (ship[r+1][c] == OPEN || ship[r+1][c] == BUTTON) && !closedSet.contains(elementCounter+size) ) {
                                    fringe.add(elementCounter+size);
                                    parent.put(elementCounter+size, currentState);
                                    closedSet.add(elementCounter+size);
                                }
                            }

                            // check to see if left neighbor is valid child
                            if (c > 0) {
                                if ( (ship[r][c-1] == OPEN || ship[r][c-1] == BUTTON) && !closedSet.contains(elementCounter-1) ) {
                                    fringe.add(elementCounter-1);
                                    parent.put(elementCounter-1, currentState);
                                    closedSet.add(elementCounter-1);
                                }
                            }

                            // check to see if right neighbor is valid child
                            if (c < (size - 1)) {
                                if ( (ship[r][c+1] == OPEN || ship[r][c+1] == BUTTON) && !closedSet.contains(elementCounter+1) ) {
                                    fringe.add(elementCounter+1);
                                    parent.put(elementCounter+1, currentState);
                                    closedSet.add(elementCounter+1);
                                }
                            }

                            breakout = true;
                            break;
                        }

                        elementCounter++;
                    }

                    if (breakout) {
                        break;
                    }
                }
            }
        }

        // Traverse parent HashMap and send the next move that bot2 needs to make
        int bot2M = -10;
        if (parent.containsKey(buttonLocation)) {
            int traversalStart = buttonLocation;
            while (traversalStart != bot2Location) {
                bot2M = traversalStart;
                traversalStart = parent.get(traversalStart);
            }
        }

        return bot2M;
    }

    public static int bot3Move(int[][] ship, int bot3Location, int buttonLocation, HashMap<Integer, Integer> cellNTF3) {
        // Keep track of parents so that you can build path to button
        HashMap<Integer, Integer> parent = new HashMap<Integer, Integer>();
        int size = ship.length;
        Queue<Integer> fringe = new LinkedList<Integer>();
        ArrayList<Integer> closedSet = new ArrayList<Integer>();

        // Perform BFS starting from bot3Location
        parent.put(bot3Location, bot3Location);
        fringe.add(bot3Location);
        closedSet.add(bot3Location);
        while (fringe.peek() != null) {
            int currentState = fringe.remove();

            if (currentState == buttonLocation) {
                break;
            } else {
                // find where currentState is on ship and add its valid children to fringe
                int elementCounter = 1;
                for (int r = 0; r < size; r++) {
                    boolean breakout = false;
                    for (int c = 0; c < size; c++) {
                        if (elementCounter == currentState) {
                            // check to see if top neighbor is valid child
                            if (r > 0) {
                                if ( ( (ship[r-1][c] == OPEN && !cellNTF3.containsKey(elementCounter-size) ) || ship[r-1][c] == BUTTON) && !closedSet.contains(elementCounter-size) ) {
                                    fringe.add(elementCounter-size);
                                    parent.put(elementCounter-size, currentState);
                                    closedSet.add(elementCounter-size);
                                }
                            }

                            // check to see if bottom neighbor is valid child
                            if (r < (size - 1)) {
                                if ( ( (ship[r+1][c] == OPEN && !cellNTF3.containsKey(elementCounter+size) ) || ship[r+1][c] == BUTTON) && !closedSet.contains(elementCounter+size) ) {
                                    fringe.add(elementCounter+size);
                                    parent.put(elementCounter+size, currentState);
                                    closedSet.add(elementCounter+size);
                                }
                            }

                            // check to see if left neighbor is valid child
                            if (c > 0) {
                                if ( ( (ship[r][c-1] == OPEN && !cellNTF3.containsKey(elementCounter-1) ) || ship[r][c-1] == BUTTON) && !closedSet.contains(elementCounter-1) ) {
                                    fringe.add(elementCounter-1);
                                    parent.put(elementCounter-1, currentState);
                                    closedSet.add(elementCounter-1);
                                }
                            }

                            // check to see if right neighbor is valid child
                            if (c < (size - 1)) {
                                if ( ( (ship[r][c+1] == OPEN && !cellNTF3.containsKey(elementCounter+1) ) || ship[r][c+1] == BUTTON) && !closedSet.contains(elementCounter+1) ) {
                                    fringe.add(elementCounter+1);
                                    parent.put(elementCounter+1, currentState);
                                    closedSet.add(elementCounter+1);
                                }
                            }

                            breakout = true;
                            break;
                        }

                        elementCounter++;
                    }

                    if (breakout) {
                        break;
                    }
                }
            }
        }

        // Traverse parent HashMap and send the next move that bot3 needs to make
        int bot3M = -10;
        if (parent.containsKey(buttonLocation)) {
            int traversalStart = buttonLocation;
            while (traversalStart != bot3Location) {
                bot3M = traversalStart;
                traversalStart = parent.get(traversalStart);
            }
            return bot3M;
        } else {
            // If bot3 could not find a path avoiding the adjacent fire cells, then
            // try to find a path without trying to avoid the adjacent fire cells
            bot3M = bot2Move(ship, bot3Location, buttonLocation);
        }

        return bot3M;

    }

    public static int bot4Move(int[][] ship, int bot4Location, int buttonLocation, HashMap<Integer, Integer> cellNTF4, double q) {
        // Make a copy of the ship to simulate the spread of fire
        int[][] shipSim = copyShip(ship);

        // Make a copy of cellNTF to keep track of the cells next to fire on simulated ship
        HashMap<Integer, Integer> cellNTFSim = new HashMap<Integer, Integer>();
        cellNTFSim.putAll(cellNTF4);

        // List to make sure that bot is not set on fire during sim
        ArrayList<Integer> cellsOnFireSim = new ArrayList<Integer>();

        // Simulate the spread of fire on the simShip for two time steps
        spreadFire(shipSim, cellNTFSim, q, cellsOnFireSim);
        spreadFire(shipSim, cellNTFSim, q, cellsOnFireSim);

        int bot4M = -10;
        if ( !cellsOnFireSim.contains(bot4Location) && !cellsOnFireSim.contains(buttonLocation) ) {
            // If bot and button have not been set on fire in simulation
            // Try to find a move for the bot by running the bot on the simulated ship
            bot4M = bot3Move(shipSim, bot4Location, buttonLocation, cellNTFSim);
        }

        // If the simulation failed, or the bot couldn't find a path to the button
        // in the simlation, try to find a path to the button on the actual ship
        // by running the same algorithm bot 3 uses
        if (bot4M == -10) {
            bot4M = bot3Move(ship, bot4Location, buttonLocation, cellNTF4);
        }

        return bot4M;
    }

    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        double flammability = Double.parseDouble(args[1]);
        if (size <= 2) {
            System.out.println("Ship size must be greater than 2!");
            return;
        }

        int successB1 = 0;
        int successB2 = 0;
        int successB3 = 0;
        int successB4 = 0;

        for (int i = 1; i <= 100; i++) {
        int[][] ship = generateShip(size);

        // HashMap to keep track of all open cells with fire cells adjacent to them
        // Key is elementCounter, Value is number of adjecent fire cells
        HashMap<Integer, Integer> cellNTF = new HashMap<Integer, Integer>();

        // ArrayList to keep track of all cells that are on fire
        ArrayList<Integer> cellsOnFire = new ArrayList<Integer>();

        // Find all open cells in the ship
        ArrayList<Integer> openCells = new ArrayList<Integer>();
        int elementCounter = 1;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (ship[r][c] == OPEN) {
                    openCells.add(elementCounter);
                }

                elementCounter++;
            }
        }

        int botStartLocation = placeBot(ship, openCells);
        int bot1Location = botStartLocation;
        int bot2Location = botStartLocation;
        int bot3Location = botStartLocation;
        int bot4Location = botStartLocation;
        int buttonLocation = placeButton(ship, openCells);
        int initialFireCell = placeFire(ship, cellNTF, openCells);

        // Make 3 copies of ship and cellNTF
        int[][] ship2 = copyShip(ship);
        HashMap<Integer, Integer> cellNTF2 = new HashMap<Integer, Integer>();
        cellNTF2.putAll(cellNTF);
        int[][] ship3 = copyShip(ship);
        HashMap<Integer, Integer> cellNTF3 = new HashMap<Integer, Integer>();
        cellNTF3.putAll(cellNTF);
        int[][] ship4 = copyShip(ship);
        HashMap<Integer, Integer> cellNTF4 = new HashMap<Integer, Integer>();
        cellNTF4.putAll(cellNTF);

        cellsOnFire.add(initialFireCell);

        // Run the task for Bot1
        boolean success = true;
        Stack<Integer> bot1P = bot1Path(ship, botStartLocation, buttonLocation);
        if ( !bot1P.empty() ) {
            while (bot1P.peek() != buttonLocation) {
                bot1Location = bot1P.pop();
                spreadFire(ship, cellNTF, flammability, cellsOnFire);
            
                if ( cellsOnFire.contains(buttonLocation) || cellsOnFire.contains(bot1Location) ) {
                    success = false;
                    break;
                }
            }
        } else {
            success = false;
        }

        if (success) {
            successB1++;;
        }



        cellsOnFire.clear();
        cellsOnFire.add(initialFireCell);
        // Run the task for Bot2
        success = false;
        while ( bot2Location != buttonLocation ) {
            bot2Location = bot2Move(ship2, bot2Location, buttonLocation);
            
            if (bot2Location == -10) {
                break;
            }
            if (bot2Location == buttonLocation) {
                success = true;
                break;
            }
            spreadFire(ship2, cellNTF2, flammability, cellsOnFire);
            
            if ( cellsOnFire.contains(buttonLocation) || cellsOnFire.contains(bot2Location) ) {
                break;
            }
        }

        if (success) {
            successB2++;
        }



        cellsOnFire.clear();
        cellsOnFire.add(initialFireCell);
        // Run the task for Bot3
        success = false;
        while ( bot3Location != buttonLocation ) {
            bot3Location = bot3Move(ship3, bot3Location, buttonLocation, cellNTF3);
            
            if (bot3Location == -10) {
                break;
            }
            if (bot3Location == buttonLocation) {
                success = true;
                break;
            }
            spreadFire(ship3, cellNTF3, flammability, cellsOnFire);
            
            if ( cellsOnFire.contains(buttonLocation) || cellsOnFire.contains(bot3Location) ) {
                break;
            }
        }

        if (success) {
            successB3++;
        }



        cellsOnFire.clear();
        cellsOnFire.add(initialFireCell);
        // Run the task for Bot4
        success = false;
        while ( bot4Location != buttonLocation ) {
            bot4Location = bot4Move(ship4, bot4Location, buttonLocation, cellNTF4, flammability);
            
            if (bot4Location == -10) {
                break;
            }
            if (bot4Location == buttonLocation) {
                success = true;
                break;
            }
            spreadFire(ship4, cellNTF4, flammability, cellsOnFire);
            
            if ( cellsOnFire.contains(buttonLocation) || cellsOnFire.contains(bot4Location) ) {
                break;
            }
        }

        if (success) {
            successB4++;
        }

        }

        System.out.println("Bot 1 succeeded: " + successB1 + " times");
        System.out.println("Bot 2 succeeded: " + successB2 + " times");
        System.out.println("Bot 3 succeeded: " + successB3 + " times");
        System.out.println("Bot 4 succeeded: " + successB4 + " times");

    }

    public static void printArray(int[][] ship) {
        for (int r = 0; r < ship.length; r++) {
            for (int c = 0; c < ship.length; c++) {
                System.out.print(ship[r][c] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int[][] copyShip(int[][] ship) {
        int size = ship.length;
        int[][] shipCopy = new int[size][size];

        for(int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                shipCopy[r][c] = ship[r][c];
            }
        }

        return shipCopy;
    }
}
