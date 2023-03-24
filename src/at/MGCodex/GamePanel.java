package at.MGCodex;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements MouseListener {

    public static final int WIDTH = 900;
    public static final int HEIGHT = 1000;
    public static boolean buff = false;
    public static boolean debuff = false;
    public static StatsPanel stats;

    public boolean movePaddleLeft = false;
    public boolean movePaddleRight = false;

    private final int DELAY = 8;
    private boolean randomLevels = false;

    private int displayTimeLaser;
    private int displayTimeLoot;
    private int xTarget;
    private int yTarget;
    private boolean gameOver = false;
    private boolean missed = false;
    private boolean cleared = false;
    private boolean laser = false;

    Timer timer;

    Image backdrop;
    Image imgGameState;
    Image imgLootText;

    Paddle pad;
    ArrayList<Ball> balls = new ArrayList<>();
    ArrayList<Brick> bricks = new ArrayList<>();
    ArrayList<BlockingBrick> blockingBricks = new ArrayList<>();
    ArrayList<Loot> greenLoot = new ArrayList<>();
    ArrayList<Loot> redLoot = new ArrayList<>();


    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBackground(Color.BLACK);
        addMouseListener(this);

        backdrop = loadImage("pic\\wallpaper.jpg");
        balls.add(new Ball("pic\\Ball_red.png"));
        pad = new Paddle(WIDTH/2-80,HEIGHT-50,160,16,10);

        loadBricks();
        loadLootTables();
        gameLoop();
    }

    private void gameLoop() {
        timer = new Timer(DELAY, e -> {
            removeDeadBricks();
            moveBlockingBrick();
            moveBrick();
            movePad();
            ballCollision();
            loot();
            gameState();
            repaint();
            refreshStats();
        });
    }

    private void moveBlockingBrick() {
        for (BlockingBrick blockingBrick : blockingBricks) {
            blockingBrick.move();
        }
    }

    private void removeDeadBricks() {
        if (blockingBricks.size() > 0) {
            for (int i = blockingBricks.size() - 1; i >= 0; i--) {
                if (!blockingBricks.get(i).isAlive()) {
                    blockingBricks.remove(i);
                }
            }
        }
    }

    private void moveBrick() {
        for (Brick brick : bricks) {
            if (brick.isFalling()) {
                brick.fall();
            }
        }
    }

    private void movePad() {
        if (movePaddleLeft) {
            pad.moveLeft();
        }
        else if (movePaddleRight) {
            pad.moveRight();
        }
    }

    private void ballCollision() {
        for (Ball ball : balls) {
            for (int i = 0; i < Math.abs(ball.getVelX()); i++) {
                ball.moveX();
                ball.collisionWalls();
                ball.collisionPaddle(pad);
                ball.collisionBricks(bricks);
                ball.collisionBlockingBrick(blockingBricks);
            }
            for (int i = 0; i < Math.abs(ball.getVelY()); i++) {
                ball.moveY();
                ball.collisionWalls();
                ball.collisionPaddle(pad);
                ball.collisionBricks(bricks);
                ball.collisionBlockingBrick(blockingBricks);
            }
        }
    }

    private void loot() {

        if (buff || debuff) {

            int index = getLootIndex();
            displayTimeLoot = 90;

            if (buff) {
                imgLootText = greenLoot.get(index).getImgText();
                stats.lootList.add(greenLoot.get(index).getText());
                greenLoot.get(index).drop();
                buff = false;
            }
            else if (debuff) {
                imgLootText = redLoot.get(index).getImgText();
                stats.lootList.add(redLoot.get(index).getText());
                redLoot.get(index).drop();
                debuff = false;
            }
        }
    }

    private void gameState() {

        // Level clear
        if (isClear(bricks)) {
            imgGameState = loadImage("pic\\lvl_clear.png");
            pad.centerPosition();
            balls.get(0).centerPosition(pad);
            blockingBricks.clear();
            stats.setLevel(stats.getLevel() + 1);
            if (stats.getLevel() > 9) {
                randomLevels = true;
            }
            displayTimeLaser = 0;
            cleared = true;
            loadBricks();
            timer.stop();
        }

        // Missed
        if (balls.get(0).getY() > HEIGHT) {
            balls.clear();
            balls.add(new Ball("pic\\Ball_red.png"));
            imgGameState = loadImage("pic\\miss.png");
            pad = new Paddle(WIDTH / 2 - 80, HEIGHT - 50, 160, 16, 10);
            for (Brick brick : bricks) {
                if (brick.isFalling()) {
                    brick.setAlive(false);
                    brick.setFalling(false);
                }
            }
            stats.setLife(stats.getLife() - 1);
            stats.lootList.clear();
            missed = true;
            timer.stop();
        }
        for (Brick brick : bricks) {
            if(brick.isAlive() && brick.isFalling() && brick.getY() + brick.getHeight() > HEIGHT) {
                brick.setAlive(false);
                brick.setFalling(false);
                balls.clear();
                balls.add(new Ball("pic\\Ball_red.png"));
                imgGameState = loadImage("pic\\miss.png");
                pad = new Paddle(WIDTH / 2 - 80, HEIGHT - 50, 160, 16, 10);
                stats.setLife(stats.getLife() - 1);
                stats.lootList.clear();
                missed = true;
                timer.stop();
            }
        }

        // Game Over
        if (stats.getLife() == 0) {
            imgGameState = loadImage("pic\\game_over.png");
            if (stats.getScore() > stats.getLowestHighscore()) {
                String name = JOptionPane.showInputDialog("Enter your name!");
                HighScoreManager.addHighscore(stats.getScore(), name);
            }
            stats.setLife(3);
            stats.setScore(0);
            stats.setLevel(1);
            loadBricks();
            missed = false;
            gameOver = true;
        }
    }

    private void refreshStats() {
        stats.setShots(pad.getShots());
        stats.setBallSize(balls.get(0).getSize());
        stats.setBallX(balls.get(0).getX());
        stats.setBallY(balls.get(0).getY());
        stats.setBallVelX(balls.get(0).getVelX());
        stats.setBallVelY(balls.get(0).getVelY());
        stats.setPadSize(pad.getWidth());
        stats.setPadX(pad.getX());
        stats.setPadY(pad.getY());
        stats.setPadSpeed(pad.getSpeed());
        stats.repaint();
    }

    private void loadBricks() {
        bricks.clear();
        Color color = new Color(3, 5, 175);

        int rows = 7;
        int cols = 9;
        int brickWidth = 80;
        int brickHeight = 20;
        int spacer = 4;
        int offsetX = (WIDTH - cols * (brickWidth + spacer)) / 2;
        int offsetY = 80;
        int[][] levelMap = new int[rows][cols];


        if (randomLevels) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    levelMap[row][col] = 9;
                }
            }
        }
        else {
            levelMap = Level.getLevel(stats.getLevel());
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = offsetX + (col * (brickWidth + spacer));
                int y = offsetY + (row * (brickHeight + spacer));
                if (levelMap[row][col] == 1) {
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, 0, 1, color));
                }
                else if (levelMap[row][col] == 2) {
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, 0, 2, color));
                }
                else if (levelMap[row][col] == 3) {
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, 0, 3, color));
                }
                else if (levelMap[row][col] == 4) {
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, 1, color));
                }
                else if (levelMap[row][col] == 5) {
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, 2, 1, color));
                }
                else if (levelMap[row][col] == 9) {
                    Random random = new Random();
                    int hits = random.nextInt(1, 4);
                    int loot;
                    int randomValue = random.nextInt(1, 101);
                    if (randomValue <= 10) {
                        loot = 1;
                        hits = 1;
                    } else if (randomValue >= 90) {
                        loot = 2;
                        hits = 1;
                    } else {
                        loot = 0;
                    }
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, loot, hits, color));
                }
            }
        }
    }

    private void loadLootTables() {

        Runnable command;


        command = () -> balls.add(new Ball("pic\\Ball_blue.png"));
        greenLoot.add(new Loot(command, loadImage("loot\\ExtraBall+.png"), " + Extra Ball"));

        command = () -> stats.setLife(stats.getLife() + 1);
        greenLoot.add(new Loot(command, loadImage("loot\\Life+.png"), " + Life"));

        command = () -> balls.get(0).setSize(balls.get(0).getSize() + 5);
        greenLoot.add(new Loot(command, loadImage("loot\\BallSize+.png"), " + Ball Size"));

        command = () -> balls.get(0).setVelY((int) (balls.get(0).getVelY() * 0.8));
        greenLoot.add(new Loot(command, loadImage("loot\\Speed+.png"), " + Speed"));

        command = () -> pad.setWidth(pad.getWidth() + 40);
        greenLoot.add(new Loot(command, loadImage("loot\\PadSize+.png"), " + Pad Size"));

        command = () -> pad.setSpeed(pad.getSpeed() + 2);
        greenLoot.add(new Loot(command, loadImage("loot\\PadSpeed+.png"), " + Pad Speed"));

        command = () -> pad.addShots();
        greenLoot.add(new Loot(command, loadImage("loot\\Laser+.png"), " + Laser"));

        command = () -> pad.moveDown(70);
        greenLoot.add(new Loot(command, loadImage("loot\\Distance+.png"), " + Distance"));



        command = () -> pad.removeShots();
        redLoot.add(new Loot(command, loadImage("loot\\Laser-.png"), " - Laser"));

        command = this::spawnBlockingBrick;
        redLoot.add(new Loot(command, loadImage("loot\\ToDo-.png"), " - Blocking Brick"));

        command = () -> randomFallingBrick(bricks);
        redLoot.add(new Loot(command, loadImage("loot\\FallingBrick-.png"), " - Falling Brick"));

        command = () -> balls.get(0).setSize(balls.get(0).getSize() - 5);
        redLoot.add(new Loot(command, loadImage("loot\\BallSize-.png"), " - Ball Size"));

        command = () -> pad.setWidth(pad.getWidth() - 40);
        redLoot.add(new Loot(command, loadImage("loot\\PadSize-.png"), " - Pad Size"));

        command = () -> pad.setSpeed(pad.getSpeed() - 2);
        redLoot.add(new Loot(command, loadImage("loot\\PadSpeed-.png"), " - Pad Speed"));

        command = () -> balls.get(0).setVelY((int) (balls.get(0).getVelY() * 1.2));
        redLoot.add(new Loot(command, loadImage("loot\\Speed-.png"), " - Speed"));

        command = () -> pad.moveUp(70);
        redLoot.add(new Loot(command, loadImage("loot\\Distance-.png"), " - Distance"));
    }

    private int getLootIndex() {

        Random random = new Random();
        int roll = random.nextInt(0,100);

        if (roll < 5) {
            return random.nextInt(0,1);
        }
        else if (roll < 10) {
            return random.nextInt(1,2);
        }
        else if (roll < 50) {
            return random.nextInt(2,4);
        }
        else if (roll < 100) {
            return random.nextInt(4, greenLoot.size());
        }
        else {
            System.err.println("Error: Loot index (getLootIndex())");
            return 0;
        }
    }

    private void randomFallingBrick(ArrayList<Brick> bricks) {
        int maxY = Integer.MIN_VALUE;
        for (Brick brick : bricks) {
            if (brick.isAlive() && !brick.isFalling() && brick.getY() > maxY) {
                maxY = brick.getY();
            }
        }

        ArrayList<Brick> bottomLineBricks = new ArrayList<>();
        for (Brick brick : bricks) {
            if (brick.isAlive() && brick.getY() == maxY) {
                bottomLineBricks.add(brick);
            }
        }

        Random random = new Random();
        if (bottomLineBricks.size() > 0) {
            int randomIndex = random.nextInt(bottomLineBricks.size());
            Brick selectedBrick = bottomLineBricks.get(randomIndex);
            selectedBrick.setHits(1);
            selectedBrick.setLoot(0);
            selectedBrick.setFalling(true);
        }
    }

    private void spawnBlockingBrick() {
        Random random = new Random();

        int count = blockingBricks.size();
        int width = random.nextInt(50, 350);
        int height = 40;
        int x = random.nextInt(0, WIDTH - width);
        int y = 280;
        int hits = 6;
        int speed = random.nextInt(-10, 10);

        blockingBricks.add(new BlockingBrick(x,y + ((height+20)*count),width,height,hits,speed));
    }

    private boolean isClear(ArrayList<Brick> bricks) {
        for (Brick brick : bricks) {
            if (brick.isAlive()) {
                return false;
            }
        }
        for (BlockingBrick blockingBrick : blockingBricks) {
            if (blockingBrick.isAlive()) {
                return false;
            }
        }
        return true;
    }

    private Image loadImage(String fileName) {
        Image image = null;
        try {
            image = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //g2.drawImage(backdrop, 0, 0, getWidth(), getHeight(), this);

        Color mainColor = new Color(9, 55, 235);
        g2.setColor(mainColor);
        g2.fillRect(0,0, getWidth(),3);
        g2.fillRect(0,getHeight() - 3, getWidth(),3);
        g2.fillRect(0, 0, 3, getHeight());

        g2.setColor(new Color(14, 8, 126));
        if (gameOver) {
            g2.drawImage(imgGameState, WIDTH/2 - imgGameState.getWidth(this)/2, HEIGHT/2 - 80, null);
            gameOver = false;
            displayTimeLoot = 0;
        }
        else if (missed) {
            g2.drawImage(imgGameState, WIDTH/2 - imgGameState.getWidth(this)/2, HEIGHT/2 - 80, null);
            int offset = imgGameState.getWidth(this) / (stats.getLife()+1);
            for (int i = 1; i < stats.getLife()+1; i++) {
                g2.drawImage(balls.get(0).getBALL_TEXTURE(),WIDTH/2 - imgGameState.getWidth(this)/2 + (offset * i - 15), HEIGHT/2 + 80, 30,30, null);
            }

            missed = false;
            displayTimeLoot = 0;
        }
        else if (cleared) {
            g2.drawImage(imgGameState, WIDTH/2 - imgGameState.getWidth(this)/2, HEIGHT/2 - 80, null);
            cleared = false;
            displayTimeLoot = 0;
        }

        pad.draw(g2);
        if (laser && displayTimeLaser > 0) {
            for (Brick brick : bricks) {
                if (brick.isMarked()) {
                    xTarget = brick.getX() + brick.getWidth()/2;
                    yTarget = brick.getY() + brick.getHeight()/2;
                    brick.setMarked(false);
                }
            }
            for (BlockingBrick blockingBrick : blockingBricks) {
                if(blockingBrick.isMarked()) {
                    xTarget = blockingBrick.getX() + blockingBrick.getWidth()/2;
                    yTarget = blockingBrick.getY() + blockingBrick.getHeight()/2;
                    blockingBrick.setMarked(false);
                }
            }
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(3.0f));
            g2.drawLine(pad.getX() + pad.getWidth()/2, pad.getY(), xTarget, yTarget);
            g2.setStroke(new BasicStroke(1.0f));
            g2.setColor(new Color(14, 8, 126));

            displayTimeLaser--;
            if (displayTimeLaser == 0) {
                laser = false;
            }
        }

        if (displayTimeLoot > 0) {
            int x = WIDTH / 2 - imgLootText.getWidth(this) / 2;
            int y = HEIGHT / 2 - displayTimeLoot;
            if (displayTimeLoot <= 20) {
                int width = (imgLootText.getWidth(this) / 20) * displayTimeLoot;
                int height = (imgLootText.getHeight(this) / 20) * displayTimeLoot;
                g2.drawImage(imgLootText, WIDTH/2-width/2, y, width, height, this);
            }
            else {
                g2.drawImage(imgLootText, x, y, imgLootText.getWidth(this), imgLootText.getHeight(this), this);
            }
            displayTimeLoot--;
        }

        for (Brick brick : bricks) {
            if (brick.isAlive()) {
                brick.draw(g2);
            }
        }

        for (BlockingBrick blockingBrick : blockingBricks) {
            blockingBrick.draw(g2);
        }

        for (Ball ball : balls) {
            ball.draw(g2);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int xClick = e.getX();
        int yClick = e.getY();
        if (pad.getShots() > 0) {
            for (Brick brick : bricks) {
                if (xClick > brick.getX() && xClick < brick.getX() + brick.getWidth() && yClick > brick.getY() && yClick < brick.getY() + brick.getHeight()) {
                    brick.setMarked(true);
                    brick.setAlive(false);
                    pad.removeShots();
                    laser = true;
                    displayTimeLaser = 10;
                }
            }
            for (BlockingBrick blockingBrick : blockingBricks) {
                if (xClick > blockingBrick.getX() && xClick < blockingBrick.getX() + blockingBrick.getWidth() && yClick > blockingBrick.getY() && yClick < blockingBrick.getY() + blockingBrick.getHeight()) {
                    blockingBrick.setMarked(true);
                    blockingBrick.hit();
                    pad.removeShots();
                    laser = true;
                    displayTimeLaser = 10;
                }
            }
        }
    }

    //region Unused
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    //endregion
}

