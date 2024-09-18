package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import mx.unam.ciencias.edd.Lista;

public class EntradaArchivoN {
    private String nombreArchivo;

    public EntradaArchivoN(String in){
        this.nombreArchivo = in;
    }

    public String[] trataArchivo(){
        String[] argsA = new String[0];

        if(new java.io.File(nombreArchivo).isFile()){
                try (BufferedReader bdr = new BufferedReader(new FileReader(nombreArchivo))) {
                    //Mandamos nuestro BufferedReader a el objeto que trata la entrada de texto
                    UsoTexto ustxt = new UsoTexto(bdr);
                    
                    // Guardamos la lista devuelta por el metodo procesarEntradaTexto que nos regresa la lista de lineas ordenadas.
                    Lista<String> lstr = ustxt.procesarEntradaTexto();

                    argsA = new String[lstr.getLongitud()];

                    int j=0;
                    for(String i: lstr){
                        argsA[j] = i;
                        j++;
                    }
                    
                //Si el archivo no se pudo leer se manda un error
                } catch (IOException e) {
                    System.err.println("Error al leer el archivo " + nombreArchivo + ": " + e.getMessage());

                }

            } else {
                System.out.println("\nArgumento incorrecto o Faltaron argumentos\n" +
                                       "Uso: java -jar proyecto2.jar <archivo>");
                System.exit(1);
            }

        return argsA;
    }
}
