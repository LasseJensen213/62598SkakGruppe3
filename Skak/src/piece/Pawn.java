package piece;

import java.util.*;

import data.Values;
import interfaces.IPiece;
import interfaces.IPiece.Type;

import java.awt.Point;

public class Pawn extends Piece {

	private static int value = Values.PAWN;
	ArrayList<Point> legalMoves;

	public Pawn(Color color, Point coordinates) {
		super(Type.Pawn, color, value, coordinates);
		legalMoves = new ArrayList<>();
	}

	public Pawn(IPiece iPiece) {
		super(Type.Pawn, iPiece.getColor(), value, iPiece.getCoordinates());
		legalMoves = new ArrayList<>();

	}

	@Override
	public ArrayList<Point> getLegalMoves() {
		// If it's a white pawn, it has to move in the positive direction.
		if (super.color.equals(Color.WHITE)) {
			legalMoves.add(new Point(0, +1));

	
			// If at start position:
			if (((int) super.getCoordinates().getY()) == 1) {
				legalMoves.add(new Point(0, +2));
			}

			legalMoves.add(new Point(+1, +1));
			legalMoves.add(new Point(-1, +1));

		}
		// If black, the negative direction.
		else {
			legalMoves.add(new Point(0, -1));

			if (((int) super.getCoordinates().getY()) == 6) {
				legalMoves.add(new Point(0, -2));

			}

			legalMoves.add(new Point(-1, -1));
			legalMoves.add(new Point(+1, -1));
		}

		return legalMoves;
	}

	public String toString()
	{
		return this.color == Color.BLACK ? "p" : "P";
	}
}
