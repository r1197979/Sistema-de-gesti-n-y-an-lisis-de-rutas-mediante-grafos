package com.example.grafos.interfaz;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PanelResultados extends VBox {

    private TextArea areaResultados;
    private TextArea areaMatriz, areaLista;
    private TextArea areaOrdenamientos;

    private TabPane pestanas;

    public PanelResultados() {
        setSpacing(10);
        setPadding(new Insets(10));
        setPrefWidth(290);

        Label titulo=new Label("Resultados");

        areaResultados = new TextArea();
        areaResultados.setEditable(false);
        areaResultados.setWrapText(true);

        areaMatriz = new TextArea();
        areaMatriz.setEditable(false);
        areaMatriz.setWrapText(false);

        areaLista=new TextArea();
        areaLista.setEditable(false);

        areaOrdenamientos = new TextArea();
        areaOrdenamientos.setEditable(false);

        // cada tipo de resultado tendra su propia pestaña
        Tab recorridos = new Tab("Recorridos", areaResultados);
        Tab matriz = new Tab("Matriz", areaMatriz);
        Tab lista=new Tab("Lista", areaLista);
        Tab ordenamientos = new Tab("Orden", areaOrdenamientos);

        pestanas = new TabPane();
        pestanas.getTabs().addAll(recorridos, matriz, lista, ordenamientos);
        pestanas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        getChildren().addAll(titulo, pestanas);
        VBox.setVgrow(pestanas, Priority.ALWAYS);
    }

    public void mostrarResultado(String texto) {
        areaResultados.setText(texto);
        pestanas.getSelectionModel().select(0);
    }

    public void mostrarMatriz(String texto) {
        areaMatriz.setText(texto);
        pestanas.getSelectionModel().select(1);
    }

    public void mostrarLista(String texto) {
        areaLista.setText(texto);
        pestanas.getSelectionModel().select(2);
    }

    public void mostrarOrdenamiento(String texto) {
        areaOrdenamientos.setText(texto);
        pestanas.getSelectionModel().select(3);
    }

    public void limpiarResultados() {
        areaResultados.clear();
        areaMatriz.clear();
        areaLista.clear();
        areaOrdenamientos.clear();
    }
}