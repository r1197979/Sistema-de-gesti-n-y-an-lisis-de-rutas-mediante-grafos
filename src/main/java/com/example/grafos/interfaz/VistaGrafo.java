package com.example.grafos.interfaz;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

public class VistaGrafo extends Pane {

    private Pane capaAristas;
    private Pane capaVertices;

    private HashMap<Integer, Point2D> posiciones;
    private HashMap<Integer, Circle> circulos;
    private HashMap<String, Shape> lineas;
    private HashMap<String, Group> pesos;
    private List<Point2D> posicionesPesos;
    private Consumer<Integer> accionSeleccionVertice;
    private Integer verticeSeleccionado;


    public VistaGrafo() {
        setMinSize(420, 380);

        setBackground(new Background(new BackgroundFill(
                Color.rgb(239, 243, 240), CornerRadii.EMPTY, Insets.EMPTY)));

        posiciones = new HashMap<>();
        circulos= new HashMap<>();
        lineas = new HashMap<>();
        pesos = new HashMap<>();
        posicionesPesos = new ArrayList<>();

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

        // selecciona un nodo para destacar sus conexiones
        circulo.setOnMouseClicked(e -> seleccionarVertice(id));
        numero.setOnMouseClicked(e -> seleccionarVertice(id));

        circulos.put(id, circulo);
        capaVertices.getChildren().addAll(circulo, numero);
    }

    public void dibujarArista(int origen, int destino, double peso, boolean dirigido) {
        Point2D p1 = posiciones.get(origen);
        Point2D p2 = posiciones.get(destino);

        if(p1==null || p2==null || origen==destino) {
            return;
        }
        String clave = origen + "-" + destino;
        if(lineas.containsKey(clave)) {
            return;
        }

        double x1 = p1.getX();
        double y1= p1.getY();
        double x2=p2.getX();
        double y2 = p2.getY();

        double distancia = p1.distance(p2);
        if(distancia <= 36) {
            return;
        }

        double ux=(x2-x1)/distancia;
        double uy = (y2-y1)/distancia;
        double inicioX=x1+ux*18;
        double inicioY=y1+uy*18;
        double finX=x2-ux*18;
        double finY=y2-uy*18;

        // una curva suave ayuda a distinguir conexiones que se cruzan
        double curva= Math.min(27, Math.max(10, distancia*0.075));
        double controlX= (inicioX+finX)/2 - uy*curva;
        double controlY= (inicioY+finY)/2 + ux*curva;

        QuadCurve linea = new QuadCurve(inicioX, inicioY, controlX, controlY, finX, finY);
        linea.setFill(Color.TRANSPARENT);
        linea.setStroke(Color.rgb(148, 168, 156));
        linea.setStrokeWidth(2);
        capaAristas.getChildren().add(linea);

        lineas.put(clave, linea);
        if(!dirigido) {
            lineas.put(destino + "-" + origen, linea);
        }

        // busca una posicion donde el peso no tape otro peso o un nodo
        Text textoPeso= new Text(String.valueOf(peso));
        textoPeso.setFill(Color.rgb(28, 36, 34));
        double anchoTexto=textoPeso.getLayoutBounds().getWidth();
        double anchoEtiqueta=anchoTexto+10;
        double altoEtiqueta=19;
        Point2D lugar = buscarPosicionPeso(inicioX,inicioY,controlX,controlY,
                finX,finY, anchoEtiqueta, altoEtiqueta);

        Rectangle fondo = new Rectangle(anchoEtiqueta, altoEtiqueta);
        fondo.setArcWidth(7);
        fondo.setArcHeight(7);
        fondo.setFill(Color.rgb(239, 243, 240, 0.96));
        fondo.setStroke(Color.rgb(148, 168, 156));
        fondo.setStrokeWidth(0.7);

        textoPeso.setX(5);
        textoPeso.setY(13);
        Group etiqueta = new Group(fondo, textoPeso);
        etiqueta.setLayoutX(lugar.getX()-anchoEtiqueta/2);
        etiqueta.setLayoutY(lugar.getY()-altoEtiqueta/2);
        etiqueta.setMouseTransparent(true);
        capaAristas.getChildren().add(etiqueta);
        pesos.put(clave, etiqueta);
        if(!dirigido) {
            pesos.put(destino + "-" + origen, etiqueta);
        }

        if(dirigido) {
            // la flecha sigue la direccion final de la curva
            double angulo = Math.atan2(finY-controlY, finX-controlX);
            Polygon flecha = new Polygon(
                    finX, finY,
                    finX-10*Math.cos(angulo)-5*Math.sin(angulo),
                    finY-10*Math.sin(angulo)+5*Math.cos(angulo),
                    finX-10*Math.cos(angulo)+5*Math.sin(angulo),
                    finY-10*Math.sin(angulo)-5*Math.cos(angulo)
            );
            flecha.setFill(Color.rgb(148, 168, 156));
            capaAristas.getChildren().add(flecha);
        }
        etiqueta.toFront();
    }

    private Point2D buscarPosicionPeso(double x1, double y1, double cx, double cy,
                                       double x2, double y2, double ancho, double alto) {
        double[] lugares = {0.5, 0.35, 0.65, 0.25, 0.75, 0.15, 0.85};
        Point2D mejor = new Point2D((x1+x2)/2, (y1+y2)/2);
        double mejorPuntaje=Double.MAX_VALUE;

        double anchoPanel=getWidth()>0 ? getWidth() : 600;
        double altoPanel=getHeight()>0 ? getHeight() : 450;

        for(double t : lugares) {
            double f=1-t;
            double x=f*f*x1+2*f*t*cx+t*t*x2;
            double y=f*f*y1+2*f*t*cy+t*t*y2;

            double dx=2*f*(cx-x1)+2*t*(x2-cx);
            double dy=2*f*(cy-y1)+2*t*(y2-cy);
            double largo=Math.hypot(dx,dy);
            if(largo < 0.001) {
                largo=1;
            }

            for(int lado : new int[]{1,-1}) {
                for(int separacion : new int[]{13,25,38,52}) {
                    double px=x-dy/largo*separacion*lado;
                    double py=y+dx/largo*separacion*lado;
                    double puntaje=separacion*0.06+Math.abs(t-0.5)*8;

                    if(px-ancho/2<6 || px+ancho/2>anchoPanel-6 ||
                            py-alto/2<6 || py+alto/2>altoPanel-6) {
                        puntaje+=1000;
                    }

                    for(Point2D anterior : posicionesPesos) {
                        if(Math.abs(anterior.getX()-px)<(ancho+42)/2 &&
                                Math.abs(anterior.getY()-py)<alto+5) {
                            puntaje+=100;
                        }
                    }
                    for(Point2D nodo : posiciones.values()) {
                        if(Math.abs(nodo.getX()-px)<ancho/2+19 &&
                                Math.abs(nodo.getY()-py)<alto/2+19) {
                            puntaje+=150;
                        }
                    }
                    if(puntaje < mejorPuntaje) {
                        mejorPuntaje=puntaje;
                        mejor=new Point2D(px,py);
                    }
                }
            }
        }
        posicionesPesos.add(mejor);
        return mejor;
    }

    // los pesos siempre se muestran y la seleccion destaca los relacionados
    private void actualizarEtiquetas() {
        for(String clave : pesos.keySet()) {
            Group etiqueta=pesos.get(clave);
            etiqueta.setVisible(true);
            if(verticeSeleccionado==null) {
                etiqueta.setOpacity(1);
            } else {
                String id=String.valueOf(verticeSeleccionado);
                boolean relacionada=clave.startsWith(id+"-") || clave.endsWith("-"+id);
                etiqueta.setOpacity(relacionada ? 1 : 0.25);
            }
        }
    }

    public void setAccionSeleccionVertice(Consumer<Integer> accion) {
        accionSeleccionVertice=accion;
    }

    public void seleccionarVertice(int id) {
        if(verticeSeleccionado!=null && verticeSeleccionado==id) {
            verticeSeleccionado=null;
            resaltarCamino(null);
            actualizarEtiquetas();
            return;
        }

        resaltarCamino(null);
        verticeSeleccionado=id;

        for(Integer numero : circulos.keySet()) {
            circulos.get(numero).setOpacity(numero==id ? 1 : 0.55);
        }

        for(String clave : lineas.keySet()) {
            Shape linea=lineas.get(clave);
            String textoId=String.valueOf(id);

            if(clave.startsWith(textoId + "-") || clave.endsWith("-" + textoId)) {
                linea.setOpacity(1);
                linea.setStroke(Color.rgb(28, 36, 34));
                linea.setStrokeWidth(3);
            } else {
                linea.setOpacity(0.18);
            }
        }

        Circle elegido = circulos.get(id);
        if(elegido!=null) {
            elegido.setFill(Color.rgb(28, 36, 34));
        }
        actualizarEtiquetas();

        if(accionSeleccionVertice!=null) {
            accionSeleccionVertice.accept(id);
        }
    }

    // marca los vertices y las conexiones del camino
    public void resaltarCamino(List<Integer> camino) {
        verticeSeleccionado=null;
        for(Circle circulo : circulos.values()) {
            circulo.setOpacity(1);
            circulo.setFill(Color.rgb(97, 142, 124));
        }

        for(Shape linea : lineas.values()) {
            linea.setOpacity(1);
            linea.setStroke(Color.rgb(148, 168, 156));
            linea.setStrokeWidth(2);
        }

        if(camino==null) {
            actualizarEtiquetas();
            return;
        }

        for(Integer id : camino) {
            Circle circulo= circulos.get(id);

            if(circulo != null) {
                circulo.setFill(Color.rgb(28, 36, 34));
            }
        }

        for(int i=0; i<camino.size()-1; i++) {
            Shape linea = lineas.get(camino.get(i) + "-" + camino.get(i+1));

            if(linea!=null) {
                linea.setStroke(Color.rgb(28, 36, 34));
                linea.setStrokeWidth(4);
            }
        }
    }

    // resalta solo los vertices visitados sin marcar conexiones
    public void resaltarVertices(List<Integer> visitados) {
        resaltarCamino(null);

        if(visitados==null) {
            return;
        }

        for(Integer id : visitados) {
            Circle circulo = circulos.get(id);
            if(circulo!=null) {
                circulo.setFill(Color.rgb(28, 36, 34));
            }
        }
    }

    public void limpiarVista() {
        verticeSeleccionado=null;
        pesos.clear();
        posicionesPesos.clear();
        capaAristas.getChildren().clear();
        capaVertices.getChildren().clear();

        posiciones.clear();
        circulos.clear();
        lineas.clear();
    }
}
