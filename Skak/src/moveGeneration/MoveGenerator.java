package moveGeneration;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import data.Board;
import data.Values;
import datastructures.Stack;
import interfaces.IBoard;
import interfaces.IPiece;
import interfaces.IPiece.Color;
import interfaces.IPiece.Type;
import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Move;
import piece.Queen;
import piece.Rook;

public class MoveGenerator {
	private ArrayList<Move> bestMoveLastRound; // The best move last round might be good again
	private ArrayList<Move> betterThanAvarage; // For moves better than avarage. Another way to sort stuff. Pawn
	// promotion, castle. Stuff like that.
	private ArrayList<Move> offensiveMoves; // Usually taking a piece is quite good
	private ArrayList<Move> officerMoves; // Movement of officers. (Queen, rook, bishob, knight)
	private ArrayList<Move> pawnMoves; // The pawn movements.

	// Shit to know about.
	private boolean kingIncheck = false;
	private IBoard boardState;


	/**
	 * Takes a boardstate, so that it knows which pieces exists.
	 */
	public MoveGenerator(IBoard boardState) {
		this.boardState = boardState;

		bestMoveLastRound = new ArrayList<>();
		betterThanAvarage = new ArrayList<>();
		offensiveMoves = new ArrayList<>();
		officerMoves = new ArrayList<>();
		pawnMoves = new ArrayList<>();

	}

	/**
	 * generates moves for the individual piece.
	 * 
	 *
	 *            Arraylist of moves
	 */
	public void GenerateMoves() {
		this.bestMoveLastRound = new ArrayList<>();
		this.betterThanAvarage = new ArrayList<>();
		this.offensiveMoves = new ArrayList<>();
		this.officerMoves = new ArrayList<>();
		this.pawnMoves = new ArrayList<>();
		ArrayList<IPiece> pieces = boardState.getPieces();
		HashMap<Point, Boolean> directions = new HashMap<>();

		// true signifies that it's possible to continue in the direction. False - Not.
		Point rightPoint = new Point(1, 0);
		Point leftPoint = new Point(-1, 0);
		Point upPoint = new Point(0, 1);
		Point downPoint = new Point(0, -1);
		Point upRightPoint = new Point(1, 1);
		Point upLeftPoint = new Point(-1, 1);
		Point downLeftPoint = new Point(-1, -1);
		Point downRightPoint = new Point(1, -1);

		Color adversaryColor = boardState.getTurn() == Color.BLACK ? Color.WHITE : Color.BLACK;
		for (IPiece piece : pieces) {


			// Reset directionsmap
			directions.clear();
			directions.put(rightPoint, true); // Right
			directions.put(leftPoint, true); // Left
			directions.put(upPoint, true); // Up
			directions.put(downPoint, true); // Down
			directions.put(upRightPoint, true); // Up - Right
			directions.put(upLeftPoint, true); // Up - Left
			directions.put(downLeftPoint, true); // Down - Left
			directions.put(downRightPoint, true); // Down - Right

//			if (piece.getType().equals(IPiece.Type.King)) {
//				this.kingIncheck = ((King) piece).isCheck();
//			}

			// If the king isn't checked.
			if (!this.kingIncheck) {

				// Fetch all legal moves.
				ArrayList<Point> pieceMoves = piece.getLegalMoves();
				Point newCoords; // The coordinates after the move.
				Point newDirection;
				for (Point p : pieceMoves) {
					newDirection = getDirection(p);

					// Check to see if it's possible to go in that direction.
					if (!directions.get(newDirection)) {
						if (!(piece.getType() == Type.Knight || piece.getType() == Type.King)) {
							continue;
						}
					}

					// Find the new coordinates.
					newCoords = getNewPosition(p, piece.getCoordinates());

					// If the move is valid.
					if (!boardState.outOfBounds(newCoords)) {
						if (!(boardState.allyPiecePresent(newCoords, piece.getColor()))) {

							Move newMove = new Move(piece, piece.getCoordinates(), newCoords);
							if(boardState.getChessBoard()[newCoords.x][newCoords.y] != null && boardState.getChessBoard()[newCoords.x][newCoords.y].getType() == Type.King)
							{
								directions.put(newDirection , false);
								continue;
							}
							newMove.setOffensive(boardState.enemyPiecePresent(newCoords, piece.getColor()));
							if (newMove.isOffensive()) {
								directions.put(newDirection, false);
							}

							if(boardState.isKingInCheckAfterMove(boardState.getTurn() , newMove))
								continue;
							newMove.setIsCheckMove(boardState.isKingInCheckAfterMove(adversaryColor , newMove ));


							switch (piece.getType()) {
							case Bishop:
								if (newMove.isOffensive()) {
									// Add extra points if a low value piece takes a higher value piece.
									if (boardState.getPiece(newCoords).getValue() > piece.getValue()) {
										newMove.addAdditionalPoints(Values.SMALLTAKEBIG);
									}
									offensiveMoves.add(newMove);
								} else {
									officerMoves.add(newMove);
								}
								break;
							case King:
								if (Math.abs(p.getX()) > 1) { // Is it trying to castle.
									if(boardState.isChecked()) // King is checked on his current position
										continue;
									// Check path to rook for short castle.
									Point fieldToCheck = new Point();
									// We trying to short castle.
									if (newMove.getStartCoor().getX() < newMove.getEndCoor().getX()) {
										boolean canCastle = boardState.getTurn() == Color.BLACK ? boardState.isBlackShortCastle() : boardState.isWhiteShortCastle();
										if(!canCastle)
											continue;
										Move castleMove = new Move(piece , piece.getCoordinates() , new Point(newCoords.x+1 , newCoords.y));
										if(boardState.isKingInCheckAfterMove(piece.getColor() , castleMove))
											continue;
										boolean spacesFree = true;
										for (int x = 5; x <= 6; x++) {
											fieldToCheck.setLocation(x, newMove.getStartCoor().getY());
											if (boardState.getPiece(fieldToCheck) != null) {
												// TODO is field threatened by enemy piece Then break
												/*
												if (boardState.getPiece(fieldToCheck).getType().equals(Type.Rook)) {
													if (!((Rook) boardState.getPiece(fieldToCheck)).isUnmoved()) {
														break;
													}
												} else {
													break;
												}*/
												spacesFree = false;
												break;
											}
										}
										if(spacesFree)
										{
											((King) newMove.getMovingPiece()).setUnmoved(false);
											newMove.setSpecial(true);
											newMove.setSpecialMove(Move.SpecialMove.SHORT_CASTLE);
											newMove.addAdditionalPoints(Values.KINGCASTLE);
											betterThanAvarage.add(newMove);
										}
									}
									// We trying to long castle.
									else {
										boolean canCastle = boardState.getTurn() == Color.BLACK ? boardState.isBlackLongCastle() : boardState.isWhiteLongCastle();
										if(!canCastle)
											continue;
										Move castleMove = new Move(piece , piece.getCoordinates() , new Point(newCoords.x-1 , newCoords.y));
										if(boardState.isKingInCheckAfterMove(piece.getColor() , castleMove))
											continue;
										boolean spacesFree = true;
										for (int x = 1; x < 4; x++) {
											fieldToCheck.setLocation(x, newMove.getStartCoor().getY());
											if (boardState.getPiece(fieldToCheck) != null) {
												// TODO is field threatened by enemy piece Then break
												/*
												if (boardState.getPiece(fieldToCheck).getType().equals(Type.Rook)) {
													if (!((Rook) boardState.getPiece(fieldToCheck)).isUnmoved()) {
														break;
													}
												} else {
													break;
												}*/
												spacesFree = false;
												break;
											}
										}
										if(spacesFree)
										{
											((King) newMove.getMovingPiece()).setUnmoved(false);
											newMove.setSpecial(true);
											newMove.setSpecialMove(Move.SpecialMove.LONG_CASTLE);
											betterThanAvarage.add(newMove);
										}

									}

								} else {
									if (newMove.isOffensive()) {
										offensiveMoves.add(newMove);
									} else {
										officerMoves.add(newMove);
									}
								}
								break;
							case Knight:
								if (newMove.isOffensive()) {
									// Add extra points if a low value piece takes a higher value piece.
									if (boardState.getPiece(newCoords).getValue() > piece.getValue()) {
										newMove.addAdditionalPoints(Values.SMALLTAKEBIG);
									}
									offensiveMoves.add(newMove);
								} else {
									officerMoves.add(newMove);
								}
								break;
							case Pawn: {
								generatePawnMove(p, piece, newCoords, newMove);
								break;
							}
							case Queen:
								if(newMove.isOffensive())
									offensiveMoves.add(newMove);
								else
									officerMoves.add(newMove);
								break;
							case Rook:
								((Rook) newMove.getMovingPiece()).setUnmoved(false);
								if (newMove.isOffensive()) {
									// Add extra points if a low value piece takes a higher value piece.
									if (boardState.getPiece(newCoords).getValue() > piece.getValue()) {
										newMove.addAdditionalPoints(Values.SMALLTAKEBIG);
									}
									offensiveMoves.add(newMove);
								} else {
									officerMoves.add(newMove);
								}
								break;
							default:
								break;
							}

						} else {
							// Ally present in direction. Can't move in that direction anymore.
							directions.put(newDirection, false);
						}

					}

					else {
						// Direction is now out of bounds.
						directions.put(newDirection, false);
					}

				}

			}
			else {
			}
		}
		

	}

	private Point getNewPosition(Point point1, Point point2) {
		Point returnPoint = new Point();
		double x = point1.getX() + point2.getX();
		double y = point1.getY() + point2.getY();
		returnPoint.setLocation(x, y);
		return returnPoint;
	}

	private void generatePawnMove(Point diff, IPiece piece, Point newCoords, Move newMove) {
		// Can the pawn move forward?
		// Promotion
		if (newMove.getEndCoor().getY() == 7 ||newMove.getEndCoor().getY() == 0) {
			IPiece queen = new Queen(newMove.getMovingPiece().getColor(), newMove.getEndCoor());
			IPiece bishop = new Bishop(newMove.getMovingPiece().getColor(), newMove.getEndCoor());
			IPiece knight = new Knight(newMove.getMovingPiece().getColor(), newMove.getEndCoor());
			IPiece rook = new Rook(newMove.getMovingPiece().getColor(), newMove.getEndCoor());

			Move queenMove = new Move(queen, newMove.getStartCoor(), newMove.getEndCoor());
			Move bishopMove = new Move(bishop, newMove.getStartCoor(), newMove.getEndCoor());
			Move knightMove = new Move(knight, newMove.getStartCoor(), newMove.getEndCoor());
			Move rookMove = new Move(rook, newMove.getStartCoor(), newMove.getEndCoor());

			queenMove.setSpecial(true);
			bishopMove.setSpecial(true);
			knightMove.setSpecial(true);
			rookMove.setSpecial(true);
			queenMove.setSpecialMove(Move.SpecialMove.PROMOTION_QUEEN);
			rookMove.setSpecialMove(Move.SpecialMove.PROMOTION_ROOK);
			bishopMove.setSpecialMove(Move.SpecialMove.PROMOTION_BISHOP);
			knightMove.setSpecialMove(Move.SpecialMove.PROMOTION_KNIGHT);


			betterThanAvarage.add(queenMove);
			betterThanAvarage.add(bishopMove);
			betterThanAvarage.add(knightMove);
			betterThanAvarage.add(rookMove);
			return;

		}
		if (diff.getX() == 0) {
			// If the pawn isn't moving in the x direction.
			if (newMove.isOffensive()) {
				// If there is an enemy piece there, the move is not valid.
				return;
			}

			// Add extra points if it is or is becoming a center pawn.
			if (1 < newCoords.getX() && newCoords.getX() < 6) {
				newMove.addAdditionalPoints(Values.CENTERPAWN);
			}

			pawnMoves.add(newMove);

			// No need to execute more code, since the move already has been added to the
			// moves list.
			return;

		} else {
			// The pawn is moving to the side. Check if offensive. Means, is there an enemy
			// piece.
			if (newMove.isOffensive()) {
				// Add extra points if a low value piece takes a higher value piece.
				if (boardState.getPiece(newCoords).getValue() > piece.getValue()) {
					newMove.addAdditionalPoints(Values.SMALLTAKEBIG);
				}
				offensiveMoves.add(newMove);

			} else {

				// If it isn't offensive (no enemy piece there) check for en passant.
				if (boardState.getEnPassant() != null) {
					// If the piece is white
					if (piece.getColor().equals(Color.WHITE)) {
						// If it's in the correct row.
						if (piece.getCoordinates().y == 4) {
							// Has a pawn recently moved over this field?

							if (boardState.getEnPassant().equals(newCoords)) {
								newMove.setSpecial(true);
								pawnMoves.add(newMove);
							}
						}
					}
					// Piece is black.
					else {
						// If it's in the correct row.
						if (piece.getCoordinates().y == 3) {
							// Has a pawn recently moved over this field?
							if (boardState.getEnPassant().equals(newCoords)) {
								newMove.setSpecial(true);
								pawnMoves.add(newMove);
							}
						}
					}
				}

				return;

			}
		}
	}

	private Point getDirection(Point p) {
		Point returnPoint = new Point();
		int x = 0, y = 0;
		double pointX = p.getX();
		double pointY = p.getY();
		if (pointX > 0) {
			x = 1;
		} else if (pointX < 0) {
			x = -1;
		}
		if (pointY > 0) {
			y = 1;
		} else if (pointY < 0) {
			y = -1;
		}
		returnPoint.setLocation(x, y);
		return returnPoint;
	}

	private void generateNewBoardStates() {
		if (!bestMoveLastRound.isEmpty()) {
			//System.out.println("bestMoveLastRound: " + bestMoveLastRound.size());
			for (Move m : bestMoveLastRound) {
				boardState.generateNewBoardState((Board) boardState, m);
			}
		}
		if (!betterThanAvarage.isEmpty()) {
			//System.out.println("betterThanAvarage: " + betterThanAvarage.size());
			for (Move m : betterThanAvarage) {
				boardState.generateNewBoardState((Board) boardState, m);
			}
		}
		if (!offensiveMoves.isEmpty()) {
			//System.out.println("offensiveMoves: " + offensiveMoves.size());
			for (Move m : offensiveMoves) {
				boardState.generateNewBoardState((Board) boardState, m);
			}
		}
		if (!officerMoves.isEmpty()) {
			//System.out.println("officerMoves: " + officerMoves.size());
			for (Move m : officerMoves) {
				boardState.generateNewBoardState((Board) boardState, m);
			}
		}
		if (!pawnMoves.isEmpty()) {
			//System.out.println("pawnMoves: " + pawnMoves.size());
			for (Move m : pawnMoves) {
				boardState.generateNewBoardState((Board) boardState, m);
			}
		}

	}
	
	public ArrayList<Move> getFinalList() {
		ArrayList<Move> finalList = new ArrayList<>();
		finalList.addAll(bestMoveLastRound);
		finalList.addAll(betterThanAvarage);
		finalList.addAll(offensiveMoves);
		finalList.addAll(officerMoves);
		finalList.addAll(pawnMoves);
		return finalList;
	}

	public Stack<Move> getFinalMoveStack()
	{
		Stack<Move> stack = new Stack<>();
		stack.pushAll(pawnMoves);
		stack.pushAll(officerMoves);
		stack.pushAll(offensiveMoves);
		stack.pushAll(betterThanAvarage);
		stack.pushAll(bestMoveLastRound);
		return stack;
	}

}
