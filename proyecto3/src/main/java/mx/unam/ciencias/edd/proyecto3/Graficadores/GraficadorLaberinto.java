package mx.unam.ciencias.edd.proyecto3.Graficadores;

import mx.unam.ciencias.edd.proyecto3.Casilla;
import mx.unam.ciencias.edd.proyecto3.Direccion;

public class GraficadorLaberinto extends Graficador {
    private Casilla[][] laberinto;
    private int n;
    private int m;

    public GraficadorLaberinto(Casilla[][] l, int n, int m){
        super(n*50+20, m*50+20);
        this.laberinto = l;
        this.n = n;
        this.m = m;
    }

    public String grafica(int x1, int y1, int x2, int y2){
        StringBuilder str = new StringBuilder();
        str.append(this.encabezado());
        int anchoInicial = 10;
        int altoInicial = 10;
        int paso = 50;

        for (int i = 0; i < n; i++) {
            int alto = altoInicial + i * paso;
            for (int j = 0; j < m; j++) {
                int ancho = anchoInicial + j * paso;
                str.append(String.format("\t\t<!-- Casilla (%d, %d) --> \n", i + 1, j + 1));
                if (laberinto[i][j].getN() == Direccion.NORTE)
                    str.append(String.format("\t\t<line x1='%d' y1='%d' x2='%d' y2='%d' stroke='#242423'/>\n", ancho, alto, ancho + paso, alto));
                if (laberinto[i][j].getS() == Direccion.SUR)
                    str.append(String.format("\t\t<line x1='%d' y1='%d' x2='%d' y2='%d' stroke='#242423'/>\n", ancho, alto + paso, ancho + paso, alto + paso));
                if (laberinto[i][j].getO() == Direccion.OESTE)
                    str.append(String.format("\t\t<line x1='%d' y1='%d' x2='%d' y2='%d' stroke='#242423'/>\n", ancho, alto, ancho, alto + paso));
                if (laberinto[i][j].getE() == Direccion.ESTE)
                    str.append(String.format("\t\t<line x1='%d' y1='%d' x2='%d' y2='%d' stroke='#242423'/>\n", ancho + paso, alto, ancho + paso, alto + paso));
                str.append("\n");
            }
        }

        anchoInicial = 35;
        altoInicial = 35;
        
        //Dibuja la solución
        str.append(String.format("\t\t<!-- Solución del Laberinto --> \n"));
        for (int i = 0; i < n; i++) {
            int alto = altoInicial + i * paso;
            for (int j = 0; j < m; j++) {

                int ancho = anchoInicial + j * paso;
                if(j!=m-1 && laberinto[i][j].inTrayectoria()){
                    if(laberinto[i][j+1].inTrayectoria() && laberinto[i][j].getE() == Direccion.ABIERTA && laberinto[i][j+1].getO() == Direccion.ABIERTA){
                        // Modificación para extender y redondear las líneas
                        str.append(String.format("\t\t<path d='M %d %d L %d %d' stroke='#E19978' stroke-width='4' stroke-dasharray='5,5' />\n", ancho - 2, alto, ancho + paso + 2, alto));
                    }
                }

                if(i!=n-1 && laberinto[i][j].inTrayectoria()){
                    if(laberinto[i+1][j].inTrayectoria() && laberinto[i][j].getS() == Direccion.ABIERTA && laberinto[i+1][j].getN() == Direccion.ABIERTA){
                        // Modificación para extender y redondear las líneas
                        str.append(String.format("\t\t<path d='M %d %d L %d %d' stroke='#E19978' stroke-width='4' stroke-dasharray='5,5' />\n", ancho, alto - 2, ancho, alto + paso + 2));
                    }
                }
            }
        }

        //Dibuja el inicio el final
        str.append(String.format("\t\t<!-- Inicio y Fin del Laberinto --> \n"));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == x1 && j == y1) {
                    str.append("\t\t<!-- Rocket icon -->\n");
                    str.append("\t\t<g transform=\"translate(" + (anchoInicial + j * paso - 15) + "," + (altoInicial + i * paso - 15) + ") scale(1)\">\n");
                    str.append("\t\t\t<path fill=\"#2F88FF\" d=\"M 11.7050001,3.89449161 L 17,0 L 22.2949999,3.89449161 C 25.8819274,6.53268984 28,10.7198227 28,15.172478 L 28,33 L 6,33 L 6,15.172478 C 6,10.7198227 8.11807256,6.53268984 11.7050001,3.89449161 Z\"/>\n");
                    str.append("\t\t\t<polygon stroke-linecap=\"round\" points=\"6 13 -2.83106871e-14 19 -2.83106871e-14 27 6 24\"/>\n");
                    str.append("\t\t\t<polygon stroke-linecap=\"round\" points=\"28 13 34 19 34 27 28 24\"/>\n");
                    str.append("\t\t\t<path stroke-linecap=\"round\" d=\"M 11,35 L 11,38\"/>\n");
                    str.append("\t\t\t<path stroke-linecap=\"round\" d=\"M 17,35 L 17,40\"/>\n");
                    str.append("\t\t\t<path stroke-linecap=\"round\" d=\"M 23,35 L 23,38\"/>\n");
                    str.append("\t\t</g>\n");
                }

                if(i == x2 && j == y2){
                    str.append(String.format("\t\t<circle cx='%d' cy='%d' r='17' fill='#2F88FF' />\n", anchoInicial + j * paso, altoInicial + i * paso));
                }
            }
        }

        return str.toString();
    }


}
