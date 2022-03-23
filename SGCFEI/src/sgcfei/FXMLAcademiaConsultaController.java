/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar un listado de academias asi como registrar, eliminar, y editar un registro de una academia
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * cargaAcademiaBD: Permite la carga de datos de SQL a la vista implementada.
 * clicMenuCatalogo:  Permite la carga de la vista principal mediante el método irMenuCatalogo.
 * clicAgregar: Método para la inyeccion de datos a traves de la vista a SQL.
 * clicEditar: Método para la actualización de datos a traves de la vista a SQL .
 * irMenuCatalogo: Rediccionamiento hacia la vista principal.
 * irPantallaFormulario: Método que permite el rediccionamiento hacia la vista agregar una academia.
 * clicEliminar: Permite la eliminacion de alguna academia mediante el método clicEliminarAcademiaBD.
 * clicEliminarAcademiaBD: Permite la eliminación de alguna academia por medio de la vista hacia SQL.
 * buscarAcademia: Método que permite el rastreo de alguna academia por medio de la vista.
 * refrescaTabla: Permite la recarga de la vista por medio de la invocacion del metodo cargaAcademiaBD.
 * clicVisualizar: Permite visualizar los elementos de academia de la fila seleccionada en el tableview.
 * irPantallaVisualizar: Redirige hacia la vista visualizar academia.
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
import pojos.Academia;
import util.Herramientas;


public class FXMLAcademiaConsultaController implements Initializable, NotificaCambios {
    @FXML
    private Label lbTitulo;
    @FXML
    private TableView<Academia> tbAcademias;
    @FXML
    private TableColumn colAcademia;
    @FXML
    private TableColumn colCoordinadorAcademia;
    @FXML
    private TextField tfBuscar;
    private ObservableList<Academia> academias;
    Alert alertConexion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        academias = FXCollections.observableArrayList();
        this.colAcademia.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colCoordinadorAcademia.setCellValueFactory(new PropertyValueFactory("coordinador"));
        
        cargaAcademiaBD();
    }

    private void cargaAcademiaBD(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT a.idAcademia, a.nombre, descripcion, idCoordinador, b.nombre, b.apellidos FROM academia a, academico b, usuario c WHERE a.idCoordinador = b.idAcademico AND b.idAcademico = c.idAcademico AND c.idRol = 2;";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery(); 
                while(rs.next()){
                    Academia academia = new Academia();
                    academia.setIdAcademia(rs.getInt("a.idAcademia"));
                    academia.setNombre(rs.getString("a.nombre"));
                    academia.setDescripcion(rs.getString("descripcion"));
                    academia.setIdCoordinador(rs.getInt("idCoordinador"));
                    academia.setCoordinador(rs.getString("b.nombre") + " " + rs.getString("b.apellidos"));                                        
                    academias.add(academia);       
                } 
                clicVisualizar();
                buscarAcademia(); 
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error en consulta", ex.getMessage(),Alert.AlertType.ERROR);
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
       irPantallaFormulario(true , null);
    }
    
    private void irMenuCatalogo(){
        try {
            Stage stage = (Stage) lbTitulo.getScene().getWindow();
            Scene sceneCatalogo= new Scene(FXMLLoader.load(getClass().getResource("FXMLMenuCatalogo.fxml")));
            stage.setScene(sceneCatalogo);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    @FXML
    private void clicEditar(ActionEvent event) {
        int seleccion = tbAcademias.getSelectionModel().getSelectedIndex();
        if (seleccion >= 0) {
            Academia academiaEdicion = academias.get(seleccion);
            irPantallaFormulario(false, academiaEdicion);
        } else {
            Alert alertaVacio = Herramientas.builderAlert("Sin selección", "Para editar un registro, debe seleccionarlo de la tabla", Alert.AlertType.WARNING);
            alertaVacio.showAndWait();
        }
    }
    
    @FXML
    private void clicEliminar(ActionEvent event){
        int seleccion = tbAcademias.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            Academia academiaEliminar = academias.get(seleccion);
            alertConexion = Herramientas.builderAlert("Eliminar", "¿Estas seguro de eliminar la academia?"
                +academiaEliminar.getNombre()+" "+"?", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> resultadoDialogo = alertConexion.showAndWait();
            if(resultadoDialogo.get() == ButtonType.OK){
                eliminarAcademiaBD(academiaEliminar.getIdAcademia());       
            }
        }else{
            alertConexion = Herramientas.builderAlert("Sin seleccion", "Primero selecciona un registro...",
               Alert.AlertType.WARNING);
            alertConexion.showAndWait();
        }
    }
    
    private void irPantallaFormulario(boolean isNuevo, Academia academia){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLAcademiaFormulario.fxml"));
            Parent root = loader.load();
            FXMLAcademiaFormularioController controlador = loader.getController();
            controlador.inicializaCampos(this, isNuevo, academia);
            Scene sceneFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneFormulario);
            stage.showAndWait();   
        }catch(IOException ex) {
            System.out.println("Error al cargar scene: "+ex.getMessage());
        }
    }
    
    private void irPantallaVisualizar(Academia academiaVisualizar){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLAcademiaVisualizar.fxml"));
            Parent root = loader.load();
            FXMLAcademiaVisualizarController controlador = loader.getController();
           controlador.inicializaCampos(academiaVisualizar);
            Scene sceneFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneFormulario);
            stage.showAndWait();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void eliminarAcademiaBD(int idAcademia){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consultas = "DELETE FROM academia WHERE idAcademia = ?";
                PreparedStatement ps = conn.prepareStatement(consultas);
                ps.setInt(1, idAcademia);
                int resultado = ps.executeUpdate();
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Academia eliminada", "Academia eliminada con éxito.", Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();
                    this.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error al eliminar la Academia", "Lo sentimos no se pudo eliminar la Academia.", Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
                }
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error de consulta", "No se puede modificar tu registro, Academia en uso.", Alert.AlertType.ERROR);
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
    
    private void buscarAcademia(){
        if(academias.size() > 0){
            FilteredList<Academia> filtroDato = new FilteredList<>(academias, p -> true);
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
                        }else if(busqueda.getCoordinador().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });    
                }
            }); 
            SortedList<Academia> sortedData = new SortedList<>(filtroDato);
            sortedData.comparatorProperty().bind(tbAcademias.comparatorProperty());
            tbAcademias.setItems(sortedData);       
        }
    }
    
    @Override
    public void refrescaTabla(boolean carga){
        try{
            academias.clear();
            cargaAcademiaBD();
        }catch(UnsupportedOperationException e) {
            System.out.println("Error" + e.getMessage());
        }
    } 

    private void clicVisualizar() {
        tbAcademias.setRowFactory( tv -> {
            TableRow<Academia> row = new TableRow<>();
            row.setOnMouseClicked( evento ->{
                if(evento.getClickCount() == 2 && (! row.isEmpty()) ){
                    int seleccion = tbAcademias.getSelectionModel().getSelectedIndex();
                    Academia academiaVisualizar = academias.get(seleccion);
                    irPantallaVisualizar(academiaVisualizar);
                }
            });
            return row ;
        });
    }  
}
