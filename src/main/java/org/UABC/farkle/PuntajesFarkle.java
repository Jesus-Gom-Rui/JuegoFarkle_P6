package org.UABC.farkle;

public enum PuntajesFarkle {

    //Puntos que se obtienen al escoger de manera individual
    UNO(100),
    CINCO(50),

    //Puntos que se obtienen al tener triples
    TRIO_UNOS(1000),
    TRIO_DOS(200),
    TRIO_TRES(300),
    TRIO_CUATRO(400),
    TRIO_CINCO(500),
    TRIO_SEIS(600),

    //Puntos que se obtienen de sacar múltiples iguales
    CUATRO_IGUALES(1000),
    CINCO_IGUALES(2000),
    SEIS_IGUALES(3000),

    //Casos especiales

    TRES_PARES(1500),
    ESCALERA(2500);

    private final int valor;

    PuntajesFarkle(int valor){
        this.valor=valor;

    }

    public int getValor(){
        return valor;
    }
}
