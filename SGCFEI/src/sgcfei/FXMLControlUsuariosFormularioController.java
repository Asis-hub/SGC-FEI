/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar campos que pueden ser ingresados y aquellos que pueden ser modificados de un usuario.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de usuario ademas de validar si este es un registro nuevo o es un registro a modificar.
 * cargaUsuarioEdicion: Hace la cargar de los datos de usuario para que sean modificados.
 * cargarPersonalDB: Método que permite rescatar la informacion de personal para ser cargada en un combobox para su posterior selección.
 * cargarRolDB: Método que permite rescatar la informacion de rol para ser cargada en un combobox para su posterior selección.
 * clicGuardar: Obtiene datos que han sido introducidos y los valida.
 * guardarUsuario: Método que realiza la inserción o actualización de la informacion que se ha proporcionado. 
 * getIndexListaPersonal: Metodo que realiza la busqueda de personal perteneciente al usuario a modificar.
* getIndexListaRol: Metodo que realiza la busqueda del rol perteneciente al usuario a modificar.
 */

package sgcfei;

import db.ConexionDB;
import interfaz.NotificaCambios;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Personal;
import pojos.Rol;
import pojos.Usuario;
import util.Herramientas;

public class FXMLControlUsuariosFormularioController implements Initializable{
    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfUsuario;
    @FXML
    private ComboBox<Personal> cbPersonal;
    @FXML
    private ComboBox<Rol> cbRol;
    private ObservableList<Personal> personales;
    private ObservableList<Rol> roles;
    private NotificaCambios notificacion;
    private boolean isNuevo;
    private Usuario usuarioEdicion;
    
    Alert alertConexion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        personales = FXCollections.observableArrayList();
        roles = FXCollections.observableArrayList();
        cargaPersonalDB();
        cargaRolDB();  
    }

    public void inicializaCampos(NotificaCambios notificacion, boolean isNuevo, Usuario usuarioEdicion){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.usuarioEdicion = usuarioEdicion;
        if(!isNuevo){
           cargaUsuarioEdicion();
        }
    }
    
    private void cargaUsuarioEdicion(){
        tfUsuario.setText(usuarioEdicion.getUsername());
        tfPassword.setText(usuarioEdicion.getPassword());
        lbTitulo.setText("Editar Usuario de " + usuarioEdicion.getAcademico());
        int posPersonal = getIndexListaPersonal(usuarioEdicion.getIdUsuario());
        cbPersonal.getSelectionModel().select(posPersonal);
        int posRol = getIndexListaRol(usuarioEdicion.getIdRol());
        cbRol.getSelectionModel().select(posRol);
    }
    
    private void cargaPersonalDB(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idAcademico, nombre, apellidos FROM academico";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Personal per = new Personal();
                    per.setIdAcademico(rs.getInt("idAcademico"));
                    per.setNombre(rs.getString("nombre") + " " + rs.getString("apellidos"));
                    personales.add(per);
                }
                cbPersonal.setItems(personales);
                conn.close();
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error en consulta", ex.getMessage(), Alert.AlertType.ERROR);
                alertConexion.showAndWait();                
           }
        }else{
            alertConexion = Herramientas.builderAlert("Error de conexión", "No se puede conectar con la base de datos en este momento", Alert.AlertType.NONE);
            alertConexion.showAndWait();
        }
    }
    
    private void cargaRolDB(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idRol, nombre FROM rol";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Rol rol = new Rol();
                    rol.setIdRol(rs.getInt("idRol"));
                    rol.setNombre(rs.getString("nombre"));
                    roles.add(rol); 
                }
                cbRol.setItems(roles);
                conn.close();
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error en consulta", ex.getMessage(), Alert.AlertType.ERROR);
                alertConexion.showAndWait();                
            }
        }else{
            alertConexion = Herramientas.builderAlert("Error de conexión", "No se puede conectar con la base de datos en este momento", Alert.AlertType.NONE);
            alertConexion.showAndWait();
        }
    }
    
    @FXML
    private void clicGuardar(ActionEvent event) {
        boolean isValido = true;
        String usernameAux = tfUsuario.getText();
        String passwordAux = tfPassword.getText();
        int posRol = cbRol.getSelectionModel().getSelectedIndex();
        int posPersonal = cbPersonal.getSelectionModel().getSelectedIndex();
        
        if(usernameAux.isEmpty()){
            isValido = false;
        }
        if(passwordAux.isEmpty()){
           isValido = false;
        }
        if(posRol < 0){
            isValido = false;
        }
        if(posPersonal < 0){
            isValido = false;
        }
        if(isValido){
            guardarUsuario(usernameAux, passwordAux, roles.get(posRol).getIdRol(), personales.get(posPersonal).getIdAcademico());
        }else{
            alertConexion = Herramientas.builderAlert("Campos obligatorios", 
                "Por favor llena todos los campos para continuar...", Alert.AlertType.ERROR);
            alertConexion.showAndWait();
        }
    }
    
    private void guardarUsuario(String usernameAux, String passwordAux, int idRol, int idAcademico) {
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String mensaje = "";
                int resultado;
                if(isNuevo){ 
                    String consulta = "INSERT INTO usuario(username, password, idRol, idAcademico) VALUES (?, ?, ?, ?);";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, usernameAux);
                    ps.setString(2, passwordAux);
                    ps.setInt(3, idRol);
                    ps.setInt(4, idAcademico);
                    resultado = ps.executeUpdate();
                    mensaje = "Usuario registrado con éxito...";
                }else{          
                    String consulta = "UPDATE usuario SET username = ? , password = ? , idRol = ? , idAcademico = ? WHERE idUsuario = ?;";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, usernameAux);
                    ps.setString(2, passwordAux);
                    ps.setInt(3, idRol);
                    ps.setInt(4, idAcademico);
                    ps.setInt(5, usuarioEdicion.getIdUsuario());
                    resultado = ps.executeUpdate();
                    mensaje = "Usuario editado con éxito...";
                }
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Operación exitosa", mensaje, Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();                 
                    Stage stage = (Stage) tfUsuario.getScene().getWindow();
                    stage.close();
                    notificacion.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error en el registro", "el usuario no pudo ser guardado en el sistema",
                        Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
                } 
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error de consulta", ex.getMessage() , Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }finally{
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }else{
            alertConexion = Herramientas.builderAlert("Error de conexión", "No se puede conectar con la base de datos en este momento",
                Alert.AlertType.ERROR);
            alertConexion.showAndWait();
        }
    }
    
    private int getIndexListaPersonal(Integer idAcademico) {
        int value = 0;
        if(personales.size() > 0){
            for(int i = 0; i < personales.size(); i++){
                Personal get = personales.get(i);
                if(get.getIdAcademico() == idAcademico){
                    return i;
                }
            }
        }
        return value;
    }
    
    private int getIndexListaRol(Integer idRol) {
        int value = 0;
        if(roles.size() > 0){
            for(int i = 0; i < roles.size(); i++){
                Rol get = roles.get(i);
                if(get.getIdRol() == idRol){
                    return i;
                }
            }
        }
        return value;
    }
}
    

