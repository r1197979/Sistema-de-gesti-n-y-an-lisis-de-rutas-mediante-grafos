package com.example.grafos.interfaz;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class VistaGrafo extends Pane {

    public VistaGrafo() {
        setMinSize(450, 400);

        setBackground(new Background(new BackgroundFill(Color.rgb(240, 243, 247),
                CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void limpiarVista() {
        getChildren().clear();
    }
}