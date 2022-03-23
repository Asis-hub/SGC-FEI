/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar la informacion de Personal.
 * 
*/

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de personal para poder recuperar la informacion que se quiere visualizar del personal.
 * cargaVisualizarPersonal: Hace la cargar de los datos de personal para que sean visualizados.
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
import pojos.Personal;

public class FXMLPersonalVisualizarController implements Initializable{
    @FXML
    private Button btSalir;
    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfNumPer;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidos;
    @FXML
    private TextField tfCorreoInstitucional;
    private Personal personalVisualizar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    } 
    
    public void inicializaCampos(Personal personalVisualizar){
        this.personalVisualizar = personalVisualizar;
        cargaVisualizarPersonal();
    }
    
    private void cargaVisualizarPersonal(){
        lbTitulo.setText("Visualizar Personal No: "+personalVisualizar.getIdAcademico());
        tfNumPer.setText(personalVisualizar.getNumeroPersonal());
        tfNombre.setText(personalVisualizar.getNombre());
        tfApellidos.setText(personalVisualizar.getApellidos());
        tfCorreoInstitucional.setText(personalVisualizar.getCorreo());
        
        tfNumPer.setDisable(true);
        tfNombre.setDisable(true);
        tfApellidos.setDisable(true);
        tfCorreoInstitucional.setDisable(true);  
    }

    @FXML
    private void clicSalir(ActionEvent event) {
        Stage stage = (Stage) btSalir.getScene().getWindow();
        stage.close();
    } 
}
