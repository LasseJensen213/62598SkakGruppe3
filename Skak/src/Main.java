import controller.GameController;
import fen.FEN;
import interfaces.IPiece;
import piece.Move;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by alexanderkarlsson on 23/04/2018.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //Wait until winboard sends either a move or a command stating the engine is white

        while(true){

            String line = scanner.nextLine();

            if(line.equals("white")){
                //Engine is white
                GameController.getInstance().board.setTurn(IPiece.Color.WHITE);
                Move move = GameController.getInstance().getAIMove();
                System.out.println("move " + move.algebraicNotation());//Send move command to WinBoard
                GameController.getInstance().makeMove(move);
            }else if(Pattern.matches("[a-h]\\d[a-h]\\d",line)){//If string matches a move, eg d2d4
                GameController.getInstance().makeMove(line);//Make the move WinBoard indicated
                break;
            }
        }
        //First move(s) have been made, let AI make move and wait for user move in loop
        while(true){
            //Make AI move
            Move move = GameController.getInstance().getAIMove();
            System.out.println("move " + move.algebraicNotation());//Send move command to WinBoard
            GameController.getInstance().makeMove(move);

            //Wait for user move
            String line = scanner.nextLine();
            while(!Pattern.matches("[a-h]\\d[a-h]\\d",line)){
                line = scanner.nextLine();
            }
            //User has made her move
            GameController.getInstance().makeMove(line);
        }

    }
}
