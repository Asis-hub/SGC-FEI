/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar un listado de experiencia educativa asi como registrar, eliminar, y editar un registro de una experiencia educativa.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * cargaExperienciaEducativaBD: Permite la carga de datos de SQL a la vista implementada.
 * clicMenuCatalogo:  Permite la carga de la vista principal mediante el método irMenuCatalogo.
 * clicAgregar: Método para la inyeccion de datos a traves de la vista a SQL.
 * clicEditar: Método para la actualización de datos a traves de la vista a SQL .
 * irMenuCatalogo: Rediccionamiento hacia la vista principal.
 * irPantallaFormulario: Método que permite el rediccionamiento hacia la vista agregar una experiencia educativa.
 * clicEliminar: Permite la eliminacion de alguna experiencia educativa mediante el método clicEliminarExperienciaBD.
 * clicEliminarExperienciaBD: Permite la eliminación de alguna experiencia educativa por medio de la vista hacia SQL.
 * buscarEE: Método que permite el rastreo de alguna experiencia educativa por medio de la vista.
 * refrescaTabla: Permite la recarga de la vista por medio de la invocacion del metodo cargaexperienciaEducativaBD.
 * clicVisualizar: Permite visualizar los elementos de experiencia educativa de la fila seleccionada en el tableview.
 * irPantallaVisualizar: Redirige hacia la vista visualizar experiencia educativa.
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
import pojos.ExperienciaEducativa;
import util.Herramientas;

public class FXMLEEConsultaController implements Initializable , NotificaCambios {
    
    @FXML
    private Label lbTitulo;
    @FXML
    private TableView<ExperienciaEducativa> tbEE;
    @FXML
    private TableColumn colNRC;
    @FXML
    private TableColumn colNombreEE;
    @FXML
    private TableColumn colNombreAcademia;
    @FXML
    private TextField tfBuscar;
    private ObservableList<ExperienciaEducativa> experiencias;
    Alert alertConexion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        experiencias = FXCollections.observableArrayList();
        this.colNRC.setCellValueFactory(new PropertyValueFactory("nrc"));
        this.colNombreEE.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colNombreAcademia.setCellValueFactory(new PropertyValueFactory("academia"));
        cargaExperienciaEducativaBD();
    }

    private void cargaExperienciaEducativaBD(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        Alert alertConexion;
        if(conn != null){
            try{
                String consulta = "SELECT a.idExperienciaEducativa, a.idMateria, a.nrc, a.idAcademia, a.idPrerequisito, a.idCorequisito, a.nombre, b.nombre, c.nombre, a2.nombre, a3.nombre FROM experienciaeducativa a, academia b, materia c JOIN experienciaeducativa a2 JOIN experienciaeducativa a3 WHERE a.idAcademia = b.idAcademia AND a.idMateria = c.idMateria AND a.idPrerequisito = a2.idExperienciaEducativa AND a.idCorequisito = a3.idExperienciaEducativa;";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    ExperienciaEducativa experiencia = new ExperienciaEducativa();
                    experiencia.setIdExperienciaEducativa(rs.getInt("idExperienciaEducativa"));
                    experiencia.setIdMateria(rs.getInt("idMateria"));
                    experiencia.setNrc(rs.getString("nrc"));
                    experiencia.setIdAcademia(rs.getInt("idAcademia"));
                    experiencia.setIdPrerequisito(rs.getInt("idPrerequisito"));
                    experiencia.setIdCorequisito(rs.getInt("idCorequisito"));
                    experiencia.setNombre(rs.getString("a.nombre"));
                    experiencia.setAcademia(rs.getString("b.nombre"));
                    experiencia.setMateria(rs.getString("c.nombre"));
                    experiencia.setPrerequisito(rs.getString("a2.nombre"));
                    experiencia.setCorequisito(rs.getString("a3.nombre")); 
                    experiencias.add(experiencia);
                }
                clicVisualizar();
                buscarEE();
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
        int seleccion = tbEE.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            ExperienciaEducativa experienciaEdicion = experiencias.get(seleccion);
            irPantallaFormulario(false, experienciaEdicion);
        }else{
            Alert alertaVacio = Herramientas.builderAlert("Sin selección", "Para editar registro debes seleccionarlo de la tabla"
                ,Alert.AlertType.WARNING);
            alertaVacio.showAndWait();
        }
    }
    
    private void clicVisualizar(){
        tbEE.setRowFactory( tv -> {
            TableRow<ExperienciaEducativa> row = new TableRow<>();
            row.setOnMouseClicked( evento -> {
                if(evento.getClickCount() == 2 && (! row.isEmpty()) ){
                    int seleccion = tbEE.getSelectionModel().getSelectedIndex();
                    ExperienciaEducativa experienciaVisualizar = experiencias.get(seleccion);
                    irPantallaVisualizar(experienciaVisualizar);
                }
            });
        return row ;
        });
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
    
    private void irPantallaFormulario(boolean isNuevo, ExperienciaEducativa experiencia){
        try { 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEEFormulario.fxml"));
            Parent root = loader.load();
            FXMLEEFormularioController controlador = loader.getController();
            controlador.inicializaCampos(this, isNuevo, experiencia);
            Scene sceneFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneFormulario);
            stage.showAndWait(); 
        } catch (IOException ex) {
            System.out.println("Error al cargar scene: "+ex.getMessage());
        }
    }
    
    private void irPantallaVisualizar(ExperienciaEducativa experienciaVisualizar){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEEVisualizar.fxml"));
            Parent root = loader.load();
            FXMLEEVisualizarController controlador = loader.getController();
            controlador.inicializaCampos(experienciaVisualizar);
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
        int seleccion = tbEE.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            ExperienciaEducativa experienciaEliminar = experiencias.get(seleccion);
            alertConexion = Herramientas.builderAlert("Eliminar", "¿Estas seguro de eliminar la experiencia educativa?"
                +experienciaEliminar.getNombre()+" "+"?", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> resultadoDialogo = alertConexion.showAndWait();
            if(resultadoDialogo.get() == ButtonType.OK){
                eliminarExperienciaBD(experienciaEliminar.getIdExperienciaEducativa());  
            }
        }else{
            alertConexion = Herramientas.builderAlert("Sin seleccion", "Primero selecciona un registro...", Alert.AlertType.WARNING);
            alertConexion.showAndWait();
        }
    }
   
    private void eliminarExperienciaBD(int idExperienciaEducativa){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consultas = "DELETE FROM experieciaeducativa WHERE idExperienciaEducativa = ?";
                PreparedStatement ps = conn.prepareStatement(consultas);
                ps.setInt(1, idExperienciaEducativa);
                int resultado = ps.executeUpdate();
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Experiencia Educativa eliminada", "Experiencia Educativa eliminada con éxito.", Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();
                    this.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error al eliminar la Experiencia Educativa", "Lo sentimos no se pudo eliminar la Experiencia Educativa.", Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
                }
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error de consulta", "No se puede modificar tu registro, Experiencia Educativa en uso.", Alert.AlertType.ERROR);
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
    
    private void buscarEE(){
        if(experiencias.size() > 0){
            FilteredList<ExperienciaEducativa> filtroDato = new FilteredList<>(experiencias, p -> true);
            tfBuscar.textProperty().addListener(new ChangeListener<String>(){
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtroDato.setPredicate(busqueda -> {
                        if(newValue == null || newValue.isEmpty()){
                           return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        if(busqueda.getNrc().toString().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getNombre().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getAcademia().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<ExperienciaEducativa> sortedData = new SortedList<>(filtroDato);
            sortedData.comparatorProperty().bind(tbEE.comparatorProperty());
            tbEE.setItems(sortedData);
        }
    }
    
    @Override
    public void refrescaTabla(boolean carga) {
        try {
            experiencias.clear();
            cargaExperienciaEducativaBD();
        } catch (Exception e) {
            irMenuCatalogo();
        }
    }
}
