import SnakeGame.GameLoop;
import SnakeGame.IA.*;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private GameLoop snakeBoard;

    public Main(){initMain();}

    public void initMain(){
        setResizable(false);

        snakeBoard = new GameLoop(new HillClimbing(50, 50, 1));
        add(snakeBoard);
        snakeBoard.start();

        setTitle("Snake [FIA] Project");
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Frame game = new Main();
            game.setVisible(true);
        });
    }

}
