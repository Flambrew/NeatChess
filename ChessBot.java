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
   private int selectedEval;
   private int lookBoard[][];
   private int heldVarA; // <\
   private int heldVarB; // <| store various values in places where value need to be stored
   private int heldVarC; // </
   private int heldPiece;
   private int kingX; //////// <\
   private int kingY; //////// <| these are used in the checkCheck method
   private boolean inCheck; // </

   // ----- ChessBot Constructors
   public ChessBot() {
      this.eval = 0;
      this.inspectedSpotValue = 0;
      this.botIsActive = false;
      this.lookBoard = new int[8][8];
      this.selectedMove = "";
      this.selectedEval = 0;
      this.heldVarA = 10;
      this.heldVarB = 10;
      this.heldVarC = 10;
      this.heldPiece = 10;
      this.kingX = 10;
      this.kingY = 10;
      this.inCheck = false;
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
               pawnLook(i, r);
               break;
            case 2:// call a bishoplook
               bishopLook(i, r);
               break;
            case 3:// call a knightlook
               knightLook(i, r);
               break;
            case 4:// call a rooklook
               rookLook(i, r);
               break;
            case 5:// call a queenlook
               queenLook(i, r);
               break;
            case 6:// call a kinglook
               kingLook(i, r);
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

   public void pawnLook(int i, int r) {
      if (logic.getTurn() == 1) {
         if (getSpot(i - 1, r - 1) < 0) {
            basicMove(i, r, i-1, r-1);
            if (logic.getTurn * selectedEval < eval()) {
               selectedMove = "";
            }
         }
      } else {
      }
   }

   public void bishopLook(int i, int r) {

   }

   public void knightLook(int i, int r) {

   }

   public void rookLook(int i, int r) {
      if (logic.getTurn() == 1) {

      } else {

      }
   }

   public void queenLook(int i, int r) {

   }

   public void kingLook(int i, int r) {
      if (logic.getTurn() == 1) {

      } else {

      }
   }

   public void basicMove(int a, int b, int x, int y) {
      heldPiece = getSpot(y, x); // used to store the value in the spot being overwritten
      setSpot(b, a, 0);
      setSpot(y, x, screen.getSelected());
      logic.sleep(125);
      if (checkCheck() == true) { // if you end up in check after everything goes down
         setSpot(y, x, heldPiece); // <<< this replaces the spot with the held piece
         setSpot(b, a, screen.getSelected());
         System.out.println("You're in check!");
      } else {

      }
      heldVarA = 10; // putting the variable back to its default
   }
// ----- ----- Checks whether a king is in check - I wish i didn't have to copy this but I did.
   public boolean checkCheck() { // This is the dumbest method name ever, and I love it.
      inCheck = false;
      for (int i = 0; i < 8; i++) { // these for loops find the king's position
         for (int r = 0; r < 8; r++) {
            if (logic.getTurn() * getSpot(i, r) == 6) {
               kingX = r;
               kingY = i;
               break;
            }
         }
      }
      logic.sleep(100);
      // first 4 ifs look for rook/queen attacks
      for (int i = 1; i < kingX; i++) { // left
         if (logic.getTurn() * getSpot(kingY, kingX - i) == -4 || logic.getTurn() * getSpot(kingY, kingX - i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY, kingX - i) != 0) {
            break;
         }
      }
      for (int i = 1; i < 7 - kingX; i++) { // right
         if (logic.getTurn() * getSpot(kingY, kingX + i) == -4 || logic.getTurn() * getSpot(kingY, kingX + i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY, kingX + i) != 0) {
            break;
         }
      }
      for (int i = 1; i < kingY; i++) { // up
         if (logic.getTurn() * getSpot(kingY - i, kingX) == -4 || logic.getTurn() * getSpot(kingY - i, kingX) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY - i, kingX) != 0) {
            break;
         }
      }
      for (int i = 1; i < 7 - kingY; i++) { // down
         if (logic.getTurn() * getSpot(kingY + i, kingX) == -4 || logic.getTurn() * getSpot(kingY + i, kingX) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY + i, kingX) != 0) {
            break;
         }
      }
      // next 4 ifs look for bishop/queen attacks
      for (int i = 1; i < kingX && i < kingY; i++) { // up/right
         if (logic.getTurn() * getSpot(kingY - i, kingX - i) == -2 || logic.getTurn() * getSpot(kingY - i, kingX - i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY - i, kingX - i) != 0) {
            break;
         }
      }
      for (int i = 1; i < 7 - kingX && i < kingY; i++) { // up/left
         if (logic.getTurn() * getSpot(kingY - i, kingX + i) == -2 || logic.getTurn() * getSpot(kingY - i, kingX + i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY - i, kingX + i) != 0) {
            break;
         }
      }
      for (int i = 1; i < 7 - kingX && i < 7 - kingY; i++) { // down/left
         if (logic.getTurn() * getSpot(kingY + i, kingX + i) == -2 || logic.getTurn() * getSpot(kingY + i, kingX + i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY + i, kingX + i) != 0) {
            break;
         }
      }
      for (int i = 1; i < kingX && i < 7 - kingY; i++) { // down/right
         if (logic.getTurn() * getSpot(kingY + i, kingX - i) == -2 || logic.getTurn() * getSpot(kingY + i, kingX - i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY + i, kingX - i) != 0) {
            break;
         }
      }
      // the next if checks for knight checks
      try {
         if (logic.getTurn() * getSpot(kingY - 2, kingX - 1) == -3 || logic.getTurn() * getSpot(kingY - 2, kingX + 1) == -3
               || logic.getTurn() * getSpot(kingY + 2, kingX - 1) == -3 || logic.getTurn() * getSpot(kingY + 2, kingX + 1) == -3
               || logic.getTurn() * getSpot(kingY - 1, kingX - 2) == -3 || logic.getTurn() * getSpot(kingY + 1, kingX - 2) == -3
               || logic.getTurn() * getSpot(kingY - 1, kingX + 2) == -3 || logic.getTurn() * getSpot(kingY + 1, kingX + 2) == -3) {
            inCheck = true;
         }
      } catch (Exception e) {
      }
      // the next if checks for pawn checks
      try {
         if (logic.getTurn() * getSpot(kingY - logic.getTurn(), kingX - 1) == -1
               || logic.getTurn() * getSpot(kingY - logic.getTurn(), kingX + 1) == -1) {
            inCheck = true;
         }
      } catch (Exception e) {
      }
      return inCheck;
   }

}