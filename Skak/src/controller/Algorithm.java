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

    public static boolean running;
    public static void makeMove(IBoard board)
    {
        running = true;
        int minimaxLevel = board.getTurn() == IPiece.Color.WHITE ? 1 : -1;
        long currentTime = System.currentTimeMillis();
        long totalTimeTaken = 0;
        //long maxTimeAllowed = 15000; //15 sekunder
        int depth = 2;
        Move result = null;
        //Her bruges der iterative deeping, hvor vi checker om vi er gået over tid hver iteration
        //Det er stadig muligt at vi bruger mere end 15 sekunder
        while(running)
        {
            long iterationTimeStart = System.currentTimeMillis();
            result = alphaBetaFirstPly(board , result ,minimaxLevel , Integer.MIN_VALUE , Integer.MAX_VALUE , depth , depth);
            TimedAlgorithm.getINSTANCE().bestMoveSoFar = result;
            long iterationTimePassed = System.currentTimeMillis() - iterationTimeStart;
            totalTimeTaken += iterationTimePassed;
            //System.err.println("Spent " + iterationTimePassed+"ms at depth "+depth + " Total time passed "+totalTimeTaken/1000.0+"s");
            depth++;
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
        if(lastBestMove != null)
            moves.push(lastBestMove);
        if(minimaxLevel == 1) //Maximizer level
        {

            while(alpha < beta && (tmp = moves.pop()) != null && running)
            {
                child =  new Board((Board) board, tmp);
                result = alphaBeta(child , minimaxLevel*-1 , alpha , beta , currentDepth-1 , maxDepth);
                if(result > alpha)
                {
                    alpha = result;
                    move = tmp;
                }

            }

        }
        else //Minimizer level
        {
            while(alpha < beta &&(tmp = moves.pop()) != null && running)
            {
                child =  new Board((Board) board, tmp);
                result = alphaBeta(child , minimaxLevel*-1 , alpha , beta , currentDepth-1 , maxDepth);
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
            return StaticEvaluator.StaticEvaulation(board , maxDepth);
        }
        //TODO Skal måske også checke om man er skakmat

        MoveGenerator mg = new MoveGenerator(board);
        mg.GenerateMoves();
        Stack<Move> moves = mg.getFinalMoveStack();
        if(moves.isEmpty())
        {
            return StaticEvaluator.StaticEvaulation(board , maxDepth-currentDepth);
        }
        IBoard child = null;
        Move move = null;
        int result = 0;

        if(minimaxLevel == 1)
        {
            while(alpha < beta && (move = moves.pop()) != null && running)
            {
                child = new Board((Board) board, move);
                result = alphaBeta(child , minimaxLevel*-1 , alpha , beta , currentDepth-1 , maxDepth);
                alpha = result > alpha ? result : alpha;

            }
            return alpha;
        }
        else
        {
            while(alpha < beta && (move = moves.pop()) != null && running)
            {
                child = new Board((Board) board, move);
                result = alphaBeta(child , minimaxLevel*-1 , alpha , beta , currentDepth-1 , maxDepth);
                beta = result < beta ? result : beta;

            }
            return beta;

        }

    }
}
