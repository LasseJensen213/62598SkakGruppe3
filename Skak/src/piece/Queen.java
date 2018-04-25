package piece;

import java.util.*;

import data.Values;
import interfaces.IPiece;
import interfaces.IPiece.Type;

import java.awt.Point;

public class Queen extends Piece {

	private static int value = Values.QUEEN;
	ArrayList<Point> legalMoves;

	public Queen(Color color, Point coordinates) {
		super(Type.Queen, color, value, coordinates);
		legalMoves = new ArrayList<>();

	}

	public Queen(IPiece iPiece) {
		super(Type.Queen, iPiece.getColor(), value,
				new Point((int) iPiece.getCoordinates().getX(), (int) iPiece.getCoordinates().getY()));		legalMoves = new ArrayList<>();

	}

	@Override
	public ArrayList<Point> getLegalMoves() {
		int leftFields = this.getCoordinates().x;
		int rightFields = 7 - leftFields;
		int bottomFields = this.getCoordinates().y;
		int topFields = 7-bottomFields;
		int leftLowerDiag = leftFields < bottomFields ? leftFields : bottomFields;
		int leftUpperDiag = leftFields < topFields ? leftFields : topFields;
		int rightLowerDiag = rightFields < bottomFields ? rightFields : bottomFields;
		int rightUpperDiag = rightFields < topFields ? rightFields : topFields;
		int i = 0;

		legalMoves = new ArrayList<>(leftFields + rightFields + bottomFields + topFields + leftLowerDiag + leftUpperDiag + rightLowerDiag + rightUpperDiag);
		for(i = 1 ; i<=leftFields ; i++)
			legalMoves.add(new Point(-i, 0)); //Left
		for(i = 1 ; i<=rightFields ; i++)
			legalMoves.add(new Point(+i, 0)); //Right
		for(i = 1 ; i<=bottomFields ; i++)
			legalMoves.add(new Point(0, -i)); //Bottom
		for (i = 1; i <= topFields; i++)
			legalMoves.add(new Point(0, +i)); //Top

		for(i = 1 ; i<=leftLowerDiag ; i++)
			legalMoves.add(new Point(-i, -i));	//Down - Left
		for(i = 1 ; i<=leftUpperDiag ; i++)
			legalMoves.add(new Point(-i, i));	//Up - Left
		for(i = 1 ; i<=rightLowerDiag ; i++)
			legalMoves.add(new Point(i, -i));	//Down - Right
		for(i = 1 ; i<=rightUpperDiag ; i++)
			legalMoves.add(new Point(i, i));	//Up - Right

		return legalMoves;
	}

	public String toString()
	{
		return this.color == Color.BLACK ? "q" : "Q";
	}

}
