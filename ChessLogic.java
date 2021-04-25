/* Andrew Matherne - Hn. Adv. Programming Final Project
 * Chess Logic
 */

import javax.swing.JOptionPane;

public class ChessLogic {

   // ----- ChessLogic Fields
   ChessScreen screen;
   ChessBot bot;
   private int board[][]; // <- the board
   private int turnCurrent; // <- whose turn it is
   private String turnString; // <- whose turn it is but a string for outputting
   private int heldPiece; // <- piece value held in the basicmove func
   private int heldVarA; // <\
   private int heldVarB; // <| store various values in places where value need to be stored
   private int heldVarC; // </
   private int storedA; // <\
   private int storedB; // <| These are used in gameloop to
   private int storedX; // <| hold the start and end coords
   private int storedY; // </
   private int storedPiece; // <- this is used in gameloop to store a piece
   private boolean gameRunning; // <- this determines if the game is still going in the gameloop
   private boolean chosen; ///// <\ these are used in selection
   private int whiteEnPassLoc; // x value of the passantable white pawn
   private int blackEnPassLoc; // x value of the passantable black pawn
   private boolean whiteRookMovedL; // <\
   private boolean whiteRookMovedR; // <|
   private boolean whiteKingMoved; /// <| These are used to keep track of which rooks
   private boolean blackRookMovedL; // <| and kings have moved for castling
   private boolean blackRookMovedR; // <|
   private boolean blackKingMoved; /// </
   private boolean castleWorks; // <- this is used in making sure a castle move works
   private String history; // <- this string stores the full game move history
   private boolean promotionTrue; // <- this is used in the promotion checking
   private int promoPawnX; // <- x of the promotion pawn
   private boolean promotionRunning; // <- in a while loop regarding promotion
   private int kingX; //////// <\
   private int kingY; //////// <| these are used in the checkCheck method
   private boolean inCheck; // </
   private int winner; // this is who won
   private int historyLineCounter; // how many lines long is history

   private String message; // <\ used with the
   private int message2; //// </ joptionpane

   // ----- ChessLogic Constructors
   public ChessLogic(ChessScreen screen, ChessBot bot) {
      this.screen = screen;
      this.bot = bot;
      this.board = new int[8][8];
      this.turnCurrent = 1;
      this.turnString = "";
      this.heldPiece = 10;
      this.heldVarA = 10;
      this.heldVarB = 10;
      this.heldVarC = 10;
      this.storedA = 10;
      this.storedB = 10;
      this.storedX = 10;
      this.storedY = 10;
      this.storedPiece = 0;
      this.gameRunning = true;
      this.chosen = false;
      this.whiteEnPassLoc = 10;
      this.blackEnPassLoc = 10;
      this.whiteRookMovedL = false;
      this.whiteRookMovedR = false;
      this.whiteKingMoved = false;
      this.blackRookMovedL = false;
      this.blackRookMovedR = false;
      this.blackKingMoved = false;
      this.castleWorks = true;
      this.history = "";
      this.promotionTrue = false;
      this.promoPawnX = 10;
      this.promotionRunning = true;
      this.kingX = 10;
      this.kingY = 10;
      this.winner = 0;
      this.historyLineCounter = 0;

      this.message = "";
      this.message2 = 0;
   }

   // ----- ChessLogic setters and getters
   // ----- ----- Sets or gets a spot on the board
   public void setSpot(int y, int x, int p) {
      this.board[y][x] = p;
   }

   public int getSpot(int y, int x) {
      return board[y][x];
   }

   // ----- ----- Sets or gets whose turn it is currently
   public void setTurn(int turn) {
      this.turnCurrent = turn;
   }

   public int getTurn() {
      return turnCurrent;
   }

   // ----- ----- gets if the game is running
   public boolean getRunning() {
      return gameRunning;
   }

   // ----- ----- Sets or gets who won
   public void setWinner(int w) {
      this.winner = w;
   }

   public int getWinner() {
      return winner;
   }

   // ----- ChessLogic General Methods
   // ----- ----- This sets up the board with the starting piece layout.
   public void setup() {
      for (int i = 0; i < 8; i++) {
         for (int r = 0; r < 8; r++) {
            setSpot(i, r, 0);
         }
      }
      for (int i = 0; i < 8; i++) { // Pawns
         setSpot(1, i, -1);
         setSpot(6, i, 1);
      }
      setSpot(0, 0, -4); // Rooks
      setSpot(0, 7, -4);
      setSpot(7, 0, 4);
      setSpot(7, 7, 4);
      setSpot(0, 1, -3); // Knights
      setSpot(0, 6, -3);
      setSpot(7, 1, 3);
      setSpot(7, 6, 3);
      setSpot(0, 2, -2); // Bishops
      setSpot(0, 5, -2);
      setSpot(7, 2, 2);
      setSpot(7, 5, 2);
      setSpot(0, 3, -5); // Queens
      setSpot(7, 3, 5);
      setSpot(0, 4, -6); // Kings
      setSpot(7, 4, 6);
   }

   // ----- ----- This is for my use only - it prints out the current board array
   public void printBoard(boolean isWhite) {
      for (int i = 0; i < 8; i++) {
         for (int r = 0; r < 8; r++) {
            if (isWhite) {
               System.out.print(getSpot(i, r) + "\t");
            } else {
               System.out.print(getSpot(7 - i, r) + "\t");
            }
         }
         System.out.println('\n');
      }
      System.out.println();
      screen.repaint();
   }

   // ---- ---- This is the gameloop. It runs each player's turns
   public void gameLoop(boolean isWhite) {
      setup(); // <- necessary setup
      printBoard(isWhite); // </
      while (gameRunning) {
         screen.setTileX(10); // make sure the tiles in screen are reset
         screen.setTileY(10);
         while (!chosen) { // loop until chosen is true
            sleep(10);
            storedA = 10; // reset the stored tile #'s
            storedB = 10;
            storedA = screen.getTileX(); // set them to whatever has been selected in screen
            storedB = screen.getTileY();
            storedPiece = screen.getSelected(); // store the piece in the first spot
            if (storedA != 10 && storedB != 10) { // if a, b have been set - exit the loop
               chosen = true;
            }
         }
         chosen = false; // reset for next loop
         screen.setTileX(10); // make sure the tiles in screen are reset
         screen.setTileY(10);
         while (!chosen) { // loop until chosen is true
            sleep(10);
            storedX = 10; // reset the stored tile #'s
            storedY = 10;
            storedX = screen.getTileX(); // set them to whatever has been selected in screen
            storedY = screen.getTileY();
            if (storedA != storedX && storedX != 10 && storedY != 10 || // if x, y have been set and are not
                  storedB != storedY && storedX != 10 && storedY != 10) { // exactly a, b - exit the loop
               chosen = true;
            }
         }
         chosen = false; // reset for next loop
         screen.setSelected(storedPiece); // set the selected piece to what the first spot contained
         storedPiece = 0; // reset the stored
         move(storedA, storedB, storedX, storedY); // make whatever move is specified
         printBoard(isWhite); // print out the board (for me fixing stuff)
         promotionRunning = true;
         if (promotionCheckWhite()) {
            message = JOptionPane.showInputDialog(null,
                  "What will the pawn promote to?\n(1 for bishop, 2 for knight, 3 for rook, 4 for queen)");
            message2 = Integer.parseInt(message);
            while (promotionRunning) {
               promotionRunning = false;
               switch (message2) {
               case 1:
                  setSpot(0, promoPawnX, 2);
                  break;
               case 2:
                  setSpot(0, promoPawnX, 3);
                  break;
               case 3:
                  setSpot(0, promoPawnX, 4);
                  break;
               case 4:
                  setSpot(0, promoPawnX, 5);
                  break;
               default:
                  System.out.println("try again");
                  promotionRunning = true;
               }
            }
            printBoard(isWhite);
         }
         if (promotionCheckBlack()) {
            message = JOptionPane.showInputDialog(null,
                  "What will the pawn promote to?\n(1 for bishop, 2 for knight, 3 for rook, 4 for queen)");
            message2 = Integer.parseInt(message);
            while (promotionRunning) {
               switch (message2) {
               case 1:
                  setSpot(7, promoPawnX, -2);
                  break;
               case 2:
                  setSpot(7, promoPawnX, -3);
                  break;
               case 3:
                  setSpot(7, promoPawnX, -4);
                  break;
               case 4:
                  setSpot(7, promoPawnX, -5);
                  break;
               default:
                  System.out.println("try again");
                  promotionRunning = true;
               }
            }
            printBoard(isWhite);
         }
      }
   }

   // ----- ----- Decides what piece is being moved
   public void move(int a, int b, int x, int y) {
      if (getTurn() > 0) {
         whiteEnPassLoc = 10;
         if (screen.getSelected() == 1) { // pawn
            pawnMove(a, b, x, y);
         } else if (screen.getSelected() == 2) { // bishop
            bishopMove(a, b, x, y);
         } else if (screen.getSelected() == 3) { // knight
            knightMove(a, b, x, y);
         } else if (screen.getSelected() == 4) { // rook
            rookMove(a, b, x, y);
         } else if (screen.getSelected() == 5) { // queen
            queenMove(a, b, x, y);
         } else if (screen.getSelected() == 6) { // king
            kingMove(a, b, x, y);
         }
      } else {
         blackEnPassLoc = 10;
         if (screen.getSelected() == -1) { // pawn
            pawnMove(a, b, x, y);
         } else if (screen.getSelected() == -2) { // bishop
            bishopMove(a, b, x, y);
         } else if (screen.getSelected() == -3) { // knight
            knightMove(a, b, x, y);
         } else if (screen.getSelected() == -4) { // rook
            rookMove(a, b, x, y);
         } else if (screen.getSelected() == -5) { // queen
            queenMove(a, b, x, y);
         } else if (screen.getSelected() == -6) { // king
            kingMove(a, b, x, y);
         }
      }
   }

   // ----- ----- Moves a pawn
   public void pawnMove(int a, int b, int x, int y) { // a, b are start coords; x, y are end coords
      if (getTurn() > 0) { // White's move
         if (x == a && y == b - 1 && getSpot(y, x) == 0 || x == a - 1 && y == b - 1 && getSpot(y, x) < 0
               || x == a + 1 && y == b - 1 && getSpot(y, x) < 0) { // pawn up one spot or capturing
            basicMove(a, b, x, y);
         } else if (x == a && y == b - 2 && getSpot(y, x) == 0 && getSpot(b - 1, a) == 0) { // pawn up two spots
            basicMove(a, b, x, y);
            whiteEnPassLoc = x;
         } else if (blackEnPassLoc == x && -1 * getSpot(y + 1, x) == getTurn() && b == 3 && getSpot(y, x) == 0) {
            if (x == a - 1 && y == b - 1 || x == a + 1 && y == b - 1) { // en passant
               enPassMove(a, b, x, y);
            }
         }

      } else { // Black's move
         if (x == a && y == b + 1 && getSpot(y, x) == 0 || x == a - 1 && y == b + 1 && getSpot(y, x) > 0
               || x == a + 1 && y == b + 1 && getSpot(y, x) > 0) { // pawn up one spot or capturing
            basicMove(a, b, x, y);
         } else if (x == a && y == b + 2 && getSpot(y, x) == 0 && getSpot(b + 1, a) == 0) { // pawn up two spots
            basicMove(a, b, x, y);
            blackEnPassLoc = x;
         } else if (whiteEnPassLoc == x && -1 * getSpot(y - 1, x) == getTurn() && b == 4 && getSpot(y, x) == 0) {
            if (x == a + 1 && y == b + 1 || x == a - 1 && y == b + 1) { // en passant
               enPassMove(a, b, x, y);
            }
         }
      }
   }

   // ----- ----- Moves a bishop
   public void bishopMove(int a, int b, int x, int y) { // a, b are start coords; x, y are end coords
      if (getTurn() > 0) { // White's move
         if (a == x || b == y) { // this is the shortest way I could discard this possibility
         } else if (Math.abs(((double) a - (double) x) / ((double) b - (double) y)) == 1) { // makes sure the move is
                                                                                            // diagonal
            heldVarA = a - x; // storing the distance we need to go on the x axis
            heldVarB = b - y; // same as above but y axis
            heldVarC = 0;
            if (heldVarA < 0 && heldVarB < 0) { // 4th quadrant
               for (int i = 1; i < (Math.abs(heldVarA)); i++) { // this loops through every spot between the
                  if (getSpot(b + i, a + i) == 0) { // start and destination squares
                     heldVarC++; // and adds one to a counter whenever
                  } else { // a spot is empty
                     break;
                  }
               }
               if (getSpot(y, x) <= 0) { // this checks the same thing as above
                  heldVarC++; // but about the destination square
               }
            } else if (heldVarA < 0 && heldVarB > 0) { // 1st Quadrant - For this and the next 2 blocks we're just
                                                       // copying
               for (int i = 1; i < (Math.abs(heldVarA)); i++) { // the 4th quadrant code and changing a couple things
                  if (getSpot(b - i, a + i) == 0) { // to make it check different directions
                     heldVarC++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) <= 0) {
                  heldVarC++;
               }
            } else if (heldVarA > 0 && heldVarB < 0) { // 3rd quadrant
               for (int i = 1; i < (Math.abs(heldVarA)); i++) {
                  if (getSpot(b + i, a - i) == 0) {
                     heldVarC++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) <= 0) {
                  heldVarC++;
               }
            } else if (heldVarA > 0 && heldVarB > 0) { // 2nd quadrant
               for (int i = 1; i < (Math.abs(heldVarA)); i++) {
                  if (getSpot(b - i, a - i) == 0) {
                     heldVarC++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) <= 0) {
                  heldVarC++;
               }
            }
            if (heldVarC == Math.abs(heldVarA)) { // if the amount of empty squares
               basicMove(a, b, x, y); // in the path + 1 if the
            } else { // destination is clear/an opponent is equal to
               // the distance of the start to dest. - make the move
            }
            heldVarA = 10;
            heldVarB = 10; // reset the variables
            heldVarC = 10;
         }
      } else { // Black's move (exact same crap as above with a couple <'s and >'s switched so
               // I'm not going to comment it)
         if (a == x || b == y) {
         } else if (Math.abs(((double) a - (double) x) / ((double) b - (double) y)) == 1) {
            heldVarA = a - x;
            heldVarB = b - y;
            heldVarC = 0;
            if (heldVarA < 0 && heldVarB < 0) { // 4th quadrant
               for (int i = 1; i < (Math.abs(heldVarA)); i++) {
                  if (getSpot(b + i, a + i) == 0) {
                     heldVarC++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) >= 0) {
                  heldVarC++;
               }
            } else if (heldVarA < 0 && heldVarB > 0) { // 1st Quadrant
               for (int i = 1; i < (Math.abs(heldVarA)); i++) {
                  if (getSpot(b - i, a + i) == 0) {
                     heldVarC++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) >= 0) {
                  heldVarC++;
               }
            } else if (heldVarA > 0 && heldVarB < 0) { // 3rd quadrant
               for (int i = 1; i < (Math.abs(heldVarA)); i++) {
                  if (getSpot(b + i, a - i) == 0) {
                     heldVarC++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) >= 0) {
                  heldVarC++;
               }
            } else if (heldVarA > 0 && heldVarB > 0) { // 2nd quadrant
               for (int i = 1; i < (Math.abs(heldVarA)); i++) {
                  if (getSpot(b - i, a - i) == 0) {
                     heldVarC++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) >= 0) {
                  heldVarC++;
               }
            }
            if (heldVarC == Math.abs(heldVarA)) {
               basicMove(a, b, x, y);
            }
            heldVarA = 10;
            heldVarB = 10; // reset the variables
            heldVarC = 10;
         }
      }
   }

   // ----- ----- Moves a knight
   public void knightMove(int a, int b, int x, int y) { // a, b are start coords; x, y are end coords
      if (getTurn() > 0) { // White's move (I'm marking moves here in WASD format)
         if (x == a - 1 && y == b - 2 && getSpot(y, x) <= 0 || x == a + 1 && y == b - 2 && getSpot(y, x) <= 0 || // WWA/WWD
               x == a + 2 && y == b - 1 && getSpot(y, x) <= 0 || x == a + 2 && y == b + 1 && getSpot(y, x) <= 0 || // WDD/SDD
               x == a + 1 && y == b + 2 && getSpot(y, x) <= 0 || x == a - 1 && y == b + 2 && getSpot(y, x) <= 0 || // SSD/SSA
               x == a - 2 && y == b + 1 && getSpot(y, x) <= 0 || x == a - 2 && y == b - 1 && getSpot(y, x) <= 0) { // SAA/WAA
            basicMove(a, b, x, y);
         }
      } else { // Black's move
         if (x == a - 1 && y == b - 2 && getSpot(y, x) >= 0 || x == a + 1 && y == b - 2 && getSpot(y, x) >= 0 || // WWA/WWD
               x == a + 2 && y == b - 1 && getSpot(y, x) >= 0 || x == a + 2 && y == b + 1 && getSpot(y, x) >= 0 || // WDD/SDD
               x == a + 1 && y == b + 2 && getSpot(y, x) >= 0 || x == a - 1 && y == b + 2 && getSpot(y, x) >= 0 || // SSD/SSA
               x == a - 2 && y == b + 1 && getSpot(y, x) >= 0 || x == a - 2 && y == b - 1 && getSpot(y, x) >= 0) { // SAA/WAA
            basicMove(a, b, x, y);
         }
      }
   }

   // ----- ----- Moves a rook
   public void rookMove(int a, int b, int x, int y) { // a, b are start coords; x, y are end coordsint heldVarA = 0;
      if (getTurn() > 0) { // White's Move
         if (y == b ^ x == a) { // ^ is an XOR gate - the destination must line up with one axis but not both
            heldVarA = 0;
            heldVarB = 0;
            if (y == b && x < a) { // moving left
               heldVarA = java.lang.Math.abs(x - a); // store the distance it needs to travel
               for (int i = 1; i < heldVarA; i++) { // check whether every spot between the start and finish
                  if (getSpot(b, a - i) == 0) { // is empty with a counter
                     heldVarB++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) <= 0) { // check whether the destination is empty or an opponent's piece
                  heldVarB++;
               }
            } else if (y == b && x > a) { // Moving right
               heldVarA = java.lang.Math.abs(x - a);
               for (int i = 1; i < heldVarA; i++) {
                  if (getSpot(b, a + i) == 0) {
                     heldVarB++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) <= 0) {
                  heldVarB++;
               }
            } else if (x == a && y < b) { // Moving up
               heldVarA = java.lang.Math.abs(y - b);
               for (int i = 1; i < heldVarA; i++) {
                  if (getSpot(b - i, a) == 0) {
                     heldVarB++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) <= 0) {
                  heldVarB++;
               }
            } else if (x == a && y > b) { // Moving down
               heldVarA = java.lang.Math.abs(y - b);
               for (int i = 1; i < heldVarA; i++) {
                  if (getSpot(b + i, a) == 0) {
                     heldVarB++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) <= 0) {
                  heldVarB++;
               }
            }
            if (heldVarB == heldVarA) { // if every spot has been counted as clear
               basicMove(a, b, x, y); // then execute the move
            }
            heldVarA = 10;
            heldVarB = 10;
         }
      } else { // all the same nonsense but for black's turn
         if (y == b ^ x == a) {
            heldVarA = 0;
            heldVarB = 0;
            if (y == b && x < a) {
               heldVarA = java.lang.Math.abs(x - a);
               for (int i = 1; i < heldVarA; i++) {
                  if (getSpot(b, a - i) == 0) {
                     heldVarB++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) >= 0) { // I have to repeat 60 LINES OF CODE because of 4 freaking '>'s
                  heldVarB++; // Now THAT... is a steaming load of garbage
               }
            } else if (y == b && x > a) {
               heldVarA = java.lang.Math.abs(x - a);
               for (int i = 1; i < heldVarA; i++) {
                  if (getSpot(b, a + i) == 0) {
                     heldVarB++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) >= 0) {
                  heldVarB++;
               }
            } else if (x == a && y < b) {
               heldVarA = java.lang.Math.abs(y - b);
               for (int i = 1; i < heldVarA; i++) {
                  if (getSpot(b - i, a) == 0) {
                     heldVarB++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) >= 0) {
                  heldVarB++;
               }
            } else if (x == a && y > b) {
               heldVarA = java.lang.Math.abs(y - b);
               for (int i = 1; i < heldVarA; i++) {
                  if (getSpot(b + i, a) == 0) {
                     heldVarB++;
                  } else {
                     break;
                  }
               }
               if (getSpot(y, x) >= 0) {
                  heldVarB++;
               }
            }
            if (heldVarB == heldVarA) {
               basicMove(a, b, x, y);
            }
            heldVarA = 10;
            heldVarB = 10;
         }
      }
   }

   // ----- ----- Moves a queen
   public void queenMove(int a, int b, int x, int y) { // a, b are start coords; x, y are end coords
      bishopMove(a, b, x, y); // These two lines of code bring me nothing but pure euphoria.
      rookMove(a, b, x, y); // A queen's movement can be summed up as that of a rook and bishop, right?
   } // So I just call the movement methods... for a rook and bishop!

   // ----- ----- Moves a king
   public void kingMove(int a, int b, int x, int y) { // a, b are start coords; x, y are end coords
      if (getTurn() > 0) { // White's move (This is pure recycled knight code with minor coord changes)
         if (x == a - 1 && y == b - 1 && getSpot(y, x) <= 0 || x == a && y == b - 1 && getSpot(y, x) <= 0 || // WA/W
               x == a + 1 && y == b - 1 && getSpot(y, x) <= 0 || x == a + 1 && y == b && getSpot(y, x) <= 0 || // WD/D
               x == a + 1 && y == b + 1 && getSpot(y, x) <= 0 || x == a && y == b + 1 && getSpot(y, x) <= 0 || // SD/S
               x == a - 1 && y == b + 1 && getSpot(y, x) <= 0 || x == a - 1 && y == b && getSpot(y, x) <= 0) { // SA/A
            basicMove(a, b, x, y);
         } else if (x == a - 2 && y == b && whiteRookMovedL == false && whiteKingMoved == false
               && getSpot(b, a - 1) == 0 && getSpot(b, a - 2) == 0 && getSpot(b, a - 3) == 0) {
            castleMoveLeft(a, b, x, y);
         } else if (x == a + 2 && y == b && whiteRookMovedR == false && whiteKingMoved == false
               && getSpot(b, a + 1) == 0 && getSpot(b, a + 2) == 0) {
            castleMoveRight(a, b, x, y);
         }
      } else { // Black's move
         if (x == a - 1 && y == b - 1 && getSpot(y, x) >= 0 || x == a && y == b - 1 && getSpot(y, x) >= 0 || // WA/W
               x == a + 1 && y == b - 1 && getSpot(y, x) >= 0 || x == a + 1 && y == b && getSpot(y, x) >= 0 || // WD/D
               x == a + 1 && y == b + 1 && getSpot(y, x) >= 0 || x == a && y == b + 1 && getSpot(y, x) >= 0 || // SD/S
               x == a - 1 && y == b + 1 && getSpot(y, x) >= 0 || x == a - 1 && y == b && getSpot(y, x) >= 0) { // SA/A
            basicMove(a, b, x, y);
         } else if (x == a - 2 && y == b && blackRookMovedL == false && blackKingMoved == false
               && getSpot(b, a - 1) == 0 && getSpot(b, a - 2) == 0 && getSpot(b, a - 3) == 0) {
            castleMoveLeft(a, b, x, y);
         } else if (x == a + 2 && y == b && blackRookMovedR == false && blackKingMoved == false
               && getSpot(b, a + 1) == 0 && getSpot(b, a + 2) == 0) {
            castleMoveRight(a, b, x, y);
         }
      }
   }

   // ----- ----- This empties a slot on the board and sets another.
   public void basicMove(int a, int b, int x, int y) {
      heldPiece = getSpot(y, x); // used to store the value in the spot being overwritten
      setSpot(b, a, 0);
      setSpot(y, x, screen.getSelected());
      printBoard(true);
      sleep(125);
      if (checkCheck() == true) { // if you end up in check after everything goes down
         setSpot(y, x, heldPiece); // <<< this replaces the spot with the held piece
         setSpot(b, a, screen.getSelected());
         System.out.println("You're in check!");
      } else {
         setTurn(getTurn() * -1); // if you don't end up in check - switch whose turn it is
         printTurn();
         if (getSpot(0, 0) != -4) {
            blackRookMovedL = true;
         } else if (getSpot(0, 7) != -4) {
            blackRookMovedR = true;
         } else if (getSpot(7, 0) != 4) {
            whiteRookMovedL = true;
         } else if (getSpot(7, 7) != 4) {
            whiteRookMovedR = true;
         }
         if (getSpot(0, 4) != -6) {
            blackKingMoved = true;
         } else if (getSpot(7, 4) != 6) {
            whiteKingMoved = true;
         }
         System.out.println(writeToHistory(a, b, x, y, ""));
      }
      heldVarA = 10; // putting the variable back to its default
   }

   // ----- ----- Moves an en passant-ing pawn
   public void enPassMove(int a, int b, int x, int y) {
      heldPiece = getSpot(y, x); // used to store the value in the spot being overwritten
      setSpot(b, a, 0);
      setSpot(y, x, screen.getSelected());
      setSpot(y + getTurn(), x, 0);
      sleep(125);
      if (checkCheck() == true) { // if you end up in check after everything goes down
         setSpot(y, x, heldPiece); // <<< this replaces the spot with the held piece
         setSpot(b, a, screen.getSelected());
         setSpot(y - 1, x, -1 * getTurn());
         System.out.println("You're in check!");
      } else {
         setTurn(getTurn() * -1); // if you don't end up in check - switch whose turn it is
         printTurn();
         System.out.println(writeToHistory(a, b, x, y, ""));
      }
      heldVarA = 10; // putting the variable back to its default
   }

   // ----- ----- Moves a left-castling king (and rook)
   public void castleMoveLeft(int a, int b, int x, int y) {
      castleWorks = true;
      if (checkCheck() == true) {
         System.out.println("You're in check!");
         castleWorks = false;
      }
      for (int i = 0; i < 2; i++) {
         setSpot(b, a - i - 1, screen.getSelected());
         setSpot(b, a - i, 0);
         sleep(125);
         if (checkCheck() == true) {
            setSpot(b, a - i - 1, 0);
            setSpot(b, a - i, screen.getSelected());
            System.out.println("You're in check!");
            castleWorks = false;
            break;
         }
      }
      if (castleWorks) {
         heldPiece = getSpot(b, a - 4);
         setSpot(b, a - 4, 0);
         setSpot(b, a - 1, heldPiece);
         setTurn(getTurn() * -1); // if you don't end up in check - switch whose turn it is
         printTurn();
         System.out.println(writeToHistory(a, b, x, y, "O-O"));
      }
   }

   // ----- ----- Moves a right-castling king (and rook)
   public void castleMoveRight(int a, int b, int x, int y) {
      castleWorks = true;
      if (checkCheck() == true) {
         System.out.println("You're in check!");
         castleWorks = false;
      }
      for (int i = 0; i < 2; i++) {
         setSpot(b, a + i + 1, screen.getSelected());
         setSpot(b, a + i, 0);
         if (checkCheck() == true) {
            setSpot(b, a + i + 1, 0);
            setSpot(b, a + i, screen.getSelected());
            System.out.println("You're in check!");
            castleWorks = false;
            break;
         }
      }
      if (castleWorks) {
         heldPiece = getSpot(b, a + 3);
         setSpot(b, a + 3, 0);
         setSpot(b, a + 1, heldPiece);
         setTurn(getTurn() * -1); // if you don't end up in check - switch whose turn it is
         printTurn();
         System.out.println(writeToHistory(a, b, x, y, "O-O-O"));
      }
   }

   // ----- ----- Checks for white pawn promotions
   public boolean promotionCheckWhite() {
      promotionTrue = false;
      for (int i = 0; i < 8; i++) {
         if (getSpot(0, i) == 1) {
            promoPawnX = i;
            promotionTrue = true;
         }
      }
      return promotionTrue;
   }

   // ----- ----- Checks for black pawn promotions
   public boolean promotionCheckBlack() {
      promotionTrue = false;
      for (int i = 0; i < 8; i++) {
         if (getSpot(7, i) == 1) {
            promoPawnX = i;
            promotionTrue = true;
         }
      }
      return promotionTrue;
   }

   // ----- ----- Prints whose turn it is
   public String printTurn() {
      if (getTurn() > 0) {
         System.out.println("White to move");
      } else {
         System.out.println("Black to move");
      }
      return turnString;
   }

   // ----- ----- Returns a string containing all moves made
   public String writeToHistory(int a, int b, int x, int y, String s) {
      history += (screen.getSelected() + "=" + a + b + x + y + s + " ");
      if (screen.getSelected() > 0) {
         history += " ";
      }
      if (history.length() - (historyLineCounter * 56) > 56) {
         history += "\n";
         historyLineCounter++;
      }
      return history;
   }

   // ----- ----- Checks whether a king is in check
   public boolean checkCheck() { // This is the dumbest method name ever, and I love it.
      inCheck = false;
      for (int i = 0; i < 8; i++) { // these for loops find the king's position
         for (int r = 0; r < 8; r++) {
            if (getTurn() * getSpot(i, r) == 6) {
               kingX = r;
               kingY = i;
               break;
            }
         }
      }
      sleep(100);
      // first 4 ifs look for rook/queen attacks
      for (int i = 1; i < kingX; i++) { // left
         if (getTurn() * getSpot(kingY, kingX - i) == -4 || getTurn() * getSpot(kingY, kingX - i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY, kingX - i) != 0) {
            break;
         }
      }
      for (int i = 1; i < 7 - kingX; i++) { // right
         if (getTurn() * getSpot(kingY, kingX + i) == -4 || getTurn() * getSpot(kingY, kingX + i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY, kingX + i) != 0) {
            break;
         }
      }
      for (int i = 1; i < kingY; i++) { // up
         if (getTurn() * getSpot(kingY - i, kingX) == -4 || getTurn() * getSpot(kingY - i, kingX) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY - i, kingX) != 0) {
            break;
         }
      }
      for (int i = 1; i < 7 - kingY; i++) { // down
         if (getTurn() * getSpot(kingY + i, kingX) == -4 || getTurn() * getSpot(kingY + i, kingX) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY + i, kingX) != 0) {
            break;
         }
      }
      // next 4 ifs look for bishop/queen attacks
      for (int i = 1; i < kingX && i < kingY; i++) { // up/right
         if (getTurn() * getSpot(kingY - i, kingX - i) == -2 || getTurn() * getSpot(kingY - i, kingX - i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY - i, kingX - i) != 0) {
            break;
         }
      }
      for (int i = 1; i < 7 - kingX && i < kingY; i++) { // up/left
         if (getTurn() * getSpot(kingY - i, kingX + i) == -2 || getTurn() * getSpot(kingY - i, kingX + i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY - i, kingX + i) != 0) {
            break;
         }
      }
      for (int i = 1; i < 7 - kingX && i < 7 - kingY; i++) { // down/left
         if (getTurn() * getSpot(kingY + i, kingX + i) == -2 || getTurn() * getSpot(kingY + i, kingX + i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY + i, kingX + i) != 0) {
            break;
         }
      }
      for (int i = 1; i < kingX && i < 7 - kingY; i++) { // down/right
         if (getTurn() * getSpot(kingY + i, kingX - i) == -2 || getTurn() * getSpot(kingY + i, kingX - i) == -5) {
            inCheck = true;
            break;
         } else if (getSpot(kingY + i, kingX - i) != 0) {
            break;
         }
      }
      // the next if checks for knight checks
      try {
         if (getTurn() * getSpot(kingY - 2, kingX - 1) == -3 || getTurn() * getSpot(kingY - 2, kingX + 1) == -3
               || getTurn() * getSpot(kingY + 2, kingX - 1) == -3 || getTurn() * getSpot(kingY + 2, kingX + 1) == -3
               || getTurn() * getSpot(kingY - 1, kingX - 2) == -3 || getTurn() * getSpot(kingY + 1, kingX - 2) == -3
               || getTurn() * getSpot(kingY - 1, kingX + 2) == -3 || getTurn() * getSpot(kingY + 1, kingX + 2) == -3) {
            inCheck = true;
         }
      } catch (Exception e) {
      }
      // the next if checks for pawn checks
      try {
         if (getTurn() * getSpot(kingY - getTurn(), kingX - 1) == -1
               || getTurn() * getSpot(kingY - getTurn(), kingX + 1) == -1) {
            inCheck = true;
         }
      } catch (Exception e) {
      }
      return inCheck;
   }

   // ----- ----- Checks whether a king is mated
   public boolean matecheck() {
      return false;
   }

   // ----- ----- This is to make sleeps simpler
   public void sleep(int s) {
      try {
         Thread.sleep(s);
      } catch (InterruptedException e) {
      }
   }
}
