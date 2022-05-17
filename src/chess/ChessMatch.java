package chess;

import java.util.InputMismatchException;
import java.util.Scanner;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;
import exceptions.ChessException;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
		initSetup();
	}

	public ChessPiece[][] getPieces() {

		ChessPiece[][] matPieces = new ChessPiece[board.getRows()][board.getColumns()];

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				matPieces[i][j] = (ChessPiece) board.piece(i, j);
			}
		}

		return matPieces;
	}
	
	public static ChessPosition readChessPosition(Scanner sc) {
		try {
			String position = sc.nextLine();
			
			char c1 = position.charAt(0);
			int  c2 = Integer.parseInt(position.substring(1));
			
			return new ChessPosition(c1, c2);
			
		} catch (RuntimeException e) {
			throw new InputMismatchException("Error read ChessPosition: Valid values are from a1 to h8");
		}
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition positionSource, ChessPosition positionTarget) {
		
		Position source = positionSource.toPosition();
		Position target = positionTarget.toPosition();
		
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		
		Piece pieceCaptured = makeMove(source, target);
		return (ChessPiece)pieceCaptured;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		return capturedPiece;
	}
	
	private void validateSourcePosition(Position source) {
		if(!board.thereIsAPiece(source)) {
			throw new ChessException("There is not piece on source position.");
		}
		
		if(!board.piece(source).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece.");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position.");
		}
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}

	private void initSetup() {

		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));

	}
}
