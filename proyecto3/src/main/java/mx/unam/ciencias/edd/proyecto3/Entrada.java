/*
 * Esta clase esta hecha para el tratamiento de la entrada normal
 * En esta clase se tratan los casos de las banderas -g, -s, -w, -h
 */

package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedInputStream;

public class Entrada {
    //Declaramos las variables que se utilizaran a lo largo del tratamiento de la entrada
    private String[] args;
    private Termina term = new Termina();

    //Se realiza un constructor para la entrada y el argumento de args
    public Entrada(String[] args){
        this.args = args;
    }

    /* 
    Funcion que realiza el tratamiento de la entrada
    Aqui se validan las entradas dependeindo de si se trata de un archivo, o un texto
    así mismo, dentro de esta función revisamos 
    */ 

    public void tratamientoEntrada() {

        if(!(args.length > 0)){
            Laberinto lb = new Laberinto();
            lb.construyeLaberinto(new BufferedInputStream(System.in));
            System.out.println(lb.graficaLaberinto());
          
        }

        if(args.length>0){

            if(args[0].compareTo("-g") == 0){
                RevisarEnteros re = new RevisarEnteros();
                int i = 1, s=0, w=0, h=0;
                boolean semilla=false;

                if(args[i].compareTo("-s")==0){
                    i++;
                    re.revisionEnteros(args[i]);
                    s = Integer.parseInt(args[i]);
                    semilla = true;
                    i++;
                }

                if(args[i].compareTo("-w")==0){
                    i++;
                    re.revisionEnteros(args[i]);
                    w = Integer.parseInt(args[i]);
                    i++;
                }
                
                if(args[i].compareTo("-h")==0){
                    i++;
                    re.revisionEnteros(args[i]);
                    h = Integer.parseInt(args[i]);
                    i++;
                }

                //revisar que h o w no sean 0, y checar las dimensiones
                if(h<2 || w<2 || w>255 || h>255){
                    term.terminaErr("Las dimensiones del laberinto no son adecuadas, deben ir de 2 a 255");
                }

                if(semilla){
                    Laberinto lb = new Laberinto(h, w, s);
                    lb.construyeLaberinto();
                    lb.writeLab();

                } else {
                    Laberinto lb = new Laberinto(h, w);
                    lb.construyeLaberinto();
                    lb.writeLab();
                }

            } else {
                term.terminaErr("En la petición inicial", "Solo puedes ingresar -g para generar, < para abrir un .mze, y > para dirigir.");
            }

        } 
        
    } 
}
