package data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import interfaces.IBoard;
import interfaces.IPiece;
import interfaces.IPiece.Color;
import interfaces.IPiece.Type;
import piece.Move;

public class Board implements IBoard {

	private IPiece[][] chessBoard;
	private ArrayList<IBoard> childBoards;
	private int additionalPoints = 0;
	private Point enPassant = null;
	private Move createdByMove = null; // only non-null for child boards
	private boolean whiteLongCastle = true;
	private boolean whiteShortCastle = true;
	private boolean blackLongCastle = true;
	private boolean blackShortCastle = true;

	private Color turn = Color.WHITE;

	public Board() {
		chessBoard = new IPiece[8][8];
		childBoards = new ArrayList<>();
	}

	public Board(Board oldBoard, Move newMove) {
		this.childBoards = new ArrayList<>();
		this.whiteLongCastle = oldBoard.isWhiteLongCastle();
		this.whiteShortCastle = oldBoard.isWhiteShortCastle();
		this.blackLongCastle = oldBoard.isBlackLongCastle();
		this.blackShortCastle = oldBoard.isBlackShortCastle();

		this.createdByMove = newMove;
		this.chessBoard = oldBoard.chessBoard.clone();
		this.additionalPoints += newMove.getAdditionalPoints();
		if (oldBoard.getTurn().equals(Color.WHITE)) {
			this.turn = Color.BLACK;
		} else {
			this.turn = Color.WHITE;
		}

		switch (newMove.getMovingPiece().getType()) {
		case Bishop:
			newMove.getMovingPiece().setCoordinates(newMove.getEndCoor());
			setPiece(newMove.getEndCoor(), newMove.getMovingPiece());
			setPieceNull(newMove.getStartCoor());

			if (newMove.isSpecial()) {
				System.out.println("Pawn promo to bishop");
			}
			break;

		case King:
			newMove.getMovingPiece().setCoordinates(newMove.getEndCoor());
			setPiece(newMove.getEndCoor(), newMove.getMovingPiece());
			setPieceNull(newMove.getStartCoor());
			switch (newMove.getMovingPiece().getColor()) {
			case BLACK:
				blackLongCastle = false;
				blackShortCastle = false;
				break;
			case WHITE:
				whiteLongCastle = false;
				whiteShortCastle = false;
				break;
			default:
				break;

			}
			break;

		case Knight:
			setPieceNull(newMove.getStartCoor());
			newMove.getMovingPiece().setCoordinates(newMove.getEndCoor());
			setPiece(newMove.getEndCoor(), newMove.getMovingPiece());
			setPieceNull(newMove.getStartCoor());

			if (newMove.isSpecial()) {
				System.out.println("Pawn promo to knight");
			}
			break;

		case Pawn:
			int distY = (int) (newMove.getEndCoor().getY() - newMove.getEndCoor().getY());
			if (distY == 2 || distY == -2) {
				// Pawn moves two fields forward. Set the field it passed over to en Passant
				// point
				Point enPassantPoint = new Point();
				switch (newMove.getMovingPiece().getColor()) {
				case BLACK:
					enPassantPoint.setLocation(newMove.getStartCoor().getX(), 6);
					this.setEnPassant(enPassantPoint);
					break;
				case WHITE:
					enPassantPoint.setLocation(newMove.getStartCoor().getX(), 3);
					this.setEnPassant(enPassantPoint);
					break;
				default:
					break;

				}

			}

			newMove.getMovingPiece().setCoordinates(newMove.getEndCoor());
			setPiece(newMove.getEndCoor(), newMove.getMovingPiece());
			setPieceNull(newMove.getStartCoor());

			if (newMove.isSpecial()) {
				Point pieceToTake = new Point();
				switch (newMove.getMovingPiece().getColor()) {
				case BLACK:
					pieceToTake.setLocation(newMove.getEndCoor().getX(), 4);

					break;
				case WHITE:
					pieceToTake.setLocation(newMove.getEndCoor().getX(), 5);

					break;
				default:
					break;

				}
				setPieceNull(pieceToTake);
			}
			break;
		case Queen:
			setPieceNull(newMove.getStartCoor());
			newMove.getMovingPiece().setCoordinates(newMove.getEndCoor());
			setPiece(newMove.getEndCoor(), newMove.getMovingPiece());
			if (newMove.isSpecial()) {
				System.out.println("Pawn promo to queen");
			}

			break;
		case Rook:

			if (whiteLongCastle) {
				Point a1Rook = new Point();
				a1Rook.setLocation(1, 1);
				if (newMove.getStartCoor().equals(a1Rook)) {
					this.whiteLongCastle = false;
				}

			}
			if (whiteShortCastle) {
				Point h1Rook = new Point();
				h1Rook.setLocation(8, 1);
				if (newMove.getStartCoor().equals(h1Rook)) {
					this.whiteShortCastle = false;
				}

			}
			if (blackLongCastle) {
				Point a8Rook = new Point();
				a8Rook.setLocation(1, 8);
				if (newMove.getStartCoor().equals(a8Rook)) {
					this.blackLongCastle = false;
				}

			}
			if (blackShortCastle) {
				Point h8Rook = new Point();
				h8Rook.setLocation(8, 8);
				if (newMove.getStartCoor().equals(h8Rook)) {
					this.blackShortCastle = false;
				}

			}

			newMove.getMovingPiece().setCoordinates(newMove.getEndCoor());
			setPiece(newMove.getEndCoor(), newMove.getMovingPiece());
			setPieceNull(newMove.getStartCoor());

			if (newMove.isSpecial()) {
				System.out.println("Pawn promo to rook");
			}
			break;
		default:
			break;

		}

	}

	/**
	 * 
	 */
	@Override
	public ArrayList<IPiece> getPieces() {
		IPiece king = null; // We need the king separately. If king is in check check, we need to find out
		// first.
		ArrayList<IPiece> pieces = new ArrayList<IPiece>(); // The other pieces.
		ArrayList<IPiece> finalList = new ArrayList<IPiece>(); // All the pieces, king first.
		IPiece temp;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				temp = chessBoard[i][j];

				if (temp == null)
					continue;
				if (temp.getColor().equals(this.turn)) {
					if (temp.getType() == Type.King) {
						king = temp;
					} else {
						pieces.add(temp);
					}
				}
			}
		}
		finalList.add(king);
		finalList.addAll(pieces);

		return finalList;
	}

	@Override
	public ArrayList<IBoard> getChildBoards() {
		return childBoards;
	}

	@Override
	public Move getMove() {
		return createdByMove;
	}

	@Override
	public void addChildBoard(IBoard newBoard) {
		this.childBoards.add(newBoard);
	}

	@Override
	public boolean outOfBounds(Point p) {
		if (p.getX() < 0 || p.getY() < 0 || p.getX() > 7 || p.getY() > 7) {
			return true;
		}
		return false;
	}

	@Override
	public boolean allyPiecePresent(Point p, Color color) {
		if (this.getPiece(p) == null) {
			return false;
		}
		if (this.getPiece(p).getColor().equals(color)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean enemyPiecePresent(Point p, Color color) {
		if (this.getPiece(p) == null) {
			return false;
		}
		if (this.getPiece(p).getColor().equals(color)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isCheckmate(Point p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void generateNewBoardState(Board oldBoard, Move m) {
		Board newBoardState = new Board(oldBoard, m);
		this.childBoards.add(newBoardState);
		// System.out.println(m.toString());
	}

	@Override
	public IPiece getPiece(Point p) {
		return chessBoard[((int) p.getX())][((int) p.getY())];
	}

	@Override
	public void setPieceNull(Point p) {
		chessBoard[((int) p.getX())][((int) p.getY())] = null;
	}

	@Override
	public void setPiece(Point p, IPiece piece) {
		chessBoard[((int) p.getX())][((int) p.getY())] = piece;
	}

	@Override
	public Point getEnPassant() {
		return enPassant;
	}

	@Override
	public void setEnPassant(Point enPassant) {
		this.enPassant = enPassant;
	}

	@Override
	public int getAdditionalPoints() {
		return additionalPoints;
	}

	@Override
	public void setAdditionalPoints(int additionalPoints) {
		this.additionalPoints = additionalPoints;
	}

	@Override
	public boolean isWhiteLongCastle() {
		return whiteLongCastle;
	}

	@Override
	public void setWhiteLongCastle(boolean whiteLongCastle) {
		this.whiteLongCastle = whiteLongCastle;
	}

	@Override
	public boolean isWhiteShortCastle() {
		return whiteShortCastle;
	}

	@Override
	public void setWhiteShortCastle(boolean whiteShortCastle) {
		this.whiteShortCastle = whiteShortCastle;
	}

	@Override
	public boolean isBlackLongCastle() {
		return blackLongCastle;
	}

	@Override
	public void setBlackLongCastle(boolean blackLongCastle) {
		this.blackLongCastle = blackLongCastle;
	}

	@Override
	public boolean isBlackShortCastle() {
		return blackShortCastle;
	}

	@Override
	public void setBlackShortCastle(boolean blackShortCastle) {
		this.blackShortCastle = blackShortCastle;
	}

	@Override
	public Color getTurn() {
		return turn;
	}

	@Override
	public void setTurn(Color turn) {
		this.turn = turn;
	}

	@Override
	public boolean isFieldthreatened(Point field) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		String toPrint = "";
		for (int i = 0; i <= 7; i++) {
			for (int j = 0; j <= 7; j++) {
				if(chessBoard[j][i] == null) {
					toPrint += " ";
				}
				else {
					switch(chessBoard[j][i].getType()) {
					case Bishop:
						toPrint += "b";
						break;
					case King:
						toPrint += "k";
						break;
					case Knight:
						toPrint += "s";
						break;
					case Pawn:
						toPrint += "p";
						break;
					case Queen:
						toPrint += "q";
						break;
					case Rook:
						toPrint += "r";
						break;
					default:
						toPrint += " ";
						break;
					
					}
				}
				
				
			}
			toPrint += "\n";
		}
		return "Board: \n" + toPrint;
	}

}
