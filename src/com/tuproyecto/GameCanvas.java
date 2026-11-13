package com.tuproyecto;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public class GameCanvas extends Canvas implements Runnable {
    private MainMIDlet midlet;
    private boolean running;
    private Thread gameThread;
    
    // Coordenadas del objeto controlado
    private int x = 120;
    private int y = 100;
    private int speed = 4;
    
    // Estados de las teclas de movimiento
    private boolean pressUp;
    private boolean pressDown;
    private boolean pressLeft;
    private boolean pressRight;

    public GameCanvas(MainMIDlet midlet) {
        this.midlet = midlet;
    }

    public void startThread() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void stopThread() {
        running = false;
        if (gameThread != null) {
            gameThread = null;
        }
    }

    public void run() {
        while (running) {
            long startTime = System.currentTimeMillis();
            
            updateGame();
            repaint();
            
            long elapsed = System.currentTimeMillis() - startTime;
            long sleepTime = 33 - elapsed; // Objetivo de ~30 FPS (1000ms / 30 = ~33ms)
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    // Ignorar la interrupción de hilo
                }
            }
        }
    }

    private void updateGame() {
        if (pressUp) y -= speed;
        if (pressDown) y += speed;
        if (pressLeft) x -= speed;
        if (pressRight) x += speed;
        
        // Límites de la pantalla
        int width = getWidth();
        int height = getHeight();
        
        if (x < 0) x = 0;
        if (x > width - 16) x = width - 16;
        if (y < 20) y = 20;
        if (y > height - 16) y = height - 16;
    }

    protected void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        
        // Fondo del escenario
        g.setColor(30, 144, 255); // Azul
        g.fillRect(0, 0, width, height);
        
        // Cabecera superior de información
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, width, 20);
        g.setColor(255, 255, 255);
        g.drawString("J2ME - X:" + x + " Y:" + y, 5, 2, Graphics.TOP | Graphics.LEFT);
        
        // Dibujar el objeto / entidad controlada
        g.setColor(50, 205, 50); // Verde lima
        g.fillRect(x, y, 16, 16);
        g.setColor(0, 0, 0);
        g.drawRect(x, y, 16, 16);
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        
        if (action == UP || keyCode == KEY_NUM2) pressUp = true;
        if (action == DOWN || keyCode == KEY_NUM8) pressDown = true;
        if (action == LEFT || keyCode == KEY_NUM4) pressLeft = true;
        if (action == RIGHT || keyCode == KEY_NUM6) pressRight = true;
    }

    protected void keyReleased(int keyCode) {
        int action = getGameAction(keyCode);
        
        if (action == UP || keyCode == KEY_NUM2) pressUp = false;
        if (action == DOWN || keyCode == KEY_NUM8) pressDown = false;
        if (action == LEFT || keyCode == KEY_NUM4) pressLeft = false;
        if (action == RIGHT || keyCode == KEY_NUM6) pressRight = false;
    }
}

