package controller;

import interfaces.IBoard;
import interfaces.IPiece;

import java.util.ArrayList;

public class StaticEvaluator {

    public static int  StaticEvaulation(IBoard board)
    {

        //Starter med en meget simplem måde at evalurer brættet, lægger bare alle brikkernes værdier sammen
        int result = 0;
        int minimax = 1;
        for(IPiece p : board.getPieces())
        {
            minimax = p.getColor() == IPiece.Color.WHITE? 1 : -1;
            result += p.getValue()*minimax;
        }
        result += board.getAdditionalPoints();
        return result;
    }
}
