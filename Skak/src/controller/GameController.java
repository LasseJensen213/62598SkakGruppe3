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
        board = new Board((Board) board, move);
    }

    public void makeMove(String move){//Converts string (eg d2d4) move to normal move
        //TODO: Check if move is legal
        int fromX = (int)move.charAt(0)-'a';
        int fromY = (int)move.charAt(1)-'0';
        int toX = (int)move.charAt(2)-'a';
        int toY = (int)move.charAt(3)-'0';
        makeMove(new Move(board.getPiece(new Point(fromX, fromY)),new Point(fromX, fromY),new Point(toX, toY)));
    }

    public Move getAIMove(){
        return Algorithm.makeMove(board);
    }
}
