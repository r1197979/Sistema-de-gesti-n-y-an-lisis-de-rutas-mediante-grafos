package com.example.grafos.algoritmos;

import java.util.ArrayList;

/*
 Dijkstra: Contiene origen, destino, secuencia de vértices y costo total
 */
public class ResultadoRuta {

    private int origen;
    private int destino;
    private ArrayList<Integer> ruta;
    private double costoTotal;
    private boolean existeRuta;

    public ResultadoRuta(int origen, int destino, ArrayList<Integer> ruta, double costoTotal){
        this.origen = origen;
        this.destino = destino;
        this.ruta = new ArrayList<>(ruta);
        this.costoTotal = costoTotal;
        this.existeRuta = true;
    }

    private ResultadoRuta(int origen, int destino){
        this.origen = origen;
        this.destino = destino;
        this.ruta = new ArrayList<>();
        this.existeRuta = false;
    }


    /*
    Crea un resultado que indica que no hay camino entre origen y destino.
    */
    public static ResultadoRuta sinRuta(int origen, int destino){
        return new ResultadoRuta(origen, destino);
    }

    /*
    Ruta formateada: "A a B a C".
    */
    public String getRutaTexto() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ruta.size(); i++) {
            if (i > 0) {
                sb.append(" a ");
            }
            sb.append(ruta.get(i));
        }
        return sb.toString();
    }

    public ArrayList<Integer> getRuta(){
        return new ArrayList<>(ruta);
    }

    public String toString(){
        if (!existeRuta){
            return "Origen: " + origen + "\nDestino: " + destino + "\nNo existe una ruta entre los vértices";
        }
        return "Origen: " + origen + "\nDestino " + destino + "\nRuta: " + getRutaTexto() + "\nCosto total: " + costoTotal;
    }


}
