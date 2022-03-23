/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar un listado de estudiantes asi como registrar, eliminar, y editar un registro de un estudiante;
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * cargaEstudianteBD: Permite la carga de datos de SQL a la vista implementada.
 * clicMenuCatalogo:  Permite la carga de la vista principal mediante el método irMenuCatalogo.
 * clicAgregar: Método para la inyeccion de datos a traves de la vista a SQL.
 * clicEditar: Método para la actualización de datos a traves de la vista a SQL .
 * irMenuCatalogo: Rediccionamiento hacia la vista principal.
 * irPantallaFormulario: Método que permite el rediccionamiento hacia la vista agregar una estudiantes.
 * clicEliminar: Permite la eliminacion de alguna academia mediante el método clicEliminarEstudianteBD.
 * eliminarEstudianteBD: Permite la eliminación de algun estudiante por medio de la vista hacia SQL.
 * buscarEstudiante: Método que permite el rastreo de algun estudiante por medio de la vista.
 * refrescaTabla: Permite la recarga de la vista por medio de la invocacion del metodo cargaEstudianteBD.
 * clicVisualizar: Permite visualizar los elementos de estudiante de la fila seleccionada en el tableview.
 * irPantallaVisualizar: Redirige hacia la vista visualizar estudiante.
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
import pojos.Estudiante;
import util.Herramientas;


public class FXMLEstudianteConsultaController implements Initializable, NotificaCambios {
    
    @FXML
    private Label lbTitulo;
    @FXML
    private TableView<Estudiante> tbEstudiante;
    @FXML
    private TableColumn colEstudiante;
    @FXML
    private TableColumn colMatriculaEstudiante;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colCarreraEstudiante;
    @FXML
    private TextField tfBuscar;
    private ObservableList<Estudiante> estudiantes;

    Alert alertConexion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        estudiantes = FXCollections.observableArrayList();
        this.colEstudiante.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colMatriculaEstudiante.setCellValueFactory(new PropertyValueFactory("matricula"));
        this.colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        this.colCarreraEstudiante.setCellValueFactory(new PropertyValueFactory("carrera"));
        cargaEstudianteBD();
    } 
    
    private void cargaEstudianteBD(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        Alert alertConexion;
        if(conn != null){
            try{
                String consulta = "SELECT idEstudiante, a.nombre, matricula, correo, a.idCarrera, b.nombre FROM estudiante a, carrera b WHERE a.idCarrera = b.idCarrera;";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Estudiante estudiante = new Estudiante();
                    estudiante.setIdEstudiante(rs.getInt("idEstudiante"));
                    estudiante.setNombre(rs.getString("a.nombre"));
                    estudiante.setMatricula(rs.getString("matricula"));
                    estudiante.setCorreo(rs.getString("correo"));
                    estudiante.setIdCarrera(rs.getInt("a.idCarrera"));
                    estudiante.setCarrera(rs.getString("b.nombre"));
                    estudiantes.add(estudiante);
                }
                clicVisualizar();
                buscarEstudiante(); 
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
    private void clicMenuCatalogo(ActionEvent event) {
        irMenuCatalogo();
    }
    
    @FXML
    private void clicAgregar(ActionEvent event) {
        irPantallaFormulario(true, null);
    }
    
    @FXML
    private void clicEditar(ActionEvent event) {
        int seleccion = tbEstudiante.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            Estudiante estudianteEdicion = estudiantes.get(seleccion);
            irPantallaFormulario(false, estudianteEdicion);
        }else{
            Alert alertaVacio = Herramientas.builderAlert("Sin selección", "Para editar registro debes seleccionarlo de la tabla"
                ,Alert.AlertType.WARNING);
            alertaVacio.showAndWait();
        }
    }
    
    private void irMenuCatalogo(){
        try{
            Stage stage = (Stage) lbTitulo.getScene().getWindow();
            Scene sceneCatalogo= new Scene(FXMLLoader.load(getClass().getResource("FXMLMenuCatalogo.fxml")));
            stage.setScene(sceneCatalogo);
            stage.show();
        }catch(IOException ex){
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irPantallaFormulario(boolean isNuevo, Estudiante estudiante){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEstudianteFormulario.fxml"));
            Parent root = loader.load();
            FXMLEstudianteFormularioController controlador = loader.getController();
            controlador.inicializaCampos(this, isNuevo, estudiante);
            Scene sceneFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneFormulario);
            stage.showAndWait();  
        } catch (IOException ex) {
            System.out.println("Error al cargar scene: "+ex.getMessage());
        }
    }
    
    private void irPantallaVisualizar(Estudiante estudianteVisualizar){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEstudianteVisualizar.fxml"));
            Parent root = loader.load();
            FXMLEstudianteVisualizarController controlador = loader.getController();
            controlador.inicializaCampos(estudianteVisualizar);
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
        int seleccion = tbEstudiante.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            Estudiante estudianteEliminar = estudiantes.get(seleccion);
            alertConexion = Herramientas.builderAlert("Eliminar", "¿Estas seguro de eliminar la/el estudiante "
                +estudianteEliminar.getNombre()+" "+"?", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> resultadoDialogo = alertConexion.showAndWait();
            if(resultadoDialogo.get() == ButtonType.OK){
                eliminarEstudianteBD(estudianteEliminar.getIdEstudiante());  
            }
        }else{
            alertConexion = Herramientas.builderAlert("Sin seleccion", "Primero selecciona un registro...", Alert.AlertType.WARNING);
            alertConexion.showAndWait();
        }
    }
   
    private void eliminarEstudianteBD(int idEstudiante){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consultas = "DELETE FROM estudiante WHERE idEstudiante = ?";
                PreparedStatement ps = conn.prepareStatement(consultas);
                ps.setInt(1, idEstudiante);
                int resultado = ps.executeUpdate();
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Estudiante eliminada/o", "Estudiante eliminada/o con éxito.", Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();
                    this.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error al eliminar el Estudiante", "Lo sentimos no se pudo eliminar la/el Estudiante.", Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
                }
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error de consulta", "No se puede modificar tu registro, Estudiante en uso.", Alert.AlertType.ERROR);
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
    
    private void buscarEstudiante(){
        if(estudiantes.size() > 0){
            FilteredList<Estudiante> filtroDato = new FilteredList<>(estudiantes, p -> true);
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
                        }else if(busqueda.getMatricula().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<Estudiante> sortedData = new SortedList<>(filtroDato);
            sortedData.comparatorProperty().bind(tbEstudiante.comparatorProperty());
            tbEstudiante.setItems(sortedData);
        }
    }
    
    @Override
    public void refrescaTabla(boolean carga) {
        try {
            estudiantes.clear();
            cargaEstudianteBD();
        } catch (Exception e) {
            irMenuCatalogo();
        } 
    }
    
    private void clicVisualizar(){
        tbEstudiante.setRowFactory( tv -> {
            TableRow<Estudiante> row = new TableRow<>();
            row.setOnMouseClicked( evento -> {
                if(evento.getClickCount() == 2 && (! row.isEmpty()) ){
                    int seleccion = tbEstudiante.getSelectionModel().getSelectedIndex();
                    Estudiante estudianteVisualizar = estudiantes.get(seleccion);
                    irPantallaVisualizar(estudianteVisualizar);
                }
            });
            return row ;
        });
    } 
}
