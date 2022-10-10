public class Connect4AIvAIAlphaBeta {
    public static void main(String[] args) {
        final int Max_depth  = (args.length > 0 ? Integer.parseInt(args[0]) : 6);
        Connect4Board board = new Connect4Board();
        int turns = 0;
        while(board.currentGameState() == Connect4Board.ONGOING) {
            System.out.println("\n\n"+board);
            int moveColumn;
            Connect4AIAlphaBeta ai = new Connect4AIAlphaBeta(board, Max_depth, turns);
            do {
                System.out.printf("AI %d determining move: ", board.getNextTurn() == Connect4Board.PLAYER_1_TURN ? 1 : 2);
                moveColumn = ai.getOptimalMove(board, turns);
                System.out.println(moveColumn+1);
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
                System.out.println("AI 1 won.\n");
            break;
                case Connect4Board.PLAYER_2_WON:
            System.out.println("AI 2 won.\n");
            break;
            default:
                System.out.println("Tie.\n");
            break;
        }
    }
  }