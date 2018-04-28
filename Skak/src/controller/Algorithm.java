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
    public static boolean running;
    public static void makeMove(IBoard board)
    {
        running = true;
        AIColor = board.getTurn();
        long currentTime = System.currentTimeMillis();
        long totalTimeTaken = 0;
        int depth = 1;
        Move result = null;
        //Her bruges der iterative deeping, hvor vi checker om vi er gået over tid hver iteration
        //Det er stadig muligt at vi bruger mere end 15 sekunder
        while(running)
        {
            long iterationTimeStart = System.currentTimeMillis();
            TimedAlgorithm.getINSTANCE().bestMoveAlpha = Integer.MIN_VALUE;
            result = alphaBetaFirstPly(board , result ,0 , Integer.MIN_VALUE , Integer.MAX_VALUE , depth , depth);
            TimedAlgorithm.getINSTANCE().bestMoveSoFar = result;
            long iterationTimePassed = System.currentTimeMillis() - iterationTimeStart;
            totalTimeTaken += iterationTimePassed;
            depth++;
            //System.err.println("Spent " + iterationTimePassed+"ms at ply "+depth + " Total time passed "+totalTimeTaken/1000.0+"s");
        }

        //System.err.println("Spent "+totalTimeTaken/1000.0+"seconds in total. Max Depth achieved "+(depth-1));

        //return result;
    }

    private static Move alphaBetaFirstPly(IBoard board , Move lastBestMove ,int minimaxLevel , int alpha , int beta , int currentDepth , int maxDepth)
    {
        Move move = null; //Dette bliver vores endelige træk
        Move tmp = null;
        int result = 0 , moveIter = 0;
        IBoard child = null;
        MoveGenerator mg = new MoveGenerator(board);
        mg.GenerateMoves();
        Stack<Move> moves = new Stack<>();
        moves = mg.getFinalMoveStack();
        if(moves.isEmpty())
        {
            return null;
        }
        if(moves.size() == 1)
        {
            TimedAlgorithm.getINSTANCE().bestMoveSoFar = moves.pop();
            TimedAlgorithm.getINSTANCE().timerThread.interrupt();
        }
        if(lastBestMove != null)
            moves.push(lastBestMove);
        if(minimaxLevel == 0) //Maximizer level
        {

            while(alpha < beta && (tmp = moves.pop()) != null && running)
            {
                child =  new Board((Board) board, tmp);
                result = alphaBeta(child , 1 , alpha , beta , currentDepth-1 , maxDepth);

                if(result > alpha)
                {
                    alpha = result;
                    move = tmp;
                    if(alpha > TimedAlgorithm.getINSTANCE().bestMoveAlpha)
                    {
                        TimedAlgorithm.getINSTANCE().bestMoveAlpha = alpha;
                        TimedAlgorithm.getINSTANCE().bestMoveSoFar = tmp;
                    }

                }


            }

        }
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


        }
        return move;
    }

    private static int alphaBeta(IBoard board , int minimaxLevel , int alpha , int beta , int currentDepth , int maxDepth)
    {
        if(currentDepth == 0)
        {
            return StaticEvaluator.StaticEvaulation(board , maxDepth , minimaxLevel , false);
        }
        MoveGenerator mg = new MoveGenerator(board);
        mg.GenerateMoves();
        Stack<Move> moves = mg.getFinalMoveStack();

        if(moves.isEmpty())
        {
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
                beta = result < beta ? result : beta;

            }
            return beta;

        }

    }
}
