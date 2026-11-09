package org.UABC.farkle;

import java.util.ArrayList;
import  java.util.List;

public class FarkleGame {
    private ArrayList<Jugador> jugadores;
    private ArrayList<Dado> dadosMatch;
    private CalculadoraDePuntos Calculadora;
    private  int turnNow;

    public ArrayList<Dado> getDadosMatch() {
        return dadosMatch;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    public Jugador getJugadorActual(){
        return jugadores.get(turnNow);
    }

    public FarkleGame(List<String> namePlayers, boolean oneplayer){
        jugadores=new ArrayList<>();
        dadosMatch=new ArrayList<>();
        Calculadora= new CalculadoraDePuntos();
        //inician dados
        for(int i=0;i<6;i++){
            dadosMatch.add(new Dado(6));
        }
        //humanos
        for(String nombre: namePlayers){
            jugadores.add(new Jugador(nombre,true));
        }
        if(oneplayer||jugadores.size()==1) {
            jugadores.add(new Jugador("IA", false));
        }
        turnNow=0;
        }
//lambda va buscando los dados que no se han seleccionado
        public void lanzarDados(){
           dadosMatch.stream()
        .filter(d->!d.isSeleccionado())
        .forEach(Dado::lanzar);
        }
        public int evaluarSeleccion(ArrayList<Dado>seleccionados){
        return Calculadora.evaluarTiro(seleccionados);
        }

    public void siguienteTurno() {
        getJugadorActual().retryTurn();
        turnNow = (turnNow+ 1) % jugadores.size();

        // Resetear y deseleccionar dados usando lambda
        dadosMatch.forEach(d -> {
            d.setSeleccionado(false);
            d.lanzar();
        });
    }

    public void ordenarJugadoresPorPuntaje() {
        jugadores.sort((j1, j2) -> Integer.compare(j2.getpTotal(), j1.getpTotal()));
    }


    }






