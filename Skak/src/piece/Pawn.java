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
		super(Type.Pawn, iPiece.getColor(), value,
				new Point((int) iPiece.getCoordinates().getX(), (int) iPiece.getCoordinates().getY()));
		legalMoves = new ArrayList<>();

	}

	@Override
	public ArrayList<Point> getLegalMoves() {
		// If it's a white pawn, it has to move in the positive direction.
		legalMoves = new ArrayList<>();
		if (super.color.equals(Color.WHITE)) {
			legalMoves.add(new Point(0, +1));

			// If at start position:

			if(getCoordinates().x != 7)
				legalMoves.add(new Point(+1, +1));
			if(getCoordinates().x != 0)
				legalMoves.add(new Point(-1, +1));
			if (((int) super.getCoordinates().getY()) == 1) {
				legalMoves.add(new Point(0, +2));
			}


		}
		// If black, the negative direction.
		else {
			legalMoves.add(new Point(0, -1));


			if(getCoordinates().x != 0)
				legalMoves.add(new Point(-1, -1));
			if(getCoordinates().x != 7)
				legalMoves.add(new Point(+1, -1));
			if (((int) super.getCoordinates().getY()) == 6) {
				legalMoves.add(new Point(0, -2));

			}
		}

		return legalMoves;
	}

	public String toString() {
		return this.color == Color.BLACK ? "p" : "P";
	}
}
