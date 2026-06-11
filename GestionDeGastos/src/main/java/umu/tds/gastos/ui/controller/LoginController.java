package umu.tds.gastos.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import umu.tds.gastos.ui.view.SceneManager;

public class LoginController {

    @FXML
    void entrar(ActionEvent event) {
        SceneManager.getInstancia().showVentanaPrincipal();
    }
}
