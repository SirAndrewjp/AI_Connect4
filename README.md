# AI_Connect4
Java program that implements min-max, alpha-beta, and Monte Carlo algorithms for a connect 4 game

-->AIvAI Has programs that pit an Ai vs another Ai
	Compile with "javac *.java"
	
-To execute the programs that use min-max or alpha-beta: "java Connect4AIvAIMinMax [Optional Argument]" or "java Connect4AIvAI AlphaBeta [Optional Argument]".

The [Optional Argument] is in place so that you can pick a tree depth, or, in other words, the difficulty of the AI.
If no Optional Argument in inserted, as in, "java Connect4AIvAIMinMax", for example, then the tree depth will default to 6
	
The same concept applies to Connect4AIvAIMonteCarlo, except the optional argument in this case is to set the amount of seconds that the algorithm will run for before choosing a move.

-->SinglePlayer Has programs that pit a human player vs an AI

	Compile with "javac *.java"
	
The program execution follow the same concept as the AIvAI programs, where you can input an optional argument to pick the "difficulty"

\--Choosing moves--/

-To pick a move, input a number between 1 and 7, i.e. the column numbers. If the move is valid, it will be performed.
This process will continue until there are no more possible moves left, or either the player or AI win.


-->TwoPlayer pits two human opponents against eachother
Compile with "javac *.java"
Execute with "java Connect4TwoPlayer"
