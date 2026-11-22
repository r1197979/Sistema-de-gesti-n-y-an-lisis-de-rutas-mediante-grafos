package com.example.grafos.modelo;

public class Arista {
    private final int origen;
    private final int destino;
    private double peso; // distancia, tiempo, costo

    /*
    Constructor que crea conexiones entre dos vértices
     */
    public Arista(int origen, int destino, double peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    /*
    Getters y setters
     */
    public int getOrigen() {
        return origen;
    }
    public int getDestino() {
        return destino;
    }
    public double getPeso() {
        return peso;
    }
    void setPeso(double peso) {
        this.peso = peso;
    }


    /*
    Representa en texto a una arista
     */
    @Override
    public String toString() {
        return "Arista{" +
                "origen=" + origen +
                ", destino=" + destino +
                ", peso=" + peso +
                '}';
    }

}
