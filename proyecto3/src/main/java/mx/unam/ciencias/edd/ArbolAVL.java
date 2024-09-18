package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            return elemento.toString() + " " + altura + "/" + balance(this);
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;

            return (altura == vertice.altura && super.equals(objeto));
        }
        
        private int alturaSuper(){
            return super.altura();
        }
    }

    private int balance(Vertice v){
        
        if(v.izquierdo==null && v.derecho==null) return 0;
        else if(v.izquierdo==null && v.derecho!=null) return -1-casteador(v.derecho).altura;
        else if(v.izquierdo!=null && v.derecho==null) return  casteador(v.izquierdo).altura-(-1);
        else return casteador(v.izquierdo).altura-casteador(v.derecho).altura;
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);

        balanceador((VerticeAVL) ultimoAgregado.padre);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        Vertice padre = null;
        Vertice elim = vertice(super.busca(elemento));
        if(elim!=null) padre = elim.padre;

        if(elim!=null && elim.hayDerecho() && elim.hayIzquierdo()){
            elim  = findIntercambiaEliminable(elim);
            padre = elim.padre;
            
        }

        super.elimina(elemento);

        balanceador((VerticeAVL) padre);
    }

    
    private Vertice findIntercambiaEliminable(Vertice vertice) {
        Vertice temp = vertice.izquierdo;

        while(temp.hayDerecho()){
            temp = temp.derecho;
        }

        return temp;
    }

    private VerticeAVL casteador(Vertice v){
        return (VerticeAVL) v;
    }

    private void recalculaAltura(VerticeAVL v){

        if(v.izquierdo== null && v.derecho==null) v.altura = 0;
        else if(v.izquierdo!= null && v.derecho==null) v.altura = casteador(v.izquierdo).altura+1;
        else if(v.izquierdo== null && v.derecho!=null) v.altura = casteador(v.derecho).altura+1;
        else v.altura = Math.max(casteador(v.izquierdo).altura, casteador(v.derecho).altura) + 1;

    }

    private void balanceador(VerticeAVL v){
        if(v==null) return;

        // v.altura = v.alturaSuper();
        recalculaAltura(v);

        if(balance(v)==-2){
            // System.out.println("Entra en -2");
            if(balance(v.derecho)>0){
                super.giraDerecha(v.derecho());
                recalculaAltura(casteador(v.derecho));
                recalculaAltura(casteador(v.derecho.derecho));
            }
            super.giraIzquierda(v);

            recalculaAltura(v);
            recalculaAltura(casteador(v.padre));

        }


        if(balance(v)==2){
            // System.out.println("Entra en 2");
            if(balance(v.izquierdo)<0){
                super.giraIzquierda(v.izquierdo());
                recalculaAltura(casteador(v.izquierdo));
                recalculaAltura(casteador(v.izquierdo.izquierdo));
            }
            super.giraDerecha(v);

            recalculaAltura(v);
            recalculaAltura(casteador(v.padre));

        }
        

        balanceador((VerticeAVL) v.padre);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
