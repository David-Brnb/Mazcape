package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
            pila = new Pila<>();
            Vertice temp = raiz;

            if(temp!=null) {
                pila.mete(temp);

                while(temp.hayIzquierdo()){
                    temp = temp.izquierdo;
                    if(temp!=null) pila.mete(temp);
                }
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice ve = null;
            if(!pila.esVacia()){
                ve = pila.saca();
                if(ve.hayDerecho()){
                    Vertice temp = ve.derecho;
                    if(temp!=null) pila.mete(temp);

                    while(temp.hayIzquierdo()){
                        temp = temp.izquierdo;
                        if(temp!=null) pila.mete(temp);

                    }

                }
            } else {
                throw new NoSuchElementException("No hay más elementos para iterar");
            }
            return ve.elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null) throw new IllegalArgumentException("El elemento es nulo");
        Vertice nuevo  = nuevoVertice(elemento);
        elementos++;
        ultimoAgregado = nuevo;

        if(raiz==null){
            raiz = nuevo;

        } else {
            agregaAux(raiz, nuevo);
        }
    }

    private void agregaAux(Vertice actual, Vertice nuevo) {
        if(nuevo.elemento.compareTo(actual.elemento)<=0){
            if(!actual.hayIzquierdo()){
                actual.izquierdo = nuevo;
                nuevo.padre = actual;
            } else{
                agregaAux(actual.izquierdo, nuevo);
            }

        } else {
            if(!actual.hayDerecho()){
                actual.derecho = nuevo;
                nuevo.padre = actual;
            } else{
                agregaAux(actual.derecho, nuevo);
            }
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice elim = vertice(busca(elemento));

        if(elim!=null){
            elementos--;

            if(!elim.hayDerecho() && !elim.hayIzquierdo()){
                elimHoja(elim);
                return;
            }

            if(elim.hayDerecho() && !elim.hayIzquierdo() || !elim.hayDerecho() && elim.hayIzquierdo()){
                eliminaVertice(elim);

            } else {
                elim  = intercambiaEliminable(elim);
                
                if(!elim.hayDerecho() && !elim.hayIzquierdo()){
                    elimHoja(elim);
                    return;
                }
    
                if(elim.hayDerecho() && !elim.hayIzquierdo() || !elim.hayDerecho() && elim.hayIzquierdo()){
                    eliminaVertice(elim);
    
                }
            }
        }
    }

    private void elimHoja(Vertice elim){
        if(elim.hayPadre()){
            Vertice pa = elim.padre;

            if(pa.hayIzquierdo()){
                if(pa.izquierdo.equals(elim)){
                    pa.izquierdo = null;

                }else pa.derecho = null;
            } else {
                pa.derecho = null;
            }

        } else {
            if(elementos == 0) {
                raiz = null;
                return;
            }
            raiz = elim;
            raiz.padre = null;
        }
        return;
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice temp = vertice.izquierdo;

        while(temp.hayDerecho()){
            temp = temp.derecho;
        }

        T aux = vertice.elemento;
        vertice.elemento = temp.elemento;
        temp.elemento = aux;

        return temp;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        if(vertice.hayDerecho() && !vertice.hayIzquierdo()){
            if(vertice.hayPadre()){
                Vertice pa = vertice.padre;

                if(pa.hayIzquierdo()){
                    if(pa.izquierdo==vertice){
                        pa.izquierdo = vertice.derecho;
                    } else {
                        pa.derecho = vertice.derecho;
                    }

                } else {
                    pa.derecho = vertice.derecho;
                }

                vertice.derecho.padre = pa;

            } else {
                raiz = vertice.derecho;
                vertice.derecho.padre = null;
            }

        } else if(!vertice.hayDerecho() && vertice.hayIzquierdo()){
            if(vertice.hayPadre()){
                Vertice pa = vertice.padre;

                if(pa.hayIzquierdo()){
                    if(pa.izquierdo==vertice){
                        pa.izquierdo = vertice.izquierdo;
                    } else {
                        pa.derecho = vertice.izquierdo;
                    }

                } else {
                    pa.derecho = vertice.izquierdo;
                }

                vertice.izquierdo.padre = pa;

            } else {
                raiz = vertice.izquierdo;
                vertice.izquierdo.padre = null;
            }

        }
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return auxDfsBusqueda(raiz, elemento);
    }

    private VerticeArbolBinario<T> auxDfsBusqueda(Vertice actual, T elemento){
        if(actual==null) return null;

        if(actual.elemento.equals(elemento)) return actual;

        if(elemento.compareTo(actual.elemento) < 0){
            return auxDfsBusqueda(actual.izquierdo, elemento);

        } else {
            return auxDfsBusqueda(actual.derecho, elemento);

        }
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        if(!vertice.hayIzquierdo()) return;
        Vertice q = vertice(vertice);
        Vertice p = q.izquierdo;
        Vertice pa = q.padre;

        if(pa!=null){
            if(pa.hayIzquierdo()){
                if(pa.izquierdo==q){
                    pa.izquierdo = p;

                } else {
                    pa.derecho = p;
                }
            } else {
                pa.derecho = p;
            }
        } else {
            raiz = p;
        }

        p.padre = pa;
        if(p.derecho!=null){
            p.derecho.padre = q;
        }

        q.izquierdo = p.derecho;
        q.padre = p;
        p.derecho = q;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        if(!vertice.hayDerecho()) return;
        Vertice q = vertice(vertice);
        Vertice p = q.derecho;
        Vertice pa = q.padre;

        if(pa!=null){
            if(pa.hayIzquierdo()){
                if(pa.izquierdo==q){
                    pa.izquierdo = p;

                } else {
                    pa.derecho = p;
                }
            } else {
                pa.derecho = p;
            }
        } else {
            raiz = p;
        }

        p.padre = pa;
        if(p.izquierdo!=null){
            p.izquierdo.padre = q;
        }

        q.derecho = p.izquierdo;
        q.padre = p;
        p.izquierdo = q;
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        auxDfsPreO(raiz, accion);
    }

    private void auxDfsPreO(Vertice v, AccionVerticeArbolBinario<T> accion){
        if(v==null) return;

        accion.actua(v);
        auxDfsPreO(v.izquierdo, accion);
        auxDfsPreO(v.derecho, accion);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        auxDfsO(raiz, accion);
    }

    private void auxDfsO(Vertice v, AccionVerticeArbolBinario<T> accion){
        if(v==null) return;

        auxDfsO(v.izquierdo, accion);
        accion.actua(v);
        auxDfsO(v.derecho, accion);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        auxDfsPostO(raiz, accion);
    }

    private void auxDfsPostO(Vertice v, AccionVerticeArbolBinario<T> accion){
        if(v==null) return;

        auxDfsPostO(v.izquierdo, accion);
        auxDfsPostO(v.derecho, accion);
        accion.actua(v);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
