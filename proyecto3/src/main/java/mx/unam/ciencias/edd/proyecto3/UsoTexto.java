package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedReader;
import java.io.IOException;
import mx.unam.ciencias.edd.Lista;

public class UsoTexto {
    private BufferedReader br;
    private static Lista<String> list = new Lista<>();

    // Se genera un constructor vació
    public UsoTexto(){
        this.br = null;
    }

    //Se crea un constructor que toma un Buffered Reader como argumento
    // este Buffered Reader nos permitirá recorrer linea por linea el texto
    public UsoTexto(BufferedReader brn){
        this.br = brn;
    }



    // Se define el metodo procesarEntradaTexto el cual procesara la entrada de texto mediante el BufferedReader
    // las lineas se van agregando y ordenando mediante un arbol binario rojinegro 
    /*
     * @return regrea una Lista<String> con las lineas ordenadas lexicográficamente
     */
    public Lista<String> procesarEntradaTexto() {
        try {
            String linea;
            while ((linea = br.readLine()) != null) {

                String[] arrS = linea.trim().split(" ");

                for(int i=0; i< arrS.length; i++){
                    //Remplazamos todos los espacios para no tomarlos como argumentos
                    arrS[i] = arrS[i].replaceAll(" ", "");
                    
                    //Si se encuentra # entonces se ignora todo lo que sigue despues
                    if(arrS[i].contains("#")) break;

                    
                    if(!arrS[i].equals("")) list.agrega(arrS[i]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al procesar la entrada de texto: " + e.getMessage());
        }

        return list;
    }
    
}
