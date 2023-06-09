package at.MGCodex;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Frame extends JFrame implements KeyListener {

    GamePanel gamePanel;
    GameStatePanel gameStatePanel;

    public Frame() {
        setTitle("Bricks 3");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1216,1039);
        setResizable(false);
        setLayout(new BorderLayout());

        gameStatePanel = new GameStatePanel();
        gamePanel = new GamePanel();
        add(gameStatePanel, BorderLayout.EAST);
        add(gamePanel, BorderLayout.CENTER);

        addKeyListener(this);
        setFocusable(true);

        setMouseCursor();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }

    private void setMouseCursor() {
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            BufferedImage image = ImageIO.read(new File("pic\\cross.png"));
            Cursor cursor = toolkit.createCustomCursor(image, new Point(15,15), "CustomCursor");
            setCursor(cursor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == ' ' && gamePanel.timer.isRunning()) {
            gamePanel.timer.stop();
        }
        else if (e.getKeyChar() == ' ' && !gamePanel.timer.isRunning()){
            gamePanel.timer.start();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
            gamePanel.movePaddleLeft = true;
        }
        else if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
            gamePanel.movePaddleRight = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
            gamePanel.movePaddleLeft = false;
        }
        else if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
            gamePanel.movePaddleRight = false;
        }
    }
}