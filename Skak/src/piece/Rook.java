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
		super(Type.Rook, iPiece.getColor(), value,
				new Point((int) iPiece.getCoordinates().getX(), (int) iPiece.getCoordinates().getY()));
		legalMoves = new ArrayList<>();

	}

	@Override
	public ArrayList<Point> getLegalMoves() {

		int leftFields = this.getCoordinates().x;
		int rightFields = 7 - leftFields;
		int bottomFields = this.getCoordinates().y;
		int topFields = 7-bottomFields;
		legalMoves = new ArrayList<>(leftFields+rightFields+bottomFields+topFields);
		int i = 0;
		for(i = 1 ; i<=leftFields ; i++)
			legalMoves.add(new Point(-i, 0)); //Left
		for(i = 1 ; i<=rightFields ; i++)
			legalMoves.add(new Point(+i, 0)); //Right
		for(i = 1 ; i<=bottomFields ; i++)
			legalMoves.add(new Point(0, -i)); //Bottom
		for (i = 1; i <= topFields; i++) {
			legalMoves.add(new Point(0, +i)); //Top

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
