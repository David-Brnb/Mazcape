package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase abtracta para estructuras lineales restringidas a operaciones
 * mete/saca/mira.
 */
public abstract class MeteSaca<T> {

    /**
     * Clase interna protegida para nodos.
     */
    protected class Nodo {
        /** El elemento del nodo. */
        public T elemento;
        /** El siguiente nodo. */
        public Nodo siguiente;

        /**
         * Construye un nodo con un elemento.
         * @param elemento el elemento del nodo.
         */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /** La cabeza de la estructura. */
    protected Nodo cabeza;
    /** El rabo de la estructura. */
    protected Nodo rabo;

    /**
     * Agrega un elemento al extremo de la estructura.
     * @param elemento el elemento a agregar.
     */
    public abstract void mete(T elemento);

    /**
     * Elimina el elemento en un extremo de la estructura y lo regresa.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T saca() {
        if(cabeza == null) throw new NoSuchElementException("La estructura es Vacía");

        T elem = cabeza.elemento;

        cabeza = cabeza.siguiente;
        if(cabeza==null) rabo = null;

        return elem;
    }

    /**
     * Nos permite ver el elemento en un extremo de la estructura, sin sacarlo
     * de la misma.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T mira() {
        if(cabeza == null) throw new NoSuchElementException("La estructura es Vacía");

        return cabeza.elemento;
    }

    /**
     * Nos dice si la estructura está vacía.
     * @return <code>true</code> si la estructura no tiene elementos,
     *         <code>false</code> en otro caso.
     */
    public boolean esVacia() {
        return cabeza==null;
    }

    /**
     * Compara la estructura con un objeto.
     * @param object el objeto con el que queremos comparar la estructura.
     * @return <code>true</code> si el objeto recibido es una instancia de la
     *         misma clase que la estructura, y sus elementos son iguales en el
     *         mismo orden; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;
        @SuppressWarnings("unchecked") MeteSaca<T> m = (MeteSaca<T>)object;

        Nodo temp1 = cabeza; 
        Nodo temp2 = m.cabeza;

        while(temp1 != null){
            if(temp1 == null || temp2 == null) return false;
            if(!(temp1.elemento.equals(temp2.elemento))) return false; 
            temp1 = temp1.siguiente;
            temp2 = temp2.siguiente;
        } 

        Nodo temp11 = m.cabeza; 
        Nodo temp21 = cabeza;

        while(temp11 != null){
            if(temp11 == null || temp21 == null) return false;
            if(!(temp11.elemento.equals(temp21.elemento))) return false; 
            temp11 = temp11.siguiente;
            temp21 = temp21.siguiente;
        } 

        return true;
    }
}
