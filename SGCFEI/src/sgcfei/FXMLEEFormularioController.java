/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar campos que pueden ser ingresados y aquellos que se pueden ser modificado de una experiencia educativa.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de experiencia educativa ademas de validar si este es un registro nuevo o es un registro a modificar.
 * cargaExperienciaEducativaEdicion: Hace la cargar de los datos de experiencia educativa para que sean modificados.
 * cargaMateriaDB: Método que permite rescatar la informacion de materia para ser cargada en un combobox para su posterior selección.
 * cargaPreRequisitoDB: Método que permite rescatar la informacion de prerequisito para ser cargada en un combobox para su posterior selección.
 * cargaCoRequisitoDB: Método que permite rescatar la informacion de corequisito para ser cargada en un combobox para su posterior selección.
 * cargaAcademiaDB: Método que permite rescatar la informacion de academia para ser cargada en un combobox para su posterior selección.
 * clicGuardar: Obtiene datos que han sido introducidos y los valida.
 * guardarExperienciaEducativa: Método que realiza la inserción o actualización de la informacion que se ha proporcionado. 
 * getIndexListaAcademia: Metodo que realiza la busqueda del academia perteneciente ala experiencia educativa a modificar.
 * getIndexListaCoRequisito:Metodo que realiza la busqueda del corequisito perteneciente ala experiencia educativa a modificar
 * getIndexListaPreRequisito: Metodo que realiza la busqueda del prerequisito perteneciente ala experiencia educativa a modificar
 * getIndexListaMateria: Metodo que realiza la busqueda del materia perteneciente ala experiencia educativa a modificar
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
import pojos.Academia;
import pojos.ExperienciaEducativa;
import pojos.Materia;
import util.Herramientas;

public class FXMLEEFormularioController implements Initializable{

    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfExperienciaEducativa;
    @FXML
    private TextField tfNRC;
    @FXML
    private ComboBox<Materia> cbMateria;
    @FXML
    private ComboBox<ExperienciaEducativa> cbCorequisito;
    @FXML
    private ComboBox<Academia> cbAcademia;
    @FXML
    private ComboBox<ExperienciaEducativa> cbPrerequisito;
    
    private ObservableList<Materia> materias;
    private ObservableList<ExperienciaEducativa> prerequisitos;
    private ObservableList<ExperienciaEducativa> corequisitos;
    private ObservableList<Academia> academias;
    
    private NotificaCambios notificacion;
    private boolean isNuevo;
    private ExperienciaEducativa experienciaEdicion;
    Alert alertConexion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        materias = FXCollections.observableArrayList();
        prerequisitos = FXCollections.observableArrayList();
        corequisitos = FXCollections.observableArrayList();
        academias = FXCollections.observableArrayList();
        cargaMateriaDB();
        cargaPreRequisitoDB();
        cargaCoRequisitoDB();
        cargaAcademiaDB();
    }
    public void inicializaCampos(NotificaCambios notificacion, boolean isNuevo, ExperienciaEducativa experienciaEdicion){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.experienciaEdicion = experienciaEdicion;
        if(!isNuevo){
           cargaExperienciaEdicion();
        }
    }
    
    private void cargaExperienciaEdicion(){
        lbTitulo.setText("Editar Experiencia Educativa " + experienciaEdicion.getNombre());
        tfExperienciaEducativa.setText(experienciaEdicion.getNombre());
        tfNRC.setText(experienciaEdicion.getNrc());
        int posMateria = getIndexListaMateria(experienciaEdicion.getIdMateria());
        cbMateria.getSelectionModel().select(posMateria);
        int posAcademia = getIndexListaAcademia(experienciaEdicion.getIdAcademia());
        cbAcademia.getSelectionModel().select(posAcademia);
        int posPreReq = getIndexListaPreRequisito(experienciaEdicion.getIdPrerequisito());
        cbPrerequisito.getSelectionModel().select(posPreReq);
        int posCoReq = getIndexListaCoRequisito(experienciaEdicion.getIdCorequisito());
        cbCorequisito.getSelectionModel().select(posCoReq);
    }
    
    private void cargaMateriaDB(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idMateria, nombre FROM materia";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Materia mat = new Materia();
                    mat.setIdMateria(rs.getInt("idMateria"));
                    mat.setNombre(rs.getString("nombre"));
                    materias.add(mat);
                }
                cbMateria.setItems(materias);
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
    
    private void cargaAcademiaDB(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idAcademia, nombre FROM academia";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Academia acad = new Academia();
                    acad.setIdAcademia(rs.getInt("idAcademia"));
                    acad.setNombre(rs.getString("nombre"));
                    academias.add(acad);
                }
                cbAcademia.setItems(academias);
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
    
    private void cargaPreRequisitoDB(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idExperienciaEducativa, nombre FROM experienciaeducativa";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    ExperienciaEducativa prereq = new ExperienciaEducativa();
                    prereq.setIdExperienciaEducativa(rs.getInt("idExperienciaEducativa"));
                    prereq.setNombre(rs.getString("nombre"));
                    prerequisitos.add(prereq);
                }
                cbPrerequisito.setItems(prerequisitos);
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
    
    private void cargaCoRequisitoDB(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idExperienciaEducativa, nombre FROM experienciaeducativa";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    ExperienciaEducativa coreq = new ExperienciaEducativa();
                    coreq.setIdExperienciaEducativa(rs.getInt("idExperienciaEducativa"));
                    coreq.setNombre(rs.getString("nombre"));
                    corequisitos.add(coreq);
                }
                cbCorequisito.setItems(corequisitos);
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
        String nombreAux = tfExperienciaEducativa.getText();
        String nrcAux = tfNRC.getText(); 
        int posMateria = cbMateria.getSelectionModel().getSelectedIndex();
        int posAcademia = cbAcademia.getSelectionModel().getSelectedIndex();
        int posPrereq = cbPrerequisito.getSelectionModel().getSelectedIndex();
        int posCoreq = cbCorequisito.getSelectionModel().getSelectedIndex();
        
        if(nombreAux.isEmpty()){
            isValido = false;
        }
        if(nrcAux.isEmpty()){
            isValido = false;
        }
        if(posMateria < 0){
            isValido = false;
        }
        if(posAcademia < 0){
            isValido = false;
        }
        if(isValido){
            guardarExperienciaEducativa(nombreAux, nrcAux, materias.get(posMateria).getIdMateria(), academias.get(posAcademia).getIdAcademia() , prerequisitos.get(posPrereq).getIdExperienciaEducativa(), corequisitos.get(posCoreq).getIdExperienciaEducativa());
        }else{
            alertConexion = Herramientas.builderAlert("Campos obligatorios", 
                "Por favor llena todos los campos para continuar...", Alert.AlertType.ERROR);
            alertConexion.showAndWait();
        }
    }
    
    private void guardarExperienciaEducativa(String nombreAux, String nrcAux, int idMateria, int idAcademia , int idPrerequisito, int idCorequisito) {
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String mensaje = "";
                int resultado;
                if(isNuevo){ 
                    String consulta = "INSERT INTO experienciaeducativa(nombre, nrc, idMateria, idAcademia, idPrerequisito, idCorequisito) VALUES (?, ?, ?, ?, ?, ?);";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, nombreAux);
                    ps.setString(2, nrcAux);
                    ps.setInt(3, idMateria);
                    ps.setInt(4, idAcademia);
                    ps.setInt(5, idPrerequisito);
                    ps.setInt(6, idCorequisito);
                    resultado = ps.executeUpdate();
                    mensaje = "Experiencia Educativa registrado con éxito...";
                }else{          
                    String consulta = "UPDATE experienciaeducativa SET nombre = ? , nrc= ? , idMateria = ? , idAcademia = ? , idPrerequisito = ? , idCorequisito = ? WHERE idExperienciaEducativa = ?;";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, nombreAux);
                    ps.setString(2, nrcAux);
                    ps.setInt(3, idMateria);
                    ps.setInt(4, idAcademia);
                    ps.setInt(5, idPrerequisito);
                    ps.setInt(6, idCorequisito);
                    ps.setInt(7, experienciaEdicion.getIdExperienciaEducativa());
                    resultado = ps.executeUpdate();
                    mensaje = "Experiencia Educativa editada con éxito...";
                }
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Operación exitosa", mensaje, Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();                 
                    Stage stage = (Stage) tfExperienciaEducativa.getScene().getWindow();
                    stage.close();
                    notificacion.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error en el registro", "la experiencia educativa no pudo ser guardado en el sistema",
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
    
    private int getIndexListaMateria(Integer idMateria) {
        int value = 0;
        if(materias.size() > 0){
            for(int i = 0; i < materias.size(); i++){
                Materia get = materias.get(i);
                if(get.getIdMateria() == idMateria){
                    return i;
                }
            }
        }
        return value;
    }
    
    private int getIndexListaAcademia(Integer idAcademia) {
        int value = 0;
        if(academias.size() > 0){
            for(int i = 0; i < academias.size(); i++){
                Academia get = academias.get(i);
                if(get.getIdAcademia()== idAcademia){
                    return i;
                }
            }
        }
        return value;
    }
    
    private int getIndexListaPreRequisito(Integer idPrerequisito) {
        int value = 0;
        if(prerequisitos.size() > 0){
            for(int i = 0; i < prerequisitos.size(); i++){
                ExperienciaEducativa get = prerequisitos.get(i);
                if(get.getIdPrerequisito()== idPrerequisito){
                    return i;
                }
            }
        }
        return value;
    }
    
    private int getIndexListaCoRequisito(Integer idCorequisito) {
        int value = 0;
        if(corequisitos.size() > 0){
            for(int i = 0; i < corequisitos.size(); i++){
                ExperienciaEducativa get = corequisitos.get(i);
                if(get.getIdCorequisito()== idCorequisito){
                    return i;
                }
            }
        }
        return value;
    }
}
