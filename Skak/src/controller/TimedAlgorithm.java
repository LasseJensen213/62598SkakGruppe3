package controller;

import data.Board;
import piece.Move;

public class TimedAlgorithm {

    private static TimedAlgorithm INSTANCE = new TimedAlgorithm();
    private long maxWaitTime = 15000;
    public Move bestMoveSoFar = null;
    public int bestMoveAlpha = Integer.MIN_VALUE;
    public boolean stalemate = false;
    public Thread timerThread = null; //Does all the work in the algorithm - and constantly updates best move


    private TimedAlgorithm() { }


    public Move getAIMove()
    {
        Thread algorithmThread = new Thread(() -> {
            Algorithm.makeMove(GameController.getInstance().board);
        });
        try {
            this.timerThread = Thread.currentThread();
            algorithmThread.start();
            maxWaitTime = (15000+500*GameController.getInstance().board.getFullMoves()) > 25000 ? 25000:(15000+500*GameController.getInstance().board.getFullMoves()) ;
            Thread.sleep(maxWaitTime);
        } catch (InterruptedException e) {

        }
        finally {
            Move result = bestMoveSoFar;
            Algorithm.running = false;
            return result;
        }

    }

    public static TimedAlgorithm getINSTANCE()
    {
        if(INSTANCE == null)
            INSTANCE = new TimedAlgorithm();
        return INSTANCE;
    }


}
