package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import exceptions.ChessException;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> piecesCaptured = new ArrayList<>();
		
		System.out.print(UI.ANSI_CYAN_BACKGROUND);
		UI.clearConsole();
		
		while(!chessMatch.isCheckMate()) {
			
			try {
			
				UI.clearConsole();
				UI.printMatch(chessMatch, piecesCaptured);
				
				System.out.println();
				System.out.print("Source: ");
				ChessPosition sourcePosition = chessMatch.readChessPosition(sc);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(sourcePosition);
				UI.clearConsole();
				UI.printBoard(chessMatch.getPieces(), possibleMoves);
				
				
				System.out.println();
				System.out.print("Target: ");
				ChessPosition targetPosition = chessMatch.readChessPosition(sc);
				
				ChessPiece capturedPiece = chessMatch.performChessMove(sourcePosition, targetPosition);
				
				if(capturedPiece != null) {
					piecesCaptured.add(capturedPiece);
				}
				
				if(chessMatch.getPromoted() != null) {
					System.out.print("Enter piece for Promotion B->Bishop, N->Knight, R->Rook, Q->Queen (B/N/R/Q):");
					String type = sc.nextLine().toUpperCase();
					while(!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
						System.out.print("Value invalid, please enter piece for Promotion B->Bishop, N->Knight, R->Rook, Q->Queen (B/N/R/Q):");
						type = sc.nextLine().toUpperCase();
					}
					
					chessMatch.replacePromotedPiece(type);
				}
			
			} catch (ChessException e) {
				System.out.print(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.print(e.getMessage());
				sc.nextLine();
			}
		}
		
		UI.clearConsole();
		UI.printMatch(chessMatch, piecesCaptured);
		
	}

}
