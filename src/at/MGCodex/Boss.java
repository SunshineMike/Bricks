package at.MGCodex;

import java.awt.*;

public class Boss {

    int x;
    int y;
    int width;
    int height;
    int hits;
    int bossTimer1;
    int bossTimer2;
    boolean isAlive;
    boolean isMarked;
    Runnable attack1;
    Runnable attack2;

    public Boss(int x, int y, int width, int height, int hits, boolean isAlive, Runnable attack1, Runnable attack2) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hits = hits;
        this.isAlive = isAlive;
        this.attack1 = attack1;
        this.attack2 = attack2;

        this.bossTimer1 = 300;
        this.bossTimer2 = 800;
        this.isMarked = false;
    }

    public void attack1() {
        if (this.attack1 != null) {
            this.attack1.run();
        }
    }

    public void attack2() {
        if (this.attack2 != null) {
            this.attack2.run();
        }
    }

    public void hit() {
        this.hits--;
        if (this.hits == 0) {
            this.isAlive = false;
        }
    }

    public void draw(Graphics2D g2, int bossTimer) {
        g2.setColor(new Color(9, 55, 235));
        g2.drawRect(x,y,width,height);

        g2.drawRect(x + width/2 - (13*hits) / 2, y + 30, 13*hits, 40);
        g2.setColor(new Color(11 * (20-hits), 38, 128 - ((20-hits)*6)));
        g2.fillRect(x + 1 + width/2 - (13*hits) / 2, y + 30 + 1, 13*hits - 2, 40 - 2);

        g2.setColor(Color.YELLOW);
        g2.fillRect(x + width/2 - (bossTimer/3) / 2, y + 10, bossTimer/3, 5);
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