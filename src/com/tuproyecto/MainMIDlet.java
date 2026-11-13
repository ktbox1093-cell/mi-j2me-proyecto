package com.tuproyecto;

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

public class MainMIDlet extends MIDlet implements CommandListener {
    private Display display;
    private GameCanvas gameCanvas;
    private Command exitCommand;

    public MainMIDlet() {
        display = Display.getDisplay(this);
        exitCommand = new Command("Salir", Command.EXIT, 1);
    }

    protected void startApp() {
        if (gameCanvas == null) {
            gameCanvas = new GameCanvas(this);
            gameCanvas.addCommand(exitCommand);
            gameCanvas.setCommandListener(this);
            gameCanvas.startThread();
        }
        display.setCurrent(gameCanvas);
    }

    protected void pauseApp() {
        if (gameCanvas != null) {
            gameCanvas.stopThread();
        }
    }

    protected void destroyApp(boolean unconditional) {
        if (gameCanvas != null) {
            gameCanvas.stopThread();
        }
        notifyDestroyed();
    }

    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand) {
            destroyApp(false);
        }
    }
}

