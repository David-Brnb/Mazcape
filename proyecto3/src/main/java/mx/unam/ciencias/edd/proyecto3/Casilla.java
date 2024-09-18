package mx.unam.ciencias.edd.proyecto3;

import java.util.Random;

/**
 * Clase para Casillas del laberintos
 */
public class Casilla {
    private Direccion N = Direccion.NORTE, E = Direccion.ESTE, S = Direccion.SUR, O = Direccion.OESTE;
    private Random random = new Random();
    private int x, y;
    private byte peso;

    private boolean visitada;
    private boolean inTrayectoria;

    /**
     * Construye una Casilla a partir de las cordenadas x, y.
     * Define las demas variables de forma conveniente. 
     * @param x la coordenada x de la Casilla.
     * @param y la coordenada y de la Casilla. 
     */
    public Casilla(int x, int y){
        this.x = x; 
        this.y = y;
        this.visitada = false;
        this.inTrayectoria = false;
        this.peso = ((byte) (1 + random.nextInt(11)));
    }

    public Casilla(int x, int y, int peso, int paredes){
        this.x = x; 
        this.y = y;
        this.visitada = false;
        this.inTrayectoria = false;
        this.peso = (byte) peso;
        if((paredes & Direccion.NORTE.getCodigo()) == 0){
            N = Direccion.ABIERTA;
        }
        if((paredes & Direccion.SUR.getCodigo()) == 0){
            S = Direccion.ABIERTA;
        }
        if((paredes & Direccion.ESTE.getCodigo()) == 0){
            E = Direccion.ABIERTA;
        }
        if((paredes & Direccion.OESTE.getCodigo()) == 0){
            O = Direccion.ABIERTA;
        }
        
    }

    public boolean fueVisitada(){
        return visitada;
    }

    public boolean inTrayectoria(){
        return inTrayectoria;
    }

    public byte getPeso(){
        return peso;
    }

    public Direccion getN(){
        return N;
    }

    public Direccion getO(){
        return O;
    }

    public Direccion getE(){
        return E;
    }

    public Direccion getS(){
        return S;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setPeso(byte p){
        this.peso = p;
    }

    public void visitar(boolean v){
        this.visitada = v;
    }

    public void setN(Direccion d){
        this.N = d;
    }

    public void setE(Direccion d){
        this.E = d;
    }

    public void setO(Direccion d){
        this.O = d;
    }
    
    public void setS(Direccion d){
        this.S = d;
    }

    public void pasaTrayectoria(boolean p){
        this.inTrayectoria = p;
    }
    
    
    public String toStringTop(){
        String str="";
        
        str += (N == Direccion.NORTE)? "###   " : "# #   ";
        
        return str;
    }

    public String toStringMed(){
        String str="";
        str += (O== Direccion.OESTE)? "# " : "  ";
        str += (E== Direccion.ESTE)? "#   " : "    ";
        
        return str;
    }

    public String toStringBot(){
        String str = "";
        str += (S== Direccion.SUR)? "###   " : "# #   ";
        
        return str;
    }

    public String toString(){
        String str = "";
        str += "("+x + ", " + y+") ";
        
        return str;
    }

    public byte daCaja(){
        return (byte) (N.getCodigo() | E.getCodigo() | S.getCodigo() | O.getCodigo());
    }
}
