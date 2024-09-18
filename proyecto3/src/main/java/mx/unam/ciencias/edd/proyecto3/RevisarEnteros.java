package mx.unam.ciencias.edd.proyecto3;

public class RevisarEnteros {

    public RevisarEnteros(){}

    public void revisionEnteros(String args){
        Termina term = new Termina();

        for(int i=0; i<args.length(); i++){
            if(!(Integer.valueOf(args.charAt(i)) > 47 && Integer.valueOf(args.charAt(i)) < 58) && (Integer.valueOf(args.charAt(i)) !=45)) {
                term.terminaErr("Se paso algo que no es entero");
            }
            
        }

        for(int i=0; i<args.length(); i++){
            if(Integer.valueOf(args.charAt(i)) == 45) {
                if(i==args.length()-1 || !(Integer.valueOf(args.charAt(i+1)) > 47 && Integer.valueOf(args.charAt(i+1)) < 58)){
                    term.terminaErr("Se paso algo que no es entero");
                }

                if(i<args.length()-1 && i>0){
                    if((Integer.valueOf(args.charAt(i-1)) > 47 && Integer.valueOf(args.charAt(i-1)) < 58) && (Integer.valueOf(args.charAt(i+1)) > 47 && Integer.valueOf(args.charAt(i+1)) < 58))
                    term.terminaErr("Se paso algo que no es entero");
                }
            }
        }
    }
}
