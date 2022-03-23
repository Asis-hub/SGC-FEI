/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar un listado de LGAC asi como registrar, eliminar, y editar un registro de una LGAC.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * cargaLGACBD: Permite la carga de datos de SQL a la vista implementada.
 * clicMenuCatalogo:  Permite la carga de la vista principal mediante el método irMenuCatalogo.
 * clicAgregar: Método para la inyeccion de datos a traves de la vista a SQL.
 * clicEditar: Método para la actualización de datos a traves de la vista a SQL .
 * irMenuCatalogo: Rediccionamiento hacia la vista principal.
 * irPantallaFormulario: Método que permite el rediccionamiento hacia la vista agregar un LGAC.
 * clicEliminar: Permite la eliminacion de algun LGAC mediante el método eliminarLGACBD.
 * eliminarLGACBD: Permite la eliminación de algun LGAC por medio de la vista hacia SQL.
 * buscarLGAC: Método que permite el rastreo de algun LGAC por medio de la vista.
 * refrescaTabla: Permite la recarga de la vista por medio de la invocacion del metodo cargaLGACBD.
 * clicVisualizar: Permite visualizar los elementos de LGAC de la fila seleccionada en el tableview.
 * irPantallaVisualizar: Redirige hacia la vista visualizar LGAC.
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
import pojos.LGCA;
import util.Herramientas;

public class FXMLLGCAConsultaController implements Initializable, NotificaCambios {
    
    @FXML
    private Label lbTitulo;
    @FXML
    private TableView<LGCA> tbLGCA;
    @FXML
    private TableColumn colGrado;
    @FXML
    private TableColumn colClave;
    @FXML
    private TableColumn colResponsableLGCA;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TextField tfBuscar;
    private ObservableList<LGCA> LGCAs;
    
    Alert alertConexion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LGCAs = FXCollections.observableArrayList();
        this.colGrado.setCellValueFactory(new PropertyValueFactory("grado"));
        this.colClave.setCellValueFactory(new PropertyValueFactory("clave"));
        this.colResponsableLGCA.setCellValueFactory(new PropertyValueFactory("responsable"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory("LGCA"));
        cargaLGCABD();
    }
    
    private void cargaLGCABD(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT idLGCA, clave, grado, desAdscripcion, idResponsable, a.nombre, b.nombre, b.apellidos, correo FROM lgca a, academico b WHERE idResponsable = idAcademico;";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    LGCA lgca = new LGCA();
                    lgca.setIdLGCA(rs.getInt("idLGCA"));
                    lgca.setClave(rs.getString("clave"));
                    lgca.setGrado(rs.getString("grado"));
                    lgca.setLGCA(rs.getString("nombre"));
                    lgca.setCorreo(rs.getString("correo"));
                    lgca.setDesAdscripcion(rs.getString("desAdscripcion"));
                    lgca.setIdResponsable(rs.getInt("idResponsable"));
                    lgca.setResponsable(rs.getString("b.nombre") + " " +rs.getString("b.apellidos"));
                    LGCAs.add(lgca);
                }
                buscarLGCA();
                clicVisualizar();
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
        int seleccion = tbLGCA.getSelectionModel().getSelectedIndex();
        if (seleccion >= 0) {
            LGCA lgcaEdicion = LGCAs.get(seleccion);
            irPantallaFormulario(false, lgcaEdicion);
        } else {
            Alert alertaVacio = Herramientas.builderAlert("Sin selección", "Para editar un registro, debe seleccionarlo de la tabla", Alert.AlertType.WARNING);
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
    
    private void irPantallaFormulario(boolean isNuevo, LGCA lgca){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLGCAFormulario.fxml"));
            Parent root = loader.load();
            FXMLLGCAFormularioController controlador = loader.getController();
            controlador.inicializaCampos(this, isNuevo, lgca);
            Scene sceneFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneFormulario);
            stage.showAndWait(); 
        } catch (IOException ex) {
            System.out.println("Error al cargar scene: "+ex.getMessage());
        }
    }

    private void irPantallaVisualizar(LGCA lgca){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLGCAVisualizar.fxml"));
            Parent root = loader.load();
            FXMLLGCAVisualizarController controlador = loader.getController();   
            controlador.inicializaCampos(lgca);   
            Scene sceneVisulizar = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneVisulizar);
            stage.showAndWait();   
        } catch (IOException ex) {
            System.out.println("Error al cargar scene: "+ex.getMessage());
        }
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
        int seleccion = tbLGCA.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            LGCA lgcaEliminar = LGCAs.get(seleccion);
            alertConexion = Herramientas.builderAlert("Eliminar", "¿Estas seguro de eliminar la LGAC"
                +lgcaEliminar.getLGCA()+" "+"?", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> resultadoDialogo = alertConexion.showAndWait();
            if(resultadoDialogo.get() == ButtonType.OK){
                eliminarLGCABD(lgcaEliminar.getIdLGCA()); 
            }
        }else{
            alertConexion = Herramientas.builderAlert("Sin seleccion", "Primero selecciona un registro...", Alert.AlertType.WARNING);
            alertConexion.showAndWait();
        }
    }
   
    private void eliminarLGCABD(int idLGCA){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consultas = "DELETE FROM lgca WHERE idLGCA = ?";
                PreparedStatement ps = conn.prepareStatement(consultas);
                ps.setInt(1, idLGCA);
                int resultado = ps.executeUpdate();
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("LGAC eliminada", "LGAC eliminada con éxito.", Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();
                    this.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error al eliminar la LGAC", "Lo sentimos no se pudo eliminar la LGAC.", Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
                }
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error de consulta", "No se puede modificar tu registro, LGAC en uso.", Alert.AlertType.ERROR);
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
    
    private void buscarLGCA(){
        if(LGCAs.size() > 0){
            FilteredList<LGCA> filtroDato = new FilteredList<>(LGCAs, p -> true);
            tfBuscar.textProperty().addListener(new ChangeListener<String>(){
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtroDato.setPredicate(busqueda -> {
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        if(busqueda.getResponsable().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getClave().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<LGCA> sortedData = new SortedList<>(filtroDato);
            sortedData.comparatorProperty().bind(tbLGCA.comparatorProperty());
            tbLGCA.setItems(sortedData);
        }
    }
    
    @Override
    public void refrescaTabla(boolean carga) {
        try {
            LGCAs.clear();
            cargaLGCABD();           
        } catch (Exception e) {
            irMenuCatalogo();
        }  
    }
    
    private void clicVisualizar(){
        tbLGCA.setRowFactory( tv -> {
            TableRow<LGCA> row = new TableRow<>();
            row.setOnMouseClicked( evento -> {
                if(evento.getClickCount() == 2 && (! row.isEmpty()) ){
                    int seleccion = tbLGCA.getSelectionModel().getSelectedIndex();
                    LGCA lgcaEdicion = LGCAs.get(seleccion);
                    irPantallaVisualizar(lgcaEdicion);
                }
            });
            return row ;
        });
    }  
}
