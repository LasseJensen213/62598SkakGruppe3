package piece;

import java.awt.Point;
import java.util.ArrayList;

import data.Values;
import interfaces.IPiece;
import interfaces.IPiece.Type;

public class Bishop extends Piece {

	private static int value = Values.BISHOP;
	ArrayList<Point> legalMoves;

	public Bishop(Color color, Point coordinates) {

		super(Type.Bishop, color, value, coordinates);
		legalMoves = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}

	public Bishop(IPiece iPiece) {
		super(Type.Bishop, iPiece.getColor(), value,
				new Point((int) iPiece.getCoordinates().getX(), (int) iPiece.getCoordinates().getY()));
		legalMoves = new ArrayList<>();
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
		legalMoves = new ArrayList<>(leftLowerDiag + leftUpperDiag + rightLowerDiag + rightUpperDiag);
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
		return this.color == Color.BLACK ? "b" : "B";
	}

}
