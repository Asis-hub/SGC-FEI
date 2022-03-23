/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar la informacion de experiencia educativa.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de experiencia educativa para poder recuperar la informacion que se quiere visualizar de la Experiencia educativa.
 * cargaVisualizarEE: Hace la cargar de los datos de experiencia educativa para que sean visualizados.
 * clicSalir: Sale de esta pantalla para regresar al consultar.
 */

package sgcfei;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.ExperienciaEducativa;


public class FXMLEEVisualizarController implements Initializable{

    @FXML
    private Button btSalir;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfNRC;
    @FXML
    private TextField tfMateria;
    @FXML
    private TextField tfAcademia;
    @FXML
    private TextField tfPrerequisito;
    @FXML
    private TextField tfCorequisito;
    private ExperienciaEducativa experienciaVisualizar;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    } 
    
    public void inicializaCampos(ExperienciaEducativa experieciaVisualizar){
        this.experienciaVisualizar = experieciaVisualizar;
        cargaVisualizarEE();
    }
    
    private void cargaVisualizarEE(){
        tfNombre.setText(experienciaVisualizar.getNombre());
        tfNRC.setText(experienciaVisualizar.getNrc());
        tfMateria.setText(experienciaVisualizar.getMateria());
        tfAcademia.setText(experienciaVisualizar.getAcademia());
        tfPrerequisito.setText(experienciaVisualizar.getPrerequisito());
        tfCorequisito.setText(experienciaVisualizar.getCorequisito());
        
        tfNombre.setDisable(true);
        tfNRC.setDisable(true);
        tfMateria.setDisable(true);
        tfAcademia.setDisable(true);
        tfPrerequisito.setDisable(true);
        tfCorequisito.setDisable(true);
    }
    
    @FXML
    private void clicSalir(ActionEvent event) {
        Stage stage = (Stage) btSalir.getScene().getWindow();
        stage.close();
    } 
}
