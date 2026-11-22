package com.example.grafos.interfaz;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class VentanaPrincipal extends BorderPane {

    private PanelEdicion panelEdicion;
    private VistaGrafo vistaGrafo;
    private PanelResultados panelResultados;
    private PanelGraficas panelGraficas;

    public VentanaPrincipal() {
        panelEdicion= new PanelEdicion();
        vistaGrafo = new VistaGrafo();
        panelResultados= new PanelResultados();
        panelGraficas = new PanelGraficas();

        Label titulo = new Label("Sistema de gestion de rutas");
        HBox encabezado =new HBox(titulo);
        encabezado.setPadding(new Insets(15));

        // separamos el grafo y las graficas en dos pestañas
        Tab tabGrafo = new Tab("Grafo", vistaGrafo);
        Tab tabGraficas = new Tab("Graficas", panelGraficas);

        TabPane pestanas = new TabPane();
        pestanas.getTabs().addAll(tabGrafo, tabGraficas);
        pestanas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        setTop(encabezado);
        setLeft(panelEdicion);
        setCenter(pestanas);
        setRight(panelResultados);

        BorderPane.setMargin(panelEdicion, new Insets(10));
        BorderPane.setMargin(pestanas,new Insets(10));
        BorderPane.setMargin(panelResultados, new Insets(10));
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