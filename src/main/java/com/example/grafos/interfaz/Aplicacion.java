package com.example.grafos.interfaz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Aplicacion extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane raiz = new BorderPane();
        raiz.setCenter(new Label("Sistema de gestión y análisis de rutas mediante grafos"));
        stage.setTitle("Sistema de grafos");
        stage.setScene(new Scene(raiz, 1000, 700));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
