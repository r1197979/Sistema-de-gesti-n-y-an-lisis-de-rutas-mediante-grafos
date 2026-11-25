package com.example.grafos.mediciones;

public class ResultadoMedicion {
    private String algoritmo;
    private String descripcion;
    private int vertices;
    private int aristas;
    private int repeticiones;
    private double tiempoPromedioMs;
    private double tiempoMinimoMs;
    private double tiempoMaximoMs;

    public ResultadoMedicion(String algoritmo, String descripcion, int vertices, int aristas, int repeticiones, double tiempoPromedioMs, double tiempoMinimoMs, double tiempoMaximoMs) {
        this.algoritmo = algoritmo;
        this.descripcion = descripcion;
        this.vertices = vertices;
        this.aristas = aristas;
        this.repeticiones = repeticiones;
        this.tiempoPromedioMs = tiempoPromedioMs;
        this.tiempoMinimoMs = tiempoMinimoMs;
        this.tiempoMaximoMs = tiempoMaximoMs;
    }

    public String getAlgoritmo(){
        return algoritmo;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public int getVertices(){
        return vertices;
    }
    public int getAristas(){
        return aristas;
    }
    public int getRepeticiones(){
        return repeticiones;
    }
    public double getTiempoPromedioMs(){
        return tiempoPromedioMs;
    }
    public double getTiempoMinimoMs(){
        return tiempoMinimoMs;
    }
    public double getTiempoMaximoMs()
    { return tiempoMaximoMs;
    }

    @Override
    public String toString() {
        return String.format("%-12s | %-18s | V=%-5d | E=%-6d | prom=%.4f ms | min=%.4f ms | max=%.4f ms",
                algoritmo, descripcion, vertices, aristas,
                tiempoPromedioMs, tiempoMinimoMs, tiempoMaximoMs);
    }
}
