/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar campos que pueden ser ingresados y aquellos que se pueden ser modificado de estudiante.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.

 * inicializaCampos: Crea el objeto de estudiante ademas de validar si este es un registro nuevo o es un registro a modificar.
 
 * cargaEstudianteEdicion: Hace la cargar de los datos de estudiante para que sean modificados.
 * cargarCarreraDB: Método que permite rescatar la informacion de carrera para ser cargada en un combobox para su posterior selección.
 * clicGuardar: Obtiene datos que han sido introducidos y los valida.
 * guardarEstudiante: Método que realiza la inserción o actualización de la informacion que se ha proporcionado. 
 * getIndexListaCarrera: Metodo que realiza la busqueda de la carrera perteneciente a la estudiante a modificar.
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
import pojos.Carrera;
import pojos.Estudiante;
import util.Herramientas;

public class FXMLEstudianteFormularioController implements Initializable{

    
    
    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField tfEstudiante;
    @FXML
    private TextField tfCorreo;
    @FXML
    private ComboBox<Carrera> cbCarrera;
    private ObservableList<Carrera> carreras;
    private boolean isNuevo;
    private Estudiante estudianteEdicion;
    private NotificaCambios notificacion;
    Alert alertConexion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carreras = FXCollections.observableArrayList();
        cargaCarreraDB();
    }    
    
    public void inicializaCampos(NotificaCambios notificacion, boolean isNuevo, Estudiante estudianteEdicion){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.estudianteEdicion = estudianteEdicion;
        if(!isNuevo){
           cargaEstudianteEdicion();
        }
    }
    
    private void cargaEstudianteEdicion(){
        tfEstudiante.setText(estudianteEdicion.getNombre());
        tfMatricula.setText(estudianteEdicion.getMatricula());
        tfCorreo.setText(estudianteEdicion.getCorreo());
        lbTitulo.setText("Editar Estudiante " + estudianteEdicion.getNombre());
        int posCarrera = getIndexListaCarrera(estudianteEdicion.getIdCarrera());
        cbCarrera.getSelectionModel().select(posCarrera);
    }
    
    private void cargaCarreraDB(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idCarrera, nombre, idFacultad FROM carrera";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Carrera car = new Carrera();
                    car.setIdCarrera(rs.getInt("idCarrera"));
                    car.setNombre(rs.getString("nombre"));
                    car.setIdFacultad(rs.getInt("idFacultad"));
                    carreras.add(car);
                }
                cbCarrera.setItems(carreras);
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
        String estudianteAux = tfEstudiante.getText();
        String matriculaAux = tfMatricula.getText();
        String correoAux = tfCorreo.getText();
        int posCarrera = cbCarrera.getSelectionModel().getSelectedIndex();
        
        if(estudianteAux.isEmpty()){
            isValido = false;
        }
        if(matriculaAux.isEmpty() || matriculaAux.length() > 9 || matriculaAux.length() < 9){
           isValido = false;
        }
        if(correoAux.isEmpty() || !correoAux.contains("@")){
           isValido = false;
        }
        if(posCarrera < 0){
            isValido = false;
        }
        if(isValido){
            guardarEstudiante(estudianteAux, matriculaAux, correoAux, carreras.get(posCarrera).getIdCarrera());
        }else{
            if(!correoAux.contains("@")){
                alertConexion = Herramientas.builderAlert("Correo inválido", 
                    "Por favor ingresa un correo electrónico válido...", Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }else{
                if(matriculaAux.length() > 9 || matriculaAux.length() < 9){
                alertConexion = Herramientas.builderAlert("Matricula inválida", 
                    "Por favor ingresa matricula válido con 9 carácteres...", Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }else{
                if(!isValido){
                alertConexion = Herramientas.builderAlert("Campos obligatorios", 
                    "Por favor llena todos los campos para continuar...", Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }}}}
    }
    
    private void guardarEstudiante(String estudianteAux, String matriculaAux, String correoAux, int idCarrera){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String mensaje = "";
                int resultado;
                if(isNuevo){ 
                    String consulta = "INSERT INTO estudiante(nombre, matricula, correo, idCarrera) VALUES (?, ?, ?, ?);";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, estudianteAux);
                    ps.setString(2, matriculaAux);
                    ps.setString(3, correoAux);
                    ps.setInt(4, idCarrera);
                    resultado = ps.executeUpdate();
                    mensaje = "Estudiante registrado con éxito...";
                }else{          
                    String consulta = "UPDATE estudiante SET nombre = ? , matricula = ? , correo = ? , idCarrera = ? WHERE idEstudiante = ?;";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, estudianteAux);
                    ps.setString(2, matriculaAux);
                    ps.setString(3, correoAux);
                    ps.setInt(4, idCarrera);
                    ps.setInt(5, estudianteEdicion.getIdEstudiante());
                    resultado = ps.executeUpdate();
                    mensaje = "Estudiante editado con éxito...";
                }
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Operación exitosa", mensaje, Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();                 
                    Stage stage = (Stage) tfEstudiante.getScene().getWindow();
                    stage.close();
                    notificacion.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error en el registro", "el estudiante no pudo ser guardado en el sistema",
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
    
    private int getIndexListaCarrera(Integer idCarrera) {
        int value = 0;
        if(carreras.size() > 0){
            for(int i = 0; i < carreras.size(); i++){
                Carrera get = carreras.get(i);
                if(get.getIdCarrera() == idCarrera){
                    return i;
                }
            }
        }
        return value;
    } 
}
