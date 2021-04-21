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
   private int boardLook1[][];

   // ----- ChessBot Constructors
   public ChessBot() {
      this.eval = 0;
      this.inspectedSpotValue = 0;
      this.botIsActive = false;
      this.boardLook1 = new int[8][8];
   }

   // ----- ChessBot setters and getters
   public void setLogic(ChessLogic logic) {
      this.logic = logic;
   }

   public void setScreen(ChessScreen screen) {
      this.screen = screen;
   }

   // ----- ChessBot General Methods
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
   public String botLook() {
      for (int i = 0; i < 8; i++) {
         for (int r = 0; r < 8; r++) {
            inspectedSpotValue = logic.getSpot(r, i);
            switch (inspectedSpotValue) {

            }

         }
      }


      return "blop";
   }
}