/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar la informacion de estudiante.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de estudiante para poder recuperar la informacion que se quiere visualizar del estudiante.
 * cargaVisualizarEstudiante: Hace la cargar de los datos de estudiante para que sean visualizados.
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
import pojos.Estudiante;


public class FXMLEstudianteVisualizarController implements Initializable{

    @FXML
    private Button btSalir;
    @FXML
    private TextField tfEstudiante;
    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField tfCorreoInstitucional;
    @FXML
    private TextField tfProgramaEducativo;
    
    private Estudiante estudianteVisualizar;
          
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void inicializaCampos(Estudiante estudianteVisualizar){
        this.estudianteVisualizar = estudianteVisualizar;
        cargaVisualizarEstudiante();
    } 
    
    private void cargaVisualizarEstudiante(){
        tfEstudiante.setText(estudianteVisualizar.getNombre());
        tfMatricula.setText(estudianteVisualizar.getMatricula());
        tfCorreoInstitucional.setText(estudianteVisualizar.getCorreo());
        tfProgramaEducativo.setText(estudianteVisualizar.getCarrera());
        
        tfEstudiante.setDisable(true);
        tfMatricula.setDisable(true);
        tfCorreoInstitucional.setDisable(true);
        tfProgramaEducativo.setDisable(true);
    }

    @FXML
    private void clicSalir(ActionEvent event) {
        Stage stage = (Stage) btSalir.getScene().getWindow();
        stage.close();
    }
}
