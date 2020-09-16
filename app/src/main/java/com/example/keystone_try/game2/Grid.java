package com.example.keystone_try.game2;

/**
 * leaned from Youtuber AtoTalKs
 */
import java.util.ArrayList;

public class Grid {

    public final Tile[][] field;
    public final Tile[][] undoField;
    private final Tile[][] bufferField;

    public Grid(int sizeX, int sizeY) {
        field = new Tile[sizeX][sizeY];
        undoField = new Tile[sizeX][sizeY];
        bufferField = new Tile[sizeX][sizeY];
        clearGrid();
        clearUndoGrid();
    }

    /**
     * get an random available cell
     * @return
     */
    public Cell randomAvailableCell() {
        ArrayList<Cell> availableCells = getAvailableCells();
        if (availableCells.size() >= 1) {
            return availableCells.get((int) Math.floor(Math.random() * availableCells.size()));
        }
        return null;
    }

    /**
     * get all available cell
     * @return
     */
    private ArrayList<Cell> getAvailableCells() {
        ArrayList<Cell> availableCells = new ArrayList<>();
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] == null) {
                    availableCells.add(new Cell(xx, yy));
                }
            }
        }
        return availableCells;
    }

    /**
     * Judge is there still have cell available
     * @return
     */
    public boolean isCellsAvailable() {
        return (getAvailableCells().size() >= 1);
    }

    /**
     * To judge a cell is available or not
     * @param cell the cell needs to be judged
     * @return
     */
    public boolean isCellAvailable(Cell cell) {
        return !isCellOccupied(cell);
    }

    /**
     * To judge a cell already has a number or not
     * @param cell
     * @return
     */
    public boolean isCellOccupied(Cell cell) {
        return (getCellContent(cell) != null);
    }

    /**
     * Get cell number
     * @param cell
     * @return
     */
    public Tile getCellContent(Cell cell) {
        if (cell != null && isCellWithinBounds(cell)) {
            return field[cell.getX()][cell.getY()];
        } else {
            return null;
        }
    }

    /**
     * get a position number
     * @param x
     * @param y
     * @return
     */
    public Tile getCellContent(int x, int y) {
        if (isCellWithinBounds(x, y)) {
            return field[x][y];
        } else {
            return null;
        }
    }

    /**
     * To judge a cell is in bound or not
     * @param cell
     * @return
     */
    public boolean isCellWithinBounds(Cell cell) {
        return 0 <= cell.getX() && cell.getX() < field.length
                && 0 <= cell.getY() && cell.getY() < field[0].length;
    }

    /**
     * To judge a position is in bound or not
     * @param x
     * @param y
     * @return
     */
    private boolean isCellWithinBounds(int x, int y) {
        return 0 <= x && x < field.length
                && 0 <= y && y < field[0].length;
    }

    /**
     * Put a number in the cell
     * @param tile
     */
    public void insertTile(Tile tile) {
        field[tile.getX()][tile.getY()] = tile;
    }

    /**
     * Delete a number from the cell
     * @param tile
     */
    public void removeTile(Tile tile) {
        field[tile.getX()][tile.getY()] = null;
    }

    /**
     * save all numbers
     */
    public void saveTiles() {
        for (int xx = 0; xx < bufferField.length; xx++) {
            for (int yy = 0; yy < bufferField[0].length; yy++) {
                if (bufferField[xx][yy] == null) {
                    undoField[xx][yy] = null;
                } else {
                    undoField[xx][yy] = new Tile(xx, yy, bufferField[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * get all number to be saved
     */
    public void prepareSaveTiles() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] == null) {
                    bufferField[xx][yy] = null;
                } else {
                    bufferField[xx][yy] = new Tile(xx, yy, field[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * get all saved number
     */
    public void revertTiles() {
        for (int xx = 0; xx < undoField.length; xx++) {
            for (int yy = 0; yy < undoField[0].length; yy++) {
                if (undoField[xx][yy] == null) {
                    field[xx][yy] = null;
                } else {
                    field[xx][yy] = new Tile(xx, yy, undoField[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * clear the grid (new game, first time in)
     */
    public void clearGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                field[xx][yy] = null;
            }
        }
    }

    /**
     * Delete last status(on undo pressed)
     */
    private void clearUndoGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                undoField[xx][yy] = null;
            }
        }
    }
}
