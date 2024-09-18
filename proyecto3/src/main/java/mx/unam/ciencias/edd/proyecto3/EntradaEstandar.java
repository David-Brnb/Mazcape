package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import mx.unam.ciencias.edd.Lista;

public class EntradaEstandar {


    //Declaramos un constructor para poder hacer uso de la clase este revise las banderas ingresadas
    public EntradaEstandar(){
    }

    //Declaramos un método para leer la entrda estandar
    public String[] leerDesdeEntradaEstandar() {
        //Creamos un arbol rojinegro para almacenar las lineas de forma ordenada
        Lista<String> lstr = new Lista<>();
        String[] args = new String[0];

        /*
         * Intentamos instanciar un BufferedReader pasando como argumento InputStreamReader que a su vez tiene 
         * como argumento la entrada del sistema, asi como la codificación de dicha entrada.
         * En esta caso la entrada el sistema es tratada como un archivo, con lo cual se busca acceder al
         * archivo en cuestión. 
         */
        try(BufferedReader bdr = new BufferedReader(new InputStreamReader(System.in, "UTF-8"))) {
                //Mandamos nuestro BufferedReader a el objeto que trata la entrada de texto
                UsoTexto ustxt = new UsoTexto(bdr);
                
                // Guardamos la lista devuelta por el metodo procesarEntradaTexto que nos regresa la lista de lineas ordenadas.
                lstr = ustxt.procesarEntradaTexto();

                args = new String[lstr.getLongitud()];

                int j=0;
                for(String i: lstr){
                    args[j] = i;
                    j++;
                }
        
        // Se manda un error 
        } catch (IOException ioe) {
            System.out.print("Ocurrió un error en la entrada estándar");
            System.exit(1);
        }

        return args;

    }

}
