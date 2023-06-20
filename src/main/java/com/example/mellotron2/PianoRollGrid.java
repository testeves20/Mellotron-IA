package com.example.mellotron2;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PianoRollGrid extends GridPane {
    private static final int NUM_ROWS = 4;
    private static final int NUM_COLUMNS = 64;
    private static final double RECTANGLE_WIDTH = 6;
    private static final double RECTANGLE_HEIGHT = 20;

    public PianoRollGrid() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int column = 0; column < NUM_COLUMNS; column++) {
                Rectangle rectangle = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
                rectangle.setFill(Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                add(rectangle, column, row);
            }
        }
    }
}
