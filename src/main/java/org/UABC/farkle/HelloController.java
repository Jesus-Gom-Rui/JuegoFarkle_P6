package org.UABC.farkle;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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

    private int puntosSeleccionados = 0;
    private Button lanzar;
    private Button guardar;
    private Button terminar;

    private FarkleGame juego;
    private StackPane rootPrincipal;

    public Parent crearVista() {
        rootPrincipal = new StackPane();
        rootPrincipal.getChildren().add(crearMenuInicio());
        return rootPrincipal;
    }

    public VBox crearMenuInicio() {
        Label titulo = new Label("BIENVENIDO A FARKLE");
        titulo.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Button btnVsIA = new Button("Jugador vs IA");
        btnVsIA.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        btnVsIA.setOnAction(e -> {
            juego = new FarkleGame(Arrays.asList("Jugador"), true);
            iniciarJuego();
        });

        Button btn2Jugadores = new Button("2 Jugadores");
        btn2Jugadores.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        btn2Jugadores.setOnAction(e -> {
            juego = new FarkleGame(Arrays.asList("Jugador 1", "Jugador 2"), false);
            iniciarJuego();
        });

        VBox menu = new VBox(30, titulo, btnVsIA, btn2Jugadores);
        menu.setAlignment(Pos.CENTER);
        menu.setStyle("-fx-background-color: #eeeeee;");
        return menu;
    }

    public void iniciarJuego() {
        rootPrincipal.getChildren().clear();
        rootPrincipal.getChildren().add(crearTableroJuego());
    }

    public VBox crearTableroJuego() {
        Label titulo = new Label("FARKLE GAME");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        jugadorActual = new Label("Turno: " + juego.getJugadorActual().getNombre());
        jugadorActual.setStyle("-fx-font-size: 18px;");

        jugador1 = new Label(juego.getJugadores().get(0).getNombre() + ": 0 puntos");
        jugador2 = new Label(juego.getJugadores().get(1).getNombre() + ": 0 puntos");

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
                if (!dados[indice].esOculto()) {
                    seleccionados[indice] = !seleccionados[indice];
                    dados[indice].setSeleccionadoVisual(seleccionados[indice]);
                    calcularPuntosActuales();
                }
            });

            tableroDados.add(dados[i], i % 3, i / 3);
        }

        puntosTurno = new Label("Puntos del turno: 0");
        puntosTurno.setStyle("-fx-font-size: 17px;");


        lanzar = new Button("Lanzar dados");
        guardar = new Button("Guardar puntos");
        terminar = new Button("Terminar turno");

        lanzar.setOnAction(e -> lanzarDados());

        guardar.setOnAction(e -> {
            if (puntosSeleccionados > 0) {
                Jugador actual = juego.getJugadorActual();
                actual.addPTurno(puntosSeleccionados);
                actual.savePturno();

                int PUNTOS_PARA_GANAR = 10000;

                if (actual.getpTotal() >= PUNTOS_PARA_GANAR) {
                    jugador1.setText(juego.getJugadores().get(0).getNombre() + ": " + juego.getJugadores().get(0).getpTotal() + " puntos");
                    jugador2.setText(juego.getJugadores().get(1).getNombre() + ": " + juego.getJugadores().get(1).getpTotal() + " puntos");

                    mensaje.setText(actual.getNombre() + " ha ganado el juego");

                    lanzar.setDisable(true);
                    guardar.setDisable(true);
                    terminar.setDisable(true);
                } else {
                    mensaje.setText(actual.getNombre() + " guardó " + puntosSeleccionados + " puntos");
                    prepararSiguienteTurno();
                }
            } else {
                mensaje.setText("Selecciona dados que den puntos para poder guardar.");
            }
        });

        terminar.setOnAction(e -> {
            mensaje.setText("Turno terminado.");
            prepararSiguienteTurno();
        });

        mensaje = new Label("Lanza los dados.");

        HBox botones = new HBox(15, lanzar, guardar, terminar);
        botones.setAlignment(Pos.CENTER);

        VBox raiz = new VBox(20, titulo, jugadorActual, jugadores, tituloDados, tableroDados, puntosTurno, botones, mensaje);
        raiz.setAlignment(Pos.CENTER);
        raiz.setPadding(new Insets(25));
        raiz.setStyle("-fx-background-color: #eeeeee;");

        return raiz;
    }

    public void jugarTurnoBot() {
        lanzar.setDisable(true);
        guardar.setDisable(true);
        terminar.setDisable(true);


        PauseTransition pausaLanzar = new PauseTransition(Duration.seconds(1.5));
        pausaLanzar.setOnFinished(e -> {
            lanzarDados();

            ArrayList<Dado> dadosRecienLanzados = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                if (!seleccionados[i] && !dados[i].esOculto()) {
                    dadosRecienLanzados.add(juego.getDadosMatch().get(i));
                }
            }

            if (juego.evaluarSeleccion(dadosRecienLanzados) > 0) {
                PauseTransition pausaSeleccionar = new PauseTransition(Duration.seconds(1.5));
                pausaSeleccionar.setOnFinished(ev -> {



                    java.util.Map<Integer, java.util.List<Integer>> porCara = new java.util.HashMap<>();
                    for (int i = 1; i <= 6; i++) porCara.put(i, new ArrayList<>());

                    java.util.List<Integer> indicesDisponibles = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        if (!dados[i].esOculto() && !seleccionados[i]) {
                            indicesDisponibles.add(i);
                            int valor = juego.getDadosMatch().get(i).getvalorVisible();
                            porCara.get(valor).add(i);
                        }
                    }

                    // 2. Verificar Escalera
                    if (indicesDisponibles.size() == 6 && porCara.values().stream().allMatch(l -> l.size() == 1)) {
                        for (int idx : indicesDisponibles) {
                            seleccionados[idx] = true;
                            dados[idx].setSeleccionadoVisual(true);
                        }
                    }
                    // 3. Verificar Tres Pares
                    else if (indicesDisponibles.size() == 6 && porCara.values().stream().filter(l -> l.size() == 2).count() == 3) {
                        for (int idx : indicesDisponibles) {
                            seleccionados[idx] = true;
                            dados[idx].setSeleccionadoVisual(true);
                        }
                    }
                    // 4. Seleccionar Tríos, Múltiples, Unos y Cincos
                    else {
                        for (int cara = 1; cara <= 6; cara++) {
                            java.util.List<Integer> listaIndices = porCara.get(cara);
                            int cantidad = listaIndices.size();

                            // Si hay 3 o más iguales
                            if (cantidad >= 3) {
                                for (int idx : listaIndices) {
                                    seleccionados[idx] = true;
                                    dados[idx].setSeleccionadoVisual(true);
                                }
                            }
                            // se toman si son 1s o 5s
                            else if (cara == 1 || cara == 5) {
                                for (int idx : listaIndices) {
                                    seleccionados[idx] = true;
                                    dados[idx].setSeleccionadoVisual(true);
                                }
                            }
                        }
                    }

                    calcularPuntosActuales();

                    // Pausa
                    PauseTransition pausaGuardar = new PauseTransition(Duration.seconds(1.5));
                    pausaGuardar.setOnFinished(ev2 -> {
                        if (puntosSeleccionados > 0) {
                            guardar.setDisable(false);
                            guardar.fire();
                        } else {
                            terminar.setDisable(false);
                            terminar.fire();
                        }
                    });
                    pausaGuardar.play();
                });
                pausaSeleccionar.play();
            }
        });
        pausaLanzar.play();
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

        for (int i = 0; i < 6; i++) {
            dadosBackend.get(i).setSeleccionado(seleccionados[i]);
        }

        juego.lanzarDados();

        ArrayList<Dado> dadosRecienLanzados = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            int valor = dadosBackend.get(i).getvalorVisible();
            dados[i].mostrarDado(valor);

            if (!seleccionados[i]) {
                dadosRecienLanzados.add(dadosBackend.get(i));
            }
        }

        int puntosPosibles = juego.evaluarSeleccion(dadosRecienLanzados);

        if (puntosPosibles == 0) {
            mensaje.setText("FARKLE, Pierdes el turno y los puntos acumulados.");
            lanzar.setDisable(true);
            guardar.setDisable(true);
            terminar.setDisable(true);

            // Pausa visual de 2 segundos
            PauseTransition pausaFarkle = new PauseTransition(Duration.seconds(2.0));
            pausaFarkle.setOnFinished(ev -> prepararSiguienteTurno());
            pausaFarkle.play();
        } else {
            mensaje.setText("Dados lanzados. Selecciona los dados.");
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

        if (!juego.getJugadorActual().isHuman()) {
            jugarTurnoBot();
        } else {
            lanzar.setDisable(false);
            guardar.setDisable(false);
            terminar.setDisable(false);
            mensaje.setText("Lanza los dados.");
        }
    }
}