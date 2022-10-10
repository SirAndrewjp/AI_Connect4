import java.util.ArrayList;
import java.util.Vector;

import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.text.html.MinimalHTMLWriter;

public class Connect4AIAlphaBeta {
  private int MAX_DEPTH;
  private int turns;
  int expandedNodes = 0;
  long memoryUsed;
  long totalTime;
  private final int width;
  private static final double EXPLORATION_PARAMETER = Math.sqrt(2);
  public Connect4AIAlphaBeta(Connect4Board board, int Max_depth, int current_turns) {
    this.width = board.width;
    this.MAX_DEPTH = Max_depth;
    this.turns = current_turns;
  }

//g-good, b-bad, z-empty
  public int heurFunction(int g, int b, int z) {
	int score = 0;
	if (g == 4) { score += 512; } // preference to go for winning move vs. block
	else if (g == 3 && z == 1) { score += 50; }
	else if (g == 2 && z == 2) { score += 10; }
    else if (g == 1 && z == 3) { score += 1; }
    else if (b == 1 && z == 3) { score -= 1; }
	else if (b == 2 && z == 2) { score -= 10; } // preference to block
	else if (b == 3 && z == 1) { score -= 50; } // preference to block
	else if (b == 4) { score -= 512; }
	return score;
}

  public int scoreSet(ArrayList<Integer> v , int player){
    int good = 0; // points in favor of player
	int bad = 0; // points against player
	int empty = 0; // neutral spots
	for (int i = 0; i < v.size(); i++) { // just enumerate how many of each
		good += (v.get(i).equals(player)) ? 1 : 0;
		bad += (v.get(i).equals(Connect4Board.PLAYER_1_TURN_int) || v.get(i).equals(Connect4Board.PLAYER_2_TURN_int)) ? 1 : 0;
		empty += (v.get(i).equals(0)) ? 1 : 0;
	}
	// bad was calculated as (bad + good), so remove good
	bad -= good;
	return heurFunction(good, bad, empty);
  }

  public int evaluate(Connect4Board board, int player){
    int score = 0;
    ArrayList<Integer> rs = new ArrayList<Integer>(board.width);
    ArrayList<Integer> cs = new ArrayList<Integer>(board.height);
    ArrayList<Integer> set = new ArrayList<Integer>(4);
    //rows
    for(int i=0; i<board.height; i++){
        for(int y=0; y<board.width; y++){
            rs.add(y,board.getContentsSpecific(i,y));
        }
        for(int y=0; y<board.width-3; y++){
            for(int j=0; j<4; j++){
                set.add(j,rs.get(y+j));
            }
            score+=scoreSet(set,player);
        }
    }
    //vertical
    for(int i=0; i<board.width; i++){
        for(int y=0; y<board.height; y++){
            cs.add(y,board.getContentsSpecific(y,i));
        }
        for(int y=0; y<board.height-3; y++){
            for(int j=0; j<4; j++){
                set.add(j,cs.get(y+j));
            }
            score+=scoreSet(set,player);
        }
    }
    //diagonais
	for (int r = 0; r < board.height - 3; r++) {
		for ( int c = 0; c < board.width; c++) {
			rs.add(c,board.getContentsSpecific(r,c));
		}
		for (int c = 0; c < board.width - 3; c++) {
			for (int i = 0; i < 4; i++) {
				set.add(i,board.getContentsSpecific(r + i,c + i));
			}
			score += scoreSet(set, player);
		}
	}
	for (int r = 0; r < board.height - 3; r++) {
		for (int c = 0; c < board.width; c++) {
			rs.add(c,board.getContentsSpecific(r,c));
		}
		for (int c = 0; c < board.width - 3; c++) {
			for (int i = 0; i < 4; i++) {
				set.add(i,board.getContentsSpecific(r + 3 - i,c + i));
			}
			score += scoreSet(set, player);
		}
	}
	return score;
  }

  public int getOptimalMove(Connect4Board board, int turns){
    return miniMax(board, turns, MAX_DEPTH, 0-Integer.MAX_VALUE, Integer.MAX_VALUE, Connect4Board.PLAYER_2_TURN_int)[1];
  }

  // Board, depth max (Vai-se decrementando), jogador atual
  public int[] miniMax(Connect4Board board, int current_turns, int depth_max, int alpha, int beta, int player){
    long startTime= System.currentTimeMillis();
    if(depth_max == 0 || depth_max >= (board.height*board.width) - current_turns){
        int col = 0;
        Runtime runtime = Runtime.getRuntime();
        memoryUsed = (runtime.totalMemory() - runtime.freeMemory())/(1024);
        totalTime = System.currentTimeMillis() - startTime;
        for(int i=0; i<board.width; i++){
            if(board.canPlace(i)){
                col=i;
            }
        }
        return new int[] {evaluate(board, Connect4Board.PLAYER_2_TURN_int), col};
    }
    //Se for o AI a jogar
    if(player==Connect4Board.PLAYER_2_TURN_int){
        int[] movesofar = {Integer.MIN_VALUE,-1};
        if(board.currentGameState()==Connect4Board.PLAYER_1_WON){
            Runtime runtime = Runtime.getRuntime();
            memoryUsed = (runtime.totalMemory() - runtime.freeMemory())/(1024);
            totalTime = System.currentTimeMillis() - startTime;
            return movesofar;
        }
        for(int i = 0; i<board.width; i++){
            if(board.canPlace(i)){
                Connect4Board copiedboard = board.copy();
                copiedboard.place(i);
                expandedNodes++;
                int score = miniMax(copiedboard, current_turns+1, depth_max-1, alpha, beta, Connect4Board.PLAYER_1_TURN_int)[0];
                if(score>movesofar[0]){
                    movesofar[0] = score;
                    movesofar[1] = i;
                }
                alpha = Math.max(alpha, movesofar[0]);
                if(alpha >= beta){
                    break; //Pruning
                }
            }
        }
        Runtime runtime = Runtime.getRuntime();
        memoryUsed = (runtime.totalMemory() - runtime.freeMemory())/(1024);
        totalTime = System.currentTimeMillis() - startTime;
        return movesofar;
    }
    else{
        int[] movesofar = {Integer.MAX_VALUE, -1};
        if(board.currentGameState()==Connect4Board.PLAYER_2_WON){
            Runtime runtime = Runtime.getRuntime();
            memoryUsed = (runtime.totalMemory() - runtime.freeMemory())/(1024);
            totalTime = System.currentTimeMillis() - startTime;
            return movesofar;
        }
        for(int i=0; i<board.width; i++){
            if(board.canPlace(i)){
                Connect4Board newboard = board.copy();
                newboard.place(i);
                expandedNodes++;
                int score = miniMax(newboard, current_turns+1, depth_max-1, alpha, beta, Connect4Board.PLAYER_2_TURN_int)[0];
                if(score<movesofar[0]){
                    movesofar[0]=score;
                    movesofar[1]=i;
                }
                beta = Math.min(beta, movesofar[0]);
                    if(alpha>=beta){
                        break; //Pruning
                    }
                }
            }
        Runtime runtime = Runtime.getRuntime();
        memoryUsed = (runtime.totalMemory() - runtime.freeMemory())/(1024);
        totalTime = System.currentTimeMillis() - startTime;
        return movesofar;
    }
  }
}