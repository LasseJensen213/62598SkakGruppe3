package piece;

import java.awt.Point;

import interfaces.IPiece;

public class Move {

	public enum SpecialMove{
		EN_PASSANT,
		LONG_CASTLE,
		SHORT_CASTLE,
		PROMOTION_QUEEN,
		PROMOTION_KNIGHT,
		PROMOTION_ROOK,
		PROMOTION_BISHOP
	}
	private IPiece movingPiece; // The actual moving piece.
	private Point startCoor; // Where it comes from
	private Point endCoor; // Where it will go
	private boolean offensive; // Taking a piece
	private int additionalPoints = 0; // Add extra points to the move.
	private boolean special = false; // For en passant and pawn evolution.
	private boolean checkMove = false; //This move checking other player
	private SpecialMove specialMove;
	private Rook castlingRook;
	public Rook getCastlingRook() {
		return castlingRook;
	}

	public void setCastlingRook(Rook castlingRook) {
		this.castlingRook = castlingRook;
	}


	public SpecialMove getSpecialMove() {
		return specialMove;
	}

	public void setSpecialMove(SpecialMove specialMove) {
		this.specialMove = specialMove;
	}



	public Move(IPiece movingPiece, Point startCoor, Point endCoor) {
		super();
		this.movingPiece = movingPiece;
		this.startCoor = startCoor;
		this.endCoor = endCoor;
	}

	public boolean getIsCheckMove(){return checkMove;}
	public void setIsCheckMove(boolean checkMove){this.checkMove = checkMove;}
	public IPiece getMovingPiece() {
		return movingPiece;
	}

	public void addMovingPiece(IPiece movingPiece) {
		this.movingPiece = movingPiece;
	}

	public Point getStartCoor() {
		return startCoor;
	}

	public void setStartCoor(Point startCoor) {
		this.startCoor = startCoor;
	}

	public Point getEndCoor() {
		return endCoor;
	}

	public void setEndCoor(Point endCoor) {
		this.endCoor = endCoor;
	}

	public boolean isOffensive() {
		return offensive;
	}

	public void setOffensive(boolean offensive) {
		this.offensive = offensive;
	}

	public int getAdditionalPoints() {
		return additionalPoints;
	}

	public void addAdditionalPoints(int additionalPoints) {
		this.additionalPoints += additionalPoints;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public String toString() {
		return "Piece: " + movingPiece.getType() + " StartCoor: " + startCoor.toString() + " endCoor: " + endCoor.toString(); 

	}

	public String algebraicNotation()
	{
		String result = ""+(char)(startCoor.x+'a')+(startCoor.y+1);
		result += ""+((char)(endCoor.x+'a'))+(endCoor.y+1);

		return result;
	}

}
