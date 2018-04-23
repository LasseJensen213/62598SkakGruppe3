package piece;

import java.awt.Point;

import interfaces.IPiece;

public class Move {
	
	
	private IPiece movingPiece;				//The actual moving piece.
	private Point startCoor;				//Where it comes from
	private Point endCoor;					//Where it will go
	private boolean offensive;				//Taking a piece
	private int additionalPoints = 0;		//Add extra points to the move. 
	private boolean special = false;		//For en passant and pawn evolution.
	
	
	
	
	public Move(IPiece movingPiece, Point startCoor, Point endCoor) {
		super();
		this.movingPiece = movingPiece;
		this.startCoor = startCoor;
		this.endCoor = endCoor;
	}
	
	
	
	
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
	public String toString(){
		return "" + (char)((startCoor.getX()-1)+'a') + startCoor.getY() + (char)((endCoor.getX()-1)+'a') + endCoor.getY();
	}
	
	
	
	
	
	
	
	

}
