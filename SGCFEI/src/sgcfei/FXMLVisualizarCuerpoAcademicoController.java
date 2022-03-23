/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar la informacion de cuerpo academico.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de cuerpo academico para poder recuperar la informacion que se quiere visualizar de cuerpo academico.
 * cargaInformacionCuerpoAcademicoDB: Hace la cargar de los datos de cuerpo academico para que sean visualizados.
 * clicSalir: Sale de esta pantalla para regresar al consultar.
 */

package sgcfei;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.CuerpoAcademico;



public class FXMLVisualizarCuerpoAcademicoController implements Initializable {
    @FXML
    private TextField lbNombreCuerpoAcademico;
    @FXML
    private TextArea taDescripcionCuerpoAcademico;
    @FXML
    private TextField tfClaveLGAC;
    @FXML
    private TextField tfReposanble;
    @FXML
    private TextField tfNombreLGAC;
    @FXML
    private TextField tfGrado;
    @FXML
    private Label lbTitulo;
    @FXML
    private TextArea taDescripcionLGAC;
    private CuerpoAcademico cuerpoAcademicoVisualizar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void inicializaCampos(CuerpoAcademico cuerpoAcademicoVisualizar){
        this.cuerpoAcademicoVisualizar = cuerpoAcademicoVisualizar;
        cargarInformacionCuerpoAcademicoBD();
    }   
    
    private void cargarInformacionCuerpoAcademicoBD() {
        lbTitulo.setText("Visualizar Cuerpo Academico: " + cuerpoAcademicoVisualizar.getNombre());
        lbNombreCuerpoAcademico.setText(cuerpoAcademicoVisualizar.getNombre());
        taDescripcionCuerpoAcademico.setText(cuerpoAcademicoVisualizar.getDescripcion());
        tfReposanble.setText(cuerpoAcademicoVisualizar.getResponsable());
        tfNombreLGAC.setText(cuerpoAcademicoVisualizar.getNombreLGCA());
        tfClaveLGAC.setText(cuerpoAcademicoVisualizar.getClave());
        tfGrado.setText(cuerpoAcademicoVisualizar.getGrado());
        taDescripcionLGAC.setText(cuerpoAcademicoVisualizar.getLgcaDescripcion());
        
        lbNombreCuerpoAcademico.setDisable(true);
        taDescripcionCuerpoAcademico.setEditable(false);
        tfReposanble.setDisable(true);
        tfNombreLGAC.setDisable(true);
        tfClaveLGAC.setDisable(true);
        tfGrado.setDisable(true);
        taDescripcionLGAC.setEditable(false);
    }
        
    @FXML
    private void clicSalir(ActionEvent event) {
        Stage stage = (Stage) lbTitulo.getScene().getWindow();
        stage.close();
    }
}
