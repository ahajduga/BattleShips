package com.battleships.main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Adam on 2015-11-04.
 */
public class Field extends Rectangle{

    public int getxCell() {
        return xCell;
    }

    public int getyCell() {
        return yCell;
    }

    private int xCell, yCell;

    public Field(int size, Color color, int xCell, int yCell) {
        super(size,size);
        this.xCell = xCell;
        this.yCell = yCell;
    }
}
