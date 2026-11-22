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

public class VistaGrafo extends Pane {

    private Pane capaAristas;
    private Pane capaVertices;

    private HashMap<Integer, Point2D> posiciones;

    public VistaGrafo() {
        setMinSize(450, 400);

        setBackground(new Background(new BackgroundFill(Color.rgb(240, 243, 247),
                CornerRadii.EMPTY, Insets.EMPTY)));

        posiciones = new HashMap<>();

        // las aristas van detras de los vertices
        capaAristas = new Pane();
        capaVertices = new Pane();

        capaAristas.setMouseTransparent(true);
        getChildren().addAll(capaAristas, capaVertices);
    }

    public void dibujarVertice(int id, double x, double y) {

        // solamente mostramos hasta 50 vertices
        if(posiciones.containsKey(id) || posiciones.size()>=50) {
            return;
        }

        posiciones.put(id, new Point2D(x, y));

        Circle circulo = new Circle(x, y, 18);
        circulo.setFill(Color.STEELBLUE);

        Text numero = new Text(String.valueOf(id));
        numero.setFill(Color.WHITE);

        double ancho = numero.getLayoutBounds().getWidth();

        numero.setX(x - ancho / 2);
        numero.setY(y + 5);

        capaVertices.getChildren().addAll(circulo, numero);
    }

    public void dibujarArista(int origen, int destino, double peso, boolean dirigido) {
        Point2D p1 = posiciones.get(origen);
        Point2D p2 = posiciones.get(destino);

        if (p1==null || p2==null || origen==destino) {
            return;
        }

        double x1= p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2= p2.getY();

        Line linea = new Line(x1, y1, x2, y2);
        linea.setStroke(Color.GRAY);
        linea.setStrokeWidth(2);

        capaAristas.getChildren().add(linea);

        Text textoPeso = new Text(String.valueOf(peso));

        double medioX = (x1+x2)/2;
        double medioY = (y1+y2)/2;

        textoPeso.setX(medioX);
        textoPeso.setY(medioY - 7);
        capaAristas.getChildren().add(textoPeso);

        if(dirigido) {
            // coloca la flecha cerca del vertice de destino
            double angulo = Math.atan2(y2-y1, x2-x1);

            double puntaX = x2 - 18 * Math.cos(angulo);
            double puntaY= y2 - 18 * Math.sin(angulo);

            Polygon flecha = new Polygon(
                    puntaX, puntaY,
                    puntaX - 10*Math.cos(angulo) - 5*Math.sin(angulo),
                    puntaY - 10*Math.sin(angulo) + 5*Math.cos(angulo),
                    puntaX - 10*Math.cos(angulo) + 5*Math.sin(angulo),
                    puntaY - 10*Math.sin(angulo) - 5*Math.cos(angulo)
            );

            flecha.setFill(Color.GRAY);
            capaAristas.getChildren().add(flecha);
        }
    }

    public void limpiarVista() {
        capaAristas.getChildren().clear();
        capaVertices.getChildren().clear();

        posiciones.clear();
    }
}