package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return indice < elementos;
            // Aquí va su código.
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (elementos <= indice)
                throw new NoSuchElementException("No hay elemento siguiente.");

            indice++;
            return arbol[indice-1];
            
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return this.elemento.compareTo(adaptador.elemento);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100);
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        arbol = nuevoArreglo(n);

        for(T i: iterable){
            arbol[elementos] = i;
            i.setIndice(elementos);
            elementos++;
        }

        int index = (n/2) - 1;

        while(index>=0){
            acomodaAbajo(index);
            index--;
        }



    }

    private void acomodaAbajo(int i){
        int izq = 2 * i + 1;
        int der = 2 * i + 2;

        int min = i;
        if (izq < elementos && arbol[izq].compareTo(arbol[min]) < 0) min = izq;

        if (der < elementos && arbol[der].compareTo(arbol[min]) < 0) min = der;

        if (min != i) {
            intercambia(i, min);
            acomodaAbajo(min);
        }
    }

    private void intercambia(int a, int b){
        T aux = arbol[a];
        arbol[a]=arbol[b];
        arbol[b]=aux;
        arbol[a].setIndice(a);
        arbol[b].setIndice(b);
    }

    private void acomodaArriba(int id){
        if(id<=0) return;
        if(arbol[id].compareTo(arbol[((id-1)/2)]) >=0) return;

        intercambia(id, ((id-1)/2));
        acomodaArriba(((id-1)/2));
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if(elementos == arbol.length){
            T[] arbolAux = nuevoArreglo(elementos * 2);
            for (int i = 0; i < elementos; i++)
                arbolAux[i] = arbol[i];
            arbol = arbolAux;
        }

        arbol[elementos]=elemento;
        elemento.setIndice(elementos);
        elementos++;
        acomodaArriba(elementos - 1);

    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(this.esVacia()) throw new IllegalStateException("El monticulo es vacio");

        T aux = arbol[0];
        intercambia(0, elementos-1);
        aux.setIndice(-1);
        elementos--;
        acomodaAbajo(0);

        return aux;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        if(elemento.getIndice() < 0 || elemento.getIndice() >= elementos) 
            return;
        
        int auxI = elemento.getIndice();
        intercambia(auxI, elementos-1);
        arbol[elementos-1].setIndice(-1);
        elementos--;

        if(auxI < elementos)
            reordena(arbol[auxI]);

    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if(elemento.getIndice() < 0 || elemento.getIndice() >= elementos) 
            return false;

        return arbol[elemento.getIndice()].equals(elemento);
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        return elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        elementos = 0; 
        for(int i=0; i<elementos; i++){
            arbol[i].setIndice(-1);
        }
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        acomodaArriba(elemento.getIndice());
        acomodaAbajo(elemento.getIndice());
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if(i<0 || i>= elementos) throw new NoSuchElementException("El indice supera la capacidad");

        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String str = "";
        for(T i: arbol){
            str += i + ", ";
        }

        return str;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;

        if(elementos != monticulo.elementos) return false;

        for (int i = 0; i < elementos; i++)
            if (!arbol[i].equals(monticulo.arbol[i]))
                return false;

        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        Lista<Adaptador<T>> la = new Lista<>();

        for(T e: coleccion){
            la.agrega(new Adaptador<T>(e));
        }

        Lista<T> lc = new Lista<>();

        MonticuloMinimo<Adaptador<T>> ma = new MonticuloMinimo<>(la);

        while(!ma.esVacia()){
            lc.agrega(ma.elimina().elemento);
        }

        



        return lc;
    }
}
