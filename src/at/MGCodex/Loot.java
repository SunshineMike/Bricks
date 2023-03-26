package at.MGCodex;

import java.awt.*;

public class Loot {

    private Runnable command;
    private Image imgText;
    private String text;

    public Loot(Runnable command, Image imgText, String text) {
        this.command = command;
        this.imgText = imgText;
        this.text = text;
    }

    public void drop() {
        if (this.command != null) {
            this.command.run();
        }
    }

    //region Getter & Setter
    public Runnable getCommand() {
        return command;
    }

    public void setCommand(Runnable command) {
        this.command = command;
    }

    public Image getImgText() {
        return imgText;
    }

    public void setImgText(Image imgText) {
        this.imgText = imgText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    //endregion
}