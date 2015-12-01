package com.battleships.main;

import com.battleships.utils.Coords;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Adam on 2015-11-04.
 */
public class Field extends Rectangle{



    private Coords coords;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        setFill(color);
        this.color = color;
    }

    private Color color;
    public Coords getCoords() {
        return coords;
    }

    public Field(int size, Color color, Coords coords) {
        super(size,size);
        super.setFill(color);
        this.coords = coords;
        this.color = color;
    }
}
