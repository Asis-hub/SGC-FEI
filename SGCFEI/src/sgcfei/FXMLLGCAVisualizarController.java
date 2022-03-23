/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de mostrar la informacion del LGCA.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize: Método que permite la carga de los componentes principales en pantalla.
 * inicializaCampos: Crea el objeto de LGCA para poder recuperar la informacion que se quiere visualizar de la LGCA.
 * cargaInformacionLGCA: Hace la cargar de los datos de academia para que sean visualizados.
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
import pojos.LGCA;

public class FXMLLGCAVisualizarController implements Initializable {
    @FXML
    private TextField tfClave;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfGrado;
    @FXML
    private TextField tfResponsable;
    @FXML
    private TextArea tfDescripcion;
    @FXML
    private TextField tfCorreo;
    @FXML
    private Label lbTitulo;
    
    private LGCA lgcaVisualizar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {   
    }    
    
    public void inicializaCampos(LGCA lgcaVisualizar){
        this.lgcaVisualizar = lgcaVisualizar;
        cargarInformacionLGCABD();
    }
    
    private void cargarInformacionLGCABD(){
        lbTitulo.setText("Visualizar LGAC: " + lgcaVisualizar.getIdLGCA());
        tfClave.setText(lgcaVisualizar.getClave());
        tfNombre.setText(lgcaVisualizar.getLGCA());
        tfGrado.setText(lgcaVisualizar.getGrado());
        tfResponsable.setText(lgcaVisualizar.getResponsable());
        tfDescripcion.setText(lgcaVisualizar.getDesAdscripcion());
        tfCorreo.setText(lgcaVisualizar.getCorreo());
        
        tfClave.setDisable(true);
        tfNombre.setDisable(true);
        tfGrado.setDisable(true);
        tfResponsable.setDisable(true);
        tfDescripcion.setEditable(false);
        tfCorreo.setDisable(true);
    }

    @FXML
    private void clicSalir(ActionEvent event) {    
        Stage stage = (Stage) tfClave.getScene().getWindow();
        stage.close();
    }
}
