// 2 player Connect 4 game

import java.util.Scanner;

public class Connect4TwoPlayer {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    Connect4BoardTwoPlayer board = new Connect4BoardTwoPlayer();
    boolean turn = Connect4Board.PLAYER_1_TURN;
    while(board.currentGameState() == Connect4Board.ONGOING) {
      System.out.println("\n\n"+board);
      int moveColumn;
      do {
        System.out.printf("Enter Player %d move: ", board.getNextTurn() == Connect4Board.PLAYER_1_TURN ? 1 : 2);
        moveColumn = in.nextInt()-1;
        if(!board.canPlace(moveColumn)){
          System.out.println("Invalid Move!");
        }
      } while(!board.canPlace(moveColumn));
      board.place(moveColumn);
    }
    int gameState = board.currentGameState();
    System.out.println("\n\n");
    switch(gameState) {
      case Connect4Board.PLAYER_1_WON:
        System.out.println("Player 1 won.\n");
        break;
      case Connect4Board.PLAYER_2_WON:
        System.out.println("Player 2 won.\n");
        break;
      default:
        System.out.println("Tie.\n");
        break;
    }
  in.close();
  }
}