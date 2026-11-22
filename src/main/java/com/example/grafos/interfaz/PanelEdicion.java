package com.example.grafos.interfaz;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.function.Consumer;

public class PanelEdicion extends VBox {

    private TextField campoVertice;
    private TextField campoOrigen, campoDestino, campoPeso;

    private Button botonVertice, botonArista;
    private Label mensaje;
    private CheckBox opcionDirigido;

    private Consumer<Integer> accionVertice;
    private AccionArista accionArista;

    // permite conectar el boton con la accion de agregar una arista
    public interface AccionArista {
        void agregar(int origen, int destino, double peso);
    }

    public PanelEdicion() {
        setSpacing(10);
        setPadding(new Insets(10));
        setPrefWidth(230);

        Label titulo= new Label("Edicion del grafo");

        opcionDirigido = new CheckBox("Grafo dirigido");
        opcionDirigido.setSelected(false);

        campoVertice = new TextField();
        campoVertice.setPromptText("Numero de vertice");

        // los botones se activaran cuando tengan una accion asignada
        botonVertice = new Button("Agregar vertice");
        botonVertice.setDisable(true);
        botonVertice.setOnAction(e -> agregarVertice());

        campoOrigen=new TextField();
        campoOrigen.setPromptText("Origen");
        campoDestino = new TextField();
        campoDestino.setPromptText("Destino");

        campoPeso =new TextField();
        campoPeso.setPromptText("Peso de la conexion");

        botonArista = new Button("Agregar conexion");
        botonArista.setDisable(true);
        botonArista.setOnAction(e -> agregarArista());

        mensaje= new Label();
        mensaje.setWrapText(true);

        Button botonLimpiar = new Button("Limpiar campos");
        botonLimpiar.setOnAction(e -> {
            limpiarCampos();
            mensaje.setText("");
        });

        getChildren().addAll(titulo, new Separator(),
                new Label("Tipo de grafo"), opcionDirigido,
                new Separator(), new Label("Vertices"),
                campoVertice, botonVertice,
                new Separator(), new Label("Conexiones"),
                campoOrigen, campoDestino, campoPeso,
                botonArista, botonLimpiar, mensaje);
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

    // devuelve el tipo de grafo seleccionado
    public boolean esDirigido() {
        return opcionDirigido.isSelected();
    }

    public void setAccionVertice(Consumer<Integer> accion) {
        accionVertice = accion;
        botonVertice.setDisable(accion==null);
    }

    public void setAccionArista(AccionArista accion) {
        accionArista=accion;
        botonArista.setDisable(accion == null);
    }

    public void mostrarMensaje(String texto) {
        mensaje.setText(texto);
    }

    public void limpiarCampos() {
        campoVertice.clear();
        campoOrigen.clear();
        campoDestino.clear();
        campoPeso.clear();
    }
}