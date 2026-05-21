package umu.tds.gastos.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
	public void start(Stage pantallaInicial) throws IOException {
		// Configuracion.getInstancia().getSceneManager().inicializar(pantallaInicial);
		// Configuracion.getInstancia().getSceneManager().abrirVentanaInicial("Gestion de Gastos");
	}

	public static void main(String[] args) {
		launch(args);
	}
}
