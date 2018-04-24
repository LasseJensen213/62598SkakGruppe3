package piece;

import java.awt.Point;
import java.util.ArrayList;

import data.Values;
import interfaces.IPiece;

public class Rook extends Piece {

	private static int value = Values.ROOK;
	ArrayList<Point> legalMoves;
	private boolean unmoved = true;

	public Rook(Color color, Point coordinates) {

		super(Type.Rook, color, value, coordinates);
		legalMoves = new ArrayList<>();

	}

	public Rook(IPiece iPiece) {
		super(Type.Pawn, iPiece.getColor(), value,
				new Point((int) iPiece.getCoordinates().getX(), (int) iPiece.getCoordinates().getY()));
		legalMoves = new ArrayList<>();

	}

	@Override
	public ArrayList<Point> getLegalMoves() {
		legalMoves = new ArrayList<>();
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

	public String toString() {
		return this.color == Color.BLACK ? "r" : "R";
	}

}
