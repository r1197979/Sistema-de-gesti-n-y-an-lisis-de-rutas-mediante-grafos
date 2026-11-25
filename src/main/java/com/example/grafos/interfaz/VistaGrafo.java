package com.example.grafos.interfaz;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;

public class VistaGrafo extends Pane {

    private Pane capaAristas;
    private Pane capaVertices;

    private HashMap<Integer, Point2D> posiciones;
    private HashMap<Integer, Circle> circulos;
    private HashMap<String, Line> lineas;

    public VistaGrafo() {
        setMinSize(420, 380);

        setBackground(new Background(new BackgroundFill(
                Color.rgb(239, 243, 240), CornerRadii.EMPTY, Insets.EMPTY)));

        posiciones = new HashMap<>();
        circulos= new HashMap<>();
        lineas = new HashMap<>();

        // las aristas van detras de los vertices
        capaAristas = new Pane();
        capaVertices = new Pane();

        capaAristas.setMouseTransparent(true);
        getChildren().addAll(capaAristas, capaVertices);
    }

    public void dibujarVertices(List<Integer> vertices) {
        limpiarVista();

        if(vertices == null || vertices.isEmpty()) {
            return;
        }

        int cantidad = Math.min(50, vertices.size());

        double ancho = getWidth()>0 ? getWidth() : 600;
        double alto= getHeight()>0 ? getHeight() : 450;

        // para pocos vertices usamos una distribucion circular
        if(cantidad <= 12) {
            double radioX = ancho/2 - 55;
            double radioY = alto/2 - 55;

            for(int i=0; i<cantidad; i++) {
                double angulo = 2*Math.PI*i/cantidad - Math.PI/2;

                double x= ancho/2 + radioX*Math.cos(angulo);
                double y = alto/2 + radioY*Math.sin(angulo);

                dibujarVertice(vertices.get(i), x, y);
            }
        } else {
            // con mas vertices usamos filas y columnas
            int columnas = (int)Math.ceil(Math.sqrt(cantidad * ancho/alto));
            int filas= (int)Math.ceil((double)cantidad/columnas);

            for(int i=0; i<cantidad; i++) {
                int columna = i%columnas;
                int fila=i/columnas;

                double x = (columna+1)*ancho/(columnas+1);
                double y= (fila+1)*alto/(filas+1);

                dibujarVertice(vertices.get(i), x, y);
            }
        }
    }

    public void dibujarVertice(int id, double x, double y) {

        // solamente mostramos hasta 50 vertices
        if(posiciones.containsKey(id) || posiciones.size()>=50) {
            return;
        }

        posiciones.put(id, new Point2D(x, y));

        Circle circulo = new Circle(x, y, 18);
        circulo.setFill(Color.rgb(97, 142, 124));
        circulo.setStroke(Color.WHITE);
        circulo.setStrokeWidth(1.5);

        Text numero = new Text(String.valueOf(id));
        numero.setFill(Color.WHITE);

        double ancho = numero.getLayoutBounds().getWidth();

        numero.setX(x - ancho / 2);
        numero.setY(y + 5);

        circulos.put(id, circulo);
        capaVertices.getChildren().addAll(circulo, numero);
    }

    public void dibujarArista(int origen, int destino, double peso, boolean dirigido) {
        Point2D p1 = posiciones.get(origen);
        Point2D p2 = posiciones.get(destino);

        if (p1==null || p2==null || origen==destino) {
            return;
        }

        if(lineas.containsKey(origen + "-" + destino)) {
            return;
        }

        double x1= p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2= p2.getY();

        double angulo=Math.atan2(y2-y1, x2-x1);

        double inicioX = x1 + 18*Math.cos(angulo);
        double inicioY= y1 + 18*Math.sin(angulo);
        double finX = x2 - 18*Math.cos(angulo);
        double finY = y2 - 18*Math.sin(angulo);

        Line linea = new Line(inicioX, inicioY, finX, finY);
        linea.setStroke(Color.rgb(148, 168, 156));
        linea.setStrokeWidth(2);

        capaAristas.getChildren().add(linea);
        lineas.put(origen + "-" + destino, linea);

        if(!dirigido) {
            lineas.put(destino + "-" + origen, linea);
        }

        Text textoPeso = new Text(String.valueOf(peso));
        textoPeso.setFill(Color.rgb(28, 36, 34));

        double medioX = (inicioX+finX)/2;
        double medioY = (inicioY+finY)/2;

        textoPeso.setX(medioX - textoPeso.getLayoutBounds().getWidth()/2);
        textoPeso.setY(medioY - 7);
        capaAristas.getChildren().add(textoPeso);

        if(dirigido) {
            // coloca la flecha cerca del vertice de destino
            Polygon flecha = new Polygon(
                    finX, finY,
                    finX - 10*Math.cos(angulo) - 5*Math.sin(angulo),
                    finY - 10*Math.sin(angulo) + 5*Math.cos(angulo),
                    finX - 10*Math.cos(angulo) + 5*Math.sin(angulo),
                    finY - 10*Math.sin(angulo) - 5*Math.cos(angulo)
            );

            flecha.setFill(Color.rgb(148, 168, 156));
            capaAristas.getChildren().add(flecha);
        }
    }

    // marca los vertices y las conexiones del camino
    public void resaltarCamino(List<Integer> camino) {
        for(Circle circulo : circulos.values()) {
            circulo.setFill(Color.rgb(97, 142, 124));
        }

        for(Line linea : lineas.values()) {
            linea.setStroke(Color.rgb(148, 168, 156));
            linea.setStrokeWidth(2);
        }

        if(camino==null) {
            return;
        }

        for(Integer id : camino) {
            Circle circulo= circulos.get(id);

            if(circulo != null) {
                circulo.setFill(Color.rgb(28, 36, 34));
            }
        }

        for(int i=0; i<camino.size()-1; i++) {
            Line linea = lineas.get(camino.get(i) + "-" + camino.get(i+1));

            if(linea!=null) {
                linea.setStroke(Color.rgb(28, 36, 34));
                linea.setStrokeWidth(4);
            }
        }
    }

    public void limpiarVista() {
        capaAristas.getChildren().clear();
        capaVertices.getChildren().clear();

        posiciones.clear();
        circulos.clear();
        lineas.clear();
    }
}
