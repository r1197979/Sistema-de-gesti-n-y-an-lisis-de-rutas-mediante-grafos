package com.example.grafos.interfaz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Aplicacion extends Application {

    @Override
    public void start(Stage stage) {
        VentanaPrincipal ventana = new VentanaPrincipal();

        Scene escena = new Scene(ventana, 1320, 790);

        stage.setTitle("Sistema de grafos");
        stage.setScene(escena);
        stage.setMinWidth(1150);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
