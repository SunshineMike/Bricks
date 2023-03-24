package at.MGCodex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Ball {

    private final Image BALL_TEXTURE;

    private int x;
    private int y;
    private int velX;
    private int velY;
    private int size;


    public Ball(String picPath) {
        this.BALL_TEXTURE = loadImage(picPath);
        defaultValues();
    }

    public void defaultValues() {
        this.size = 20;
        this.x = GamePanel.WIDTH/2 - this.size/2;
        this.y = GamePanel.HEIGHT - 50 - this.size;
        this.velX = setRandomVelX();
        this.velY = -10;
    }

    public void centerPosition(Paddle pad) {
        this.x = GamePanel.WIDTH/2 - this.size/2;
        this.y = pad.getY() - this.size;
        this.velX = setRandomVelX();
    }

    private int setRandomVelX() {
        Random rng = new Random();
        int value = 0;
        while (value == 0) {
            value = rng.nextInt(-4, 5);
        }
        return value;
    }

    public void moveX() {
        if (velX < 0) {
            this.x -= 1;
        }
        else if (velX > 0) {
            this.x += 1;
        }
    }

    public void moveY() {
        if (velY < 0) {
            this.y -= 1;
        }
        else if (velY > 0) {
            this.y += 1;
        }
    }

    public void collisionWalls() {
        if (x < 0) {
            x = 0;
            velX = -velX;
        }
        if (x + size > GamePanel.WIDTH) {
            x = GamePanel.WIDTH - size;
            velX = -velX;
        }
        if (y < 0) {
            y = 0;
            velY = -velY;
        }
    }

    public void collisionPaddle(Paddle pad) {
        Rectangle hitBoxMid = new Rectangle(pad.getX() + (int)(pad.getWidth() * 0.25), pad.getY(), (int)(pad.getWidth() * 0.5), pad.getHeight()-5);
        Rectangle hitBoxLeft = new Rectangle(pad.getX(), pad.getY(), (int)(pad.getWidth() * 0.25), pad.getHeight()-5);
        Rectangle hitBoxRight = new Rectangle(pad.getX() + (int)(pad.getWidth() * 0.75), pad.getY(), (int)(pad.getWidth() * 0.25), pad.getHeight()-5);
        if (this.getHitBox().intersects(hitBoxMid) && velY > 0) {
            velY = -velY;
        }
        else if (this.getHitBox().intersects(hitBoxLeft) && velY > 0 && velX >= 0) {
            velY = -velY;
            velX = velX - 1;
        }
        else if (this.getHitBox().intersects(hitBoxLeft) && velY > 0 && velX < 0) {
            velY = -velY;
            velX = velX - 1;
        }
        else if(this.getHitBox().intersects(hitBoxLeft) && y + size < pad.getY()) {
            velX = -velX;
        }
        else if (this.getHitBox().intersects(hitBoxRight) && velY > 0 && velX >= 0) {
            velY = -velY;
            velX = velX + 1;
        }
        else if (this.getHitBox().intersects(hitBoxRight) && velY > 0 && velX < 0) {
            velY = -velY;
            velX = velX + 1;
        }
        else if (this.getHitBox().intersects(hitBoxRight) && y + size < pad.getY()) {
            velX = -velX;
        }
    }

    public void collisionBricks(ArrayList<Brick> bricks) {
        int hitBoxDepth = 3;
        for (Brick brick : bricks) {
            if (brick.isAlive()) {
                Rectangle hitBoxTop = new Rectangle(brick.getX() + 1, brick.getY() - 1, brick.getWidth() - 2, hitBoxDepth);
                Rectangle hitBoxBottom = new Rectangle(brick.getX() + 1, brick.getY() + brick.getHeight() - 1, brick.getWidth() - 2, hitBoxDepth);
                Rectangle hitBoxLeft = new Rectangle(brick.getX() - 1, brick.getY() + 1, hitBoxDepth, brick.getHeight() - 2);
                Rectangle hitBoxRight = new Rectangle(brick.getX() + brick.getWidth() - 1, brick.getY() + 1, hitBoxDepth, brick.getHeight() - 2);

                if (this.getHitBox().intersects(hitBoxTop) && this.velY > 0) {
                    this.velY = -this.velY;
                    brick.hit();
                } else if (this.getHitBox().intersects(hitBoxBottom) && this.velY < 0) {
                    this.velY = -this.velY;
                    brick.hit();
                } else if (this.getHitBox().intersects(hitBoxLeft) && this.velX > 0) {
                    this.velX = -this.velX;
                    brick.hit();
                } else if (this.getHitBox().intersects(hitBoxRight) && this.velX < 0) {
                    this.velX = -this.velX;
                    brick.hit();
                }
            }
        }
    }

    public void collisionBlockingBrick(ArrayList<BlockingBrick> blockingBricks) {
        int hitBoxDepth = 3;
        for (BlockingBrick blockingBrick : blockingBricks) {
            if (blockingBrick.isAlive()) {
                Rectangle hitBoxTop = new Rectangle(blockingBrick.getX() + 1, blockingBrick.getY() - 1, blockingBrick.getWidth() - 2, hitBoxDepth);
                Rectangle hitBoxBottom = new Rectangle(blockingBrick.getX() + 1, blockingBrick.getY() + blockingBrick.getHeight() - 1, blockingBrick.getWidth() - 2, hitBoxDepth);
                Rectangle hitBoxLeft = new Rectangle(blockingBrick.getX() - 1, blockingBrick.getY() + 1, hitBoxDepth, blockingBrick.getHeight() - 2);
                Rectangle hitBoxRight = new Rectangle(blockingBrick.getX() + blockingBrick.getWidth() - 1, blockingBrick.getY() + 1, hitBoxDepth, blockingBrick.getHeight() - 2);

                if (this.getHitBox().intersects(hitBoxTop) && this.velY > 0) {
                    this.velY = -this.velY;
                    blockingBrick.hit();
                } else if (this.getHitBox().intersects(hitBoxBottom) && this.velY < 0) {
                    this.velY = -this.velY;
                    blockingBrick.hit();
                } else if (this.getHitBox().intersects(hitBoxLeft) && this.velX > 0) {
                    this.velX = -this.velX;
                    blockingBrick.hit();
                } else if (this.getHitBox().intersects(hitBoxRight) && this.velX < 0) {
                    this.velX = -this.velX;
                    blockingBrick.hit();
                }
            }
        }
    }

    public void collisionBoss(Boss boss) {
        int hitBoxDepth = 3;
        if (boss.isAlive) {
            Rectangle hitBoxTop = new Rectangle(boss.getX() + 1, boss.getY() - 1, boss.getWidth() - 2, hitBoxDepth);
            Rectangle hitBoxBottom = new Rectangle(boss.getX() + 1, boss.getY() + boss.getHeight() - 1, boss.getWidth() - 2, hitBoxDepth);
            Rectangle hitBoxLeft = new Rectangle(boss.getX() - 1, boss.getY() + 1, hitBoxDepth, boss.getHeight() - 2);
            Rectangle hitBoxRight = new Rectangle(boss.getX() + boss.getWidth() - 1, boss.getY() + 1, hitBoxDepth, boss.getHeight() - 2);

            if (this.getHitBox().intersects(hitBoxTop) && this.velY > 0) {
                this.velY = -this.velY;
                boss.hit();
            } else if (this.getHitBox().intersects(hitBoxBottom) && this.velY < 0) {
                this.velY = -this.velY;
                boss.hit();
            } else if (this.getHitBox().intersects(hitBoxLeft) && this.velX > 0) {
                this.velX = -this.velX;
                boss.hit();
            } else if (this.getHitBox().intersects(hitBoxRight) && this.velX < 0) {
                this.velX = -this.velX;
                boss.hit();
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(BALL_TEXTURE, x, y, size, size, null);
    }

    public Rectangle getHitBox() {
        return new Rectangle(x, y, size, size);
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

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Image getBALL_TEXTURE() {
        return BALL_TEXTURE;
    }
    //endregion
}
