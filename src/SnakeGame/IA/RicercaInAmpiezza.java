package SnakeGame.IA;

import SnakeGame.Enum.Direction;
import SnakeGame.Enum.GameStatus;
import SnakeGame.Snake;
import SnakeGame.Util.UtilBase;

import java.awt.*;
import java.util.*;

public class RicercaInAmpiezza extends IA {
    public RicercaInAmpiezza(int height, int width, int maxFoodOnBoard) {
        super(height, width, maxFoodOnBoard);
    }

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
            nodiEsplorati++;
            UtilBase padre = frontiera.removeFirst();
            if (isFood(padre.getHead()))
                return padre.getMoves();
            esplorati.add(padre.getHead());

            for (Direction d : avaiableDirection) {
                UtilBase figlio = simulate(padre.fakeAdd(d));

                if (!esplorati.contains(figlio.getHead()) && !frontiera.contains(figlio)) {
                    if (figlio.isAlive() && isFood(figlio.getHead())) {
                        long timeEnd = System.currentTimeMillis();
                        tempo += (timeEnd - timeStart);
                        nodiAllaSolzuione += figlio.getMoves().size();
                        System.out.println(nodiEsplorati + " " + nodiAllaSolzuione + " "+ tempo);

//                        System.out.println("nodi esplorati: " + nodiEsplorati);
//                        String out = "Tempo Necessario per la ricerca: "
//                                + (timeEnd - timeStart) +
//                                " millisecondi\n"
//                                + figlio.getMoves().size() +
//                                " mosse necessarie per arrivare ad una soluzione" + "\n";
//                        System.out.println(out);
                        return figlio.getMoves();
                    } else {
                        frontiera.addLast(figlio);
                    }
                }
            }
        }
        lunghezzaFinale = ia.getCoords().size();
        System.out.println("Nessuna mossa trovata");
        return new LinkedList<>();
    }
}
