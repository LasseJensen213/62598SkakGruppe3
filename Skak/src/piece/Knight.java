package piece;

import java.util.ArrayList;

import data.Values;

import java.awt.Point;

public class Knight extends Piece {

	private static int value = Values.KNIGHT;
	ArrayList<Point> legalMoves;

	public Knight(Color color, Point coordinates) {
		super(Type.Knight, color, value, coordinates);
		legalMoves = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Point> getLegalMoves() {
		legalMoves.add(new Point(-2, -1));
		legalMoves.add(new Point(-2, +1));
		legalMoves.add(new Point(-1, +2));
		legalMoves.add(new Point(+1, +2));
		legalMoves.add(new Point(+2, +1));
		legalMoves.add(new Point(+2, -1));
		legalMoves.add(new Point(+1, -2));
		legalMoves.add(new Point(-1, -2));

		return legalMoves;
	}

}
