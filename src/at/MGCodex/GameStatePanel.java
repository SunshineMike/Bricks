package at.MGCodex;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameStatePanel extends JPanel {

    private int score = 0;
    private int level = 1;
    private int life = 3;
    private int shots = 0;

    private int ballVelX;
    private int ballVelY;
    private int ballX;
    private int ballY;
    private int ballSize;
    private int padSize;
    private int padX;
    private int padY;
    private int padSpeed;

    private int lowestHighscore;

    Image logo;
    ArrayList<String> lootList = new ArrayList<>();


    public GameStatePanel() {
        setPreferredSize(new Dimension(300,900));
        setBackground(Color.BLACK);
        setLayout(null);
        GamePanel.gameState = this;
        Brick.gameState = this;
        BlockingBrick.gameState = this;
        logo = loadImage("pic\\Bricks.png");
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

        Font header = new Font(Font.SANS_SERIF, Font.BOLD, 25);
        Font basic = new Font(Font.MONOSPACED, Font.BOLD, 20);
        Color mainColor = new Color(9, 55, 235);
        Color secColor = new Color(9, 55, 155);

        g2.drawImage(logo, -20,0, getWidth() + 40, 90, null);

        g2.setColor(mainColor);
        g2.fillRect(0,0,3, getHeight());
        g2.fillRect(getWidth() - 3,0,3, getHeight());
        g2.fillRect(0,0, getWidth(), 3);
        g2.fillRect(0,90, getWidth(), 3);
        g2.fillRect(0,210, getWidth(), 3);
        g2.fillRect(0,getHeight()-3, getWidth(), 3);
        g2.fillRect(0, 500, getWidth(), 3);
        g2.fillRect(0, 680, getWidth(), 3);
        g2.fillRect(0, 840, getWidth(), 3);


        g2.setColor(secColor);
        g2.setFont(header);
        g2.drawString("Score: " + score, 20, 130);
        g2.drawString("Level: " + level, 20,160);
        g2.drawString("Balls:  " + life, 20,190);

        g2.setFont(header);
        g2.drawString("Ball:", 20,240);
        g2.setFont(basic);
        g2.drawString("x: " + ballX, 50, 265);
        g2.drawString("y: " + ballY, 50, 285);
        g2.drawString("Size: " + ballSize, 50, 305);
        g2.drawString("Speed X: " + ballVelX, 50, 325);
        g2.drawString("Speed Y: " + ballVelY, 50, 345);

        g2.setFont(header);
        g2.drawString("Paddle:", 20,395);
        g2.setFont(basic);
        g2.drawString("x: " + padX, 50,420);
        g2.drawString("y: " + padY, 50,440);
        g2.drawString("Size: " + padSize, 50,460);
        g2.drawString("Speed: " + padSpeed, 50, 480);


        g2.setFont(header);
        g2.drawString("Last loot", 20,530);
        g2.setFont(basic);

        int index = lootList.size() - 1;
        int counter = 0;
        int start = Math.max(0, index - 5);

        for (int i = index; i >= start; i--) {
            if (lootList.get(i).contains("+")) {
                g2.setColor(new Color(45, 154, 3));
            } else if (lootList.get(i).contains("-")) {
                g2.setColor(new Color(154, 4, 4));
            }
            g2.drawString(lootList.get(i), 50, 560 + (21 * counter));
            counter++;
        }

        g2.setColor(secColor);
        g2.setFont(header);
        g2.drawString("Highscore", 20,710);
        g2.setFont(basic);
        ArrayList<HighScore> highscore = HighScoreManager.getHighscores();
        lowestHighscore = highscore.get(4).score;
        counter = 0;
        for (HighScore hs : highscore) {
            g2.drawString((counter+1) + ".  " + hs.name + ": " + hs.score, 50,740 + (counter*20));
            counter++;
        }

        g2.setFont(header);
        g2.drawString("Controls", 20,870);
        g2.setFont(basic);
        g2.drawString("[A] --- Paddle left", 50,900);
        g2.drawString("[D] --- Paddle right", 50,925);
        g2.drawString("[SPACE] --- Start / Stop", 50,950);
        g2.drawString("[R-Click] --- Laser", 50,975);
    }

    //region Getter & Setter
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public int getBallVelX() {
        return ballVelX;
    }

    public void setBallVelX(int ballVelX) {
        this.ballVelX = ballVelX;
    }

    public int getBallVelY() {
        return ballVelY;
    }

    public void setBallVelY(int ballVelY) {
        this.ballVelY = ballVelY;
    }

    public int getBallX() {
        return ballX;
    }

    public void setBallX(int ballX) {
        this.ballX = ballX;
    }

    public int getBallY() {
        return ballY;
    }

    public void setBallY(int ballY) {
        this.ballY = ballY;
    }

    public int getBallSize() {
        return ballSize;
    }

    public void setBallSize(int ballSize) {
        this.ballSize = ballSize;
    }

    public int getPadSize() {
        return padSize;
    }

    public void setPadSize(int padSize) {
        this.padSize = padSize;
    }

    public int getPadX() {
        return padX;
    }

    public void setPadX(int padX) {
        this.padX = padX;
    }

    public int getPadY() {
        return padY;
    }

    public void setPadY(int padY) {
        this.padY = padY;
    }

    public int getPadSpeed() {
        return padSpeed;
    }

    public void setPadSpeed(int padSpeed) {
        this.padSpeed = padSpeed;
    }

    public int getLowestHighscore() {
        return lowestHighscore;
    }

    public void setLowestHighscore(int lowestHighscore) {
        this.lowestHighscore = lowestHighscore;
    }

    public ArrayList<String> getLootList() {
        return lootList;
    }

    public void setLootList(ArrayList<String> lootList) {
        this.lootList = lootList;
    }
    //endregion
}
