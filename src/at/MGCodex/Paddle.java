package at.MGCodex;

import java.awt.*;

public class Paddle {

    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private int shots;

    public Paddle(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.shots = 3;
    }

    public void centerPosition() {
        this.x = GamePanel.WIDTH / 2 - this.getWidth()/2 ;
    }

    public void moveLeft() {
        if (x <= 0) {
            x = 1;
        }
        else {
            x -= speed;
        }
    }

    public void moveRight() {
        if (x + width > GamePanel.WIDTH) {
            x = GamePanel.WIDTH - width - 5;
        }
        else {
            x += speed;
        }
    }

    public void moveUp(int deltaY) {
        this.setY(this.getY() - deltaY);
    }

    public void moveDown(int deltaY) {
        if (this.getY() < GamePanel.HEIGHT - 50) {
            this.setY(this.getY() + deltaY);
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.drawRect(x,y,width,height);
        g2.setColor(Color.RED);
        g2.drawRect(getX() + (int)(getWidth() * 0.25) + 2, getY() + 2, (int)(getWidth() * 0.5) - 4, getHeight()-4);
        g2.setColor(Color.YELLOW);
        g2.drawRect(getX() + 2, getY() + 2, (int)(getWidth() * 0.25) - 4, getHeight()-4);
        g2.drawRect(getX() + (int)(getWidth() * 0.75) + 2, getY() + 2, (int)(getWidth() * 0.25) - 4, getHeight()-4);

        if (shots > 0) {
            int offset = width / (shots + 1);
            for (int i = 1; i <= shots; i++) {
                g2.setColor(Color.GREEN);
                g2.fillOval(x + (offset * i - 3), y + 25, 10,10);
            }
        }
    }

    public void addShots() {
        this.shots++;
    }

    public void removeShots() {
        if (this.shots > 0) {
            this.shots--;
        }
    }

    //region Getter & Setter
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    //endregion
}