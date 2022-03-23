/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar la informacion de la academia.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de academia para poder recuperar la informacion que se quiere visualizar de la academia.
 * cargaVisualizarAcademia: Hace la cargar de los datos de academia para que sean visualizados.
 * clicSalir: Sale de esta pantalla para regresar al consultar.
 */

package sgcfei;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Academia;

public class FXMLAcademiaVisualizarController implements Initializable{
    @FXML
    private Button btSalir;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private TextField tfNombreAcademia;
    @FXML
    private TextField tfCoordinador;

    private Academia academiaVisualizar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }  
    
    public void inicializaCampos(Academia academiaVisualizar){
        this.academiaVisualizar = academiaVisualizar;
        cargaVisualizarAcademia();
    }
    
    private void cargaVisualizarAcademia(){
        tfNombreAcademia.setText(academiaVisualizar.getNombre());
        taDescripcion.setText(academiaVisualizar.getDescripcion());
        tfCoordinador.setText(academiaVisualizar.getCoordinador());
        
        tfNombreAcademia.setDisable(true);
        taDescripcion.setEditable(false);
        tfCoordinador.setDisable(true);
    }

    @FXML
    private void clicSalir(ActionEvent event) {
        Stage stage = (Stage) btSalir.getScene().getWindow();
        stage.close();
    }  
}
