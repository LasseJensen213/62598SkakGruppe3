package controller;

import data.Board;
import interfaces.IBoard;
import interfaces.IPiece;
import moveGeneration.MoveGenerator;
import piece.Move;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.asList;

public class Algorithm {

    public static Move makeMove(IBoard board)
    {
        int minimaxLevel = board.getTurn() == IPiece.Color.WHITE ? 1 : -1;
        long currentTime = System.currentTimeMillis();
        long totalTimeTaken = 0;
        long maxTimeAllowed = 15000; //15 sekunder
        int depth = 1;
        Move result = null;
        //Her bruges der iterative deeping, hvor vi checker om vi er gået over tid hver iteration
        //Det er stadig muligt at vi bruger mere end 15 sekunder
        while(totalTimeTaken < maxTimeAllowed)
        {

            long iterationTimeStart = System.currentTimeMillis();
            result = alphaBetaFirstPly(board , result ,minimaxLevel , Integer.MIN_VALUE , Integer.MAX_VALUE , depth , depth);
            long iterationTimePassed = System.currentTimeMillis() - iterationTimeStart;
            totalTimeTaken += iterationTimePassed;
            System.err.println("Spent " + iterationTimePassed+"ms at depth "+depth);
            depth++;
        }
        System.err.println("Spent "+totalTimeTaken+"ms in total. Max Depth achieved "+(depth-1));

        return result;
    }

    private static Move alphaBetaFirstPly(IBoard board , Move lastBestMove ,int minimaxLevel , int alpha , int beta , int currentDepth , int maxDepth)
    {
        Move move = null; //Dette bliver vores endelige træk
        int result = 0 , childIter = 0;
        IBoard child = null;
        MoveGenerator mg = new MoveGenerator(board);
        mg.GenerateMoves();
        ArrayList<IBoard> children = board.getChildBoards();

        if(lastBestMove != null)
        {
            IBoard[] best = {new Board((Board) board, lastBestMove)};
            ArrayList<IBoard> newChildren = new ArrayList<>(children.size()+1);
            newChildren.add(new Board((Board) board, lastBestMove));
            newChildren.addAll(children);
            children = newChildren;
        }
        if(minimaxLevel == 1) //Maximizer level
        {

            while(alpha < beta && childIter < children.size())
            {
                child = children.get(childIter);
                childIter ++;
                result = alphaBeta(child , minimaxLevel*-1 , alpha , beta , currentDepth-- , maxDepth);
                if(result > alpha)
                {
                    alpha = result;
                    move = child.getMove();
                }

            }
        }
        else //Minimizer level
        {
            while(alpha < beta && childIter < children.size())
            {
                child = children.get(childIter);
                childIter++;
                result = alphaBeta(child , minimaxLevel*-1 , alpha , beta , currentDepth-- , maxDepth);
                if(result < beta)
                {
                    beta = result;
                    move = child.getMove();
                }
            }


        }
        return move;
    }

    private static int alphaBeta(IBoard board , int minimaxLevel , int alpha , int beta , int currentDepth , int maxDepth)
    {

        if(currentDepth == 0)
        {
            return StaticEvaluator.StaticEvaulation(board);
        }
        //TODO Skal måske også checke om man er skakmat

        MoveGenerator mg = new MoveGenerator(board);
        mg.GenerateMoves();
        ArrayList<IBoard> children = board.getChildBoards();
        IBoard child = null;
        int result = 0;
        int childIter = 0;
        if(minimaxLevel == 1)
        {
            while(alpha < beta && childIter<children.size())
            {
                child = children.get(childIter);
                childIter ++;
                result = alphaBeta(child , minimaxLevel*-1 , alpha , beta , currentDepth-- , maxDepth);
                if(result > alpha)
                {
                    alpha = result;
                }
            }
            return alpha;
        }
        else
        {
            while(alpha < beta && childIter<children.size())
            {
                child = children.get(childIter);
                childIter ++;
                result = alphaBeta(child , minimaxLevel*-1 , alpha , beta , currentDepth-- , maxDepth);
                if(result < beta)
                {
                    beta = result;
                }
            }
            return beta;

        }

    }
}
