package Evaluable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;



/**
 * El Controlador actúa como un intermediario entre la Vista y el Modelo.
 * Gestiona los eventos y las interacciones entre el usuario y la aplicación.
 */
public class Controlador {

	private Modelo model;
	private Vista view;
	
	/**
     * Crea un nuevo Controlador con un Modelo y una Vista dados.
     *
     * @param model El modelo de la aplicación.
     * @param view La vista de la aplicación.
     */
	public Controlador(Modelo model, Vista view) {
		this.model = model;
		this.view = view;
		initEventHandlers();
	}

	/**
     * Inicializa los manejadores de eventos para los elementos de la interfaz de usuario.
     */
	public void initEventHandlers() {

		model.connectToDatabase("./client.xml");

		/**
         * Maneja el evento de inicio de sesión cuando se presiona el botón "Iniciar Sesión".
         */
		view.btnIniciarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String usuario = view.txtfUsuario.getText();
				String Password = view.txtfPassword.getText();
				boolean loggedIn = model.verificarUsuario(usuario, Password);
				if (loggedIn) {
					JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso");
					view.mostrarRestoComponentes(true);
					view.panelLogin.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "Nombre de usuario o Password incorrectos");
					view.txtfUsuario.setText("");
					view.txtfPassword.setText("");
				}
			}
		});

		/**
         * Maneja el evento de ejecución de consulta cuando se presiona el botón "Ejecutar".
         */
		view.btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String consulta = view.txtConsulta.getText();
				model.displayQueryResults(consulta, view.txtInfoPanel);
			}
		});

		/**
         * Maneja el evento de cierre de sesión cuando se presiona el botón "Cerrar Sesión".
         */
		view.btnCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (model.confirmCloseConexion()) {
					model.closeConexion();
					model.connectToDatabase("./client.xml");
					view.mostrarRestoComponentes(false);
					view.panelLogin.setVisible(true);
					view.txtfUsuario.setText("Usuario");
					view.txtfPassword.setText("Password");
					view.txtConsulta.setText("Escribe tu consulta...");
					model.cleanTable();
				}
			}
		});

		/**
         * Maneja el evento de cierre de la base de datos cuando se presiona el botón "Cerrar BDD".
         */
		view.btnCerrarBDD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (model.confirmCloseConexion()) {
					model.closeConexion();
					view.mostrarRestoComponentes(false);
					view.dispose();
				}
			}
		});
	}

}
