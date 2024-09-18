package mx.unam.ciencias.edd.proyecto3;
/**
 * Enumeración para las puertas de las casillas
 */
public enum Direccion {
    ESTE((byte) 0b0001), //La puerta Este esta cerrada
    NORTE((byte) 0b0010), //La puerta Norte esta cerrada
    OESTE((byte) 0b0100), //La puerta Oeste esta cerrada
    SUR((byte) 0b1000), //La puerta Sur esta cerrada
    ABIERTA((byte) 0b0000); //La puerta esta abierta

    private final byte codigo;

    Direccion(byte codigo) {
        this.codigo = codigo;
    }

    public byte getCodigo() {
        return codigo;
    }

}