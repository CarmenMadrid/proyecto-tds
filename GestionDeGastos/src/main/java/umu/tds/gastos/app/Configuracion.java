package umu.tds.gastos.app;

import umu.tds.gastos.ui.view.SceneManager;

public class Configuracion {

    private static Configuracion instancia;
    private final SceneManager sceneManager = SceneManager.getInstancia();

    static void setInstancia(Configuracion impl) {
		Configuracion.instancia = impl;
	}

	public static Configuracion getInstancia() {
		return Configuracion.instancia;
	}

	public SceneManager getSceneManager() {
		return sceneManager;
	}

}
