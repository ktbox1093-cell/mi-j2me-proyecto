package com.tuproyecto;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public class GameCanvas extends Canvas implements Runnable {
    private MainMIDlet midlet;
    private boolean running;
    private Thread gameThread;
    
    private WorldMap worldMap;
    
    // Posición del jugador en coordenadas del mundo
    private int playerX = 64;
    private int playerY = 64;
    private int playerSize = 16;
    private int speed = 3;
    
    // Estados de las teclas
    private boolean pressUp, pressDown, pressLeft, pressRight;

    public GameCanvas(MainMIDlet midlet) {
        this.midlet = midlet;
        this.worldMap = new WorldMap();
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
        int nextX = playerX;
        int nextY = playerY;

        if (pressUp) nextY -= speed;
        if (pressDown) nextY += speed;
        if (pressLeft) nextX -= speed;
        if (pressRight) nextX += speed;

        // Comprobación de colisiones en las 4 esquinas del cuadrado del jugador
        boolean collision = worldMap.isSolid(nextX, nextY) ||
                            worldMap.isSolid(nextX + playerSize - 1, nextY) ||
                            worldMap.isSolid(nextX, nextY + playerSize - 1) ||
                            worldMap.isSolid(nextX + playerSize - 1, nextY + playerSize - 1);

        if (!collision) {
            playerX = nextX;
            playerY = nextY;
        }
    }

    protected void paint(Graphics g) {
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        
        // Calcular la cámara para que siga al jugador centrado en pantalla
        int cameraX = playerX - (screenWidth / 2) + (playerSize / 2);
        int cameraY = playerY - (screenHeight / 2) + (playerSize / 2);

        // Limites de la cámara para que no salga del mapa
        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > worldMap.getWorldWidth() - screenWidth) {
            cameraX = worldMap.getWorldWidth() - screenWidth;
        }
        if (cameraY > worldMap.getWorldHeight() - screenHeight) {
            cameraY = worldMap.getWorldHeight() - screenHeight;
        }

        // 1. Dibujar el mundo y los obstáculos con la cámara aplicada
        worldMap.draw(g, cameraX, cameraY, screenWidth, screenHeight);

        // 2. Dibujar al jugador ajustado a la posición de la cámara
        int drawPlayerX = playerX - cameraX;
        int drawPlayerY = playerY - cameraY;

        g.setColor(255, 215, 0); // Cuadrado amarillo del personaje
        g.fillRect(drawPlayerX, drawPlayerY, playerSize, playerSize);
        g.setColor(0, 0, 0);
        g.drawRect(drawPlayerX, drawPlayerY, playerSize, playerSize);

        // 3. HUD Superior FIJO (no se desplaza con la cámara)
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, screenWidth, 18);
        g.setColor(255, 255, 255);
        g.drawString("RPG | X:" + playerX + " Y:" + playerY, 4, 1, Graphics.TOP | Graphics.LEFT);
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
