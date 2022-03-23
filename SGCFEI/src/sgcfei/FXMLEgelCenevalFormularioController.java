/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar campos que pueden ser ingresados y aquellos que se pueden ser modificado de una EGEL-CENEVAL.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de academia ademas de validar si este es un registro nuevo o es un registro a modificar.
 * cargaCenavalEdicion: Hace la cargar de los datos de EGEL-CENEVAL para que sean modificados.
 * cargarEstudianteDB: Método que permite rescatar la informacion de estudiante para ser cargada en un combobox para su posterior selección.
 * clicGuardar: Obtiene datos que han sido introducidos y los valida.
 * guardarCeneval: Método que realiza la inserción o actualización de la informacion que se ha proporcionado. 
 * getIndexListaEstudiante: Metodo que realiza la busqueda del estudiante perteneciente al EGEL-CENEVAL a modificar.
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Ceneval;
import pojos.Estudiante;
import util.Herramientas;



public class FXMLEgelCenevalFormularioController implements Initializable {

    @FXML
    private TextField tfPeriodo;
    @FXML
    private ComboBox<Estudiante> cbEstudiante;
    @FXML
    private TextField tfFechaExamen;
    @FXML
    private TextField tfPuntaje;
    @FXML
    private Label lbTitulo;
    private ObservableList<Ceneval> cenevales;
    private ObservableList<Estudiante> estudiantes;
    
    Alert alertConexion;
    
    private NotificaCambios notificacion;
    private Boolean isNuevo;
    private Ceneval cenevalEdicion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cenevales = FXCollections.observableArrayList();
        estudiantes = FXCollections.observableArrayList();
        cargaEstudiantesBD();
    }

    public void inicializaCampos(NotificaCambios notificacion, boolean isNuevo, Ceneval cenevalesEdicion){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.cenevalEdicion = cenevalesEdicion;
        if(!isNuevo){
            cargaCenevalesEdicion();
        }
    }
    
    private void cargaCenevalesEdicion(){
        String puntaje = Float.toString(cenevalEdicion.getPuntaje());
        String idCeneval = Integer.toString(cenevalEdicion.getIdCeneval());
        tfFechaExamen.setText(cenevalEdicion.getFechaExamen().toString());
        tfPeriodo.setText(cenevalEdicion.getPeriodo());
        tfPuntaje.setText(puntaje);
        lbTitulo.setText("Editar Ceneval No: " + idCeneval);
        int posEstudiante = getIndexListaEstudiante(cenevalEdicion.getIdEstudiante());
        cbEstudiante.getSelectionModel().select(posEstudiante);
    }
    
    private void cargaEstudiantesBD(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idEstudiante, nombre FROM estudiante";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Estudiante estudiante = new Estudiante();
                    estudiante.setIdEstudiante(rs.getInt("idEstudiante"));
                    estudiante.setNombre(rs.getString("nombre"));
                    estudiantes.add(estudiante);
                }
                cbEstudiante.setItems(estudiantes);
                conn.close();
            }catch(SQLException e){
                alertConexion = Herramientas.builderAlert("Error de consulta", e.getMessage(), Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }
        }else{
            alertConexion = Herramientas.builderAlert("Error de conexion", "No se puede conectar la base de datos en este momento", Alert.AlertType.ERROR);
                alertConexion.showAndWait();
        }
    }
  

    @FXML
    private void clicGuardar(ActionEvent event) {
        boolean isValido = true;
        int posEstudiante = cbEstudiante.getSelectionModel().getSelectedIndex();
        String fecha = tfFechaExamen.getText();
        String fechaDefault = "2020-01-01";
        Date fechaAux = Date.valueOf(fechaDefault);
        try {
            fechaAux = Date.valueOf(fecha);
        } catch (Exception e) {
            isValido = false;
            alertConexion = Herramientas.builderAlert("Fecha inválida", 
                    "Por favor ingresa una fecha válido...", Alert.AlertType.ERROR);
            alertConexion.showAndWait();
        }
        String periodoAux = tfPeriodo.getText();
        String puntajeST = tfPuntaje.getText();
        float puntajeAux = 0;
        try {
            puntajeAux = Float.parseFloat(puntajeST);
        } catch (Exception e) {
            isValido = false;
            alertConexion = Herramientas.builderAlert("Puntaje inválido", 
                    "Por favor ingresa un puntaje válido...", Alert.AlertType.ERROR);
            alertConexion.showAndWait();
        }
        if(posEstudiante < 0){
            isValido = false;
        }
        if(fechaAux.toString().isEmpty()){
            isValido = false;
        }
        if(periodoAux.isEmpty()){
            isValido = false;
        }
        if(puntajeAux < 0 || puntajeAux > 10){
            isValido=false;
        }
        if(isValido){
            guardarCeneval(estudiantes.get(posEstudiante).getIdEstudiante(),fechaAux, periodoAux, puntajeAux);
        }else{
            if(puntajeAux < 0 || puntajeAux > 10){
                alertConexion = Herramientas.builderAlert("Puntaje inválido", 
                    "Por favor ingresa un puntaje válido...", Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }else{
                if(!isValido){
                alertConexion = Herramientas.builderAlert("Campos obligatorios", 
                    "Por favor llena todos los campos para continuar...", Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }}}
    }
    
    private void guardarCeneval(int idEstudiante, Date fechaAux, String periodoAux, float puntaje){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String mensaje = "";
                int resultado;
                if(isNuevo){
                    String consulta = "INSERT INTO ceneval(idEstudiante, fechaExamen, periodo, puntaje) VALUES (?, ?, ?, ?);";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setInt(1, idEstudiante);
                    ps.setDate(2, fechaAux);
                    ps.setString(3, periodoAux);
                    ps.setFloat(4, puntaje);
                    resultado = ps.executeUpdate();
                    mensaje = "EGEL-CENEVAL registrado con éxito...";
                }else{
                    String consulta = "UPDATE ceneval SET idEstudiante = ? , fechaExamen = ? , periodo = ?, puntaje = ? WHERE idCeneval = ?;";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setInt(1, idEstudiante);
                    ps.setDate(2, fechaAux);
                    ps.setString(3, periodoAux);
                    ps.setFloat(4, puntaje);
                    ps.setInt(5, cenevalEdicion.getIdCeneval());
                    resultado = ps.executeUpdate();
                    mensaje = "EGEL-CENEVAL editado con éxito...";
                }
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("operacion exitosa", mensaje, Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();
                    Stage stage = (Stage) tfFechaExamen.getScene().getWindow();
                    stage.close();                   
                    notificacion.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("operacion exitosa", "a Academia no pudo ser guardado en el sistema", Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
                }
            }catch(SQLException e){
                    alertConexion = Herramientas.builderAlert("Error de consulta", e.getMessage(), Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
            }finally{
                try{
                   conn.close();
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }else{
            alertConexion = Herramientas.builderAlert("Error de consulta", "No se puede conectar con la base de datos en este momento", Alert.AlertType.ERROR);
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
}