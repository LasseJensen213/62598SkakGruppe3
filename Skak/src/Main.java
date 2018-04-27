import controller.GameController;
import data.Board;
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
        String fen = "";
        if(args.length >0)
        {
            for(int i = 0 ; i<args.length; i++)
            {
                fen += args[i]+" ";
            }
            fen = fen.substring(0 , fen.length()-1);
            GameController.startFEN = fen;
            System.out.println("feature setboard=1");
            System.out.println("setboard "+fen);
        }
        Scanner scanner = new Scanner(System.in);
        //Wait until winboard sends either a move or a command stating the engine is white
        FEN.FILE_PATH = System.getProperty("user.dir")+"/FENLOG.txt";
        while(true){
            String line = scanner.nextLine();
            if(line.equals("white")){
                //Engine is white
                GameController.getInstance().board.setTurn(IPiece.Color.WHITE);
                Move move = GameController.getInstance().getAIMove();
                if(move == null)
                    System.out.println("resign");
                else if(move.getSpecialMove()== Move.SpecialMove.PROMOTION_QUEEN)
                {
                    System.out.println("move " + move.algebraicNotation()+"q");//Send move command to WinBoard
                }
                else if(move.getSpecialMove()== Move.SpecialMove.PROMOTION_KNIGHT)
                {
                    System.out.println("move " + move.algebraicNotation()+"n");//Send move command to WinBoard
                }
                else if(move.getSpecialMove() == Move.SpecialMove.PROMOTION_BISHOP)
                {
                    System.out.println("move " + move.algebraicNotation()+"b");//Send move command to WinBoard
                }
                else if(move.getSpecialMove() == Move.SpecialMove.PROMOTION_ROOK)
                {
                    System.out.println("move " + move.algebraicNotation()+"r");//Send move command to WinBoard
                }
                else
                {
                    System.out.println("move " + move.algebraicNotation());//Send move command to WinBoard
                }
                GameController.getInstance().makeMove(move);
                fen = FEN.encode((Board) GameController.getInstance().board);
                FEN.saveToFile(fen);
            }else if(Pattern.matches("[a-h]\\d[a-h]\\d",line)){//If string matches a move, eg d2d4
                GameController.getInstance().makeMove(line);//Make the move WinBoard indicated
                fen = FEN.encode((Board) GameController.getInstance().board);
                FEN.saveToFile(fen);
                break;
            }
            else if(Pattern.matches("[a-h]\\d[a-h]\\d[qrnb]]",line))
            {
                GameController.getInstance().makePromotionMove(line);//Make the move WinBoard indicated
                fen = FEN.encode((Board) GameController.getInstance().board);
                FEN.saveToFile(fen);
                break;
            }
        }
        //First move(s) have been made, let AI make move and wait for user move in loop

        while(true){
            //Make AI move
            Move move = GameController.getInstance().getAIMove();
            if(move == null)
                System.out.println("resign");
            else
            {
                System.out.println("move " + move.algebraicNotation());//Send move command to WinBoard

            }

            GameController.getInstance().makeMove(move);
            fen = FEN.encode((Board) GameController.getInstance().board);
            FEN.saveToFile(fen);

            //Wait for user move
            String line = scanner.nextLine();
            while(!Pattern.matches("[a-h]\\d[a-h]\\d",line)){
                line = scanner.nextLine();
            }
            //User has made her move
            GameController.getInstance().makeMove(line);
            fen = FEN.encode((Board) GameController.getInstance().board);
            FEN.saveToFile(fen);
        }

    }
}
