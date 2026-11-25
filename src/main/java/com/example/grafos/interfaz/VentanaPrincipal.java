package com.example.grafos.interfaz;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class VentanaPrincipal extends BorderPane {

    private PanelEdicion panelEdicion;
    private VistaGrafo vistaGrafo;
    private PanelResultados panelResultados;
    private PanelGraficas panelGraficas;

    private Label estado, contador;

    public VentanaPrincipal() {
        panelEdicion= new PanelEdicion();
        vistaGrafo = new VistaGrafo();
        panelResultados= new PanelResultados();
        panelGraficas = new PanelGraficas();

        setBackground(new Background(new BackgroundFill(
                Color.rgb(224, 232, 227), CornerRadii.EMPTY, Insets.EMPTY)));

        Label titulo = new Label("Sistema de gestion y analisis de rutas");
        titulo.setTextFill(Color.WHITE);
        titulo.setFont(Font.font("System", FontWeight.BOLD, 18));

        contador = new Label("Sin grafo cargado");
        contador.setTextFill(Color.rgb(205, 216, 209));

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        HBox encabezado =new HBox(15, titulo, espacio, contador);
        encabezado.setPadding(new Insets(15));
        encabezado.setBackground(new Background(new BackgroundFill(
                Color.rgb(28, 36, 34), CornerRadii.EMPTY, Insets.EMPTY)));

        // permite desplazarnos cuando el panel tenga mas controles
        ScrollPane scrollEdicion = new ScrollPane(panelEdicion);
        scrollEdicion.setFitToWidth(true);
        scrollEdicion.setPrefWidth(290);
        scrollEdicion.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Tab tabGrafo = new Tab("Grafo", vistaGrafo);
        Tab tabGraficas = new Tab("Graficas", panelGraficas);

        TabPane pestanas = new TabPane();
        pestanas.getTabs().addAll(tabGrafo, tabGraficas);
        pestanas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        estado = new Label("Sin operaciones realizadas");
        estado.setTextFill(Color.rgb(205, 216, 209));

        HBox barraEstado = new HBox(estado);
        barraEstado.setPadding(new Insets(10, 15, 10, 15));
        barraEstado.setBackground(new Background(new BackgroundFill(
                Color.rgb(28, 36, 34), CornerRadii.EMPTY, Insets.EMPTY)));

        setTop(encabezado);
        setLeft(scrollEdicion);
        setCenter(pestanas);
        setRight(panelResultados);
        setBottom(barraEstado);

        BorderPane.setMargin(scrollEdicion, new Insets(10));
        BorderPane.setMargin(pestanas,new Insets(10));
        BorderPane.setMargin(panelResultados, new Insets(10));
    }

    public void actualizarEstado(String texto) {
        estado.setText(texto);
    }

    public void actualizarConteo(int vertices, int aristas) {
        contador.setText("Vertices: " + vertices + " | Aristas: " + aristas);
    }

    public PanelEdicion getPanelEdicion() {
        return panelEdicion;
    }

    public VistaGrafo getVistaGrafo() {
        return vistaGrafo;
    }

    public PanelResultados getPanelResultados() {
        return panelResultados;
    }

    public PanelGraficas getPanelGraficas() {
        return panelGraficas;
    }
}
