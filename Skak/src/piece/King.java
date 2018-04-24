package piece;

import java.util.ArrayList;

import data.Values;
import interfaces.IPiece;

import java.awt.Point;

public class King extends Piece {

	private boolean check; // King checked (I skak)
	private static int value = Values.KING;
	private boolean unmoved = true;

	ArrayList<Point> legalMoves;
	ArrayList<Point> inCheckMoves;

	public King(Color color, Point coordinates) {
		super(Type.King, color, value, coordinates);
		legalMoves = new ArrayList<>();
		inCheckMoves = new ArrayList<>();
	}

	public King(IPiece iPiece) {
		super(Type.King, iPiece.getColor(), value, iPiece.getCoordinates());
		legalMoves = new ArrayList<>();
		inCheckMoves = new ArrayList<>();
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	@Override
	public ArrayList<Point> getLegalMoves() {
		legalMoves = new ArrayList<>(8);
		if (unmoved) {
			legalMoves.add(new Point(2, 0)); // Short castle
			legalMoves.add(new Point(-2, 0)); // Long castle
		}

		legalMoves.add(new Point(+1, 0));
		legalMoves.add(new Point(0, +1));
		legalMoves.add(new Point(-1, 0));
		legalMoves.add(new Point(0, -1));
		legalMoves.add(new Point(+1, +1));
		legalMoves.add(new Point(+1, +1));
		legalMoves.add(new Point(-1, -1));
		legalMoves.add(new Point(+1, -1));

		return legalMoves;
	}

	
	
	
	
	
	public ArrayList<Point> getInCheckMoves() {
		// Can be checked from all directions.
		for (int i = 1; i < 8; i++) {
			legalMoves.add(new Point(i, 0));	//Right
			legalMoves.add(new Point(-i, 0));	//Left
			legalMoves.add(new Point(0, i));	//Up
			legalMoves.add(new Point(0, -i));	//Down
			legalMoves.add(new Point(i, i));	//Up - Right
			legalMoves.add(new Point(-i, i));	//Up - Left
			legalMoves.add(new Point(-i, -i));	//Down - Left
			legalMoves.add(new Point(i, -i));	//Down - Right
		}
		//And from knight moves. 
		legalMoves.add(new Point(-2, -1));
		legalMoves.add(new Point(-2, +1));
		legalMoves.add(new Point(-1, +2));
		legalMoves.add(new Point(+1, +2));
		legalMoves.add(new Point(+2, +1));
		legalMoves.add(new Point(+2, -1));
		legalMoves.add(new Point(+1, -2));
		legalMoves.add(new Point(-1, -2));
		
		return inCheckMoves;
	}

	public boolean isUnmoved() {
		return unmoved;
	}

	public void setUnmoved(boolean unmoved) {
		this.unmoved = unmoved;
	}

	public String toString()
	{
		return this.color == Color.BLACK ? "k" : "K";
	}

}
