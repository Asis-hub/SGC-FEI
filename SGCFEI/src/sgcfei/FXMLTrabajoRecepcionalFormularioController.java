/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar campos que pueden ser ingresados y aquellos que se pueden ser modificado de trabajo recepcional.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de trabajo recepcional ademas de validar si este es un registro nuevo o es un registro a modificar.
 * cargaRecepcionalEdicion: Hace la cargar de los datos de trabajo recepcional para que sean modificados.
 * cargarCoDirectorDB: Método que permite rescatar la informacion de codirector para ser cargada en un combobox para su posterior selección.
 * cargarDirectorDB: Método que permite rescatar la informacion de director para ser cargada en un combobox para su posterior selección.
 * cargarEstudianteDB: Método que permite rescatar la informacion de estudiante para ser cargada en un combobox para su posterior selección. 
 * cargarSinodalDB: Método que permite rescatar la informacion de sinodal para ser cargada en un combobox para su posterior selección.
 * clicGuardar: Obtiene datos que han sido introducidos y los valida.
 * guardarTrabajoRecepcional: Método que realiza la inserción o actualización de la informacion que se ha proporcionado. 
 * getIndexListaCoDirector: Metodo que realiza la busqueda del codirector perteneciente al trabajo recepcional a modificar.
 * getIndexListaDirector: Metodo que realiza la busqueda del director perteneciente al trabajo recepcional a modificar.
 * getIndexListaEstudiante: Metodo que realiza la busqueda del estudiante perteneciente al trabajo recepcional a modificar.
 * getIndexListaSinodal: Metodo que realiza la busqueda del codirector perteneciente al trabajo recepcional a modificar.
 */

package sgcfei;

import db.ConexionDB;
import interfaz.NotificaCambios;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import pojos.Estudiante;
import pojos.Personal;
import pojos.TrabajoRecepcional;
import util.Herramientas;

public class FXMLTrabajoRecepcionalFormularioController implements Initializable{

    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfFechaRegistro;
    @FXML
    private TextField tfTrabajoRecepcional;
    @FXML
    private TextArea tfDescripcion;
    @FXML
    private ComboBox<Estudiante> cbEstudiante;
    @FXML
    private ComboBox<Personal> cbDirector;
    @FXML
    private ComboBox<Personal> cbCoDirector;
    @FXML
    private ComboBox<Personal> cbSinodal; 
    private ObservableList<Estudiante> estudiantes;
    private ObservableList<Personal> directores; 
    private ObservableList<Personal> codirectores; 
    private ObservableList<Personal> sinodales; 
    private NotificaCambios notificacion;
    private boolean isNuevo;
    private TrabajoRecepcional recepcionalEdicion;
    Alert alertConexion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        estudiantes = FXCollections.observableArrayList();
        directores = FXCollections.observableArrayList();
        codirectores = FXCollections.observableArrayList();
        sinodales = FXCollections.observableArrayList();
        
        cargaEstudianteDB();
        cargaDirectorDB();
        cargaCoDirectorDB();
        cargaSinodalDB();
    }
    
    public void inicializaCampos(NotificaCambios notificacion, boolean isNuevo, TrabajoRecepcional recepcionalEdicion){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.recepcionalEdicion = recepcionalEdicion;
        if(!isNuevo){
            cargarRecepcionalEdicion();
        }
    }
    
    private void cargarRecepcionalEdicion(){
        lbTitulo.setText("Editar Trabajo Recepcional de " + recepcionalEdicion.getEstudiante());
        tfTrabajoRecepcional.setText(recepcionalEdicion.getNombre());
        tfDescripcion.setText(recepcionalEdicion.getDescripcion());
        tfFechaRegistro.setText(recepcionalEdicion.getFechaRegistro().toString());
        int posEstudiante = getIndexListaEstudiante(recepcionalEdicion.getIdEstudiante());
        cbEstudiante.getSelectionModel().select(posEstudiante);
        int posDirector = getIndexListaDirector(recepcionalEdicion.getIdDirector());
        cbDirector.getSelectionModel().select(posDirector);
        int posCoDirector = getIndexListaCoDirector(recepcionalEdicion.getIdCoDirector());
        cbCoDirector.getSelectionModel().select(posCoDirector);
        int posSinodal = getIndexListaSiondal(recepcionalEdicion.getIdSinodal());
        cbSinodal.getSelectionModel().select(posSinodal);
    }
    
    private void cargaEstudianteDB(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idEstudiante, nombre FROM estudiante";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Estudiante est = new Estudiante();
                    est.setIdEstudiante(rs.getInt("idEstudiante"));
                    est.setNombre(rs.getString("nombre"));
                    estudiantes.add(est);
                }
                cbEstudiante.setItems(estudiantes);
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
    
    private void cargaDirectorDB(){
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
                    directores.add(per);
                }
                cbDirector.setItems(directores);
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
    
    private void cargaCoDirectorDB(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idAcademico, nombre, apellidos FROM academico";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Personal codir = new Personal();
                    codir.setIdAcademico(rs.getInt("idAcademico"));
                    codir.setNombre(rs.getString("nombre") + " " + rs.getString("apellidos"));
                    codirectores.add(codir);
                }
                cbCoDirector.setItems(codirectores);
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
    
    private void cargaSinodalDB(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idAcademico, nombre, apellidos FROM academico";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Personal sin = new Personal();
                    sin.setIdAcademico(rs.getInt("idAcademico"));
                    sin.setNombre(rs.getString("nombre") + " " + rs.getString("apellidos"));
                    sinodales.add(sin);
                }
                cbSinodal.setItems(sinodales);
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
        String recepcionalAux = tfTrabajoRecepcional.getText();
        String descripcionAux = tfDescripcion.getText(); 
        String fechaRegistro = tfFechaRegistro.getText();
        String fechaDefault = "2020-01-01";
        Date fechaAux = Date.valueOf(fechaDefault);
        try {
            fechaAux = Date.valueOf(fechaRegistro);
        } catch (Exception e) {
            isValido = false;
            alertConexion = Herramientas.builderAlert("Fecha inválida", 
                "Por favor ingresa una fecha válida...", Alert.AlertType.ERROR);
            alertConexion.showAndWait();
        }                
        int posEstudiante = cbEstudiante.getSelectionModel().getSelectedIndex();
        int posDirector = cbDirector.getSelectionModel().getSelectedIndex();
        int posCoDirector = cbCoDirector.getSelectionModel().getSelectedIndex();
        int posSinodal = cbSinodal.getSelectionModel().getSelectedIndex();
        
        if(recepcionalAux.isEmpty()){
            isValido = false;
        }
        if(descripcionAux.isEmpty()){
           isValido = false;
        }
        if(fechaAux.toString().isEmpty()){
           isValido = false;
        }
        if(posEstudiante < 0){
            isValido = false;
        }
        if(posDirector < 0){
            isValido = false;
        }
        if(posCoDirector < 0){
            isValido = false;
        }
        if(posSinodal < 0){
            isValido = false;
        }
        if(isValido){
            guardarTrabajoRecepcional(recepcionalAux, descripcionAux, fechaAux, estudiantes.get(posEstudiante).getIdEstudiante()
                ,directores.get(posDirector).getIdAcademico(), codirectores.get(posCoDirector).getIdAcademico(), sinodales.get(posSinodal).getIdAcademico());
        }else{
            alertConexion = Herramientas.builderAlert("Campos obligatorios", 
                "Por favor llena todos los campos para continuar...", Alert.AlertType.ERROR);
            alertConexion.showAndWait();
        }
    }
    
    private void guardarTrabajoRecepcional(String recepcionalAux, String descripcionAux, Date fechaAux, int idEstudiante, int idDirector, int idCoDirector, int idSinodal) {
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String mensaje = "";
                int resultado;
                if(isNuevo){ 
                    String consulta = "INSERT INTO trabajorecepcional(nombre, descripcion, fechaRegistro, idEstudiante, idDirector, idCoDirector, idSinodal) VALUES (?, ?, ?, ?, ?, ?, ?);";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, recepcionalAux);
                    ps.setString(2, descripcionAux);
                    ps.setString(3, fechaAux.toString());
                    ps.setInt(4, idEstudiante);
                    ps.setInt(5, idDirector);
                    ps.setInt(6, idCoDirector);
                    ps.setInt(7, idSinodal);
                    resultado = ps.executeUpdate();
                    mensaje = "Trabajo Recepcional registrado con éxito...";
                }else{          
                    String consulta = "UPDATE trabajorecepcional SET nombre = ?, descripcion = ?, fechaRegistro = ?, idEstudiante = ?, idDirector = ?, idCoDirector = ?, idSinodal = ? WHERE idTrabajoRecepcional = ?;";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, recepcionalAux);
                    ps.setString(2, descripcionAux);
                    ps.setString(3, fechaAux.toString());
                    ps.setInt(4, idEstudiante);
                    ps.setInt(5, idDirector);
                    ps.setInt(6, idCoDirector);
                    ps.setInt(7, idSinodal);
                    ps.setInt(8, recepcionalEdicion.getIdTrabajoRecepcional());
                    resultado = ps.executeUpdate();
                    mensaje = "Trabajo Recepcional editado con éxito...";
                }
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Operación exitosa", mensaje, Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();                 
                    Stage stage = (Stage) tfTrabajoRecepcional.getScene().getWindow();
                    stage.close();
                    notificacion.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error en el registro", "el trabajo recepcional no pudo ser guardado en el sistema",
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
    
    private int getIndexListaEstudiante(Integer idEstudiante) {
        int value = 0;
        if(estudiantes.size() > 0){
            for(int i = 0; i < estudiantes.size(); i++){
                Estudiante get = estudiantes.get(i);
                if(get.getIdEstudiante() == idEstudiante){
                    return i;
                }
            }
        }
        return value;
    }
    
    private int getIndexListaDirector(Integer idDirector) {
        int value = 0;
        if(directores.size() > 0){
            for(int i = 0; i < directores.size(); i++){
                Personal get = directores.get(i);
                if(get.getIdAcademico() == idDirector){
                    return i;
                }
            }
        }
        return value;
    }
    
    private int getIndexListaCoDirector(Integer idCoDirector) {
        int value = 0;
        if(codirectores.size() > 0){
            for(int i = 0; i < codirectores.size(); i++){
                Personal get = codirectores.get(i);
                if(get.getIdAcademico() == idCoDirector){
                    return i;
                }
            }
        }
        return value;
    }
    
    private int getIndexListaSiondal(Integer idSinodal) {
        int value = 0;
        if(sinodales.size() > 0){
            for(int i = 0; i < sinodales.size(); i++){
                Personal get = sinodales.get(i);
                if(get.getIdAcademico() == idSinodal){
                    return i;
                }
            }
        }
        return value;
    }   
}
