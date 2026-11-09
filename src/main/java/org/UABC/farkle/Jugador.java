package org.UABC.farkle;

public class Jugador {
    private String nombre;
    private int pTurno=0;
    private int pTotal=0;
    private boolean isHuman;
        public void addPTurno(int puntos){
          this.pTurno += puntos;
        }
        public void savePturno(){
            this.pTotal += this.pTurno;
            this.pTurno=0;
        }
        public void retryTurn(){
            this.pTurno=0;
        }

    public String getNombre() {
        return nombre;
    }

    public int getpTurno() {
        return pTurno;
    }

    public int getpTotal() {
        return pTotal;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public Jugador(String nombre, boolean isHuman) {
        this.nombre = nombre;
        this.pTurno = 0;
        this.pTotal = 0;
        this.isHuman = isHuman;
    }
}
