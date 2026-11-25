package com.example.grafos.ordenamientos;

import com.example.grafos.modelo.Arista;
import com.example.grafos.modelo.Vertice;

public class Ordenamientos {

    /*
     Ordena aristas de acuerdo al metodo Shell Sort, por peso de las aristas
     Itera sobre el arreglo haciendo comparaciones separadas por un salto que va disminuyendo
     */
    public void shellSortAristas(Arista[] aristas){
        int salto= aristas.length / 2; // primer salto es la mitad del arreglo
        while(salto > 0){
            // recorrer desde la posicion de salto hasta el final
            for(int i= salto; i < aristas.length; i++){
                Arista temporal= aristas[i]; //arista que se va a acomodar
                int j= i; // pos donde se va a colocar arista guardada

                // mientras la arista de atras pese mas
                while(j >= salto && aristas[j - salto].getPeso() > temporal.getPeso()){
                    aristas[j]= aristas[j - salto]; //recorre la arista de mayor peso hacia adelante
                    j= j - salto;
                }
                aristas[j]= temporal; //coloca la arista en su lugar
            }
            salto= salto / 2; //reduce el salto a la mitad
        }
    }

    /*
    Ordena de acuerdo al metodo Shell Sort, por nombre de los vertices
    Compara vertices separados por un salto que se va haciendo mas chico
     */
    public void shellSortVertices(Vertice[] vertices){
        int salto= vertices.length/2; //el primer salto es la mitad del arreglo

        while(salto>0){
            for(int i= salto; i < vertices.length; i++){
                Vertice temporal= vertices[i]; //guarda el vertice que se va a acomodar
                int j= i; //posicion donde se va a colocar el vertice guardado

                // mientras que todavia haya vertice atras para comparar, y su nombre vaya despues del que se esta acomodando
                while(j>=salto && vertices[j-salto].getNombre().compareToIgnoreCase(temporal.getNombre()) > 0){ // > 0 el de atras va despues alfabeticamente
                    vertices[j]= vertices[j - salto]; //recorre el vertice hacia adelante
                    j= j - salto; //retrocede un salto para seguir comparando
                }
                vertices[j]= temporal; //coloca el vertice guardado en su lugar
            }
            salto= salto / 2; // reducir salto a la mitad
        }
    }

    /*
    Ordena las aristas de menor a mayor peso usando QuickSort.
     */
    public void quickSortAristas(Arista[] aristas){
        quickSortAristas(aristas, 0, aristas.length - 1); //ordena desde la primera hasta la ultima posicion
    }

    /*
    Toma el pivote como el centro. Ordena del lado izquierdo luego derecho.
     */
    private void quickSortAristas(Arista[] aristas, int inicio, int fin){
        if(inicio >= fin){ //si hay una arista o ninguna, ya esta ordenado
            return;
        }
        int medio= (inicio+fin) / 2; //posicion del centro
        intercambiar(aristas, inicio, medio); // pasa el pivote a la primera posicion
        Arista pivote= aristas[inicio]; // la guarda como pivote
        int izq= inicio+1; //busca de izquierda a derecha, se salta el pivote al inicio
        int der= fin; //busca de derecha a izquierda

        while(izq <= der){ //hasta que las busquedas se crucen
            while(izq <= der && aristas[izq].getPeso() < pivote.getPeso()){ //avanza mientras pese menos que el pivote
                izq++;
            }
            while(izq <= der && aristas[der].getPeso() > pivote.getPeso()){ //retrocede mientras pese mas que el pivote
                der--;
            }
            if(izq <= der){ //encontro una de cada lado que estan en el lado equivocado
                intercambiar(aristas, izq, der);
                izq++;
                der--;
            }
        }
        intercambiar(aristas, inicio, der); //coloca el pivote en su lugar final, entre los menores y los mayores
        quickSortAristas(aristas, inicio, der - 1); //ordena el lado izquierdo
        quickSortAristas(aristas, der + 1, fin); //ordena el lado derecho
    }

    /*
    Intercambia de lugar dos aristas del arreglo
     */
    private void intercambiar(Arista[] aristas, int i, int j){
        Arista temporal= aristas[i]; //guarda la primera
        aristas[i]= aristas[j]; //pone la segunda en el lugar de la primera
        aristas[j]= temporal; //pone la primera en el lugar de la segunda
    }

    /*
    Ordena los vertices alfabeticamente usando nombre con el metodo Quick Sort
     */
    public void quickSortVertices(Vertice[] vertices){
        quickSortVertices(vertices, 0, vertices.length - 1); //ordena desde la primera hasta la ultima posicion
    }

    /* Elige un pivote, pasa los vertices con nombre anterior a su izquierda y los de nombre posterior a su derecha,
       y despues ordena cada lado de la misma forma */
    private void quickSortVertices(Vertice[] vertices, int inicio, int fin){
        if(inicio >= fin){ //si hay un vertice o ninguno ya esta ordenado
            return;
        }
        int medio= (inicio + fin) / 2; //posicion del centro
        intercambiar(vertices, inicio, medio); //el pivote es el del centro y se pasa al inicio
        Vertice pivote= vertices[inicio]; //vertice con el que se compara
        int izq= inicio + 1; //busca de izquierda a derecha
        int der= fin; //busca de derecha a izquierda

        while(izq <= der){ //hasta que las busquedas se crucen
            while(izq <= der && vertices[izq].getNombre().compareToIgnoreCase(pivote.getNombre()) < 0){ //avanza mientras el nombre vaya antes que el pivote
                izq++;
            }
            while(izq <= der && vertices[der].getNombre().compareToIgnoreCase(pivote.getNombre()) > 0){ //retrocede mientras el nombre vaya despues que el pivote
                der--;
            }
            if(izq <= der){ //encontro uno de cada lado que estan en el lado equivocado
                intercambiar(vertices, izq, der);
                izq++;
                der--;
            }
        }
        intercambiar(vertices, inicio, der); //coloca el pivote en su lugar final
        quickSortVertices(vertices, inicio, der - 1); //ordena el lado izquierdo
        quickSortVertices(vertices, der + 1, fin); //ordena el lado derecho
    }

    /* Intercambia de lugar dos vertices del arreglo */
    private void intercambiar(Vertice[] vertices, int i, int j){
        Vertice temporal= vertices[i]; //guarda el primero
        vertices[i]= vertices[j]; //pone el segundo en el lugar del primero
        vertices[j]= temporal; //pone el primero en el lugar del segundo
    }


}
