/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar campos que pueden ser ingresados y aquellos que se pueden ser modificado de una academia.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de academia ademas de validar si este es un registro nuevo o es un registro a modificar.
 * cargaAcademiaEdicion: Hace la cargar de los datos de academia para que sean modificados.
 * cargarCoordinadorDB: Método que permite rescatar la informacion de coordinador para ser cargada en un combobox para su posterior selección.
 * clicGuardar: Obtiene datos que han sido introducidos y los valida.
 * guardarAcademia: Método que realiza la inserción o actualización de la informacion que se ha proporcionado. 
 * getIndexListaCoordinador: Metodo que realiza la busqueda del coordinador perteneciente ala academia a modificar.
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
import pojos.Academia;
import pojos.Personal;
import util.Herramientas;


public class FXMLAcademiaFormularioController implements Initializable{
    @FXML
    private TextArea tfDescripcionAcademia;
    @FXML
    private TextField tfAcademia;
    @FXML
    private ComboBox<Personal> cbCoordinador;
    @FXML
    private Label lbTitulo;
    private ObservableList<Personal> personales; 
    private NotificaCambios notificacion;
    private boolean isNuevo;
    private Academia academiaEdicion;
    Alert alertConexion;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personales = FXCollections.observableArrayList();
        cargaCoordinadorDB();
    }
    
    public void inicializaCampos(NotificaCambios notificacion, boolean isNuevo, Academia academiaEdicion){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.academiaEdicion = academiaEdicion;
        if(!isNuevo){
            cargaAcademiaEdicion();
        }
    }
    
    private void cargaAcademiaEdicion(){
        tfDescripcionAcademia.setText(academiaEdicion.getDescripcion());
        tfAcademia.setText(academiaEdicion.getNombre());
        lbTitulo.setText("Editar Academia " + academiaEdicion.getNombre());
        int posCoordinador = getIndexListaCoordinador(academiaEdicion.getIdCoordinador());
        cbCoordinador.getSelectionModel().select(posCoordinador);
    }
    
    private void cargaCoordinadorDB(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT a.idAcademico, nombre, apellidos FROM academico a, usuario b WHERE a.idAcademico = b.idAcademico AND b.idRol = 2;";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Personal coordinador = new Personal();
                    coordinador.setIdAcademico(rs.getInt("a.idAcademico"));
                    coordinador.setNombre(rs.getString("nombre") + " " + rs.getString("apellidos"));
                    personales.add(coordinador);
                }   
                cbCoordinador.setItems(personales);
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
        String academiaAux = tfAcademia.getText();
        String descripcionAux = tfDescripcionAcademia.getText();
        int posCoordinador = cbCoordinador.getSelectionModel().getSelectedIndex();
        
        if(academiaAux.isEmpty()){
            isValido = false;
        }
        if(descripcionAux.isEmpty()){
            isValido = false;
        }
        if(posCoordinador < 0){
            isValido = false;
        }
        if(isValido){
            guardarAcademia(descripcionAux, academiaAux,personales.get(posCoordinador).getIdAcademico());
        }else{
            alertConexion = Herramientas.builderAlert("Campos obligatorios", 
                "Por favor llena todos los campos para continuar...", Alert.AlertType.ERROR);
            alertConexion .showAndWait();
        }
    }

    private void guardarAcademia(String descripcionAux, String academiaAux, Integer idCoordinador) {
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String mensaje = "";
                int resultado;  
                if(isNuevo){ 
                    String consulta = "INSERT INTO academia(nombre, descripcion, idCoordinador) VALUES (?, ?, ?);";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, academiaAux);
                    ps.setString(2, descripcionAux);
                    ps.setInt(3, idCoordinador);
                    resultado = ps.executeUpdate();
                    mensaje = "Academia registrado con éxito...";
                }else{          
                    String consulta = "UPDATE academia SET nombre = ? , descripcion = ? , idCoordinador = ? WHERE idAcademia = ?;";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, academiaAux);
                    ps.setString(2, descripcionAux);
                    ps.setInt(3, idCoordinador);
                    ps.setInt(4, academiaEdicion.getIdAcademia());
                    resultado = ps.executeUpdate();
                    mensaje = "Academia editada con éxito...";
                }
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Operación exitosa", mensaje, Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();                 
                    Stage stage = (Stage) tfAcademia.getScene().getWindow();
                    stage.close(); 
                    notificacion.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error en el registro", "la Academia no pudo ser guardado en el sistema",
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
    
    private int getIndexListaCoordinador(Integer idCoordinador) {
        int value = 0;
        if(personales.size() > 0){
            for(int i = 0; i < personales.size(); i++){
                Personal get = personales.get(i);
                if(get.getIdAcademico()== idCoordinador){
                    return i;
                }
            }
        }
        return value;
    }
}