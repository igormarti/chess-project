package application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class UI {
	
	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
	
	public static void printMatch(ChessMatch chessMatch, List<ChessPiece> piecesCaptured) {
		printBoard(chessMatch.getPieces());
		System.out.println();
		printCapturedPieces(piecesCaptured);
		System.out.println();
		System.out.println("Turn: "+ chessMatch.getTurn());
		System.out.println("Waiting player: "+ chessMatch.getCurrentPlayer());
		if(chessMatch.isCheck()) {
			System.out.println("CHECK!");
		}
	}

	public static void printBoard(ChessPiece[][] pieces){
		
		for(int i=0; i < pieces.length; i++) {
			System.out.print(ANSI_CYAN_BACKGROUND);
			System.out.print(ANSI_BLUE+(8-i)+" ");
			for(int j=0; j < pieces.length; j++) {
				printPiece(pieces[i][j], false);
			}
			System.out.println();
		}
		
		System.out.print("  a  b  c  d  e  f  g  h");
		System.out.println();
	}
	
	public static void printBoard(ChessPiece[][] pieces,boolean[][] possibleMoves){
		
		for(int i=0; i < pieces.length; i++) {
			System.out.print(ANSI_CYAN_BACKGROUND);
			System.out.print(ANSI_BLUE+(8-i)+" ");
			for(int j=0; j < pieces.length; j++) {
//				System.out.println("row:"+i+" column:"+j+": "+possibleMoves[i][j]);
				printPiece(pieces[i][j], possibleMoves[i][j]);
			}
			System.out.println();
		}
		
		System.out.print("  a  b  c  d  e  f  g  h");
		System.out.println();
	}

	private static void printPiece(ChessPiece piece, boolean background){
		
		if(background) {
			System.out.print(ANSI_YELLOW_BACKGROUND);
		}
		
		if (piece == null) {
            System.out.print(ANSI_BLUE+"-"+ANSI_CYAN_BACKGROUND);
        }
        else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece+ ANSI_CYAN_BACKGROUND);
            }
            else {
                System.out.print(ANSI_BLACK + piece + ANSI_CYAN_BACKGROUND);
            }
        }
        System.out.print("  ");
	}
	
    public static void clearConsole(){
        try{
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system
              
            if(operatingSystem.contains("Windows")){        
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            } 
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    private static void printCapturedPieces(List<ChessPiece> pieces) {
    	List<ChessPiece> piecesWhite = pieces.stream().filter(p -> p.getColor() == Color.WHITE).collect(Collectors.toList());
    	List<ChessPiece> piecesBlack = pieces.stream().filter(p -> p.getColor() == Color.BLACK).collect(Collectors.toList());
    	System.out.println("Captured pieces:");
    	System.out.print(ANSI_WHITE);
    	System.out.print("White: ");
    	System.out.print(Arrays.toString(piecesWhite.toArray())+" ");
    	System.out.print(ANSI_BLACK);
    	System.out.print("Black: ");
    	System.out.print(Arrays.toString(piecesBlack.toArray())+" ");
    }
	
}
