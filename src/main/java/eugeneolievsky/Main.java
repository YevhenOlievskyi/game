package eugeneolievsky;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        SnakeGame snakeGame = new SnakeGame();

        frame.setSize(620, 620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(snakeGame);
        frame.setVisible(true);
    }
}
