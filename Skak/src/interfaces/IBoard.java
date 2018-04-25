package interfaces;

import java.awt.Point;
import java.util.ArrayList;

import data.Board;
import interfaces.IPiece.Color;
import piece.Move;


public interface IBoard {

	
	
	ArrayList<IPiece> getPieces();
	ArrayList<IBoard> getChildBoards();
	Move getMove();
	boolean outOfBounds(Point p);
	boolean allyPiecePresent(Point p, Color color);
	boolean enemyPiecePresent(Point p, Color color);
	boolean isCheckmate(Point p);
	
	
	IPiece getPiece(Point p);
	void setPieceNull(Point p);
	void setPiece(Point p, IPiece piece);
	Point getEnPassant();
	void setEnPassant(Point enPassant);
	void setTurn(Color turn);
	Color getTurn();
	void setBlackShortCastle(boolean blackShortCastle);
	boolean isBlackShortCastle();
	void setBlackLongCastle(boolean blackLongCastle);
	boolean isBlackLongCastle();
	void setWhiteShortCastle(boolean whiteShortCastle);
	boolean isWhiteShortCastle();
	void setWhiteLongCastle(boolean whiteLongCastle);
	boolean isWhiteLongCastle();
	void setAdditionalPoints(int additionalPoints);
	int getAdditionalPoints();
	void addChildBoard(IBoard newBoard);
	boolean isFieldthreatened(Point field);
	void generateNewBoardState(Board oldBoard, Move m);
	IPiece[][] getChessBoard();

	
	
	
}
