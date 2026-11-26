package com.example.grafos.interfaz;

import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.ScrollPane;

public class PanelGraficas extends BorderPane {

    private BarChart<String, Number> grafica;
    private BarChart<String, Number> graficaAlgoritmos;
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

        CategoryAxis ejeX2 = new CategoryAxis();
        ejeX2.setLabel("Algoritmos");

        NumberAxis ejeY2 = new NumberAxis();
        ejeY2.setLabel("Tiempo en ms");

        graficaAlgoritmos = new BarChart<>(ejeX2, ejeY2);
        graficaAlgoritmos.setTitle("Comparacion sin importacion CSV");
        graficaAlgoritmos.setAnimated(false);
        graficaAlgoritmos.setLegendVisible(true);
        graficaAlgoritmos.setVisible(false);

        sinMediciones= new Label("Todavia no hay mediciones registradas");
        sinMediciones.setTextFill(Color.rgb(76, 96, 85));

        grafica.setMinHeight(390);
        graficaAlgoritmos.setMinHeight(390);

        VBox graficas = new VBox(12, grafica, graficaAlgoritmos);
        graficas.setPadding(new javafx.geometry.Insets(12));

        ScrollPane desplazamiento = new ScrollPane(graficas);
        desplazamiento.setFitToWidth(true);
        desplazamiento.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        StackPane contenido = new StackPane(desplazamiento, sinMediciones);

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

        XYChart.Series<String, Number> serieAlgoritmos = new XYChart.Series<>();
        serieAlgoritmos.setName(prueba);

        for(XYChart.Data<String, Number> dato : serie.getData()) {
            if(!dato.getXValue().equalsIgnoreCase("Importar CSV")) {
                serieAlgoritmos.getData().add(new XYChart.Data<>(dato.getXValue(), dato.getYValue()));
            }
        }

        if(!serieAlgoritmos.getData().isEmpty()) {
            graficaAlgoritmos.getData().add(serieAlgoritmos);
            graficaAlgoritmos.setVisible(true);
        }
        sinMediciones.setVisible(false);
    }

    public void limpiarGrafica() {
        grafica.getData().clear();
        graficaAlgoritmos.getData().clear();
        grafica.setVisible(false);
        graficaAlgoritmos.setVisible(false);
        sinMediciones.setVisible(true);
    }
}
