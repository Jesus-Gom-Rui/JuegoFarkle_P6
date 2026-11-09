package org.UABC.farkle;

import java.util.Random;

public class Dado {
    private int numeroCaras;
    private int valorVisible;
    private boolean seleccionado;

    public Dado() {
        numeroCaras = 6;
        valorVisible = 1;
        seleccionado = false;
    }

    public Dado(int numeroCaras) {
        this.numeroCaras = numeroCaras;
        valorVisible = 1;
        seleccionado = false;
    }

    public void setNumeroCaras(int numeroCaras) {
        this.numeroCaras = numeroCaras;
    }

    public int getNumeroCaras() {
        return numeroCaras;
    }
    public void lanzar() {

        if (!seleccionado) {
            Random rmd = new Random();
            valorVisible = rmd.nextInt(numeroCaras) + 1;
        }
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public int getvalorVisible() {
        return valorVisible;
    }

    public String toString() {
        return "Dado con " + numeroCaras + " cuyo Valor actual es " + valorVisible;
    }
}
