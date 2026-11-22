package com.example.grafos.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {

    private boolean dirigido;
    private List<Vertice> vertices; // en el orden en que se agregan
    private Map<Integer, List<Arista>> listaAdyacencia; // id del vertice -> aristas que salen de el

    /*
    Crea un grafo vacio, dirigido o no dirigido
     */
    public Grafo(boolean dirigido){
        this.dirigido = dirigido;
        vertices = new ArrayList<>();
        listaAdyacencia = new HashMap<>();
    }

    /*
    Indica si el grafo es dirigido
     */
    public boolean esDirigido(){
        return dirigido;
    }

    /*
    Indica si existe un vertice con ese id
     */
    public boolean existeVertice(int id){
        return listaAdyacencia.containsKey(id);
    }

    /*
    Agrega un vertice usando su id como nombre
     */
    public boolean agregarVertice(int id){
        return agregarVertice(id, String.valueOf(id));
    }

    /*
    Agrega un vertice con nombre, no se permiten id's ni nombres repetidos
    */
    public boolean agregarVertice(int id, String nombre){
        nombre = Vertice.validarNombre(nombre, id);
        if(existeVertice(id) || buscarVerticePorNombre(nombre)!= null){
            return false;
        }
        vertices.add(new Vertice(id, nombre));
        listaAdyacencia.put(id, new ArrayList<>()); // empieza sin aristas
        return true;
    }

    /*
    Elimina el vertice y todas las aristas que salen o llegan a el
     */
    public boolean eliminarVertice(int id){
        Vertice vertice = obtenerVertice(id);
        if(vertice==null) {return false;}

        vertices.remove(vertice);
        listaAdyacencia.remove(id); // aristas que salen del vertice
        for(List<Arista> aristas : listaAdyacencia.values()){
            for(int i = aristas.size() - 1; i >= 0; i--){
                if(aristas.get(i).getDestino() == id){
                    aristas.remove(i); // aristas que llegan al vertice
                }
            }
        }
        return true;
    }

    /*
    Cambia el nombre de un vertice si el nombre nuevo no lo usa otro vertice
     */
    public boolean cambiarNombreVertice(int id, String nuevoNombre){
        Vertice vertice = obtenerVertice(id);
        if (vertice==null) {return false;} // no existe

        nuevoNombre = Vertice.validarNombre(nuevoNombre, id); // eliminar espacios
        Vertice v = buscarVerticePorNombre(nuevoNombre);
        if(v!=null && v.getId()!=id){ // ya hay vertice con ese nombre
            return false;
        }
        vertice.setNombre(nuevoNombre);
        return true;
    }

    /*
    Busca un vertice por id
     */
    public Vertice obtenerVertice(int id){
        for(Vertice vertice : vertices){
            if(vertice.getId()==id){
                return vertice;
            }
        }
        return null;
    }

    /*
    Busca un vertice por nombre
    */
    public Vertice buscarVerticePorNombre(String nombre){
        if (nombre==null){return null;}

        for(Vertice vertice : vertices){
            if(vertice.getNombre().equals(nombre.trim())){
                return vertice;
            }
        }
        return null;
    }

    /*
    Regresa una copia de la lista de vertices
    */
    public List<Vertice> obtenerVertices(){
        return new ArrayList<>(vertices);
    }

    /*
    Numero de vertices del grafo
    */
    public int cantidadVertices(){
        return vertices.size();
    }

    /*
    Numero de aristas del grafo
     */
    public int cantidadAristas(){
        return obtenerAristas().size();
    }

    /*
    Revisa si se puede agregar la arista
     */
    public String validarArista(int origen, int destino, double peso){
        if(!existeVertice(origen) || !existeVertice(destino)){
            return "Alguno o ninguno de los vértices existe";
        }else if (origen==destino){
            return "El vértice no se puede conectar consigo mismo";
        }else if (peso < 0){
            return "El peso no puede ser negativo";
        }else if (existeArista(origen, destino)){
            return "La conexión ya existe";
        }
        return null;
    }

    /*
    Agrega una arista
    Para grafo no dirigido debe ser bidireccional la conexion
     */
    public boolean agregarArista(int origen, int destino, double peso) {
        if(validarArista(origen, destino, peso) != null) {return false;}
        listaAdyacencia.get(origen).add(new Arista(origen, destino, peso));

        if(!dirigido){
            listaAdyacencia.get(destino).add(new Arista(destino, origen, peso)); // bidireccional
        }
        return true;
    }

    /*
    Busca una arista dados dos vértices
     */
    public Arista obtenerArista(int origen, int destino) {
        if(!existeVertice(origen)){return null;}

        for(Arista arista : listaAdyacencia.get(origen)){
            if(arista.getDestino()==destino){
                return arista;
            }
        }
        return null;
    }

    /*
    Elimina una arista
    */
    public boolean eliminarArista(int origen, int destino) {
        Arista arista = obtenerArista(origen, destino);
        if(arista==null) {return false;}
        listaAdyacencia.get(origen).remove(arista);

        if(!dirigido){
            listaAdyacencia.get(destino).remove(obtenerArista(destino, origen));
        }
        return true;
    }

    /*
    Cambia el peso de una arista
    */
    public boolean cambiarPesoArista(int origen, int destino, double nuevoPeso) {
        Arista arista=obtenerArista(origen, destino);
        if(arista==null || nuevoPeso<0) {return false;}
        arista.setPeso(nuevoPeso);
        if (!dirigido) {
            obtenerArista(destino, origen).setPeso(nuevoPeso); // mismo peso en el otro sentido
        }
        return true;
    }

    /*
    Indica si existe una arista de origen a destino
    */
    public boolean existeArista(int origen, int destino){
        return obtenerArista(origen, destino) != null;
    }

    /*
    Regresa todas las aristas
    */
    public List<Arista> obtenerAristas(){
        List<Arista> resultado = new ArrayList<>();
        for(Vertice vertice : vertices){
            for(Arista arista : listaAdyacencia.get(vertice.getId())){
                if(dirigido || arista.getOrigen()<arista.getDestino()){
                    resultado.add(arista);
                }
            }
        }
        return resultado;
    }

    /*
    Regresa las aristas que salen de un vertice
     */
    public List<Arista> obtenerAristasDesde(int id){
        if(!existeVertice(id)){return new ArrayList<>();} // no existe arista, devuelve lista vacía
        return new ArrayList<>(listaAdyacencia.get(id)); // devuelve la lista de aristas de un vértice
    }

    /*
    Posición del vertice en la lista de vertices y en la matriz
     */
    public int obtenerIndiceVertice(int id){
        for(int i=0; i<vertices.size(); i++){
            if(vertices.get(i).getId()==id){
                return i;
            }
        }
        return -1;
    }

    /*
    Crea la matriz de adyacencia
     */
    public double[][] crearMatriz(){
        int num=vertices.size();
        double[][] matriz = new double[num][num];

        // llenar tabla con -1, significa que no hay arista
        for(int i=0; i<num; i++){ //fila
            for(int j=0; j<num; j++){ // columna
                matriz[i][j] = -1;
            }
        }

        // colocar pesos existentes
        for(int i=0; i<num; i++){
            for(Arista arista : listaAdyacencia.get(vertices.get(i).getId())){ //fila
                int j=obtenerIndiceVertice(arista.getDestino()); // obtener columna
                matriz[i][j]= arista.getPeso(); // colocar peso
            }
        }
        return matriz;
    }

    /*
    Regresa la lista de adyacencia como texto, una línea por vertice
    */
    public String mostrarListaAdyacencia() {
        StringBuilder texto= new StringBuilder();
        for(Vertice vertice : vertices){
            texto.append(vertice.getNombre()).append(" -> ");
            for(Arista arista : listaAdyacencia.get(vertice.getId())){
                texto.append(obtenerVertice(arista.getDestino()).getNombre());
                texto.append(" (").append(arista.getPeso()).append(")  ");
            }
            texto.append("\n");
        }
        return texto.toString();
    }

}
