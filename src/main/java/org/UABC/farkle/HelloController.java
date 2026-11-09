package org.UABC.farkle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;

public class HelloController {



    private DadoView[] dados = new DadoView[6];
    private boolean[] seleccionados = new boolean[6];

    private Label jugadorActual;
    private Label puntosTurno;
    private Label mensaje;

    private Label jugador1;
    private Label jugador2;

    private FarkleGame juego = new FarkleGame(Arrays.asList("Jugador 1", "Jugador 2"), false);
    private int puntosSeleccionados = 0;



    public Parent crearVista() {

        Label titulo = new Label("FARKLE GAME");


        jugadorActual = new Label("Turno: " + juego.getJugadorActual().getNombre());


        jugador1 = new Label("Jugador 1: 0 puntos");
        jugador2 = new Label("Jugador 2: 0 puntos");

        HBox jugadores = new HBox(40, jugador1, jugador2);
        jugadores.setAlignment(Pos.CENTER);

        Label tituloDados = new Label("Selecciona tus dados");

        GridPane tableroDados = new GridPane();
        tableroDados.setAlignment(Pos.CENTER);
        tableroDados.setHgap(15);
        tableroDados.setVgap(15);

        for (int i = 0; i < 6; i++) {
            final int indice = i;

            dados[i] = new DadoView();


            dados[i].setOnMouseClicked(e -> {
                // Solo permitimos seleccionar si ya se lanzó (no tiene el "?")
                if (!dados[indice].esOculto()) {
                    seleccionados[indice] = !seleccionados[indice];

                    // Delega el trabajo visual a tu clase DadoView
                    dados[indice].setSeleccionadoVisual(seleccionados[indice]);

                    calcularPuntosActuales();
                }
            });

            tableroDados.add(dados[i], i % 3, i / 3);
        }

        puntosTurno = new Label("Puntos del turno: 0");

        Button lanzar = new Button("Lanzar dados");
        Button guardar = new Button("Guardar puntos");

        lanzar.setOnAction(e -> lanzarDados());

        guardar.setOnAction(e -> {
            if (puntosSeleccionados > 0) {
                Jugador actual = juego.getJugadorActual();
                actual.addPTurno(puntosSeleccionados);
                actual.savePturno();


                int PUNTOS_PARA_GANAR = 2000;

                if (actual.getpTotal() >= PUNTOS_PARA_GANAR) {
                    // Actualizamos los textos por última vez
                    jugador1.setText(juego.getJugadores().get(0).getNombre() + ": " + juego.getJugadores().get(0).getpTotal() + " puntos");
                    jugador2.setText(juego.getJugadores().get(1).getNombre() + ": " + juego.getJugadores().get(1).getpTotal() + " puntos");

                    mensaje.setText(actual.getNombre() + " ha ganado el juego.");

                    // Desactivamos los botones para terminar la partida
                    lanzar.setDisable(true);
                    guardar.setDisable(true);
                } else {
                    // Si aún no gana, el juego sigue normalmente
                    mensaje.setText(actual.getNombre() + " guardó " + puntosSeleccionados + " puntos");
                    prepararSiguienteTurno();
                }
            } else {
                mensaje.setText("Selecciona dados que den puntos para poder guardar.");
            }
        });


        mensaje = new Label("Lanza los dados.");

        HBox botones = new HBox(15, lanzar, guardar);
        botones.setAlignment(Pos.CENTER);

        VBox raiz = new VBox(20, titulo, jugadorActual, jugadores, tituloDados, tableroDados, puntosTurno, botones, mensaje);
        raiz.setAlignment(Pos.CENTER);
        raiz.setPadding(new Insets(25));
        raiz.setStyle("-fx-background-color: #eeeeee;");

        return raiz;
    }

    public void lanzarDados() {
        ArrayList<Dado> dadosBackend = juego.getDadosMatch();


        boolean todosSeleccionados = true;
        for (boolean seleccionado : seleccionados) {
            if (!seleccionado) todosSeleccionados = false;
        }

        if (todosSeleccionados) {
            juego.getJugadorActual().addPTurno(puntosSeleccionados);
            puntosSeleccionados = 0;
            for (int i = 0; i < 6; i++) {
                seleccionados[i] = false;
                dadosBackend.get(i).setSeleccionado(false);
                dados[i].setSeleccionadoVisual(false);
            }
        }

        // Sincronizar selección actual con el backend
        for (int i = 0; i < 6; i++) {
            dadosBackend.get(i).setSeleccionado(seleccionados[i]);
        }

        // Lanzar solo los dados que no están seleccionados
        juego.lanzarDados();

        ArrayList<Dado> dadosRecienLanzados = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            int valor = dadosBackend.get(i).getvalorVisible();
            dados[i].mostrarDado(valor);

            // Separar los dados que acaban de caer para evaluarlos
            if (!seleccionados[i]) {
                dadosRecienLanzados.add(dadosBackend.get(i));
            }
        }


        int puntosPosibles = juego.evaluarSeleccion(dadosRecienLanzados);

        if (puntosPosibles == 0) {

            mensaje.setText("Farkle, pierdes el turno y los puntos.");
            prepararSiguienteTurno();
        } else {
            mensaje.setText("Dados lanzados. Selecciona los dados");
        }
    }

    public void calcularPuntosActuales() {
        ArrayList<Dado> dadosBackend = juego.getDadosMatch();
        ArrayList<Dado> dadosAEvaluar = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            if (seleccionados[i]) {
                dadosAEvaluar.add(dadosBackend.get(i));
            }
        }

        puntosSeleccionados = juego.evaluarSeleccion(dadosAEvaluar);
        puntosTurno.setText("Puntos del turno: " + puntosSeleccionados);
    }

    public void prepararSiguienteTurno() {
        juego.siguienteTurno();
        puntosSeleccionados = 0;

        for (int i = 0; i < 6; i++) {
            seleccionados[i] = false;

            dados[i].setSeleccionadoVisual(false);
            dados[i].ocultarDado();
        }

        jugadorActual.setText("Turno: " + juego.getJugadorActual().getNombre());
        puntosTurno.setText("Puntos del turno: 0");

        jugador1.setText(juego.getJugadores().get(0).getNombre() + ": " + juego.getJugadores().get(0).getpTotal() + " puntos");
        jugador2.setText(juego.getJugadores().get(1).getNombre() + ": " + juego.getJugadores().get(1).getpTotal() + " puntos");
    }
}