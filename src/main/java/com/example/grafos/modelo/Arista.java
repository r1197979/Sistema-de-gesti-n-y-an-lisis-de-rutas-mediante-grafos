package com.example.grafos.modelo;

public class Arista {
    private final int origen;
    private final int destino;
    private double peso;

    public Arista(int origen, int destino, double peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

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

    @Override
    public String toString() {
        return origen + " -> " + destino + " (" + peso + ")";
    }
}
