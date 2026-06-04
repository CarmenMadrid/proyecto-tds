package umu.tds.gastos.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import umu.tds.gastos.ui.view.SceneManager;

public class MainApp extends Application {
    @Override
	public void start(Stage pantallaInicial) throws IOException {
    	
    	//Inicializar configuración (repositorios y controladores de dominio)
        Configuracion.getInstancia();

        //Inicializar SceneManager
        SceneManager sceneManager = SceneManager.getInstancia();
        sceneManager.init(pantallaInicial);

        sceneManager.showVentanaPrincipal();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
