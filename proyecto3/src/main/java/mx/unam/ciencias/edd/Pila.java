package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        String str = "";

        Nodo temp = cabeza; 

        while(temp!=null){
            str+=temp.elemento + "\n";
            temp = temp.siguiente;
        }

        return str;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if(elemento == null) throw new IllegalArgumentException("El elemento ingresado es vacío");
        Nodo n = new Nodo(elemento);

        if(esVacia()){
            cabeza = n; 
            rabo = n;

        } else {
            n.siguiente = cabeza; 
            cabeza = n;
        }
    }
}
