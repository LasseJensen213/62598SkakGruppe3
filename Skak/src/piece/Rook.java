package piece;

import java.util.ArrayList;

import data.Values;

import java.awt.Point;

public class Rook extends Piece {

	private static int value = Values.ROOK;
	ArrayList<Point> legalMoves;
	private boolean unmoved = true;

	public Rook(Color color, Point coordinates) {

		super(Type.Rook, color, value, coordinates);
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

}
