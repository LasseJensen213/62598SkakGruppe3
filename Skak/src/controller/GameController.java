package controller;

import data.Board;
import fen.FEN;
import fen.InvalidFENStringException;
import interfaces.IBoard;
import interfaces.IPiece;
import piece.Move;

import java.awt.*;

//Singleton class controlling the game
public class GameController {
    public IBoard board;
    static GameController gameController;

    private GameController(){
        try {
            board = FEN.decode("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");//Default start board
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
        IPiece piece = board.getPiece(move.getStartCoor());
        board.setPiece(move.getEndCoor(), piece);
        board.setPieceNull(move.getStartCoor());
    }

    public void makeMove(String move){//Converts string (eg d2d4) move to normal move
        //TODO: Check if move is legal
        int fromX = move.charAt(0)-'a'+1;
        int toX = move.charAt(2)-'a'+1;
        makeMove(new Move(board.getPiece(new Point(fromX, (int)move.charAt(1))),new Point(fromX, (int)move.charAt(1)),new Point(toX, (int)move.charAt(3))));
    }

    public Move getAIMove(){
        return Algorithm.makeMove(board);
    }
}
