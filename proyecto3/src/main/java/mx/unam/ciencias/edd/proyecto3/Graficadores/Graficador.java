package mx.unam.ciencias.edd.proyecto3.Graficadores;

public class Graficador {
    private int height;
    private int width;

    public Graficador(int h, int w){
        this.height = h;
        this.width = w;
    }

    public String encabezado(){
        return "<?xml version='1.0' encoding='UTF-8' ?>\n" +
               "<svg width='"+ width +"' height='"+ height +"'> \n" +
               "\t<g>\n";
    }


    public String cierre(){
        return "\t</g>\n"+
               "</svg>";
    }

}
