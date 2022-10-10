import java.util.Scanner;

public class Connect4SinglePlayerAlphaBeta {
  public static void main(String[] args) {
    final int Max_depth  = (args.length > 0 ? Integer.parseInt(args[0]) : 6);
    Scanner in = new Scanner(System.in);
    boolean turn;
    System.out.println("Qual joga primeiro? Insira 1 para o utilizador, 2 para o AI");
    int jogador = in.nextInt();
    if(jogador==1){
      turn = Connect4Board.PLAYER_1_TURN;
    }
    else{
      turn = Connect4Board.PLAYER_2_TURN;

    }
    Connect4Board board = new Connect4Board(turn);
    int turns=0;
    Connect4AIAlphaBeta ai = new Connect4AIAlphaBeta(board, Max_depth, turns);
    while(board.currentGameState() == Connect4Board.ONGOING) {
      System.out.println("\n\n"+board);
      int moveColumn;
      do {
        if(board.getNextTurn() == Connect4Board.PLAYER_1_TURN) {
          System.out.printf("Enter your move: ", board.getNextTurn() == Connect4Board.PLAYER_1_TURN ? 1 : 2);
          moveColumn = in.nextInt()-1;
        }
        else {
          System.out.print("AI determining move: ");
          moveColumn = ai.getOptimalMove(board, turns);
          System.out.println(moveColumn+1);
        }
        if(!board.canPlace(moveColumn)){
          System.out.println("Invalid Move!");
        }
      } while(!board.canPlace(moveColumn));
      board.place(moveColumn);
      System.out.println("Memória usada:" +ai.memoryUsed+"k");
      System.out.println("Tempo usado:" +ai.totalTime+"ms");
      System.out.println("Nós expandidos:" +ai.expandedNodes);
      turns++;
    }
    int gameState = board.currentGameState();
    System.out.println("\n\n\n\n\n");
    System.out.println(board);
    switch(gameState) {
      case Connect4Board.PLAYER_1_WON:
        System.out.println("You won.\n");
        break;
      case Connect4Board.PLAYER_2_WON:
        System.out.println("AI won.\n");
        break;
      default:
        System.out.println("Tie.\n");
        break;
    }
    in.close();
    }
}