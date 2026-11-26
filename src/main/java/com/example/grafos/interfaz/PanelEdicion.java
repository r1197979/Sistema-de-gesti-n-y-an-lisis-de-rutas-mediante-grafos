package com.example.grafos.interfaz;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

public class PanelEdicion extends VBox {

    private TextField campoVertice, campoNuevoId;
    private TextField campoOrigen, campoDestino, campoPeso;
    private TextField campoInicio, campoFinal;

    private Button botonNuevo, botonImportar, botonExportar;
    private Button botonVertice, botonModificarVertice, botonEliminarVertice;
    private Button botonArista, botonModificarArista, botonEliminarArista;
    private Button botonBfs, botonDfs, botonDijkstra, botonOrdenar;
    private Button botonPruebas;

    private CheckBox opcionDirigido;
    private ComboBox<String> metodoOrden, datoOrden;
    private Label mensaje;

    private Consumer<Boolean> accionNuevo;
    private Consumer<File> accionImportar, accionExportar;

    private Consumer<Integer> accionVertice, accionEliminarVertice;
    private BiConsumer<Integer,String> accionModificarVertice;

    private AccionArista accionArista, accionModificarArista;
    private BiConsumer<Integer,Integer> accionEliminarArista;

    private Consumer<Integer> accionBfs, accionDfs;
    private BiConsumer<Integer,Integer> accionDijkstra;
    private BiConsumer<String,String> accionOrdenar;
    private Runnable accionPruebas;

    // permite conectar el boton con la accion de agregar una arista
    public interface AccionArista {
        void agregar(int origen, int destino, double peso);
    }

    public PanelEdicion() {
        setSpacing(10);
        setPadding(new Insets(12));
        setPrefWidth(270);

        setBackground(new Background(new BackgroundFill(
                Color.rgb(48, 60, 56), CornerRadii.EMPTY, Insets.EMPTY)));

        Label titulo= new Label("Herramientas");
        titulo.setTextFill(Color.WHITE);

        opcionDirigido = new CheckBox("Grafo dirigido");
        opcionDirigido.setSelected(false);
        opcionDirigido.setTextFill(Color.WHITE);

        botonNuevo = new Button("Nuevo grafo");
        botonImportar = new Button("Importar CSV");
        botonExportar = new Button("Exportar CSV");

        botonNuevo.setDisable(true);
        botonImportar.setDisable(true);
        botonExportar.setDisable(true);

        botonNuevo.setOnAction(e -> {
            if(accionNuevo != null) {
                accionNuevo.accept(esDirigido());
            }
        });

        botonImportar.setOnAction(e -> seleccionarArchivo(false));
        botonExportar.setOnAction(e -> seleccionarArchivo(true));

        TitledPane archivos = crearGrupo("1. Grafo y archivos",
                opcionDirigido, botonNuevo,
                filaBotones(botonImportar, botonExportar));

        campoVertice = new TextField();
        campoVertice.setPromptText("Numero de vertice");

        campoNuevoId= new TextField();
        campoNuevoId.setPromptText("Nuevo nombre del vertice");

        // los botones se activaran cuando tengan una accion asignada
        botonVertice = new Button("Agregar vertice");
        botonModificarVertice = new Button("Cambiar nombre");
        botonEliminarVertice = new Button("Eliminar vertice");

        botonVertice.setDisable(true);
        botonModificarVertice.setDisable(true);
        botonEliminarVertice.setDisable(true);

        botonVertice.setOnAction(e -> agregarVertice());
        botonModificarVertice.setOnAction(e -> modificarVertice());
        botonEliminarVertice.setOnAction(e -> procesarVertice(accionEliminarVertice));

        TitledPane vertices = crearGrupo("2. Vertices",
                crearEtiqueta("ID del vertice"), campoVertice,
                crearEtiqueta("Nuevo nombre (solo al modificar)"), campoNuevoId,
                filaBotones(botonVertice, botonModificarVertice, botonEliminarVertice));

        campoOrigen=new TextField();
        campoOrigen.setPromptText("Origen");
        campoDestino = new TextField();
        campoDestino.setPromptText("Destino");

        campoPeso =new TextField();
        campoPeso.setPromptText("Peso de la conexion");

        botonArista = new Button("Agregar conexion");
        botonModificarArista = new Button("Modificar peso");
        botonEliminarArista = new Button("Eliminar conexion");

        botonArista.setDisable(true);
        botonModificarArista.setDisable(true);
        botonEliminarArista.setDisable(true);

        botonArista.setOnAction(e -> agregarArista());
        botonModificarArista.setOnAction(e -> procesarArista(accionModificarArista));
        botonEliminarArista.setOnAction(e -> eliminarArista());

        TitledPane conexiones = crearGrupo("3. Conexiones",
                crearEtiqueta("Vertice origen"), campoOrigen,
                crearEtiqueta("Vertice destino"), campoDestino,
                crearEtiqueta("Peso de la conexion"), campoPeso,
                filaBotones(botonArista, botonModificarArista, botonEliminarArista));

        campoInicio = new TextField();
        campoInicio.setPromptText("Vertice inicial");

        campoFinal=new TextField();
        campoFinal.setPromptText("Vertice destino para Dijkstra");

        botonBfs = new Button("BFS");
        botonDfs = new Button("DFS");
        botonDijkstra = new Button("Dijkstra");

        botonBfs.setDisable(true);
        botonDfs.setDisable(true);
        botonDijkstra.setDisable(true);

        botonBfs.setOnAction(e -> ejecutarRecorrido(accionBfs));
        botonDfs.setOnAction(e -> ejecutarRecorrido(accionDfs));
        botonDijkstra.setOnAction(e -> ejecutarDijkstra());

        TitledPane busquedas= crearGrupo("4. Recorridos y rutas",
                crearEtiqueta("Vertice inicial"), campoInicio,
                crearEtiqueta("Destino (solo Dijkstra)"), campoFinal,
                filaBotones(botonBfs, botonDfs, botonDijkstra));

        metodoOrden = new ComboBox<>();
        metodoOrden.getItems().addAll("QuickSort", "Shell Sort");
        metodoOrden.getSelectionModel().selectFirst();
        metodoOrden.setMaxWidth(Double.MAX_VALUE);

        datoOrden=new ComboBox<>();
        datoOrden.getItems().addAll("Vertices por nombre", "Conexiones por peso");
        datoOrden.getSelectionModel().selectFirst();
        datoOrden.setMaxWidth(Double.MAX_VALUE);

        botonOrdenar = new Button("Ordenar");
        botonOrdenar.setDisable(true);

        botonOrdenar.setOnAction(e -> {
            if (accionOrdenar!=null) {
                accionOrdenar.accept(metodoOrden.getValue(), datoOrden.getValue());
            }
        });

        botonPruebas = new Button("Pruebas 20, 100, 500, 1000");
        botonPruebas.setDisable(true);
        botonPruebas.setOnAction(e -> {
            if(accionPruebas!=null) {
                accionPruebas.run();
            }
        });

        TitledPane ordenamientos = crearGrupo("5. Ordenamientos",
                crearEtiqueta("Metodo"), metodoOrden,
                crearEtiqueta("Datos a ordenar"), datoOrden, botonOrdenar);

        TitledPane pruebas = crearGrupo("6. Pruebas de rendimiento", botonPruebas);

        mensaje= new Label();
        mensaje.setTextFill(Color.rgb(224, 235, 228));
        mensaje.setWrapText(true);

        Button botonLimpiar = new Button("Limpiar campos");
        botonLimpiar.setOnAction(e -> {
            limpiarCampos();
            mensaje.setText("");
        });

        Accordion secciones = new Accordion();
        secciones.getPanes().addAll(archivos, vertices, conexiones,
                busquedas, ordenamientos, pruebas);
        secciones.setExpandedPane(archivos);

        getChildren().addAll(titulo, secciones, botonLimpiar, mensaje);
    }

    private TitledPane crearGrupo(String titulo, Node... controles) {
        VBox grupo=new VBox(8);
        grupo.setPadding(new Insets(12));

        grupo.setBackground(new Background(new BackgroundFill(
                Color.rgb(69, 85, 78), new CornerRadii(5), Insets.EMPTY)));

        grupo.getChildren().addAll(controles);

        TitledPane seccion = new TitledPane(titulo, grupo);
        seccion.setAnimated(false);
        seccion.setMaxWidth(Double.MAX_VALUE);
        return seccion;
    }

    private Label crearEtiqueta(String texto) {
        Label etiqueta = new Label(texto);
        etiqueta.setTextFill(Color.WHITE);
        return etiqueta;
    }

    private FlowPane filaBotones(Button... botones) {
        FlowPane fila = new FlowPane(7, 7);
        fila.getChildren().addAll(botones);

        return fila;
    }

    private Integer leerEntero(TextField campo) {
        try {
            return Integer.parseInt(campo.getText().trim());

        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese un numero entero valido");
            return null;
        }
    }

    private Double leerPeso() {
        try {
            double peso= Double.parseDouble(campoPeso.getText().trim());

            if(!Double.isFinite(peso) || peso < 0) {
                mostrarMensaje("El peso debe ser un numero no negativo");
                return null;
            }

            return peso;
        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese un peso numerico valido");
            return null;
        }
    }

    private void agregarVertice() {
        try {
            // convierte el texto ingresado a un numero entero
            int id = Integer.parseInt(campoVertice.getText().trim());
            mensaje.setText("");

            if (accionVertice !=null) {
                accionVertice.accept(id);
            }

        } catch (NumberFormatException e) {
            mensaje.setText("Ingrese un numero entero valido");
        }
    }

    private void procesarVertice(Consumer<Integer> accion) {
        Integer id= leerEntero(campoVertice);

        if(id != null && accion!=null) {
            mensaje.setText("");
            accion.accept(id);
        }
    }

    private void modificarVertice() {
        Integer actual = leerEntero(campoVertice);
        if(actual==null) {
            return;
        }

        String nuevo= campoNuevoId.getText().trim();
        if(nuevo.isEmpty()) {
            mostrarMensaje("Ingrese un nuevo nombre");
            return;
        }

        if (accionModificarVertice!=null) {
            mensaje.setText("");
            accionModificarVertice.accept(actual, nuevo);
        }
    }

    private void agregarArista() {
        try {
            int origen= Integer.parseInt(campoOrigen.getText().trim());
            int destino = Integer.parseInt(campoDestino.getText().trim());
            double peso=Double.parseDouble(campoPeso.getText().trim());

            // no se permiten conexiones del vertice hacia si mismo
            if(origen == destino) {
                mensaje.setText("No se puede conectar un vertice consigo mismo");
                return;
            }

            // el peso no puede ser negativo ni infinito
            if (!Double.isFinite(peso) || peso<0) {
                mensaje.setText("El peso debe ser un numero no negativo");
                return;
            }

            mensaje.setText("");
            if (accionArista!=null) {
                accionArista.agregar(origen, destino, peso);
            }

        } catch (NumberFormatException e) {
            mensaje.setText("Revisa los valores de la conexion");
        }
    }

    private void procesarArista(AccionArista accion) {
        Integer origen = leerEntero(campoOrigen);
        if(origen==null) {
            return;
        }

        Integer destino= leerEntero(campoDestino);
        if (destino == null) {
            return;
        }

        if(origen.equals(destino)) {
            mostrarMensaje("No se puede conectar un vertice consigo mismo");
            return;
        }

        Double peso = leerPeso();

        if (peso!=null && accion != null) {
            mensaje.setText("");
            accion.agregar(origen, destino, peso);
        }
    }

    private void eliminarArista() {
        Integer origen= leerEntero(campoOrigen);
        if (origen==null) {
            return;
        }

        Integer destino = leerEntero(campoDestino);

        if(destino!=null && accionEliminarArista != null) {
            mensaje.setText("");
            accionEliminarArista.accept(origen, destino);
        }
    }

    private void ejecutarRecorrido(Consumer<Integer> accion) {
        Integer inicio = leerEntero(campoInicio);

        if(inicio !=null && accion != null) {
            mensaje.setText("");
            accion.accept(inicio);
        }
    }

    private void ejecutarDijkstra() {
        Integer inicio= leerEntero(campoInicio);
        if(inicio == null) {
            return;
        }

        Integer destino = leerEntero(campoFinal);

        if(destino!=null && accionDijkstra != null) {
            mensaje.setText("");
            accionDijkstra.accept(inicio, destino);
        }
    }

    private void seleccionarArchivo(boolean guardar) {
        FileChooser selector = new FileChooser();
        selector.setTitle(guardar ? "Guardar grafo CSV" : "Seleccionar archivo CSV");

        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));

        File archivo;

        if(guardar) {
            selector.setInitialFileName("grafo.csv");
            archivo=selector.showSaveDialog(getScene().getWindow());
        } else {
            archivo = selector.showOpenDialog(getScene().getWindow());
        }

        if (archivo==null) {
            return;
        }

        // el archivo se entrega a la clase encargada de procesarlo
        if(guardar && accionExportar != null) {
            accionExportar.accept(archivo);
        } else if(!guardar && accionImportar!=null) {
            accionImportar.accept(archivo);
        }
    }

    // devuelve el tipo de grafo seleccionado
    public boolean esDirigido() {
        return opcionDirigido.isSelected();
    }

    public void setDirigido(boolean dirigido) {
        opcionDirigido.setSelected(dirigido);
    }

    // estas acciones se conectaran con las clases de logica
    public void setAccionNuevo(Consumer<Boolean> accion) {
        accionNuevo= accion;
        botonNuevo.setDisable(accion == null);
    }

    public void setAccionImportar(Consumer<File> accion) {
        accionImportar =accion;
        botonImportar.setDisable(accion==null);
    }

    public void setAccionExportar(Consumer<File> accion) {
        accionExportar=accion;
        botonExportar.setDisable(accion == null);
    }

    public void setAccionVertice(Consumer<Integer> accion) {
        accionVertice = accion;
        botonVertice.setDisable(accion==null);
    }

    public void setAccionModificarVertice(BiConsumer<Integer,String> accion) {
        accionModificarVertice=accion;
        botonModificarVertice.setDisable(accion == null);
    }

    public void setAccionEliminarVertice(Consumer<Integer> accion) {
        accionEliminarVertice = accion;
        botonEliminarVertice.setDisable(accion==null);
    }

    public void setAccionArista(AccionArista accion) {
        accionArista=accion;
        botonArista.setDisable(accion == null);
    }

    public void setAccionModificarArista(AccionArista accion) {
        accionModificarArista=accion;
        botonModificarArista.setDisable(accion==null);
    }

    public void setAccionEliminarArista(BiConsumer<Integer,Integer> accion) {
        accionEliminarArista=accion;
        botonEliminarArista.setDisable(accion == null);
    }

    public void setAccionBfs(Consumer<Integer> accion) {
        accionBfs =accion;
        botonBfs.setDisable(accion == null);
    }

    public void setAccionDfs(Consumer<Integer> accion) {
        accionDfs= accion;
        botonDfs.setDisable(accion==null);
    }

    public void setAccionDijkstra(BiConsumer<Integer,Integer> accion) {
        accionDijkstra = accion;
        botonDijkstra.setDisable(accion ==null);
    }

    public void setAccionOrdenar(BiConsumer<String,String> accion) {
        accionOrdenar=accion;
        botonOrdenar.setDisable(accion == null);
    }

    public void setAccionPruebas(Runnable accion) {
        accionPruebas=accion;
        botonPruebas.setDisable(accion == null);
    }

    public void mostrarMensaje(String texto) {
        mensaje.setText(texto);
    }

    public void limpiarCampos() {
        campoVertice.clear();
        campoNuevoId.clear();

        campoOrigen.clear();
        campoDestino.clear();
        campoPeso.clear();

        campoInicio.clear();
        campoFinal.clear();
    }
}
