package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import exceptions.ChessException;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		
		
		System.out.print(UI.ANSI_CYAN_BACKGROUND);
		UI.clearConsole();
		
		while(true) {
			
			try {
			
				UI.clearConsole();
				UI.printBoard(chessMatch.getPieces());
				
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
			
			} catch (ChessException e) {
				System.out.print(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.print(e.getMessage());
				sc.nextLine();
			}
		}
		
	}

}
