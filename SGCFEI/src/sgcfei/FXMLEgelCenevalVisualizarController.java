/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar la informacion del EGEL-CENEVAL.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de EGEL-CENEVAL para poder recuperar la informacion que se quiere visualizar deL EGEL-CENEVAL.
 * cargaVisualizarCeneval: Hace la cargar de los datos de EGEL-CENEVAL para que sean visualizados.
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
import pojos.Ceneval;


public class FXMLEgelCenevalVisualizarController implements Initializable{

    @FXML
    private Button btSalir;
    @FXML
    private TextField tfEstudiante;
    @FXML
    private TextField tfFechaExamen;
    @FXML
    private TextField tfperiodo;
    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField tfpuntaje;
    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfCarrera;
    private Ceneval cenevalVisualizar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void inicializaCampos(Ceneval cenevalVisualizar){
        this.cenevalVisualizar = cenevalVisualizar;
        cargaVisualizarCeneval();
    }
    
    private void cargaVisualizarCeneval(){
        String puntaje = Float.toString(cenevalVisualizar.getPuntaje());
        tfFechaExamen.setText(cenevalVisualizar.getFechaExamen().toString());
        tfperiodo.setText(cenevalVisualizar.getPeriodo());
        tfpuntaje.setText(puntaje);
        lbTitulo.setText("Visualizar Ceneval No: " + cenevalVisualizar.getIdCeneval());
        tfEstudiante.setText(cenevalVisualizar.getEstudiante());
        tfMatricula.setText(cenevalVisualizar.getMatricula());
        tfCarrera.setText(cenevalVisualizar.getCarrera());
        
        tfCarrera.setDisable(true);
        tfEstudiante.setDisable(true);
        tfFechaExamen.setDisable(true);
        tfMatricula.setDisable(true);
        tfperiodo.setDisable(true);
        tfpuntaje.setDisable(true);
    }
    
    @FXML
    private void clicSalir(ActionEvent event) {
        Stage stage = (Stage) btSalir.getScene().getWindow();
        stage.close();
    }
}