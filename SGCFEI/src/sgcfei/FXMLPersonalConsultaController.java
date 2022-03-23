/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar un listado de personal asi como registrar, eliminar, y editar un registro de un personal.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * cargaPersonalBD: Permite la carga de datos de SQL a la vista implementada.
 * clicMenuCatalogo: Permite la carga de la vista principal mediante el método irMenuCatalogo.
 * clicAgregar: Método para la inyeccion de datos a traves de la vista a SQL.
 * clicEditar: Método para la actualización de datos a traves de la vista a SQL.
 * irMenuCatalogo: Rediccionamiento hacia la vista principal.
 * irPantallaFormulario: Método que permite el rediccionamiento hacia la vista agregar una personal.
 * clicEliminar: Permite la eliminacion de algun personal mediante el método clicEliminarPersonalBD.
 * clicEliminarPersonalBD: Permite la eliminación de algun personal por medio de la vista hacia SQL.
 * buscarPersonal: Método que permite el rastreo de algun personal por medio de la vista.
 * refrescaTabla: Permite la recarga de la vista por medio de la invocacion del metodo cargaPersonalBD.
 * clicVisualizar: Permite visualizar los elementos de personal de la fila seleccionada en el tableview.
 * irPantallaVisualizar: Redirige hacia la vista visualizar personal.
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

import pojos.Personal;
import util.Herramientas;

public class FXMLPersonalConsultaController implements Initializable, NotificaCambios {
    
    @FXML
    private Label lbTitulo;
    @FXML
    private TableView<Personal> tbPersonal;
    @FXML
    private TableColumn colNombrePersonal;
    @FXML
    private TableColumn colNoPersonal;
    @FXML
    private TableColumn colCorreoPersonal;
    @FXML
    private TableColumn colApellidos;
    private ObservableList<Personal> personales;
    @FXML
    private TextField tfBuscar;
    
    String nombreCompleto; 
    Alert alertConexion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        personales = FXCollections.observableArrayList();
        this.colNombrePersonal.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colNoPersonal.setCellValueFactory(new PropertyValueFactory("numeroPersonal"));
        this.colCorreoPersonal.setCellValueFactory(new PropertyValueFactory("correo"));
        this.colApellidos.setCellValueFactory(new PropertyValueFactory("apellidos"));
        cargaPersonalBD();
    }    
    
    private void cargaPersonalBD(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        Alert alertConexion;
        if(conn != null){
            try{
                String consulta = "SELECT idAcademico, numeroPersonal, nombre, apellidos, correo FROM academico";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Personal personal = new Personal();
                    personal.setIdAcademico(rs.getInt("idAcademico"));
                    personal.setNumeroPersonal(rs.getString("numeroPersonal"));
                    personal.setNombre(rs.getString("nombre"));
                    personal.setApellidos(rs.getString("apellidos"));
                    personal.setCorreo(rs.getString("correo"));
                    personales.add(personal);
                }
                clicVisualizar();
                buscarPersonal();  
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
        int seleccion = tbPersonal.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            Personal personalEdicion = personales.get(seleccion);
            irPantallaFormulario(false, personalEdicion);
        }else{
            Alert alertaVacio = Herramientas.builderAlert("Sin selección", "Para editar registro debes seleccionarlo de la tabla"
                , Alert.AlertType.WARNING);
            alertaVacio.showAndWait();
        }
    }
    
    private void irMenuCatalogo(){
        try{
            Stage stage = (Stage) lbTitulo.getScene().getWindow();
            Scene sceneCatalogo= new Scene(FXMLLoader.load(getClass().getResource("FXMLMenuCatalogo.fxml")));
            stage.setScene(sceneCatalogo);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irPantallaFormulario(boolean isNuevo, Personal personal){
        try {  
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPersonalFormulario.fxml"));
            Parent root = loader.load();
            FXMLPersonalFormularioController controlador = loader.getController();
            controlador.inicializaCampos(this, isNuevo, personal); 
            Scene sceneFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneFormulario);
            stage.showAndWait();   
        } catch (IOException ex) {
            System.out.println("Error al cargar scene: "+ex.getMessage());
        }
    }
    
    private void irPantallaVisualizar(Personal personalVisualizar){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPersonalVisualizar.fxml"));
            Parent root = loader.load();
            FXMLPersonalVisualizarController controlador = loader.getController();
            controlador.inicializaCampos(personalVisualizar);
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
        int seleccion = tbPersonal.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            Personal personalEliminar = personales.get(seleccion);
            alertConexion = Herramientas.builderAlert("Eliminar", "¿Estas seguro de eliminar el académico "
                + personalEliminar.getNombre() + " " + personalEliminar.getApellidos() + " ?", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> resultadoDialogo = alertConexion.showAndWait();
            if(resultadoDialogo.get() == ButtonType.OK){
                eliminarPersonalBD(personalEliminar.getIdAcademico()); 
            }
        }else{
            alertConexion = Herramientas.builderAlert("Sin seleccion", "Primero selecciona un registro...", Alert.AlertType.WARNING);
            alertConexion.showAndWait();
        }
    }
   
    private void eliminarPersonalBD(int idAcademico){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consultas = "DELETE FROM academico WHERE idAcademico = ?";
                PreparedStatement ps = conn.prepareStatement(consultas);
                ps.setInt(1, idAcademico);
                int resultado = ps.executeUpdate();
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Académico eliminada", "Académico eliminada con éxito.", Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();
                    this.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error al eliminar la Academico", "Lo sentimos no se pudo eliminar el académico.", Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
                }
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error de consulta", "No se puede modificar tu registro, Académico en uso.", Alert.AlertType.ERROR);
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
    
    private void buscarPersonal(){
        if(personales.size() > 0){
            FilteredList<Personal> filtroDato = new FilteredList<>(personales, p -> true);
            tfBuscar.textProperty().addListener(new ChangeListener<String>(){  
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                    filtroDato.setPredicate(busqueda -> {
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        if(busqueda.getNombre().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getNumeroPersonal().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getApellidos().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<Personal> sortedData = new SortedList<>(filtroDato);
            sortedData.comparatorProperty().bind(tbPersonal.comparatorProperty());
            tbPersonal.setItems(sortedData);
        }
    }
    
    @Override
    public void refrescaTabla(boolean carga) {
        try {
            personales.clear();
            cargaPersonalBD();  
        } catch (Exception e) {
            irMenuCatalogo();
        }   
    }
    
    private void clicVisualizar(){
        tbPersonal.setRowFactory( tv -> {
            TableRow<Personal> row = new TableRow<>();
            row.setOnMouseClicked( evento -> {
                if(evento.getClickCount() == 2 && (! row.isEmpty()) ){
                    int seleccion = tbPersonal.getSelectionModel().getSelectedIndex();
                    Personal personalVisualizar = personales.get(seleccion);
                    irPantallaVisualizar(personalVisualizar);
                }
            });
            return row ;
        });
    }
}
