package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            if(color == Color.ROJO){
                return "R{" +super.toString()+"}";

            } else return "N{" +super.toString()+"}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            return(color == vertice.color && super.equals(vertice));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        try {
            VerticeRojinegro verti = (VerticeRojinegro) vertice;
            return verti.color;
        } catch (ClassCastException e) {
            throw new ClassCastException("El vértice no es instancia de VerticeRojinegro");
        }
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);

        VerticeRojinegro nvert = (VerticeRojinegro) ultimoAgregado;

        nvert.color = Color.ROJO;

        rebalanceAdd(nvert);

    }

    private void rebalanceAdd(VerticeRojinegro v){
        //Caso 1
        if(v.padre == null){
            v.color = Color.NEGRO;
            return;

        //Caso 2
        } else if(this.getColor(v.padre) == Color.NEGRO){
            return;  
        
        //Caso 3
        } else if(dameTio(v)!=null && dameTio(v).color == Color.ROJO){
            VerticeRojinegro t = dameTio(v);
            VerticeRojinegro p = (VerticeRojinegro) v.padre;
            VerticeRojinegro abu = (VerticeRojinegro) p.padre;

            t.color = Color.NEGRO;
            p.color = Color.NEGRO;
            abu.color = Color.ROJO;

            rebalanceAdd(abu);

        } else {
            VerticeRojinegro p = (VerticeRojinegro) v.padre;
            VerticeRojinegro abu = (VerticeRojinegro) p.padre;
            
            //caso 4
            if(estanCruzados(v)){
                if(abu.izquierdo == p){
                    super.giraIzquierda(p);

                    p = (VerticeRojinegro) abu.izquierdo;

                    v = (VerticeRojinegro) p.izquierdo;

                } else {
                    super.giraDerecha(p);

                    p = (VerticeRojinegro) abu.derecho;

                    v = (VerticeRojinegro) p.derecho;

                }
            
            } 

            //Caso 5
            p.color = Color.NEGRO;
            abu.color = Color.ROJO;
            if(abu.izquierdo == p && p.izquierdo == v){  
                super.giraDerecha(abu);
            } else {
                super.giraIzquierda(abu);
            }
        }
    }

    private VerticeRojinegro dameTio(VerticeRojinegro v){
        Vertice p = v.padre;
        Vertice abu = p.padre;

        if(abu.derecho == p) return (VerticeRojinegro) abu.izquierdo;
        else return (VerticeRojinegro) abu.derecho;

    }

    private boolean estanCruzados(VerticeRojinegro v){
        Vertice p = v.padre;
        Vertice abu = p.padre;

        if(abu.izquierdo == p) return p.derecho == v;
        
        return p.izquierdo == v;
        
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        if(elemento == null) return;
        VerticeRojinegro hijo;
        VerticeRojinegro eliminar = (VerticeRojinegro) super.busca(elemento);

        if(eliminar == null) return;
        elementos--;

        if(eliminar.hayIzquierdo()){
            eliminar = (VerticeRojinegro) super.intercambiaEliminable(eliminar);

        }

        if(!eliminar.hayIzquierdo() && !eliminar.hayDerecho()){
            eliminar.izquierdo = (VerticeRojinegro) nuevoVertice(null);
        }

        hijo = sacaHijo(eliminar);

        super.eliminaVertice(eliminar);

        if(hijo.color == Color.ROJO){
            hijo.color = Color.NEGRO;

        } else if(!esNegro(eliminar)){

        } else if(esNegro(eliminar) && esNegro(hijo)){

            balanceoEliminar(hijo);
        }

        eliminaFantasma(hijo);
    }

    private VerticeRojinegro sacaHijo(VerticeRojinegro padre){
        if(padre.hayIzquierdo()) return (VerticeRojinegro) padre.izquierdo;
        else return (VerticeRojinegro) padre.derecho;
    }

    private boolean esNegro(VerticeRojinegro v){
        return v == null || v.color == Color.NEGRO || v.color == Color.NINGUNO;
    }

    private void eliminaFantasma(VerticeRojinegro elim){
        if(elim.elemento == null) elimHoja(elim);
        
    }

    private void elimHoja(Vertice elim){
        if(elim.hayPadre()){
            Vertice pa = elim.padre;

            if(pa.hayIzquierdo()){
                if(pa.izquierdo==elim){
                    pa.izquierdo = null;

                }else pa.derecho = null;
            } else {
                pa.derecho = null;
            }

        } else {
            this.raiz = null;
            this.ultimoAgregado = null;
        }
        return;
    }

    private void balanceoEliminar(VerticeRojinegro v){
        VerticeRojinegro pa = (VerticeRojinegro) v.padre;

        //Caso 1
        if(pa == null){
            return;
        } 

        //Caso 2
        VerticeRojinegro h = dameHermano(v);
        if(h.color == Color.ROJO){
            pa.color = Color.ROJO;
            h.color = Color.NEGRO;

            if(pa.izquierdo == v){
                super.giraIzquierda(pa);

            } else {
                super.giraDerecha(pa);

            }

            pa = (VerticeRojinegro) v.padre;
            h = dameHermano(v);
        }

        VerticeRojinegro hi = (VerticeRojinegro) h.izquierdo;
        VerticeRojinegro hd = (VerticeRojinegro) h.derecho;

        //Caso 3 y 4
        if(esNegro(h) && esNegro(hi) && esNegro(hd)){

            h.color = Color.ROJO;

            //Caso 3
            if(esNegro(pa)){
                balanceoEliminar(pa);
                return;

            //Caso 4
            } else {
                pa.color = Color.NEGRO;
                return;
            }

        }

        //Caso 5
        if(pa.izquierdo == v && !esNegro(hi) && esNegro(hd) || pa.derecho == v && esNegro(hi) && !esNegro(hd)){
            h.color = Color.ROJO;
            if(esNegro(hi)) hd.color = Color.NEGRO;
            else hi.color = Color.NEGRO;

            if(pa.izquierdo == v) super.giraDerecha(h);
            else super.giraIzquierda(h);
            
            h = this.dameHermano(v);
            hi = (VerticeRojinegro) h.izquierdo;
            hd = (VerticeRojinegro) h.derecho;

        } 

        //Caso 6
        h.color = pa.color;
        pa.color = Color.NEGRO;

        if(pa.izquierdo == v){
            if(hd!=null) hd.color = Color.NEGRO;
            super.giraIzquierda(pa);
        } else {
            if(hi!=null) hi.color = Color.NEGRO;
            super.giraDerecha(pa);
        }
    }

    private VerticeRojinegro dameHermano(VerticeRojinegro v){
        Vertice p = v.padre;

        if(p.derecho == v) return (VerticeRojinegro) p.izquierdo;
        else return (VerticeRojinegro) p.derecho;
    }


    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
