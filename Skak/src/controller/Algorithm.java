package controller;

import data.Board;
import datastructures.Stack;
import interfaces.IBoard;
import interfaces.IPiece;
import moveGeneration.MoveGenerator;
import piece.Move;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.asList;

public class Algorithm {
    private static IPiece.Color AIColor;
    private static ArrayList<Move> firstMoves = null; // The available moves for our current position
    private static int nodesSearched = 0;
    private static int bestScore = 0;
    private static int bestMoveIndex = -1;
    public static boolean running;
    public static void makeMove(IBoard board)
    {
        running = true;
        AIColor = board.getTurn();
        long currentTime = System.currentTimeMillis();
        long totalTimeTaken = 0;
        int depth = 1;
        Move result = null;
        MoveGenerator mg = new MoveGenerator(board);
        mg.GenerateMoves();
        firstMoves = mg.getFinalList();
        //Her bruges der iterative deeping, hvor vi checker om vi er gået over tid hver iteration
        while(running)
        {
            nodesSearched = 0;
            long iterationTimeStart = System.currentTimeMillis();
            result = alphaBetaFirstPly(board , result ,0 , Integer.MIN_VALUE , Integer.MAX_VALUE , depth , depth);
            long iterationTimePassed = System.currentTimeMillis() - iterationTimeStart;
            TimedAlgorithm.getINSTANCE().bestMoveSoFar = result;
            totalTimeTaken += iterationTimePassed;
            depth++;
            if(running && TimedAlgorithm.getINSTANCE().post)
            {
                System.out.println(depth +"    "+bestScore+"    "+(int)(totalTimeTaken/10.0)+"    "+nodesSearched+"    WHODIS\t");
                System.out.flush();
                if(totalTimeTaken*depth > TimedAlgorithm.getINSTANCE().maxWaitTime)
                {
                    TimedAlgorithm.getINSTANCE().timerThread.interrupt();
                }
            }
            //System.err.println("Spent " + iterationTimePassed+"ms at ply "+depth + " Total time passed "+totalTimeTaken/1000.0+"s"+ " Nodes searched "+nodesSearched);
        }

        //System.err.println("Spent "+totalTimeTaken/1000.0+"seconds in total. Max Depth achieved "+(depth-1));

        //return result;
    }

    private static Move alphaBetaFirstPly(IBoard board , Move lastBestMove ,int minimaxLevel , int alpha , int beta , int currentDepth , int maxDepth)
    {
        int b = -1;
        Move move = null; //Dette bliver vores endelige træk
        Move tmp;
        int result = 0 , moveIter = 0;
        IBoard child;
        if(firstMoves.size() == 0)
        {
            return null;
        }
        if(firstMoves.size() == 1)
        {
            TimedAlgorithm.getINSTANCE().bestMoveSoFar = firstMoves.get(0);
            TimedAlgorithm.getINSTANCE().timerThread.interrupt();
        }
        if(lastBestMove != null && bestMoveIndex > 0)
        {
            firstMoves.remove(bestMoveIndex);
            firstMoves.add(0 , lastBestMove);
        }

        //if(minimaxLevel == 0) //Maximizer level
        //{

            while(alpha < beta && moveIter < firstMoves.size() && running)
            {
                tmp = firstMoves.get(moveIter);
                child =  new Board((Board) board, tmp);
                //long tBefore = System.currentTimeMillis();
                result = alphaBeta(child , 1 , alpha , beta , currentDepth-1 , maxDepth);
                //nodesSearched++;
                if(result > alpha)
                {
                    alpha = result;
                    move = tmp;
                    bestMoveIndex = moveIter;
                }
                moveIter++;
                //long tPassed = System.currentTimeMillis() - tBefore;
                //System.out.println("Spent "+tPassed/1000.0 +" seconds on move "+moveIter+"/"+firstMoves.size());
            }
            bestScore = alpha;
        return move;

        /*}
        else //Minimizer level
        {
            while(alpha < beta &&(tmp = moves.pop()) != null && running)
            {
                child =  new Board((Board) board, tmp);
                result = alphaBeta(child , 0 , alpha , beta , currentDepth-1 , maxDepth);
                if(result < beta)
                {
                    beta = result;
                    move = tmp;
                }
            }


        }*/

    }

    private static int alphaBeta(IBoard board , int minimaxLevel , int alpha , int beta , int currentDepth , int maxDepth)
    {
        if(currentDepth == 0)
        {
            nodesSearched++;
            return StaticEvaluator.StaticEvaulation(board , maxDepth , minimaxLevel , false);
        }
        MoveGenerator mg = new MoveGenerator(board);
        mg.GenerateMoves();
        Stack<Move> moves = mg.getFinalMoveStack();

        if(moves.isEmpty())
        {
            nodesSearched++;
            return StaticEvaluator.StaticEvaulation(board , maxDepth-currentDepth , minimaxLevel , true);
        }
        IBoard child = null;
        Move move = null;
        int result = 0;

        if(minimaxLevel == 0)
        {
            while(alpha < beta && (move = moves.pop()) != null && running)
            {
                child = new Board((Board) board, move);
                result = alphaBeta(child , 1 , alpha , beta , currentDepth-1 , maxDepth);
                //nodesSearched++;
                alpha = result > alpha ? result : alpha;

            }
            return alpha;
        }
        else
        {
            while(alpha < beta && (move = moves.pop()) != null && running)
            {
                child = new Board((Board) board, move);
                result = alphaBeta(child , 0 , alpha , beta , currentDepth-1 , maxDepth);
                //nodesSearched++;
                beta = result < beta ? result : beta;

            }
            return beta;

        }

    }
}
