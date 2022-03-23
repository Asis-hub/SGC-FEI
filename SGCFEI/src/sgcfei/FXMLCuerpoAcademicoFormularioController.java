/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar campos que pueden ser ingresados y aquellos que se pueden ser modificado de un cuerpo academico
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de cuerpo academico ademas de validar si este es un registro nuevo o es un registro a modificar.
 * cargaCuerpoAcademicoEdicion: Hace la cargar de los datos de cuerpo academico para que sean modificados.
 * cargarlgcasDB: Método que permite rescatar la informacion de LGCA para ser cargada en un combobox para su posterior selección.
 * clicGuardar: Obtiene datos que han sido introducidos y los valida.
 * guardarCuerpoAcademico: Método que realiza la inserción o actualización de la informacion que se ha proporcionado. 
 * getIndexListaLGCA: Metodo que realiza la busqueda del LGCA perteneciente al cuerpo academico a modificar.
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
import pojos.CuerpoAcademico;
import pojos.LGCA;
import util.Herramientas;

public class FXMLCuerpoAcademicoFormularioController implements Initializable{
    @FXML
    private ComboBox<LGCA> cbLGCA;
    @FXML
    private TextField textNombreCuerpoAcademico;
    @FXML
    private TextField textDescripcion;
    @FXML
    private Label lbTitulo;
    private ObservableList<LGCA> lgcas; 
    private NotificaCambios notificacion;
    private boolean isNuevo;
    private CuerpoAcademico cuerpoAcademicoEdicion;
    Alert alertConexion;
    
    @Override
    public void initialize(URL url, ResourceBundle resources){
        lgcas = FXCollections.observableArrayList();
        cargarlgcasDB();      
    }
    
    public void inicializaCampos(NotificaCambios notificacion, boolean isNuevo, CuerpoAcademico cuerpoAcademicoEdicion){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.cuerpoAcademicoEdicion = cuerpoAcademicoEdicion;
        if(!isNuevo){
            cargaCuerpoAcademicoEdicion();
        }
    }
    
    private void cargaCuerpoAcademicoEdicion(){     
        textNombreCuerpoAcademico.setText(cuerpoAcademicoEdicion.getNombre());
        textDescripcion.setText(cuerpoAcademicoEdicion.getDescripcion());
        lbTitulo.setText("Editar Cuerpo Academico " + cuerpoAcademicoEdicion.getNombre());
        int posLGCA = getIndexListaLGCA(cuerpoAcademicoEdicion.getIdLGCA());
        cbLGCA.getSelectionModel().select(posLGCA);
    }


    private void cargarlgcasDB(){    
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String consulta = "SELECT * FROM lgca";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    LGCA lgca = new LGCA();
                    lgca.setIdLGCA(rs.getInt("idLGCA"));
                    lgca.setLGCA(rs.getString("nombre"));
                    lgcas.add(lgca);  
                }
                cbLGCA.setItems(lgcas);
                conn.close(); 
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
    private void clicGuardar(ActionEvent event) {
        boolean isValido = true;
        String textNombreCuerpoAcademicoAux = textNombreCuerpoAcademico.getText();
        String textDescripcionAux = textDescripcion.getText();
        int posLGCA = cbLGCA.getSelectionModel().getSelectedIndex();
        if(textNombreCuerpoAcademicoAux.isEmpty()){
            isValido = false;
        }
        if(textDescripcionAux.isEmpty()){
            isValido = false;
        }
        if(posLGCA < 0){
            isValido = false;
        }
        if(isValido){
            guardarCuerpoAcademico(textNombreCuerpoAcademicoAux, textDescripcionAux, lgcas.get(posLGCA).getIdLGCA());
        }else{
            alertConexion = Herramientas.builderAlert("Campos obligatorios", 
                "Por favor llena todos los campos para continuar...", Alert.AlertType.ERROR);
            alertConexion .showAndWait();
        } 
    }

    private void guardarCuerpoAcademico(String textNombreCuerpoAcademicoAux, String textDescripcionAux, Integer idLGCA){
        Connection conn = ConexionDB.abrirConexionMySQL();
        if(conn != null){
            try{
                String mensaje = "";
                int resultado;
                if(isNuevo){
                    String consulta = "INSERT INTO cuerpoacademico(nombre, descripcion, idLGCA) VALUES (?, ?, ?);";
                    PreparedStatement ps = conn.prepareStatement(consulta);
                    ps.setString(1, textNombreCuerpoAcademicoAux);
                    ps.setString(2, textDescripcionAux);
                    ps.setInt(3, idLGCA);
                    resultado = ps.executeUpdate();
                    mensaje = "Cuerpo Academico registrado con éxito...";
                }else{   
                    String consulta = "UPDATE cuerpoacademico SET nombre = ? , descripcion = ? , idLGCA = ? WHERE idCuerpoAcademico = ?;";
                    PreparedStatement ps = conn.prepareStatement(consulta);      
                    ps.setString(1, textNombreCuerpoAcademicoAux);
                    ps.setString(2, textDescripcionAux);
                    ps.setInt(3, idLGCA);
                    ps.setInt(4, cuerpoAcademicoEdicion.getIdCuerpoAcademico());
                    resultado = ps.executeUpdate();
                    mensaje = "Cuerpo Academia editada con éxito...";
                } 
                if(resultado > 0){
                    alertConexion = Herramientas.builderAlert("Operación exitosa", mensaje, Alert.AlertType.INFORMATION);
                    alertConexion.showAndWait();                 
                    Stage stage = (Stage) textNombreCuerpoAcademico.getScene().getWindow();
                    stage.close();
                    notificacion.refrescaTabla(true);
                }else{
                    alertConexion = Herramientas.builderAlert("Error en el registro", "El cuerpo academico no pudo ser guardado en el sistema",
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
  
    private int getIndexListaLGCA(Integer idLGCA) {
        int value = 0;
        if(lgcas.size() > 0){
            for(int i = 0; i < lgcas.size(); i++){
                LGCA get = lgcas.get(i);
                if(get.getIdLGCA() == idLGCA){
                    return i;
                }
            }
        }
        return value;
    }
}
