package mx.unam.ciencias.edd.proyecto3;

public class Termina {
    public Termina(){}

    public void terminaErr(String error){
        System.out.println("\nArgumento incorrecto. "+ error +"\n" +
                            "Uso: java -jar proyecto3.jar <archivo>");
        System.exit(1);
    }

    public void terminaErr(String error, String argu){
        System.out.println("\n"+argu+"\nArgumento incorrecto. "+ error +"\n" +
                            "Uso: java -jar proyecto3.jar <archivo>");
        System.exit(1);
    }
    
}
