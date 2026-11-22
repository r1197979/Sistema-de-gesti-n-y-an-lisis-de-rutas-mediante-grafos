package com.example.grafos.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Grafo {
    public static final double SIN_CONEXION = Double.POSITIVE_INFINITY;

    private final boolean dirigido;

    private final Map<Integer, Vertice> vertices = new LinkedHashMap<>();

    private final Map<String, Integer> idsPorNombre = new HashMap<>();

    private final Map<Integer, Map<Integer, Arista>> adyacencia = new LinkedHashMap<>();

    private final List<Arista> aristas = new ArrayList<>();

    public Grafo(boolean dirigido) {
        this.dirigido = dirigido;
    }

    public boolean esDirigido() {
        return dirigido;
    }

    public boolean agregarVertice(int id) {
        return agregarVertice(id, String.valueOf(id));
    }

    public boolean agregarVertice(int id, String nombre) {
        String nombreFinal = Vertice.normalizarNombre(nombre, id);
        if (vertices.containsKey(id) || idsPorNombre.containsKey(nombreFinal)) {
            return false;
        }
        vertices.put(id, new Vertice(id, nombreFinal));
        idsPorNombre.put(nombreFinal, id);
        adyacencia.put(id, new LinkedHashMap<>());
        return true;
    }

    public boolean eliminarVertice(int id) {
        Vertice vertice = vertices.remove(id);
        if (vertice == null) {
            return false;
        }
        idsPorNombre.remove(vertice.getNombre());
        adyacencia.remove(id);
        for (Map<Integer, Arista> salientes : adyacencia.values()) {
            salientes.remove(id);
        }
        List<Arista> restantes = new ArrayList<>();
        for (Arista arista : aristas) {
            if (arista.getOrigen() != id && arista.getDestino() != id) {
                restantes.add(arista);
            }
        }
        aristas.clear();
        aristas.addAll(restantes);
        return true;
    }

    public boolean cambiarNombreVertice(int id, String nuevoNombre) {
        Vertice vertice = vertices.get(id);
        if (vertice == null) {
            return false;
        }
        String nombreFinal = Vertice.normalizarNombre(nuevoNombre, id);
        Integer dueno = idsPorNombre.get(nombreFinal);
        if (dueno != null && dueno != id) {
            return false;
        }
        idsPorNombre.remove(vertice.getNombre());
        vertice.setNombre(nombreFinal);
        idsPorNombre.put(nombreFinal, id);
        return true;
    }

    public boolean cambiarIdVertice(int idActual, int idNuevo) {
        if (!vertices.containsKey(idActual)) {
            return false;
        }
        if (idActual == idNuevo) {
            return true;
        }
        if (vertices.containsKey(idNuevo)) {
            return false;
        }
        Vertice vertice = vertices.get(idActual);
        String nombreNuevo = vertice.getNombre();
        if (nombreNuevo.equals(String.valueOf(idActual))) {
            nombreNuevo = String.valueOf(idNuevo);
            Integer dueno = idsPorNombre.get(nombreNuevo);
            if (dueno != null && dueno != idActual) {
                return false;
            }
        }

        List<Vertice> verticesAnteriores = new ArrayList<>(vertices.values());
        List<Arista> aristasAnteriores = new ArrayList<>(aristas);
        vertices.clear();
        idsPorNombre.clear();
        adyacencia.clear();
        aristas.clear();
        for (Vertice v : verticesAnteriores) {
            if (v.getId() == idActual) {
                agregarVertice(idNuevo, nombreNuevo);
            } else {
                agregarVertice(v.getId(), v.getNombre());
            }
        }
        for (Arista a : aristasAnteriores) {
            int origen = a.getOrigen() == idActual ? idNuevo : a.getOrigen();
            int destino = a.getDestino() == idActual ? idNuevo : a.getDestino();
            agregarArista(origen, destino, a.getPeso());
        }
        return true;
    }

    public boolean existeVertice(int id) {
        return vertices.containsKey(id);
    }

    public Vertice obtenerVertice(int id) {
        return vertices.get(id);
    }

    public Vertice buscarVerticePorNombre(String nombre) {
        if (nombre == null) {
            return null;
        }
        Integer id = idsPorNombre.get(nombre.trim());
        return id == null ? null : vertices.get(id);
    }

    public List<Vertice> obtenerVertices() {
        return new ArrayList<>(vertices.values());
    }

    public List<Integer> obtenerIdsVertices() {
        return new ArrayList<>(vertices.keySet());
    }

    public int cantidadVertices() {
        return vertices.size();
    }

    public String validarArista(int origen, int destino, double peso) {
        if (!vertices.containsKey(origen)) {
            return "El vértice de origen " + origen + " no existe.";
        }
        if (!vertices.containsKey(destino)) {
            return "El vértice de destino " + destino + " no existe.";
        }
        if (origen == destino) {
            return "No se permite conectar un vértice consigo mismo.";
        }
        if (Double.isNaN(peso) || Double.isInfinite(peso)) {
            return "El peso debe ser un número.";
        }
        if (peso < 0) {
            return "El peso no puede ser negativo.";
        }
        if (existeArista(origen, destino)) {
            return "La conexión entre " + origen + " y " + destino + " ya existe.";
        }
        return null;
    }

    public boolean agregarArista(int origen, int destino, double peso) {
        if (validarArista(origen, destino, peso) != null) {
            return false;
        }
        Arista arista = new Arista(origen, destino, peso);
        aristas.add(arista);
        adyacencia.get(origen).put(destino, arista);
        if (!dirigido) {
            adyacencia.get(destino).put(origen, new Arista(destino, origen, peso));
        }
        return true;
    }

    public boolean eliminarArista(int origen, int destino) {
        if (!existeArista(origen, destino)) {
            return false;
        }
        adyacencia.get(origen).remove(destino);
        if (!dirigido) {
            adyacencia.get(destino).remove(origen);
        }
        for (int i = 0; i < aristas.size(); i++) {
            Arista a = aristas.get(i);
            boolean mismoSentido = a.getOrigen() == origen && a.getDestino() == destino;
            boolean sentidoContrario = !dirigido && a.getOrigen() == destino && a.getDestino() == origen;
            if (mismoSentido || sentidoContrario) {
                aristas.remove(i);
                break;
            }
        }
        return true;
    }

    public boolean modificarPesoArista(int origen, int destino, double nuevoPeso) {
        if (!existeArista(origen, destino) || Double.isNaN(nuevoPeso)
                || Double.isInfinite(nuevoPeso) || nuevoPeso < 0) {
            return false;
        }
        adyacencia.get(origen).get(destino).setPeso(nuevoPeso);
        if (!dirigido) {
            adyacencia.get(destino).get(origen).setPeso(nuevoPeso);
        }
        return true;
    }

    public boolean existeArista(int origen, int destino) {
        Map<Integer, Arista> salientes = adyacencia.get(origen);
        return salientes != null && salientes.containsKey(destino);
    }

    public Arista obtenerArista(int origen, int destino) {
        Map<Integer, Arista> salientes = adyacencia.get(origen);
        return salientes == null ? null : salientes.get(destino);
    }

    public List<Arista> obtenerAristas() {
        return new ArrayList<>(aristas);
    }

    public List<Arista> obtenerAristasDesde(int id) {
        Map<Integer, Arista> salientes = adyacencia.get(id);
        if (salientes == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(salientes.values());
    }

    public List<Integer> obtenerVecinos(int id) {
        Map<Integer, Arista> salientes = adyacencia.get(id);
        if (salientes == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(salientes.keySet());
    }

    public int cantidadAristas() {
        return aristas.size();
    }

    public double[][] crearMatriz() {
        int n = vertices.size();
        Map<Integer, Integer> indices = crearMapaIndices();
        double[][] matriz = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matriz[i][j] = SIN_CONEXION;
            }
        }
        for (Map.Entry<Integer, Map<Integer, Arista>> fila : adyacencia.entrySet()) {
            int i = indices.get(fila.getKey());
            for (Arista arista : fila.getValue().values()) {
                matriz[i][indices.get(arista.getDestino())] = arista.getPeso();
            }
        }
        return matriz;
    }

    public int obtenerIndiceVertice(int id) {
        int indice = 0;
        for (int idActual : vertices.keySet()) {
            if (idActual == id) {
                return indice;
            }
            indice++;
        }
        return -1;
    }

    public Map<Integer, Integer> crearMapaIndices() {
        Map<Integer, Integer> indices = new HashMap<>();
        int indice = 0;
        for (int id : vertices.keySet()) {
            indices.put(id, indice);
            indice++;
        }
        return indices;
    }

    public String mostrarListaAdyacencia() {
        StringBuilder texto = new StringBuilder();
        for (Map.Entry<Integer, Map<Integer, Arista>> fila : adyacencia.entrySet()) {
            texto.append(vertices.get(fila.getKey())).append(" -> ");
            boolean primero = true;
            for (Arista arista : fila.getValue().values()) {
                if (!primero) {
                    texto.append(", ");
                }
                texto.append(vertices.get(arista.getDestino()))
                        .append(" [").append(arista.getPeso()).append("]");
                primero = false;
            }
            texto.append(System.lineSeparator());
        }
        return texto.toString();
    }

    public void limpiar() {
        vertices.clear();
        idsPorNombre.clear();
        adyacencia.clear();
        aristas.clear();
    }

    @Override
    public String toString() {
        return "Grafo " + (dirigido ? "dirigido" : "no dirigido")
                + " con " + cantidadVertices() + " vértices y " + cantidadAristas() + " aristas";
    }
}
