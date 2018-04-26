package controller;

import piece.Move;

public class TimedAlgorithm {

    private static TimedAlgorithm INSTANCE = new TimedAlgorithm();
    private long maxWaitTime = 999999999;
    public Move bestMoveSoFar = null;
    public Thread algorithmThread = null; //Does all the work in the algorithm - and constantly updates best move


    private TimedAlgorithm() { }


    public Move getAIMove()
    {
        Thread algorithmThread = new Thread(() -> {
            Algorithm.makeMove(GameController.getInstance().board);
        });
        this.algorithmThread = algorithmThread;
        try {
            algorithmThread.start();
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
