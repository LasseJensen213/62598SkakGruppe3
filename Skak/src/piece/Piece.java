package piece;

import java.awt.Point;
import java.util.ArrayList;

import interfaces.IPiece;

public abstract class Piece implements IPiece {

	private Type type;
	protected Color color;
	protected boolean moved;
	protected int value;
	private Point coordinates;

	public Piece(Type type, Color color, int value, Point coordinates) {
		super();
		this.type = type;
		this.color = color;
		this.value = value;
		this.coordinates = coordinates;
		moved = false;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	abstract public ArrayList<Point> getLegalMoves();

	@Override
	public Point getCoordinates() {
		return coordinates;
	}

	@Override
	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}

}
