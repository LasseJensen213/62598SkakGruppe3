package controller;

import data.Board;
import fen.FEN;
import fen.InvalidFENStringException;
import interfaces.IBoard;
import interfaces.IPiece;
import piece.*;

import java.awt.*;

//Singleton class controlling the game
public class GameController {
    public IBoard board;
    public static String startFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    static GameController gameController;


    private GameController(){
        try {
            board = FEN.decode(startFEN);//Default start board
            FEN.saveToFile("**********************NEW GAME**********************");
            FEN.saveToFile(FEN.encode((Board) board));
        } catch (InvalidFENStringException e) {
            e.printStackTrace();//Should not happen
        }
    }

    public static GameController getInstance() {
        if(gameController == null){
            gameController = new GameController();
        }
        return gameController;
    }

    public void makeMove(Move move){
        //TODO: Check if move is legal

        board = new Board((Board) board, move);

    }

    public void makeMove(String move){//Converts string (eg d2d4) move to normal move
        //TODO: Check if move is legal
        int fromX = (int)(move.charAt(0)-'a');
        int fromY = (int)(move.charAt(1)-'0')-1;
        int toX = (int)(move.charAt(2)-'a');
        int toY = (int)(move.charAt(3)-'0')-1;
        Point startcoor = new Point(fromX , fromY);
        Point endcoor = new Point(toX , toY);
        IPiece piece = board.getPiece(startcoor);
        IPiece other = board.getPiece(endcoor);
        boolean offensive = false;
        if(other != null)
            offensive = true;
        Move newMove = new Move(piece , startcoor,endcoor);
        newMove.setOffensive(offensive);
        if(piece.getType() == IPiece.Type.King)
        {
            if((fromX-toX) == 2)
                newMove.setSpecialMove(Move.SpecialMove.LONG_CASTLE);
            else if((fromX-toX) == -2)
                newMove.setSpecialMove(Move.SpecialMove.SHORT_CASTLE);
        }
        else if(piece.getType() == IPiece.Type.Pawn)
            {
                if(board.getEnPassant() != null && board.getEnPassant().equals(new Point(toX, toY))){
                    newMove.setSpecialMove(Move.SpecialMove.EN_PASSANT);
                    newMove.setSpecial(true);
                }

            }
        newMove.setIsCheckMove(board.isKingInCheckAfterMove((board.getTurn() == IPiece.Color.BLACK ? IPiece.Color.WHITE : IPiece.Color.BLACK) , newMove));
        makeMove(newMove);
    }

    public void makePromotionMove(String move)
    {
        int fromX = (int)(move.charAt(0)-'a');
        int fromY = (int)(move.charAt(1)-'0')-1;
        int toX = (int)(move.charAt(2)-'a');
        int toY = (int)(move.charAt(3)-'0')-1;
        char promotedTo = move.charAt(4);
        Point endcoor = new Point(toX , toY);
        Point startcoor = new Point(fromX , fromY);
        IPiece piece = board.getPiece(startcoor);
        IPiece other = board.getPiece(endcoor);
        boolean offensive = false;
        if(other != null)
            offensive = true;
        if(promotedTo == 'q')
            piece = new Queen(piece.getColor() , endcoor);
        else if(promotedTo=='r')
            piece = new Rook(piece.getColor() , endcoor);
        else if(promotedTo=='n')
            piece = new Knight(piece.getColor() , endcoor);
        else
            piece = new Bishop(piece.getColor() , endcoor);
        Move newMove = new Move(piece , startcoor,endcoor);
        newMove.setOffensive(offensive);
        newMove.setSpecial(true);
        newMove.setIsCheckMove(board.isKingInCheckAfterMove((board.getTurn() == IPiece.Color.BLACK ? IPiece.Color.WHITE : IPiece.Color.BLACK) , newMove));
        makeMove(newMove);


    }

    public Move getAIMove(){
        return TimedAlgorithm.getINSTANCE().getAIMove();
    }
}
