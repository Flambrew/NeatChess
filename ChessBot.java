/* Andrew Matherne - Hn. Adv. Programming Final Project
 * Chess Bot
 */

public class ChessBot {

   // ----- ChessBot Fields
   ChessScreen screen;
   ChessLogic logic;
   private int eval;
   private int inspectedSpotValue;
   private boolean botIsActive;
   private String selectedMove;
   private int lookBoard[][];

   // ----- ChessBot Constructors
   public ChessBot() {
      this.eval = 0;
      this.inspectedSpotValue = 0;
      this.botIsActive = false;
      this.lookBoard = new int[8][8];
      this.selectedMove = "";
   }

   // ----- ChessBot setters and getters
   public void setLogic(ChessLogic logic) {
      this.logic = logic;
   }

   public void setScreen(ChessScreen screen) {
      this.screen = screen;
   }

   public void setSpot(int y, int x, int p) {
      this.lookBoard[y][x] = p;
   }

   public int getSpot(int y, int x) {
      return lookBoard[y][x];
   }

   // ----- ChessBot General Methods

   // ----- ----- This sets the look board to the current normal board
   public void setupLookBoard() {
      for (int i = 0; i < 8; i++) {
         for (int r = 0; r < 8; r++) {
            setSpot(r, i, logic.getSpot(r, i));
         }
      }
   }

   // ----- ----- This evaluates the position
   public int runEval() {
      for (int i = 0; i < 8; i++) {
         for (int r = 0; r < 8; r++) {
            switch (logic.getSpot(i, r)) {
            case -5:
               eval -= 9;
               break;
            case -4:
               eval -= 5;
               break;
            case -3:
               eval -= 3;
               break;
            case -2:
               eval -= 3;
               break;
            case -1:
               eval -= 1;
               break;
            case 1:
               eval += 1;
               break;
            case 2:
               eval += 3;
               break;
            case 3:
               eval += 3;
               break;
            case 4:
               eval += 5;
               break;
            case 5:
               eval += 9;
               break;
            }
         }
      }
      return eval;
   }

   // ----- ----- This is the method that oversees finding the bot's move
   public String botLook() {
      setupLookBoard();
      for (int i = 0; i < 8; i++) {
         for (int r = 0; r < 8; r++) {
            inspectedSpotValue = getSpot(r, i);
            switch (Math.abs(inspectedSpotValue)) {
            case 0:// nothing
               break;
            case 1:// call a pawnlook
               break;
            case 2:// call a bishoplook
               break;
            case 3:// call a knightlook
               break;
            case 4:// call a rooklook
               break;
            case 5:// call a queenlook
               break;
            case 6:// call a kinglook
               break;
            }
         }
      }
      if (botIsActive) {
         return selectedMove;
         // call a botMove or smth idk yet
      } else {
         return selectedMove;
         // checking checkmate
      }
   }
}