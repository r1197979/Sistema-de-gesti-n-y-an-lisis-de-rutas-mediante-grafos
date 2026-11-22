package com.example.grafos.interfaz;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class VentanaPrincipal extends BorderPane {

    private PanelEdicion panelEdicion;
    private VistaGrafo vistaGrafo;
    private PanelResultados panelResultados;

    public VentanaPrincipal() {
        panelEdicion= new PanelEdicion();
        vistaGrafo = new VistaGrafo();
        panelResultados= new PanelResultados();

        Label titulo = new Label("Sistema de gestion de rutas");
        HBox encabezado =new HBox(titulo);
        encabezado.setPadding(new Insets(15));

        setTop(encabezado);
        setLeft(panelEdicion);
        setCenter(vistaGrafo);
        setRight(panelResultados);

        BorderPane.setMargin(panelEdicion, new Insets(10));
        BorderPane.setMargin(vistaGrafo,new Insets(10));
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
}