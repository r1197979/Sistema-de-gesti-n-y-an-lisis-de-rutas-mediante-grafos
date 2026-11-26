package com.example.grafos.interfaz;

import com.example.grafos.algoritmos.RecorridosGrafo;
import com.example.grafos.algoritmos.ResultadoRuta;
import com.example.grafos.algoritmos.RutasCortas;
import com.example.grafos.archivos.ArchivosCSV;
import com.example.grafos.mediciones.AnalisisTiempos;
import com.example.grafos.mediciones.ResultadoMedicion;
import com.example.grafos.ordenamientos.Ordenamientos;
import com.example.grafos.modelo.Arista;
import com.example.grafos.modelo.Grafo;
import com.example.grafos.modelo.Vertice;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ControladorGrafo {

    private VentanaPrincipal ventana;
    private PanelEdicion edicion;
    private PanelResultados resultados;
    private VistaGrafo vista;

    private Grafo grafo;
    private ArchivosCSV archivosCSV = new ArchivosCSV();
    private Ordenamientos ordenamientos = new Ordenamientos();
    private AnalisisTiempos analisisTiempos = new AnalisisTiempos();
    private List<ResultadoMedicion> mediciones = new ArrayList<>();

    public ControladorGrafo(VentanaPrincipal ventana) {
        this.ventana = ventana;
        edicion = ventana.getPanelEdicion();
        resultados = ventana.getPanelResultados();
        vista = ventana.getVistaGrafo();

        grafo = new Grafo(edicion.esDirigido());

        conectarAcciones();
        actualizarVista();

        ventana.actualizarEstado("Grafo vacio listo para editar");
    }

    private void conectarAcciones() {
        // conecta los controles con los metodos del controlador
        edicion.setAccionNuevo(dirigido -> nuevoGrafo(dirigido));
        edicion.setAccionImportar(archivo -> importarCsv(archivo));
        edicion.setAccionExportar(archivo -> exportarCsv(archivo));

        edicion.setAccionVertice(id -> agregarVertice(id));
        edicion.setAccionModificarVertice((id, nombre) -> modificarVertice(id, nombre));
        edicion.setAccionEliminarVertice(id -> eliminarVertice(id));

        edicion.setAccionArista((origen, destino, peso) -> agregarArista(origen, destino, peso));
        edicion.setAccionModificarArista((origen,destino,peso) -> modificarArista(origen,destino,peso));
        edicion.setAccionEliminarArista((origen, destino) -> eliminarArista(origen, destino));

        edicion.setAccionBfs(inicio -> ejecutarBfs(inicio));
        edicion.setAccionDfs(inicio -> ejecutarDfs(inicio));
        edicion.setAccionDijkstra((origen,destino) -> ejecutarDijkstra(origen,destino));
        edicion.setAccionOrdenar((metodo, dato) -> ordenarDatos(metodo, dato));
        vista.setAccionSeleccionVertice(id -> mostrarInfoVertice(id));

        resultados.setAccionVerMatriz(() ->
                resultados.mostrarMatriz(generarTextoMatriz()));

        resultados.setAccionVerLista(() ->
                resultados.mostrarLista(grafo.mostrarListaAdyacencia()));
    }

    private void nuevoGrafo(boolean dirigido) {
        if(grafo.cantidadVertices() > 0) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Nuevo grafo");
            alerta.setHeaderText("Se reemplazara el grafo actual");
            alerta.setContentText("¿Desea continuar?");

            if(alerta.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                return;
            }
        }

        grafo = new Grafo(dirigido);

        edicion.limpiarCampos();
        resultados.limpiarResultados();
        ventana.getPanelGraficas().limpiarGrafica();
        mediciones.clear();

        actualizarVista();

        informar(dirigido ? "Grafo dirigido creado" : "Grafo no dirigido creado");
    }

    private void agregarVertice(int id) {
        if(!grafo.agregarVertice(id)) {
            informar("El ID o nombre del vertice ya existe");
            return;
        }

        actualizarVista();
        informar("Vertice " + id + " agregado");
    }

    private void modificarVertice(int id, String nombre) {
        if(nombre==null || nombre.trim().isEmpty()) {
            informar("Ingrese un nombre para el vertice");
            return;
        }

        if(!grafo.cambiarNombreVertice(id, nombre)) {
            informar("No se pudo modificar el vertice. Revise el ID o nombre");
            return;
        }

        actualizarVista();
        informar("Nombre del vertice " + id + " actualizado");
    }

    private void eliminarVertice(int id) {
        if (!grafo.eliminarVertice(id)) {
            informar("El vertice " + id + " no existe");
            return;
        }

        actualizarVista();
        informar("Vertice " + id + " eliminado");
    }

    private void agregarArista(int origen, int destino, double peso) {
        if(!Double.isFinite(peso)) {
            informar("El peso debe ser un numero finito");
            return;
        }

        String error = grafo.validarArista(origen, destino, peso);

        if(error!=null) {
            informar(error);
            return;
        }

        if(grafo.agregarArista(origen, destino, peso)) {
            actualizarVista();
            informar("Conexion agregada: " + origen + " a " + destino);
        }
    }

    private void modificarArista(int origen, int destino, double peso) {
        if(!Double.isFinite(peso) || peso<0) {
            informar("El peso debe ser un numero no negativo");
            return;
        }

        if(!grafo.cambiarPesoArista(origen, destino, peso)) {
            informar("No se pudo modificar la conexion");
            return;
        }

        actualizarVista();
        informar("Peso de la conexion actualizado");
    }

    private void eliminarArista(int origen, int destino) {
        if(!grafo.eliminarArista(origen, destino)) {
            informar("La conexion no existe");
            return;
        }

        actualizarVista();
        informar("Conexion eliminada");
    }

    private void ejecutarBfs(int inicio) {
        try {
            List<Integer> orden = RecorridosGrafo.bfs(grafo, inicio);
            mostrarRecorrido("BFS", inicio, orden);
            registrarMedicion(analisisTiempos.medirBFS(grafo, inicio, descripcionActual()));
        } catch (IllegalArgumentException e) {
            informar(e.getMessage());
        }
    }

    private void ejecutarDfs(int inicio) {
        try {
            List<Integer> orden = RecorridosGrafo.dfs(grafo, inicio);
            mostrarRecorrido("DFS", inicio, orden);
            registrarMedicion(analisisTiempos.medirDFS(grafo, inicio, descripcionActual()));
        } catch (IllegalArgumentException e) {
            informar(e.getMessage());
        }
    }

    private void mostrarRecorrido(String algoritmo, int inicio, List<Integer> orden) {
        StringBuilder texto = new StringBuilder();
        texto.append(algoritmo).append(" desde el vertice ").append(inicio);
        texto.append("\nOrden de visita: ");

        for(int i=0; i<orden.size(); i++) {
            if(i>0) {
                texto.append(" -> ");
            }
            texto.append(orden.get(i));
        }

        texto.append("\nVertices visitados: ").append(orden.size());

        resultados.mostrarResultado(texto.toString());
        vista.resaltarVertices(orden);
        informar(algoritmo + " completado desde el vertice " + inicio);
    }

    private void ejecutarDijkstra(int origen, int destino) {
        try {
            ResultadoRuta resultado = RutasCortas.dijsktra(grafo, origen, destino);
            List<Integer> ruta = resultado.getRuta();

            resultados.mostrarResultado("Dijkstra\n" + resultado);
            vista.resaltarCamino(ruta);

            if(ruta.isEmpty()) {
                informar("No existe una ruta entre " + origen + " y " + destino);
            } else {
                informar("Ruta mas corta calculada entre " + origen + " y " + destino);
            }
            registrarMedicion(analisisTiempos.medirDijkstra(grafo, origen, destino, descripcionActual()));
        } catch (IllegalArgumentException e) {
            informar(e.getMessage());
        }
    }

    private void importarCsv(File archivo) {
        if(grafo.cantidadVertices()>0) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Importar CSV");
            alerta.setHeaderText("Se reemplazara el grafo actual");
            alerta.setContentText("¿Desea cargar el archivo seleccionado?");

            if(alerta.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                return;
            }
        }

        long inicio = System.nanoTime();
        Grafo importado = archivosCSV.importar(archivo.getAbsolutePath(), edicion.esDirigido());
        double tiempoMs = (System.nanoTime() - inicio) / 1000000.0;
        List<String> errores = archivosCSV.obtenerExcepciones();

        if(importado==null) {
            resultados.mostrarResultado("No se pudo importar " + archivo.getName() +
                    "\n" + String.join("\n", errores));
            informar("No se pudo leer el archivo CSV");
            return;
        }

        for(Arista arista : importado.obtenerAristas()) {
            if(!Double.isFinite(arista.getPeso())) {
                resultados.mostrarResultado("El archivo contiene pesos NaN o Infinity. " +
                        "Corrija esos valores antes de importar");
                informar("CSV rechazado por pesos no finitos");
                return;
            }
        }

        grafo = importado;
        resultados.limpiarResultados();
        ventana.getPanelGraficas().limpiarGrafica();
        mediciones.clear();
        actualizarVista();

        StringBuilder informe = new StringBuilder("Archivo: ").append(archivo.getName());
        informe.append("\n").append(archivosCSV.informeGrafo(grafo));

        if(!errores.isEmpty()) {
            informe.append("\n\nRegistros omitidos (" ).append(errores.size()).append("):\n");
            for(String error : errores) {
                informe.append(error).append("\n");
            }
        }

        resultados.mostrarResultado(informe.toString());
        registrarMedicion(new ResultadoMedicion("Importar CSV", descripcionActual(),
                grafo.cantidadVertices(), grafo.cantidadAristas(), 1, tiempoMs, tiempoMs, tiempoMs));

        if(errores.isEmpty()) {
            informar("Archivo CSV importado correctamente");
        } else {
            informar("CSV importado con " + errores.size() + " registros omitidos. Revise Recorridos");
        }
    }

    private void exportarCsv(File archivo) {
        long inicio = System.nanoTime();
        boolean guardado = archivosCSV.exportar(grafo, archivo.getAbsolutePath());
        double tiempoMs = (System.nanoTime() - inicio) / 1000000.0;

        if(!guardado) {
            informar("No se pudo exportar: " + String.join(". ", archivosCSV.obtenerExcepciones()));
            return;
        }

        registrarMedicion(new ResultadoMedicion("Exportar CSV", descripcionActual(),
                grafo.cantidadVertices(), grafo.cantidadAristas(), 1, tiempoMs, tiempoMs, tiempoMs));

        HashSet<Integer> conectados = new HashSet<>();
        for(Arista arista : grafo.obtenerAristas()) {
            conectados.add(arista.getOrigen());
            conectados.add(arista.getDestino());
        }

        int aislados=0;
        for(Vertice vertice : grafo.obtenerVertices()) {
            if(!conectados.contains(vertice.getId())) {
                aislados++;
            }
        }

        if(aislados>0) {
            informar("CSV guardado. Aviso: " + aislados + " vertices sin conexiones no se incluyen en el formato CSV");
        } else {
            informar("Grafo exportado a " + archivo.getName());
        }
    }

    private void ordenarDatos(String metodo, String dato) {
        boolean quick = "QuickSort".equals(metodo);
        if(!quick && !"Shell Sort".equals(metodo)) {
            informar("Seleccione un metodo de ordenamiento");
            return;
        }

        StringBuilder texto = new StringBuilder(metodo + " - " + dato + "\n\n");
        String nombreMedicion;
        ResultadoMedicion medicion;

        if(dato!=null && dato.startsWith("Vertices")) {
            Vertice[] originales= grafo.obtenerVertices().toArray(new Vertice[0]);
            if(originales.length==0) {
                informar("No hay vertices para ordenar");
                return;
            }

            Vertice[] ordenados = originales.clone();
            if(quick) {
                ordenamientos.quickSortVertices(ordenados);
            } else {
                ordenamientos.shellSortVertices(ordenados);
            }

            for(Vertice vertice : ordenados) {
                texto.append(vertice.getId()).append(" - ").append(vertice.getNombre()).append("\n");
            }

            nombreMedicion=metodo + " vertices";
            medicion = analisisTiempos.medir(nombreMedicion, descripcionActual(),
                    grafo.cantidadVertices(), grafo.cantidadAristas(), () -> {
                        Vertice[] copia=originales.clone();
                        if(quick) {
                            ordenamientos.quickSortVertices(copia);
                        } else {
                            ordenamientos.shellSortVertices(copia);
                        }
                    });
        } else if("Conexiones por peso".equals(dato)) {
            Arista[] originales= grafo.obtenerAristas().toArray(new Arista[0]);
            if(originales.length==0) {
                informar("No hay conexiones para ordenar");
                return;
            }

            Arista[] ordenadas = originales.clone();
            if(quick) {
                ordenamientos.quickSortAristas(ordenadas);
            } else {
                ordenamientos.shellSortAristas(ordenadas);
            }

            for(Arista arista : ordenadas) {
                texto.append(arista.getOrigen()).append(" -> ").append(arista.getDestino());
                texto.append("  Peso: ").append(arista.getPeso()).append("\n");
            }

            nombreMedicion=metodo + " aristas";
            medicion = analisisTiempos.medir(nombreMedicion, descripcionActual(),
                    grafo.cantidadVertices(), grafo.cantidadAristas(), () -> {
                        Arista[] copia=originales.clone();
                        if(quick) {
                            ordenamientos.quickSortAristas(copia);
                        } else {
                            ordenamientos.shellSortAristas(copia);
                        }
                    });
        } else {
            informar("Seleccione los datos que desea ordenar");
            return;
        }

        resultados.mostrarOrdenamiento(texto.toString());
        registrarMedicion(medicion);
        informar("Ordenamiento " + metodo + " completado sin modificar el grafo");
    }

    private String descripcionActual() {
        return "Grafo V=" + grafo.cantidadVertices() + " E=" + grafo.cantidadAristas();
    }

    private void registrarMedicion(ResultadoMedicion medicion) {
        mediciones.add(medicion);
        actualizarMediciones();
    }

    private void actualizarMediciones() {
        resultados.mostrarMediciones(mediciones);
        ventana.getPanelGraficas().limpiarGrafica();

        // organiza las mediciones segun el tamaño del grafo
        Map<String, Map<String, Double>> grupos = new LinkedHashMap<>();
        for(ResultadoMedicion medicion : mediciones) {
            String descripcion=medicion.getDescripcion();
            Map<String, Double> tiempos = grupos.get(descripcion);

            if(tiempos==null) {
                tiempos=new LinkedHashMap<>();
                grupos.put(descripcion, tiempos);
            }
            tiempos.put(medicion.getAlgoritmo(), medicion.getTiempoPromedioMs());
        }

        for(Map.Entry<String, Map<String, Double>> grupo : grupos.entrySet()) {
            String[] nombres = grupo.getValue().keySet().toArray(new String[0]);
            double[] tiempos = new double[nombres.length];
            for(int i=0; i<nombres.length; i++) {
                tiempos[i]=grupo.getValue().get(nombres[i]);
            }
            ventana.getPanelGraficas().mostrarTiempos(grupo.getKey(), nombres, tiempos);
        }
    }

    // actualiza el dibujo y las tablas con los datos reales
    private void actualizarVista() {
        resultados.limpiarRecorridos();
        resultados.limpiarOrdenamientos();

        List<Integer> ids = new ArrayList<>();
        List<String[]> filasAristas = new ArrayList<>();
        Map<Integer,String> nombres = new LinkedHashMap<>();

        for(Vertice vertice : grafo.obtenerVertices()) {
            ids.add(vertice.getId());
            nombres.put(vertice.getId(), vertice.getNombre());
        }

        vista.dibujarVertices(ids);

        for(Arista arista : grafo.obtenerAristas()) {
            vista.dibujarArista(arista.getOrigen(), arista.getDestino(),
                    arista.getPeso(), grafo.esDirigido());

            filasAristas.add(new String[]{
                    String.valueOf(arista.getOrigen()),
                    String.valueOf(arista.getDestino()),
                    String.valueOf(arista.getPeso())
            });
        }

        resultados.actualizarNombres(nombres);
        resultados.actualizarDatos(ids, filasAristas);
        resultados.actualizarPestanaActual();

        ventana.actualizarConteo(
                grafo.cantidadVertices(), grafo.cantidadAristas());
    }

    private void mostrarInfoVertice(int id) {
        Vertice v = grafo.obtenerVertice(id);
        if(v==null) {
            return;
        }

        int salientes = grafo.obtenerAristasDesde(id).size();
        int entrantes=0;
        for(Arista arista : grafo.obtenerAristas()) {
            if(arista.getDestino()==id) {
                entrantes++;
            }
        }
        String texto = "Vertice " + v.getNombre() + " (ID " + id + ")";
        if(grafo.esDirigido()) {
            texto += " | Salientes: " + salientes + " | Entrantes: " + entrantes;
        } else {
            texto += " | Conexiones: " + salientes;
        }
        ventana.actualizarEstado(texto);
    }

    private String generarTextoMatriz() {
        List<Vertice> vertices = grafo.obtenerVertices();
        double[][] matriz = grafo.crearMatriz();

        if(vertices.isEmpty()) {
            return "El grafo no tiene vertices";
        }

        StringBuilder texto = new StringBuilder("ID");

        for(Vertice v : vertices) {
            texto.append("\t").append(v.getId());
        }
        texto.append("\n");

        for(int i=0; i<vertices.size(); i++) {
            texto.append(vertices.get(i).getId());

            for(int j=0; j<vertices.size(); j++) {
                texto.append("\t");

                if(matriz[i][j] == -1) {
                    texto.append("-");
                } else {
                    texto.append(matriz[i][j]);
                }
            }
            texto.append("\n");
        }

        return texto.toString();
    }

    private void informar(String texto) {
        edicion.mostrarMensaje(texto);
        ventana.actualizarEstado(texto);
    }
}
