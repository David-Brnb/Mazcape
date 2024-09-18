package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Clase para Laberintos 
 */
import mx.unam.ciencias.edd.Pila;
import mx.unam.ciencias.edd.proyecto3.Graficadores.GraficadorLaberinto;


public class Laberinto {
    private Casilla[][] laberinto;
    private Random random;
    private int n, m;
    private Termina term = new Termina();
    private int x1, x2, y1, y2;

    public Laberinto(){}

    public Laberinto(int n, int m){
        random = new Random();
        this.n=n;
        this.m=m;
    }

    public Laberinto(int n, int m, long s){
        random = new Random(s);
        this.n=n;
        this.m=m;
    }

    // constructuor laberinto a partir de .mze
    public void construyeLaberinto(BufferedInputStream bis) {
        try {

            int idx = 0;
            int i=0;
            int x = 0; //alto;
            int y = 0; // ancho;
            while((i = bis.read()) != -1){
                if(idx > 5){
                    if(idx-5 > n*m){
                        term.terminaErr("Se ingreso una entrada que contiene más casillas de la capacidad indicada");
                    }

                    laberinto[x][y] = new Casilla(x, y, i>>4, i&15);

                    if (y == m - 1){
                        y = 0;
                        x = x +1;
                    } else {
                        y = y +1;   
                    }

                } else {
                    if(idx == 0 && i!= 0x4d)
                        term.terminaErr("El archivo ingresado tiene un caracter erroneo al inicio 1. " + i);
                    
                    if (idx == 1 && i != 0x41) 
                        term.terminaErr("El archivo ingresado tiene un caracter erroneo al inicio 2. "+ i);

                    if (idx == 2 && i != 0x5a)
                        term.terminaErr("El archivo ingresado tiene un caracter erroneo al inicio 3. "+ i);

                    if (idx == 3 && i != 0x45) 
                        term.terminaErr("El archivo ingresado tiene un caracter erroneo al inicio 4. "+ i);

                    if (idx == 4){
                        this.n = i;
                    }
                    if(idx == 5){
                        this.m = i;
                        this.laberinto = new Casilla[n][m];
                    }
                }
                idx++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean v1=true, v2=true;
        // Verificar laberinto
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(j!=m-1 && laberinto[i][j].getE() == Direccion.ESTE){
                    if(laberinto[i][j+1].getO() != Direccion.OESTE){
                        v1=false;
                        break;
                    }
                }

                if(i!=n-1 && laberinto[i][j].getS() == Direccion.SUR){
                    if(laberinto[i+1][j].getN() != Direccion.NORTE){
                        v2=false;
                        break;
                    }
                }
            }
        }
        
        // En caso de que no se cumpla la apertura de las paredes se manda una advertencia y termina el programa
        if(!(v1&&v2)){
            term.terminaErr("Se detecto una anomalia en el archivo .mze ingresado");
        }

        //Busqueda de entradas
        x1=-1;
        y1=-1;
        x2=-1;
        y2=-1;

        int cnt=0;

        for (int i = 0; i < n; i++) {
            if(laberinto[i][0].getO() == Direccion.ABIERTA){
                if(cnt==0){
                    x1 = i; 
                    y1 = 0;
                } else if(cnt == 1){
                    x2 = i; 
                    y2 = 0;

                } else {
                    term.terminaErr("Se decto más de una entrada ");

                }

                cnt++;
            }
        }

        for (int i = 0; i < n; i++) {
            if(laberinto[i][m-1].getE() == Direccion.ABIERTA){
                if(cnt==0){
                    x1 = i; 
                    y1 = m-1;
                } else if(cnt == 1){
                    x2 = i; 
                    y2 = m-1;

                } else {
                    term.terminaErr("Se decto más de una entrada ");
                }

                cnt++;
            }
        }

        for (int j = 0; j < m; j++) {
            if(laberinto[0][j].getN() == Direccion.ABIERTA){
                if(cnt==0){
                    x1 = 0; 
                    y1 = j;
                } else if(cnt == 1){
                    x2 = 0; 
                    y2 = j;

                } else {
                    term.terminaErr("Se decto más de una entrada ");
                }
                
                cnt++;
            }
        }

        for (int j = 0; j < m; j++) {
            if(laberinto[n-1][j].getS() == Direccion.ABIERTA){
                if(cnt==0){
                    x1 = n-1; 
                    y1 = j;
                } else if(cnt == 1){
                    x2 = n-1; 
                    y2 = j;

                } else {
                    term.terminaErr("Se decto más de una entrada ");
                }
                
                cnt++;
            }
        }

        resuelveLaberinto(x1, y1, x2, y2);

    }
    

    //Construir laberinto nuevo
    public void construyeLaberinto(){
        Casilla[][] c = new Casilla[n][m];

        //Inicializamos nuestro laberinto. 
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                c[i][j] = new Casilla(i, j);
            }
        }

        // llamamos a nuestro metodo para crear laberintos
        this.creaLaberinto(c);

        // Asignamos pesos a las orillas tal como se pide en las
        // especificaciones del proyecto
        int[] arr = {0, m-1};
        for(int e=0; e<2; e++){
            for(int i=0; i<n; i++){
                c[i][arr[e]].setPeso((byte) 1);
            }
        }

        arr[1] = n-1;
        for(int e=0; e<2; e++){
            for(int i=0; i<m; i++){
                c[arr[e]][i].setPeso((byte) 1);
            }
        }


        //Realizamos una asignacion random de entrada y salida del laberinto
        int ei = random.nextInt(n);
        int ej = random.nextInt(m);

        int r = random.nextInt(4);

        switch(r){
            case 0:
                ei = 0;
                c[ei][ej].setN(Direccion.ABIERTA);
            break;

            case 1:
                ei = n-1;
                c[ei][ej].setS(Direccion.ABIERTA);
            break;

            case 2:
                ej = 0;
                c[ei][ej].setO(Direccion.ABIERTA);
            break;

            case 3:
                ej = m-1;
                c[ei][ej].setE(Direccion.ABIERTA);
            break;
        }


        int si=0, sj=0;

        do{
            si = random.nextInt(n);
            sj = random.nextInt(m);

            r = random.nextInt(4);

            switch(r){
                case 0:
                    si = 0;
                    if(!(si==ei && sj==ej || (si==ei && (sj == ej+1 || sj == ej-1)) || (sj==ej && (si == ei+1 || si == ei-1)))){
                        c[si][sj].setN(Direccion.ABIERTA);
                    }
                break;

                case 1:
                    si = n-1;
                    if(!(si==ei && sj==ej || (si==ei && (sj == ej+1 || sj == ej-1)) || (sj==ej && (si == ei+1 || si == ei-1)))){
                        c[si][sj].setS(Direccion.ABIERTA);
                    }
                break;

                case 2:
                    sj = 0;
                    if(!(si==ei && sj==ej || (si==ei && (sj == ej+1 || sj == ej-1)) || (sj==ej && (si == ei+1 || si == ei-1)))){
                        c[si][sj].setO(Direccion.ABIERTA);
                    }
                break;

                case 3:
                    sj = m-1;
                    if(!(si==ei && sj==ej || (si==ei && (sj == ej+1 || sj == ej-1)) || (sj==ej && (si == ei+1 || si == ei-1)))){
                        c[si][sj].setE(Direccion.ABIERTA);
                    }
                break;
            }
        } while(si==ei && sj==ej || (si==ei && (sj == ej+1 || sj == ej-1)) || (sj==ej && (si == ei+1 || si == ei-1)) );


        laberinto = c; 
    }

    /**
     * Crea un laberinto dado un arreglo y las dimenciones 
     * de n filas y m columnas.
     * El laberinto es creado mediante el uso de backtracking y 
     * con un recorrido de forma aleatorio a lo largo del laberinto
     * original.
     * @param l matriz que contiene al laberinto, cada elemento de la matriz
     * tiene un dato de tipo Casilla, el cual contiene información sobre cada casilla.
     */
    public void creaLaberinto(Casilla[][] l){
        Pila<Casilla> pc = new Pila<>();

        pc.mete(l[0][0]);

        /*
         * Se aplica un algoritmo que realiza un recorrido aleatorio del laberinto
         * mientras haya vecinos disponibles a los cuales ir, el algoritmo elegiria
         * un vecino de forma aleatoria (rompiedo así la pared), a su vez irá 
         * guardando en una pila la traza de los vecinos visitados. 
         * Cuando se encuentre con el caso de que ya no hay vecinos, entonces sale 
         * del while secundario que va avanzando y saca al anterior de la pila, 
         * si el anterior no tiene vecinos, entonces saca a su anterior, así hasta
         * acabarnos los elementos, y de esa fora es que termina. 
         */
        while(!pc.esVacia()){
            Casilla caTemp = pc.saca();
            
            /*
             * Se declaran en false para el correcto funcionamiento del while, 
             * pues cuando no haya salidas todo sera true, y de esta manera
             * cuando solo uno cambie a true el ciclo no acaba, solo cuando 
             * todos lo hagan. 
             */
            boolean ent1=false, ent2=false, ent3=false, ent4=false;
            while(!(ent1 && ent2 && ent3 && ent4)){
                caTemp.visitar(true);

                //Buscamos casillas y vamos visitando / agregando pesos
                int ranit = random.nextInt(4);

                switch(ranit){
                    case 0: 
                    if(caTemp.getX() == 0){
                        ent1 = true;

                    } else if(l[caTemp.getX()-1][caTemp.getY()].fueVisitada()){
                        ent1 = true;

                    } else {
                        pc.mete(caTemp);
                        caTemp.setN(Direccion.ABIERTA);
                        caTemp = l[caTemp.getX()-1][caTemp.getY()];
                        caTemp.setS(Direccion.ABIERTA);
                        ent1=false;
                        ent2=false;
                        ent3=false; 
                        ent4=false;
                    }

                    break;


                    case 1: 
                    if(caTemp.getX() == n-1){
                        ent2 = true;

                    } else if(l[caTemp.getX()+1][caTemp.getY()].fueVisitada()){
                        ent2 = true;

                    } else {
                        pc.mete(caTemp);
                        caTemp.setS(Direccion.ABIERTA);
                        caTemp = l[caTemp.getX()+1][caTemp.getY()];
                        caTemp.setN(Direccion.ABIERTA);
                        ent1=false;
                        ent2=false;
                        ent3=false; 
                        ent4=false;
                    }
                    
                    break;


                    case 2:
                    if(caTemp.getY() == 0){
                        ent3 = true;

                    } else if(l[caTemp.getX()][caTemp.getY()-1].fueVisitada()){
                        ent3 = true;

                    } else {
                        pc.mete(caTemp);
                        caTemp.setO(Direccion.ABIERTA);
                        caTemp = l[caTemp.getX()][caTemp.getY()-1];
                        caTemp.setE(Direccion.ABIERTA);
                        ent1=false;
                        ent2=false;
                        ent3=false; 
                        ent4=false;
                    }
                    
                    break;

                    case 3:
                    if(caTemp.getY() == m-1){
                        ent4 = true;

                    } else if(l[caTemp.getX()][caTemp.getY()+1].fueVisitada()){
                        ent4 = true;

                    } else {
                        pc.mete(caTemp);
                        caTemp.setE(Direccion.ABIERTA);
                        caTemp = l[caTemp.getX()][caTemp.getY()+1];
                        caTemp.setO(Direccion.ABIERTA);
                        ent1=false;
                        ent2=false;
                        ent3=false; 
                        ent4=false;
                    } 
                    
                    break;
                }
            }

        }
    }


    /*
     * Algoritmo para resolver Laberinto
     * El algoritmo mediante backtraking recorre el laberinto para 
     * de esa forma llevar un registro de en donde ha recorrido y 
     * para cuando se encuentra en el destino
     */
    private void resuelveLaberinto(int x1, int y1, int x2, int y2){
        Casilla temp = laberinto[x1][y1];
        Pila<Casilla> pc = new Pila<>();

        // Ponemos a todas las casillas en no visitadas para evitar problemas
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                laberinto[i][j].visitar(false);
            }
        }

        int i=0;
        //recorremos el laberinto haciendo backtracking
        while(!(temp.getX() == x2 && temp.getY() == y2)){
            if(i>70000){
                term.terminaErr("Se recorrio más de 70 mil veces el laberinto y no hubo solución");
                break;
            }
            temp.visitar(true);

            if(temp.getX() == 0 && temp.getY() == 0){
                if(temp.getE() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()+1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()+1];

                } else if(temp.getS() == Direccion.ABIERTA && !laberinto[temp.getX()+1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()+1][temp.getY()];

                } else {
                    temp = pc.saca();
                }

            } else if(temp.getX() == n-1 && temp.getY() == 0){
                if(temp.getE() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()+1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()+1];

                } else if(temp.getN() == Direccion.ABIERTA && !laberinto[temp.getX()-1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()-1][temp.getY()];

                } else {
                    temp = pc.saca();
                }

            } else if(temp.getX() == 0 && temp.getY() == m-1){
                if(temp.getO() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()-1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()-1];

                } else if(temp.getS() == Direccion.ABIERTA && !laberinto[temp.getX()+1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()+1][temp.getY()];

                } else {
                    temp = pc.saca();

                }
                
            } else if(temp.getX() == 0 && temp.getY() == 0){
                if(temp.getO() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()-1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()-1];

                } else if(temp.getS() == Direccion.ABIERTA && !laberinto[temp.getX()-1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()-1][temp.getY()];

                } else {
                    temp = pc.saca();

                }
                
                
            } else if(temp.getX() == 0){
                if(temp.getE() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()+1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()+1];

                } else if(temp.getO() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()-1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()-1];

                } else if(temp.getS() == Direccion.ABIERTA && !laberinto[temp.getX()+1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()+1][temp.getY()];

                } else {
                    temp = pc.saca();

                }

            } else if(temp.getX() == n-1){
                if(temp.getE() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()+1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()+1];

                } else if(temp.getN() == Direccion.ABIERTA && !laberinto[temp.getX()-1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()-1][temp.getY()];

                } else if(temp.getO() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()-1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()-1];

                } else {
                    temp = pc.saca();

                }

            } else if(temp.getY() == 0){
                if(temp.getE() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()+1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()+1];

                } else if(temp.getN() == Direccion.ABIERTA && !laberinto[temp.getX()-1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()-1][temp.getY()];

                } else if(temp.getS() == Direccion.ABIERTA && !laberinto[temp.getX()+1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()+1][temp.getY()];

                } else {
                    temp = pc.saca();
                }

            } else if(temp.getY() == m-1){
                if(temp.getN() == Direccion.ABIERTA && !laberinto[temp.getX()-1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()-1][temp.getY()];

                } else if(temp.getO() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()-1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()-1];

                } else if(temp.getS() == Direccion.ABIERTA && !laberinto[temp.getX()+1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()+1][temp.getY()];

                } else {
                    temp = pc.saca();
                }

            } else {
                if(temp.getE() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()+1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()+1];

                } else if(temp.getN() == Direccion.ABIERTA && !laberinto[temp.getX()-1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()-1][temp.getY()];

                } else if(temp.getO() == Direccion.ABIERTA && !laberinto[temp.getX()][temp.getY()-1].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()][temp.getY()-1];

                } else if(temp.getS() == Direccion.ABIERTA && !laberinto[temp.getX()+1][temp.getY()].fueVisitada()){
                    pc.mete(temp);
                    temp = laberinto[temp.getX()+1][temp.getY()];

                } else {
                    temp = pc.saca();
                }
            }

        }

        pc.mete(temp);

        while(!pc.esVacia()){
            pc.saca().pasaTrayectoria(true);
        }

    }

    public String graficaLaberinto(){
        GraficadorLaberinto gl = new GraficadorLaberinto(laberinto, n, m);

        return gl.grafica(x1, y1, x2, y2) + gl.cierre();
    }

    public void writeLab(){
        WriteLaberinto wl = new WriteLaberinto(laberinto, n, m);
        wl.escribe();
    }    
}