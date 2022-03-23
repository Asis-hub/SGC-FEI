/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar campos que pueden ser ingresados y aquellos que se pueden ser modificado de una LGCA.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de LGCA ademas de validar si este es un registro nuevo o es un registro a modificar.
 * cargalgcaEdicion: Hace la cargar de los datos de LGCA para que sean modificados.
 * cargarPersonalDB: Método que permite rescatar la informacion de personal para ser cargada en un combobox para su posterior selección.
 * clicGuardar: Obtiene datos que han sido introducidos y los valida.
 * guardarLGCA: Método que realiza la inserción o actualización de la informacion que se ha proporcionado. 
 * getIndexListaResponsable: Metodo que realiza la busqueda del reponsable perteneciente al LGCA a modificar.
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.LGCA;
import pojos.Personal;
import util.Herramientas;


public class FXMLLGCAFormularioController implements Initializable{
    @FXML
    private TextField tfClave;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfGrado;
    @FXML
    private TextArea ttDescripcion;
    @FXML
    private Label lbTitulo;
    @FXML
    private ComboBox<Personal> cbResponsable;
    
    private ObservableList<Personal> personales;
    
    Alert alertConexion;
    
    private NotificaCambios notificacion;
    private boolean isNuevo;
    private LGCA lgcaEdicion;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         personales = FXCollections.observableArrayList();  
        cargaPersonalBD();
    }
    
    public void inicializaCampos(NotificaCambios notificacion, boolean isNuevo, LGCA lgcaEdicion){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.lgcaEdicion = lgcaEdicion;
        if(!isNuevo){
            cargalgcaEdicion();
        }
    }

    private void cargalgcaEdicion() {
        tfClave.setText(lgcaEdicion.getClave());
        tfNombre.setText(lgcaEdicion.getLGCA());
        tfGrado.setText(lgcaEdicion.getGrado());
        ttDescripcion.setText(lgcaEdicion.getDesAdscripcion());
        lbTitulo.setText("Editar LGAC: " + lgcaEdicion.getLGCA());
        int posResponsable = getIndexListaResponsable(lgcaEdicion.getIdResponsable());
        cbResponsable.getSelectionModel().select(posResponsable);  
    }
    private void cargaPersonalBD() {
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT * FROM academico;";              
                PreparedStatement ps = conn.prepareStatement(consulta);                
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Personal personal = new Personal();
                    personal.setIdAcademico(rs.getInt("idAcademico"));
                    personal.setNombre(rs.getString("nombre"));
                    personal.setApellidos(rs.getString("apellidos"));
                    personales.add(personal);
                }   
                cbResponsable.setItems(personales);
                conn.close();
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error en consulta", ex.getMessage(),
                    Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }
        }else{
            alertConexion = Herramientas.builderAlert("Error de conexión", "No se puede conectar con la base de datos en este momento",
                Alert.AlertType.ERROR);
            alertConexion.showAndWait();
        }
    }
    
    @FXML
    private void clicGuardar(ActionEvent event) {
        boolean isValido = true;
        String claveAux = tfClave.getText();
        String nombreAux = tfNombre.getText();
        String gradoAux = tfGrado.getText();
        String descripcionAux = ttDescripcion.getText();
        int posResponsable = cbResponsable.getSelectionModel().getSelectedIndex();      
        
        if(claveAux.isEmpty()){
            isValido = false;
        }
        if(nombreAux.isEmpty()){
            isValido = false;
        }
        if(gradoAux.isEmpty()){
            isValido = false;
        }
        if(descripcionAux.isEmpty()){
            isValido = false;
        }
        if(posResponsable < 0){
            isValido = false;
        }
        if(isValido){
            guardarLGCA(claveAux, nombreAux, gradoAux,descripcionAux,personales.get(posResponsable).getIdAcademico());
        }else{
            alertConexion = Herramientas.builderAlert("Campos obligatorios", 
                "Por favor llena todos los campos para continuar...", Alert.AlertType.ERROR);
            alertConexion .showAndWait();
        }
    }
    
     private void guardarLGCA(String claveAux, String nombreAux, String gradoAux, String descripcionAux, Integer idAcademico){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String mensaje = "";
                int resultado; 
                if(isNuevo){
                    String consulta = "INSERT INTO  lgca(clave, grado, desAdscripcion, idResponsable, nombre) VALUES (?,?,?,?,?);";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1,claveAux);
                    ps.setString(2,gradoAux);
                    ps.setString(3, descripcionAux);
                    ps.setInt(4, idAcademico);
                    ps.setString(5,nombreAux);
                    resultado = ps.executeUpdate();
                    mensaje = "LGAC registrado con éxito...";
                }else{            
                    String consulta = "UPDATE lgca SET clave = ?, grado = ?, desAdscripcion = ?, idResponsable = ?, nombre = ? WHERE idLGCA = ?;";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1,claveAux);
                    ps.setString(2,gradoAux);
                    ps.setString(3, descripcionAux);
                    ps.setInt(4, idAcademico);
                    ps.setString(5,nombreAux);
                    ps.setInt(6, lgcaEdicion.getIdLGCA());
                    resultado = ps.executeUpdate();
                    mensaje = "LGAC editado con éxito...";
                }
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Operación exitosa", mensaje, Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();                 
                    Stage stage = (Stage) lbTitulo.getScene().getWindow();
                    stage.close();
                    notificacion.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error en el registro", "El LGAC no pudo ser guardado en el sistema",
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
 
    private int getIndexListaResponsable(Integer idResponsable) {
        int value = 0;
        if(personales.size() > 0){
            for(int i = 0; i < personales.size(); i++){
                Personal get = personales.get(i);
                if(get.getIdAcademico() == idResponsable){
                    return i;
                }
            }
        }
        return value;
    }   
}
