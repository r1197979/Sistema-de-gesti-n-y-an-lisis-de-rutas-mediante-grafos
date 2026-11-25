package com.example.grafos.interfaz;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class PanelResultados extends VBox {

    private TextArea areaResultados;
    private TextArea areaMatriz, areaLista;
    private TextArea areaOrdenamientos;

    private TableView<Integer> tablaVertices;
    private TableView<String[]> tablaAristas;

    private TabPane pestanas;

    public PanelResultados() {
        setSpacing(10);
        setPadding(new Insets(12));
        setPrefWidth(335);

        setBackground(new Background(new BackgroundFill(
                Color.rgb(224, 232, 227), CornerRadii.EMPTY, Insets.EMPTY)));

        Label titulo=new Label("Resultados");
        titulo.setTextFill(Color.rgb(28, 36, 34));

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

        tablaVertices = new TableView<>();
        tablaAristas=new TableView<>();

        TableColumn<Integer, String> columnaId = new TableColumn<>("Vertice");
        columnaId.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(String.valueOf(d.getValue())));

        tablaVertices.getColumns().add(columnaId);
        tablaVertices.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tablaAristas.getColumns().addAll(
                crearColumna("Origen", 0),
                crearColumna("Destino", 1),
                crearColumna("Peso", 2));

        tablaAristas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label tituloVertices = new Label("Vertices");
        tituloVertices.setTextFill(Color.rgb(28, 36, 34));

        Label tituloAristas= new Label("Aristas");
        tituloAristas.setTextFill(Color.rgb(28, 36, 34));

        VBox bloqueVertices = new VBox(5, tituloVertices, tablaVertices);
        VBox bloqueAristas = new VBox(5, tituloAristas, tablaAristas);

        VBox.setVgrow(tablaVertices, Priority.ALWAYS);
        VBox.setVgrow(tablaAristas, Priority.ALWAYS);

        SplitPane tablas = new SplitPane(bloqueVertices, bloqueAristas);
        tablas.setOrientation(javafx.geometry.Orientation.VERTICAL);
        tablas.setDividerPositions(0.45);

        // cada tipo de resultado tendra su propia pestaña
        Tab recorridos = new Tab("Recorridos", areaResultados);
        Tab matriz = new Tab("Matriz", areaMatriz);
        Tab lista=new Tab("Lista", areaLista);
        Tab ordenamientos = new Tab("Orden", areaOrdenamientos);
        Tab datos = new Tab("Datos", tablas);

        pestanas = new TabPane();
        pestanas.getTabs().addAll(recorridos, matriz, lista, ordenamientos, datos);
        pestanas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        getChildren().addAll(titulo, pestanas);
        VBox.setVgrow(pestanas, Priority.ALWAYS);
    }

    private TableColumn<String[], String> crearColumna(String titulo, int indice) {
        TableColumn<String[], String> columna=new TableColumn<>(titulo);

        columna.setCellValueFactory(d -> {
            String[] fila= d.getValue();

            if(fila == null || fila.length<=indice) {
                return new ReadOnlyStringWrapper("");
            }

            return new ReadOnlyStringWrapper(fila[indice]);
        });

        return columna;
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

    // muestra los datos completos aunque no se dibujen todos los vertices
    public void mostrarVertices(List<Integer> vertices) {
        tablaVertices.getItems().clear();

        if(vertices != null) {
            tablaVertices.getItems().addAll(vertices);
        }

        pestanas.getSelectionModel().select(4);
    }

    public void mostrarAristas(List<String[]> aristas) {
        tablaAristas.getItems().clear();

        if(aristas!=null) {
            tablaAristas.getItems().addAll(aristas);
        }
        pestanas.getSelectionModel().select(4);
    }

    public void limpiarResultados() {
        areaResultados.clear();
        areaMatriz.clear();
        areaLista.clear();
        areaOrdenamientos.clear();

        tablaVertices.getItems().clear();
        tablaAristas.getItems().clear();
    }
}
