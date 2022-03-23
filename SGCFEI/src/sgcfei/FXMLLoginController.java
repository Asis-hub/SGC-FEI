/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de veriricar y validar la entrada de los usuarios al sistema.
 */

/*
 *Lista de Contenidos
 * Metodos:
 * clicIniciarSesion: Se encarga validar que no se dejen campos vacios y de obtener las credencias ingresadas.
 * consultaCredencialesBD: Hace la verificación de que las credenciales ingresadas se encuentren en la BD para permitir el ingreso al sistema. 
 * irMenuPrincipal: Rediccionamiento hacia la vista principal del sistema.
 */

package sgcfei;

import db.ConexionDB;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.Herramientas;

public class FXMLLoginController implements Initializable{
    @FXML
    private TextField tfUser;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lbErrorUser;
    @FXML
    private Label lbErrorPassword;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
    }    
 
    @FXML
    private void clicIniciarSesion(ActionEvent event){
        lbErrorUser.setText("");
        lbErrorPassword.setText("");
        String txtUsuario = tfUser.getText();
        String txtPassword = pfPassword.getText();
        boolean isCorrecto = true;
        if(txtUsuario.isEmpty()){
            lbErrorUser.setText("Campo usuario obligatorio");
            isCorrecto = false;
        }
        if(txtPassword.isEmpty()){
            lbErrorPassword.setText("Campo contraseña obligatorio");
            isCorrecto = false;
        }
        if(isCorrecto){
           consultaCredencialesBD(txtUsuario, txtPassword);
        }
    }
    
    private void consultaCredencialesBD(String username, String password){
        Connection conn = ConexionDB.abrirConexionMySQL();
        Alert alertConexion;
        if(conn != null){
            try{
                String consulta = "SELECT * FROM usuario a, academico b WHERE username = ? AND password = ? AND a.idAcademico = b.idAcademico";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet resultadoDB = ps.executeQuery();
                if(resultadoDB.next()){
                    String nombreUser = resultadoDB.getString("nombre");
                    String apellidosUser = resultadoDB.getString("apellidos");
                    alertConexion = Herramientas.builderAlert("Usuario encontrado", "Bienvenido " +nombreUser+ " " + apellidosUser + " al sistema.", Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();
                    irMenuPrincipal();
                }else{
                   alertConexion = Herramientas.builderAlert("Datos incorrectos", "Tu usuario y/o contraseña son incorrectos, favor de identificarlos"
                    , Alert.AlertType.ERROR);
                   alertConexion.showAndWait();
                }
                conn.close();
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error en consulta", ex.getMessage() ,
                    Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }
        }else{
            alertConexion = Herramientas.builderAlert("Error de conexión", "No se puede conectar con la base de datos en este momento",
                Alert.AlertType.ERROR);
            alertConexion.showAndWait();
        }
    }

    private void irMenuPrincipal(){
        try{
            Stage stage = (Stage) tfUser.getScene().getWindow();
            Scene scenePrincipal = new Scene(FXMLLoader.load(getClass().getResource("FXMLMenuPrincipal.fxml")));
            stage.setScene(scenePrincipal);
            stage.show();
        }catch(IOException ex){
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
}
