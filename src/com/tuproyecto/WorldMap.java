package com.tuproyecto;

import javax.microedition.lcdui.Graphics;

public class WorldMap {
    public static final int TILE_SIZE = 32;
    
    // Matriz del mundo abierto (0 = Pasto transitable, 1 = Muro sólido)
    private int[][] mapData = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
        {1, 0, 1, 1, 1, 1, 0, 1, 0, 1},
        {1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
        {1, 0, 1, 1, 0, 0, 0, 1, 0, 1},
        {1, 0, 0, 1, 1, 1, 0, 1, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public int getCols() {
        return mapData[0].length;
    }

    public int getRows() {
        return mapData.length;
    }

    public int getWorldWidth() {
        return getCols() * TILE_SIZE;
    }

    public int getWorldHeight() {
        return getRows() * TILE_SIZE;
    }

    // Verificación de colisiones por píxel contra bloques sólidos
    public boolean isSolid(int pixelX, int pixelY) {
        int tileX = pixelX / TILE_SIZE;
        int tileY = pixelY / TILE_SIZE;
        
        if (tileX < 0 || tileX >= getCols() || tileY < 0 || tileY >= getRows()) {
            return true;
        }
        return mapData[tileY][tileX] == 1;
    }

    // Renderizado del mapa con desplazamiento de cámara
    public void draw(Graphics g, int cameraX, int cameraY, int screenWidth, int screenHeight) {
        int startCol = cameraX / TILE_SIZE;
        int endCol = (cameraX + screenWidth) / TILE_SIZE + 1;
        int startRow = cameraY / TILE_SIZE;
        int endRow = (cameraY + screenHeight) / TILE_SIZE + 1;

        if (endCol > getCols()) endCol = getCols();
        if (endRow > getRows()) endRow = getRows();

        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                int drawX = (col * TILE_SIZE) - cameraX;
                int drawY = (row * TILE_SIZE) - cameraY;

                if (mapData[row][col] == 1) {
                    // Dibujar Muro / Obstáculo
                    g.setColor(105, 105, 105);
                    g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                    g.setColor(60, 60, 60);
                    g.drawRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                } else {
                    // Dibujar Terreno / Pasto
                    g.setColor(50, 205, 50);
                    g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                    g.setColor(34, 139, 34);
                    g.drawRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }
}
