import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
public class ForestFire {
    public static void main(String[] args)  throws FileNotFoundException {
        int duration = 10;
        // Step 1: Read in the data file (forests.csv) and create Forest objects.
        //   - Open the CSV file.
        //   - Skip/read the header row.
        //   - Parse each line into fields and construct a Forest.
        //   - Store forests in an ArrayList: ArrayList<Forest> forests = ...
        ArrayList<Forest> forests = new ArrayList<>();
        Scanner s = new Scanner(new File("forests.csv"));
        s.nextLine();
        while (s.hasNextLine()) {
            String line = s.nextLine();
            String[] temp = line.split(",");
            Forest f = new Forest(temp[0], temp[1], temp[2],  Double.parseDouble(temp[7]), Integer.parseInt(temp[9]), Integer.parseInt(temp[8]),Integer.parseInt(temp[5]), Integer.parseInt(temp[6]));
            //0, 1, 2, 7, 9, 8, 5, 6
            forests.add(f);
        }
        // Step 2: Pick one forest to run the simulation.
        //   - Choose by index.
        //   - Start at least one burning tree to begin the fire.
        int r = (int) (Math.random() * forests.size());
        Forest f = forests.get(r);

        // Step 3: Run the simulation.
        //   - Repeat spreadFire() for a fixed number of steps (or until fire ends).
        //   - At the end, print percentBurned() and summary stats.
        //   - At the end of each simulation step, you should write the current state of the Tree[][] grid to a file

        BufferedWriter writer = new BufferedWriter(new PrintWriter("simulation-results.txt"));
        for (int i = 0; i < duration; i++) {
            // String results = "";
            // f.spreadFire();
            // try{
            // BufferedWriter writer = new BufferedWriter(new PrintWriter("simulation-results.txt"));
            // for (int r = 0; r < grid.length; r++) {
            //     for (int c = 0; c < grid[0].length; c++) {
            //         Tree[][] t = f.getGrid();
            //         results += t[r][c].getState();
            //     }
            // }
            writer.write();
            writer.close();
            } catch (IOException e) {
                System.out.println("Error");
            }
        }
        System.out.println(f.percentBurned());
        // Step 4: We will vibe code our way to visualization

    }

}
