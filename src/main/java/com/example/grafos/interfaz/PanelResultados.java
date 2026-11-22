package com.example.grafos.interfaz;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class PanelResultados extends VBox {

    private TextArea areaResultados;

    public PanelResultados() {
        setSpacing(10);
        setPadding(new Insets(10));
        setPrefWidth(250);

        Label titulo=new Label("Resultados");

        areaResultados = new TextArea();
        areaResultados.setEditable(false);
        areaResultados.setWrapText(true);
        areaResultados.setPrefHeight(400);

        getChildren().addAll(titulo, areaResultados);
    }

    public void mostrarResultado(String texto) {
        areaResultados.setText(texto);
    }

    public void limpiarResultados() {
        areaResultados.clear();
    }
}