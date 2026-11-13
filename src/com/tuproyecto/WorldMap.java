package com.tuproyecto;

import javax.microedition.lcdui.Graphics;

public class WorldMap {
    // Tamaño de cada bloque (tile) en píxeles
    public static final int TILE_SIZE = 32;
    
    // Matriz del mapa de ejemplo (0 = Suelo, 1 = Muro/Obstáculo)
    private int[][] mapData = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
        {1, 0, 1, 1, 1, 1, 0, 1, 0, 1},
        {1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public int getCols() {
        return mapData[0].length;
    }

    public int getRows() {
        return mapData.length;
    }

    // Método para verificar colisiones antes de mover al jugador
    public boolean isSolid(int pixelX, int pixelY) {
        int tileX = pixelX / TILE_SIZE;
        int tileY = pixelY / TILE_SIZE;
        
        if (tileX < 0 || tileX >= getCols() || tileY < 0 || tileY >= getRows()) {
            return true; // Fuera del mapa es sólido
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
                    // Dibujar Muro
                    g.setColor(105, 105, 105); // Gris piedra
                    g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                    g.setColor(60, 60, 60);
                    g.drawRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                } else {
                    // Dibujar Suelo
                    g.setColor(50, 205, 50); // Pasto brillante
                    g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                    g.setColor(34, 139, 34);
                    g.drawRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }
}

