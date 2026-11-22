package com.example.grafos.interfaz;

import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;

public class PanelGraficas extends BorderPane {

    private BarChart<String, Number> grafica;

    public PanelGraficas() {
        CategoryAxis ejeX = new CategoryAxis();
        ejeX.setLabel("Algoritmos");

        NumberAxis ejeY = new NumberAxis();
        ejeY.setLabel("Tiempo en ms");

        grafica = new BarChart<>(ejeX, ejeY);
        grafica.setTitle("Comparacion de tiempos");
        grafica.setAnimated(false);

        setCenter(grafica);
    }

    // agrega los tiempos de una prueba
    public void mostrarTiempos(String prueba, String[] nombres, double[] tiempos) {
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(prueba);

        for(int i=0; i<nombres.length && i<tiempos.length; i++) {
            serie.getData().add(new XYChart.Data<>(nombres[i], tiempos[i]));
        }

        grafica.getData().add(serie);
    }

    public void limpiarGrafica() {
        grafica.getData().clear();
    }
}