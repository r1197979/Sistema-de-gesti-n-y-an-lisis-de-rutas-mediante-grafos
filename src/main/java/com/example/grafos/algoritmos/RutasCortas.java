package com.example.grafos.algoritmos;

import com.example.grafos.modelo.Arista;
import com.example.grafos.modelo.Grafo;

import java.util.*;

/*
Algoritmo de Dijkstra
 */
public class RutasCortas {
    private static class Nodo implements Comparable<Nodo>{
        int vertice;
        double distancia;

        Nodo(int vertice, double distancia){
            this.vertice = vertice;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(Nodo otro) {
            return Double.compare(distancia, otro.distancia);
        }
    }

    public static ResultadoRuta dijsktra(Grafo grafo, int origen, int destino){
        if(grafo == null){
            throw new IllegalArgumentException("El grafo no puede ser nulo");
        }
        if(!grafo.existeVertice(origen)){
            throw  new IllegalArgumentException("El vértice de origen no existe: " + origen);
        }
        if (!grafo.existeVertice(destino)){
            throw new IllegalArgumentException("El vértice de destino no existe: " + destino);
        }

        HashMap<Integer, Double> dist = new HashMap<>();
        HashMap<Integer, Integer> previo = new HashMap<>();
        HashSet<Integer> procesados = new HashSet<>();
        PriorityQueue<Nodo> cola = new PriorityQueue<>();

        dist.put(origen, 0.0);
        cola.add(new Nodo(origen, 0.0));

        while (!cola.isEmpty()){
            int u = cola.poll().vertice;

            if(!procesados.add(u)){
                continue;
            }

            if(u == destino){
                break;
            }

            for(Arista a: grafo.obtenerAristasDesde(u)){
                if (a.getPeso() < 0){
                    throw new IllegalArgumentException("Dijkstra requiere pesos no negativos");
                }
                int v = a.getDestino();
                if (procesados.contains(v)){
                    continue;
                }
                double nueva = dist.get(u) + a.getPeso();
                Double actualV = dist.get(v);
                if (actualV == null || nueva < actualV){
                    dist.put(v, nueva);
                    previo.put(v, u);
                    cola.add(new Nodo(v, nueva));
                }
            }
        }
        if(!dist.containsKey(destino)){
            return ResultadoRuta.sinRuta(origen, destino);
        }

        ArrayDeque<Integer> pilaRuta = new ArrayDeque<>();
        for (Integer v = destino; v != null; v = previo.get(v)) {
            pilaRuta.addFirst(v);
        }

        ArrayList<Integer> ruta = new ArrayList<>(pilaRuta);
        return new ResultadoRuta(origen, destino, ruta, dist.get(destino));


    }
}
