/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar un listado de EGEL-CENEVAL asi como registrar, eliminar, y editar un registro de una EGEL-CENEVAL.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * cargaCenevalBD: Permite la carga de datos de SQL a la vista implementada.
 * clicMenuCatalogo:  Permite la carga de la vista principal mediante el método irMenuCatalogo.
 * clicAgregar: Método para la inyeccion de datos a traves de la vista a SQL.
 * clicEditar: Método para la actualización de datos a traves de la vista a SQL .
 * irMenuCatalogo: Rediccionamiento hacia la vista principal.
 * irPantallaFormulario: Método que permite el rediccionamiento hacia la vista agregar un EGEL-CENEVAL.
 * clicEliminar: Permite la eliminacion de algun EGEL-CENEVAL mediante el método clicEliminarCenevalBD.
 * clicEliminarCenevalBD: Permite la eliminación de algun EGEL-CENEVAL por medio de la vista hacia SQL.
 * buscarCeneval: Método que permite el rastreo de algun EGEL-CENEVAL por medio de la vista.
 * refrescaTabla: Permite la recarga de la vista por medio de la invocacion del metodo cargaCenevalBD.
 * clicVisualizar: Permite visualizar los elementos de EGEL-CENEVAL de la fila seleccionada en el tableview.
 * irPantallaVisualizar: Redirige hacia la vista visualizar EGEL-CENEVAL.
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
import pojos.Ceneval;
import util.Herramientas;


public class FXMLEgelCenevalConsultaController implements Initializable, NotificaCambios {

    @FXML
    private Label tfTitulo;
    @FXML
    private TableView<Ceneval> tbCeneval;
    @FXML
    private TableColumn colCeneval;
    @FXML
    private TableColumn colMatriculaCeneval;
    @FXML
    private TableColumn colPeriodoCeneval;
    @FXML
    private TableColumn colPuntaje;
    @FXML
    private TextField tfBuscar;
    private ObservableList<Ceneval> cenevales;
    Alert alertConexion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cenevales = FXCollections.observableArrayList();
        this.colCeneval.setCellValueFactory(new PropertyValueFactory("estudiante"));
        this.colMatriculaCeneval.setCellValueFactory(new PropertyValueFactory("matricula"));
        this.colPeriodoCeneval.setCellValueFactory(new PropertyValueFactory("periodo"));
        this.colPuntaje.setCellValueFactory(new PropertyValueFactory("puntaje"));
        cargaCenevalBD();
    }

    private void clicVisualizar(){
        tbCeneval.setRowFactory( tv -> {
            TableRow<Ceneval> row = new TableRow<>();
            row.setOnMouseClicked( evento -> {
                if(evento.getClickCount() == 2 && (! row.isEmpty()) ){
                    int seleccion = tbCeneval.getSelectionModel().getSelectedIndex();
                    Ceneval cenevalVisualizar = cenevales.get(seleccion);
                    irPantallaVisualizar(cenevalVisualizar);
                }
            });
            return row ;
        });
    }    
    
    private void cargaCenevalBD(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        Alert alertConexion;
        if(conn != null){
            try{
                String consulta = "SELECT ceneval.idCeneval, fechaExamen, periodo, puntaje, estudiante.nombre, matricula, carrera.nombre FROM ceneval inner join estudiante ON ceneval.idEstudiante = estudiante.idEstudiante INNER JOIN carrera ON estudiante.idCarrera = carrera.idCarrera;";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Ceneval ceneval = new Ceneval();
                    ceneval.setIdCeneval(rs.getInt("idCeneval"));
                    ceneval.setFechaExamen(rs.getDate("fechaExamen"));
                    ceneval.setPeriodo(rs.getString("periodo"));
                    ceneval.setPuntaje(rs.getFloat("puntaje"));
                    ceneval.setEstudiante(rs.getString("estudiante.nombre"));
                    ceneval.setMatricula(rs.getString("matricula"));
                    ceneval.setCarrera(rs.getString("carrera.nombre"));
                    cenevales.add(ceneval);
                }
                clicVisualizar();
                buscarCeneval(); 
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
        irPantallaFormulario(true , null);
    }
    
    private void irMenuPrincipal(){
        try{
            Stage stage = (Stage) tfTitulo.getScene().getWindow();
            Scene scenePrincipal= new Scene(FXMLLoader.load(getClass().getResource("FXMLMenuPrincipal.fxml")));
            stage.setScene(scenePrincipal);
            stage.show();
        } catch (IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irPantallaFormulario(Boolean isNuevo, Ceneval ceneval){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEgelCenevalFormulario.fxml"));
            Parent root = loader.load();
            FXMLEgelCenevalFormularioController controlador = loader.getController();
            controlador.inicializaCampos(this, isNuevo, ceneval);
            Scene sceneFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneFormulario);
            stage.showAndWait();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irPantallaVisualizar(Ceneval cenevalVisualizar){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEgelCenevalVisualizar.fxml"));
            Parent root = loader.load();
            FXMLEgelCenevalVisualizarController controlador = loader.getController();
            controlador.inicializaCampos(cenevalVisualizar);
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
        int seleccion = tbCeneval.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            Ceneval cenevalEliminar = cenevales.get(seleccion);
            alertConexion = Herramientas.builderAlert("Eliminar", "¿Estas seguro de eliminar el CENEVAL de "
                +cenevalEliminar.getEstudiante()+" "+"?", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> resultadoDialogo = alertConexion.showAndWait();
            if(resultadoDialogo.get() == ButtonType.OK){
                eliminarCenevalBD(cenevalEliminar.getIdCeneval());  
            }
        }else{
            alertConexion = Herramientas.builderAlert("Sin seleccion", "Primero selecciona un registro...", Alert.AlertType.WARNING);
            alertConexion.showAndWait();
        }
    }
    
    private void eliminarCenevalBD(int idCeneval){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consultas = "DELETE FROM ceneval WHERE idCeneval = ?";
                PreparedStatement ps = conn.prepareStatement(consultas);
                ps.setInt(1, idCeneval);
                int resultado = ps.executeUpdate();
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Examen CENEVAL eliminado", "Examen CENEVAL eliminado con éxito.", Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();
                    this.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error al eliminar el examen CENEVAL", "Lo sentimos no se pudo eliminar la Academia.", Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
                }
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error de consulta", "No se puede modificar tu registro, CENEVAL en uso.", Alert.AlertType.ERROR);
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
    
    private void buscarCeneval(){
        if(cenevales.size() > 0){
            FilteredList<Ceneval> filtroDato = new FilteredList<>(cenevales, p -> true);
            tfBuscar.textProperty().addListener(new ChangeListener<String>(){  
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtroDato.setPredicate(busqueda -> {
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        if(busqueda.getEstudiante().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getMatricula().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<Ceneval> sortedData = new SortedList<>(filtroDato);
            sortedData.comparatorProperty().bind(tbCeneval.comparatorProperty());
            tbCeneval.setItems(sortedData);
        }
    }

    @Override
    public void refrescaTabla(boolean carga) {
        try{
           cenevales.clear();
           cargaCenevalBD(); 
        }catch (Exception e){
           irMenuPrincipal();
        }
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        int seleccion = tbCeneval.getSelectionModel().getSelectedIndex();
        if (seleccion >= 0) {
            Ceneval cenevalEdicion = cenevales.get(seleccion);
            irPantallaFormulario(false, cenevalEdicion);
        } else {
            Alert alertaVacio = Herramientas.builderAlert("Sin selección", "Para editar un registro, debe seleccionarlo de la tabla", Alert.AlertType.WARNING);
            alertaVacio.showAndWait();
        }
    }
}
