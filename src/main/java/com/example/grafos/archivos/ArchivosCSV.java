package com.example.grafos.archivos;
import com.example.grafos.modelo.Arista;
import com.example.grafos.modelo.Grafo;
import com.example.grafos.modelo.Vertice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArchivosCSV {

    private List<String> listaExcepciones; //almacenar errores para poder mostrarlos en interfaz

    public ArchivosCSV(){
        listaExcepciones= new ArrayList<>();
    }


    /*
    Lee archivo y regresa lista con cada linea
     */
    private List<String> leerArchivo(String ruta){
        List<String> lineas= new ArrayList<>();
        try{
            FileReader archivo= new FileReader(ruta);
            BufferedReader br= new BufferedReader(archivo);
            String linea= br.readLine();
            while (linea != null){ // null significa que ya no hay mas lineas
                lineas.add(linea);
                linea= br.readLine(); // lee la siguiente linea
            }
            br.close();
        }catch(IOException e){
            listaExcepciones.add("No se pudo leer el archivo: " + e.getMessage());
            return null;
        }
        return lineas;
    }

    /*
    Busca el vertice por su nombre y regresa su id.
     */
    private int identificarVertices(String nombre, Grafo grafo){
        Vertice vertice= grafo.buscarVerticePorNombre(nombre);
        if(vertice != null){ // ya existe, solo se regresa su id
            return vertice.getId();
        }
        // si no existe el vertice, se agrega al grafo con el sig id
        int id= grafo.cantidadVertices() + 1;
        grafo.agregarVertice(id, nombre);
        return id;
    }

    /*
    Crea la arista entre dos vertices usando sus nombres.
     */
    private boolean crearArista(String origen, String destino, double peso, Grafo grafo){
        // obtener id de cada vértice o crearlo si es nuevo, agregar la arista al grafo con su peso
        int idOrigen= identificarVertices(origen, grafo);
        int idDestino= identificarVertices(destino, grafo);
        return grafo.agregarArista(idOrigen, idDestino, peso); // regresa false si el grafo no la acepta
    }

    /*
    Evalua si el archivo tiene las columnas requeridas, encabezado
     */
    private boolean validarEncabezado(String linea){
        String[] columnas= linea.split(","); // separar linea
        if(columnas.length < 3){
            listaExcepciones.add("El archivo no tiene las tres columnas necesarias");
            return false;
        }
        if(!columnas[0].trim().equals("origen") || !columnas[1].trim().equals("destino") || !columnas[2].trim().equals("peso")){
            listaExcepciones.add("El encabezado no tiene el orden correcto (origen,destino,peso)");
            return false;
        }
        return true;
    }

    /*
    Busca si hay algun registro que no cumpla con los 3 campos del encabezado
     */
    private boolean validarRegistroCompleto(String[] partes, int numeroLinea){
        if(partes.length<3){
            listaExcepciones.add("Linea "+numeroLinea+", registro incompleto");
            return false;
        }
        return true;
    }

    /*
    Evita conexiones con vertices vacios
     */
    private boolean validarVerticesVacios(String origen, String destino, int numeroLinea){
        if(origen.isEmpty() || destino.isEmpty()){
            listaExcepciones.add("Linea "+numeroLinea+", la conexion tiene un vertice vacio");
            return false;
        }
        return true;
    }

    /*
    Validar peso numerico y no negativo
     */
    private boolean validarPeso(String textoPeso, int numeroLinea){
        double peso;
        try{
            peso= Double.parseDouble(textoPeso); // convertir texto a numero
        }catch(NumberFormatException e){ // el texto no es un numero
            listaExcepciones.add("Linea "+numeroLinea+", el peso " + textoPeso + " no es numerico");
            return false;
        }
        if(peso<0){
            listaExcepciones.add("Linea "+numeroLinea+", el peso no puede ser negativo");
            return false;
        }
        return true;
    }

    /*
    Evita que un vertice se conecte consigo mismo
     */
    private boolean validarConexionVertice(String origen, String destino, int numeroLinea){
        if(origen.equals(destino)){
            listaExcepciones.add("Linea "+numeroLinea+", un vertice no puede conectarse consigo mismo");
            return false;
        }
        return true;
    }

    /*
    Identifica conexiones duplicadas
     */
    private boolean validarDuplicada(String origen, String destino, int numeroLinea, Grafo grafo){
        // busca vertice por nombre y los obtiene
        Vertice verticeOrigen= grafo.buscarVerticePorNombre(origen);
        Vertice verticeDestino= grafo.buscarVerticePorNombre(destino);

        // se evalua si los dos nodos ya existen en el grafo, y ya hay una arista entre ellos
        if(verticeOrigen!=null && verticeDestino!=null && grafo.existeArista(verticeOrigen.getId(), verticeDestino.getId())){
            listaExcepciones.add("Linea "+numeroLinea+", la conexion " + origen + "/" + destino + " esta duplicada");
            return false; // no se importa la fila
        }
        return true; // nueva conexion y se importa la fila
    }

    /*
    Revisa una fila completa usando metodos auxiliares de validaciones
     */
    private boolean validarRegistros(String linea, int numeroLinea, Grafo grafo){
        String[] partes= linea.split(",");
        if(!validarRegistroCompleto(partes, numeroLinea)){ // evalua si tiene las 3 columnas
            return false;
        }
        // obtener dato de cada columna
        String origen= partes[0].trim();
        String destino= partes[1].trim();
        String textoPeso= partes[2].trim();

        return validarVerticesVacios(origen, destino, numeroLinea) && validarPeso(textoPeso, numeroLinea)
                && validarConexionVertice(origen, destino, numeroLinea) && validarDuplicada(origen, destino, numeroLinea, grafo);
    }

    /*
    Lee un archivo y devuelve un grafo construido
    Regresa null si el archivo no se pudo leer o el encabezado es incorrecto
     */
    public Grafo importar(String ruta, boolean dirigido){
        listaExcepciones.clear(); // borra los errores de una importacion anterior
        Grafo grafo= new Grafo(dirigido);

        List<String> lineas= leerArchivo(ruta);
        if(lineas==null){ // no se pudo leer el archivo
            return null;
        }
        if(lineas.isEmpty()){ // archivo vacio
            listaExcepciones.add("El archivo esta vacio");
            return null;
        }
        if(!validarEncabezado(lineas.get(0))){ // manda el encabezado para revisar orden
            return null;
        }

        // RECORRER ARCHIVO
        for(int i= 1; i < lineas.size(); i++){ // empieza en 1 para saltar el encabezado
            String linea= lineas.get(i);
            int numeroLinea= i + 1; // numero de linea como se ve en el archivo
            if(linea.trim().isEmpty()){ // ignorar lineas vacias
                continue;
            }
            if(validarRegistros(linea, numeroLinea, grafo)){ // solo importar filas validas
                String[] partes= linea.split(",");
                String origen= partes[0].trim();
                String destino= partes[1].trim();
                double peso= Double.parseDouble(partes[2].trim());
                crearArista(origen, destino, peso, grafo); // llenado del grafo
            }
        }
        return grafo;
    }

    /*
    Informa el numero de vertices y aristas importadas
     */
    public String informeGrafo(Grafo grafo){
        if(grafo==null){
            return "Fallo en la importación. Errores: " + listaExcepciones.size();
        }
        return "El grafo tiene "+grafo.cantidadVertices()+" vertices y " + grafo.cantidadAristas() +" aristas.";
    }

    /*
    Regresa una copia de la lista de errores para mostrarlos en la interfaz
     */
    public List<String> obtenerExcepciones(){
        return new ArrayList<>(listaExcepciones);
    }

    /*
    Guarda el grafo en un archivo CSV con el formato origen,destino,peso
    Regresa false si no se pudo guardar
     */
    public boolean exportar(Grafo grafo, String ruta){
        listaExcepciones.clear(); // borra los errores anteriores
        if(grafo==null){ // no hay grafo para guardar
            listaExcepciones.add("No hay un grafo para exportar");
            return false;
        }
        try{
            FileWriter archivo= new FileWriter(ruta); // crear el archivo
            BufferedWriter bw= new BufferedWriter(archivo); // permite escribir en el archivo linea por linea
            bw.write("origen,destino,peso");
            bw.newLine();
            for(Arista arista : grafo.obtenerAristas()){ // recorre las aristas, una linea por cada arista
                // la arista guarda el id del origen con el que se busca el vertice para obtener su nombre
                String origen= grafo.obtenerVertice(arista.getOrigen()).getNombre();
                String destino= grafo.obtenerVertice(arista.getDestino()).getNombre();
                bw.write(origen+","+destino+","+arista.getPeso()); // escribir con separacion
                bw.newLine();
            }
            bw.close();
        }catch(IOException e){
            listaExcepciones.add("No se pudo guardar el archivo. Error: " + e.getMessage());
            return false;
        }
        return true;
    }


}
