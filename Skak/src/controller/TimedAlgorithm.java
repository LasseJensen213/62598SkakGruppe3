package controller;

import piece.Move;

public class TimedAlgorithm {

    private static TimedAlgorithm INSTANCE = new TimedAlgorithm();
    public final static long  MAX_TIME_LIMIT = 25000;
    public final static long START_TIME_LIMIT = 15000;
    public long currentTimeLimit = START_TIME_LIMIT;
    public Move bestMoveSoFar = null;
    public boolean stalemate = false;
    public Thread timerThread = null; //Does all the work in the algorithm - and constantly updates best move
    public boolean post = false;


    private TimedAlgorithm() { }


    public Move getAIMove()
    {
        Thread algorithmThread = new Thread(() -> {
            Algorithm.makeMove(GameController.getInstance().board);
        });
        try {
            this.timerThread = Thread.currentThread();
            algorithmThread.start();
            currentTimeLimit = (START_TIME_LIMIT+500*GameController.getInstance().board.getFullMoves()) > MAX_TIME_LIMIT ?
                                MAX_TIME_LIMIT:(START_TIME_LIMIT+500*GameController.getInstance().board.getFullMoves()) ;
            Thread.sleep(currentTimeLimit);
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
