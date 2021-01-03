package SnakeGame.IA;

import SnakeGame.Enum.Direction;
import SnakeGame.Enum.GameStatus;
import SnakeGame.Snake;
import SnakeGame.Util.UtilBase;

import java.awt.*;
import java.util.*;

public class RicercaInProfondita extends IA {
    public RicercaInProfondita(int height, int width, int maxFoodOnBoard) {
        super(height, width, maxFoodOnBoard);
    }


    @Override
    public GameStatus update() {
        Snake ia = getSnake();
        if (ia.isMovesFinisched()) {
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

        LinkedList<UtilBase> frontiera = new LinkedList<>();
        HashSet<Point> esplorati = new HashSet<>();
        frontiera.add(new UtilBase(ia.getHead(), ia.isAlive()));

        long timeStart = System.currentTimeMillis();
        while (!frontiera.isEmpty()) {
            UtilBase padre = frontiera.removeFirst();
            if (isFood(padre.getHead()))
                return padre.getMoves();
            esplorati.add(padre.getHead());

            for (Direction d : avaiableDirection) {
                UtilBase figlio = simulate(padre.fakeAdd(d));

                if (!esplorati.contains(figlio.getHead()) && !frontiera.contains(figlio)) {
                    if (figlio.isAlive() && isFood(figlio.getHead())) {
                        long timeEnd = System.currentTimeMillis();
                        String out = "Tempo Necessario per la ricerca: "
                                + (timeEnd - timeStart) +
                                " millisecondi\n"
                                + figlio.getMoves().size() +
                                " mosse necessarie per arrivare ad una soluzione";
                        System.out.println(out);
                        return figlio.getMoves();
                    } else {
                        frontiera.addFirst(figlio);
                    }
                }
            }
        }
        System.out.println("Nessuna mossa trovata");
        System.out.println("Destinato alla morte con una lunghezza di " + ia.getCoords().size());
        return new LinkedList<>();
    }
}
