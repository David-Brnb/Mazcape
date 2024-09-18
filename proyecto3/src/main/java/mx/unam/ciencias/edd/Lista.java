package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            start();
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente!=null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if(hasNext()){
                anterior = siguiente; 
                siguiente = siguiente.siguiente;
                return anterior.elemento;

            } else throw new NoSuchElementException("No hay elemento siguiente.");
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior!=null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (hasPrevious()) {
                siguiente = anterior;
                anterior = anterior.anterior;
                return siguiente.elemento;
            } else throw new NoSuchElementException("No hay elemento anterior.");
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            this.anterior = null;
            this.siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            this.anterior = rabo;
            this.siguiente = null; 
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return getElementos();
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return cabeza == null;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) throw new IllegalArgumentException("La lista no recibe elementos null");

        Nodo nuevo = new Nodo(elemento);
        longitud++;

        if(esVacia()){
            cabeza = nuevo;
            rabo = nuevo;

        } else {
            rabo.siguiente = nuevo;
            nuevo.anterior = rabo;
            rabo = nuevo;  
        }
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        if (elemento == null) throw new IllegalArgumentException("elemento no puede ser nulo");

        agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if (elemento == null) throw new IllegalArgumentException("elemento no puede ser nulo");

        Nodo nuevo = new Nodo(elemento);

        longitud++;
        if(esVacia()){
            cabeza = nuevo;
            rabo = nuevo;

        } else {
            cabeza.anterior = nuevo;
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        }
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if (elemento == null) throw new IllegalArgumentException("elemento no puede ser nulo");

        if(i<1){
            agregaInicio(elemento);

        } else if(i>longitud-1){
            agregaFinal(elemento);

        } else {
            longitud++;
            Nodo elementoi = iesimoI(i);
            Nodo nuevo = new Nodo(elemento);

            nuevo.anterior = elementoi.anterior;
            elementoi.anterior.siguiente = nuevo;

            nuevo.siguiente = elementoi;
            elementoi.anterior = nuevo;
        }
    }

    private Nodo iesimoI(int i){
        T n;

        Iterador iteradorsito = new Iterador();
        iteradorsito.start();

        for(int j=0; j<i; j++){
            n = iteradorsito.next();
        }
        return iteradorsito.siguiente;

    }


    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Nodo eliminado = buscarNodo(elemento);

        if(eliminado != null) {
            longitud--;

            if(longitud == 0){
                cabeza = null;
                rabo = null;

            } else if(eliminado.anterior == null){
                Nodo n = cabeza;

                if(cabeza.siguiente == null){
                    cabeza = null;
                    rabo = null;
                } else {
                
                    cabeza = cabeza.siguiente;
                    cabeza.anterior = null;
                    n.anterior = null;
                }

            } else if(eliminado.siguiente == null){
                Nodo n = rabo;

                if(rabo.anterior == null){
                    cabeza = null;
                    rabo = null;
        
                } else {
                    rabo = rabo.anterior;
                    rabo.siguiente = null;
                    n.anterior = null;
                }

            } else {
                eliminado.anterior.siguiente = eliminado.siguiente;
                eliminado.siguiente.anterior = eliminado.anterior;
            }
        }
    }

    private Nodo buscarNodo(T elemento){
        T n;

        Iterador iteradorsito = new Iterador();
        iteradorsito.start();

        while(iteradorsito.hasNext()){
           n = iteradorsito.next();
            if(n.equals(elemento)) return iteradorsito.anterior;
        }

        return null;

    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if(cabeza == null) throw new NoSuchElementException("No se puede el elemento de una lista vacia");

        longitud--;

        Nodo n = cabeza;

        if(cabeza.siguiente == null){
            cabeza = null;
            rabo = null;
        } else {
        
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
            n.anterior = null;
        }

        return n.elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if(longitud == 0) throw new NoSuchElementException("No se puede el elemento de una lista vacia");

        longitud--;
        
        Nodo n = rabo;

        if(rabo.anterior == null){
            cabeza = null;
            rabo = null;

        } else {
            rabo = rabo.anterior;
            rabo.siguiente = null;
            n.anterior = null;
        }

        return n.elemento;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        T n; 

        Iterador iteradorsito = new Iterador();
        iteradorsito.start();

        while(iteradorsito.hasNext()){
           n = iteradorsito.next();
            if(n.equals(elemento)) return true;
        }

        return false; 
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> lista = new Lista<T>();
        Iterador iteradorsito = new Iterador();
        iteradorsito.end();

        while(iteradorsito.hasPrevious()){
            T n = iteradorsito.previous();
            lista.agrega(n);
        }

        return lista;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> lista = new Lista<T>();
        Iterador iteradorsito = new Iterador();
        iteradorsito.start();

        while(iteradorsito.hasNext()){
            T n = iteradorsito.next();
            lista.agrega(n);
        }

        return lista;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        longitud = 0;
        cabeza = null;
        rabo = null;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if(cabeza == null) throw new NoSuchElementException("No hay elementos en la lista");
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if(rabo == null) throw new NoSuchElementException("No hay elementos en la lista");

        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if(i < 0 || i>= longitud) throw new ExcepcionIndiceInvalido("El indice no esta dentro de las capacidades");

        Nodo elementoi = iesimoI(i);

        return elementoi.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        int cnt = 0;

        Iterador iteradorsito = new Iterador();
        iteradorsito.start();

        while(iteradorsito.hasNext()){
            T n = iteradorsito.next();
            if(n.equals(elemento)) return cnt;
            cnt++;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        String cadena = ""; //tiene que llevar private?

        if(esVacia()) cadena += "[]";
        else {
            Iterador iteradorsito = new Iterador();
            iteradorsito.start();
            T n = iteradorsito.next();

            cadena  += "[" + n.toString();

            while(iteradorsito.hasNext()){
                n = iteradorsito.next();
                cadena += ", " + n.toString();
            }

            cadena += "]";
        }

        return cadena; 
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        
        if(lista.longitud != longitud) return false;
        else {
            Nodo temp1 = cabeza; 
            Nodo temp2 = lista.cabeza;

            while(temp1 != null){
                if(!(temp1.elemento.equals(temp2.elemento))) return false; 
                temp1 = temp1.siguiente;
                temp2 = temp2.siguiente;
            } 

        }

        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {

        return mergeandoSort(this, comparador);
        // return null;
        
    }

    private Lista<T> mergeandoSort(Lista<T> list, Comparator<T> comparador){
        if(list.longitud == 0 || list.longitud == 1){
            return list;
        } 

        int size1;
        if(list.longitud%2!=0) {
            size1 = (list.longitud-1)/2;

        } else {
            size1 = list.longitud/2;
        }

        Lista<T> list1 = new Lista<T>();
        Lista<T> list2 = new Lista<T>();

        Nodo temp = list.cabeza;
        int cnt = 0;

        while(temp!=null){
            if(cnt<size1) list1.agrega(temp.elemento);
            else list2.agrega(temp.elemento);
            temp = temp.siguiente;
            cnt++;
        }

        list1 = mergeandoSort(list1, comparador);
        list2 = mergeandoSort(list2, comparador);


        return mezcla(list1, list2, comparador);
        // return list2;
        
    }

    private Lista<T> mezcla(Lista<T> l1, Lista<T> l2, Comparator<T> comparador){
        Lista<T> list = new Lista<T>();
        Nodo i = l1.cabeza;
        Nodo j = l2.cabeza;

        while(true){
            if(comparador.compare(i.elemento, j.elemento)<=0){
                list.agrega(i.elemento);
                i = i.siguiente;

                if(i==null){
                    while(j!=null){
                        list.agrega(j.elemento);
                        j = j.siguiente;
                    }

                    return list;
                }

            } else {
                list.agrega(j.elemento);
                j = j.siguiente;

                if(j==null){
                    while(i!=null){
                        list.agrega(i.elemento);
                        i = i.siguiente;
                    }
                    
                    return list;
                }
            }
        }
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        Nodo temp = cabeza;

        while(temp!=null){
            if(comparador.compare(temp.elemento, elemento)==0) return true;
            if(comparador.compare(temp.elemento, elemento)>0) return false;
            temp = temp.siguiente;
        }
        
        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
