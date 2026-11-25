package com.example.grafos.mediciones;

import com.example.grafos.algoritmos.RecorridosGrafo;
import com.example.grafos.algoritmos.RutasCortas;
import com.example.grafos.modelo.Arista;
import com.example.grafos.modelo.Grafo;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AnalisisTiempos {
    private int repeticiones;

    public AnalisisTiempos(){
        this(10);
    }

    public  AnalisisTiempos(int repeticiones){
        if (repeticiones < 1){
            throw new IllegalArgumentException("repeticiones invalidas");
        }
        this.repeticiones = repeticiones;
    }

    public ResultadoMedicion medir(String algoritmo, String descripcion, int vertices, int aristas, Runnable tarea){
        double suma = 0;
        double min = Double.MAX_VALUE;
        double max = 0;

        for (int i = 0; i < repeticiones; i++){
            long inicio = System.nanoTime();
            tarea.run();
            double ms = (System.nanoTime() - inicio) / 1000000.0;

            suma += ms;
            min = Math.min(min, ms);
            max = Math.max(max, ms);
        }
        return new ResultadoMedicion(algoritmo, descripcion, vertices, aristas, repeticiones, suma / repeticiones, min, max);
    }

    public ResultadoMedicion medirImportacion(String descripcion, Supplier<Grafo> importador){
        Grafo grafo = importador.get();
        return medir("Importar CSV", descripcion, grafo.cantidadVertices(), grafo.cantidadAristas(), importador::get);
    }

    public ResultadoMedicion medirConstruccion(String descripcion, int vertices, ArrayList<Arista> aristas, Supplier<Grafo> grafoVacio) {
        return medir("Construccion", descripcion, vertices, aristas.size(), () -> {
            Grafo g = grafoVacio.get();
            for (int i = 0; i < vertices; i++) {
                g.agregarVertice(i);
            }
            for (Arista a : aristas) {
                g.agregarArista(a.getOrigen(), a.getDestino(), a.getPeso());
            }
        });
    }

    public ResultadoMedicion medirBFS(Grafo grafo, int inicio, String descripcion) {
        return medir("BFS", descripcion, grafo.cantidadVertices(), grafo.cantidadAristas(), () -> RecorridosGrafo.bfs(grafo, inicio));
    }

    public ResultadoMedicion medirDFS(Grafo grafo, int inicio, String descripcion) {
        return medir("DFS", descripcion, grafo.cantidadVertices(), grafo.cantidadAristas(), () -> RecorridosGrafo.dfs(grafo, inicio));
    }

    public ResultadoMedicion medirDijkstra(Grafo grafo, int origen, int destino, String descripcion) {
        return medir("Dijkstra", descripcion, grafo.cantidadVertices(), grafo.cantidadAristas(), () -> RutasCortas.dijsktra(grafo, origen, destino));
    }

    public ResultadoMedicion medirOrdenamiento(String algoritmo, String descripcion, ArrayList<Arista> aristas, Consumer<ArrayList<Arista>> ordenador) {
        return medir(algoritmo, descripcion, 0, aristas.size(), () -> ordenador.accept(new ArrayList<>(aristas)));
    }

}
