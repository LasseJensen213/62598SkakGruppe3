package piece;

import java.awt.Point;
import java.util.ArrayList;

import data.Values;
import interfaces.IPiece;

public class Bishop extends Piece {

	private static int value = Values.BISHOP;
	ArrayList<Point> legalMoves;

	public Bishop(Color color, Point coordinates) {

		super(Type.Bishop, color, value, coordinates);
		legalMoves = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}

	public Bishop(IPiece iPiece) {
		super(Type.Bishop, iPiece.getColor(), value, iPiece.getCoordinates());
		legalMoves = new ArrayList<>();
	}

	@Override
	public ArrayList<Point> getLegalMoves() {

		for (int i = 1; i < 8; i++) {
			legalMoves.add(new Point(+i, +i)); // diagonalt op positiv retning
			legalMoves.add(new Point(+i, -i)); // diagonalt ned positiv retning
			legalMoves.add(new Point(-i, -i)); // diagonalt ned negative retning
			legalMoves.add(new Point(-i, +i)); // diagonalt op negative retning
		}

		return legalMoves;
	}

	public String toString()
	{
		return this.color == Color.BLACK ? "b" : "B";
	}

}
