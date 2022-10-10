// Monte Carlo Tree Search AI for Connect 4

import java.util.ArrayList;
import java.util.*;


public class Connect4AIMonteCarlo {
  private MCTSNode root; // starting state
  int expandedNodes = 0;
  long memoryUsed;
  long totalTime;
  private final int width;
  private static final double EXPLORATION_PARAMETER = Math.sqrt(2);
  private long givenTime;
  public Connect4AIMonteCarlo(Connect4Board board, long givenTime) {
    this.width = board.width;
    this.givenTime = givenTime;
    root = new MCTSNode(null, board.copy());
  }

  // sets root to new board state given move
  public void update(int move) {
      root = root.children[move] != null 
        ? root.children[move] 
        : new MCTSNode(null, root.board.getNextState(move));
  }

  // returns the optimal move for the current player
  public int getOptimalMove() {
    long startTime= System.currentTimeMillis();
    for (long stop = System.nanoTime()+givenTime; stop>System.nanoTime();) {
      MCTSNode selectedNode = select();
      if(selectedNode == null)
        continue;
      MCTSNode expandedNode = expand(selectedNode);
      double result = simulate(expandedNode);
      backpropagate(expandedNode, result);
    }

    int maxIndex = -1;
    for(int i = 0; i < width; i++) {
      if(root.children[i] != null) {
        if(maxIndex == -1 || root.children[i].visits > root.children[maxIndex].visits)
          maxIndex = i;
        // System.out.printf("\nlocation%d: p1wins: %f/%d = %f", i, root.children[i].player1Wins, root.children[i].visits, root.children[i].player1Wins/root.children[i].visits);
      }
    }
    Runtime runtime = Runtime.getRuntime();
    memoryUsed = (runtime.totalMemory() - runtime.freeMemory())/(1024);
    totalTime = System.currentTimeMillis() - startTime;
    return maxIndex;
  }

  private MCTSNode select() {
    return select(root);
  }

  private MCTSNode select(MCTSNode parent) {
    // Se o parent tem pelo menos uma child sem estatística, seleciona o parent
    for(int i = 0; i < width; i++) {
      if(parent.children[i] == null && parent.board.canPlace(i)) {
        return parent;
      }
    }
  // Se todos os children têm estatística, utiliza-se a UCT para calcular o próximo nó a visitar
    double maxSelectionVal = -1;
    int maxIndex = -1;
    for(int i = 0; i < width; i++) {
      if(!parent.board.canPlace(i))
        continue;
      MCTSNode currentChild = parent.children[i];
      double wins = parent.board.getNextTurn() == Connect4Board.PLAYER_1_TURN ? currentChild.player1Wins : (currentChild.visits-currentChild.player1Wins);
      double selectionVal = wins/currentChild.visits + EXPLORATION_PARAMETER*Math.sqrt(Math.log(parent.visits)/currentChild.visits); // UCT
      if(selectionVal > maxSelectionVal) {
        maxSelectionVal = selectionVal;
        maxIndex = i;
      }
    }
    //MaxIndex encontra erro em que é atribuido o valor -1 || """"Solução""""
    if(maxIndex == -1)
      return null;
    return select(parent.children[maxIndex]);
  }

  private MCTSNode expand(MCTSNode selectedNode) {
    // get unvisited child nodes
    ArrayList<Integer> unvisitedChildrenIndices = new ArrayList<Integer>(width);
    for(int i = 0; i < width; i++) {
      if(selectedNode.children[i] == null && selectedNode.board.canPlace(i)) {
        unvisitedChildrenIndices.add(i);
      }
    }

    // Escolhe child aleatório e cria um nó para ele
    int selectedIndex = unvisitedChildrenIndices.get((int)(Math.random()*unvisitedChildrenIndices.size()));
    selectedNode.children[selectedIndex] = new MCTSNode(selectedNode, selectedNode.board.getNextState(selectedIndex));
    return selectedNode.children[selectedIndex];
  } 

  // simula jogadas
  private double simulate(MCTSNode expandedNode) {
    Connect4Board simulationBoard = expandedNode.board.copy();
    while(simulationBoard.currentGameState() == Connect4Board.ONGOING) {
      simulationBoard.place((int)(Math.random()*width));
      ++expandedNodes;
    }
      // System.out.println(simulationBoard);

    switch(simulationBoard.currentGameState()) {
      case Connect4Board.PLAYER_1_WON:
        return 1;
      case Connect4Board.PLAYER_2_WON:
        return 0;
      default:
        return 0.5;
    }
  }
  //efetua a backpropagation, atualizando os valores de cada nó ascendente
  private void backpropagate(MCTSNode expandedNode, double simulationResult) {
    MCTSNode currentNode = expandedNode;
    while(currentNode != null) {
      currentNode.incrementVisits();
      currentNode.incrementPlayer1Wins(simulationResult);
      currentNode = currentNode.parent;
    }
  }


//Utilizado para o cálculo da UCT (Verificar livro caso exista dúvida)
  private class MCTSNode {
    private MCTSNode parent;
    // children[i] representa o próximo estado do jogo, em que o jogador atual coloca o disco na posição i
    private MCTSNode[] children;
    private int visits;
    private double player1Wins;
    private final Connect4Board board;
    public MCTSNode(MCTSNode parent, Connect4Board board) {
      this.parent = parent;
      this.board = board;
      this.visits = 0;
      this.player1Wins = 0;
      children = new MCTSNode[width];
    }

    public int incrementVisits() {
      return ++visits;
    }
    public double incrementPlayer1Wins(double result) {
      player1Wins += result;
      return player1Wins;
    }
  }
}