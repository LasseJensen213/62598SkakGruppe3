package piece;

import java.util.ArrayList;

import data.Values;
import interfaces.IPiece;
import interfaces.IPiece.Type;

import java.awt.Point;

public class Rook extends Piece {

	private static int value = Values.ROOK;
	ArrayList<Point> legalMoves;
	private boolean unmoved = true;

	public Rook(Color color, Point coordinates) {

		super(Type.Rook, color, value, coordinates);
		legalMoves = new ArrayList<>();

	}

	public Rook(IPiece iPiece) {
		super(Type.Rook, iPiece.getColor(), value, iPiece.getCoordinates());
		legalMoves = new ArrayList<>();

	}

	@Override
	public ArrayList<Point> getLegalMoves() {
		for (int i = 1; i < 8; i++) {
			legalMoves.add(new Point(+i, 0)); // Right
			legalMoves.add(new Point(-i, 0)); // Left
			legalMoves.add(new Point(0, +i)); // Up
			legalMoves.add(new Point(0, -i)); // Down

		}

		return legalMoves;
	}

	public boolean isUnmoved() {
		return unmoved;
	}

	public void setUnmoved(boolean unmoved) {
		this.unmoved = unmoved;
	}

	public String toString()
	{
		return this.color == Color.BLACK ? "r" : "R";
	}

}
