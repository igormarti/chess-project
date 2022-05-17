package chess;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;
import exceptions.ChessException;

public class ChessMatch {

	private Integer turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> piecesCaptured = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
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
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, pieceCaptured);
			throw new ChessException("You can't put yourself in check");
		}
		
		check = testCheck(opponent(currentPlayer)) ? true: false;
		
		nextTurn();
		return (ChessPiece)pieceCaptured;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			piecesCaptured.add(capturedPiece);
			piecesOnTheBoard.remove(capturedPiece);
		}
		
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece pieceCaptured) {
		Piece p = board.removePiece(target);
		board.placePiece(p, source);
		
		if(pieceCaptured!=null) {
			board.placePiece(pieceCaptured, target);
			piecesCaptured.remove(pieceCaptured);
			piecesOnTheBoard.add(pieceCaptured);
		}
	}
	
	private void validateSourcePosition(Position source) {
		if(!board.thereIsAPiece(source)) {
			throw new ChessException("There is not piece on source position.");
		}
		
		if(currentPlayer != ( (ChessPiece) board.piece(source)).getColor()) {
			throw new ChessException("The chosen piece is not yours.");
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
	
	private void nextTurn() {
		turn++;
		currentPlayer = currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return color == Color.WHITE ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color )
				.collect(Collectors.toList());
		for(Piece p: list) {
			if(p instanceof King) {
				return (ChessPiece)p;
			}
		}
		
		throw new IllegalStateException("There is no "+color+" king on the board");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> oppnentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color) )
				.collect(Collectors.toList());
		
		for (Piece p : oppnentPieces) {
			boolean[][] mat = p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		
		return false;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
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

	public Integer getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean isCheck() {
		return check;
	}

}
