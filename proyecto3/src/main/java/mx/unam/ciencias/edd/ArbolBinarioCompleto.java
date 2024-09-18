package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            cola = new Cola<>();
            if(raiz!=null) cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            Vertice ve = null;
            if(!cola.esVacia()){
                ve = cola.saca();
                if(ve.hayIzquierdo()) cola.mete(ve.izquierdo);
                if(ve.hayDerecho()) cola.mete(ve.derecho);
            } else {
                throw new NoSuchElementException("No hay más elementos para iterar");
            }
            return ve.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null) throw new IllegalArgumentException("El elemento es nulo");

        Vertice ve = nuevoVertice(elemento);
        elementos++;

        if(raiz==null) raiz = ve;
        else {

            //Aqui se implemento en O(log(n))
            Vertice tmp = raiz;
            String route = decimalBinario(elementos);
            for(int i=route.length()-2; i>0; i--){
                if(route.charAt(i)=='0'){
                    tmp = tmp.izquierdo;
                } else {
                    tmp = tmp.derecho;
                }
            }

            if(route.charAt(0)=='0'){
                tmp.izquierdo = ve;
                ve.padre = tmp;

            } else {
                tmp.derecho = ve;
                ve.padre = tmp;
            }
        }
    }

    private String decimalBinario(int num) { 
        String str = ""; 
        while (num > 0) { 
        if ((num & 1) == 1) str += '1'; 
        else str += '0'; 

        num >>= 1; //es como dividir entre 2 pero mas cabron
        } 
        return str; 
    } 

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice ve = vertice(busca(elemento));
        
        if(ve != null){
            elementos--;
            if(elementos == 0){
                raiz = null;

            } else {
                final Cola<Vertice> cola = new Cola<>();
                cola.mete(raiz);
                Vertice tmp = raiz;

                while(!cola.esVacia()){
                    tmp = cola.saca();
                    if(tmp.hayIzquierdo()) cola.mete(tmp.izquierdo);
                    if(tmp.hayDerecho())cola.mete(tmp.derecho);
                }

                T aux = ve.elemento;
                ve.elemento = tmp.elemento;
                tmp.elemento = aux;

                Vertice p = tmp.padre;
                if(p == null) tmp = null;
                else {
                    if(p.izquierdo().equals(tmp)) p.izquierdo = null;
                    else p.derecho = null;
                }
            
            }
                    
        }
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        if(raiz==null) return -1;
        else return log2(elementos);
    }

    public static int log2(int N) {
        int result = (int)(Math.log(N) / Math.log(2));

        return result;
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        final Cola<Vertice> cola = new Cola<>();
        cola.mete(raiz);
        
        while(!cola.esVacia()){
            VerticeArbolBinario<T> temp = cola.saca();
            
            accion.actua(temp);

            if(temp.hayIzquierdo()) cola.mete(vertice(temp.izquierdo()));
            if(temp.hayDerecho()) cola.mete(vertice(temp.derecho()));
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
