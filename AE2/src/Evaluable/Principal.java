package Evaluable;

public class Principal {

	public static void main(String[] args) {
		Vista vista = new Vista();
		Modelo model = new Modelo();
		Controlador controlador = new Controlador(model, vista);
	}

}
