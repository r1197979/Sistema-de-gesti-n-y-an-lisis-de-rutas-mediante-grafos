package com.example.grafos.modelo;

import java.util.Objects;

public class Vertice {
    private int id;
    private String nombre;

    /*
    Constructor que solo recibe valor, su nombre será el mismo número
     */
    public Vertice(int id) {
        this.id = id;
        nombre = String.valueOf(id);
    }

    /*
    Constructor que crea vértice con valor y nombre
     */
    public Vertice(int id, String nombre) {
        this.id = id;
        this.nombre = validarNombre(nombre, id);
    }

    /*
    Setters y getters
     */
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
        this.nombre = validarNombre(nombre, id);
    }

    /*
    Permite evaluar nombre de vértice para evitar nodos duplicados o vacíos
    trim() elimina espacios al inicio y final del texto
     */
    static String validarNombre(String nombre, int id) {
        if(nombre==null || nombre.trim().isEmpty()) { // vértice sin nombre, se usa el id
            return String.valueOf(id);
        }
        return nombre.trim(); // eliminar espacios
    }

    /*
    Compara el id de dos vértices, son iguales si comparten el mismo aunque tengan distinto nombre
     */
    @Override
    public boolean equals(Object otro) {
        if (this==otro) {return true;} // vértices iguales
        if (!(otro instanceof Vertice)) {return false;} // no es un vértice
        return id == ((Vertice) otro).id; // compara id's
    }

    /*
    Genera código a partir del id del vértice para usarse en HashMap y Set
     */
    @Override
    public int hashCode() {
        return id;
    }

    /*
    Representa en texto a un objeto. Concatena el id y nombre de un vértice
     */
    @Override
    public String toString() {
        if (nombre.equals(String.valueOf(id))) {
            return nombre;
        }
        return nombre + " (" + id + ")";
    }
}
