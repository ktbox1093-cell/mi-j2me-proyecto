package com.tuproyecto;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.io.IOException;

public class GameCanvas extends Canvas implements Runnable {
    private MainMIDlet midlet;
    private boolean running;
    private Thread gameThread;
    
    // Coordenadas del personaje
    private int x = 120;
    private int y = 100;
    private int speed = 4;
    
    // Imagen del personaje (Sprite)
    private Image playerImg;
    private boolean imgLoaded = false;
    
    // Estados de las teclas
    private boolean pressUp, pressDown, pressLeft, pressRight;

    public GameCanvas(MainMIDlet midlet) {
        this.midlet = midlet;
        
        // Carga de recursos (Asegúrate de colocar tu imagen player.png en la carpeta res/)
        try {
            playerImg = Image.createImage("/player.png");
            imgLoaded = true;
        } catch (IOException e) {
            imgLoaded = false; // Si falla, usaremos el rectángulo de respaldo por seguridad
        }
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
        gameThread = null;
    }

    public void run() {
        while (running) {
            long startTime = System.currentTimeMillis();
            
            updateGame();
            repaint();
            
            long elapsed = System.currentTimeMillis() - startTime;
            long sleepTime = 33 - elapsed; // ~30 FPS
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {}
            }
        }
    }

    private void updateGame() {
        if (pressUp) y -= speed;
        if (pressDown) y += speed;
        if (pressLeft) x -= speed;
        if (pressRight) x += speed;
        
        // Límites de pantalla (Resolución típica S60: 320x240)
        int width = getWidth();
        int height = getHeight();
        
        if (x < 0) x = 0;
        if (x > width - 20) x = width - 20;
        if (y < 20) y = 20;
        if (y > height - 20) y = height - 20;
    }

    protected void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        
        // Fondo del escenario (Estilo pasto / mundo abierto)
        g.setColor(34, 139, 34); // Verde bosque
        g.fillRect(0, 0, width, height);
        
        // HUD Superior
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, width, 20);
        g.setColor(255, 255, 255);
        g.drawString("RPG J2ME - X:" + x + " Y:" + y, 5, 2, Graphics.TOP | Graphics.LEFT);
        
        // Renderizado del Jugador (Sprite o respaldo gráfico)
        if (imgLoaded && playerImg != null) {
            g.drawImage(playerImg, x, y, Graphics.TOP | Graphics.LEFT);
        } else {
            // Respaldo por si no encuentra la imagen
            g.setColor(255, 215, 0); // Dorado
            g.fillRect(x, y, 16, 16);
            g.setColor(0, 0, 0);
            g.drawRect(x, y, 16, 16);
        }
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
