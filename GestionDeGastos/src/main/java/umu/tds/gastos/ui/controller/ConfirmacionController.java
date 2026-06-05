package umu.tds.gastos.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import umu.tds.gastos.ui.view.SceneManager;

public class ConfirmacionController {

    @FXML
    private Label mensajeLabel;

    private Runnable onAccept;

    public void setMensaje(String mensaje) {
        mensajeLabel.setText(mensaje);
    }

    public void setOnAccept(Runnable onAccept) {
        this.onAccept = onAccept;
    }

    @FXML
    void aceptar(ActionEvent event) {
        SceneManager.getInstancia().closeDialog();
        if (onAccept != null) onAccept.run();
    }

    @FXML
    void cancelar(ActionEvent event) {
        SceneManager.getInstancia().closeDialog();
    }
}
