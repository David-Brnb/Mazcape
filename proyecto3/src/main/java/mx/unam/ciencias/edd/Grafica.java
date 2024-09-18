package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            this.iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        private T elemento;
        /* El color del vértice. */
        private Color color;
        /* La lista de vecinos del vértice. */
        private Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.color = Color.NINGUNO;
            vecinos = new Lista<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null || contiene(elemento)) throw new IllegalArgumentException("El elemento ya esta en la grafica o es nulo");

        Vertice nw = new Vertice(elemento);

        vertices.agrega(nw);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        Vertice c1 = (Vertice) vertice(a);
        Vertice c2 = (Vertice) vertice(b);

        if(c1.equals(c2) || sonVecinos(a,b)) throw new IllegalArgumentException("Es el mismo elemento o ya son vecinos");

        c1.vecinos.agrega(c2);
        c2.vecinos.agrega(c1);
        aristas++;

    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice c1 = (Vertice) vertice(a);
        Vertice c2 = (Vertice) vertice(b);
        if(!sonVecinos(a,b)) throw new IllegalArgumentException("No hay arista a desconectar");

        aristas--;
        c1.vecinos.elimina(c2);
        c2.vecinos.elimina(c1);

    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for(Vertice i : vertices){
            if(i.elemento.equals(elemento)) return true;
        }

        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice ve = (Vertice) vertice(elemento);

        vertices.elimina(ve);

        for(Vertice v: ve.vecinos){
            v.vecinos.elimina(ve);
            aristas--;
        }
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        Vertice c1 = (Vertice) vertice(a);
        Vertice c2 = (Vertice) vertice(b);

        boolean res1 = false, res2 = false;

        for(Vertice i : c1.vecinos){
            if(i.equals(c2)){
                res1 = true;
                break;
            }
        }

        for(Vertice i : c2.vecinos){
            if(i.equals(c1)){
                res2 = true;
                break;
            }
        }

        return res1 && res2;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        if(!contiene(elemento)) throw new NoSuchElementException("No esta el elemento en la grafica");

        for(Vertice i : vertices){
            if(i.elemento.equals(elemento)) return i;
        }

        return null;
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if(!vertice.getClass().equals(Vertice.class)) throw new IllegalArgumentException("Vertice invalido");
        Vertice r = (Vertice) vertice;

        r.color = color;
    }

    private int i = 0;
    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        // Vertice v = (Vertice) vertice(vertices.getPrimero().elemento);
        // for(Vertice vi: vertices){
        //     vi.color = Color.ROJO;
        // }

        // Cola<Vertice> qu = new Cola<>();

        // v.color = Color.NEGRO;
        // qu.mete(v);

        // while(!qu.esVacia()){
        //     Vertice ac = qu.saca();

        //     for(Vertice i: ac.vecinos){
        //         if(i.color== Color.ROJO) {
        //             i.color = Color.NEGRO;
        //             qu.mete(i);
        //         }
        //     }
        // }

        // for(Vertice vi: vertices){
        //     if(vi.color==Color.ROJO){
        //         res = false;
        //         break;
        //     } 
        // }

        // for(Vertice vi: vertices){
        //     vi.color = Color.NINGUNO;
        // }

        

        bfs(vertices.getPrimero().elemento, (a) -> i++);
        boolean res = i==vertices.getElementos();
        i=0;

        return res;

    }


    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice v: vertices) accion.actua(v);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = (Vertice) vertice(elemento);
        for(Vertice vi: vertices){
            vi.color = Color.ROJO;
        }

        Cola<Vertice> qu = new Cola<>();

        v.color = Color.NEGRO;
        qu.mete(v);

        while(!qu.esVacia()){
            Vertice ac = qu.saca();

            accion.actua(ac);

            for(Vertice i: ac.vecinos){
                if(i.color== Color.ROJO) {
                    i.color = Color.NEGRO;
                    qu.mete(i);
                }
            }
        }

        for(Vertice vi: vertices){
            vi.color = Color.NINGUNO;
        }
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = (Vertice) vertice(elemento);
        for(Vertice vi: vertices){
            vi.color = Color.ROJO;
        }

        Pila<Vertice> pi = new Pila<>();

        v.color = Color.NEGRO;
        pi.mete(v);

        while(!pi.esVacia()){
            Vertice ac = pi.saca();

            accion.actua(ac);

            for(Vertice i: ac.vecinos){
                if(i.color== Color.ROJO) {
                    i.color = Color.NEGRO;
                    pi.mete(i);
                }
            }
        }

        for(Vertice vi: vertices){
            vi.color = Color.NINGUNO;
        }
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas=0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        //Jala pero esta bien? 
        String str = "{";
        for(Vertice v: vertices){
            str+= v.elemento + ", ";
        }

        str+="}, {";

        for(Vertice v: vertices){
            for(Vertice i: v.vecinos){
                if(i.color!=Color.NEGRO){
                    str+="(" + v.elemento + ", " + i.elemento + "), ";
                }
            }
            v.color = Color.NEGRO;
        }

        str+="}";

        for(Vertice v: vertices)
            v.color = Color.NINGUNO;
        

        return str;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        
        if (objeto == null || getClass() != objeto.getClass())
            return false;

        @SuppressWarnings("unchecked")
        Grafica<T> otra = (Grafica<T>) objeto;

        if (this.vertices.getLongitud() != otra.vertices.getLongitud() || this.aristas != otra.getAristas()) 
            return false;

        for(Vertice v: vertices){
            if(!otra.contiene(v.elemento)) return false;

            for(Vertice i: v.vecinos){
                if(!otra.sonVecinos(v.elemento, i.elemento)) return false;
            }
        }

        return true;
    }


    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}