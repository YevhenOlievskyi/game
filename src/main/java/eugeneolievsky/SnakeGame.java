package eugeneolievsky;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener ,KeyListener {
    public SnakeGame() {
        setSize(600,600);
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        gameOver = false;
        isPaused = false;

        snakeHead = new Snake(4,4);
        snakeBody = new ArrayList<>();

        food = new Snake(10,10);
        random = new Random();

        setVisible(true);
        timer = new Timer(100,this);
        timer.start();
    }
    private class Snake{
        int x;
        int y;
        public Snake(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private boolean gameOver;
    private boolean isPaused;
    private Timer timer;

    private final int boardWidth = 600;
    private final int boardHeight = 600;
    private final int tileSize = 20;
    private int maxLengthSnake = (boardHeight / tileSize) * (boardWidth / tileSize);

    private int dx = 1;
    private int dy = 0;
    private Snake snakeHead;
    private ArrayList<Snake> snakeBody;

    private char prevDirection = 'd';
    private Snake food;
    private Random random;
    boolean canChangeDirection = true;


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x <= boardWidth; x += tileSize) {
            g.drawLine(x, 0, x, boardHeight);
        }
        for (int y = 0; y <= boardHeight; y += tileSize) {
            g.drawLine(0, y, boardWidth, y);
        }
        g.setColor(Color.GREEN);
        g.fillRect(snakeHead.x * tileSize,snakeHead.y * tileSize,tileSize,tileSize);

        for (Snake part : snakeBody) {
            g.setColor(Color.green);
            g.fillRect(part.x * tileSize, part.y * tileSize, tileSize, tileSize);
        }
        g.setColor(Color.RED);
        g.fillRect(food.x * tileSize,food.y * tileSize,tileSize,tileSize);

        if (isPaused) {
            g.setColor(Color.WHITE);
            Font pausedFont = new Font("Arial", Font.BOLD, 30);
            g.setFont(pausedFont);
            g.drawString("Paused", boardWidth / 2 - 70, boardHeight / 2);
        }

    }
    public void restartGame(){
        snakeBody.clear();
        snakeHead= new Snake(5,5);
        dx =1;
        dy =0;
        gameOver = false;
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused && !gameOver) {
            gameIsOver();
            grow();
            movie();
            canChangeDirection = true;
            repaint();
        }
    }
    private void grow(){
        if (food.x == snakeHead.x && food.y == snakeHead.y){
            placeFood();
            if (snakeBody.isEmpty()){
                snakeBody.add(new Snake(snakeHead.x,snakeHead.y));
            } else {
                Snake snake = snakeBody.get(snakeBody.size()-1);
                snakeBody.add(new Snake(snake.x,snake.y));
            }
        }
    }

    private void movie(){
        int prevX = snakeHead.x;
        int prevY = snakeHead.y;

        snakeHead.x+=dx;
        snakeHead.y+=dy;

        for (int i = 0; i <snakeBody.size() ; i++) {
            Snake snake = snakeBody.get(i);

            int snakex = snake.x;
            int snakey = snake.y;

            snake.x = prevX;
            snake.y = prevY;

            prevX = snakex;
            prevY = snakey;
        }
    }

    private void placeFood(){
        food.x = random.nextInt(getWidth() / tileSize);
        food.y = random.nextInt(getHeight() / tileSize);
    }

    private void gameIsOver(){
        if (snakeHead.x < 0 || snakeHead.y < 0 ||
            snakeHead.x >= boardWidth / tileSize ||
            snakeHead.y >= boardHeight / tileSize) {
            gameOver = true;
        }
        for (Snake part : snakeBody) {
            if (snakeHead.x == part.x && snakeHead.y == part.y) {
                gameOver = true;
            }
        }
        if (gameOver || maxLengthSnake == snakeBody.size()) restartGame();

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_ESCAPE) {
            isPaused = !isPaused;
            if (isPaused) {
                timer.stop();
            } else {
                timer.start();
            }
            repaint();
        }

        if (KeyEvent.VK_W == code && prevDirection != 's'){
            dy = -1;
            dx = 0;
            prevDirection = 'w';
            canChangeDirection = false;
        } else if (KeyEvent.VK_D == code && prevDirection != 'a') {
            dx = 1;
            dy = 0;
            prevDirection = 'd';
            canChangeDirection = false;
        } else if (KeyEvent.VK_A == code && prevDirection != 'd') {
            dy = 0;
            dx = -1;
            prevDirection = 'a';
            canChangeDirection = false;
        } else if (KeyEvent.VK_S == code && prevDirection != 'w') {
            dx=0;
            dy=1;
            prevDirection = 's';
            canChangeDirection = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

}
