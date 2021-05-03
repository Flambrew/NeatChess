/* Andrew Matherne - Hn. Adv. Programming Final Project
 * Started 4/06/2021
 * Chess Main
 */

public class ChessMain {

   public static void main(String[] args) {

      ChessBot bot = new ChessBot();
      ChessScreen screen = new ChessScreen(bot);
      ChessLogic logic = new ChessLogic(screen, bot);

      screen.setLogic(logic);
      bot.setLogic(logic);
      bot.setScreen(screen);

      logic.gameLoop(true);
   }
}