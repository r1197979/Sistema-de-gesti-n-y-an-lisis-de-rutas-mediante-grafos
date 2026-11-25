package com.example.grafos.algoritmos;

import com.example.grafos.modelo.Arista;
import com.example.grafos.modelo.Grafo;

import java.util.*;

/*
Algoritmos de búsqueda BFS y DFS
Ambos devuelven el orden en que se visitan los vértices a partir de un vértice inicial
 */

public class RecorridosGrafo {
    public static ArrayList<Integer> bfs(Grafo grafo, int inicio){
        validarInicio(grafo, inicio);

        ArrayList<Integer> orden = new ArrayList<>();
        HashSet<Integer> visitados = new HashSet<>();
        Queue<Integer> cola = new ArrayDeque<>();

        visitados.add(inicio);
        cola.add(inicio);

        while (!cola.isEmpty()){
            int actual = cola.poll();
            orden.add(actual);

            for (Arista a : grafo.obtenerAristasDesde(actual)){
                int vecino = a.getDestino();
                if (visitados.add(vecino)){
                    cola.add(vecino);
                }
            }
        }
        return orden;
    }

    /*
    DFS iterativo con pila para evita StackOverflowError
     */
    public static ArrayList<Integer> dfs(Grafo grafo, int inicio) {
        validarInicio(grafo, inicio);

        ArrayList<Integer> orden = new ArrayList<>();
        HashSet<Integer> visitados = new HashSet<>();
        Deque<Integer> pila = new ArrayDeque<>();

        pila.push(inicio);

        while (!pila.isEmpty()) {
            int actual = pila.pop();
            if (!visitados.add(actual)) {
                continue;
            }
            orden.add(actual);

            List<Arista> adyacentes = grafo.obtenerAristasDesde(actual);
            for (int i = adyacentes.size() - 1; i >= 0; i--) {
                int vecino = adyacentes.get(i).getDestino();
                if (!visitados.contains(vecino)) {
                    pila.push(vecino);
                }
            }
        }
            return orden;
    }

        private static void validarInicio (Grafo grafo,int inicio){
            if (grafo == null) {
                throw new IllegalArgumentException("El grafo no puede ser nulo");
            }
            if (!grafo.existeVertice(inicio)) {
                throw new IllegalArgumentException("El vértice inicial no existe en el grafo: " + inicio);
            }
        }
}

