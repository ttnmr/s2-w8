// Forest.java
// Stores metadata and grid for a forest

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Forest {
    private String name;
    private String type;
    private String vegetation;
    private double burnRate;
    private int initialTreeCount;
    private int burnDuration;
    private Tree[][] grid;

    public Forest(String name, String type, String vegetation, double burnRate, int burnDuration, int initialTreeCount, int gridRows, int gridCols) {
        this.grid = new Tree[gridRows][gridCols]; //TODO; initialize grid to using gridRows and gridCols
        this.name = name;
        this.type = type;
        this.vegetation = vegetation;
        this.burnRate = burnRate;
        this.initialTreeCount = initialTreeCount;
        this.burnDuration = burnDuration;
        this.initializeForest(); //This is going to populate the grid with trees.
    }
    
    public void initializeForest(){
        // Step 1: Determine how many cells in the grid should start as TREE
        //         based on initialTreeCount and the grid dimensions.
        // Step 2: Fill the grid with EMPTY trees first so every cell has a Tree object.
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                grid[r][c] = new Tree(0);
            }
        }
        // Step 3: Randomly place TREE states until the target initial tree count is reached.
        for (int i = 0; i < initialTreeCount; i++) {
            int r1 = (int) (Math.random()*6);
            int r2 = (int) (Math.random()*6);
            if (grid[r1][r2].getState() == 0) {
                grid[r1][r2] = new Tree(1);
            }
        }
        // Step 4: Reset any burn-time/state tracking needed for a fresh simulation start.

    }

    public Tree[][] deepCopy(){
        // Step 1: Create a new Tree[][] with the same dimensions as grid.
        Tree[][] grid2 = grid;
        // Step 2: Loop through every cell in grid.
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
        // Step 3: Copy each Tree by value (state and burnTime), not by reference.
                grid2[r][c] = new Tree(grid[r][c]);
            }
        }
        // Step 4: Return the copied Tree[][].
        return grid2;
    }

    public void spreadFire() {
        // Step 1: Call deepCopy() to create a separate "next step" grid so updates happen simultaneously.
        Tree[][] grid2 = deepCopy();
        // Step 2: For each BURNING tree, check valid neighbors (up/down/left/right).
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c].getState() == 2) {
        // Step 3: For each neighboring TREE, ignite it with probability burnRate.
                    if (r < grid.length-1 && Math.random() > burnRate) {
                        grid2[r+1][c].setState(2);
                    }
                    if (r > 0 && Math.random() > burnRate) {
                        grid2[r-1][c].setState(2);
                    }
                    if (c < grid[0].length-1 && Math.random() > burnRate) {
                        grid2[r][c+1].setState(2);
                    }
                    if (c < 0 && Math.random() > burnRate) {
                        grid2[r][c-1].setState(2);
                    }
        // Step 4: Increase burn time of currently burning trees.
                    grid2[r][c].setBurnTime(grid[r][c].getBurnTime() + 1);
        // Step 5: Turn trees to EMPTY once burn time reaches burnDuration.
                    if (grid2[r][c].getBurnTime() == burnDuration) {
                        grid2[r][c].setState(0);
                    }
                }
            }
        }
        // Step 6: Replace the current grid with the updated next-step grid.
        grid = grid2;
    }

    public double percentBurned() {
        // Step 1: Count how many trees have burned out (commonly represented as EMPTY after burning).
        int burnedCount = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c].getState() == 0 && grid[r][c].getBurnTime() == burnDuration) {
                    burnedCount++;
                }
            }
        }
        // Step 2: Compute and return (burnedCount * 100.0) / initialTreeCount as a percentage.
        // Step 3: Guard against divide-by-zero if the initialTreeCount is 0.
        if (initialTreeCount != 0) return (burnedCount*100.0)/initialTreeCount;
        else return -1;
    }

    public void saveGridSnapshotToFile() {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return;
        }

        String safeName = name == null ? "forest" : name.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
        if (safeName.isEmpty()) {
            safeName = "forest";
        }
        String fileName = safeName + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("=== GRID SNAPSHOT ===");
            writer.newLine();
            for (Tree[] row : grid) {
                StringBuilder line = new StringBuilder();
                for (Tree tree : row) {
                    char cell;
                    if (tree == null) {
                        cell = '?';
                    } else if (tree.getState() == Tree.EMPTY) {
                        cell = '.';
                    } else if (tree.getState() == Tree.TREE) {
                        cell = 'T';
                    } else if (tree.getState() == Tree.BURNING) {
                        cell = 'F';
                    } else {
                        cell = '?';
                    }
                    line.append(cell);
                }
                writer.write(line.toString());
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write grid snapshot to file: " + fileName, e);
        }
    }

    public void setGrid(Tree[][] grid) {
        this.grid = grid;
    }
    public Tree[][] getGrid() {
        return grid;
    }
    public double getBurnRate() { return burnRate; }
    public int getBurnDuration() { return burnDuration; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getVegetation() { return vegetation; }
    public int getInitialTreeCount() { return initialTreeCount; }
}
