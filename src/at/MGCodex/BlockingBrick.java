package at.MGCodex;

import java.awt.*;
import java.util.ArrayList;

public class BlockingBrick {

    private int x;
    private int y;
    private int width;
    private int height;
    private int hits;
    private int speedX;
    private boolean isMarked;
    private boolean isAlive;

    public static GameStatePanel gameState;

    public BlockingBrick(int x, int y, int width, int height, int hits, int speedX) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hits = hits;
        this.speedX = speedX;

        this.isMarked = false;
        this.isAlive = true;
    }

    public void move() {
        this.x += speedX;
        collisionWalls();
    }

    public void hit() {
        this.hits--;
        if (this.hits == 0) {
            this.isAlive = false;
        }
        gameState.setScore(gameState.getScore() + 10);
    }

    public void collisionWalls() {
        if (this.x < 0) {
            this.x = 0;
            this.speedX = -this.speedX;
        }
        else if (this.x + this.width > GamePanel.WIDTH) {
            this.x = GamePanel.WIDTH - this.width - 5;
            this.speedX = -this.speedX;
        }
    }

    public void collisionBrick(ArrayList<BlockingBrick> blockingBricks, int index) {
        for (int i = 0; i < blockingBricks.size(); i++) {
            if (i != index){
                Rectangle hitBox = new Rectangle(x,y,width,height);
                Rectangle brickHitBox = new Rectangle(blockingBricks.get(i).getX(), blockingBricks.get(i).getY(), blockingBricks.get(0).getWidth(), blockingBricks.get(0).getHeight());

                if (hitBox.intersects(brickHitBox) && this.speedX >= 0 && blockingBricks.get(i).getSpeedX() <= 0) {
                    this.speedX = -this.speedX;
                    blockingBricks.get(i).setSpeedX(-blockingBricks.get(0).getSpeedX());
                }
                else if (hitBox.intersects(brickHitBox) && this.speedX <= 0 && blockingBricks.get(i).getSpeedX() >= 0) {
                    this.speedX = -this.speedX;
                    blockingBricks.get(i).setSpeedX(-blockingBricks.get(0).getSpeedX());
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(6, 17, 224 - (20*this.hits)));
        g2.drawRect(this.x,this.y,this.width,this.height);
        for (int i = this.hits - 1; i > 0; i--) {
            g2.drawRect(this.x + (i*3),this.y + (i*3),this.width - (i*6),this.height - (i*6));
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

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    //endregion
}
