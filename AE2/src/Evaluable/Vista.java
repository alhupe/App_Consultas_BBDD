package Evaluable;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * La clase Vista representa la interfaz gráfica de usuario para la aplicación.
 * Contiene elementos como botones, campos de texto y tablas para interactuar
 * con la base de datos.
 */
public class Vista extends JFrame {

	private static final long serialVersionUID = 1L;
	public JPanel contentPane;
	public JTextField txtfUsuario;
	public JPasswordField txtfPassword;
	public JPanel panelInfo;
	public JButton btnExecute;
	public JTextArea txtConsulta;
	public JTextArea txtInfoPanel;
	public JButton btnIniciarSesion;
	public JButton btnCerrarSesion;
	public JButton btnCerrarBDD;
	public JTable tableResult;
	public static DefaultTableModel tableModel;
	private JScrollPane scrollTable;
	public JPanel panelLogin;
	private JScrollPane scrollQuery;

//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    Vista frame = new Vista();
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

	public Vista() {
		initComponents();
	}

	/**
	 * Inicializa y configura los componentes de la interfaz de usuario.
	 */
	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 779, 476);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		panelLogin = new JPanel();
		panelLogin.setBounds(20, 20, 411, 56);
		contentPane.add(panelLogin);
		panelLogin.setLayout(null);

		btnIniciarSesion = new JButton("Iniciar sesion");
		btnIniciarSesion.setBounds(265, 22, 136, 23);
		panelLogin.add(btnIniciarSesion);

		txtfUsuario = new JTextField();
		txtfUsuario.setBounds(10, 24, 111, 20);
		panelLogin.add(txtfUsuario);
		txtfUsuario.setText("Usuario");
		txtfUsuario.setToolTipText("");
		txtfUsuario.setColumns(10);
		txtfUsuario.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtfUsuario.getText().equals("Usuario")) {
					txtfUsuario.setText("");
					txtfUsuario.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtfUsuario.getText().isEmpty()) {
					txtfUsuario.setText("Usuario");
					txtfUsuario.setForeground(Color.GRAY);
				}
			}
		});

		txtfPassword = new JPasswordField();
		txtfPassword.setBounds(131, 24, 111, 20);
		panelLogin.add(txtfPassword);
		txtfPassword.setText("Password");
		txtfPassword.setColumns(10);

		JLabel lblUser = new JLabel("Usuario:");
		lblUser.setBounds(10, 10, 111, 13);
		panelLogin.add(lblUser);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(131, 10, 111, 13);
		panelLogin.add(lblPassword);
		txtfPassword.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtfPassword.getText().equals("Password")) {
					txtfPassword.setText("");
					txtfPassword.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtfPassword.getText().isEmpty()) {
					txtfPassword.setText("Password");
					txtfPassword.setForeground(Color.GRAY);
				}
			}
		});

		panelInfo = new JPanel();
		panelInfo.setBounds(20, 86, 724, 109);
		contentPane.add(panelInfo);
		panelInfo.setLayout(null);

		txtConsulta = new JTextArea();
		txtConsulta.setText("Escribe tu consulta...");
		txtConsulta.setBounds(10, 11, 704, 52);
		panelInfo.add(txtConsulta);

		btnExecute = new JButton("EXECUTE");
		btnExecute.setBounds(301, 73, 115, 23);
		panelInfo.add(btnExecute);

		scrollQuery = new JScrollPane(txtConsulta);
		scrollQuery.setBounds(10, 11, 704, 52);
		panelInfo.add(scrollQuery);

		txtConsulta.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtConsulta.getText().equals("Escribe tu consulta...")) {
					txtConsulta.setText("");
					txtConsulta.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtConsulta.getText().isEmpty()) {
					txtConsulta.setText("Escribe tu consulta...");
					txtConsulta.setForeground(Color.GRAY);
				}
			}
		});

		btnCerrarSesion = new JButton("Cerrar Sesión");
		btnCerrarSesion.setBounds(432, 37, 149, 23);
		contentPane.add(btnCerrarSesion);

		btnCerrarBDD = new JButton("Desconectar BBDD");
		btnCerrarBDD.setBounds(595, 37, 149, 23);
		contentPane.add(btnCerrarBDD);
		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableResult = new JTable(tableModel);
		tableResult.setBounds(20, 205, 735, 224);
		contentPane.add(tableResult);

		scrollTable = new JScrollPane(tableResult);
		scrollTable.setBounds(20, 205, 724, 224);
		contentPane.add(scrollTable);

		mostrarRestoComponentes(false);
		setVisible(true);
	}

	/**
	 * Muestra u oculta componentes específicos de la interfaz de usuario.
	 *
	 * @param mostrar Indica si se deben mostrar (true) u ocultar (false) los
	 *                componentes.
	 */
	public void mostrarRestoComponentes(boolean mostrar) {
		txtConsulta.setVisible(mostrar);
		btnExecute.setVisible(mostrar);
		tableResult.setVisible(mostrar);
		panelInfo.setVisible(mostrar);
		btnCerrarSesion.setVisible(mostrar);
		btnCerrarBDD.setVisible(mostrar);
	}
}
