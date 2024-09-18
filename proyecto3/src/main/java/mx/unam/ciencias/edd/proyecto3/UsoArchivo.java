package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import mx.unam.ciencias.edd.Lista;

public class UsoArchivo {
    // Declaramos las variables de clase que se utilizarán
    private String nombreArchivo = "";
    private static Lista<String> list = new Lista<>();

    //Declaramos un constructor 
    public UsoArchivo(){
        this.nombreArchivo = "";
    }

    // Declaramos el contructor que toma como parametro el nombre del archivo
    public UsoArchivo(String nombreArchivo){
        this.nombreArchivo = nombreArchivo;
    }

    // Método que se encarga de leer el archivo y a su vez ordenar.
    public Lista<String> leerArchivo() {
        /*
         * Se hace uso del BufferedReader para leer todas las lineas del archivo y estas se van agregando al arbol binario
         * El arbol binario mantiene el orden gracias a que la clase de ABO fue modificada para ordenar de acuerdo
         * al Normalizador creado para este proyecto. 
         */
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                list.agrega(linea.trim());
            }
           
        //Si el archivo no se pudo leer se manda un error
        } catch (IOException e) {
            System.err.println("Error al leer el archivo " + nombreArchivo + ": " + e.getMessage());
        }

        //Se regresa la lista
        return list;
    }
    
}
