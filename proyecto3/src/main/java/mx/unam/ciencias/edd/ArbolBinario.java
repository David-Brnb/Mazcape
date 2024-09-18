package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        protected T elemento;
        /** El padre del vértice. */
        protected Vertice padre;
        /** El izquierdo del vértice. */
        protected Vertice izquierdo;
        /** El derecho del vértice. */
        protected Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        protected Vertice(T elemento) {
            this.elemento = elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <code>true</code> si el vértice tiene padre,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayPadre() {
            return padre != null;
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <code>true</code> si el vértice tiene izquierdo,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            return izquierdo != null;
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <code>true</code> si el vértice tiene derecho,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayDerecho() {
            return derecho != null;
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            if(hayPadre()) return padre;
            else throw new NoSuchElementException("El vertice no tiene padre");
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            if(hayIzquierdo()) return izquierdo;
            else throw new NoSuchElementException("El vertice no tiene izquierdo");
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            if(hayDerecho()) return derecho;
            else throw new NoSuchElementException("El vertice no tiene derecho");
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            if(!hayIzquierdo() && !hayDerecho()) return 0;

            else if(!hayDerecho() && hayIzquierdo()) return 1 + izquierdo.altura();

            else if(hayDerecho() && !hayIzquierdo()) return 1 + derecho.altura();

            else return 1 + Math.max(izquierdo.altura(), derecho.altura());
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            if(!hayPadre()) return 0;
            
            return 1 + padre.profundidad();
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            return elemento;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;

            if(!this.elemento.equals(vertice.elemento)) 
                return false;

            if((this.izquierdo != null && vertice.izquierdo != null) && (this.derecho != null && vertice.derecho != null)) 
                return this.derecho.equals(vertice.derecho) && this.izquierdo.equals(vertice.izquierdo);

            if((this.izquierdo != null && vertice.izquierdo == null) || (this.izquierdo == null && vertice.izquierdo != null)) 
                return false;
            
            if((this.derecho != null && vertice.derecho == null) || (this.derecho == null && vertice.derecho != null)) 
                return false;
            
            if(this.izquierdo == null && vertice.izquierdo == null){
                if(this.derecho == null && vertice.derecho == null)
                    return true;
                else return this.derecho.equals(vertice.derecho);

            } else if(this.derecho == null && vertice.derecho == null)
                return this.izquierdo.equals(vertice.izquierdo);

            return true;
        }

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        @Override public String toString() {
            return this.elemento.toString();
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        for( T i : coleccion){
            agrega(i);
        }
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        return new Vertice(elemento);
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        if(raiz == null) return -1;
        else return raiz.altura();
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return busca(elemento)!=null;
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <code>null</code>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <code>null</code> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        return buscar(raiz, elemento);

    }

    private VerticeArbolBinario<T> buscar(VerticeArbolBinario<T> raiz, T elemento){
        if(raiz==null) return null;

        if(raiz.get().equals(elemento)) return raiz;
        
        VerticeArbolBinario<T> ved = raiz.hayDerecho() ? buscar(raiz.derecho(), elemento) : null;
        if(ved!= null) return ved;
        else return raiz.hayIzquierdo() ? buscar(raiz.izquierdo(), elemento) : null;
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        if(raiz != null) return raiz;
        else throw new NoSuchElementException("No esta el elemento");
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return raiz == null;
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        raiz = null;
        elementos = 0;
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;
            
            if(raiz == null) return true;
            
            return raiz.equals(arbol.raiz);
    }

    private String dibujaEspacios(int niv, int[] arr){
        String str = "";

        for(int i=0; i<=niv-1; i++){
            if(arr[i] == 1){
                str +="│  ";

            } else str+="   ";
        }

        return str;
    }

    private String toString(Vertice v, int niv, int[] arr){
        String str = v.toString() + "\n";

        arr[niv] = 1;
        if(v.izquierdo != null && v.derecho!=null){
            str+=dibujaEspacios(niv, arr);
            str+="├─›";
            str+=toString(v.izquierdo, niv+1, arr);
            str+=dibujaEspacios(niv, arr);
            str+="└─»";
            arr[niv]=0;
            str+=toString(v.derecho, niv+1, arr);

        } else if(v.izquierdo != null){
            str+=dibujaEspacios(niv, arr);
            str+="└─›";
            arr[niv]=0;
            str+=toString(v.izquierdo, niv+1, arr);

        } else if(v.derecho != null){
            str+=dibujaEspacios(niv, arr);
            str+="└─»";
            arr[niv]=0;
            str+=toString(v.derecho, niv+1, arr);
        }

        return str;

    }

    private String toString(ArbolBinario<T> t){
        if(t.raiz==null) return "";

        int[] arr = new int[t.altura()+1];
        
        return toString(t.raiz, 0, arr);
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        return toString(this);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }
}
