
package SnakeGame.AI;

import SnakeGame.Enum.Direction;
import SnakeGame.Enum.GameStatus;
import SnakeGame.Snake;
import SnakeGame.Util.GeometricalDistance;
import SnakeGame.Util.UtilBase;
import SnakeGame.Util.UtilEuristic;

import java.awt.*;
import java.util.*;

public class BestFirstAstar extends IA {
    public BestFirstAstar(int height, int width, int maxFoodOnBoard) {
        super(height, width, maxFoodOnBoard);
    }

    public GameStatus update() {
        if (ia.isMovesFinisched()) {
            ia.setRelativeMoves(getMossa());
            ready = true;
        }
        if (ready)
            return super.update();
        return GameStatus.Waiting;
    }


    public UtilBase simulate(LinkedList<Direction> moves) {
        Snake s = new Snake(ia);
        s.setRelativeMoves(moves);
        while (!s.isMovesFinisched()) {
            s.move();
            if (s.checkSelfCollision() || outOfTheMap(s) || checkCollition(s)) {
                s.kill();
                break;
            }
        }
        int distance = Integer.MAX_VALUE;
        for (Point p : getFood()) {
            int x = GeometricalDistance.distanzaManhattan(s.getHead(), p);
            if (x < distance)
                distance = x;
        }
        return new UtilEuristic(s.getHead(), s.isAlive(), moves, distance + 1);
    }

    public LinkedList<Direction> getMossa() {
        ready = false;
        System.out.println("Inizio Calcolo!");
        Direction[] avaiableDirection = new Direction[]{Direction.Up, Direction.Left, Direction.Right};

        PriorityQueue<UtilEuristic> frontiera = new PriorityQueue<>();
        HashSet<Point> esplorati = new HashSet<>();
        frontiera.add(new UtilEuristic(ia.getHead(), ia.isAlive(), 0));

        long timeStart = System.currentTimeMillis();
        while (!frontiera.isEmpty()) {
            UtilBase padre = frontiera.poll();
            if (padre.isAlive() && isFood(padre.getHead())) {
                long timeEnd = System.currentTimeMillis();
                String out = "Tempo Necessario per la ricerca: "
                        + (timeEnd - timeStart) +
                        " millisecondi\n"
                        + padre.getMoves().size() +
                        " mosse necessarie per arrivare ad una soluzione";
                System.out.println(out);
                return padre.getMoves();
            }
            esplorati.add(padre.getHead());

            for (Direction d : avaiableDirection) {
                UtilEuristic figlio = (UtilEuristic) simulate(padre.fakeAdd(d));

                if (!esplorati.contains(figlio.getHead()) && !frontiera.contains(figlio))
                    frontiera.add(figlio);
                else
                    for (UtilEuristic u : frontiera)
                        if (u.equals(figlio) && u.compareTo(figlio) > 0) {
                            frontiera.remove(u);
                            frontiera.add(figlio);
                            break;
                        }
            }
        }
        System.out.println("Nessuna mossa trovata");
        System.out.println("Destinato alla morte con una lunghezza di " + ia.getCoords().size());
        return new LinkedList<>();
    }
}