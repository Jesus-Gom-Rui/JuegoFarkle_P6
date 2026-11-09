package org.UABC.farkle;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class DadoView extends HBox {

    public final int EMOJI_DADO = 9855;
    private final Label labelDado;

    public DadoView() {

        this.setPrefSize(80, 80);
        this.setAlignment(Pos.CENTER);

        // Estilo inicial tipo "botón"
        this.setStyle("-fx-background-color: white; -fx-border-color: #999999; -fx-border-width: 1px; -fx-cursor: hand;");

        this.labelDado = new Label("?"); // Valor inicial oculto
        this.labelDado.setFont(new Font("Arial", 50));

        this.getChildren().add(labelDado);
    }

    public void mostrarDado(int valor) {
        String caraDado = new String(Character.toChars(EMOJI_DADO + valor));
        this.labelDado.setText(caraDado);
    }


    public void setSeleccionadoVisual(boolean seleccionado) {
        if (seleccionado) {
            this.setStyle("-fx-background-color: #75c995; -fx-border-color: #24864a; -fx-border-width: 2px; -fx-cursor: hand;");
        } else {
            this.setStyle("-fx-background-color: white; -fx-border-color: #999999; -fx-border-width: 1px; -fx-cursor: hand;");
        }
    }


    public void ocultarDado() {
        this.labelDado.setText("?");
    }
    public boolean esOculto() {
        return this.labelDado.getText().equals("?");
    }
}