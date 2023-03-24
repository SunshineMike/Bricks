package at.MGCodex;

import java.awt.*;

public class Brick {

    private int x;
    private int y;
    private int width;
    private int height;
    private int hits;
    private int loot;
    private boolean isAlive;
    private boolean isMarked;
    private boolean isFalling;
    private Color color;

    public static GameStatePanel gameState;


    public Brick(int x, int y, int width, int height, int loot, int hits, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.loot = loot;
        this.hits = hits;
        this.color = color;

        this.isAlive = true;
        this.isMarked = false;
        this.isFalling = false;
    }

    public void draw(Graphics2D g2) {
        if (this.isFalling) {
            g2.setColor(new Color(255, 255 - (int)((y/10)*2.5), (int)((y/10)*2.5)));
            g2.drawRect(x, y, width, height);
        }
        else if (this.hits == 1) {
            g2.setColor(this.color);
            g2.drawRect(x, y, width, height);
        }
        else if (this.hits == 2) {
            g2.setColor(this.color);
            g2.drawRect(x, y, width, height);
            g2.setColor(new Color(8, 29, 124));
            g2.drawRect(x+6,y+6,width-12,height-12);
        }
        else if (this.hits == 3) {
            g2.setColor(this.color);
            g2.drawRect(x, y, width, height);
            g2.setColor(new Color(8, 29, 124));
            g2.drawRect(x+6,y+6,width-12,height-12);
            g2.setColor(new Color(4, 18, 70));
            g2.fillRect(x+6,y+6,width-12,height-12);
        }

        if (this.loot == 1) {
            g2.setColor(Color.GREEN);
            g2.drawRect(x,y,width,height);
        }
        else if (this.loot == 2) {
            g2.setColor(Color.RED);
            g2.drawRect(x,y,width,height);
        }
    }

    public void hit() {
        this.hits--;
        switch (loot) {
            case 1 -> GamePanel.buff = true;
            case 2 -> GamePanel.debuff = true;
        }
        if (this.hits == 0) {
            this.isAlive = false;
        }
        gameState.setScore(gameState.getScore() + 10);
    }

    public void fall() {
        if (isFalling) {
            this.y += 1;
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

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    public int getLoot() {
        return loot;
    }

    public void setLoot(int loot) {
        this.loot = loot;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void setFalling(boolean falling) {
        isFalling = falling;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    //endregion
}
