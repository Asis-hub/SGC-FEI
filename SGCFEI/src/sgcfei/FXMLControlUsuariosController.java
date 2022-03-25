/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar un listado de usuarios asi como registrar, eliminar, y editar un registro de un usuario
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * cargaUsuarioBD: Permite la carga de datos de SQL a la vista implementada.
 * clicMenuPrincipal:  Permite la carga de la vista principal mediante el método irMenuCatalogo.
 * clicAgregar: Método para la inyeccion de datos a traves de la vista a SQL.
 * clicEditar: Método para la actualización de datos a traves de la vista a SQL.
 * irMenuPrincipal: Rediccionamiento hacia la vista menu principal.
 * irPantallaFormulario: Método que permite el rediccionamiento hacia la vista agregar usuario.
 * clicEliminar: Permite la eliminacion de algunusuario mediante el método eliminarUsuarioBD.
 * eliminarUsuarioBD: Permite la eliminación de algun usuario por medio de la vista hacia SQL.
 * buscarUsuario: Método que permite el rastreo de algun usuario por medio de la vista.
 * refrescaTabla: Permite la recarga de la vista por medio de la invocacion del metodo cargaUsuarioBD.
 * clicVisualizar: Permite visualizar los elementos de usuario de la fila seleccionada en el tableview.
 * irPantallaVisualizar: Redirige hacia la vista visualizar usuario.
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
import pojos.Usuario;
import util.Herramientas;



public class FXMLControlUsuariosController implements Initializable, NotificaCambios  {
    
    Alert alertConexion;
    
    @FXML
    private Label tfTitulo;
    @FXML
    private TableView<Usuario> tbUsers;
    @FXML
    private TableColumn colUsername;
    @FXML
    private TableColumn colNombreUsuario;
    
    private ObservableList<Usuario> usuarios;
    @FXML
    private TextField tfBuscar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarios = FXCollections.observableArrayList();
        this.colUsername.setCellValueFactory(new PropertyValueFactory("username"));
        this.colNombreUsuario.setCellValueFactory(new PropertyValueFactory("academico"));
        colUsername.setResizable(false);
        
        cargaUsuarioBD();
    }
    
    private void cargaUsuarioBD(){
        Connection conn = ConexionDB.abrirConexionMySQL();
        Alert alertConexion;
        if(conn != null){
            try{
                //String consulta = "SELECT idUsuario, username, password, a.idRol, a.idAcademico, b.nombre, b.apellidos, correo, c.nombre FROM usuario a, academico b, rol c WHERE a.idAcademico = b.idAcademico AND a.idRol = c.idRol;";
                
                while(rs.next()){
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("idUsuario"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setIdRol(rs.getInt("a.idRol"));
                    usuario.setIdAcademico(rs.getInt("idAcademico"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setRol(rs.getString("c.nombre"));
                    usuario.setAcademico(rs.getString("b.nombre") + " " + rs.getString("b.apellidos"));
                    
                    usuarios.add(usuario);
                }
                clicVisualizar();
                buscarUsuario();
                
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
    private void clicAgregar(ActionEvent event) {
        irPantallaFormulario(true, null);  
    }
    
    @FXML
    private void clicEditar(ActionEvent event) {
        int seleccion = tbUsers.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            Usuario usuarioEdicion = usuarios.get(seleccion);
            irPantallaFormulario(false, usuarioEdicion);
        }else{
            Alert alertaVacio = Herramientas.builderAlert("Sin selección", "Para editar registro debes seleccionarlo de la tabla"
                    , Alert.AlertType.WARNING);
            alertaVacio.showAndWait();
        }
    }
    
    @FXML
    private void clicMenuPrincipal(ActionEvent event) {
        irMenuPrincipal();
    }
    
    private void clicVisualizar(){
        tbUsers.setRowFactory( tv -> {
            TableRow<Usuario> row = new TableRow<>();
            row.setOnMouseClicked( evento -> {
                if(evento.getClickCount() == 2 && (! row.isEmpty()) ){
                    int seleccion = tbUsers.getSelectionModel().getSelectedIndex();
                    Usuario usuarioVisualizar = usuarios.get(seleccion);
                    irPantallaVisualizar(usuarioVisualizar);
                }
            });
            return row ;
        });
    }
    
    private void irMenuPrincipal(){
        try{
            Stage stage = (Stage) tfTitulo.getScene().getWindow();
            Scene scenePrincipal= new Scene(FXMLLoader.load(getClass().getResource("FXMLMenuPrincipal.fxml")));
            stage.setScene(scenePrincipal);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irPantallaVisualizar(Usuario usuario){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLControlUsuariosVisualizar.fxml"));
            Parent root = loader.load();
            FXMLControlUsuariosVisualizarController controlador = loader.getController();
            controlador.inicializaCampos(usuario);
            Scene sceneVisualizar = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneVisualizar);
            stage.showAndWait();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irPantallaFormulario(boolean isNuevo, Usuario usuario){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLControlUsuariosFormulario.fxml"));
            Parent root = loader.load();
            FXMLControlUsuariosFormularioController controlador = loader.getController();
            controlador.inicializaCampos(this, isNuevo, usuario);
            
            Scene sceneFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneFormulario);
            stage.showAndWait();
            
        } catch (IOException ex) {
            System.out.println("Error al cargar scene: "+ex.getMessage());
        }
    }
    
    @FXML
    private void clicEliminar(ActionEvent event) {
        int seleccion = tbUsers.getSelectionModel().getSelectedIndex();
        if(seleccion >= 0){
            Usuario usuarioEliminar = usuarios.get(seleccion);
            alertConexion = Herramientas.builderAlert("Eliminar", "¿Estas seguro de eliminar el usuario "
                +usuarioEliminar.getUsername()+" "+"perteneciente a "+ usuarioEliminar.getAcademico()+" ?", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> resultadoDialogo = alertConexion.showAndWait();
            if(resultadoDialogo.get() == ButtonType.OK){
                eliminarUsuarioBD(usuarioEliminar.getIdUsuario());  
            }
        }else{
            alertConexion = Herramientas.builderAlert("Sin seleccion", "Primero selecciona un registro...", Alert.AlertType.WARNING);
            alertConexion.showAndWait();
        }
    }
   
    private void eliminarUsuarioBD(int idUsuario){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consultas = "DELETE FROM usuario WHERE idUsuario = ?";
                PreparedStatement ps = conn.prepareStatement(consultas);
                ps.setInt(1, idUsuario);
                int resultado = ps.executeUpdate();
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Usuario eliminado", "Usuario eliminado con éxito.", Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();
                    this.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error al eliminar el Usuario", "Lo sentimos no se pudo eliminar el Usuario.", Alert.AlertType.ERROR);
                    alertConexion.showAndWait();
                }
            }catch(SQLException ex){
                alertConexion = Herramientas.builderAlert("Error de consulta", "No se puede modificar tu registro, Usuario en uso.", Alert.AlertType.ERROR);
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
    
    private void buscarUsuario(){
        if(usuarios.size() > 0){
            FilteredList<Usuario> filtroDato = new FilteredList<>(usuarios, p -> true);
            tfBuscar.textProperty().addListener(new ChangeListener<String>(){
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtroDato.setPredicate(busqueda -> {
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        if(busqueda.getUsername().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getAcademico().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<Usuario> sortedData = new SortedList<>(filtroDato);
            sortedData.comparatorProperty().bind(tbUsers.comparatorProperty());
            tbUsers.setItems(sortedData);
        }
    }
    
    @Override
    public void refrescaTabla(boolean carga) {
        try {
            usuarios.clear();
            cargaUsuarioBD(); 
        } catch (Exception e) {
            irMenuPrincipal();
        }
    }
}
