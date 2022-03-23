/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar campos que pueden ser ingresados y aquellos que se pueden ser modificado de una personal.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de personal ademas de validar si este es un registro nuevo o es un registro a modificar.
 * cargaAcademiaEdicion: Hace la cargar de los datos de academia para que sean modificados.
 * cargaPersonal: Método que permite rescatar la informacion de personal para ser cargada en un combobox para su posterior selección.
 * clicGuardar: Obtiene datos que han sido introducidos y los valida.
 * guardarPersonal: Método que realiza la inserción o actualización de la informacion que se ha proporcionado. 
 */

package sgcfei;

import db.ConexionDB;
import interfaz.NotificaCambios;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Personal;
import util.Herramientas;


public class FXMLPersonalFormularioController implements Initializable{
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfNoPersonal;
    @FXML
    private TextField tfApellidos;
    @FXML
    private TextField tfCorreo;
    @FXML
    private Label lbTitulo;
    private NotificaCambios notificacion;
    private boolean isNuevo;
    private Personal personalEdicion;
    Alert alertConexion;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
    }    
    
    public void inicializaCampos(NotificaCambios notificacion, boolean isNuevo, Personal personalEdicion){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.personalEdicion = personalEdicion;
        if(!isNuevo){
            cargaPersonalEdicion();
        }
    }
    
    private void cargaPersonalEdicion(){
        tfNoPersonal.setText(personalEdicion.getNumeroPersonal());
        tfNombre.setText(personalEdicion.getNombre());
        tfApellidos.setText(personalEdicion.getApellidos());
        tfCorreo.setText(personalEdicion.getCorreo());
        lbTitulo.setText("Editar académico " + personalEdicion.getNombre() + " " + personalEdicion.getApellidos());
    }
    
    @FXML
    private void clicGuardar(ActionEvent event){
        boolean isValido = true;
        String noPersonalAux = tfNoPersonal.getText();
        String nombreAux = tfNombre.getText();
        String apellidosAux = tfApellidos.getText();
        String correoAux = tfCorreo.getText();
        if(noPersonalAux.isEmpty() || !noPersonalAux.matches("[0-9]+") || noPersonalAux.length() > 6 || noPersonalAux.length() < 6){
            isValido = false;
        }
        if(nombreAux.isEmpty()){
           isValido = false;
        }if(apellidosAux.isEmpty()){
            isValido = false;
        }
        if(correoAux.isEmpty() || !correoAux.contains("@")){
           isValido = false;            
        }
        if(isValido){
            guardarPersonal(noPersonalAux, nombreAux, apellidosAux, correoAux);
        }else{
            if(!correoAux.contains("@")){
                alertConexion = Herramientas.builderAlert("Campos obligatorios", 
                    "Por favor llena todos los campos para continuar...", Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }else{
                if(!noPersonalAux.matches("[0-9]+") || noPersonalAux.length() > 6 || noPersonalAux.length() < 6){
                alertConexion = Herramientas.builderAlert("Numero de Personal inválido", 
                    "Por favor ingresa número de personal válido con 6 números...", Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }else{
                if(!isValido){
                alertConexion = Herramientas.builderAlert("Correo inválido", 
                    "Por favor ingresa un correo electrónico válido...", Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }}}
        }
    }
    
    private void guardarPersonal(String noPersonalAux, String nombreAux, String apellidosAux, String correoAux){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try {
                String mensaje = "";
                int resultado;
                if(isNuevo){
                    String consulta = "INSERT INTO academico(numeroPersonal, nombre, apellidos, correo) VALUES (?, ?, ?, ?);";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, noPersonalAux);
                    ps.setString(2, nombreAux);
                    ps.setString(3, apellidosAux);
                    ps.setString(4, correoAux);
                    resultado = ps.executeUpdate();
                    mensaje = "Académico registrado con éxito...";
                }else{
                    String consulta = "UPDATE academico SET numeroPersonal = ? , nombre = ? , apellidos = ? , correo = ? WHERE idAcademico = ?;";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, noPersonalAux);
                    ps.setString(2, nombreAux);
                    ps.setString(3, apellidosAux);
                    ps.setString(4, correoAux);
                    ps.setInt(5, personalEdicion.getIdAcademico());
                    resultado = ps.executeUpdate();
                    mensaje = "Académico editado con éxito...";
                }
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Operación exitosa", mensaje, Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();                 
                    Stage stage = (Stage) tfNoPersonal.getScene().getWindow();
                    stage.close();                    
                    notificacion.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error en el registro", "el académico no pudo ser guardado en el sistema",
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
}
