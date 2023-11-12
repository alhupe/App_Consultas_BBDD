package Evaluable;

import org.w3c.dom.*;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.xml.parsers.*;
import java.io.*;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Modelo {
	private static Connection dbConnection;
	private  String tipoUsuario;

	/**
    * Constructor de la clase Modelo.
    */
	public Modelo() {
	}

	/**
 * Método para conectar a la base de datos utilizando la información del archivo XML.
 * 
 * @param xmlFile El nombre del archivo XML que contiene los detalles de la conexión.
 * @return Verdadero si la conexión se establece con éxito, falso si hay un error.
 */

	public boolean connectToDatabase(String xmlFile) {
		try {
			String path = System.getProperty("user.dir") + File.separator + xmlFile;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(new File(path));
			//Element raiz = document.getDocumentElement();
			NodeList nodeList = document.getElementsByTagName("conexion");
			Node conexionNode = nodeList.item(0);
			if (conexionNode.getNodeType() == Node.ELEMENT_NODE) {
				Element conexionElement = (Element) conexionNode;

				String url = conexionElement.getElementsByTagName("url").item(0).getTextContent();
				Class.forName("com.mysql.cj.jdbc.Driver");
				String username = conexionElement.getElementsByTagName("username").item(0).getTextContent();
				String password = conexionElement.getElementsByTagName("password").item(0).getTextContent();

				dbConnection = DriverManager.getConnection(url, username, password);
				return true;
			} else {
				System.out.println("Error al leer el nodo de conexión en el archivo XML.");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	* Pregunta al usuario si está seguro de cerrar la sesión.
	* 
	* @return Verdadero si el usuario confirma el cierre de sesión, falso si no.
	*/
	public boolean confirmCloseConexion() {
		int confirmation = JOptionPane.showConfirmDialog(null, "¿Estás seguro de cerrar la sesión?", "Cerrar Sesión",
				JOptionPane.YES_NO_OPTION);
		return confirmation == JOptionPane.YES_OPTION;
	}

	/**
	* Cierra la conexión con la base de datos.
	*/
	public void closeConexion() {
		try {
			if (dbConnection != null && !dbConnection.isClosed()) {
				dbConnection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	* Verifica las credenciales de inicio de sesión del usuario en la base de datos.
	* 
	* @param usuario     El nombre de usuario.
	* @param password  La password del usuario.
	* @return Verdadero si el usuario y la password coinciden, falso en caso contrario.
	*/

	public boolean verificarUsuario(String usuario, String password) {
		try {
			String query = "SELECT type, pass FROM users WHERE user=?";
			PreparedStatement statement = dbConnection.prepareStatement(query);
			statement.setString(1, usuario);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String hashFromDB = resultSet.getString("pass");
				String hashedPassword = getMD5(password);
				tipoUsuario = resultSet.getString("type");
				if (hashFromDB.equals(hashedPassword)) {
					if (tipoUsuario.equals("admin")) {
						dbConnection.close();
						connectToDatabase("admin.xml");
					}
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	* Calcula el valor hash MD5 de una cadena.
	* 
	* @param input La cadena para calcular el hash MD5.
	* @return El valor hash MD5.
	*/

	public String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	* Limpia la tabla de resultados.
	*/

	public void cleanTable() {
		Vista.tableModel.setRowCount(0);
		Vista.tableModel.setColumnCount(0);
	}

	/**
	* Muestra los resultados de una consulta en una tabla y gestiona diferentes tipos de consultas.
	* 
	* @param query       La consulta SQL.
	* @param txtInfoPanel El área de texto para mostrar información.
	*/

	public void displayQueryResults(String query, JTextArea txtInfoPanel) {
		try {
			cleanTable();
			if(tipoUsuario.equals("client") && (query.toLowerCase().contains("update") || query.toLowerCase().contains("delete") || query.toLowerCase().contains("insert"))) {
				JOptionPane.showMessageDialog(null,"No tienes permisos para realizar esa operacion.");
			}
			else {
			
			PreparedStatement statement = dbConnection.prepareStatement(query);
			boolean hasResultSet = statement.execute();
			
			if (!hasResultSet) {
				int rowsAffected = statement.getUpdateCount();

				if (rowsAffected != -1) {
					int confirmation = JOptionPane.showConfirmDialog(null,
							"¿Estás seguro de realizar esta modificación?", "Confirmación", JOptionPane.YES_NO_OPTION);

					if (confirmation == JOptionPane.YES_OPTION) {
						JOptionPane.showMessageDialog(null,"Operación completada exitosamente.");
					} else {
						JOptionPane.showMessageDialog(null, "Operación cancelada por el usuario.");
					}
				}
			} else {
				ResultSet resultSet = statement.getResultSet();
				ResultSetMetaData metaData = resultSet.getMetaData();

				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					Vista.tableModel.addColumn(metaData.getColumnName(i));
				}

				while (resultSet.next()) {
					Object[] row = new Object[metaData.getColumnCount()];
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						row[i - 1] = resultSet.getObject(metaData.getColumnName(i));
					}
					Vista.tableModel.addRow(row);
				}
				JOptionPane.showMessageDialog(null, "Consulta realizada exitosamente.");

				resultSet.close();
			}

			statement.close();
		}} catch (SQLException e) {
            logError(e); // Llamamos a la función para registrar el error
			System.out.println(e.getMessage());
            if (e.getMessage().toLowerCase().contains("syntax")) {
                JOptionPane.showMessageDialog(null, "Error de sintaxis en la consulta");
            } else {
                if (e.getMessage().toLowerCase().contains("insufficient privileges")) {
                    JOptionPane.showMessageDialog(null, "No tienes permisos para realizar esta operación");
                } else {
                    JOptionPane.showMessageDialog(null, "Error en la consulta: " + e.getMessage());
                }
            }
        }
	}

	/**
	* Registra un error.
	* 
	* @param e La excepción SQLException.
	*/
    private void logError(SQLException e) {
        String errorMessage = "SQLState: " + e.getSQLState() + " - Message: " + e.getMessage();
        System.err.println(errorMessage); // Imprimir el error en la consola como ejemplo
    }

}
