package controller;

import interfaces.IBoard;
import interfaces.IPiece;
import piece.King;

public class StaticEvaluator {


    //Kaare's vurderings funktion
    private static int[] pawRow = {0 , 0 , -1 , 0 , 2 , 14 , 30 , 0};
    private static int[] pawLin = {-2 , 0 , 3 , 4 , 5 , 1 , -2 , -2};

    public static int  StaticEvaulation(IBoard board , int currentDepth , int minimaxLevel, boolean noMovesLeft)
    {
        int blackValue = 0 , whiteValue = 0, result = 0;

        if(noMovesLeft)
        {
            if(board.isChecked())//Checkmate!
            {
                if(currentDepth == 1)
                    System.out.println(board);
                if(minimaxLevel == 0)
                    return -2000000+currentDepth*100;
                else
                    return 2000000-currentDepth*100;
            }
            else//Stalemate
            {
                if(minimaxLevel==0)
                {
                    if(currentDepth==0)
                        TimedAlgorithm.getINSTANCE().stalemate = true;
                    return -100000+currentDepth;
                }
                else
                {
                    return 100000-currentDepth;
                }

            }
        }
        IPiece piece = null;
        IPiece[][] chessBoard = board.getChessBoard();

        for(int y = 0 ; y<8 ; y++)
        {
            for(int x = 0 ; x<8 ; x++)
            {
                piece = chessBoard[x][y];
                if(piece == null)
                    continue;

                switch (piece.getColor())
                {
                    case WHITE:
                        switch (piece.getType())
                        {
                            case Bishop:
                                whiteValue += 300 + 2*coveredFieldsBishop(chessBoard , x ,y , IPiece.Color.WHITE);
                                break;
                            case King:
                                if(((King) piece).isCheck())
                                    whiteValue += 10000 - 100*(currentDepth/2);
                                else
                                    whiteValue += 10000;
                                break;
                            case Queen:
                                whiteValue += 900 + coveredFieldsQueen(chessBoard , x , y , IPiece.Color.WHITE);
                                break;
                            case Pawn:
                                whiteValue += 100 + pawRow[y]+ (pawLin[x]*y)/2;
                                break;
                            case Rook:
                                whiteValue += 500 + 1.5*coveredFieldsRook(chessBoard , x , y , IPiece.Color.WHITE);
                                break;
                            case Knight:
                                whiteValue += 300 + 3.0*(4-distToCenter(x ,y));
                                break;

                        }
                        break;
                    case BLACK:
                        switch (piece.getType())
                        {
                            case Bishop:
                                blackValue += 300 + 2*coveredFieldsBishop(chessBoard , x ,y , IPiece.Color.BLACK);
                                break;
                            case King:
                                if(((King) piece).isCheck())
                                    blackValue += 10000 - 100*(currentDepth/2);
                                else
                                    blackValue += 10000;
                                break;
                            case Queen:
                                blackValue += 900 + coveredFieldsQueen(chessBoard , x , y , IPiece.Color.BLACK);
                                break;
                            case Pawn:
                                blackValue += 100 + pawRow[y]+ (pawLin[7-x]*y)/2;
                                break;
                            case Rook:
                                blackValue += 500 + 1.5*coveredFieldsRook(chessBoard , x , y , IPiece.Color.BLACK);
                                break;
                            case Knight:
                                blackValue += 300 + 3.0*(4-distToCenter(x , y));
                                break;

                        }
                        break;
                }
            }
        }
        //result += board.getAdditionalPoints();
        IPiece.Color aiPLayerCol , adversaryCol;
        if(minimaxLevel == 0)                         //At maximizer level
        {
            if(board.getTurn() == IPiece.Color.BLACK) //AI Player is black
            {
                result = blackValue - whiteValue;
            }
            else                                     //AI Player is white
            {
                result = whiteValue - blackValue;
            }
        }
        else                                          //At Minimizer level
        {
            if(board.getTurn() == IPiece.Color.BLACK) //AI Player is white
            {
                result = whiteValue - blackValue;
            }
            else                                      //AI Player is black
            {
                result = blackValue - whiteValue;
            }

        }
        return result;
    }


    private static int coveredFieldsBishop(IPiece[][] chessBoard , int x , int y, IPiece.Color color)
    {
        int leftFields = x;
        int rightFields = 7 - leftFields;
        int bottomFields = y;
        int topFields = 7-bottomFields;
        int leftLowerDiag = leftFields < bottomFields ? leftFields : bottomFields;
        int leftUpperDiag = leftFields < topFields ? leftFields : topFields;
        int rightLowerDiag = rightFields < bottomFields ? rightFields : bottomFields;
        int rightUpperDiag = rightFields < topFields ? rightFields : topFields;
        int i = 0 , coveredFields  = 0;
        IPiece piece = null;
        for(i = 1 ; i<=leftLowerDiag ; i++)
        {
            piece = chessBoard[x-i][y-i];
            if(piece == null)
                coveredFields++;
            else if(piece.getColor() == color)
                break;
            else{
                coveredFields++;
                break;
            }
        }
        for(i = 1 ; i<=leftUpperDiag ; i++)
        {
            piece = chessBoard[x-i][y+i];
            if(piece == null)
                coveredFields++;
            else if(piece.getColor() == color)
                break;
            else{
                coveredFields++;
                break;
            }
        }
        for(i = 1 ; i<=rightLowerDiag ; i++)
        {
            piece = chessBoard[x+i][y-i];
            if(piece == null)
                coveredFields++;
            else if(piece.getColor() == color)
                break;
            else{
                coveredFields++;
                break;
            }
        }
        for(i = 1 ; i<=rightUpperDiag ; i++)
        {
            piece = chessBoard[x+i][y+i];
            if(piece == null)
                coveredFields++;
            else if(piece.getColor() == color)
                break;
            else{
                coveredFields++;
                break;
            }
        }

        return coveredFields;
    }
    private static int coveredFieldsQueen(IPiece[][] chessBoard , int x , int y , IPiece.Color color)
    {
        return coveredFieldsRook(chessBoard , x , y , color) + coveredFieldsBishop(chessBoard , x , y , color);
    }
    private static int coveredFieldsRook(IPiece[][] chessBoard , int x , int y , IPiece.Color color)
    {
        int i = 0 , coveredFields = 0;
        IPiece piece = null;
        for(i = x-1 ; i>=0 ; i--)
        {
            piece = chessBoard[i][y];
            if(piece == null) {
                coveredFields++;
            }
            else if(piece.getColor() == color) {
                break;
            }
            else {
                coveredFields++;
                break;
            }
        }
        for(i = x+1 ; i<8 ; i++)
        {
            piece = chessBoard[i][y];
            if(piece == null) {
                coveredFields++;
            }
            else if(piece.getColor() == color) {
                break;
            }
            else {
                coveredFields++;
                break;
            }
        }
        for(i = y-1 ; i>=0 ; i--)
        {
            piece = chessBoard[x][i];
            if(piece == null) {
                coveredFields++;
            }
            else if(piece.getColor() == color) {
                break;
            }
            else {
                coveredFields++;
                break;
            }
        }
        for(i = y+1 ; i<8 ; i++)
        {
            piece = chessBoard[x][i];
            if(piece == null) {
                coveredFields++;
            }
            else if(piece.getColor() == color) {
                break;
            }
            else {
                coveredFields++;
                break;
            }
        }
        return coveredFields;

    }

    private static int distToCenter(int x , int y)
    {
        double dx = x-3.5;
        double dy = y-3.5;
        return (int)Math.sqrt(dx*dx+dy*dy);
    }
}
