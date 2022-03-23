/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar la informacion de usuario.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de usuario para poder recuperar la informacion que se quiere visualizar de la academia.
 * cargaUsuario: Hace la cargar de los datos de usuario para que sean visualizados.
 * clicSalir: Sale de esta pantalla para regresar al consultar.
 */


package sgcfei;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Usuario;

public class FXMLControlUsuariosVisualizarController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfUsuario;
    @FXML
    private TextField tfAcademico;
    @FXML
    private TextField tfRol;
    @FXML
    private TextField tfCorreo;
    @FXML
    private Button btSalir;
    private Usuario usuarioVisualizar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    }    
    
    public void inicializaCampos(Usuario usuarioVisualizar){
        this.usuarioVisualizar = usuarioVisualizar;
        cargaUsuario();
    }
    
    private void cargaUsuario(){
        lbTitulo.setText("Visualizar Usuario: " + usuarioVisualizar.getUsername());
        tfUsuario.setText(usuarioVisualizar.getUsername());
        tfAcademico.setText(usuarioVisualizar.getAcademico());
        tfRol.setText(usuarioVisualizar.getRol());
        tfCorreo.setText(usuarioVisualizar.getCorreo());
    }

    @FXML
    private void clicSalir(ActionEvent event) {
        Stage stage = (Stage) btSalir.getScene().getWindow();
        stage.close();
    }
}
