/* Andrew Matherne - Hn. Adv. Programming Final Project
 * Chess Rendering
 */

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.File;

public class ChessScreen extends JComponent implements MouseListener {

   // ----- ChessScreen Fields
   private static final long serialVersionUID = 1L; // I think JGrasp automatically
   ChessBot bot; // makes IDs but I tried using VSCode for a bit and it doesn't.
   ChessLogic logic;
   JFrame frame = new JFrame();
   Container content = frame.getContentPane();
   private final Color BACKGROUND;
   private final Color OUTLINEBOX;
   private final Color BUTTON;
   private final Color DARKTILES;
   private final Color LIGHTTILES;
   private int pieceSelected; // which piece is currently selected
   private int tileX, tileY; // which tile has been selected
   private boolean waitingForRelease; // this is used in the input
   private int screenNumber; // which screen should be visible

   private BufferedImage pawnWhite;
   private BufferedImage bishopWhite;
   private BufferedImage knightWhite;
   private BufferedImage rookWhite;
   private BufferedImage queenWhite;
   private BufferedImage kingWhite;
   private BufferedImage pawnBlack;
   private BufferedImage bishopBlack;
   private BufferedImage knightBlack;
   private BufferedImage rookBlack;
   private BufferedImage queenBlack;
   private BufferedImage kingBlack;

   // ----- ChessScreen Constructors
   ChessScreen(ChessBot bot) {
      this.bot = bot;
      this.pieceSelected = 0;
      this.tileX = 10;
      this.tileY = 10;
      this.BACKGROUND = new Color(35, 39, 42);
      this.OUTLINEBOX = new Color(100, 116, 128);
      this.BUTTON = new Color(75, 87, 96);
      this.DARKTILES = new Color(184, 139, 74);
      this.LIGHTTILES = new Color(227, 193, 111);
      this.waitingForRelease = false;
      this.screenNumber = 1;
      try {
         this.pawnWhite = ImageIO.read(new File("piecePawnWhite.png"));
      } catch (IOException e) {
         System.out.println("pawnWhite could not be found");
      }
      try {
         this.bishopWhite = ImageIO.read(new File("pieceBishopWhite.png"));
      } catch (IOException e) {
         System.out.println("bishopWhite could not be found");
      }
      try {
         this.knightWhite = ImageIO.read(new File("pieceKnightWhite.png"));
      } catch (IOException e) {
         System.out.println("knightWhite could not be found");
      }
      try {
         this.rookWhite = ImageIO.read(new File("pieceRookWhite.png"));
      } catch (IOException e) {
         System.out.println("rookWhite could not be found");
      }
      try {
         this.queenWhite = ImageIO.read(new File("pieceQueenWhite.png"));
      } catch (IOException e) {
         System.out.println("queenWhite could not be found");
      }
      try {
         this.kingWhite = ImageIO.read(new File("pieceKingWhite.png"));
      } catch (IOException e) {
         System.out.println("kingWhite could not be found");
      }
      try {
         this.pawnBlack = ImageIO.read(new File("piecePawnBlack.png"));
      } catch (IOException e) {
         System.out.println("pawnBlack could not be found");
      }
      try {
         this.bishopBlack = ImageIO.read(new File("pieceBishopBlack.png"));
      } catch (IOException e) {
         System.out.println("bishopBlack could not be found");
      }
      try {
         this.knightBlack = ImageIO.read(new File("pieceKnightBlack.png"));
      } catch (IOException e) {
         System.out.println("knightBlack could not be found");
      }
      try {
         this.rookBlack = ImageIO.read(new File("pieceRookBlack.png"));
      } catch (IOException e) {
         System.out.println("rookBlack could not be found");
      }
      try {
         this.queenBlack = ImageIO.read(new File("pieceQueenBlack.png"));
      } catch (IOException e) {
         System.out.println("queenBlack could not be found");
      }
      try {
         this.kingBlack = ImageIO.read(new File("pieceKingBlack.png"));
      } catch (IOException e) {
         System.out.println("kingBlack could not be found");
      }
      setup();
   }

   // ----- ----- Sets or gets which piece is selected
   public void setSelected(int piece) {
      this.pieceSelected = piece;
   }

   public int getSelected() {
      return pieceSelected;
   }

   // ----- ----- Sets or gets tile selections
   public void setTileX(int x) {
      this.tileX = x;
   }

   public int getTileX() {
      return tileX;
   }

   public void setTileY(int y) {
      this.tileY = y;
   }

   public int getTileY() {
      return tileY;
   }

   // ----- ----- Sets the Logic object to actually being logic
   public void setLogic(ChessLogic logic) {
      this.logic = logic;
   }

   // ----- ChessScreen General Methods
   public void setup() {
      content.setBackground(BACKGROUND);
      content.add(this);
      frame.getContentPane().addMouseListener(this);

      frame.setSize(1315, 725);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   public void paintComponent(Graphics g) {
      if (screenNumber == 1) {
         g.setColor(OUTLINEBOX);
         g.fillRect(315, 264, 664, 166);
         g.setColor(BUTTON);
         g.fillRect(481, 364, 332, 51);
      } else if (screenNumber == 2) {
         for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
               int x = c * 83;
               int y = r * 83;
               if ((r + c) % 2 != 0) {
                  g.setColor(DARKTILES);
                  g.fillRect((x + 315), (y + 15), 83, 83);
               } else {
                  g.setColor(LIGHTTILES);
                  g.fillRect((x + 315), (y + 15), 83, 83);
               }
               if (logic.getSpot(r, c) == -6) {
                  blackKingDraw(g, x, y);
               } else if (logic.getSpot(r, c) == -5) {
                  blackQueenDraw(g, x, y);
               } else if (logic.getSpot(r, c) == -4) {
                  blackRookDraw(g, x, y);
               } else if (logic.getSpot(r, c) == -3) {
                  blackKnightDraw(g, x, y);
               } else if (logic.getSpot(r, c) == -2) {
                  blackBishopDraw(g, x, y);
               } else if (logic.getSpot(r, c) == -1) {
                  blackPawnDraw(g, x, y);
               } else if (logic.getSpot(r, c) == 1) {
                  whitePawnDraw(g, x, y);
               } else if (logic.getSpot(r, c) == 2) {
                  whiteBishopDraw(g, x, y);
               } else if (logic.getSpot(r, c) == 3) {
                  whiteKnightDraw(g, x, y);
               } else if (logic.getSpot(r, c) == 4) {
                  whiteRookDraw(g, x, y);
               } else if (logic.getSpot(r, c) == 5) {
                  whiteQueenDraw(g, x, y);
               } else if (logic.getSpot(r, c) == 6) {
                  whiteKingDraw(g, x, y);
               }
               g.setColor(BUTTON);
               g.fillRect(45, 25, 100, 50);
               g.fillRect(170, 25, 100, 50);
               g.fillRect(1024, 25, 100, 50);
               g.fillRect(1149, 25, 100, 50);
            }
         }
      } else if (screenNumber == 3) {
         g.setColor(OUTLINEBOX);
         g.fillRect(315, 264, 664, 166);
         g.setColor(BUTTON);
         g.fillRect(481, 364, 332, 51);
         switch (logic.getWinner()) {
         case 1:
            g.setColor(Color.WHITE);
            break;
         case 2:
            g.setColor(Color.BLACK);
            break;
         case 3:
            g.setColor(Color.GRAY);
            break;
         default:
            g.setColor(OUTLINEBOX);
         }
         g.fillRect(481, 299, 332, 51);
      }
   }

   public void blackKingDraw(Graphics g, int x, int y) {
      g.drawImage(kingBlack, x + 315, y + 15, null);
   }

   public void blackQueenDraw(Graphics g, int x, int y) {
      g.drawImage(queenBlack, x + 315, y + 15, null);
   }

   public void blackRookDraw(Graphics g, int x, int y) {
      g.drawImage(rookBlack, x + 315, y + 15, null);
   }

   public void blackKnightDraw(Graphics g, int x, int y) {
      g.drawImage(knightBlack, x + 315, y + 15, null);
   }

   public void blackBishopDraw(Graphics g, int x, int y) {
      g.drawImage(bishopBlack, x + 315, y + 15, null);
   }

   public void blackPawnDraw(Graphics g, int x, int y) {
      g.drawImage(pawnBlack, x + 315, y + 15, null);
   }

   public void whiteKingDraw(Graphics g, int x, int y) {
      g.drawImage(kingWhite, x + 315, y + 15, null);
   }

   public void whiteQueenDraw(Graphics g, int x, int y) {
      g.drawImage(queenWhite, x + 315, y + 15, null);
   }

   public void whiteRookDraw(Graphics g, int x, int y) {
      g.drawImage(rookWhite, x + 315, y + 15, null);
   }

   public void whiteKnightDraw(Graphics g, int x, int y) {
      g.drawImage(knightWhite, x + 315, y + 15, null);
   }

   public void whiteBishopDraw(Graphics g, int x, int y) {
      g.drawImage(bishopWhite, x + 315, y + 15, null);
   }

   public void whitePawnDraw(Graphics g, int x, int y) {
      g.drawImage(pawnWhite, x + 315, y + 15, null);
   }

   public void mousePressed(MouseEvent e) {
      if (screenNumber == 1) {
         if (e.getX() >= 481 && e.getY() >= 364 && e.getX() <= 813 && e.getY() <= 415) { // if you hit the start button
            screenNumber = 2;
            repaint();
         }
      } else if (screenNumber == 2) {
         if (e.getX() > 315 && e.getY() > 15 && e.getX() < 980 && e.getY() < 690
               && logic.getSpot((e.getY() - 15) / 83, (e.getX() - 315) / 83) != 0) { // if on the screen tell us what
                                                                                     // tile
            this.tileX = ((e.getX() - 315) / 83);
            this.tileY = ((e.getY() - 15) / 83);
            System.out.println((tileX + 1) + "," + (tileY + 1));
            setSelected(logic.getSpot(tileY, tileX));
            waitingForRelease = true;
         } else if (e.getX() >= 45 && e.getY() >= 25 && e.getX() <= 145 && e.getY() <= 75) { // exit to menu
            screenNumber = 1;
            repaint();
            logic.setup();
         } else if (e.getX() >= 170 && e.getY() >= 25 && e.getX() <= 270 && e.getY() <= 75) { // resign white
            screenNumber = 3;
            logic.setWinner(2);
            repaint();
            logic.setup();
         } else if (e.getX() >= 1024 && e.getY() >= 25 && e.getX() <= 1124 && e.getY() <= 75) { // resign black
            screenNumber = 3;
            logic.setWinner(1);
            repaint();
            logic.setup();
         } else if (e.getX() >= 1149 && e.getY() >= 25 && e.getX() <= 1249 && e.getY() <= 75) { // declare draw
            screenNumber = 3;
            logic.setWinner(3);
            repaint();
            logic.setup();
         }
      } else if (screenNumber == 3) {
         if (e.getX() >= 481 && e.getY() >= 364 && e.getX() <= 813 && e.getY() <= 415) { // if you hit the start button
            screenNumber = 1;
            logic.setWinner(0);
            repaint();
         }
      }
   }

   public void mouseReleased(MouseEvent e) {
      if (screenNumber == 2) {
         if (e.getX() > 315 && e.getY() > 15 && e.getX() < 980 && e.getY() < 690
               && logic.getSpot((e.getY() - 15) / 83, (e.getX() - 315) / 83) != 0
               || e.getX() > 315 && e.getY() > 15 && e.getX() < 980 && e.getY() < 690 && waitingForRelease == true) {
            this.tileX = ((e.getX() - 315) / 83);
            this.tileY = ((e.getY() - 15) / 83);
            System.out.println((tileX + 1) + "," + (tileY + 1));
            setSelected(logic.getSpot(tileY, tileX));
            waitingForRelease = false;
         }
      }
   }

   public void mouseEntered(MouseEvent e) {
   } // why do they make you

   public void mouseExited(MouseEvent e) {
   } // have empty methods?

   public void mouseClicked(MouseEvent e) {
   } // this is really dumb

}