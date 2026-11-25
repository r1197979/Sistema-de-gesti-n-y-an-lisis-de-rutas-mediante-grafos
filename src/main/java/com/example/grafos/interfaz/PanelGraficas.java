package com.example.grafos.interfaz;

import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class PanelGraficas extends BorderPane {

    private BarChart<String, Number> grafica;
    private Label sinMediciones;

    public PanelGraficas() {
        CategoryAxis ejeX = new CategoryAxis();
        ejeX.setLabel("Algoritmos");

        NumberAxis ejeY = new NumberAxis();
        ejeY.setLabel("Tiempo en ms");

        grafica = new BarChart<>(ejeX, ejeY);
        grafica.setTitle("Comparacion de tiempos");
        grafica.setAnimated(false);
        grafica.setVisible(false);

        sinMediciones= new Label("Todavia no hay mediciones registradas");
        sinMediciones.setTextFill(Color.rgb(76, 96, 85));

        StackPane contenido = new StackPane(grafica, sinMediciones);

        setBackground(new Background(new BackgroundFill(
                Color.rgb(239, 243, 240), CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));

        setCenter(contenido);
    }

    // agrega los tiempos de una prueba
    public void mostrarTiempos(String prueba, String[] nombres, double[] tiempos) {
        if(nombres==null || tiempos==null) {
            return;
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(prueba);

        for(int i=0; i<nombres.length && i<tiempos.length; i++) {
            if (nombres[i] != null && Double.isFinite(tiempos[i]) && tiempos[i]>=0) {
                serie.getData().add(new XYChart.Data<>(nombres[i], tiempos[i]));
            }
        }

        if(serie.getData().isEmpty()) {
            return;
        }

        grafica.getData().add(serie);
        grafica.setVisible(true);
        sinMediciones.setVisible(false);
    }

    public void limpiarGrafica() {
        grafica.getData().clear();
        grafica.setVisible(false);
        sinMediciones.setVisible(true);
    }
}
