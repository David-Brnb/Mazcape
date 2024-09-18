package mx.unam.ciencias.edd.proyecto3;
// Clase para escribir laberintos

public class WriteLaberinto {
    private Casilla[][] laberinto;
    private int n;
    private int m;

    public WriteLaberinto(Casilla[][] lab, int n, int m){
        this.laberinto = lab;
        this.n = n;
        this.m = m;
    }

    public void escribe(){
        escribeEncabezado();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.flush();
                System.out.write((laberinto[i][j].getPeso()<<4) | laberinto[i][j].daCaja());
                System.out.flush();
            }
        }
    }

    private void escribeEncabezado(){
        System.out.flush();
        System.out.write((byte) 0x4d);
        System.out.write((byte) 0x41);
        System.out.write((byte) 0x5a);
        System.out.write((byte) 0x45);
        System.out.write((byte) n);
        System.out.write((byte) m);
        System.out.flush();
    }


}
