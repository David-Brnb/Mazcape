package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        quickSortAux(arreglo, 0, arreglo.length-1, comparador);
    }

    public static <T> void
    quickSortAux(T[] arreglo, int inicio, int fin, Comparator<T> comparador) {
        if(fin<=inicio) return;

        int i = inicio+1;
        int j= fin;
        while(i<j){
            if(comparador.compare(arreglo[i], arreglo[inicio]) > 0 && comparador.compare(arreglo[j], arreglo[inicio]) <= 0){
                intercambia(arreglo, i, j);
                i+=1;
                j-=1;

            } else if(comparador.compare(arreglo[i], arreglo[inicio]) <= 0){
                i+=1;

            } else {
                j-=1;
            }  
        }

        if(comparador.compare(arreglo[i], arreglo[inicio]) > 0){
            i-=1;
        }
        intercambia(arreglo, inicio, i);
        quickSortAux(arreglo, inicio, i-1, comparador);
        quickSortAux(arreglo, i+1, fin, comparador);
        
    }

    private static <T> void intercambia(T[] arr, int a, int b){
        T aux = arr[a];
        arr[a] = arr[b];
        arr[b] = aux;
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        for(int i=0; i<arreglo.length; i++){
            int min = i;

            for(int j=i; j<arreglo.length; j++){
                if(comparador.compare(arreglo[j], arreglo[min]) < 0){
                    min = j;
                } 
            }
            intercambia(arreglo, i, min);
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        return busquedaAux(arreglo, 0, arreglo.length-1, elemento, comparador);
    }

    private static <T> int busquedaAux(T[] arr, int a, int b, T i, Comparator<T> comparador){
        if(a>b) return -1;

        int m = (a+b)/2;
        if(comparador.compare(arr[m], i) == 0) return m;
        else if(comparador.compare(i, arr[m]) < 0) return busquedaAux(arr, a, m-1, i, comparador);
        else if(comparador.compare(i, arr[m]) > 0) return busquedaAux(arr, m+1, b, i, comparador);

        return 0;
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
