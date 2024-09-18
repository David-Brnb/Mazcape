package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        String str = "";

        Nodo temp = cabeza; 

        while(temp!=null){
            str = str + temp.elemento + ",";
            temp = temp.siguiente;
        }

        return str;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if(elemento == null) throw new IllegalArgumentException("El elemento ingresado es vacío");
        Nodo n = new Nodo(elemento);
        
        if(rabo == null){
            cabeza = n;
            rabo = n;

        } else {
            rabo.siguiente = n;
            rabo = n;
        }
    }
}
