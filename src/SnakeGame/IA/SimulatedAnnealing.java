package SnakeGame.IA;

import SnakeGame.Enum.Direction;
import SnakeGame.Enum.GameStatus;
import SnakeGame.Snake;
import SnakeGame.Util.UtilBase;
import SnakeGame.Util.Utilities;

import java.util.LinkedList;
import java.util.Random;

public class SimulatedAnnealing extends IA {
    private final static double temperature = 1000;
    private final static double coolingFactor = 0.997;

    public SimulatedAnnealing(int height, int width, int maxFoodOnBoard) {
        super(height, width, maxFoodOnBoard);
    }

    public GameStatus update() {
        Snake ia = getSnake();

        if (ia.isMovesFinisched()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ia.setRelativeMoves(getMossa());
            ready = true;
        }
        if (ready)
            return super.update();
        return GameStatus.Waiting;
    }

    private LinkedList<Direction> getMossa() {
        ready = false;
        Snake ia = getSnake();

        Random r = new Random();
        int avaiableDirectionSize = avaiableDirection.length;
        UtilBase current = new UtilBase(ia.getHead(), ia.isAlive());
        for (double t = temperature; t > 1; t *= coolingFactor) {
            UtilBase next = simulate(current.fakeAdd(avaiableDirection[r.nextInt(avaiableDirectionSize)]));
            if (next.isAlive())
                if (Math.random() <= Utilities.probability(getDistanceFromApple(next.getHead()), getDistanceFromApple(current.getHead()), t))
                    current = new UtilBase(next);
        }
        return current.getMoves();
    }

}
