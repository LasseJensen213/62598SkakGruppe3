package data;

import java.awt.Point;
import java.util.ArrayList;

import interfaces.IBoard;
import interfaces.IPiece;
import interfaces.IPiece.Color;
import interfaces.IPiece.Type;
import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Move;
import piece.Pawn;
import piece.Queen;
import piece.Rook;

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
	private int fullmoveNumber = 1;
	private int halfMoveClock = 0;
	private Color turn = Color.WHITE;
	private King blackKing;
	private King whiteKing;
	private boolean isChecked = false;
	private static int[] knightMoves = {0x21 , 0x1F , 0x12 , 0x0E , -0x21 , -0x1F , -0x12 , -0x0E}; // Mainly using this for checking if king is in check

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

		this.chessBoard = new IPiece[8][8];
		if(newMove.isOffensive() || newMove.getMovingPiece().getType() == Type.Pawn)
			this.halfMoveClock=0;
		else
			this.halfMoveClock=oldBoard.halfMoveClock+1;


		//this.chessBoard = tempBoard;
//
		for(int i = 0;i<8;i++) {
			for(int j = 0;j<8;j++) {
				if(oldBoard.chessBoard[i][j] != null) {
					switch(oldBoard.chessBoard[i][j].getType()) {
					case Bishop:
						this.chessBoard[i][j] = (Bishop) new Bishop(oldBoard.chessBoard[i][j]);
						break;
					case King:
						this.chessBoard[i][j] = (King) new King(oldBoard.chessBoard[i][j]);

						if(chessBoard[i][j].getColor() == Color.WHITE)
							this.whiteKing = (King)chessBoard[i][j];
						else
							this.blackKing = (King)chessBoard[i][j];
						break;
					case Knight:
						this.chessBoard[i][j] = (Knight) new Knight(oldBoard.chessBoard[i][j]);
						break;
					case Pawn:
						this.chessBoard[i][j] = (Pawn) new Pawn(oldBoard.chessBoard[i][j]);
						break;
					case Queen:
						this.chessBoard[i][j] = (Queen) new Queen(oldBoard.chessBoard[i][j]);
						break;
					case Rook:
						this.chessBoard[i][j] = (Rook) new Rook(oldBoard.chessBoard[i][j]);

						break;
					default:
						break;
					
					}
				}
				
				
				
			}
		}
		
		this.additionalPoints += newMove.getAdditionalPoints();

		if (oldBoard.getTurn().equals(Color.WHITE)) {
			this.turn = Color.BLACK;
			this.fullmoveNumber = oldBoard.fullmoveNumber;
		} else {
			this.turn = Color.WHITE;
			this.fullmoveNumber = oldBoard.fullmoveNumber+1;
		}
		this.isChecked = newMove.getIsCheckMove();
		if(this.isChecked)
		{
			if(this.turn == Color.WHITE)
				this.whiteKing.setCheck(true);
			else
				this.blackKing.setCheck(true);
		}

		IPiece piece = this.chessBoard[newMove.getEndCoor().x][newMove.getEndCoor().y];
		if(piece != null && piece.getType() == Type.Rook)
		{
			if(this.turn == Color.WHITE)
			{
				if(this.whiteLongCastle)
				{
					if(piece.getCoordinates().x == 0 && piece.getCoordinates().y == 0)
						this.whiteLongCastle = false;
				}
				if(this.whiteShortCastle)
				{
					if(piece.getCoordinates().x == 7 && piece.getCoordinates().y == 0)
						this.whiteShortCastle = false;
				}
			}
			else{
				if(this.blackLongCastle)
				{
					if(piece.getCoordinates().x == 0 && piece.getCoordinates().y==7)
						this.blackLongCastle = false;
				}
				if (this.blackShortCastle)
				{
					if(piece.getCoordinates().x == 7 && piece.getCoordinates().y == 7)
						this.blackShortCastle = false;
				}
			}
		}






		switch (newMove.getMovingPiece().getType()) {
		case Bishop:
			this.getPiece(newMove.getStartCoor()).setCoordinates(newMove.getEndCoor());
			this.setPiece(newMove.getEndCoor(), this.getPiece(newMove.getStartCoor()));
			setPieceNull(newMove.getStartCoor());

			if (newMove.isSpecial()) {
				chessBoard[newMove.getEndCoor().x][newMove.getEndCoor().y] = newMove.getMovingPiece();
			}
			break;

		case King:
			this.getPiece(newMove.getStartCoor()).setCoordinates(newMove.getEndCoor());
			this.setPiece(newMove.getEndCoor(), this.getPiece(newMove.getStartCoor()));
			((King)newMove.getMovingPiece()).setUnmoved(false);
			setPieceNull(newMove.getStartCoor());
			switch (newMove.getMovingPiece().getColor()) {
			case BLACK:
				this.blackLongCastle = false;
				this.blackShortCastle = false;
				if(newMove.getSpecialMove() == Move.SpecialMove.LONG_CASTLE)
				{
					IPiece rook = this.chessBoard[0][7];
					this.chessBoard[0][7] = null;
					this.chessBoard[3][7] = rook;
					rook.setCoordinates(new Point(3,7));
				}
				else if(newMove.getSpecialMove() == Move.SpecialMove.SHORT_CASTLE)
				{
					IPiece rook = this.chessBoard[7][7];
					this.chessBoard[7][7] = null;
					this.chessBoard[5][7] = rook;
					rook.setCoordinates(new Point(5 ,7));
				}
				break;
			case WHITE:
				this.whiteLongCastle = false;
				this.whiteShortCastle = false;
				if(newMove.getSpecialMove() == Move.SpecialMove.LONG_CASTLE)
				{
					IPiece rook = this.chessBoard[0][0];
					this.chessBoard[0][0] = null;
					this.chessBoard[3][0] = rook;
					rook.setCoordinates(new Point(3 ,0));
				}
				else if(newMove.getSpecialMove() == Move.SpecialMove.SHORT_CASTLE)
				{
					IPiece rook = this.chessBoard[7][0];
					this.chessBoard[7][0] = null;
					this.chessBoard[5][0] = rook;
					rook.setCoordinates(new Point(5 ,0));
				}
				break;
			default:
				break;

			}
			break;

		case Knight:
			this.getPiece(newMove.getStartCoor()).setCoordinates(newMove.getEndCoor());
			this.setPiece(newMove.getEndCoor(), this.getPiece(newMove.getStartCoor()));
			this.setPiece(newMove.getStartCoor(),null);
			//setPieceNull(newMove.getStartCoor());

			if (newMove.isSpecial()) {
				chessBoard[newMove.getEndCoor().x][newMove.getEndCoor().y] = newMove.getMovingPiece();
			}
			break;

		case Pawn:
			int distY = (int) (newMove.getStartCoor().getY() - newMove.getEndCoor().getY());
			this.halfMoveClock = 0;
			if (distY == 2 || distY == -2) {
				// Pawn moves two fields forward. Set the field it passed over to en Passant
				// point
				Point enPassantPoint = new Point();
				switch (newMove.getMovingPiece().getColor()) {
				case BLACK:
					enPassantPoint.setLocation(newMove.getStartCoor().getX(), 5);
					this.setEnPassant(enPassantPoint);
					break;
				case WHITE:
					enPassantPoint.setLocation(newMove.getStartCoor().getX(), 2);
					this.setEnPassant(enPassantPoint);
					break;
				default:
					break;

				}

			}
			
			this.getPiece(newMove.getStartCoor()).setCoordinates(newMove.getEndCoor());
			this.setPiece(newMove.getEndCoor(), this.getPiece(newMove.getStartCoor()));
			setPieceNull(newMove.getStartCoor());

			if (newMove.isSpecial()) {
				Point pieceToTake = new Point();
				switch (newMove.getMovingPiece().getColor()) {
				case BLACK:
					pieceToTake.setLocation(newMove.getEndCoor().getX(), 3);

					break;
				case WHITE:
					pieceToTake.setLocation(newMove.getEndCoor().getX(), 4);

					break;
				default:
					break;

				}
				setPieceNull(pieceToTake);
			}
			break;
		case Queen:
			this.getPiece(newMove.getStartCoor()).setCoordinates(newMove.getEndCoor());
			this.setPiece(newMove.getEndCoor(), this.getPiece(newMove.getStartCoor()));
			setPieceNull(newMove.getStartCoor());
			if (newMove.isSpecial()) {
				chessBoard[newMove.getEndCoor().x][newMove.getEndCoor().y] = newMove.getMovingPiece();
			}

			break;
		case Rook:
			if (whiteLongCastle) {
				Point a1Rook = new Point();
				a1Rook.setLocation(0, 0);
				if (newMove.getStartCoor().equals(a1Rook)) {
					this.whiteLongCastle = false;
				}

			}
			if (whiteShortCastle) {
				Point h1Rook = new Point();
				h1Rook.setLocation(7, 0);
				if (newMove.getStartCoor().equals(h1Rook)) {
					this.whiteShortCastle = false;
				}

			}
			if (blackLongCastle) {
				Point a8Rook = new Point();
				a8Rook.setLocation(0, 7);
				if (newMove.getStartCoor().equals(a8Rook)) {
					this.blackLongCastle = false;
				}

			}
			if (blackShortCastle) {
				Point h8Rook = new Point();
				h8Rook.setLocation(7, 7);
				if (newMove.getStartCoor().equals(h8Rook)) {
					this.blackShortCastle = false;
				}

			}

			this.getPiece(newMove.getStartCoor()).setCoordinates(newMove.getEndCoor());
			this.setPiece(newMove.getEndCoor(), this.getPiece(newMove.getStartCoor()));
			setPieceNull(newMove.getStartCoor());

			if (newMove.isSpecial()) {
				chessBoard[newMove.getEndCoor().x][newMove.getEndCoor().y] = newMove.getMovingPiece();
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
		ArrayList<IPiece> pieces = new ArrayList<IPiece>(); // The other pieces.
		ArrayList<IPiece> finalList = new ArrayList<IPiece>(); // All the pieces, king first.
		IPiece temp;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				temp = chessBoard[i][j];

				if (temp == null)
					continue;
				if (temp.getColor().equals(this.turn)) {
					
						pieces.add(temp);
					
				}
			}
		}
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
		return (p.getX() < 0 || p.getY() < 0 || p.getX() > 7 || p.getY() > 7);
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
		// System.out.println("OldBoard: " + oldBoard.toString());
		Board newBoardState = new Board(oldBoard, m);
		oldBoard.addChildBoard(newBoardState);
		// System.out.println(m.toString());
	}

	@Override
	public IPiece getPiece(Point p) {
		return chessBoard[((int) p.getX())][((int) p.getY())];
	}

	@Override
	public void setPieceNull(Point p) {
		this.chessBoard[((int) p.getX())][((int) p.getY())] = null;
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
		String toPrint = "\n-----------------\n";
		for (int i = 7; i >= 0; i--) {
			for (int j = 0; j <= 7; j++) {
				if (chessBoard[j][i] == null) {
					toPrint += "| ";
				} else {
					toPrint += "|"+chessBoard[j][i];
				}

			}
			toPrint += "|\n-----------------\n";
		}
		return "Board: \n" + toPrint;
	}

	public IPiece[][] getChessBoard() {
		return chessBoard;
	}

	@Override
	public int getFullMoves() {
		return fullmoveNumber;
	}

	@Override
	public void setFullMoves(int fullMoveNumber) {
		this.fullmoveNumber = fullMoveNumber;
	}

	@Override
	public int getHalfMoveClock() {
		return halfMoveClock;
	}

	@Override
	public void setHalfMoveClock(int halfMoveClock) {
		this.halfMoveClock = halfMoveClock;
	}

	@Override
	public boolean isKingInCheckAfterMove(Color color , Move move) {
		King currentKing = null;
		King adversaryKing = null;
		IPiece possibleCapture = chessBoard[move.getEndCoor().x][move.getEndCoor().y]; // Need to save this for when undoing move
		chessBoard[move.getStartCoor().x][move.getStartCoor().y] = null;
		chessBoard[move.getEndCoor().x][move.getEndCoor().y] = move.getMovingPiece();
		int kingX , kingY;
		if(color == Color.WHITE)
		{
			currentKing = this.whiteKing;
			adversaryKing = this.blackKing;
		}
		else
		{
			currentKing = this.blackKing;
			adversaryKing = this.whiteKing;
		}
		if(color == this.turn)
		{
			if(move.getMovingPiece().getType() == Type.King)
			{
				kingX = move.getEndCoor().x;
				kingY = move.getEndCoor().y;
				int dx = Math.abs(kingX - adversaryKing.getCoordinates().x);
				int dy = Math.abs(kingY - adversaryKing.getCoordinates().y);
				if((dx == 0 || dx == 1) && (dy == 0 || dy==1))
				{
					chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
					chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
					return true;
				}
			}
			else
			{
				kingX = currentKing.getCoordinates().x;
				kingY = currentKing.getCoordinates().y;
			}
		}
		else
		{
			kingX = currentKing.getCoordinates().x;
			kingY = currentKing.getCoordinates().y;
		}

		int leftFields = kingX; 									 				 //Distance to left border
		int rightFields = 7 - leftFields;          									 //Distance to right border
		int bottomFields = kingY;									 				 //Distance to bottom border
		int topFields = 7-bottomFields;												 //Distance to top border
		int leftLowerDiag = leftFields < bottomFields ? leftFields : bottomFields;   //How many lower left diagonal fields before hitting either left or bottom border
		int leftUpperDiag = leftFields < topFields ? leftFields : topFields;		 //How many upper left diagonal fields before hitting either left or top border
		int rightLowerDiag = rightFields < bottomFields ? rightFields : bottomFields;//How many lower right diagonal fields before hitting either right or bottom border
		int rightUpperDiag = rightFields < topFields ? rightFields : topFields;		 //How many upper right diagonal fields before hitting either right or top border
		int i = 0 , x ,y;
		x = kingX;
		y = kingY;
		IPiece piece = null;

		for(i = 1 ; i<=leftFields ; i++) // Check to the left for enemy rooks or queens
		{
			x--;
			piece = chessBoard[x][y];
			if(piece != null)
			{
				if(piece.getColor() == currentKing.getColor()) // Ally piece is blocking this passage
					break;
				else
				{
					if(piece.getType() == Type.Rook || piece.getType() == Type.Queen)
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}
					break; // Break because the enenemy piece is blocking for its own pieces
				}
			}
		}
		x = kingX;
		y = kingY;
		for(i = 1 ; i<=rightFields ; i++) // Check to the right for enemy rooks or queens
		{
			x++;
			piece = chessBoard[x][y];
			if(piece != null)
			{
				if(piece.getColor() == currentKing.getColor()) // Ally piece is blocking this passage
					break;
				else
				{
					if(piece.getType() == Type.Rook || piece.getType() == Type.Queen)
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}
					break; // Break because the enenemy piece is blocking for its own pieces
				}
			}
		}
		x = kingX;
		y = kingY;
		for(i = 1 ; i<=bottomFields ; i++) // Check to the bottom for enemy rooks or queens
		{
			y--;
			piece = chessBoard[x][y];
			if(piece != null)
			{
				if(piece.getColor() == currentKing.getColor()) // Ally piece is blocking this passage
					break;
				else
				{
					if(piece.getType() == Type.Rook || piece.getType() == Type.Queen)
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}
					break; // Break because the enenemy piece is blocking for its own pieces
				}
			}
		}
		x = kingX;
		y = kingY;
		for(i = 1 ; i<=topFields ; i++) // Check to the top for enemy rooks or queens
		{
			y++;
			piece = chessBoard[x][y];
			if(piece != null)
			{
				if(piece.getColor() == currentKing.getColor()) // Ally piece is blocking this passage
					break;
				else
				{
					if(piece.getType() == Type.Rook || piece.getType() == Type.Queen)
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}
					break; // Break because the enenemy piece is blocking for its own pieces
				}

			}
		}
		//Need to check for enemy pawns now
		if(currentKing.getColor() == Color.WHITE && kingY!=7)
		{
			if(kingX != 0)
			{
				piece = chessBoard[kingX-1][kingY+1];
				if(piece != null && piece.getColor() != currentKing.getColor() && piece.getType() == Type.Pawn)
				{
					//Need to check that we can't attack this piece with en passant
					if(move.getMovingPiece().getType() == Type.Pawn && enPassant != null && enPassant.equals(move.getEndCoor()))
					{

					}
					else
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}
				}
			}
			if(kingX != 7)
			{
				piece = chessBoard[kingX+1][kingY+1];
				if(piece != null && piece.getColor() != currentKing.getColor() && piece.getType() == Type.Pawn)
				{
					if(move.getMovingPiece().getType() == Type.Pawn && enPassant != null && enPassant.equals(move.getEndCoor()))
					{

					}
					else
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}

				}
			}
		}
		else if(currentKing.getColor() == Color.BLACK && kingY != 0)
		{

			if(kingX != 0)
			{
				piece = chessBoard[kingX-1][kingY-1];
				if(piece != null && piece.getColor() != currentKing.getColor() && piece.getType() == Type.Pawn)
				{
					if(move.getMovingPiece().getType() == Type.Pawn && enPassant != null && enPassant.equals(move.getEndCoor()))
					{

					}
					else
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}
				}
			}
			if(kingX != 7)
			{
				piece = chessBoard[kingX+1][kingY-1];
				if(piece != null && piece.getColor() != currentKing.getColor() && piece.getType() == Type.Pawn)
				{
					if(move.getMovingPiece().getType() == Type.Pawn && enPassant != null && enPassant.equals(move.getEndCoor()))
					{

					}
					else
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}
				}
			}
		}

		//Checking diagonals now for bishops / Queens
		x = kingX;
		y = kingY;
		for(i = 1 ; i<=leftLowerDiag ; i++)
		{
			x--;
			y--;
			piece = chessBoard[x][y];
			if(piece != null)
			{
				if(piece.getColor() == currentKing.getColor()) // Ally piece is blocking this passage
					break;
				else
				{
					if(piece.getType() == Type.Bishop || piece.getType() == Type.Queen)
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}
					break; // Break because the enenemy piece is blocking for its own pieces
				}
			}
		}
		x = kingX;
		y = kingY;
		for(i = 1 ; i<=leftUpperDiag ; i++)
		{
			x--;
			y++;
			piece = chessBoard[x][y];
			if(piece != null)
			{
				if(piece.getColor() == currentKing.getColor()) // Ally piece is blocking this passage
					break;
				else
				{
					if(piece.getType() == Type.Bishop || piece.getType() == Type.Queen)
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}
					break; // Break because the enenemy piece is blocking for its own pieces
				}
			}
		}
		x = kingX;
		y = kingY;
		for(i = 1 ; i<=rightLowerDiag ; i++)
		{
			x++;
			y--;
			piece = chessBoard[x][y];
			if(piece != null)
			{
				if(piece.getColor() == currentKing.getColor()) // Ally piece is blocking this passage
					break;
				else
				{
					if(piece.getType() == Type.Bishop || piece.getType() == Type.Queen)
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}
					break; // Break because the enenemy piece is blocking for its own pieces
				}
			}
		}
		x = kingX;
		y = kingY;
		for(i = 1 ; i<=rightUpperDiag ; i++)
		{
			x++;
			y++;
			piece = chessBoard[x][y];
			if(piece != null)
			{
				if(piece.getColor() == currentKing.getColor()) // Ally piece is blocking this passage
					break;
				else
				{
					if(piece.getType() == Type.Bishop || piece.getType() == Type.Queen)
					{
						chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
						chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
						return true;
					}
					break; // Break because the enenemy piece is blocking for its own pieces
				}
			}
		}
		//Now we check if a knight is in range
		int hexPos = kingX + (kingY<<4) , newPos;
		for(i = 0 ; i<8 ; i++)
		{
			newPos = knightMoves[i] + hexPos;
			if( (newPos & 0x88) == 0 )
			{
				piece = chessBoard[newPos&15][(newPos&240)>>4];
				if(piece != null && piece.getColor() != currentKing.getColor() && piece.getType() == Type.Knight)
				{
					chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
					chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
					return true;
				}
			}


		}


		chessBoard[move.getEndCoor().x][move.getEndCoor().y] = possibleCapture;
		chessBoard[move.getStartCoor().x][move.getStartCoor().y] = move.getMovingPiece(); // Undoing the move
		return false;
	}

	@Override
	public void setBlackKing(King king) {
		this.blackKing = king;
	}

	@Override
	public void setWhiteKing(King king) {
		this.whiteKing = king;
	}

	@Override
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}


}
