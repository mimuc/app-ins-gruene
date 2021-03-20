package de.lmu.treeapp.activities.minigames.slidePuzzle;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class MoveCalculator {
    
    private int dimensions;
    private List<Tile> tiles;
    private List<Integer> falseTiles = new ArrayList<>();
    static final int INVALID = -1;
    static final int LEFT = 0;
    static final int TOP = 1;
    static final int RIGHT = 2;
    static final int BOTTOM = 3;

    MoveCalculator(){
        tiles = new ArrayList<>();
    }

    private void reset() {
        tiles.clear();
        int position = 0;
        for (int i = 0; i< dimensions; i++){
            for (int j = 0; j < dimensions; j++){
                tiles.add(new Tile(position, i, j));
                position ++;
            }
        }
    }

    void setSquareRootNum(int squareRootNum){
        this.dimensions = squareRootNum;
        reset();
    }

    private void switchTiles(Tile tile, Tile newTile) {

        int position = tile.pos;
        int x = tile.x;
        int y = tile.y;

        tile.pos = newTile.pos;
        tile.x = newTile.x;
        tile.y = newTile.y;

        newTile.pos = position;
        newTile.x = x;
        newTile.y = y;
    }

    boolean swapValueWithInvisibleModel(int index){
        Tile formModel = tiles.get(index);
        Tile invisibleModel = tiles.get(0);
        switchTiles(formModel, invisibleModel);
        return isCompleted();
    }

    private boolean isCompleted(){
        int num = dimensions * dimensions;
        falseTiles.clear();
        for (int i = 0; i < num; i++){
            Tile tile = tiles.get(i);
            if(tile.pos != i){
                falseTiles.add(i);
            }
        }
        return falseTiles.size() <= 0;
    }

    public Tile getModel(int index){
        return tiles.get(index);
    }

    private int getIndexByCurrentPosition(int currentPosition){
        int num = dimensions * dimensions;
        for (int i = 0; i < num; i++) {
            if(tiles.get(i).pos == currentPosition)
                return i;
        }
        return -1;
    }

    public int findNeighborIndexOfInvisibleModel() {
        Tile emptyTile = tiles.get(0);
        int position = emptyTile.pos;
        int x = position % dimensions;
        int y = position / dimensions;
        int direction = new Random(System.nanoTime()).nextInt(4);
        switch (direction){
            case LEFT:
                if(x != 0)
                    return getIndexByCurrentPosition(position - 1);
            case TOP:
                if(y != 0)
                    return getIndexByCurrentPosition(position - dimensions);
            case RIGHT:
                if(x != dimensions - 1)
                    return getIndexByCurrentPosition(position + 1);
            case BOTTOM:
                if(y != dimensions - 1)
                    return getIndexByCurrentPosition(position + dimensions);
        }
        return findNeighborIndexOfInvisibleModel();
    }

    int getScrollDirection(int index){

        Tile tile = tiles.get(index);
        int position = tile.pos;

        int x = position % dimensions;
        int y = position / dimensions;
        int emptyTilePos = tiles.get(0).pos;

        if(x != 0 && emptyTilePos == position - 1)
            return LEFT;

        if(x != dimensions - 1 && emptyTilePos == position + 1)
            return RIGHT;

        if(y != 0 && emptyTilePos == position - dimensions)
            return TOP;

        if(y != dimensions - 1 && emptyTilePos == position + dimensions)
            return BOTTOM;

        return INVALID;
    }

    List<Integer> getFalseTiles(){
        int num = dimensions * dimensions;
        List<Integer> testtile = new ArrayList<>();
        falseTiles.clear();
        for (int i = 1; i < num; i++){
            Tile tile = tiles.get(i);
            if(tile.pos != i){
                falseTiles.add(tile.pos);
                testtile.add(i);
            }
        }
        Log.d("i list:", String.valueOf(testtile));
        return falseTiles;
    }
}

