/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar la informacion de trabajo recepcional.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de trabajo recepcional para poder recuperar la informacion que se quiere visualizar de trabajo recepcional.
 * cargaVisualizarTrabajo Recepcional: Hace la cargar de los datos de trabajo recepcional para que sean visualizados.
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
import pojos.TrabajoRecepcional;


public class FXMLTrabajoRecepcionalVisualizarController implements Initializable{

    @FXML
    private Button btSalir;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfFecha;
    @FXML
    private TextField tfEstudiante;
    @FXML
    private TextField tfDirector;
    @FXML
    private TextField tfCodirector;
    @FXML
    private TextField tfSinodal;
    @FXML
    private TextArea taDescripcion;

    private TrabajoRecepcional trabajoRecepcionalVisualizar;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    } 
    
    public void inicializaCampos(TrabajoRecepcional trabajoRecepcionalVisualizar){
        this.trabajoRecepcionalVisualizar = trabajoRecepcionalVisualizar;
        cargaVisualizarTrabajoRecepcional();
    }
    
    private void cargaVisualizarTrabajoRecepcional(){
        tfNombre.setText(trabajoRecepcionalVisualizar.getNombre());
        tfFecha.setText(trabajoRecepcionalVisualizar.getFechaRegistro().toString());
        tfEstudiante.setText(trabajoRecepcionalVisualizar.getEstudiante());
        tfDirector.setText(trabajoRecepcionalVisualizar.getDirector());
        tfCodirector.setText(trabajoRecepcionalVisualizar.getCoDirector());
        tfSinodal.setText(trabajoRecepcionalVisualizar.getSinodal());
        taDescripcion.setText(trabajoRecepcionalVisualizar.getDescripcion());
    }

    @FXML
    private void clicSalir(ActionEvent event) {
        Stage stage = (Stage) btSalir.getScene().getWindow();
        stage.close();
    }  
}
