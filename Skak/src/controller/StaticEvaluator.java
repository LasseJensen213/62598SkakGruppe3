package controller;

import interfaces.IBoard;
import interfaces.IPiece;

import java.awt.*;
import java.util.ArrayList;

public class StaticEvaluator {

    public static int  StaticEvaulation(IBoard board)
    {

        //Starter med en meget simplem måde at evalurer brættet, lægger bare alle brikkernes værdier sammen
        int result = 0;
        Point p = new Point(0 , 0);
        IPiece piece = null;
        int blackValue = 0 , whiteValue = 0;
        for(int y = 0 ; y<8 ; y++)
        {
            for(int x = 0 ; x<8 ; x++)
            {
                p.x = x;
                p.y = y;
                piece = board.getPiece(p);
                if(piece != null)
                {
                    if(piece.getColor() == IPiece.Color.WHITE)
                        whiteValue += piece.getValue();
                    else
                        blackValue += piece.getValue();
                }

            }
        }
        result = whiteValue -blackValue;
        result += board.getAdditionalPoints();
        return result;
    }
}
