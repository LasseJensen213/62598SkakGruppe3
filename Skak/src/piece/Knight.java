package piece;

import java.awt.Point;
import java.util.ArrayList;

import data.Values;
import interfaces.IPiece;

public class Knight extends Piece {

	private static int value = Values.KNIGHT;
	ArrayList<Point> legalMoves;

	public Knight(Color color, Point coordinates) {
		super(Type.Knight, color, value, coordinates);
		legalMoves = new ArrayList<>();
		instantiateMoves();

	}

	public Knight(IPiece iPiece) {
		super(Type.Knight, iPiece.getColor(), value,
				new Point((int) iPiece.getCoordinates().getX(), (int) iPiece.getCoordinates().getY()));
		legalMoves = new ArrayList<>();
		instantiateMoves();

	}

	@Override
	public ArrayList<Point> getLegalMoves() {
				return legalMoves;
	}
	
	
	public void instantiateMoves() {
		legalMoves = new ArrayList<>(8);
		legalMoves.add(new Point(-2, -1));
		legalMoves.add(new Point(-2, +1));
		legalMoves.add(new Point(-1, +2));
		legalMoves.add(new Point(+1, +2));
		legalMoves.add(new Point(+2, +1));
		legalMoves.add(new Point(+2, -1));
		legalMoves.add(new Point(+1, -2));
		legalMoves.add(new Point(-1, -2));
	}

	public String toString() {
		return this.color == Color.BLACK ? "n" : "N";
	}

}
