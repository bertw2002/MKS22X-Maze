import java.util.*;
import java.io.*;
public class Maze{


    private char[][] maze;
    private boolean animate;//false by default
    private int Erow;
    private int Ecol;
    private int Scol;
    private int Srow;
    private int numAts;
    private int[] dirx = {1, -1, 0, 0};
    private int[] diry = {0, 0, -1, 1};
    /*Constructor loads a maze text file, and sets animate to false by default.

      1. The file contains a rectangular ascii maze, made with the following 4 characters:
      '#' - Walls - locations that cannot be moved onto
      ' ' - Empty Space - locations that can be moved onto
      'E' - the location of the goal (exactly 1 per file)

      'S' - the location of the start(exactly 1 per file)

      2. The maze has a border of '#' around the edges. So you don't have to check for out of bounds!


      3. When the file is not found OR the file is invalid (not exactly 1 E and 1 S) then:

         throw a FileNotFoundException or IllegalStateException

    */

    public Maze(String filename) throws FileNotFoundException{
      animate = false;
      File fname = new File(filename);
      Scanner scanner = new Scanner(fname);
      int row = 0;
      int col = 0;
      int countE = 0;
      int countS = 0;
      numAts = 0;
      String nextline;
      while (scanner.hasNextLine()){
        nextline = scanner.nextLine();
        col = nextline.length();
        row++;
      }
      maze = new char[row][col];
      int x = 0;
      Scanner scanner2 = new Scanner(fname);
      while (scanner2.hasNextLine()){
        nextline = scanner2.nextLine();
        for (int y = 0; y < col; y++){
          maze[x][y] = nextline.charAt(y);
          if (maze[x][y] == 'E'){
            countE++;
            Ecol = y;
            Erow = x;
          }
          if (maze[x][y] == 'S'){
             countS++;
             Scol = y;
             Srow = x;
          }
        }
        x++;
      }
      if (countE != 1) throw new IllegalStateException();
      if (countS != 1) throw new IllegalStateException();
    }

    public String toString(){
      String str = "";
      for (int x = 0; x < maze.length; x++){
        for (int y = 0; y < maze[0].length; y++){
          str += maze[x][y];
        }
        str += "\n";
      }
      return str;
    }


    private void wait(int millis){
         try {
             Thread.sleep(millis);
         }
         catch (InterruptedException e) {
         }
     }


    public void setAnimate(boolean b){

        animate = b;

    }


    public void clearTerminal(){

        //erase terminal, go to top left of screen.

        System.out.println("\033[2J\033[1;1H");

    }



    /*Wrapper Solve Function returns the helper function

      Note the helper function has the same name, but different parameters.
      Since the constructor exits when the file is not found or is missing an E or S, we can assume it exists.

    */
    public int solve(){
      return solve(Srow, Scol);
    }
    /*
      Recursive Solve function:

      A solved maze has a path marked with '@' from S to E.

      Returns the number of @ symbols from S to E when the maze is solved,
      Returns -1 when the maze has no solution.


      Postcondition:

        The S is replaced with '@' but the 'E' is not.

        All visited spots that were not part of the solution are changed to '.'

        All visited spots that are part of the solution are changed to '@'
    */
    private int solve(int row, int col){ //you can add more parameters since this is private
      //automatic animation! You are welcome. thanks
      if(animate){

          clearTerminal();
          System.out.println(this);

          wait(20);
      }
      int moves = 0;
      if (maze[row][col]=='E'){
        for (int r = 0; r < maze.length; r++){
          for (int c = 0; c < maze[0].length; c++){
            if (maze[r][c] == '@'){
              moves++;
            }
          }
        }
      }
      for (int count= 0; count < dirx.length; count++){
        char after = maze[row + dirx[count]][col+diry[count]];
        if (after == 'E'){
          for (int r = 0; r < maze.length; r++){
            for (int c = 0; c < maze[0].length; c++){
              if (maze[r][c] == '@'){
                moves++;
              }
            }
          }
          return moves;
        }
        else if (after == ' '){
          maze[dirx[count] + row][diry[count] + col] = '@';
          moves++;
          return solve(dirx[count] + row, diry[count] + col);
        }
        else{
          if (count >= 3){
            for (int x = 0; x < dirx.length; x++){
              char before = maze[row + dirx[x]][col + diry[x]];
              if (before == 'E'){
                for (int r = 0; r < maze.length; r++){
                  for (int c = 0; c < maze[0].length; c++){
                    if (maze[r][c] == '@'){
                      moves++;
                    }
                  }
                }
                return moves;
              }
              else if (before == '@'){
                moves--;

                maze[row][col] = '.';

                return solve(dirx[x] + row, diry[x] + col);
              }
            }
          }
        }
      }
      return -1;
    }


}
