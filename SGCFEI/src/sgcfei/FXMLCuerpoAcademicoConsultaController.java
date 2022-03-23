/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar un listado de cuerpos academico asi como registrar, eliminar, y editar un registro de cuerpo academico
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * cargaCuerpoAcademicoBD: Permite la carga de datos de SQL a la vista implementada.
 * clicMenuCatalogo:  Permite la carga de la vista principal mediante el método irMenuCatalogo.
 * clicAgregar: Método para la inyeccion de datos a traves de la vista a SQL.
 * clicEditar: Método para la actualización de datos a traves de la vista a SQL .
 * irMenuCatalogo: Rediccionamiento hacia la vista principal.
 * irPantallaFormulario: Método que permite el rediccionamiento hacia la vista agregar un cuerpo academico.
 * clicEliminar: Permite la eliminacion de algun cuerpo academico mediante el método clicEliminarCuerpoAcademicoBD.
 * clicEliminarCuerpoAcademicoBD: Permite la eliminación de algun Cuerpo Academico por medio de la vista hacia SQL.
 * buscarCuerpoAcademico: Método que permite el rastreo de algun Cuerpo Academico por medio de la vista.
 * refrescaTabla: Permite la recarga de la vista por medio de la invocacion del metodo cargaCuerpoAcademicoBD.
 * clicVisualizar: Permite visualizar los elementos de cuerpo academico de la fila seleccionada en el tableview.
 * irPantallaVisualizar: Redirige hacia la vista visualizar cuerpo academico.
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
import pojos.CuerpoAcademico;
import util.Herramientas;


public class FXMLCuerpoAcademicoConsultaController implements Initializable, NotificaCambios  {
    
    @FXML
    private Label lbTitulo;
    @FXML
    private TableColumn colCuerpoAcademico;
    @FXML
    private TableColumn colLGCA;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableColumn colResponsable;
    @FXML
    private TableColumn colConsolidacion;
    @FXML  
    private TableView<CuerpoAcademico> tbCuerpoAcademico;
    
    private ObservableList<CuerpoAcademico> cuerposacademicos;
    
    Alert alertConexion;

/*
*
*Esta seccion abarca el cargado de los datos en las tablas
*
*/   
    //Metodo que carga los datos en la tablas.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cuerposacademicos = FXCollections.observableArrayList();
        this.colCuerpoAcademico.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colLGCA.setCellValueFactory(new PropertyValueFactory("nombreLGCA"));
        this.colResponsable.setCellValueFactory(new PropertyValueFactory("responsable"));
        this.colConsolidacion.setCellValueFactory(new PropertyValueFactory("grado"));
        
        cargaCuerpoAcademicoBD();
    }
    //Metodo intermediario entre la BD y la vista, con el se obtienen y se mandan los datos para cargarlos.
    private void cargaCuerpoAcademicoBD(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT b.idCuerpoAcademico, b.nombre, b.descripcion, b.idLGCA, a.idLGCA, a.clave, a.grado, a.nombre,a.clave, a.desAdscripcion, c.nombre, c.apellidos FROM cuerpoacademico b inner join lgca a on b.idLGCA = a.idLGCA inner join academico c on a.idResponsable = c.idAcademico";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                
                while(rs.next()){
                    CuerpoAcademico cuerpoacademico = new CuerpoAcademico();
                    cuerpoacademico.setIdCuerpoAcademico(rs.getInt("b.idCuerpoAcademico"));
                    cuerpoacademico.setNombre(rs.getString("b.nombre"));
                    cuerpoacademico.setDescripcion(rs.getString("b.descripcion"));
                    cuerpoacademico.setNombreLGCA(rs.getString("a.nombre"));
                    cuerpoacademico.setGrado(rs.getString("a.grado"));
                    cuerpoacademico.setResponsable(rs.getString("c.nombre") + " " + rs.getString("c.apellidos"));
                    cuerpoacademico.setIdLGCA(rs.getInt("b.idLGCA"));
                    cuerpoacademico.setClave(rs.getString("a.clave"));
                    cuerpoacademico.setLgcaDescripcion(rs.getString("a.desAdscripcion"));

                    cuerposacademicos.add(cuerpoacademico);  
                }
                buscarCuerpoAcademico();
                clicVisualizar();
                
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
    
/*
*
*Esta seccion maneja los eventodes de los  botones
*
*/
    @FXML
    private void clicMenuCatalogo(ActionEvent event){
        irMenuCatalogo();
    }
    @FXML
    private void clicAgregar(ActionEvent event){
        irPantallaFormulario(true, null);
    }
      
    @FXML
    private void clicEliminar(ActionEvent event){
        int seleccion = tbCuerpoAcademico.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            CuerpoAcademico cuerpoAcademicoEliminar = cuerposacademicos.get(seleccion);
            alertConexion = Herramientas.builderAlert("Eliminar", "¿Estas seguro de eliminar el cuerpo académico?"
                +cuerpoAcademicoEliminar.getNombre()+" "+"?", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> resultadoDialogo = alertConexion.showAndWait();
            if(resultadoDialogo.get() == ButtonType.OK){
                eliminarCuerpoAcademiaBD(cuerpoAcademicoEliminar.getIdCuerpoAcademico());  
            }
        }else{
            alertConexion = Herramientas.builderAlert("Sin seleccion", "Primero selecciona un registro...", Alert.AlertType.WARNING);
            alertConexion.showAndWait();
        }
    }

    private void irMenuCatalogo(){
        try {
            Stage stage = (Stage) lbTitulo.getScene().getWindow();
            Scene sceneCatalogo= new Scene(FXMLLoader.load(getClass().getResource("FXMLMenuCatalogo.fxml")));
            stage.setScene(sceneCatalogo);
            stage.show();
        }catch (IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irPantallaFormulario(boolean isNuevo, CuerpoAcademico cuerpoAcademico){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLCuerpoAcademicoFormulario.fxml"));
            Parent root = loader.load();
            FXMLCuerpoAcademicoFormularioController controlador = loader.getController();
            controlador.inicializaCampos(this, isNuevo, cuerpoAcademico);
            
            Scene sceneFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneFormulario);
            stage.showAndWait();
        } catch (IOException ex) {
            System.out.println("Error al cargar scene: "+ex.getMessage());
        }
    }
    
    private void irPantallaVisualizar(CuerpoAcademico cuerpoAcademicoVisualizar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLVisualizarCuerpoAcademico.fxml"));
            Parent root = loader.load();
            FXMLVisualizarCuerpoAcademicoController controlador = loader.getController();   
            controlador.inicializaCampos(cuerpoAcademicoVisualizar);   
            Scene sceneVisulizar = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneVisulizar);
            stage.showAndWait();   
        } catch (IOException ex) {
            System.out.println("Error al cargar scene: "+ex.getMessage());
        }
    }
   
    private void eliminarCuerpoAcademiaBD(int idCuerpoAcademico){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consultas = "DELETE FROM cuerpoacademico WHERE idCuerpoAcademico = ?";
                PreparedStatement ps = conn.prepareStatement(consultas);
                ps.setInt(1, idCuerpoAcademico);
                int resultado = ps.executeUpdate();
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Cuerpo académico eliminado", "Cuerpo académico eliminado con éxito.", 
                    Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();
                    this.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error al eliminar el Cuerpo Académico", "Lo sentimos no se pudo eliminar el cuerpo académico.", Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
                }
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error de consulta", "No se puede modificar tu registro, Cuerpo Académico en uso.", 
                    Alert.AlertType.ERROR);
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
   
    private void buscarCuerpoAcademico(){
        if(cuerposacademicos.size() > 0){
            FilteredList<CuerpoAcademico> filtroDato = new FilteredList<>(cuerposacademicos, p -> true);
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
                        }
                        return false;
                    });
                }
            });
            SortedList<CuerpoAcademico> sortedData = new SortedList<>(filtroDato);
            sortedData.comparatorProperty().bind(tbCuerpoAcademico.comparatorProperty());
            tbCuerpoAcademico.setItems(sortedData);
        }
    }
    
    @Override
    public void refrescaTabla(boolean carga) {
        try {
            cuerposacademicos.clear();
            cargaCuerpoAcademicoBD();
        } catch (Exception e) {
            irMenuCatalogo();
        }   
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        int seleccion = tbCuerpoAcademico.getSelectionModel().getSelectedIndex();
        if (seleccion >= 0) {
            CuerpoAcademico cuerpoAcademicoEdicion = cuerposacademicos.get(seleccion);
            irPantallaFormulario(false, cuerpoAcademicoEdicion);
        }else{
            Alert alertaVacio = Herramientas.builderAlert("Sin selección", "Para editar un registro, debe seleccionarlo de la tabla", Alert.AlertType.WARNING);
            alertaVacio.showAndWait();
        }
    }

    private void clicVisualizar() {
        tbCuerpoAcademico.setRowFactory( tv -> {
            TableRow<CuerpoAcademico> row = new TableRow<>();
            row.setOnMouseClicked( evento -> {
                if(evento.getClickCount() == 2 && (! row.isEmpty()) ){
                    int seleccion = tbCuerpoAcademico.getSelectionModel().getSelectedIndex();
                    CuerpoAcademico cuerpoAcademicoVisualizar = cuerposacademicos.get(seleccion);
                    irPantallaVisualizar(cuerpoAcademicoVisualizar);
                }
            });
            return row ;
        });
    } 
}
