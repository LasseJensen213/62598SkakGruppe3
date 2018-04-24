package piece;

import java.util.ArrayList;

import data.Values;
import interfaces.IPiece;
import interfaces.IPiece.Type;

import java.awt.Point;

public class Knight extends Piece {

	private static int value = Values.KNIGHT;
	ArrayList<Point> legalMoves;

	public Knight(Color color, Point coordinates) {
		super(Type.Knight, color, value, coordinates);
		legalMoves = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}

	public Knight(IPiece iPiece) {
		super(Type.Knight, iPiece.getColor(), value,
				new Point((int) iPiece.getCoordinates().getX(), (int) iPiece.getCoordinates().getY()));		legalMoves = new ArrayList<>();
	}

	@Override
	public ArrayList<Point> getLegalMoves() {
		legalMoves = new ArrayList<>(8);
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

	public String toString()
	{
		return this.color == Color.BLACK ? "n" : "N";
	}

}
