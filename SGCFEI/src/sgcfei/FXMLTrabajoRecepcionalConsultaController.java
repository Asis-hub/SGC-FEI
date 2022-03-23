/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar un listado de trabajos recepcionales asi como registrar, eliminar, y editar un registro de trabajo recepcional.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * cargaTrabajoRecepcionalBD: Permite la carga de datos de SQL a la vista implementada.
 * clicMenuPrincipal: Permite la carga de la vista menu principal mediante el método irMenuPrincipal.
 * clicAgregar: Método para la inyeccion de datos a traves de la vista a SQL.
 * clicEditar: Método para la actualización de datos a traves de la vista a SQL .
 * irMenuPrincipal: Rediccionamiento hacia la vista menu principal.
 * irPantallaFormulario: Método que permite el rediccionamiento hacia la vista agregar trabajo recepcional.
 * clicEliminar: Permite la eliminacion de algun trabajo recepcional mediante el método eliminarTrabajorecepcionalBD.
 * eliminarTrabajoRecepcionalBD: Permite la eliminación de alguna academia por medio de la vista hacia SQL.
 * buscarTrabajoRecepcional: Método que permite el rastreo de algun trabajo recepcional por medio de la vista.
 * refrescaTabla: Permite la recarga de la vista por medio de la invocacion del metodo cargaAcademiaBD.
 * clicVisualizar: Permite visualizar los elementos de trabajo recepcional de la fila seleccionada en el tableview.
 * irPantallaVisualizar: Redirige hacia la vista visualizar trabajo recepcional.
 */
package sgcfei;

import db.ConexionDB;
import interfaz.NotificaCambios;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.TrabajoRecepcional;
import util.Herramientas;


public class FXMLTrabajoRecepcionalConsultaController implements Initializable, NotificaCambios {
    
    Alert alertConexion;
    
    @FXML
    private Label lbTitulo;
    @FXML
    private TableView<TrabajoRecepcional> tbTrabajoRecepcional;
    @FXML
    private TableColumn colTrabajoRecepcional;
    @FXML
    private TableColumn colFechaRegistro;
    @FXML
    private TableColumn colEstudiante;
    
    private ObservableList<TrabajoRecepcional> trabajosrecepcionales;
    @FXML
    private TextField tfBuscar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        trabajosrecepcionales = FXCollections.observableArrayList();
        this.colTrabajoRecepcional.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colFechaRegistro.setCellValueFactory(new PropertyValueFactory("fechaRegistro"));
        this.colEstudiante.setCellValueFactory(new PropertyValueFactory("estudiante"));
        
        cargaTrabajoRecepcionalBD();
        //cargaTrabajoRecepcionalCoDirectorBD();
    }    
    
    private void cargaTrabajoRecepcionalBD(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        Alert alertConexion;
        if(conn != null){
            try{
                String consulta = "SELECT idTrabajoRecepcional, a.nombre, fechaRegistro, descripcion, a.idEstudiante, idDirector, idCoDirector, idSinodal, b.nombre, c.nombre, c.apellidos, a2.nombre, a2.apellidos, b2.nombre, b2.apellidos FROM trabajorecepcional a, estudiante b, academico c JOIN academico a2 JOIN academico b2  WHERE a.idEstudiante = b.idEstudiante AND a.idDirector = c.idAcademico AND a.idCoDirector = a2.idAcademico AND a.idSinodal = b2.idAcademico;";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    TrabajoRecepcional trabajorecepcional = new TrabajoRecepcional();      
                    trabajorecepcional.setIdTrabajoRecepcional(rs.getInt("idTrabajoRecepcional"));
                    trabajorecepcional.setNombre(rs.getString("a.nombre"));
                    trabajorecepcional.setFechaRegistro(rs.getDate("fechaRegistro"));
                    trabajorecepcional.setDescripcion(rs.getString("descripcion"));
                    trabajorecepcional.setEstudiante(rs.getString("b.nombre"));
                    trabajorecepcional.setIdDirector(rs.getInt("idDirector"));
                    trabajorecepcional.setIdCoDirector(rs.getInt("idCoDirector"));
                    trabajorecepcional.setIdSinodal(rs.getInt("idSinodal"));
                    trabajorecepcional.setEstudiante(rs.getString("b.nombre"));
                    trabajorecepcional.setDirector(rs.getString("c.nombre") + " " + rs.getString("c.apellidos"));
                    trabajorecepcional.setCoDirector(rs.getString("a2.nombre") + " " + rs.getString("a2.apellidos"));
                    trabajorecepcional.setSinodal(rs.getString("b2.nombre") + " " + rs.getString("b2.apellidos"));
                    trabajosrecepcionales.add(trabajorecepcional);
                }
                clicVisualizar();
                buscarTrabajoRecepcional();
                
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
    private void clicMenuPrincipal(ActionEvent event) {
        irMenuPrincipal();
    }
    
    @FXML
    private void clicAgregar(ActionEvent event) {
        irPantallaFormulario(true, null);
    }
    
    @FXML
    private void clicEditar(ActionEvent event) {
        int seleccion = tbTrabajoRecepcional.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            TrabajoRecepcional recepcionalEdicion = trabajosrecepcionales.get(seleccion);
            irPantallaFormulario(false, recepcionalEdicion);
        }else{
            Alert alertaVacio = Herramientas.builderAlert("Sin selección", "Para editar registro debes seleccionarlo de la tabla"
                    , Alert.AlertType.WARNING);
            alertaVacio.showAndWait();
        }
    }
    
    private void irMenuPrincipal(){
    try {
                Stage stage = (Stage) lbTitulo.getScene().getWindow();
                Scene scenePrincipal= new Scene(FXMLLoader.load(getClass().getResource("FXMLMenuPrincipal.fxml")));
                stage.setScene(scenePrincipal);
                stage.show();
            } catch (IOException ex) {
                System.out.println("Error al cargar FXML: "+ex.getMessage());
            }
    }
    
    private void irPantallaFormulario(boolean isNuevo, TrabajoRecepcional recepcional){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLTrabajoRecepcionalFormulario.fxml"));
            Parent root = loader.load();
            FXMLTrabajoRecepcionalFormularioController controlador = loader.getController();
            controlador.inicializaCampos(this, isNuevo, recepcional);
            
            Scene sceneFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneFormulario);
            stage.showAndWait();
            
        } catch (IOException ex) {
            System.out.println("Error al cargar scene: "+ex.getMessage());
        }
    }
    
    private void irPantallaVisualizar(TrabajoRecepcional trabajoRecepcionalVisualizar){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLTrabajoRecepcionalVisualizar.fxml"));
            Parent root = loader.load();
            FXMLTrabajoRecepcionalVisualizarController controlador = loader.getController();
            controlador.inicializaCampos(trabajoRecepcionalVisualizar);
            Scene sceneFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneFormulario);
            stage.showAndWait();
        }catch (IOException ex) {
               ex.printStackTrace();
        }
    }
    
    @FXML
    private void clicEliminar(ActionEvent event) {
        int seleccion = tbTrabajoRecepcional.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            TrabajoRecepcional trabajoRecepcionalEliminar = trabajosrecepcionales.get(seleccion);
            alertConexion = Herramientas.builderAlert("Eliminar", "¿Estas seguro de eliminar el Trabajo Recepcional de "
                    +trabajoRecepcionalEliminar.getEstudiante()+" "+"?", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> resultadoDialogo = alertConexion.showAndWait();
            if(resultadoDialogo.get() == ButtonType.OK){
                eliminarTrabajoRecepcionalBD(trabajoRecepcionalEliminar.getIdTrabajoRecepcional());
                
            }
        }else{
            alertConexion = Herramientas.builderAlert("Sin seleccion", "Primero selecciona un registro...", Alert.AlertType.WARNING);
            alertConexion.showAndWait();
        }
    }
   
    private void eliminarTrabajoRecepcionalBD(int idTrabajoRecepcional){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consultas = "DELETE FROM trabajorecepcional WHERE idTrabajoRecepcional = ?";
                PreparedStatement ps = conn.prepareStatement(consultas);
                ps.setInt(1, idTrabajoRecepcional);
                int resultado = ps.executeUpdate();
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Trabajo Recepcional eliminado", "Trabajo Recepcional eliminado con éxito.", Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();
                    this.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error al eliminar el Trabajo Recepcional", "Lo sentimos no se pudo eliminar el Trabajo Recepcional.", Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
                }
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error de consulta", "No se puede modificar tu registro, Trabajo Recepcional en uso.", Alert.AlertType.ERROR);
                alertConexion.showAndWait();
            }finally{
                try{
                    conn.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }else{
            alertConexion = Herramientas.builderAlert("Error de conexion", "No se puede conectar con la base de datos en este momento.", Alert.AlertType.ERROR);
            alertConexion.showAndWait();
        }
        
    }
    
    private void buscarTrabajoRecepcional(){
        if(trabajosrecepcionales.size() > 0){
            FilteredList<TrabajoRecepcional> filtroDato = new FilteredList<>(trabajosrecepcionales, p -> true);
            
            tfBuscar.textProperty().addListener(new ChangeListener<String>(){
                
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtroDato.setPredicate(busqueda -> {
                    
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        
                        String lowerCaseFilter = newValue.toLowerCase();
                        if(busqueda.getNombre().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getEstudiante().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                }
             
            });
        
        
        SortedList<TrabajoRecepcional> sortedData = new SortedList<>(filtroDato);
        sortedData.comparatorProperty().bind(tbTrabajoRecepcional.comparatorProperty());
        tbTrabajoRecepcional.setItems(sortedData);
    }
    }
    
    @Override
    public void refrescaTabla(boolean carga) {
        try {
            trabajosrecepcionales.clear();
            cargaTrabajoRecepcionalBD();
            
        } catch (Exception e) {
            irMenuPrincipal();
        }
            
        
    }
    
    private void clicVisualizar(){
        tbTrabajoRecepcional.setRowFactory( tv -> {
                    TableRow<TrabajoRecepcional> row = new TableRow<>();
                    row.setOnMouseClicked( evento -> {
                        if(evento.getClickCount() == 2 && (! row.isEmpty()) ){
                            int seleccion = tbTrabajoRecepcional.getSelectionModel().getSelectedIndex();
                            TrabajoRecepcional trabajoRecepcionalVisualizar = trabajosrecepcionales.get(seleccion);
                            irPantallaVisualizar(trabajoRecepcionalVisualizar);
                        }
                    });
                    return row ;
                });
    }
    
}
