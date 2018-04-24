package piece;

import java.util.*;

import data.Values;

import java.awt.Point;

public class Queen extends Piece {

	private static int value = Values.QUEEN;
	ArrayList<Point> legalMoves;

	public Queen(Color color, Point coordinates) {
		super(Type.Queen, color, value, coordinates);
		legalMoves = new ArrayList<>();

	}

	@Override
	public ArrayList<Point> getLegalMoves() {
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

		return legalMoves;
	}

	public String toString()
	{
		return this.color == Color.BLACK ? "q" : "Q";
	}

}
