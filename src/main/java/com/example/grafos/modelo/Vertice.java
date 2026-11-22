package com.example.grafos.modelo;

public class Vertice {
    private int id;
    private String nombre;

    public Vertice(int id) {
        this(id, String.valueOf(id));
    }

    public Vertice(int id, String nombre) {
        this.id = id;
        this.nombre = normalizarNombre(nombre, id);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    void setId(int id) {
        this.id = id;
    }

    void setNombre(String nombre) {
        this.nombre = normalizarNombre(nombre, id);
    }

    static String normalizarNombre(String nombre, int id) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return String.valueOf(id);
        }
        return nombre.trim();
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Vertice)) {
            return false;
        }
        return id == ((Vertice) otro).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        if (nombre.equals(String.valueOf(id))) {
            return nombre;
        }
        return nombre + " (" + id + ")";
    }
}
